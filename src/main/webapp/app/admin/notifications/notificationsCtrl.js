app.controller('notificationsCtrl', ['$scope', '$http', '$location', 'sharedData', 'util',
    function($scope, $http, $location, sharedData, util) {


	/* Функция на получения всех уведомлений, типов уведомлений и заказов, вызываются сразу */
	var getExecutedNotifications = function() {
		$http({
			url: sharedData.getLinks().https + '/notifications',
			method: 'GET',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfExecutedNotifications = data.data;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	};

	getExecutedNotifications();

	var getCurrentNotifications = function() {
        $http({
            url: sharedData.getLinks().https + '/notifications/current',
            method: 'GET',
            headers: { 'Content-Type' : 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.listOfNotifications = data.data;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    };

    getCurrentNotifications();

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
	/* редирект на главную если не админ */
	(function() {
		if (!sharedData.getIsAdmin()) { $location.path('/') };
	}());

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

	/* Инициализация служебных переменных */
	function resetFlags() {
		$scope.added = false;
		$scope.updated = false;
		$scope.deleted = false;
	}

	$scope.notification = {};
	resetFlags();

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
		console.log("a");
		$http({
			url: sharedData.getLinks().https + '/notifications/execute/' + $scope.idForOperation,
			method: 'PUT'
		}).then(function(data) {
			console.log("b");
			console.log(data);
			getCurrentNotifications();
			$scope.stage = "marked";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}


	/* Функции подготовки запросов */
	$scope.prepareToAddNotification = function() {
		$scope.indexForOperation = "";
		$scope.notification.idForOperation = "";
		$scope.notification.author = "";
		$scope.notification.type = "";
		$scope.notification.sendDate = "";
		$scope.notification.message = "";
		$scope.notification.order = "";
		resetFlags();
		$scope.stage = "adding";
		$scope.modificationMode = true;
	}

	$scope.prepareToEditNotification = function(notificationId, index) {
		$http({
			url: sharedData.getLinks().https + '/notifications/' + notificationId,
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		}).then(function(data) {
			console.log(data);
			$scope.indexForOperation = index;
			$scope.notification.idForOperation = notificationId;
			$scope.notification.author = data.data.author.objectId;
            $scope.notification.type = data.data.notificationType.objectId;
            $scope.notification.sendDate = data.data.sendDate;
            $scope.notification.message = data.data.message;
            $scope.notification.order = data.data.order.objectId;
			resetFlags();
			$scope.stage = "editing";
			$scope.modificationMode = true;

		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}

	$scope.prepareToDeleteNotification = function(notificationId, index) {
		$scope.indexForOperation = index;
        $scope.notification.idForOperation = notificationId;
		resetFlags();
		$scope.stage = "deleting";
	}

	/* Возврат на просмотр */
	$scope.back = function() {
		$scope.stage = "looking";
		$scope.modificationMode = false;
	}


	/* Функции, выполняющие запросы */
	var addNotification = function() {
		resetFlags();
		console.log($scope.notification.author);
		console.log($scope.notification.type);
		console.log($scope.notification.message);
		console.log($scope.notification.order);
		$http({
			url: sharedData.getLinks().https + '/notifications',
			method: 'POST',
			data: {
                authorId : 		    $scope.notification.author,
				notificationTypeId :  $scope.notification.type,
				message :           $scope.notification.message,
				orderId : 			$scope.notification.order
			},
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfNotifications.push({
				idForOperation :          data.data.objectId,
				author :            $scope.notification.author,
				notificationType :  util.getObjectInArrayById($scope.listOfNotificationTypes, $scope.notification.type),
				sendDate :          $scope.notification.sendDate,
				message :           $scope.notification.message,
				order :             util.getObjectInArrayById($scope.listOfOrders, $scope.notification.order)
			});
			$scope.prepareToAddNotification();
			$scope.added = true;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
            $scope.errMessage = "serverErr";
		});
	}

	var editNotification = function() {
		resetFlags();
        console.log($scope.notification.author);
        console.log($scope.notification.type);
        console.log($scope.notification.message);
        console.log($scope.notification.order);
		$http({
			url: sharedData.getLinks().https + '/notifications/' + $scope.notification.idForOperation,
			method: 'PUT',
			data: {
                authorId : 		    $scope.notification.author,
                notificationTypeId :  $scope.notification.type,
                message :           $scope.notification.message,
                orderId : 			$scope.notification.order
			},
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfNotifications[$scope.indexForOperation].author = util.getObjectInArrayById($scope.listOfUsers, $scope.notification.author);
			$scope.listOfNotifications[$scope.indexForOperation].type = util.getObjectInArrayById($scope.listOfNotificationTypes, $scope.notification.type);
			$scope.listOfNotifications[$scope.indexForOperation].message = $scope.notification.message;
			$scope.listOfNotifications[$scope.indexForOperation].order = util.getObjectInArrayById($scope.listOfOrders, $scope.notification.order);
			$scope.updated = true;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
            $scope.errMessage = "serverErr";
		});
	}

	var deleteNotification = function() {
		resetFlags();
		$http({
			url: sharedData.getLinks().https + '/notifications/' + $scope.notification.idForOperation,
			method: 'DELETE',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfNotifications.splice($scope.indexForOperation, 1);
			$scope.deleted = true;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
            $scope.errMessage = "serverErr";
		});
	}


	/* Для листания страниц с объектами */
	$scope.nextNotifications = function() {
		$scope.pager.startPaging = util.nextEntities($scope.listOfNotifications.length, $scope.pager.startPaging, $scope.pager.objectsOnPage);
	}
	$scope.previousNotifications = function() {
		$scope.pager.startPaging = util.previousEntities($scope.pager.startPaging, $scope.pager.objectsOnPage);
	}
}]);