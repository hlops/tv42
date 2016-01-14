'use strict';

import ngRoute from 'angular-route';

import routing from './tvGuide.routes.js';
import tvGuideController from './tvGuide.controller';

export default angular.module('app.tvGuide', [ngRoute])
    .config(routing, ['$routeProvider'])
    .controller('tvGuideController', tvGuideController)
    .name;