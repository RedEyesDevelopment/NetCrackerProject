app.controller('settingsCtrl', ['$scope', '$http', '$location', 'sharedData',
	function($scope, $http, $location, sharedData) {

	$scope.myself = {}
	if (sharedData.getMyself() !== undefined) {
		$scope.myself = sharedData.getMyself();
	}
	sharedData.setPersonalAreaMyself($scope.myself);	

	
	$scope.addEmptyPhone = function() {
		// $scope.myself.phones.push({});
		console.log($scope);
	}

	$scope.removePhoneFromList = function(index) {
		console.log(index);
		$scope.myself.phones.splice(index, 1);
	}


}]);