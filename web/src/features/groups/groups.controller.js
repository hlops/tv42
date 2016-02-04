'use strict';

import CommonPageController from '../../common/common.page.controller';

const name = 'groups';

export default class GroupsController extends CommonPageController {
  constructor($scope) {
    super($scope, name);

    this.model = {
    };

    this.init($scope);
  }

  init($scope) {
  }

}

GroupsController.$inject = ['$scope'];
