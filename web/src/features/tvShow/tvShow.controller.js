'use strict';

import CommonPageController from '../../common/common.page.controller';

const name = 'tvShow';

export default class TvShowController extends CommonPageController {
  constructor($scope, tvService) {
    super($scope, name);
    this.tvService = tvService;

    this.init($scope);
  }

  createModel() {
    return {
      caption: 'Программа передач',
      channels: []
    };
  }

  init($scope) {
    this.readTvShow();
  }

  readTvShow() {
    var vm = this;
    var groups;
    vm.tvService.getGroups().then(function(res) {
      groups = res.data;
      return vm.tvService.getTvShow();
    }).then(
        function(res) {
          vm.model.channels = res.data.sort(function(c1, c2) {
            return groups.indexOf(c1.group) - groups.indexOf(c2.group) ||
                c1.name.localeCompare(c2.name);
          });
        });
  }
}

TvShowController.$inject = ['$scope', 'tvService'];
