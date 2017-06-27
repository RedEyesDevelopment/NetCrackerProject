app.controller('settingsCtrl', ['$scope', '$http', '$location', 'sharedData',
	function($scope, $http, $location, sharedData) {

	$scope.myself = sharedData.getMyself();
	console.log($scope.myself);

	
	$scope.addEmptyPhone = function() {
		$scope.myself.phones.push({});
	}

	$scope.removePhoneFromList = function(index) {
		console.log(index);
		$scope.myself.phones.splice(index, 1);
	}


}]);