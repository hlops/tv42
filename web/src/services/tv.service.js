'use strict';

class tvService {
  constructor($http) {
    this.$http = $http;
  }

  getSources() {
    return this.$http.get('/rest/sources');
  }

  getSourceTypes() {
    return this.$http.get('/rest/sources/types');
  }

  saveSource(source) {
    return this.$http.post('/rest/sources', source);
  }
}

tvService.$inject = ['$http'];

export default angular.module('services.tv', [])
    .service('tvService', tvService)
    .name;
