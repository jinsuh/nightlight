'use strict'
const Promise = require('promise');
const weather = require('weather-js');

module.exports = {
  getTemperature: () => {
    return findTemperature();
  }
};

function findTemperature() {
  return new Promise((resolve, reject) => {
    console.log('Finding temperature');
    weather.find({search: 'San Francisco, CA', degreeType: 'F'}, (err, res) => {
      if (err) {
        console.log(err);
        reject(err);
      } else {
        let jsonResponse = JSON.parse(JSON.stringify(res));
        jsonResponse = jsonResponse[0]['current']['temperature'];
        console.log(jsonResponse);
        resolve(jsonResponse);
      }
    });
  });
}
