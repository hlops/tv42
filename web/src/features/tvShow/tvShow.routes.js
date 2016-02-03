'use strict';

export default function routes($routeProvider) {
  $routeProvider
      .when('/tvShow', {
        template: require('./tvShow.html'),
        controller: 'tvShowController',
        controllerAs: 'tvShowCtrl',
        reloadOnSearch: false
      })
}

routes.$inject = ['$routeProvider'];
