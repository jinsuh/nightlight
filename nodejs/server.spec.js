'use strict'

const chai = require('chai');
const chaiHttp = require('chai-http');
const dataRetriever = require('data-retriever');
const expect = chai.expect;
const Promise = require('promise');
const server = require('./server');
const should = chai.should();
const sinon = require('sinon');
const sinonStubPromise = require('sinon-stub-promise');

chai.use(chaiHttp);
sinonStubPromise(sinon);

describe('Routes', () => {
  describe('/ GET', () => {
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
});
