'use strict';

class tvService {
  constructor($http) {
    this.$http = $http;
  }

  getSources() {
    return this.$http.get('/rest/sources');
  }
}

tvService.$inject = ['$http'];

export default angular.module('services.tv', [])
    .service('tvService', tvService)
    .name;
