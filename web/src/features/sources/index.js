'use strict';

import ngRoute from 'angular-route';

import routing from './sources.routes.js';
import sourcesController from './sources.controller';

export default angular.module('app.sources', [ngRoute])
    .config(routing, ['$routeProvider'])
    .controller('sourcesController', sourcesController)
    .name;
