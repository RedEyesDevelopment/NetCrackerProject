var app = angular.module('app' , ['ngRoute' , 'ngAnimate']);


app.config(['$routeProvider' , '$locationProvider' , function($routeProvider , $locationProvider) {
	$locationProvider.hashPrefix('');

	$routeProvider
		.when('/rooms' , {
			templateUrl: './room_type.html',
			controller: 'roomTypeController'
		});
}]);


app.controller('search-available' , ['$scope' , function($scope) {

	
	$scope.submit = function(eve) {
		
		// if ( !$scope.validDate() ) {
		// 	eve.preventDefault();
		// 	return;
		// }


	var order = {
			arrival : $scope.book.from.getTime(),
			departure : $scope.book.till.getTime(),
			livingPersons: $scope.book.adults,
			categoryId: $scope.book.type,
		}

	$http({
		url: 'http://localhost:8080/orders/searchavailability',
		method: 'POST',
		data : {
			arrival : $scope.book.from.getTime(),
			departure : $scope.book.till.getTime(),
			livingPersons: $scope.book.adults,
			categoryId: $scope.book.type
		},


		headers: {
			'Content-Type': 'application/json; charset=utf-8'
		}


	})

	}
	

}]);

app.controller('roomTypeController' , ['$scope', '$http' , function($scope, $http) {

    $http({
        url: 'http://localhost:8080/orders/searchavailability',
        method: 'POST',

        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        }
    }).then(function(response) {
		 $scope.list = response.data
	});

	$scope.validateDate = function(date) {
		console.log(date);

		return false;
	}


	$scope.bookApartment = function(id) {
		console.log(id);
	}	
    
}]);