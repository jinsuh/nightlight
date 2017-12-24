'use strict'

const lighter = require('lighter');
const expect = require('chai').expect;

describe('lighter module', () => {
  describe('"Exporting functions"', () => {
    it('should export a function', () => {
      expect(lighter.setRGB).to.be.a('function');
    });
  });
});
