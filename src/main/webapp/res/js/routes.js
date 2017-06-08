var app = angular.module('app', ['ngRoute', 'ngAnimate']);


app.factory('sharedData' , function() {
    var sharedData = {};

    return {
        getData: function() {
            return sharedData;
        },

        setData: function(data) {
            sharedData = data;

        }

    }


});


app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');

    $routeProvider
        .when('/rooms', {
            templateUrl: './room_type.html',
            controller: 'roomTypeController'
        });
}]);


app.controller('search-available', ['$scope', '$http', '$location' , 'sharedData' , function ($scope, $http, $location, sharedData) {

    $http({
        url: 'http://localhost:8080/orders',
        method: 'GET',

        headers: {'Content-Type': 'application/x-www-form-urlencoded'},

    }).then(function (data) {
        console.log(data);
    });


    $scope.submit = function (eve) {

        console.log($scope.book.from.getTime());
        console.log($scope.book.till.getTime());
        console.log(parseInt($scope.book.adults));
        console.log(parseInt($scope.book.type));

        $http({
            url: 'http://localhost:8080/orders/searchavailability',
            method: 'POST',
            data: {
                arrival:   $scope.book.from.getTime(),
                departure: $scope.book.till.getTime(),
                livingPersons: parseInt($scope.book.adults),
                categoryId: parseInt($scope.book.type)
            },


            headers: {
                'Content-Type': 'application/json'
            }


        }).then(function(data) {

            sharedData.setData(data);


            $location.path('/rooms');


        }, function(response) {
            console.log(response);
        });


    }



}]);

app.controller('roomTypeController', ['$scope', '$http', 'sharedData' , function ($scope, $http, sharedData) {

    var book = sharedData.getData();

    $scope.list = book.data;
    window.scrollTo(0,1000);

    $scope.bookApartment = function (id) {
        console.log(id);
    }

}]);