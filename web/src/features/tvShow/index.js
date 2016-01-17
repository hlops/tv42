'use strict';

import ngRoute from 'angular-route';

import routing from './tvShow.routes.js';
import tvShowController from './tvShow.controller';

import tvService from '../../services/tv.service';

export default angular.module('app.tvShow', [ngRoute, tvService])
    .config(routing, ['$routeProvider'])
    .controller('tvShowController', tvShowController)
    .name;