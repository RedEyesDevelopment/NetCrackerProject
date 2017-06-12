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

// app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
//     $locationProvider.hashPrefix('');
//
//     $routeProvider
//         .when('/user', {
//             templateUrl: './personal_settings.html',
//             controller: 'authorizationController'
//         });
// }]);
app.controller('login', ['$scope', '$http', '$location' , 'sharedData', '$document', function ($scope, $http, $location, sharedData, $document) {

    $scope.login = function (eve) {

        console.log("Why you dont display?");
        //console.log($document.getElementById("loginform").innerHTML);
        //console.log($document.getElementById("passform").innerHTML);
        console.log($scope.userLogin);
        console.log($scope.userPassword);

        $http({
            url: 'http://localhost:8080/auth/login',
            method: 'POST',
            data: {
                "login": $scope.userLogin,
                "password": $scope.userPassword
            },
            //headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            headers: {'Content-Type': 'application/json'}
        }).then(function (data) {

            if (data.data == true) {
                console.log(data.data);
            }
            //$scope.wasUpdatedUser = false;

            console.log(data.data);

            $http({
                url: 'http://localhost:8080/orders',
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(function(data) {
                console.log(data);
            }, function(response) {
                console.log(response);
            });
        }, function (response) {
            console.log(response);
            console.log("I_AM_TEAPOT!");
        });


    }
}]);

app.controller('search-available', ['$scope', '$http', '$location' , 'sharedData', '$document', function ($scope, $http, $location, sharedData, $document) {

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

