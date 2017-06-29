app.controller('roomTypesCtrl', ['$scope', '$http', '$location', 'sharedData', 'util',
     function($scope, $http, $location, sharedData, util){

    /* Функция на получения всех типов комнат, вызывается сразу */
    (function() {
        $http({
            url: sharedData.getLinks().https + '/roomTypes',
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

    $scope.roomType = {};
    resetFlags();

    $scope.stage = "looking";

    $scope.pager = {
        startPaging : 0,
        objectsOnPage : 6
    }

    /* Функции подготовки запросов */
    $scope.prepareToAddRoomType = function() {
        $scope.indexForOperation = "";
        $scope.roomType.idForOperation = "";
        $scope.roomType.title = "";
        $scope.roomType.content = "";
        resetFlags();
        $scope.stage = "adding";
        $scope.modificationMode = true;
    }

    $scope.prepareToEditRoomType = function(roomTypeId, index) {
        $http({
            url: sharedData.getLinks().https + '/roomTypes/' + roomTypeId,
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        }).then(function(data) {
            console.log(data);
            $scope.indexForOperation = index;
            $scope.roomType.idForOperation = roomTypeId;
            $scope.roomType.title = data.data.roomTypeTitle;
            $scope.roomType.content = data.data.roomTypeContent;
            resetFlags();
            $scope.stage = "editing";
            $scope.modificationMode = true;

        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }

    $scope.prepareToDeleteRoomType = function(roomTypeId, index) {
        $scope.indexForOperation = index;
        $scope.roomType.idForOperation = roomTypeId;
        resetFlags();
        $scope.stage = "deleting"
    }

    /* Возврат на просмотр */
    $scope.back = function() {
        $scope.stage = "looking";
        $scope.modificationMode = false;
    }

    /* Вызывает нужный запрос в зависимости от типа операции */
    $scope.query = function() {
        switch ($scope.stage) {
            case 'adding': addRoomType();
                break;
            case 'editing': editRoomType();
                break;
            case 'deleting': deleteRoomType();
                break;
        }
    }

    /* Функции, выполняющие запросы */
    var addRoomType = function() {
        resetFlags();
        $http({
            url: sharedData.getLinks().https + '/roomTypes/',
            method: 'POST',
            data: {
                roomTypeTitle :     $scope.roomType.title,
                roomTypeContent :   $scope.roomType.content
            },
            headers: { 'Content-Type' : 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.listOfRoomTypes.push({
                objectId :          data.data.objectId,
                roomTypeTitle :     $scope.roomType.title,
                roomTypeContent :   $scope.roomType.content
            });
            $scope.prepareToAddRoomType();
            $scope.added = true;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }

    var editRoomType = function() {
        resetFlags();
        $http({
            url: sharedData.getLinks().https + '/roomTypes/' + $scope.roomType.idForOperation,
            method: 'PUT',
            data: {
                roomTypeTitle :     $scope.roomType.title,
                roomTypeContent :   $scope.roomType.content
            },
            headers: { 'Content-Type' : 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.listOfRoomTypes[$scope.indexForOperation].roomTypeTitle = $scope.roomType.title;
            $scope.listOfRoomTypes[$scope.indexForOperation].roomTypeContent = $scope.roomType.content;
            $scope.updated = true;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }

    var deleteRoomType = function() {
        resetFlags();
        $http({
            url: sharedData.getLinks().https + '/roomTypes/' + $scope.roomType.idForOperation,
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.listOfRoomTypes.splice($scope.indexForOperation, 1);
            $scope.deleted = true;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }


    /* Для листания страниц с объектами */
    $scope.nextRoomTypes = function() {
        $scope.pager.startPaging = util.nextEntities($scope.listOfRoomTypes.length, $scope.pager.startPaging, $scope.pager.objectsOnPage);
    }
    $scope.previousRoomTypes = function() {
        $scope.pager.startPaging = util.previousEntities($scope.pager.startPaging, $scope.pager.objectsOnPage);
    }
}]);
