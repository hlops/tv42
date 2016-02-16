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

          vm.model.tvShowChannels = res[1].data.sort(function(l1, l2) {
            return l1.name.localeCompare(l2.name);
          });
          vm.model.tvShowChannelsMap = vm.model.tvShowChannels.reduce(function(result, value) {
            result[value.id] = value.name;
            return result;
          }, {});

          vm.model.links.forEach(function(link) {
            link.tvShowName = vm.model.tvShowChannelsMap[link.tvShow];
          });
        });
  }

  add() {
    this.edit({});
  }

  edit(link) {
    this.model.autocomplete.tvShowSearch = undefined;
    this.model.editLink = angular.extend({}, link);
    this.model.rightPanelVisible = true;
  }

  close() {
    this.model.rightPanelVisible = false;
    this.model.editLink = undefined;
  }

  save() {
    var link = this.model.editLink;
    this.tvService.saveLink(link);
    for (var i = 0; i < this.model.links.length; i++) {
      if (this.model.links[i].channel === link.channel) {
        this.model.links[i] = link;
        break;
      } else if (this.model.links[i].channel > link.channel) {
        this.model.links.splice(i, 0, link);
        break;
      }
    }
    this.close();
  }

  shiftSlider(value) {
    if (this.model.editLink) {
      if (!arguments.length) {
        return this.model.editLink.shift || 0;
      } else {
        this.model.editLink.shift = value;
      }
    }
  }

}

LinksController.$inject = ['$scope', '$q', 'tvService'];
