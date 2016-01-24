'use strict';

var $mdSidenav;
export default class AppController {
  constructor($scope, $mdSidenav) {
    this.model = {
      currentItem: undefined
    }
    this.$mdSidenav = $mdSidenav;
  }

  isActiveMenuItem(item) {
    return this.model.currentItem === item;
  }

  toggleSidenav(id) {
    this.$mdSidenav(id).toggle();

  }
}

AppController.$inject = ['$scope', '$mdSidenav'];