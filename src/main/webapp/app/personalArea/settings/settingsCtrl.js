app.controller('settingsCtrl', ['$scope', '$http', '$location', 'sharedData',
	function($scope, $http, $location, sharedData) {

	$scope.myself = sharedData.getMyself();
	console.log($scope.myself);

	


}]);