app.controller('ordersCtrl', ['$scope', '$http', '$location', 'sharedData', 'util',
	function($scope, $http, $location, sharedData, util) {

	/* All orders */
	(function() {
		$http({
			url: sharedData.getLinks().https + '/orders',
			method: 'GET',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfOrders = data.data;
			$scope.listOfOrders.forEach(function(order) {
				order.maintenanceSum = 0;
				if (order.journalRecords != null) {
					order.journalRecords.forEach(function(record) {
						order.maintenanceSum += record.cost;
					});
				}
				order.total = order.sum + order.maintenanceSum;

			});
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}());
	/* All catefories */
	(function() {
		$http({
			url: sharedData.getLinks().https + '/categories',
			method: 'GET',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfCategories = data.data;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}());
	/* All rooms */
    (function() {
		$http({
			url: sharedData.getLinks().https + '/rooms/simpleList',
			method: 'GET',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfRooms = data.data;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}());
    /* All users */
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
	/* All maintenances */
	(function() {
		$http({
			url: sharedData.getLinks().https + '/maintenances',
			method: 'GET',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfMaintenances = data.data;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}());
	/* редирект на главную если не админ */
	(function() {
		if (!sharedData.getIsAdmin()) { $location.path('/') };
	}());

	// это для кнопок журнала использованных услуг (не удалять!)
	$scope.expand = {id : 0};


	/* Инициализация служебных переменных */
	function resetFlags() {
		$scope.added = false;
		$scope.updated = false;
		$scope.deleted = false;
	}

	$scope.paided = [true, false];
	$scope.confirmed = [true, false];
	$scope.order = {};
	$scope.newMaintenance = { count: 1};
	resetFlags();

	$scope.stage = "looking";

	$scope.pager = {
		startPaging : 0,
		objectsOnPage : 6
	}


	/* Для навешивания услуг на заказ */
	$scope.prepareToAddMaintenance = function(orderId, index) {
		$scope.newMaintenance.orderId = orderId;
		$scope.indexForOperation = index;
	}	

	$scope.addMaintenance = function() {
		console.log($scope.newMaintenance);
		$http({
			url: sharedData.getLinks().https + '/journalRecords',
			method: 'POST',
			data: {
				orderId: $scope.newMaintenance.orderId,
			    maintenanceId: $scope.newMaintenance.maintenanceId,
			    count: $scope.newMaintenance.count
			},
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$http({
				url: sharedData.getLinks().https + '/journalRecords/' + data.data.objectId,
				method: 'GET',
				headers: { 'Content-Type' : 'application/json' }
			}).then(function(data) {
				$scope.listOfOrders[$scope.indexForOperation].journalRecords.push(data.data);
			}, function(response) {
				console.log("Smth wrong!!");
				console.log(response);
			});
			$scope.newMaintenance = { count: 1 };
			$scope.stage = "looking";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}


	/* Функции подготовки запросов */
	$scope.prepareToAddOrder = function() {
		$scope.indexForOperation = "";
		$scope.order.idForOperation = "";
		$scope.order.category = "";
		$scope.order.room = "";
		$scope.order.clientId = "";
		$scope.order.isPaidFor = "";
        $scope.order.isConfirmed = "";
        $scope.order.livingStartDate = "";
        $scope.order.livingFinishDate = "";
        $scope.order.sum = "";
        $scope.order.comment = "";
		resetFlags();
		$scope.stage = "adding";
	}

	$scope.prepareToEditOrder = function(orderId, index) {
		$http({
			url: sharedData.getLinks().https + '/orders/' + orderId,
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		}).then(function(data) {
			console.log(data);
			$scope.indexForOperation = index;
			$scope.order.idForOperation = orderId;
			$scope.order.category = data.data.category.objectId;
			$scope.order.room = data.data.room.objectId;
			$scope.order.client = data.data.client;
			$scope.order.isPaidFor = data.data.isPaidFor;
            $scope.order.isConfirmed = data.data.isConfirmed;
            $scope.order.livingStartDate = data.data.livingStartDate;
            $scope.order.livingFinishDate = data.data.livingFinishDate;
            $scope.order.sum = data.data.sum;
            $scope.order.comment = data.data.comment;
			resetFlags();
			$scope.stage = "editing";
			$scope.modificationMode = true;

		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}

	$scope.prepareToDeleteOrder = function(orderId, index) {
		$scope.indexForOperation = index;
		$scope.order.idForOperation = orderId;
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
			case 'adding': addOrder();
				break;
			case 'editing': editOrder();
				break;
			case 'deleting': deleteOrder();
				break;
		}
	}

	/* Функции, выполняющие запросы */
	var addOrder = function() {
		resetFlags();
		sharedData.setUserIdForOrderFromAdmin($scope.order.clientId);
		$location.path('/book');
	}

	var editOrder = function() {
		resetFlags();
		$http({
			url: sharedData.getLinks().https + '/orders/' + $scope.order.idForOperation,
			method: 'PUT',
			data: {
				category : 			    parseInt($scope.order.category),
                room : 			        parseInt($scope.order.room),
                client : 		        $scope.order.client,
                isPaidFor :             $scope.order.isPaidFor,
                isConfirmed :           $scope.order.isConfirmed,
                livingStartDate :       $scope.order.livingStartDate,
                livingFinishDate : 		$scope.order.livingFinishDate,
                sum :                   parseInt($scope.order.sum),
                comment : 			    $scope.order.comment
			},
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfOrders[$scope.indexForOperation].category = util.getObjectInArrayById($scope.listOfCategories, $scope.order.category);
			$scope.listOfOrders[$scope.indexForOperation].room = util.getObjectInArrayById($scope.listOfRooms, $scope.order.room);
			$scope.listOfOrders[$scope.indexForOperation].client = $scope.order.client;
            $scope.listOfOrders[$scope.indexForOperation].isPaidFor = $scope.order.isPaidFor;
            $scope.listOfOrders[$scope.indexForOperation].isConfirmed = $scope.order.isConfirmed;
            $scope.listOfOrders[$scope.indexForOperation].livingStartDate = $scope.order.livingStartDate;
            $scope.listOfOrders[$scope.indexForOperation].livingFinishDate = $scope.order.livingFinishDate;
            $scope.listOfOrders[$scope.indexForOperation].sum = $scope.order.sum;
            $scope.listOfOrders[$scope.indexForOperation].comment = $scope.order.comment;
			$scope.updated = true;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
            $scope.errMessage = "serverErr";
		});
	}

	var deleteOrder = function() {
		resetFlags();
		$http({
			url: sharedData.getLinks().https + '/orders/' + $scope.order.idForOperation,
			method: 'DELETE',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfOrders.splice($scope.indexForOperation, 1);
			$scope.deleted = true;
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
            $scope.errMessage = "serverErr";
		});
	}


	/* Для листания страниц с объектами */
	$scope.nextOrders = function() {
		$scope.pager.startPaging = util.nextEntities($scope.listOfOrders.length, $scope.pager.startPaging, $scope.pager.objectsOnPage);
	}
	$scope.previousOrders = function() {
		$scope.pager.startPaging = util.previousEntities($scope.pager.startPaging, $scope.pager.objectsOnPage);
	}
}]);