'use strict';

config.$inject = ['$locationProvider', '$routeProvider', '$mdThemingProvider'];

export default function config($locationProvider, $routeProvider, $mdThemingProvider) {
  $locationProvider.html5Mode(true);
  $routeProvider.otherwise({redirectTo: '/tvShow'});

  $mdThemingProvider.theme('default')
      .primaryPalette('light-blue')
      .accentPalette('blue');

}