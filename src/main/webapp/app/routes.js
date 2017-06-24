app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
	$locationProvider.hashPrefix('');
    $routeProvider

        // Book routes
        .when('/book', {
            templateUrl: 'app/book/book.html',
            controller: 'bookCrtl'
        })

        // Personal area routes
        // .when('/personal/settings', {
        //     templateUrl: 'user_settings.html',
        //     controller: 'userSettingsController'
        // })
        // .when('/personal/sendmessage', {
        //     templateUrl: 'send_message.html',
        //     controller: 'sendMessageController'
        // })
        // .when('/personal/orderlist', {
        //     templateUrl: 'checklist.html',
        //     controller: 'orderListController'
        // })

        // // Admin routes
        // .when('/admin/notifications', {
        //     templateUrl: 'page/notifications/notifications.html',
        //     controller: 'notificationsCtrl'
        // })
        // .when('/admin/orders', {
        //     templateUrl: 'page/orders/orders.html',
        //     controller: 'ordersCtrl'
        // })
        // .when('/admin/users', {
        //     templateUrl: 'page/users/users.html',
        //     controller: 'usersCtrl'
        // })
        // .when('/admin/rooms', {
        //     templateUrl: 'page/rooms/rooms.html',
        //     controller: 'roomsCtrl'
        // })
        // .when('/admin/blocks', {
        //     templateUrl: 'page/blocks/blocks.html',
        //     controller: 'blocksCtrl'
        // })
        // .when('/admin/maintenances', {
        //     templateUrl: 'page/maintenances/maintenances.html',
        //     controller: 'maintenancesCtrl'
        // })
        // .when('/admin/categories', {
        //     templateUrl: 'page/categories/categories.html',
        //     controller: 'categoriesCtrl'
        // })
        // .when('/admin/notificationTypes', {
        //     templateUrl: 'page/notificationTypes/notificationTypes.html',
        //     controller: 'notificationTypesCtrl'
        // })
        // .when('/admin/roomTypes', {
        //     templateUrl: 'page/roomTypes/roomTypes.html',
        //     controller: 'roomTypesCtrl'
        // })

        // otherwise
        // .when('/', {
        //     templateUrl: './app/book/book.html',
        //     controller: 'bookCtrl'
        // })
        // .otherwise({
        //     templateUrl: './room_type.html',
        //     controller: 'roomTypeController'
        // })
}]);



