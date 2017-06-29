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

    $scope.maintenance = {};
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
        $scope.maintenance.dollars = 0;
        $scope.maintenance.cents = 0;
        $scope.stage = "adding";
        $scope.modificationMode = true;
    }

    $scope.prepareToEditMaintenance = function(maintenanceId, index) {
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
        });
    }

    $scope.prepareToDeleteMaintenance = function(maintenanceId, index) {
        $scope.indexForOperation = index;
        $scope.maintenance.idForOperation = maintenanceId;
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
                $scope.listOfMaintenances.push({
                    objectId : data.data.objectId,
                    maintenanceTitle :  $scope.maintenance.title,
                    maintenanceType :   $scope.maintenance.type,
                    maintenancePrice :  ($scope.maintenance.dollars * 100) + $scope.maintenance.cents
                });
                $scope.prepareToAddMaintenance();
                $scope.stage = 'added';
            }, function(response) {
                console.log("Smth wrong!!");
                console.log(response);
            });
        }
    }

    var editMaintenance = function() {
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
            $scope.listOfMaintenances[$scope.indexForOperation].maintenanceTitle = $scope.maintenance.title;
            $scope.listOfMaintenances[$scope.indexForOperation].maintenanceType = $scope.maintenance.type;
            $scope.listOfMaintenances[$scope.indexForOperation].maintenancePrice = ($scope.maintenance.dollars * 100) + $scope.maintenance.cents;
            $scope.stage = 'updated';
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }

    var deleteMaintenance = function() {
        $http({
            url: sharedData.getLinks().https + '/maintenances/' + $scope.maintenance.idForOperation,
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.listOfMaintenances.splice($scope.indexForOperation, 1);
            $scope.stage = 'deleted';
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }


    /* Для листания страниц с объектами */
    $scope.nextMaintenances = function() {
        $scope.pager.startPaging = util.nextEntities($scope.listOfMaintenances.length, $scope.pager.startPaging, $scope.pager.objectsOnPage);
    }
    $scope.previousMaintenances = function() {
        $scope.pager.startPaging = util.previousEntities($scope.pager.startPaging, $scope.pager.objectsOnPage);
    }
}]);