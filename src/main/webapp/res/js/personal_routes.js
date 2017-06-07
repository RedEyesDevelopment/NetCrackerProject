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


app.controller('userSettingsController', ['$scope', '$http', '$location' , 'sharedData' , function ($scope, $http, $location, sharedData) {

    var user;

    $http({
        url: 'http://localhost:8080/users/901',
        method: 'GET',

        headers: {'Content-Type': 'application/x-www-form-urlencoded'},

    }).then(function (data) {
        $scope.yourData = data;
        user = data;
        console.log(data);
    });


    $scope.updateUser = function (eve) {
        console.log('I am into user update');

        $http({
            url: 'http://localhost:8080/users/901',
            method: 'PUT',
            data: {
                "objectId": 901,
                "email": "krasavchik@gmail.com",
                "password": "lkjaytuayiaaotwiuy",
                "firstName": "Johnny",
                "lastName": "Depp",
                "additionalInfo": "Капитан!",
                "enabled": true,
                "role": {
                    "objectId": 3,
                    "roleName": "CLIENT"
                },
                "phones": [
                    {
                        "objectId": 1102,
                        "userId": 901,
                        "phoneNumber": "08001234518"
                    },
                    {
                        "objectId": 1101,
                        "userId": 901,
                        "phoneNumber": "08001234517"
                    }
                ]
            },


            headers: {
                'Content-Type': 'application/json'
            }


        }).then(function(data) {

            sharedData.setData(data);


            $location.path('/rooms');


        }, function(response) {
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


//
// app.controller('roomTypeController', ['$scope', '$http', 'sharedData' , function ($scope, $http, sharedData) {
//
//     var book = sharedData.getData();
//
//     $scope.list = book.data;
//     window.scrollTo(0,1000);
//
//     $scope.bookApartment = function (id) {
//         console.log(id);
//     }
//
// }]);