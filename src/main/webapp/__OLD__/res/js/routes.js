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

app.controller('search-available', ['$scope', '$rootScope', '$http', '$location' , 'sharedData', '$document', function
        ($scope, $rootScope, $http, $location, sharedData, $document) {


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

    $scope.bookViewChange = function() {
        $rootScope.doesNeedToShowRoomType = false;
        $location.path('/');
    }

}]);

app.controller('roomTypeController', ['$scope', '$rootScope', '$http', 'sharedData', '$location' , function
    ($scope, $rootScope, $http, sharedData, $location) {

    $scope.$watchCollection(
        function () {
            return MapService.sharedData;
        }, function (newVal, oldVal, scope) {
            // the `scope` parameter refers to the current scope
            scope.sharedData = MapService.sharedData
        }
    )

    console.log("in room type controller");

    var book = sharedData.getData();

    $rootScope.doesNeedToShowRoomType = true;

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

app.controller('finishOrderController', ['$scope', '$rootScope', '$http', 'sharedData', '$location' , function ($scope, $rootScope, $http, sharedData, $location) {

    $scope.finishOrder = sharedData.getData().data;
    $scope.auth = window.auth;
    $rootScope.doesNeedToShowFinishOrder = true;

    $scope.book = function() {
        $http({
            url: 'http://localhost:8080/orders/accept',
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(function(data) {
            sharedData.setData(data);
            $location.path('/fineBooked');
            console.log(data);
        }, function(response) {
            console.log("We are in response");
        });
    }

    $scope.cancel = function() {
        $http({
            url: 'http://localhost:8080/orders/cancel',
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(function(data) {
            $rootScope.doesNeedToShowFinishOrder = false;
            $location.path('/rooms');
            console.log(data);
        }, function(response) {
            console.log("We are in response");
        });
    }

}]);

app.controller('fineBookedController', ['$scope', '$rootScope', '$http', 'sharedData', '$location' , function ($scope, $rootScope, $http, sharedData, $location) {

    $scope.finishOrderController = sharedData.getData().data;
    $scope.auth = window.auth;

    $scope.thanks = function() {
        $rootScope.doesNeedToShowFinishOrder = false;
        $location.path('/');
    }

}]);

app.controller('registration', ['$scope', '$http', '$location' , 'sharedData', function ($scope, $http, $location, sharedData) {

    $scope.submit = function(eve) {

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


        $('.windowRegistration').height(390);
        $('.errorMessage').hide();
        $('.passwordLabel').css({
            'color' : 'black'
        });
        $('.emailLabel').css({
            'color' : 'black'
        });

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
                $('.emailLabel').css({
                    'color' : 'red'
                });
                $('.windowRegistration').height(420);
                $('.errorMessage').text(response.data.message).show(300);
            });
        } else {
            $('.passwordLabel').css({
                'color' : 'red'
            });
            $('.errorMessage').text('Passwords not the same!').show(300);
            $('.windowRegistration').height(420);
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

