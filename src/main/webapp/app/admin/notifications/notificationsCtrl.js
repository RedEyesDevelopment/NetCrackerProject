app.controller('notificationsCtrl', ['$scope', '$http', '$location', 'sharedData', 'util',
    function($scope, $http, $location, sharedData, util) {


	/* Функция на получения всех уведомлений, типов уведомлений и заказов, вызываются сразу */
	(function() {
		$http({
			url: sharedData.getLinks().https + '/notifications',
			method: 'GET',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfNotifications = data.data;
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
			$scope.notification.author = data.data.author;
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

	/* Вызывает нужный запрос в зависимости от типа операции */
	$scope.query = function() {
		switch ($scope.stage) {
			case 'adding': addNotification();
				break;
			case 'editing': editNotification();
				break;
			case 'deleting': deleteNotification();
				break;
		}
	}

	/* Функции, выполняющие запросы */
	var addNotification = function() {
		resetFlags();
		$http({
			url: sharedData.getLinks().https + '/notifications',
			method: 'POST',
			data: {
                author : 		    $scope.notification.author,
				notificationType :  parseInt($scope.notification.type),
				sendDate :          $scope.notification.sendDate,
				message :           $scope.notification.message,
				order : 			parseInt($scope.notification.order)
			},
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfNotifications.push({
				objectId :          data.data.objectId,
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
		$http({
			url: sharedData.getLinks().https + '/rooms/' + $scope.room.idForOperation,
			method: 'PUT',
			data: {
				author : 		    $scope.notification.author,
                notificationType :  parseInt($scope.notification.type),
                sendDate :          $scope.notification.sendDate,
                message :           $scope.notification.message,
                order : 			parseInt($scope.notification.order)
			},
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfRooms[$scope.indexForOperation].roomNumber = $scope.room.number;
			$scope.listOfRooms[$scope.indexForOperation].numberOfResidents = $scope.room.numberOfResidents;
			$scope.listOfRooms[$scope.indexForOperation].roomType = util.getObjectInArrayById($scope.listOfRoomTypes, $scope.room.type);
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
			url: sharedData.getLinks().https + '/rooms/' + $scope.room.idForOperation,
			method: 'DELETE',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfRooms.splice($scope.indexForOperation, 1);
			$scope.deleted = true;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
            $scope.errMessage = "serverErr";
		});
	}


	/* Для листания страниц с объектами */
	$scope.nextRooms = function() {
		$scope.pager.startPaging = util.nextEntities($scope.listOfRooms.length, $scope.pager.startPaging, $scope.pager.objectsOnPage);
	}
	$scope.previousRooms = function() {
		$scope.pager.startPaging = util.previousEntities($scope.pager.startPaging, $scope.pager.objectsOnPage);
	}
}]);