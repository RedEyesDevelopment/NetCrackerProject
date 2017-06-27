app.controller('settingsCtrl', ['$scope', '$http', '$location', 'sharedData',
	function($scope, $http, $location, sharedData) {

	$scope.myself = {}
	if (sharedData.getMyself() !== undefined) {
		$scope.myself = sharedData.getMyself();
	}
	sharedData.setPersonalAreaMyself($scope.myself);	

	$scope.stage = "looking";
	
	$scope.prepareToEditBasicInfo = function() {
		$scope.stage = "editBasicInfo";
	}

	$scope.prepareToChangePassword = function() {
		$scope.stage = "changePassword";
	}

	$scope.prepareToAddPhone = function() {
		$scope.stage = "addPhone";
	}

	$scope.prepareToEditPhone = function() {
		$scope.stage = "editPhone";
	}

	$scope.prepareToDeletePhone = function() {
		$scope.stage = "deletePhone";
	}












	


}]);