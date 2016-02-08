'use strict';

export default class CommonPageController {
  constructor($scope, currentMenuItem) {
    $scope.appCtrl.model.currentMenuItem = currentMenuItem;
    $scope.appCtrl.pageLoaded();
    this.$scope = $scope;
  }
}

CommonPageController.$inject = ['$scope', 'currentMenuItem'];
