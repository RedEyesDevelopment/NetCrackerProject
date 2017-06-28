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

	$scope.prepareToEditPhone = function(objId, index) {
		$scope.phoneObjIdForOperation = objId;
		$scope.phoneIndexForOperation = index;
		$scope.stage = "editPhone";
	}

	$scope.prepareToDeletePhone = function(objId, index) {
		$scope.phoneObjIdForOperation = objId;
		$scope.phoneIndexForOperation = index;
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

	var addPhone = function() {
		$http({
			url: sharedData.getLinks().https + '/phones',
			method: 'POST',
			data: {
				phoneNumber: $scope.myself.newPhone
			},
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.myself.phones.push({
				objectId: data.data.objectId,
				phone: $scope.myself.newPhone
			});
			$scope.stage = "done";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}

	var editPhone = function() {
		$http({
			url: sharedData.getLinks().https + '/phones/' + $scope.phoneObjIdForOperation,
			method: 'PUT',
			data: {
				phoneNumber: $scope.myself.phones[$scope.phoneIndexForOperation].phoneNumber
			},
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.stage = "done";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}

	var deletePhone = function() {
		$http({
			url: sharedData.getLinks().https + '/phones/' + $scope.phoneObjIdForOperation,
			method: 'DELETE',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.myself.phones.splice($scope.phoneIndexForOperation, 1);
			$scope.stage = "done";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}

}]);