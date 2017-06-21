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
			console.log(response);
		});
	}();

	$scope.start = 0;
	$scope.objectsOnPage = 3;

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