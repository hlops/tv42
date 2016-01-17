'use strict';

class tvService {
  constructor($http) {
    this.$http = $http;
  }

  test() {
    this.$http.get('/rest/test').then(function (q) {
      console.log(q)
    });
  }
}

tvService.$inject = ['$http'];

export default angular.module('services.tv', [])
    .service('tvService', tvService)
    .name;