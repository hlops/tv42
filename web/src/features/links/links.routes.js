'use strict';

export default function routes($routeProvider) {
  $routeProvider
      .when('/links', {
        template: require('./links.html'),
        controller: 'linksController',
        controllerAs: 'linksCtrl',
        reloadOnSearch: false
      })
}

routes.$inject = ['$routeProvider'];
