'use strict';

export default function routes($routeProvider) {
  $routeProvider
      .when('/channels', {
        template: require('./channels.html'),
        controller: 'channelsController',
        controllerAs: 'channelsCtrl',
        reloadOnSearch: false
      })
}

routes.$inject = ['$routeProvider'];