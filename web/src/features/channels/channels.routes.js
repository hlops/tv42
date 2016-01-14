'use strict';

export default function routes($routeProvider) {
    $routeProvider
        .when('/channels', {
            template: require('./channels.html'),
            reloadOnSearch: false
        })
}

routes.$inject = ['$routeProvider'];