'use strict';

import ngRoute from 'angular-route';

import routing from './tvShow.routes.js';
import tvShowController from './tvShow.controller';

export default angular.module('app.tvShow', [ngRoute])
    .config(routing, ['$routeProvider'])
    .controller('tvShowController', tvShowController)
    .name;
