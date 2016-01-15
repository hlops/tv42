'use strict';

import ngRoute from 'angular-route';

import routing from './tvGuide.routes.js';
import tvGuideController from './tvGuide.controller';

import tvService from '../../services/tv.service';

export default angular.module('app.tvGuide', [ngRoute, tvService])
	.config(routing, ['$routeProvider'])
	.controller('tvGuideController', tvGuideController)
	.name;