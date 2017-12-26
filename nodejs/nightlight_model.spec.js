'use strict'

const NightlightModel = require('./nightlight_model');
const expect = require('chai').expect;

const testModel = new NightlightModel();
describe('Nightlight model', () => {
  it('should have proper initial values', () => {
    expect(testModel.isOn).to.be.false;
    expect(testModel.redValue).to.be.eq(0);
    expect(testModel.greenValue).to.be.eq(0);
    expect(testModel.blueValue).to.be.eq(0);
  });
  it('should flip state properly', () => {
    testModel.flipState();
    expect(testModel.isOn).to.be.true;
    testModel.flipState();
    expect(testModel.isOn).to.be.false;
  });
  it('should set the right color values', () => {
    let expectedRed = 21;
    let expectedGreen = 26;
    let expectedBlue = 623;
    testModel.updateColorValues(expectedRed, expectedGreen, expectedBlue);
    expect(testModel.redValue).to.be.eq(expectedRed);
    expect(testModel.greenValue).to.be.eq(expectedGreen);
    expect(testModel.blueValue).to.be.eq(expectedBlue);
  });
});
