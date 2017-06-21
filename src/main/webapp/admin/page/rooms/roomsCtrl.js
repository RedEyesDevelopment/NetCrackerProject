adminDesktop.controller('roomsCtrl', ['$scope', '$http', function($scope, $http){

	/*  */
	getAllRooms = function(e) {
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

	$scope.start = 0;
	$scope.objectsOnPage = 3;

	$scope.attemptToEditRoom = function(objectId) {
		$http({
			url: 'http://localhost:8080/rooms/' + objectId,
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		}).then(function(data) {
			console.log(data);
			$scope.editing = true;
			$scope.roomNumber = data.data.roomNumber;
			$scope.numberOfResidents = data.data.numberOfResidents;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}

	$scope.back = function() {
		$scope.editing = false;
	}

	$scope.editRoom = function(event) {
		$scope.updated = false;
		$http({
			url: 'http://localhost:8080/rooms/' + $scope.roomID,
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