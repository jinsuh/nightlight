'use strict'

const CronJob = require('cron').CronJob;
const dataRetriever = require('data-retriever');

module.exports = {
  setRGB: function(r, g, b) {
    changeRGB(r, g, b);
  },
  createWeatherCronJob: function() {
    scheduleWeatherCronJob();
  }
};

let cronJob = null;

function changeRGB(r, g, b) {
  console.log('Retrieved command to set rgb: %s, %s, %s', r, g, b);
}

function scheduleWeatherCronJob() {
  cronJob = new CronJob({
    cronTime: '* * * * *',
    onTick: () => {
      console.log('running a task every minute');
      dataRetriever.getTemperature().then(val => {
        console.log('temperature: %d', val);
      }).catch(error => {
        console.log('Temperature was not found due to reason: %s', error);
      });
    },
    start: true,
    runOnInit: true
  });
}

function stopCronJob() {
  if (cronJob != null) {
    cronJob.stop();
  }
  cronJob = null;
}
