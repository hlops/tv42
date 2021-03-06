'use strict';

export default class AppController {
  constructor($scope, $mdSidenav, tvService) {
    this.$mdSidenav = $mdSidenav;
    this.tvService = tvService;

    this.init();
  }

  init() {

  }

  isActiveMenuItem(item) {
    return this.model && this.model.currentMenuItem === item;
  }

  toggleSidenav(id) {
    this.$mdSidenav(id).toggle();
  }

  pageLoaded() {
    this.tvService.getGroups();
  }
}

AppController.$inject = ['$scope', '$mdSidenav', 'tvService'];
