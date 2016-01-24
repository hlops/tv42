'use strict';

import CommonPageController from '../../common/common.page.controller';

const name = 'sources';

var $mdSidenav;
export default class SourcesController extends CommonPageController {
  constructor($scope, tvService, $mdSidenav) {
    super($scope, name);
    this.tvService = tvService;
    this.$mdSidenav = $mdSidenav;
    this.model = {};
    var vm = this;

    this.readSources();
    this.tvService.getSourceTypes().then(
        function(res) {
          vm.model.sourceTypes = res.data;
        });
  }

  readSources() {
    var vm = this;
    this.tvService.getSources().then(
        function(res) {
          vm.model.sources = res.data;
        });
  }

  add() {
    this.edit({});
  }

  edit(source) {
    this.model.editSource = angular.extend({}, source);
    this.$mdSidenav('right').toggle();
  }

  save() {
    var vm = this;
    this.tvService.saveSource(this.model.editSource).then(function() {
      vm.readSources();
    });
    this.model.editSource = undefined;
    this.$mdSidenav('right').toggle();

  }

  close() {
    this.model.editSource = undefined;
    this.$mdSidenav('right').toggle();
  }
}

SourcesController.$inject = ['$scope', 'tvService', '$mdSidenav'];
