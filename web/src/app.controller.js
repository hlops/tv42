'use strict';

export default class AppController {
  constructor($scope) {
    this.model = {
      currentItem: undefined
    }
  }

  isActiveMenuItem(item) {
    return this.model.currentItem === item;
  }
}

AppController.$inject = ['$scope'];
