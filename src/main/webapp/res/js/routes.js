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

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');

    $routeProvider
        .when('/order', {
            templateUrl: './order.html',
            controller: 'finishOrder'
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
                eventFire(document.getElementById('loginformclose'), 'click');
            } else {
            var loginerrorlabel = document.getElementById('autherror');
            loginerrorlabel.className="showLoginErrorLabel"}
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
            console.log(data);
            sharedData.setData(data);
            $location.path('/rooms');
        }, function(response) {
            console.log(response);
        });
    }

}]);

app.controller('roomTypeController', ['$scope', '$http', 'sharedData', '$location' , function ($scope, $http, sharedData, $location) {

    var book = sharedData.getData();

    $scope.list = book.data;
    $scope.auth = window.auth;
    window.scrollTo(0,1000);

    $scope.bookApartment = function (id) {
        console.log("THIS IS IDDDDDDDDDDDDDDDDDDDDDDDD: " + id);
        $http({
            url: 'http://localhost:8080/orders/book/' + id,
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(function(data) {
            sharedData.setData(data);
            $location.path('/order');
            console.log(data);
        }, function(response) {
            console.log(response);
        });
    }

}]);

app.controller('finishOrder', ['$scope', '$http', 'sharedData', '$location' , function ($scope, $http, sharedData, $location) {

    $scope.finishOrder = sharedData.getData().data;
    $scope.auth = window.auth;

    // $scope.bookApartment = function (id) {
    //     console.log("THIS IS IDDDDDDDDDDDDDDDDDDDDDDDD: " + id);
    //     $http({
    //         url: 'http://localhost:8080/orders/book/' + id,
    //         method: 'GET',
    //         headers: {
    //             'Content-Type': 'application/json'
    //         }
    //     }).then(function(data) {
    //         sharedData.setData(data);
    //         $location.path('/order');
    //         console.log(data);
    //     }, function(response) {
    //         console.log(response);
    //     });
    // }

}]);

app.controller('registration', ['$scope', '$http', '$location' , 'sharedData', function ($scope, $http, $location, sharedData) {

    $scope.submit = function (eve) {

        console.log("Why you dont display?");
        // console.log(document.getElementById("logincform").innerHTML);
        //console.log($document.getElementById("passform").innerHTML);

        console.log($scope.userEmail);
        console.log($scope.userPassword);
        console.log($scope.userPasswordRepeat);
        console.log($scope.userName);
        console.log($scope.userSurname);
        console.log($scope.userInfo);
        console.log($scope.userPhone);

        if ($scope.userPasswordRepeat === $scope.userPassword) {
            $http({
                url: 'http://localhost:8080/users/registration',
                method: 'POST',
                data: {
                    "objectId": 0,
                    "email": $scope.userEmail,
                    "password": $scope.userPassword,
                    "firstName": $scope.userName,
                    "lastName": $scope.userSurname,
                    "additionalInfo": $scope.userInfo,
                    "enabled": true,
                    "role": {
                        "objectId": 0,
                        "roleName": ""
                    },
                    "phones": [
                        {
                            "objectId": 0,
                            "userId": 0,
                            "phoneNumber": $scope.userPhone
                        }
                    ]
                },
                //headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                headers: {'Content-Type': 'application/json'}
            }).then(function (data) {
                console.log(data);
                alert("Registration done!");
            }, function (response) {
                console.log(response);
                console.log("I_AM_TEAPOT!");
                alert(response.data.message);
            });
        } else {
            errorMessage = $('.errorMessage').text();
            $('.errorMessage').text(errorMessage + 'Passwords not the same!\n');
            $('.password').css({
                'color' : 'red'
            });
            alert("Passwords not the same!");
        }


    }
}]);

function eventFire(el, etype){
  if (el.fireEvent) {
    el.fireEvent('on' + etype);
  } else {
    var evObj = document.createEvent('Events');
    evObj.initEvent(etype, true, false);
    el.dispatchEvent(evObj);
  }
}

