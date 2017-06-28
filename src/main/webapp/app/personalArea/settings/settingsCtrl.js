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

	$scope.prepareToEditPhone = function(idForOperation) {
		$scope.phoneIdForOperation = idForOperation;
		$scope.stage = "editPhone";
	}

	$scope.prepareToDeletePhone = function(idForOperation) {
		$scope.phoneIdForOperation = idForOperation;
		$scope.stage = "deletePhone";
	}


	$scope.query = function() {
		switch ($scope.stage) {
			case 'editBasicInfo': editBasicInfo();
				break;
			case 'changePassword': changePassword();
				break;
			case 'addPhone': addPhone();
				break;
			case 'editPhone': editPhone();
				break;
			case 'deletePhone': deletePhone();
				break;
		}
	}


	var editBasicInfo = function() {
		$http({
			url: sharedData.getLinks().https + '/users/update/basic/' + $scope.myself.objectId,
			method: 'PUT',
			data: {
				firstName : $scope.myself.firstName,
				lastName : $scope.myself.lastName,
				email : $scope.myself.email,
				additionalInfo : $scope.myself.additionalInfo
			},
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			sharedData.setPersonalAreaMyself($scope.myself);
			$scope.stage = "done";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}

	var changePassword = function() {
		if ($scope.myself.newPassword === $scope.myself.newPasswordRep) {			
			$http({
				url: sharedData.getLinks().https + '/users/update/password/' + $scope.myself.objectId,
				method: 'PUT',
				data: {
					oldPassword : $scope.myself.oldPassword,
					newPassword : $scope.myself.newPassword
				},
				headers: { 'Content-Type' : 'application/json' }
			}).then(function(data) {
				console.log(data);
				$scope.stage = "done";
			}, function(response) {
				console.log("Smth wrong!!");
				console.log(response);
			});
		} else {
			$scope.stage = "diffPasswords";
		}
	}















}]);