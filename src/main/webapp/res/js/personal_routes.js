/**
 * Created by Arizel on 06.06.2017.
 */
var app = angular.module('ps', ['ngRoute', 'ngAnimate']);


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
        .when('/settings', {
            templateUrl: 'user_settings.html',
            controller: 'userSettingsController'
        })
        .otherwise({
            templateUrl: 'user_settings.html',
            controller: 'userSettingsController'
        })
}]);

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');

    $routeProvider
        .when('/sendmessage', {
            templateUrl: 'send_message.html',
            controller: 'sendMessageController'
        })
}]);

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');

    $routeProvider
        .when('/orderlist', {
            templateUrl: 'checklist.html',
            controller: 'orderListController'
        })
}]);


app.controller('userSettingsController', ['$scope', '$http', '$location' , 'sharedData', '$timeout', function ($scope, $http, $location, sharedData, $timeout) {
    //$scope.userFlag = true;

    var user;

    $http({
        url: 'http://localhost:8080/users/myself',
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },

    }).then(function (data) {
        $scope.yourData = data;
        user = data;
        console.log(data);
    });


    $scope.updateUser = function () {
        console.log('I am into user update');
        console.log(user.data.objectId);
        console.log(document.getElementById("userEmail").innerHTML);
        console.log(document.getElementById("userPassword").innerHTML);
        console.log(document.getElementById("userFirstName").innerHTML);
        console.log(document.getElementById("userLastName").innerHTML);
        console.log(user.data.additionalInfo);
        console.log(user.data.enabled);
        console.log(user.data.role.roleName);
        console.log('I am into user update after');
        console.log('http://localhost:8080/users/' + user.data.objectId)

        $http({
            url: 'http://localhost:8080/users/' + user.data.objectId,
            method: 'PUT',
            data: {
                "objectId": user.data.objectId,
                "email": document.getElementById("userEmail").innerHTML,
                "password": document.getElementById("userPassword").innerHTML,
                "firstName": document.getElementById("userFirstName").innerHTML,
                "lastName": document.getElementById("userLastName").innerHTML,
                "additionalInfo": user.data.additionalInfo,
                "enabled": user.data.enabled,
                "role": {
                    "objectId": user.data.role.objectId,
                    "roleName": user.data.role.roleName
                },
                "phones": [
                    {
                        "objectId": user.data.phones[0].objectId,
                        "userId": user.data.phones[0].userId,
                        "phoneNumber": user.data.phones[0].phoneNumber
                    },
                    {
                        "objectId": user.data.phones[0].objectId,
                        "userId": user.data.phones[0].userId,
                        "phoneNumber": user.data.phones[0].phoneNumber
                    }
                ]
            },


            headers: {
                'Content-Type': 'application/json'
            }


        }).then(function(data) {

            if (data.data.successful == true) $scope.wasUpdatedUser = true;
            //$scope.wasUpdatedUser = false;

            $timeout(function () {
                console.log($scope.wasUpdatedUser)

                $scope.wasUpdatedUser = false;

            }, 5000);

            console.log(data.data);

        }, function(response) {
            if (response.data.successful == false) $scope.notUpdatedUser = true;

            $timeout(function () {
                console.log($scope.wasUpdatedUser)

                $scope.notUpdatedUser = false;

            }, 5000);

            $scope.messageUpdate = response.data.message;
            console.log(response);
            console.log("I_AM_TEAPOT!")
        });


    }





    $(document).ready(function(){
        $("#loginLink").click(function(){
            $('#loginModal').modal('show');
        });

        $("#signupLink").click(function(){
            $('#signupModal').modal('show');
        });


        //EDIT FIELDS

        $.fn.inlineEdit = function(replaceWith, connectWith) {

            $(this).hover(function() {
                $(this).addClass('hover');
            }, function() {
                $(this).removeClass('hover');
            });

            $(this).click(function() {

                var elem = $(this);

                elem.hide();
                elem.after(replaceWith);
                replaceWith.focus();

                replaceWith.blur(function() {

                    if ($(this).val() != "") {
                        connectWith.val($(this).val()).change();
                        elem.text($(this).val());
                    }

                    $(this).remove();
                    elem.show();
                });
            });
        };

        var replaceWith = $('<input name="temp" type="text"  />'),
            connectWith = $('input[name="hiddenField"]');

        $('.changable').inlineEdit(replaceWith, connectWith);

        //END EDIIT

        //ADD NUMBER
        $('.multi-number-wrapper').each(function() {
            var $wrapper = $('.multi-numbers', this);
            $(".add-number", $(this)).click(function(e) {
                if ($('.multi-number', $wrapper).length < 5)
                    $('.multi-number:first-child', $wrapper).clone(true).appendTo($wrapper).find('p').val('').focus();
            });
            $('.multi-number .remove-number', $wrapper).click(function() {
                if ($('.multi-number', $wrapper).length > 1)
                    $(this).parent('.multi-number').remove();
            });
        });
        //END NUMBER

    });




}]);



app.controller('orderListController', ['$scope', '$http', 'sharedData' , function ($scope, $http, sharedData) {

    $http({
        url: 'http://localhost:8080/orders/byUser',
        method: 'GET',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},

    }).then(function (data) {
        $scope.yourData = data;
        console.log(data);
    });


}]);


app.controller('sendMessageController', ['$scope', '$http', 'sharedData', '$document', '$timeout', function ($scope, $http, sharedData, $document, $timeout) {

    $scope.onSendMessage = function () {
        console.log("I am here!");

        $http({
            url: 'http://localhost:8080/users/sendMessage',
            method: 'POST',
            data: {
                "themeMessage": $scope.themeMessage,
                "message": $scope.message
            },

        }).then(function (data) {
            $scope.yourData = data;

            if(data.data.successful == true) {
                $scope.succesfulSentMessage = true;
                $scope.themeMessage = "";
                $scope.message = "";

                $timeout(function () {
                    console.log($scope.succesfulSentMessage)

                    $scope.succesfulSentMessage = false;

                }, 5000);
            }

        }, function (response) {
            if (response.data.successful == false) {
                $scope.notSentMessage = true;

                $timeout(function () {

                    $scope.notSentMessage = false;

                }, 5000);

                $scope.messageResponse = response.data.message;
            }
        });

    }

}]);