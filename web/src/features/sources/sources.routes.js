'use strict';

export default function routes($routeProvider) {
  $routeProvider
      .when('/sources', {
        template: require('./sources.html'),
        reloadOnSearch: false
      })
}

routes.$inject = ['$routeProvider'];