'use strict';

export default class TvGuideController {
    constructor(tvService) {
	    tvService.test();
    }
}

TvGuideController.$inject = ['tvService'];