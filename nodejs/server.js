'use strict'
const bodyParser = require('body-parser');
const dataRetriever = require('data-retriever');
const express = require('express');
const lighter = require('lighter');

const hostname = '127.0.0.1';
const port = process.env.PORT || 8080;
const app = express();

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.get('/', (req, res) => {
  res.send('Hello world!'); 
});

app.post('/setrgb', function(req, res) {
  let r = req.body.red;
  let g = req.body.green;
  let b = req.body.blue;
  res.send(r + ' ' + b + ' ' + g);
});

app.get('/temperature', (req, res) => {
  dataRetriever.getTemperature().then(val => {
    console.log('Temperature is ' + val);
    res.setHeader('Content-Type', 'text/plain');
    res.end(val);
  }).catch(error => {
    console.log('Temperature was not found due to reason: %s', error);
    res.status(500, {
      error: error
    });
  });
});

app.listen(port, () => {
  console.log('RESTful API server started on: ' + port);
});

module.exports = app; // for testing
