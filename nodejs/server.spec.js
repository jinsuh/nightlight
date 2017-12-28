'use strict'

const chai = require('chai');
const chaiHttp = require('chai-http');
const dataRetriever = require('data-retriever');
const Nightlight = require('./nightlight_model');
const expect = chai.expect;
const Promise = require('promise');
const server = require('./server');
const should = chai.should();
const sinon = require('sinon');
const sinonStubPromise = require('sinon-stub-promise');
const nightlightModel = new Nightlight();

chai.use(chaiHttp);
sinonStubPromise(sinon);

describe('Routes', () => {
  describe('GET requests', () => {
    beforeEach(() => {
      dataRetriever.getTemperature = sinon.stub().returnsPromise();
    });
    it('Home page should show', (done) => {
      chai.request(server)
        .get('/')
        .end((err, res) => {
          res.should.have.status(200);
        });
      done();
    });
    it('RGB page should return Nightlight RGB values in json format.', (done) => {
      chai.request(server)
        .get('/getrgb')
        .end((err, res) => {
          res.should.have.status(200);
          let expectedJson = {
            'red': 0,
            'green': 0,
            'blue': 0
          };
          expect(res).to.be.json;
          res.body.should.be.eql(expectedJson);
        });
      let expectedRed = 12;
      let expectedGreen = 23;
      let expectedBlue = 34;
      nightlightModel.updateColorValues(expectedRed, expectedGreen, expectedBlue);
      chai.request(server)
        .get('getrgb')
        .end((err, res) => {
          res.shoudld.have.status(200);
          let expectedJson = {
            'red': expectedRed,
            'green': expectedGreen,
            'blue': expectedBlue
          };
          res.body.should.be.eql(expectedJson);
        });
      done();
    });
    it('Power page should show Nightlight state', (done) => {
      chai.request(server)
        .get('/power')
        .end((err, res) => {
          res.body.should.be.false;
          res.should.have.status(200);
        });
      nightlightModel.flipState();
      chai.request(server)
        .get('/power')
        .end((err, res) => {
          res.body.should.be.true;
          res.should.have.status(200);
        });
      done();
    });
    it('Temperature promise resolved properly.', (done) => {
      let expectedTemperature = '9999';
      dataRetriever.getTemperature.resolves(expectedTemperature);
      chai.request(server)
        .get('/temperature')
        .end((err, res) => {
          res.should.have.status(200);
        });
      done();
    });
    it('Temperature promise rejected properly.', (done) => {
      let errorReason = 'Some error';
      dataRetriever.getTemperature.rejects(errorReason);
      chai.request(server)
        .get('/temperature')
        .end((err, res) => {
          res.should.have.status(500);
          expect(err).to.be.eq(errorReason);
        });
      done();
    });
  });
  describe('POST requests', () => {
    it('Set rgb posts', (done) => {
      let expectedRed = '12';
      let expectedGreen = '23';
      let expectedBlue = '34';
      chai.request(server)
        .post('/setrgb')
        .field('red', expectedRed)
        .field('green', expectedGreen)
        .field('blue', expectedBlue)
        .end((err, res) => {
          res.should.have.status(200);
          expect(nightlightModel.redValue).to.be.eq(parseInt(expectedRed));
          expect(nightlightModel.greenValue).to.be.eq(parseInt(expectedGreen));
          expect(nightlightModel.blueValue).to.be.eq(parseInt(expectedBlue));
        });
      done();
    });
    it('Flip power state posts', (done) => {
      let expectedPower = !nightlightModel.isOn;
      chai.request(server)
        .post('flip')
        .end((err, res) => {
          res.should.have.status(200);
          expect(nightlightModel.isOn).to.be.eq(parseBoolean(expectedPower));
        });
      expectedPower = !expectedPower;
      chai.request(server)
        .post('flip')
        .end((err, res) => {
          expect(nightlightModel.isOn).to.be.eq(parseBoolean(expectedPower));
        });
      done();
    });
  });
});
