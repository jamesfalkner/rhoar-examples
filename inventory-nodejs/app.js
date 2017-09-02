'use strict';

/*
 *
 *  Copyright 2016-2017 Red Hat, Inc, and individual contributors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

const express = require('express');
const path = require('path');
const bodyParser = require('body-parser');

const app = express();

const probe = require('kube-probe');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(express.static(path.join(__dirname, 'public')));

let isOnline = true;

//
app.use('/api/greeting', (request, response) => {
  if (!isOnline) {
    return response.sendStatus(400);
  }

  const name = request.query ? request.query.name : undefined;
  return response.send({content: `Hello, ${name || 'World'}`});
});

app.use('/api/inventory/:itemId', (request, response) => {
  var itemId = request.params.itemId;

  return response.send({
    itemId: itemId,
    location: 'Idaho',
    quentity: Math.floor(Math.random() * 1000),
    link: 'http://google.com/idaho'
  });
});
app.use('/api/killme', (request, response) => {
  isOnline = false;
  return response.send('Stopping HTTP server, Bye bye world !');
});

const options = {
  livenessCallback: (request, response) => {
    console.log('livenessCallback called, we are " ' + (isOnline ? 'ONLINE' : 'OFFLINE'));
    return isOnline ? response.send('OK') : response.sendStatus(500);
  }
};

probe(app, options);

module.exports = app;
