app.controller('notificationsCtrl', ['$scope', '$http', '$location', 'sharedData', 'util',
    function($scope, $http, $location, sharedData, util) {
	
	var getExecutedNotifications = function() {
		$http({
			url: sharedData.getLinks().https + '/notifications',
			method: 'GET',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfExecNotifications = data.data;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	};
	// Получаем все выполненные
	getExecutedNotifications();

	var getCurrentNotifications = function() {
        $http({
            url: sharedData.getLinks().https + '/notifications/current',
            method: 'GET',
            headers: { 'Content-Type' : 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.listOfCurrNotifications = data.data;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    };
	// Получаем все НЕЕЕЕЕЕЕ выполненные
    getCurrentNotifications();

    // All users
	(function() {
		$http({
			url: sharedData.getLinks().https + '/users',
			method: 'GET',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfUsers = data.data;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}());
	// All notification type
	(function() {
		$http({
			url: sharedData.getLinks().https + '/notificationtypes',
			method: 'GET',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfNotificationTypes = data.data;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}());
	// All orders
	(function() {
		$http({
			url: sharedData.getLinks().https + '/orders',
			method: 'GET',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfOrders = data.data;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}());
	/* редирект на главную если не админ и не рецепция */
	(function() {
		if (!sharedData.getIsAdmin() && !sharedData.getIsReception()) { $location.path('/') };
	}());

	$scope.isAdmin = sharedData.getIsAdmin();

	/* Возврат на просмотр */
	$scope.back = function() {
		$scope.stage = "looking";
		$scope.modificationMode = false;
	}

	$scope.updateList = function() {
		getCurrentNotifications();
	}

	$scope.query = function() {
		switch ($scope.stage) {
			case 'markAsDone': markAsDone();
				break;
			case 'adding': addNotification();
				break;
			case 'editing': editNotification();
				break;
			case 'deleting': deleteNotification();
				break;
		}
	}

	$scope.notification = {};

	$scope.stage = "looking";

	$scope.pager = {
		startPaging : 0,
		objectsOnPage : 6
	}

	$scope.prepareToMarkAsDone = function(notifId) {
		$scope.idForOperation = notifId;
		$scope.stage = "markAsDone";
	}

	var markAsDone = function() {
		console.log($scope.idForOperation);
		$http({
			url: sharedData.getLinks().https + '/notifications/execute/' + $scope.idForOperation,
			method: 'PUT'
		}).then(function(data) {
			console.log(data);
			getCurrentNotifications();
			$scope.stage = "marked";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}


	$scope.prepareToAddNotification = function() {
		$scope.idForOperation = "";
		$scope.indexForOperation = "";
		$scope.notification = {};
		$scope.stage = "adding";
		$scope.modificationMode = true;
	}

	var addNotification = function() {
		$http({
			url: sharedData.getLinks().https + '/notifications',
			method: 'POST',
			data: {
                authorId: 		    sharedData.getMyself().objectId,
				notificationTypeId: $scope.notification.typeId,
				message:           	$scope.notification.message,
				orderId: 			$scope.notification.orderId
			},
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			getCurrentNotifications();
			$scope.prepareToAddNotification();
			$scope.stage = "added";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
            $scope.errMessage = "serverErr";
		});
	}


	$scope.prepareToEditNotification = function(notificationId, index) {
		$http({
			url: sharedData.getLinks().https + '/notifications/notexecuted/' + notificationId,
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		}).then(function(data) {
			console.log(data);
			$scope.idForOperation = notificationId;
			$scope.indexForOperation = index;
			$scope.notification.authorId = sharedData.getMyself().objectId;
            $scope.notification.typeId = data.data.notificationType.objectId;
            $scope.notification.message = data.data.message;
            $scope.notification.orderId = data.data.order.objectId;
            $scope.stage = "editing";
			$scope.modificationMode = true;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}

	var editNotification = function() {
		$http({
			url: sharedData.getLinks().https + '/notifications/' + $scope.idForOperation,
			method: 'PUT',
			data: {
                authorId: 		    sharedData.getMyself().objectId,
				notificationTypeId: $scope.notification.typeId,
				message:           	$scope.notification.message,
				orderId: 			$scope.notification.orderId
			},
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			getCurrentNotifications();
			$scope.stage = "edited";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
            $scope.errMessage = "serverErr";
		});
	}


	$scope.prepareToDeleteNotification = function(notificationId, index) {
        $scope.idForOperation = notificationId;
		$scope.indexForOperation = index;
		$scope.stage = "deleting";
	}

	var deleteNotification = function() {
		$http({
			url: sharedData.getLinks().https + '/notifications/' + $scope.idForOperation,
			method: 'DELETE',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			getCurrentNotifications();
			$scope.stage = "deleted";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
            $scope.errMessage = "serverErr";
		});
	}


	/* Для листания страниц с объектами */
	$scope.nextNotifications = function() {
		$scope.pager.startPaging = util.nextEntities($scope.listOfCurrNotifications.length, $scope.pager.startPaging, $scope.pager.objectsOnPage);
	}
	$scope.previousNotifications = function() {
		$scope.pager.startPaging = util.previousEntities($scope.pager.startPaging, $scope.pager.objectsOnPage);
	}
}]);