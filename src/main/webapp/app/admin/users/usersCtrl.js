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

	/* Инициализация служебных переменных */
	function resetFlags() {
		$scope.added = false;
		$scope.updated = false;
		$scope.deleted = false;
	}

	$scope.enabled = [true, false];
	$scope.user = {};
	resetFlags();

	$scope.stage = "looking";

	$scope.pager = {
		startPaging : 0,
		objectsOnPage : 6
	}


	/* Функции подготовки запросов */
	$scope.prepareToAddUser = function() {
		$scope.indexForOperation = "";
		$scope.user.idForOperation = "";
		$scope.user.lastName = "";
		$scope.user.firstName = "";
		$scope.user.role = "";
		$scope.user.email = "";
        $scope.user.additionalInfo = "";
        $scope.user.enabled = "";
		resetFlags();
		$scope.stage = "adding";
		$scope.modificationMode = true;
	}

	$scope.prepareToEditUser = function(userId, index) {
		$http({
			url: sharedData.getLinks().https + '/users/' + userId,
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		}).then(function(data) {
			console.log(data);
			$scope.indexForOperation = index;
			$scope.user.idForOperation = userId;
			$scope.user.lastName = data.data.lastName;
			$scope.user.firstName = data.data.firstName;
			$scope.user.role = data.data.role.objectId;
			$scope.user.email = data.data.email;
            $scope.user.additionalInfo = data.data.additionalInfo;
            $scope.user.enabled = data.data.enabled;
			resetFlags();
			$scope.stage = "editing";
			$scope.modificationMode = true;

		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}

	$scope.prepareToDeleteUser = function(userId, index) {
		$scope.indexForOperation = index;
		$scope.user.idForOperation = userId;
		resetFlags();
		$scope.stage = "deleting";
	}

	/* Возврат на просмотр */
	$scope.back = function() {
		$scope.stage = "looking";
		$scope.modificationMode = false;
	}

	/* Вызывает нужный запрос в зависимости от типа операции */
	$scope.query = function() {
		switch ($scope.stage) {
			case 'adding': addUser();
				break;
			case 'editing': editUser();
				break;
			case 'deleting': deleteUser();
				break;
		}
	}

	/* Функции, выполняющие запросы */
	var addUser = function() {
		resetFlags();
		console.log($scope.user.confirm)
        if ($scope.user.confirm == $scope.user.password) {
            $http({
                url: sharedData.getLinks().https + '/users',
                method: 'POST',
                data: {
                    lastName: $scope.user.lastName,
                    firstName: $scope.user.firstName,
                    password: $scope.user.password,
                    role: parseInt($scope.user.role),
                    email: $scope.user.email,
                    additionalInfo: $scope.user.additionalInfo,
                    enabled: $scope.user.enabled
                },
                headers: {'Content-Type': 'application/json'}
            }).then(function (data) {
                console.log(data);
                $scope.listOfUsers.push({
                    objectId: data.data.objectId,
                    lastName: $scope.user.lastName,
                    firstName: $scope.user.firstName,
                    role: util.getObjectInArrayById($scope.listOfRoles, $scope.user.role),
                    email: $scope.user.email,
                    additionalInfo: $scope.user.additionalInfo,
                    enabled: $scope.user.enabled
                });
                $scope.prepareToAddUser();
                $scope.added = true;
            }, function (response) {
                console.log("Smth wrong!!");
                console.log(response);
                $scope.errMessage = "serverErr";
            });
        } else {
            $scope.errMessage = "'invalidInputData'";
        }
	}

	var editUser = function() {
		resetFlags();
        console.log($scope.user.confirm);
        if ($scope.user.confirm == $scope.user.password) {
            $http({
                url: sharedData.getLinks().https + '/users/' + $scope.user.idForOperation,
                method: 'PUT',
                data: {
                    lastName: $scope.user.lastName,
                    firstName: $scope.user.firstName,
                    role: parseInt($scope.user.role),
                    email: $scope.user.email,
                    additionalInfo: $scope.user.additionalInfo,
                    enabled: $scope.user.enabled
                },
                headers: {'Content-Type': 'application/json'}
            }).then(function (data) {
                console.log(data);
                $scope.listOfUsers[$scope.indexForOperation].lastName = $scope.user.lastName;
                $scope.listOfUsers[$scope.indexForOperation].firstName = $scope.user.firstName;
                $scope.listOfUsers[$scope.indexForOperation].role = util.getObjectInArrayById($scope.listOfRoles, $scope.user.role);
                $scope.listOfUsers[$scope.indexForOperation].email = $scope.user.email;
                $scope.listOfUsers[$scope.indexForOperation].additionalInfo = $scope.user.additionalInfo;
                $scope.listOfUsers[$scope.indexForOperation].enabled = $scope.user.enabled;
                $scope.stage = 'updated';
            }, function (response) {
                console.log("Smth wrong!!");
                console.log(response);
                $scope.errMessage = "serverErr";
            });
        } else {
            $scope.stage = "invalidInputData";
        }
	}

	var deleteUser = function() {
		resetFlags();
		$http({
			url: sharedData.getLinks().https + '/users/' + $scope.user.idForOperation,
			method: 'DELETE',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfUsers.splice($scope.indexForOperation, 1);
			$scope.deleted = true;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
            $scope.errMessage = "serverErr";
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