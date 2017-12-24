'use strict'

const dataRetriever = require('data-retriever');
const expect = require('chai').expect;
const sinon = require('sinon');
const weather = require('weather-js');


describe('data-retriever module', () => {
  beforeEach(function () {
    weather.find = sinon.stub();
  });

  afterEach(function () {
    weather.find.reset();
  });

  describe('Get temperature', () => {
    it('should export a function', () => {
      expect(dataRetriever.getTemperature).to.be.a('function');
    });
    it('Check valid promise.', () => {
      let expectedTemperature = 30;
      let mockJSONResponse = [{'current': {'temperature': expectedTemperature}}];
      weather.find.callsArgWith(1, null, mockJSONResponse);
      const getTemperatureResult = dataRetriever.getTemperature();
      return Promise.resolve(getTemperatureResult)
        .then(data => {
          expect(data).to.be.eq(expectedTemperature);
        }).catch(error => {
          expect(error).to.be.undefined;
        });
    });
    it('Check invalid promise handled properly.', () => {
      let expectedError = 'ERROR';
      weather.find.callsArgWith(1, 'ERROR', {});
      return Promise.resolve(dataRetriever.getTemperature())
        .then(data => {
          throw Error('Expected error to be raised in promise.');
        }).catch(error => {
          expect(error).to.be.eq(expectedError);
        });
    });
  });
});
