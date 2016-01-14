'use strict';

import angular from 'angular';
import angularMaterial from 'angular-material';
import ngRoute from 'angular-route';

import 'angular-material/angular-material.css';
import 'font-awesome/css/font-awesome.css';
import './app.css';

import config from './app.config';

import tvGuide from './features/tvGuide/index';
import channels from './features/channels/index';

angular.module('app', [
    tvGuide, channels,
    'ngMaterial'])
    .config(config);
