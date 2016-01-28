'use strict';

tvMoveToParent.$inject = [];

export default function tvMoveToParent() {
  return {
    restrict: 'A',
    link: function($scope, element, attributes) {
      var selector = attributes['tvMoveToParent'];
      var placeholder = angular.element(document.querySelector(selector));
      placeholder.replaceWith(element);

      $scope.$on('$destroy', function() {
        element.replaceWith(placeholder);
      });
    }
  };
}
