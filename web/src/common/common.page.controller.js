'use strict';

export default class CommonPageController {
  constructor($scope, currentMenuItem) {
    $scope.appCtrl.pageLoaded();
    this.$scope = $scope;
    this.model = this.createModel();
    $scope.appCtrl.model = this.model;
    this.model.currentMenuItem = currentMenuItem;
  }

  createModel() {
    return {};
  }

}

CommonPageController.$inject = ['$scope', 'currentMenuItem'];
