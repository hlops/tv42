'use strict';

import CommonPageController from '../../common/common.page.controller';

const name = 'channels';

export default class ChannelsController extends CommonPageController {
  constructor($scope) {
    super($scope, name);
  }

}

ChannelsController.$inject = ['$scope'];
