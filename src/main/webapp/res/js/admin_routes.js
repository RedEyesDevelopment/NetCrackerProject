var app = angular.module('as', ['ngRoute','ngAnimate']);


app.factory('' , function(){
    var sharedData = {};

    return {
        getData: function(){
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
        .when('/admin/notifications', {
            templateUrl: 'admin_notifications.html',
            controller: 'notificationsController'
        })
        .otherwise ({templateUrl: 'admin_notifications.html'}
                     controller: 'notificationsController')
}]);

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');

    $routeProvider
        .when('/admin/notification_types', {
            templateUrl: 'admin_notification_type.html',
            controller: 'notificationTypesController'
        })
}]);

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');

    $routeProvider
        .when('/admin/orders', {
            templateUrl: 'admin_orders.html',
            controller: 'orderController'
        })
}]);

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');

    $routeProvider
        .when('/admin/rooms', {
            templateUrl: 'admin_rooms.html',
            controller: 'roomsController'
        })
}]);

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');

    $routeProvider
        .when('/admin/room_types', {
            templateUrl: 'admin_room_types.html',
            controller: 'roomTypesController'
        })
}]);

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');

    $routeProvider
        .when('/admin/block', {
            templateUrl: 'admin_block.html',
            controller: 'blockController'
        })
}]);

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');

    $routeProvider
        .when('/admin/categories', {
            templateUrl: 'admin_categories.html',
            controller: 'categoriesController'
        })
}]);

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');

    $routeProvider
        .when('/admin/maintenance', {
            templateUrl: 'admin_maintenance.html',
            controller: 'maintenanceController'
        })
}]);

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');

    $routeProvider
        .when('/admin/users', {
            templateUrl: 'admin_users.html',
            controller: 'usersController'
        })
}]);


app.controller ('notificationsController', ['$scope', '$http', '$location' , 'sharedData' , function ($scope, $http, $location, sharedData) {

    $http{{
        url: 'http://localhost:8080/admin/notifications',
        method: 'GET',

        headers: {'Content-Type': 'application/x-www-form-urlencoded'},

            }).then(function (data) {
                console.log(data);
            });
    }
}]);

app.controller ('notificationTypesController', ['$scope', '$http', '$location' , 'sharedData' , function ($scope, $http, $location, sharedData) {

    $http{{
        url: 'http://localhost:8080/admin/notification_types',
        method: 'GET',

        headers: {'Content-Type': 'application/x-www-form-urlencoded'},

            }).then(function (data) {
                console.log(data);
            });
    }
}]);
