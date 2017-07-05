app.controller('maintenancesCtrl', ['$scope', '$http', '$location', 'sharedData', 'util',
    function($scope, $http, $location, sharedData, util){

    /* Функция на получения всех услуг, вызываются сразу */
    (function() {
        $http({
            url: sharedData.getLinks().https + '/maintenances',
            method: 'GET',
            headers: { 'Content-Type' : 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.originListOfMaintenances = data.data;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
            $scope.errMessage = response.data.message;
        });
    }());
    /* редирект на главную если не админ и не рецепция */
    (function() {
        if (!sharedData.getIsAdmin() && !sharedData.getIsReception()) { $location.path('/') };
    }());

    $scope.isAdmin = sharedData.getIsAdmin();

    $scope.maintenance = {};
    $scope.stage = "looking";

    $scope.pager = {
        startPaging : 0,
        objectsOnPage : 4
    }

    /* Функции подготовки запросов */
    $scope.prepareToAddMaintenance = function() {
        $scope.errMessage = false;
        $scope.indexForOperation = "";
        $scope.maintenance.idForOperation = "";
        $scope.maintenance.title = "";
        $scope.maintenance.type = "";
        $scope.maintenance.price = "";
        $scope.maintenance.dollars = 0;
        $scope.maintenance.cents = 0;
        $scope.stage = "adding";
        $scope.modificationMode = true;
    }

    $scope.prepareToEditMaintenance = function(maintenanceId, index) {
        $scope.errMessage = false;
        $http({
            url: sharedData.getLinks().https + '/maintenances/' + maintenanceId,
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        }).then(function(data) {
            $scope.indexForOperation = index;
            $scope.maintenance.idForOperation = maintenanceId;
            $scope.maintenance.title = data.data.maintenanceTitle;
            $scope.maintenance.type = data.data.maintenanceType;
            $scope.maintenance.price = data.data.maintenancePrice;
            $scope.maintenance.dollars = Math.floor(data.data.maintenancePrice / 100);
            $scope.maintenance.cents = data.data.maintenancePrice % 100;
            $scope.stage = "editing";
            $scope.modificationMode = true;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
            $scope.errMessage = response.data.message;
        });
    }

    $scope.prepareToDeleteMaintenance = function(maintenanceId, index) {
        $scope.errMessage = false;
        $scope.indexForOperation = index;
        $scope.maintenance.idForOperation = maintenanceId;
        $scope.stage = "deleting";
    }

    /* Возврат на просмотр */
    $scope.back = function() {
        $scope.errMessage = false;
        $scope.stage = "looking";
        $scope.modificationMode = false;
    }

    /* Вызывает нужный запрос в зависимости от типа операции */
    $scope.query = function() {
        switch ($scope.stage) {
            case 'adding': addMaintenance();
                break;
            case 'editing': editMaintenance();
                break;
            case 'deleting': deleteMaintenance();
                break;
        }
    }

    /* Функции, выполняющие запросы */
    var addMaintenance = function() {
        $scope.errMessage = false;
        if (        $scope.maintenance.dollars >= 0
                &&  $scope.maintenance.cents >= 0
                &&  $scope.maintenance.cents <= 99) {
            $http({
                url: sharedData.getLinks().https + '/maintenances/',
                method: 'POST',
                data: {
                    maintenanceTitle :  $scope.maintenance.title,
                    maintenanceType :   $scope.maintenance.type,
                    maintenancePrice :  ($scope.maintenance.dollars * 100) + $scope.maintenance.cents
                },
                headers: { 'Content-Type' : 'application/json' }
            }).then(function(data) {
                console.log(data);
                $scope.originListOfMaintenances.push({
                    objectId : data.data.objectId,
                    maintenanceTitle :  $scope.maintenance.title,
                    maintenanceType :   $scope.maintenance.type,
                    maintenancePrice :  ($scope.maintenance.dollars * 100) + $scope.maintenance.cents
                });
                $scope.prepareToAddMaintenance();
                $scope.stage = 'added';
                $scope.resetFilter();
            }, function(response) {
                console.log("Smth wrong!!");
                console.log(response);
                $scope.errMessage = response.data.message;
            });
        }
    }

    var editMaintenance = function() {
        $scope.errMessage = false;
        $http({
            url: sharedData.getLinks().https + '/maintenances/' + $scope.maintenance.idForOperation,
            method: 'PUT',
            data: {
                maintenanceTitle :  $scope.maintenance.title,
                maintenanceType :   $scope.maintenance.type,
                maintenancePrice :  ($scope.maintenance.dollars * 100) + $scope.maintenance.cents
            },
            headers: { 'Content-Type' : 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.originListOfMaintenances[$scope.indexForOperation].maintenanceTitle = $scope.maintenance.title;
            $scope.originListOfMaintenances[$scope.indexForOperation].maintenanceType = $scope.maintenance.type;
            $scope.originListOfMaintenances[$scope.indexForOperation].maintenancePrice = ($scope.maintenance.dollars * 100) + $scope.maintenance.cents;
            $scope.stage = 'updated';
            $scope.resetFilter();
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
            $scope.errMessage = response.data.message;
        });
    }

    var deleteMaintenance = function() {
        $scope.errMessage = false;
        $http({
            url: sharedData.getLinks().https + '/maintenances/' + $scope.maintenance.idForOperation,
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.originListOfMaintenances.splice($scope.indexForOperation, 1);
            $scope.stage = 'deleted';
            $scope.resetFilter();
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
            $scope.errMessage = response.data.message;
        });
    }


    /* Filters */
    $scope.resetFilter = function() {
        $scope.filter = {};
        $scope.filteredListOfMaintenances = [];
    }   
    $scope.resetFilter();
    $scope.updateFilter = function() {
        $scope.filteredListOfMaintenances = $scope.originListOfMaintenances.filter(function(item) {
            return $scope.filter.priceTop ? (Math.round(item.maintenancePrice/100) <= $scope.filter.priceTop) : true;
        }).filter(function(item) {
            return $scope.filter.priceBottom ? ($scope.filter.priceBottom <= Math.round(item.maintenancePrice/100)) : true;
        });
    }


    /* Для листания страниц с объектами */
    $scope.nextMaintenances = function() {
        $scope.pager.startPaging = util.nextEntities($scope.originListOfMaintenances.length, $scope.pager.startPaging, $scope.pager.objectsOnPage);
    }
    $scope.previousMaintenances = function() {
        $scope.pager.startPaging = util.previousEntities($scope.pager.startPaging, $scope.pager.objectsOnPage);
    }
}]);