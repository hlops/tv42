'use strict';

import ngRoute from 'angular-route';

import routing from './links.routes.js';
import linksController from './links.controller';

export default angular.module('app.links', [ngRoute])
    .config(routing, ['$routeProvider'])
    .controller('linksController', linksController)
    .name;
