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
	/* All room types */
    (function() {
		$http({
			url: sharedData.getLinks().https + '/roomTypes/simpleList',
			method: 'GET',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.listOfRoomTypes = data.data;
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
	/* Residents */
	(function() {
		$scope.listOfResidents = [1, 2, 3];
	}());
	/* редирект на главную если не админ */
	(function() {
		if (!sharedData.getIsAdmin()) { $location.path('/') };
	}());

	// это для кнопок журнала использованных услуг (не удалять!)
	$scope.expand = {id : 0};


	$scope.order = {};
	$scope.confirmed = {};
	$scope.newMaintenance = { count: 1};

	$scope.stage = "looking";
	$scope.mode = "look";

	$scope.pager = {
		startPaging : 0,
		objectsOnPage : 6
	}

	/* Возврат на просмотр */
	$scope.back = function() {
		$scope.stage = "looking";
		$scope.mode = "look";
	}


	/* Вызывает нужный запрос в зависимости от типа операции */
	$scope.query = function() {
		switch ($scope.stage) {
			case 'switch': switchPaidConfirmed();
				break;
			case 'adding': addOrder();
				break;
			case 'searchForEdit': searchForEdit();
				break;
			case 'deleting': deleteOrder();
				break;
		}
	}


	/* Изменение состояний paid | confirmed */
	$scope.prepareToSwitch = function(orderId, index, isPaidFor, isConfirmed) {
		$scope.idForOperation = orderId;
		$scope.indexForOperation = index;
		$scope.confirmed.isPaidFor = isPaidFor;
		$scope.confirmed.isConfirmed = isConfirmed;
		$scope.stage = "switch";
	}

	var switchPaidConfirmed = function() {
		console.log("confirmed: ");
		console.log($scope.confirmed);
		$http({
			url: sharedData.getLinks().https + '/orders/admin/confirm/' + $scope.idForOperation,
			method: 'PUT',
			data: {
				isPaidFor: $scope.confirmed.isPaidFor,
				isConfirmed: $scope.confirmed.isConfirmed
			},
			headers: {'Content-Type' : 'application/json'}
		}).then(function(data) {
			console.log(data);
			console.log("confirmed: ");
			console.log($scope.confirmed);
			$scope.listOfOrders[$scope.indexForOperation].isPaidFor = $scope.confirmed.isPaidFor;
			$scope.listOfOrders[$scope.indexForOperation].isConfirmed = $scope.confirmed.isConfirmed;
			$scope.stage = "looking";
			$scope.mode = "look";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
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


	/* Добавление заказа */
	$scope.prepareToAddOrder = function() {
		$scope.orderId = undefined;
		$scope.indexForOperation = undefined;
		$scope.order = {}
		$scope.stage = "adding";
		$scope.mode = "add";
	}

	var addOrder = function() {
		resetFlags();
		sharedData.setUserIdForOrderFromAdmin($scope.order.clientId);
		$location.path('/book');
	}

	/* Изменение заказа */
	$scope.prepareToEditOrder = function(orderId, index) {
		$http({
			url: sharedData.getLinks().https + '/orders/' + orderId,
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		}).then(function(data) {
			console.log(data);
			$scope.indexForOperation = index;
			$scope.idForOperation = orderId;
            $scope.order.livingStartDate = new Date(data.data.livingStartDate);
            $scope.order.livingFinishDate = new Date(data.data.livingFinishDate);
			$scope.order.room = data.data.room;
			$scope.order.category = data.data.category;
            $scope.order.comment = data.data.comment;
			$scope.stage = "searchForEdit";
			$scope.mode = "edit";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}

	var searchForEdit = function() {
		data = {
			numberOfResidents: 	$scope.order.room.numberOfResidents,
			roomTypeId: 		$scope.order.room.roomType.objectId,
			categoryId: 		$scope.order.category.objectId,
			livingStartDate: 	$scope.order.livingStartDate.getTime(),
			livingFinishDate: 	$scope.order.livingFinishDate.getTime(),
			roomId: 			$scope.order.room.objectId
		};
		console.log(data);
		$http({
			url: sharedData.getLinks().https + '/orders/updateinfo/' + $scope.idForOperation,
			method: 'PUT',
			data: {
				numberOfResidents: 	$scope.order.room.numberOfResidents,
				roomTypeId: 		$scope.order.room.roomType.objectId,
				categoryId: 		$scope.order.category.orderId,
				livingStartDate: 	$scope.order.livingStartDate.getTime(),
				livingFinishDate: 	$scope.order.livingFinishDate.getTime(),
				roomId: 			$scope.order.room.objectId
			},
			headers: {'Content-Type' : 'application/json'}
		}).then(function(data) {
			console.log(data);
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}

	// private int roomTypeId;			~
 //    private String roomTypeName;
 //    private String roomTypeDescription;
 //    private boolean isAvailable;
 //    private long categoryCost;	~
 //    private long livingCost;		+
 //    private Date arrival;		+
 //    private Date departure;		+
 //    private int livingPersons;
 //    private int categoryId;
 //    private Integer clientId;   	+
 //    private Integer roomId;




	$scope.prepareToDeleteOrder = function(orderId, index) {
		$scope.indexForOperation = index;
		$scope.order.idForOperation = orderId;
		resetFlags();
		$scope.stage = "deleting";
	}




	/* Функции, выполняющие запросы */


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