'use strict';

import angular from 'angular';
import angularMaterial from 'angular-material';
import ngRoute from 'angular-route';

import 'angular-material/angular-material.css';
import './app.css';

import tvMoveToParent from './directives/tvMoveToParent'
import config from './app.config';

import './services/tv.service';

import sources from './features/sources/index';
import tvShow from './features/tvShow/index';
import channels from './features/channels/index';
import groups from './features/groups/index';
import links from './features/links/index';

import AppController from './app.controller';

angular.module('app',
    [sources, tvShow, channels, groups, links, 'ngMaterial', 'services.tv'])
    .controller('AppController', AppController)
    .directive('tvMoveToParent', tvMoveToParent)
    .config(config)
;
