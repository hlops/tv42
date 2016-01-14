'use strict';

import ngRoute from 'angular-route';

import routing from './home.routes';
import HomeController from './home.controller';

export default angular.module('app.home', [ngRoute, 'ngMaterial'])
    .config(routing, ['$routeProvider'])
    .controller('HomeController', HomeController)
    .name;