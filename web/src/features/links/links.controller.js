'use strict';

import CommonPageController from '../../common/common.page.controller';

const name = 'links';

export default class LinksController extends CommonPageController {
  constructor($scope, $q, tvService) {
    super($scope, name);
    this.tvService = tvService;

    this.model = {
      links: [],
      tvShowChannels: [],
      tvShowChannelsMap: {},
      rightPanelVisible: false,
      editLink: undefined,
      autocomplete: {}
    };

    this.init($q);
  }

  init($q) {
    this.readLinks($q);
  }

  readLinks($q) {
    var vm = this;
    $q.all([this.tvService.getLinks(), this.tvService.getLinksChannels()])
        .then(
        function(res) {
          vm.model.links = res[0].data.sort(function(l1, l2) {
            return l1.channel.localeCompare(l2.channel);
          });
          vm.model.tvShowChannels = res[1].data.sort();
          vm.model.tvShowChannelsMap = vm.model.tvShowChannels.reduce(function(result, value) {
            result[value.id] = value.name;
            return result;
          }, {});
          vm.calculateChannelNames();
        });
  }

  calculateChannelNames() {
    var vm = this;

    this.model.links.forEach(function(link) {
      link.tvShowName = vm.model.tvShowChannelsMap[link.tvShow];
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

LinksController.$inject = ['$scope', '$q', 'tvService'];
