'use strict';

export default class CommonPageController {
  constructor($scope, currentItem) {
    $scope.appCtrl.model.currentItem = currentItem;
  }
}
