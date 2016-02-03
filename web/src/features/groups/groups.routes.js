'use strict';

export default function routes($routeProvider) {
  $routeProvider
      .when('/groups', {
        template: require('./groups.html'),
        controller: 'groupsController',
        controllerAs: 'groupsCtrl',
        reloadOnSearch: false
      })
}

routes.$inject = ['$routeProvider'];
