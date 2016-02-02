'use strict';

import ngRoute from 'angular-route';

import routing from './groups.routes.js';
import groupsController from './groups.controller';

export default angular.module('app.groups', [ngRoute])
    .config(routing, ['$routeProvider'])
    .controller('groupsController', groupsController)
    .name;
