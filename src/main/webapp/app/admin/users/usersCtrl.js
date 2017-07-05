app.controller('usersCtrl', ['$scope', '$http', '$location', 'sharedData', 'util',
	function($scope, $http, $location, sharedData, util) {

	// All users
	(function() {
		$http({
			url: sharedData.getLinks().https + '/users/',
			method: 'GET',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.originListOfUsers = data.data;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}());
	// All roles
	(function() {
		$http({
			url: sharedData.getLinks().https + '/roles',
			method: 'GET',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			if ($scope.isAdmin) {
				$scope.listOfRoles = data.data;
			} else {
				$scope.listOfRoles = [util.getObjectInArrayById(data.data, 3)];
			}
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}());
	/* редирект на главную если не админ и не рецепция */
	(function() {
		if (!sharedData.getIsAdmin() && !sharedData.getIsReception()) { $location.path('/') };
	}());

	$scope.isAdmin = sharedData.getIsAdmin();

	// это для кнопок дополнительной информации (не удалять!)
	$scope.expand = {id : 0};

	$scope.enabled = [true, false];
	$scope.user = {};

	$scope.mode = "look";

	$scope.pager = {
		startPaging : 0,
		objectsOnPage : 6
	}

	$scope.myObjectId = function() {
		return sharedData.getMyself().objectId;
	}

	/* Возврат на просмотр */
	$scope.back = function() {
		$scope.mode = "look";
		$scope.modificationMode = false;
	}

	$scope.query = function() {
		switch ($scope.stage) {
			case 'addingByAdmin': addUser();
				break;
			case 'addingByReception': registration();
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
		$scope.user = {
			additionalInfo: ""
		};
		if (!$scope.isAdmin) {
			$scope.user.role = $scope.listOfRoles[0].objectId;
			$scope.stage = "addingByReception";
		} else {
			$scope.stage = "addingByAdmin";
		}
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
                    $scope.originListOfUsers.push(data.data);
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

	var registration = function() {
        if ($scope.user.password === $scope.user.confirm) {
            $http({
                url: sharedData.getLinks().https + '/users/registration',
                method: 'POST',
                data: {
                    objectId: 0,
                    email: $scope.user.email,
                    password: $scope.user.password,
                    firstName: $scope.user.firstName,
                    lastName: $scope.user.lastName,
                    additionalInfo: $scope.user.additionalInfo,
                    enabled: true,
                    role: { objectId: 0, roleName: "" },
                    phones: [
                        {
                            objectId: 0,
                            userId: 0,
                            phoneNumber: $scope.user.phone
                        }
                    ]
                },
                headers: {'Content-Type' : 'application/json'}
            }).then(function (data) {
                console.log(data);
                $http({
                    url: sharedData.getLinks().https + '/users/' + data.data.objectId,
                    method: 'GET',
                    headers: {'Content-Type': 'application/json'}
                }).then(function(data) {
                    console.log(data);
                    $scope.originListOfUsers.push(data.data);
                }, function(response) {
                    console.log("Smth wrong!!");
                    console.log(response);
                });
                $scope.prepareToAddUser();
                $scope.stage = "added";
            }, function (response) {
                console.log(response);
                console.log("Smth wrong!!");
                // тут отобразить отказ от сервера
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
			$scope.originListOfUsers[$scope.userIndexForOperation].enabled = false;
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
			$scope.originListOfUsers[$scope.userIndexForOperation].enabled = true;
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
			$scope.originListOfUsers[$scope.userIndexForOperation].role = util.getObjectInArrayById($scope.listOfRoles, $scope.user.role.objectId);
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
			$scope.originListOfUsers[$scope.userIndexForOperation].firstName = $scope.user.firstName;
			$scope.originListOfUsers[$scope.userIndexForOperation].lastName = $scope.user.lastName;
			$scope.originListOfUsers[$scope.userIndexForOperation].email = $scope.user.email;
			$scope.originListOfUsers[$scope.userIndexForOperation].additionalInfo = $scope.user.additionalInfo;
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
			$scope.originListOfUsers[$scope.userIndexForOperation].phones.push(newPhone);
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
			$scope.originListOfUsers[$scope.userIndexForOperation].phones[$scope.phoneIndexForOperation].phoneNumber = $scope.user.phones[$scope.phoneIndexForOperation].phoneNumber
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
			$scope.originListOfUsers[$scope.userIndexForOperation].phones.splice($scope.phoneIndexForOperation, 1);
			$scope.stage = "edited";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}


	/* Filters */
	$scope.resetFilter = function() {
		$scope.filter = {};
		$scope.filteredListOfUsers = [];
	}	
	$scope.resetFilter();
	$scope.updateFilter = function() {
		$scope.filteredListOfUsers = $scope.originListOfUsers.filter(function(item) {
			return $scope.filter.roleId ? (item.role.objectId === $scope.filter.roleId) : true;
		});
	}



	/* Для листания страниц с объектами */
	$scope.nextUsers = function() {
		$scope.pager.startPaging = util.nextEntities($scope.originListOfUsers.length, $scope.pager.startPaging, $scope.pager.objectsOnPage);
	}
	$scope.previousUsers = function() {
		$scope.pager.startPaging = util.previousEntities($scope.pager.startPaging, $scope.pager.objectsOnPage);
	}
}]);