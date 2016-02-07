'use strict';

import CommonPageController from '../../common/common.page.controller';

const name = 'links';

export default class LinksController extends CommonPageController {
  constructor($scope, tvService) {
    super($scope, name);
    this.tvService = tvService;

    this.model = {
      links: [],
      rightPanelVisible: false,
      editLink: undefined
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
          vm.model.links = res.data.sort(function(l1, l2) {
            return l1.channel.localeCompare(l2.channel);
          });
        });
  }

  add() {
    this.edit({});
  }

  edit(link) {
    this.model.editLink = angular.extend({}, link);
    this.model.rightPanelVisible = true;
  }

  close() {
    this.model.rightPanelVisible = false;
    this.model.editLink = undefined;
  }

}

LinksController.$inject = ['$scope', 'tvService'];
