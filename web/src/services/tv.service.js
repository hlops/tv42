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

  executeSource(sourceId) {
    return this.$http.put('/rest/sources/action', JSON.stringify(sourceId));
  }

  getChannels() {
    return this.$http.get('/rest/channels');
  }

  getLinks() {
    return this.$http.get('/rest/links');
  }

  getGroups() {
    return this.$http.get('/rest/groups');
  }

  getTvShow() {
    return this.$http.get('/rest/tvShow');
  }
}

tvService.$inject = ['$http'];

export default angular.module('services.tv', [])
    .service('tvService', tvService)
    .name;
