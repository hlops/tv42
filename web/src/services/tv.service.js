'use strict';

var groupPromise;
const MAX_GROUPS_AGE = 5000;

class tvService {
  constructor($http) {
    this.$http = $http;
    this.model = {
      groups: []
    }
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

  getChannelNames() {
    return this.$http.get('/rest/channels/names');
  }

  getLinks() {
    return this.$http.get('/rest/links');
  }

  saveLink(link) {
    return this.$http.post('/rest/links', link);
  }

  getLinksChannels() {
    return this.$http.get('/rest/links/channels');
  }

  getGroups() {
    var vm = this;
    var now = new Date().getTime();
    if (groupPromise && now - groupPromise.age < MAX_GROUPS_AGE) {
      return groupPromise;
    }
    groupPromise = this.$http.get('/rest/groups');
    groupPromise.then(function(res) {
      vm.model.groups = res.data;
    });
    groupPromise.age = now;
    return groupPromise;
  }

  getTvShow() {
    return this.$http.get('/rest/tvShow');
  }
}

tvService.$inject = ['$http'];

export default angular.module('services.tv', [])
    .service('tvService', tvService)
    .name;
