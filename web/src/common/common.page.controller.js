'use strict';

export default class CommonPageController {
  constructor($scope, currentMenuItem) {
    $scope.appCtrl.model.currentMenuItem = currentMenuItem;
    this.$scope = $scope;
  }

  getGroups() {
    return this.$scope.appCtrl.model.groups;
  }

}
