'use strict';

import CommonPageController from '../../common/common.page.controller';

const name = 'links';

export default class LinksController extends CommonPageController {
  constructor($scope, tvService) {
    super($scope, name);
    this.tvService = tvService;

    this.model = {
      links: []
    };

    this.init($scope);
  }

  init($scope) {
    this.readLinks();
    var vm = this;
  }

  readLinks() {
    var vm = this;
    this.tvService.getLinks().then(
        function(res) {
          vm.model.links = res.data;
        });
  }

}

LinksController.$inject = ['$scope', 'tvService'];
