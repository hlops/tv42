'use strict';

import angular from 'angular';
import angularMaterial from 'angular-material';
import ngRoute from 'angular-route';

import 'angular-material/angular-material.css';
import './app.css';

import config from './app.config';

import sources from './features/sources/index';
import tvShow from './features/tvShow/index';
import channels from './features/channels/index';

import AppController from './app.controller';

angular.module('app',
    [sources, tvShow, channels, 'ngMaterial'])
    .controller('AppController', AppController)
    .config(config)
;
