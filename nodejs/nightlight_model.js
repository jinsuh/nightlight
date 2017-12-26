'use strict'

let Nightlight = function() {
  this.isOn = false;
  this.redValue = 0;
  this.greenValue = 0;
  this.blueValue = 0;
}

Nightlight.prototype.flipState = function() {
  this.isOn = !this.isOn;
}

Nightlight.prototype.updateColorValues = function(red, green, blue) {
  this.redValue = red;
  this.greenValue = green;
  this.blueValue = blue;
}

module.exports = Nightlight;
