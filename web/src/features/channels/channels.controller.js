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
    this.tvService.getChannels().then(
        function(res) {
          vm.model.channels = res.data;
        });
  }

}

ChannelsController.$inject = ['$scope', 'tvService'];
