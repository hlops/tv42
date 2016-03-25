'use strict';

import CommonPageController from '../../common/common.page.controller';

const name = 'links';

var injections = {};

export default class LinksController extends CommonPageController {
  constructor($scope, $q, tvService) {
    super($scope, name);
    this.tvService = tvService;

    injections['$q'] = $q;
    this.init();
  }

  createModel() {
    return {
      caption: 'Связи',
      links: [],
      prelinks: [],
      tvShowChannels: [],
      tvShowChannelsMap: {},
      rightPanelVisible: false,
      editLink: undefined,
      autocomplete: {}
    };
  }

  init() {
    this.readLinks();
  }

  readLinks() {
    var $q = injections['$q'];
    var vm = this;
    $q.all([this.tvService.getLinks(), this.tvService.getLinksChannels(), this.tvService.getChannelNames()])
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

              var prelinksMap = res[2].data.reduce(function(result, value) {
                result[value] = true;
                return result;
              }, {});

              vm.model.links.forEach(function(link) {
                link.tvShowName = vm.model.tvShowChannelsMap[link.tvShow];
                delete prelinksMap[link.channel]
              });

              vm.model.prelinks = Object.keys(prelinksMap).sort();
            });
  }

  add(channel) {
    this.edit({channel});
  }

  edit(link) {
    this.model.autocomplete = {};
    this.model.editLink = angular.extend({}, link);
    this.model.rightPanelVisible = true;
  }

  close() {
    this.model.rightPanelVisible = false;
    this.model.editLink = undefined;
  }

  save() {
    var vm = this;
    var link = this.model.editLink;
    this.tvService.saveLink(link).then(function() {
      vm.readLinks();
    });
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

  getGroups() {
    return this.tvService.model.groups;
  }

  channelSelected(channel) {
    if (channel && channel.id) {
      this.model.editLink.tvShow = channel.id;
    }
  }

  groupSelected(group) {
    if (group) {
      this.model.editLink.group = group;
    }
  }

  download() {
    var link = angular.element('<a/>');
    link.attr({
      href: '/rest/links/raw',
      download: 'default.links.json',
      style: 'display: none;'
    });
    //document.body.appendChild(link[0]);
    link[0].click();
    link.remove();
  }

}

LinksController.$inject = ['$scope', '$q', 'tvService'];
