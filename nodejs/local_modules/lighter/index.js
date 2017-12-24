'use strict'

module.exports = {
  setRGB: function(r, g, b) {
    changeRGB(r, g, b);
  }
};

function changeRGB(r, g, b) {
  console.log('Retrieved command to set rgb: %s, %s, %s', r, g, b);
}
