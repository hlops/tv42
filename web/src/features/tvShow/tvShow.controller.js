'use strict';

import CommonPageController from '../../common/common.page.controller';

const name = 'tvShow';

export default class TvShowController extends CommonPageController {
  constructor($scope, tvService) {
    super($scope, name);
    this.tvService = tvService;

    this.model = {
      channels: []
    };

    this.init($scope);
  }

  init($scope) {
    this.readTvShow();
  }

  readTvShow() {
    var vm = this;
    this.tvService.getTvShow().then(
        function(res) {
          vm.model.channels = res.data;
        });
  }
}

TvShowController.$inject = ['$scope', 'tvService'];
