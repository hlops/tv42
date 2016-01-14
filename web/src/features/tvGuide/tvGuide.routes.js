'use strict';

export default function routes($routeProvider) {
    $routeProvider
        .when('/tv', {
            template: require('./tvGuide.html'),
            reloadOnSearch: false
        })
}

routes.$inject = ['$routeProvider'];