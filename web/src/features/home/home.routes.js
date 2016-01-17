'use strict';

export default function routes($routeProvider) {
  $routeProvider
      .when('/', {
        template: require('./home.html'),
        controller: 'HomeController',
        controllerAs: 'homeCtrl',
        reloadOnSearch: false
      })
}

routes.$inject = ['$routeProvider'];