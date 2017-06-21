var adminDesktop = angular.module('adminDesktop', ['ngRoute', 'ngAnimate', 'ngResource']);




adminDesktop.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
	$locationProvider.hashPrefix('');
    $routeProvider
        .when('/notifications', {
            templateUrl: 'page/notifications/notifications.html',
            controller: 'notificationsCtrl'
        });
}]);

adminDesktop.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
	$locationProvider.hashPrefix('');
    $routeProvider
        .when('/orders', {
            templateUrl: 'page/orders/orders.html',
            controller: 'ordersCtrl'
        });
}]);

adminDesktop.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
	$locationProvider.hashPrefix('');
    $routeProvider
        .when('/users', {
            templateUrl: 'page/users/users.html',
            controller: 'usersCtrl'
        });
}]);

adminDesktop.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
	$locationProvider.hashPrefix('');
    $routeProvider
        .when('/rooms', {
            templateUrl: 'page/rooms/rooms.html',
            controller: 'roomsCtrl'
        });
}]);

adminDesktop.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
	$locationProvider.hashPrefix('');
    $routeProvider
        .when('/blocks', {
            templateUrl: 'page/blocks/blocks.html',
            controller: 'blocksCtrl'
        });
}]);

adminDesktop.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
	$locationProvider.hashPrefix('');
    $routeProvider
        .when('/maintenances', {
            templateUrl: 'page/maintenances/maintenances.html',
            controller: 'maintenancesCtrl'
        });
}]);

adminDesktop.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
	$locationProvider.hashPrefix('');
    $routeProvider
        .when('/categories', {
            templateUrl: 'page/categories/categories.html',
            controller: 'categoriesCtrl'
        });
}]);

adminDesktop.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
	$locationProvider.hashPrefix('');
    $routeProvider
        .when('/notificationTypes', {
            templateUrl: 'page/notificationTypes/notificationTypes.html',
            controller: 'notificationTypesCtrl'
        });
}]);

adminDesktop.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
	$locationProvider.hashPrefix('');
    $routeProvider
        .when('/roomTypes', {
            templateUrl: 'page/roomTypes/roomTypes.html',
            controller: 'roomTypesCtrl'
        });
}]);




 

















