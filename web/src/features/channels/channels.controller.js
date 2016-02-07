'use strict';

import CommonPageController from '../../common/common.page.controller';

const name = 'channels';

export default class ChannelsController extends CommonPageController {
  constructor($scope, tvService) {
    super($scope, name);
    this.tvService = tvService;

    this.model = {
      channels: []
    };

    this.init($scope);
  }

  init($scope) {
    this.readChannels();
    var vm = this;
  }

  readChannels() {
    var vm = this;
    var groups;
    vm.tvService.getGroups().then(function(res) {
      groups = res.data;
      return vm.tvService.getChannels();
    }).then(
        function(res) {
          vm.model.channels = res.data.sort(function(c1, c2) {
            return groups.indexOf(c1.group) - groups.indexOf(c2.group) ||
                c1.name.localeCompare(c2.name);
          });
        });
  }

}

ChannelsController.$inject = ['$scope', 'tvService'];
