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

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(express.static(path.join(__dirname, 'public')));

const isOnline = true;

app.use('/api/inventory/:itemId', (request, response) => {
  const itemId = request.params.itemId;

  return response.send({
    itemId: itemId,
    location: 'Idaho',
    quantity: 337,
    link: 'http://google.com/idaho'
  });
});

const probe = require('kube-probe');

const options = {
  livenessCallback: (request, response) => {
    console.log('livenessCallback called, we are " ' + (isOnline ? 'ONLINE' : 'OFFLINE'));
    return isOnline ? response.send('OK') : response.sendStatus(500);
  }
};

probe(app, options);

module.exports = app;
