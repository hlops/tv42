'use strict';

import CommonPageController from '../../common/common.page.controller';

const name = 'groups';

export default class GroupsController extends CommonPageController {
  constructor($scope, tvService) {
    super($scope, name);
    this.tvService = tvService;

    this.model = {
      groups: []
    };

    this.init($scope);
  }

  init($scope) {
    this.readGroups();
  }

  readGroups() {
    var vm = this;
    this.tvService.getGroups().then(
        function(res) {
          vm.model.groups = res.data;
        });
  }

}

GroupsController.$inject = ['$scope', 'tvService'];
