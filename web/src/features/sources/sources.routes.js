'use strict';

export default function routes($routeProvider) {
  $routeProvider
      .when('/sources', {
        template: require('./sources.html'),
        controller: 'sourcesController',
        controllerAs: 'sourcesCtrl',
        reloadOnSearch: false
      })
}

routes.$inject = ['$routeProvider'];