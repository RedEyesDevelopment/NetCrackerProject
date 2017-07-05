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
			$scope.errMessage = response.data.message;
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
			$scope.errMessage = response.data.message;
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
			$scope.errMessage = response.data.message;
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
			$scope.errMessage = response.data.message;
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
			$scope.errMessage = response.data.message;
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
			$scope.errMessage = response.data.message;
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
		$scope.errMessage = false;
		$scope.stage = "looking";
		$scope.mode = "look";
	}


	/* Вызывает нужный запрос в зависимости от типа операции */
	$scope.query = function() {
		$scope.errMessage = false;
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
		$scope.errMessage = false;
		$scope.idForOperation = orderId;
		$scope.indexForOperation = index;
		$scope.confirmed.isPaidFor = isPaidFor;
		$scope.confirmed.isConfirmed = isConfirmed;
		$scope.stage = "switch";
	}

	var switchPaidConfirmed = function() {
		$scope.errMessage = false;
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
			$scope.errMessage = response.data.message;
		});
	}


	/* Для навешивания услуг на заказ */
	$scope.prepareToAddMaintenance = function(orderId, index) {
		$scope.errMessage = false;
		$scope.newMaintenance.orderId = orderId;
		$scope.indexForOperation = index;
	}	

	$scope.addMaintenance = function() {
        $scope.errMessage = false;
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
				$scope.errMessage = response.data.message;
			});
			$scope.newMaintenance = { count: 1 };
			$scope.stage = "looking";
		}, function(response) {
			console.log("Smth wrong!!");
            $scope.errMessage = response.data.message;
		});
	}

	$scope.prepareToDeleteJournalRecord = function(journalRecordId, orderIndex, journalRecordIndex) {
		$scope.errMessage = false;
		$scope.idForOperation = journalRecordId;
		$scope.indexForOperation = orderIndex;
		$scope.indexJournalForOperation = journalRecordIndex;
		$scope.stage = "deleteJournalRecord";
	}

	var deleteJournalRecord = function() {
        $scope.errMessage = false;
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
            $scope.errMessage = response.data.message;
		});
	}


	/* Добавление заказа */
	$scope.prepareToAddOrder = function() {
		$scope.errMessage = false;
		$scope.idForOperation = undefined;
		$scope.indexForOperation = undefined;
		$scope.order = {}
		$scope.stage = "adding";
		$scope.mode = "add";
	}

	var addOrder = function() {
		$scope.errMessage = false;
		sharedData.setUserIdForOrderFromAdmin($scope.order.clientId);
		$location.path('/book');
	}

	/* Изменение заказа */
	$scope.prepareToEditOrder = function(orderId, index) {
		$scope.errMessage = false;
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
			$scope.errMessage = response.data.message;
		});
	}

	$scope.searchForEdit = function() {
        $scope.errMessage = false;
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
                $scope.errMessage = response.data.message;
            });
        } else {
            $scope.errMessage = "Invalid dates! Pls fix and try again!";
        }
	}

	$scope.backTochoosingAnotherVariants = function() {
		$scope.errMessage = false;
		$scope.stage = "searchForEdit";
	}

	var editOrder = function() {
        $scope.errMessage = false;
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
                $scope.resetFilter();
            }, function (response) {
                console.log("Smth wrong!!");
                console.log(response);
                $scope.errMessage = response.data.message;
            });
        } else {
            $scope.errMessage = "Invalid dates! Pls fix and try again!";
        }
	}

	$scope.prepareToDeleteOrder = function(orderId, index) {
		$scope.idForOperation = orderId;
		$scope.indexForOperation = index;
		$scope.stage = "deleting";
	}

	var deleteOrder = function() {
        $scope.errMessage = false;
		$http({
			url: sharedData.getLinks().https + '/orders/' + $scope.idForOperation,
			method: 'DELETE',
			headers: { 'Content-Type' : 'application/json' }
		}).then(function(data) {
			console.log(data);
			$scope.originListOfOrders.splice($scope.indexForOperation, 1);
			$scope.stage = "deleted";
			$scope.resetFilter();
		}, function(response) {
			console.log("Smth wrong!!");
			console.log(response);
            $scope.errMessage = response.data.message;
		});
	}


	/* Filters */
	$scope.resetFilter = function() {
		$scope.filteredListOfOrders = [];
		$scope.filter = {};
	}	
	$scope.resetFilter();
	$scope.updateFilter = function() {
		$scope.filteredListOfOrders = $scope.originListOfOrders.filter(function(item) {
			if ($scope.filter.status !== undefined) {
				var today = Date.now();
				var start = item.livingStartDate;
				var finish = item.livingFinishDate;
				var day = 24 * 60 * 60 * 1000;
				switch ($scope.filter.status) {
					case 'future': if(today < start) return true
						break;
					case 'current': if(start-day < today && today < finish+day) return true
						break;
					case 'past': if(finish < today) return true
						break;
					default: return false;
				}
			} else return true;
		}).filter(function(item) {
			return $scope.filter.userId ? (item.client.objectId === $scope.filter.userId) : true;
		}).filter(function(item) {
			return $scope.filter.roomId ? (item.room.objectId === $scope.filter.roomId) : true;
		}).filter(function(item) {
			return $scope.filter.roomTypeId ? (item.room.roomType.objectId === $scope.filter.roomTypeId) : true;
		}).filter(function(item) {
			return $scope.filter.categoryId ? (item.category.objectId === $scope.filter.categoryId) : true;
		}).filter(function(item) {
			return ($scope.filter.isPaidFor !== undefined) ? (item.isPaidFor == $scope.filter.isPaidFor) : true;
		}).filter(function(item) {
			return ($scope.filter.isConfirmed !== undefined) ? (item.isConfirmed == $scope.filter.isConfirmed) : true;
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