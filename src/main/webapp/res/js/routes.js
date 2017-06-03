var app = angular.module('app', ['ngRoute', 'ngAnimate']);


app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');

    $routeProvider
        .when('/rooms', {
            templateUrl: './room_type.html',
            controller: 'roomTypeController'
        });
}]);


app.controller('search-available', ['$scope', '$http', function ($scope, $http) {

    $http({
        url: 'http://localhost:8080/orders',
        method: 'GET',

        headers: {'Content-Type': 'application/x-www-form-urlencoded'},

    }).then(function (data) {
        console.log(data);
    });


    $scope.submit = function (eve) {


        console.log({
            arrival: $scope.book.from.getTime(),
            departure: $scope.book.till.getTime(),
            livingPersons: $scope.book.adults,
            categoryId: $scope.book.type
        });


        $http({
            url: 'http://localhost:8080/orders/searchavailability',
            method: 'POST',
            data: {
                arrival: $scope.book.from.getTime(),
                departure: $scope.book.till.getTime(),
                livingPersons: parseInt($scope.book.adults),
                categoryId: parseInt($scope.book.type)
            },


            headers: {
                'Content-Type': 'application/json'
            }


        }).then(function(data) {
            console.log(data);
        }, function(response) {
            console.log(response);
            console.log("I_AM_TEAPOT!")
        });

    }


}]);

app.controller('roomTypeController', ['$scope', '$http', function ($scope, $http) {

    console.log({
        arrival : $scope.book.from.getTime(),
        departure : $scope.book.till.getTime(),
        livingPersons: $scope.book.adults,
        categoryId: $scope.book.type
    });

    $http({
        url: 'http://localhost:8080/orders/searchavailability',
        method: 'POST',

        data: {
            arrival: 1496955600000, departure: 1496955600000, livingPersons: 2, categoryId: 450
        },

        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        }
    }).then(function (response) {
        $scope.list = response.data
    });

    $scope.validateDate = function (date) {
        console.log(date);

        return false;
    }


    $scope.bookApartment = function (id) {
        console.log(id);
    }

}]);