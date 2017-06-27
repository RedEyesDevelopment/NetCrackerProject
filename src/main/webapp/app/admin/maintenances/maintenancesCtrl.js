app.controller('maintenancesCtrl', ['$scope', '$https', '$location', 'sharedData', 'util',
    function($scope, $https, $location, sharedData, util){


    /* Функция на получения всех услуг, вызываются сразу */
    (function() {
        $http({
            url: sharedData.getLinks().https + '/maintenances',
            method: 'GET',
            headers: {
                'Content-Type' : 'application/json'
            }
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
        if(!sharedData.getIsAdmin()) { $location.path('/') };
    }());

    /* Инициализация служебных переменных */
    function resetFlags() {
        $scope.added = false;
        $scope.updated = false;
        $scope.deleted = false;
    }

    $scope.maintenance = {};
    resetFlags();

    $scope.stage = "looking";

    $scope.pager = {
        startPaging : 0,
        objectsOnPage : 6
    }



    /* Функции подготовки запросов */
    $scope.prepareToAddMaintenance = function() {
        $scope.indexForOperation = "";
        $scope.maintenance.idForOperation = "";
        $scope.maintenance.title = "";
        $scope.maintenance.type = "";
        $scope.maintenance.price = "";
        resetFlags();
        $scope.stage = "adding";
        $scope.modificationMode = true;
    }

    $scope.prepareToEditMaintenance = function(maintenanceId, index) {
        $http({
            url: sharedData.getLinks().https + '/maintenance' + maintenanceId,
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        }).then(function(data) {
            console.log(data);
            $scope.indexForOperation = index;
            $scope.maintenance.idForOperation = maintenanceId;
            $scope.maintenance.title = data.data.maintenanceTitle;
            $scope.maintenance.type = data.data.maintenanceType;
            $scope.maintenance.price = data.data.maintenancePrice;
            resetFlags();
            $scope.stage = "editing";
            $scope.modificationMode = true;

        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }

    $scope.prepareToDeleteMaintenance = function(maintenanceId, index) {
        $scope.indexForOperation = index;
        $scope.maintenance.idForOperation = maintenanceId;
        resetFlags();
        $scope.stage = "deleting"
    }

    /* Вызывает нужный запрос в зависимости от типа операции */
    $scope.query = function() {
        switch ($scope.stage) {
            case 'adding': addMaintenance();
                break;
            case 'editing': editMaintenance();
                break;
            case 'deleting': deleteMaintenance;
                break;
        }
    }

    /* Функции, выполняющие запросы */
    var addMaintenance = function() {
        resetFlags();
        $http ({
            url: sharedData.getLinks().https + '/maintenance',
            method: 'POST',
            data: {
                maintenanceTitle :  $scope.maintenance.title,
                maintenanceType :   $scope.maintenance.type,
                maintenancePrice :  $scope.maintenance.price
            },
            headers: { 'Content-Type': 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.listOfMaintenances.push({
                objectId : data.data.objectId,
                maintenanceTitle :  $scope.maintenance.title,
                maintenanceType :   $scope.maintenance.type,
                maintenancePrice :  $scope.maintenance.price
            });
            $scope.prepareToAddMaintenance();
            $scope.added = true;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }

    var editMaintenance = function() {
        resetFlags();
        $http ({
            url: sharedData.getLinks().https + '/maintenance' + $scope.maintenance.idForOperation,
            method: 'PUT',
            data: {
                maintenanceTitle :  $scope.maintenance.title,
                maintenanceType :   $scope.maintenance.type,
                maintenancePrice :  $scope.maintenance.price
            },
            headers: { 'Content-Type': 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.listOfMaintenances[$scope.indexForOperation].maintenanceTitle = $scope.maintenance.title;
            $scope.listOfMaintenances[$scope.indexForOperation].maintenanceType = $scope.maintenance.type;
            $scope.listOfMaintenances[$scope.indexForOperation].maintenancePrice = $scope.maintenance.price;
            $scope.updated = true;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }

    var deleteMaintenance = function() {
        resetFlags();
        $http({
            url: sharedData.getLinks().https + '/maintenance' + $scope.maintenance.idForOperation,
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.listOfMaintenances.splice($scope.indexForOperation, 1);
            $scope.deleted = true;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
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