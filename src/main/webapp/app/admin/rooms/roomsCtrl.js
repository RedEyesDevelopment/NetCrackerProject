app.controller('roomsCtrl', ['$scope', '$http', '$location', 'sharedData', 'util',
	function($scope, $http, $location, sharedData, util) {


	/* Функция на получения всех комнат и типов комнат, вызываются сразу */
	(function() {
		$http({
			url: 'http://localhost:8080/rooms',
			method: 'GET',
			headers: {
				'Content-Type' : 'application/json'
			}
		}).then(function(data) {
			console.log(data);
			$scope.listOfRooms = data.data;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}());

	(function() {
		$http({
			url: 'http://localhost:8080/roomtypes',
			method: 'GET',
			headers: {
				'Content-Type' : 'application/json'
			}
		}).then(function(data) {
			console.log(data);
			$scope.listOfRoomTypes = data.data;
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

	$scope.residents = [1, 2, 3];
	$scope.room = {};
	resetFlags();

	$scope.stage = "looking";

	$scope.pager = {
		startPaging : 0,
		objectsOnPage : 3
	}


	/* Функции подготовки запросов */
	$scope.prepareToAddRoom = function() {
		$scope.indexForOperation = "";
		$scope.room.idForOperation = "";
		$scope.room.number = "";
		$scope.room.numberOfResidents = "";
		$scope.room.type = "";
		resetFlags();
		$scope.stage = "additing";
		$scope.modificationMode = true;
	}

	$scope.prepareToEditRoom = function(roomId, index) {
		console.log();
		$http({
			url: 'http://localhost:8080/rooms/' + roomId,
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		}).then(function(data) {
			console.log(data);
			$scope.indexForOperation = index;
			$scope.room.idForOperation = roomId;
			$scope.room.number = data.data.roomNumber;
			$scope.room.numberOfResidents = data.data.numberOfResidents;
			$scope.room.type = data.data.roomType.objectId;
			resetFlags();
			$scope.stage = "editing";
			$scope.modificationMode = true;

		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}

	$scope.prepareToDeleteRoom = function(roomId, index) {
		$scope.indexForOperation = index;
		$scope.room.idForOperation = roomId;
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
			case 'additing': addRoom();
				break;
			case 'editing': editRoom();
				break;
			case 'deleting': deleteRoom();
				break;
		}
	}

	/* Функции, выполняющие запросы */
	addRoom = function() {
		$scope.added = false;
		$http({
			url: 'http://localhost:8080/rooms',
			method: 'POST',
			data: {
				roomNumber : 		$scope.room.number,
				numberOfResidents : parseInt($scope.room.numberOfResidents),
				roomType : 			parseInt($scope.room.type)
			},
			headers: {'Content-Type': 'application/json'}
		}).then(function(data) {
			console.log(data);
			$scope.listOfRooms.push({
				objectId : data.data.objectId,
				roomNumber : $scope.room.number,
				numberOfResidents : $scope.room.numberOfResidents,
				roomType : util.getObjectInArrayById($scope.listOfRoomTypes, $scope.room.type)
			});
			$scope.prepareToAddRoom();
			$scope.added = true;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}

	editRoom = function() {
		$scope.updated = false;
		$http({
			url: 'http://localhost:8080/rooms/' + $scope.room.idForOperation,
			method: 'PUT',
			data: {
				roomNumber : 		$scope.room.number,
				numberOfResidents : parseInt($scope.room.numberOfResidents),
				roomType : 			parseInt($scope.room.type)
			},
			headers: {'Content-Type': 'application/json'}
		}).then(function(data) {
			console.log(data);
			$scope.listOfRooms[$scope.indexForOperation].roomNumber = $scope.room.number;
			$scope.listOfRooms[$scope.indexForOperation].numberOfResidents = $scope.room.numberOfResidents;
			$scope.listOfRooms[$scope.indexForOperation].roomType = util.getObjectInArrayById($scope.listOfRoomTypes, $scope.room.type);
			$scope.updated = true;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}

	deleteRoom = function() {
		$scope.deleted = false;
		$http({
			url: 'http://localhost:8080/rooms/' + $scope.room.idForOperation,
			method: 'DELETE',
			headers: {'Content-Type': 'application/json'}
		}).then(function(data) {
			console.log(data);
			$scope.listOfRooms.splice($scope.indexForOperation, 1);
			$scope.deleted = true;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}


	/* Для листания страниц с объектами */
	$scope.nextRooms = function() {
		$scope.pager.startPaging = util.nextEntities($scope.listOfRooms.length, $scope.pager.startPaging, $scope.pager.objectsOnPage);
	}
	$scope.previousRooms = function() {
		$scope.pager.startPaging = util.previousEntities($scope.pager.startPaging, $scope.pager.objectsOnPage);
	}
}]);