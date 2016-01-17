'use strict';

export default function routes($routeProvider) {
  $routeProvider
      .when('/tvShow', {
        template: require('./tvShow.html'),
        reloadOnSearch: false
      })
}

routes.$inject = ['$routeProvider'];