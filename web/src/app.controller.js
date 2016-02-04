'use strict';

export default class AppController {
  constructor($scope, $mdSidenav, tvService) {
    this.model = {
      currentMenuItem: undefined,
      groups: []
    };
    this.$mdSidenav = $mdSidenav;
    this.tvService = tvService;

    this.readGroups();
  }

  readGroups() {
    var vm = this;
    this.tvService.getGroups().then(
        function(res) {
          vm.model.groups = res.data;
        });
  }

  isActiveMenuItem(item) {
    return this.model.currentMenuItem === item;
  }

  toggleSidenav(id) {
    this.$mdSidenav(id).toggle();

  }
}

AppController.$inject = ['$scope', '$mdSidenav', 'tvService'];
