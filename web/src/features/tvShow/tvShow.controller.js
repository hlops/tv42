'use strict';

import CommonPageController from '../../common/common.page.controller';

const name = 'tvShow';

export default class TvShowController extends CommonPageController {
  constructor($scope, tvService) {
    super($scope, name);
  }
}

TvShowController.$inject = ['$scope', 'tvService'];
