app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
	$locationProvider.hashPrefix('');
    $routeProvider

        // Book route
        .when('/book', {
            templateUrl: 'app/book/book.html',
            controller: 'bookCrtl'
        })

        // Personal area routes
        .when('/personal/settings', {
            templateUrl: 'app/personalArea/settings/settings.html',
            controller: 'settingsCtrl'
        })
        .when('/personal/messageToAdmin', {
            templateUrl: 'app/personalArea/messageToAdmin/messageToAdmin.html',
            controller: 'messageToAdminCtrl'
        })
        .when('/personal/checklist', {
            templateUrl: 'app/personalArea/checklist/checklist.html',
            controller: 'checklistCtrl'
        })

        // // Admin routes
        .when('/admin/statistic', {
            templateUrl: 'app/admin/statistic/statistic.html',
            controller: 'statisticCtrl'
        })
        .when('/admin/notifications', {
            templateUrl: 'app/admin/notifications/notifications.html',
            controller: 'notificationsCtrl'
        })
        .when('/admin/orders', {
            templateUrl: 'app/admin/orders/orders.html',
            controller: 'ordersCtrl'
        })
        .when('/admin/users', {
            templateUrl: 'app/admin/users/users.html',
            controller: 'usersCtrl'
        })
        .when('/admin/rooms', {
            templateUrl: 'app/admin/rooms/rooms.html',
            controller: 'roomsCtrl'
        })
        .when('/admin/blocks', {
            templateUrl: 'app/admin/blocks/blocks.html',
            controller: 'blocksCtrl'
        })
        .when('/admin/maintenances', {
            templateUrl: 'app/admin/maintenances/maintenances.html',
            controller: 'maintenancesCtrl'
        })
        .when('/admin/categories', {
            templateUrl: 'app/admin/categories/categories.html',
            controller: 'categoriesCtrl'
        })
        .when('/admin/notificationTypes', {
            templateUrl: 'app/admin/notificationTypes/notificationTypes.html',
            controller: 'notificationTypesCtrl'
        })
        .when('/admin/roomTypes', {
            templateUrl: 'app/admin/roomTypes/roomTypes.html',
            controller: 'roomTypesCtrl'
        })

        // otherwise
        .when('/', {
            templateUrl: 'app/book/book.html',
            controller: 'bookCrtl'
        })
        .otherwise({
            templateUrl: 'app/book/book.html',
            controller: 'bookCrtl'
        })
}]);



