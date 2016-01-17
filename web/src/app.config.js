'use strict';

config.$inject = ['$locationProvider', '$routeProvider'];

export default function config($locationProvider, $routeProvider) {
  $locationProvider.html5Mode(true);
  $routeProvider.otherwise({redirectTo: '/tvShow'});
}