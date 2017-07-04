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
			$scope.originListOfOrders = data.data;
			$scope.originListOfOrders.forEach(function(order) {
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

	/* редирект на главную если не админ и не рецепция */
	(function() {
		if (!sharedData.getIsAdmin() && !sharedData.getIsReception()) { $location.path('/') };
	}());

	$scope.isAdmin = sharedData.getIsAdmin();

	// это для кнопок журнала использованных услуг (не удалять!)
	$scope.expand = {id : 0};


	$scope.order = {};
	$scope.confirmed = {};
	$scope.newMaintenance = { count: 1};
	$scope.anotherVariant = {};

	$scope.maxDate = new Date();
	$scope.maxDate.setFullYear(new Date().getFullYear() + 1);

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
			case 'deleteJournalRecord': deleteJournalRecord();
				break;
			case 'adding': addOrder();
				break;
			case 'choosingAnotherVariant': editOrder();
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
			$scope.originListOfOrders[$scope.indexForOperation].isPaidFor = $scope.confirmed.isPaidFor;
			$scope.originListOfOrders[$scope.indexForOperation].isConfirmed = $scope.confirmed.isConfirmed;
			$scope.originListOfOrders[$scope.indexForOperation].lastModificator = util.getObjectInArrayById($scope.listOfUsers, sharedData.getMyself().objectId);
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
				$scope.originListOfOrders[$scope.indexForOperation].journalRecords.push(data.data);
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

	$scope.prepareToDeleteJournalRecord = function(journalRecordId, orderIndex, journalRecordIndex) {
		$scope.idForOperation = journalRecordId;
		$scope.indexForOperation = orderIndex;
		$scope.indexJournalForOperation = journalRecordIndex;
		$scope.stage = "deleteJournalRecord";
	}

	var deleteJournalRecord = function() {
		$http({
			url: sharedData.getLinks().https + '/journalRecords/' + $scope.idForOperation,
			method: 'DELETE',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.originListOfOrders[$scope.indexForOperation].journalRecords.splice($scope.indexJournalForOperation, 1);
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}


	/* Добавление заказа */
	$scope.prepareToAddOrder = function() {
		$scope.idForOperation = undefined;
		$scope.indexForOperation = undefined;
		$scope.order = {}
		$scope.stage = "adding";
		$scope.mode = "add";
	}

	var addOrder = function() {
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
            $scope.order.client = data.data.client;
            $scope.order.sum = data.data.sum;
			$scope.stage = "searchForEdit";
			$scope.mode = "edit";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
		});
	}

	$scope.searchForEdit = function() {
        if ($scope.order.livingStartDate.getTime() < $scope.order.livingFinishDate.getTime()
            && $scope.order.livingStartDate.getTime() >= new Date().getTime()
            && $scope.order.livingFinishDate.getTime() <= $scope.maxDate.getTime()) {

            $http({
                url: sharedData.getLinks().https + '/orders/updateinfo/' + $scope.idForOperation,
                method: 'PUT',
                data: {
                    numberOfResidents: 	$scope.order.room.numberOfResidents,
                    roomTypeId: 		$scope.order.room.roomType.objectId,
                    categoryId: 		$scope.order.category.objectId,
                    livingStartDate: 	$scope.order.livingStartDate.getTime(),
                    livingFinishDate: 	$scope.order.livingFinishDate.getTime(),
                    roomId: 			$scope.order.room.objectId
                },
                headers: {'Content-Type' : 'application/json'}
            }).then(function(data) {
                console.log(data);
                if (data.data.rooms !== null) {
                    $scope.anotherVariant = data.data;
                    $scope.stage = "choosingAnotherVariant";
                } else {
                    $scope.stage = "noAvailable";
                }
            }, function(response) {
                console.log("Smth wrong!!");
                console.log(response);
            });
        } else {
            $scope.errMessage = "invalidInputData";
        }
	}

	$scope.backTochoosingAnotherVariants = function() {
		$scope.stage = "searchForEdit";
	}

	var editOrder = function() {
        if ($scope.order.livingStartDate.getTime() < $scope.order.livingFinishDate.getTime()
            && $scope.order.livingStartDate.getTime() >= new Date().getTime()
            && $scope.order.livingFinishDate.getTime() <= $scope.maxDate.getTime()) {

            $http({
                url: sharedData.getLinks().https + '/orders/admin/update/' + $scope.idForOperation,
                method: 'PUT',
                data: {
                    roomTypeId: null,
                    roomTypeName: null,
                    roomTypeDescription: null,
                    isAvailable: null,
                    categoryCost: $scope.anotherVariant.categoryCost,
                    livingCost: $scope.anotherVariant.livingCost,
                    arrival: $scope.order.livingStartDate.getTime(),
                    departure: $scope.order.livingFinishDate.getTime(),
                    livingPersons: null,
                    categoryId: null,
                    clientId: $scope.order.client.objectId,
                    roomId: $scope.order.room.objectId
                },
                headers: {'Content-Type': 'application/json'}
            }).then(function (data) {
                console.log(data);
                $scope.originListOfOrders[$scope.indexForOperation].livingStartDate = $scope.order.livingStartDate;
                $scope.originListOfOrders[$scope.indexForOperation].livingFinishDate = $scope.order.livingFinishDate;
                $scope.originListOfOrders[$scope.indexForOperation].sum = $scope.anotherVariant.total;
                $scope.originListOfOrders[$scope.indexForOperation].category = util.getObjectInArrayById($scope.listOfCategories, $scope.order.category.objectId);
                $scope.originListOfOrders[$scope.indexForOperation].room = util.getObjectInArrayById($scope.listOfRooms, $scope.order.room.objectId);
                $scope.originListOfOrders[$scope.indexForOperation].lastModificator = util.getObjectInArrayById($scope.listOfUsers, sharedData.getMyself().objectId);
                $scope.stage = "updated";
                $scope.mode = "look";
            }, function (response) {
                console.log("Smth wrong!!");
                console.log(response);
                $scope.errMessage = "serverErr";
            });
        } else {
            $scope.errMessage = "invalidInputData";
        }
	}

	$scope.prepareToDeleteOrder = function(orderId, index) {
		$scope.idForOperation = orderId;
		$scope.indexForOperation = index;
		$scope.stage = "deleting";
	}

	var deleteOrder = function() {
		$http({
			url: sharedData.getLinks().https + '/orders/' + $scope.idForOperation,
			method: 'DELETE',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.originListOfOrders.splice($scope.indexForOperation, 1);
			$scope.stage = "deleted";
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
            $scope.errMessage = "serverErr";
		});
	}


	/* Filters */
	$scope.resetFilter = function() {
		$scope.filter = {};
		$scope.filteredListOfOrders = [];
	}	
	$scope.resetFilter();
	$scope.updateFilter = function() {
		$scope.filteredListOfOrders = $scope.originListOfOrders.filter(function(item) {
			if ($scope.filter.userId !== undefined) return (item.client.objectId === $scope.filter.userId); else return true;
		}).filter(function(item) {
			if ($scope.filter.roomId !== undefined) return (item.room.objectId === $scope.filter.roomId); else return true;
		}).filter(function(item) {
			if ($scope.filter.roomTypeId !== undefined) return (item.room.roomType.objectId === $scope.filter.roomTypeId); else return true;
		}).filter(function(item) {
			if ($scope.filter.categoryId !== undefined) return (item.category.objectId === $scope.filter.categoryId); else return true;
		});
	}


	/* Для листания страниц с объектами */
	$scope.nextOrders = function() {
		$scope.pager.startPaging = util.nextEntities($scope.originListOfOrders.length, $scope.pager.startPaging, $scope.pager.objectsOnPage);
	}
	$scope.previousOrders = function() {
		$scope.pager.startPaging = util.previousEntities($scope.pager.startPaging, $scope.pager.objectsOnPage);
	}
}]);