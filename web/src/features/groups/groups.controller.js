'use strict';

import CommonPageController from '../../common/common.page.controller';

const name = 'groups';

export default class GroupsController extends CommonPageController {
  constructor($scope, tvService) {
    super($scope, name, {});
    this.tvService = tvService;

    this.init($scope);
  }

  createModel() {
    return {
      caption: 'Группы'
    };
  }

  init($scope) {
  }

  getGroups() {
    return this.tvService.model.groups;
  }
}

GroupsController.$inject = ['$scope', 'tvService'];
