adminDesktop.controller('roomsCtrl', ['$scope', '$http', function($scope, $http){

	getAllRooms = function() {
		$http({
			url: 'http://localhost:8080/rooms',
			method: 'GET',
			headers: {
				'Content-Type': 'application/json'
			}
		}).then(function(data) {
			console.log(data);
			$scope.listOfRooms = data.data;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}();

	getAllRoomTypes = function() {
		$http({
			url: 'http://localhost:8080/roomtypes',
			method: 'GET',
			headers: {
				'Content-Type': 'application/json'
			}
		}).then(function(data) {
			console.log(data);
			$scope.listOfRoomTypes = data.data;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}();

	$scope.start = 0;
	$scope.objectsOnPage = 3;
	$scope.stage = "looking";
	$scope.added = false;
	$scope.updated = false;
	$scope.deleted = false;
	$scope.residents = [1, 2, 3];

	$scope.prepareToAddRoom = function() {
		$scope.addingOrEditing = true;
		$scope.stage = "additing";
	}

	$scope.prepareToEditRoom = function(roomId) {
		$scope.roomIdForOperation = roomId;
		$http({
			url: 'http://localhost:8080/rooms/' + roomId,
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		}).then(function(data) {
			console.log(data);
			$scope.roomNumber = data.data.roomNumber;
			$scope.numberOfResidents = data.data.numberOfResidents;
			$scope.roomType = data.data.roomType.objectId;
			$scope.addingOrEditing = true;
			$scope.stage = "editing";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}

	$scope.prepareToDeleteRoom = function(roomId) {
		$scope.roomIdForOperation = roomId;
		$scope.stage = "deleting";
	}

	$scope.back = function() {
		$scope.addingOrEditing = false;
		$scope.stage = "looking";
	}

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

	editRoom = function() {
		$scope.updated = false;
		console.log($scope.roomNumber);
		console.log($scope.numberOfResidents);
		console.log($scope.roomType);
		data = {
				"roomNumber": 		$scope.roomNumber,
				"numberOfResidents": 	$scope.numberOfResidents,
				"roomType": 			$scope.roomType
			}
		console.log(data);
		$http({
			url: 'http://localhost:8080/rooms/' + $scope.roomIdForOperation,
			method: 'PUT',
			data: {
				roomNumber: 		$scope.roomNumber,
				numberOfResidents: 	$scope.numberOfResidents,
				roomType: 			$scope.roomType
			},
			headers: {'Content-Type': 'application/json'}
		}).then(function(data) {
			console.log(data);
			$scope.updated = true;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}

	$scope.nextRooms = function() {
		if ($scope.listOfRooms.length > $scope.start + $scope.objectsOnPage) {
			$scope.start += $scope.objectsOnPage;
		}
	}

	$scope.previousRooms = function() {
		if ($scope.start - $scope.objectsOnPage >= 0) {
			$scope.start -= $scope.objectsOnPage;
		} else if ($scope.start > 0) {
			$scope.start = 0;
		}
	}
}])