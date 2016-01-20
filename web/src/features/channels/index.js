'use strict';

import ngRoute from 'angular-route';

import routing from './channels.routes.js';
import channelsController from './channels.controller';

export default angular.module('app.channels', [ngRoute])
    .config(routing, ['$routeProvider'])
    .controller('channelsController', channelsController)
    .name;
