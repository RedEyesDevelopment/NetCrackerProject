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

//app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
//    $locationProvider.hashPrefix('');
//
//    $routeProvider
//        .when('/admin_list', {
//            templateUrl: 'admin_notifications.html',
//            controller: 'listController'
//        })
//}]);

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');

    $routeProvider
        .when('/admin_notifications', {
            templateUrl: 'admin_notifications.html',
            controller: 'notificationsController'
        })
        .otherwise ({templateUrl: 'admin_notifications.html',
                     controller: 'notificationsController'})
}]);

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');

    $routeProvider
        .when('/admin_notification_types', {
            templateUrl: 'admin_notification_type.html',
            controller: 'notificationTypesController'
        })
}]);

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');

    $routeProvider
        .when('/admin_orders', {
            templateUrl: 'admin_orders.html',
            controller: 'orderController'
        })
}]);

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');

    $routeProvider
        .when('/admin_rooms', {
            templateUrl: 'admin_rooms.html',
            controller: 'roomsController'
        })
}]);

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');

    $routeProvider
        .when('/admin_room_types', {
            templateUrl: 'admin_room_types.html',
            controller: 'roomTypesController'
        })
}]);

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');

    $routeProvider
        .when('/admin_block', {
            templateUrl: 'admin_block.html',
            controller: 'blockController'
        })
}]);

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');

    $routeProvider
        .when('/admin_categories', {
            templateUrl: 'admin_categories.html',
            controller: 'categoriesController'
        })
}]);

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');

    $routeProvider
        .when('/admin_maintenance', {
            templateUrl: 'admin_maintenance.html',
            controller: 'maintenanceController'
        })
}]);

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');

    $routeProvider
        .when('/admin_users', {
            templateUrl: 'admin_users.html',
            controller: 'usersController'
        })
}]);

//app.controller('listController', ['$scope', '$http', 'sharedData' , function ($scope, $http, sharedData) {
//
//    var entity = sharedData.getData();
//
//    $scope.list = entity.data;
//
//}]);

app.controller ('notificationsController', ['$scope', '$http', '$location' , 'sharedData' , function ($scope, $http, $location, sharedData) {

    var notification;
    $http({
        url: 'http://localhost:8080/notification',
        method: 'GET',

        headers: {'Content-Type': 'application/x-www-form-urlencoded'},

            }).then(function (response) {

                    $scope.yourData = response;
                    $scope.author = response.data.author.firstName + " " + response.data.author.lastName;
                    $scope.notificationType = response.data.notificationType.notificationTypeTittle;
                    $scope.message = response.data.message;
                    $scope.sendDate = response.data.sendDate;
                    $scope.executedBy = response.data.executedBy;
                    $scope.executedDate = response.data.executedDate;
                    $scope.order = response.data.order;

                    console.log(response);
            }, function(response){
                console.log(response);
            });

}]);

app.controller ('notificationTypesController', ['$scope', '$http', '$location' , 'sharedData' , function ($scope, $http, $location, sharedData) {

    $http({
        url: 'http://localhost:8080/admin_notification_types',
        method: 'GET',

        headers: {'Content-Type': 'application/x-www-form-urlencoded'},

            }).then(function (data) {
                console.log(data);
            });

}]);
