app.controller('asidePersonalAreaCtrl', ['$scope', 'sharedData',
	function($scope, sharedData) {

    $scope.getMyself = function() { return sharedData.getMyself(); }

}]);