'use strict';

class menuService {
  constructor() {
    this.current = undefined;
  }
}

menuService.$inject = [];

export default angular.module('services.menu', [])
    .service('menuService', menuService)
    .name;
