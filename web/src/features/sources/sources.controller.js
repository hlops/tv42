'use strict';

import CommonPageController from '../../common/common.page.controller';

const name = 'sources';

export default class SourcesController extends CommonPageController {
  constructor($scope, tvService) {
    super($scope, name);
    var vm = this;

    tvService.getSources().then(
        function(res) {
          vm.sources = res.data;
        })
  }
}

SourcesController.$inject = ['$scope', 'tvService'];
