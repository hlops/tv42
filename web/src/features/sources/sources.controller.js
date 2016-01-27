'use strict';

import CommonPageController from '../../common/common.page.controller';

const name = 'sources';

export default class SourcesController extends CommonPageController {
  constructor($scope, tvService, $location) {
    super($scope, name);
    this.tvService = tvService;
    this.$location = $location;

    this.model = {
      sources: [],
      rightPanelVisible: false,
      editSource: undefined
    };
    this.init($scope);
  }

  init($scope) {
    var vm = this;
    /*
     $scope.$watch(() => this.model.rightPanelVisible, function(newValue, oldValue) {
     if (newValue != oldValue) {
     var action = 'new';
     if (vm.model.editSource.id) {
     action = 'edit';
     }
     vm.$location.search(action, newValue ? vm.model.editSource.id : null);
     }
     });
     */
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
    this.model.rightPanelVisible = true;
  }

  save() {
    var vm = this;
    this.tvService.saveSource(this.model.editSource).then(function() {
      vm.readSources();
    });
    this.close();
  }

  close() {
    this.model.editSource = undefined;
    this.model.rightPanelVisible = false;
  }
}

SourcesController.$inject = ['$scope', 'tvService', '$location'];
