app.controller('settingsCtrl', ['$scope', '$http', '$location', 'sharedData',
	function($scope, $http, $location, sharedData) {


	$scope.stage = "looking";


	$scope.getMyself = function() {
		return sharedData.getMyself();
	}
	
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


	$scope.cancelPhone = function() {
		$scope.errMessage = false;
		$scope.stage = "looking";
	}

	var editBasicInfo = function() {
		$scope.errMessage = false;
		$http({
			url: sharedData.getLinks().https + '/users/update/basic/' + $scope.getMyself().objectId,
			method: 'PUT',
			data: {
				firstName : $scope.getMyself().firstName,
				lastName : $scope.getMyself().lastName,
				email : $scope.getMyself().email,
				additionalInfo : $scope.getMyself().additionalInfo
			},
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.stage = "done";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
			$scope.errMessage = response.data.message;
		});
	}

	var changePassword = function() {
		$scope.errMessage = false;
		if ($scope.getMyself().newPassword === $scope.getMyself().newPasswordRep) {			
			$http({
				url: sharedData.getLinks().https + '/users/update/password/' + $scope.getMyself().objectId,
				method: 'PUT',
				data: {
					oldPassword : $scope.getMyself().oldPassword,
					newPassword : $scope.getMyself().newPassword
				},
				headers: { 'Content-Type' : 'application/json' }
			}).then(function(data) {
				console.log(data);
				$scope.stage = "done";
			}, function(response) {
				console.log("Smth wrong!!");
				console.log(response);
				$scope.errMessage = response.data.message;
			});
		} else {
			$scope.stage = "diffPasswords";
		}
	}

	var addPhone = function() {
		$scope.errMessage = false;
		$http({
			url: sharedData.getLinks().https + '/phones',
			method: 'POST',
			data: {
				userId: 		$scope.getMyself().objectId,
				phoneNumber: 	$scope.getMyself().newPhone
			},
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			$scope.getMyself().phones.push({
				objectId: data.data.objectId,
				phoneNumber: $scope.getMyself().newPhone
			});
			$scope.stage = "done";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
			$scope.errMessage = response.data.message;
		});
	}

	var editPhone = function() {
		$scope.errMessage = false;
		$http({
			url: sharedData.getLinks().https + '/phones/' + $scope.phoneObjIdForOperation,
			method: 'PUT',
			data: {
				userId: 		$scope.getMyself().objectId,
				phoneNumber: 	$scope.getMyself().phones[$scope.phoneIndexForOperation].phoneNumber
			},
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.stage = "done";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
			$scope.errMessage = response.data.message;
		});
	}

	var deletePhone = function() {
		$scope.errMessage = false;
		$http({
			url: sharedData.getLinks().https + '/phones/' + $scope.phoneObjIdForOperation,
			method: 'DELETE',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.getMyself().phones.splice($scope.phoneIndexForOperation, 1);
			$scope.stage = "done";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
			$scope.errMessage = response.data.message;
		});
	}

}]);