app.controller('usersCtrl', ['$scope', '$http', '$location', 'sharedData', 'util',
	function($scope, $http, $location, sharedData, util) {

	/* Функция на получения всех юзеров и их ролей, вызываются сразу */
	(function() {
		$http({
			url: sharedData.getLinks().https + '/users/',
			method: 'GET',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfUsers = data.data;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}());

	(function() {
		$http({
			url: sharedData.getLinks().https + '/roles',
			method: 'GET',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfRoles = data.data;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}());
	/* редирект на главную если не админ */
	(function() {
		if (!sharedData.getIsAdmin()) { $location.path('/') };
	}());

	// это для кнопок дополнительной информации (не удалять!)
	$scope.expand = {id : 0};

	$scope.enabled = [true, false];
	$scope.user = {};

	$scope.mode = "look";

	$scope.pager = {
		startPaging : 0,
		objectsOnPage : 6
	}

	/* Возврат на просмотр */
	$scope.back = function() {
		$scope.mode = "look";
		$scope.modificationMode = false;
	}

	$scope.query = function() {
		switch ($scope.stage) {
			case 'adding': addUser();
				break;
			case 'deleting': deleteUser();
				break;
			case 'reestablishing': reestablishUser();
				break;
			case 'changeRole': changeRole();
				break;
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


	$scope.prepareToAddUser = function() {
		$scope.userIdForOperation = "";
		$scope.userIndexForOperation = "";
		$scope.user = {};
		$scope.stage = "adding";
		$scope.mode = "add";
	}

	var addUser = function() {
        if ($scope.user.confirm == $scope.user.password) {
            $http({
                url: sharedData.getLinks().https + '/users',
                method: 'POST',
                data: {
                    email: $scope.user.email,
                    password: $scope.user.password,
                    firstName: $scope.user.firstName,
                    lastName: $scope.user.lastName,
                    additionalInfo: $scope.user.additionalInfo,
                    phoneNumber : $scope.user.phone,
                    roleId: $scope.user.role
                },
                headers: {'Content-Type': 'application/json'}
            }).then(function (data) {
                console.log(data);
                $http({
                    url: sharedData.getLinks().https + '/users/' + data.data.objectId,
                    method: 'GET',
                    headers: {'Content-Type': 'application/json'}
                }).then(function(data) {
                    console.log(data);
                    $scope.listOfUsers.push(data.data);
                }, function(response) {
                    console.log("Smth wrong!!");
                    console.log(response);
                });
                $scope.prepareToAddUser();
                $scope.stage = "added";
            }, function (response) {
                console.log("Smth wrong!!");
                console.log(response);
                $scope.errMessage = "serverErr";
            });
        } else {
            $scope.errMessage = "'invalidInputData'";
        }
	}


	$scope.prepareToDeleteUser = function(userId, index) {
		$scope.userIndexForOperation = index;
		$scope.userIdForOperation = userId;
		$scope.stage = "deleting";
	}

	var deleteUser = function() {
		$http({
			url: sharedData.getLinks().https + '/users/delete/' + $scope.userIdForOperation,
			method: 'PUT',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfUsers[$scope.userIndexForOperation].enabled = false;
			$scope.stage = "deleted";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
            $scope.errMessage = "serverErr";
		});
	}


	$scope.prepareToReestablishUser = function(userId, index) {
		$scope.userIdForOperation = userId;
		$scope.userIndexForOperation = index;
		$scope.stage = "reestablishing";
	}

	var reestablishUser = function() {
		$http({
			url: sharedData.getLinks().https + '/users/restore/' + $scope.userIdForOperation,
			method: 'PUT',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfUsers[$scope.userIndexForOperation].enabled = true;
			$scope.stage = "reestablished";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
            $scope.errMessage = "serverErr";
		});
	}



	$scope.prepareToEditUser = function(userId, index) {
		$scope.userIdForOperation = userId;
		$scope.userIndexForOperation = index;
		$http({
			url: sharedData.getLinks().https + '/users/' + $scope.userIdForOperation,
			method: 'GET',
			headers: {'Content-Type' : 'application/json'}
		}).then(function(data) {
			console.log(data);
			$scope.user = data.data;
			$scope.stage = "editing";
			$scope.mode = 'edit';
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}


	$scope.prepareToChangeRole = function() {
		$scope.stage = "changeRole";
	}

	var changeRole = function() {
		$http({
			url: sharedData.getLinks().https + '/users/admin/update/role/' + $scope.userIdForOperation,
			method: 'PUT',
			data: {
				roleId: parseInt($scope.user.role.objectId)
			},
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfUsers[$scope.userIndexForOperation].role = util.getObjectInArrayById($scope.listOfRoles, $scope.user.role.objectId);
			$scope.stage = "edited";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}


	$scope.prepareToEditBasicInfo = function() {
		$scope.stage = "editBasicInfo";
	}

	var editBasicInfo = function() {
		$http({
			url: sharedData.getLinks().https + '/users/admin/update/basic/' + $scope.userIdForOperation,
			method: 'PUT',
			data: {
				firstName : $scope.user.firstName,
				lastName : $scope.user.lastName,
				email : $scope.user.email,
				additionalInfo : $scope.user.additionalInfo
			},
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfUsers[$scope.userIndexForOperation].firstName = $scope.user.firstName;
			$scope.listOfUsers[$scope.userIndexForOperation].lastName = $scope.user.lastName;
			$scope.listOfUsers[$scope.userIndexForOperation].email = $scope.user.email;
			$scope.listOfUsers[$scope.userIndexForOperation].additionalInfo = $scope.user.additionalInfo;
			$scope.stage = "edited";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}



	$scope.prepareToChangePassword = function() {
		$scope.stage = "changePassword";
	}

	var changePassword = function() {
		if ($scope.user.newPassword === $scope.user.newPasswordRep) {			
			$http({
				url: sharedData.getLinks().https + '/users/admin/update/password/' + $scope.userIdForOperation,
				method: 'PUT',
				data: {
					oldPassword : $scope.user.oldPassword,
					newPassword : $scope.user.newPassword
				},
				headers: { 'Content-Type' : 'application/json' }
			}).then(function(data) {
				console.log(data);
				$scope.stage = "edited";
			}, function(response) {
				console.log("Smth wrong!!");
				console.log(response);
			});
		} else {
			$scope.stage = "diffPasswords";
		}
	}



	$scope.prepareToAddPhone = function() {
		$scope.stage = "addPhone";
	}

	$scope.cancelPhone = function() {
		$scope.stage = "editing";
	}

	var addPhone = function() {
		$http({
			url: sharedData.getLinks().https + '/phones',
			method: 'POST',
			data: {
				userId: 		$scope.userIdForOperation,
				phoneNumber: 	$scope.user.newPhone
			},
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			var newPhone = {
				objectId: data.data.objectId,
				phoneNumber: $scope.user.newPhone,
				userId: $scope.userIdForOperation
			};
			$scope.user.phones.push(newPhone);
			$scope.listOfUsers[$scope.userIndexForOperation].phones.push(newPhone);
			$scope.stage = "edited";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}


	
	$scope.prepareToEditPhone = function(objId, index) {
		$scope.phoneObjIdForOperation = objId;
		$scope.phoneIndexForOperation = index;
		$scope.stage = "editPhone";
	}

	var editPhone = function() {
		$http({
			url: sharedData.getLinks().https + '/phones/' + $scope.phoneObjIdForOperation,
			method: 'PUT',
			data: {
				userId: 		$scope.userIdForOperation,
				phoneNumber: 	$scope.user.phones[$scope.phoneIndexForOperation].phoneNumber
			},
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfUsers[$scope.userIndexForOperation].phones[$scope.phoneIndexForOperation].phoneNumber = $scope.user.phones[$scope.phoneIndexForOperation].phoneNumber
			$scope.stage = "edited";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}



	$scope.prepareToDeletePhone = function(objId, index) {
		$scope.phoneObjIdForOperation = objId;
		$scope.phoneIndexForOperation = index;
		$scope.stage = "deletePhone";
	}

	var deletePhone = function() {
		$http({
			url: sharedData.getLinks().https + '/phones/' + $scope.phoneObjIdForOperation,
			method: 'DELETE',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.user.phones.splice($scope.phoneIndexForOperation, 1);
			$scope.listOfUsers[$scope.userIndexForOperation].phones.splice($scope.phoneIndexForOperation, 1);
			$scope.stage = "edited";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}



	/* Для листания страниц с объектами */
	$scope.nextUsers = function() {
		$scope.pager.startPaging = util.nextEntities($scope.listOfUsers.length, $scope.pager.startPaging, $scope.pager.objectsOnPage);
	}
	$scope.previousUsers = function() {
		$scope.pager.startPaging = util.previousEntities($scope.pager.startPaging, $scope.pager.objectsOnPage);
	}
}]);