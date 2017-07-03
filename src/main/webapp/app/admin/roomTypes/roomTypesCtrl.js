app.controller('roomTypesCtrl', ['$scope', '$http', '$location', 'sharedData', 'util',
     function($scope, $http, $location, sharedData, util){

    /* Функция на получения всех типов комнат, вызывается сразу */
    var getAllRoomTypes = function() {
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
    };
    getAllRoomTypes();
    /* редирект на главную если не админ */
    (function() {
        if(!sharedData.getIsAdmin()) { $location.path('/') };
    }());

    // это для кнопок списка rates (не удалять!)
    $scope.expand = {id : 0};

    $scope.roomType = {};
    $scope.rate = {
        priceForOneCent: 0,
        priceForTwoCent: 0,
        priceForThreeCent: 0
    }

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
            case 'addingRoomType': addRoomType();
                break;
            case 'editingRoomType': editRoomType();
                break;
            case 'deletingRoomType': deleteRoomType();
                break;
            case 'addingRate': addRate();
                break;
            case 'editingRate': addRate(); // it's not a mistake
                break;
        }
    }


    $scope.prepareToAddRoomType = function() {
        $scope.roomType= {};
        $scope.stage = "addingRoomType";
        $scope.mode = "addOrEditRoomType";
    }

    var addRoomType = function() {
        console.log({
                roomTypeTitle :     $scope.roomType.roomTypeTitle,
                roomTypeContent :   $scope.roomType.content
            });
        $http({
            url: sharedData.getLinks().https + '/roomTypes',
            method: 'POST',
            data: {
                roomTypeTitle :     $scope.roomType.roomTypeTitle,
                roomTypeContent :   $scope.roomType.content
            },
            headers: { 'Content-Type' : 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.listOfRoomTypes.push({
                objectId :          data.data.objectId,
                roomTypeTitle :     $scope.roomType.roomTypeTitle,
                content :   $scope.roomType.content
            });
            $scope.prepareToAddRoomType();
            $scope.stage = "added";
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
            $scope.errMessage = "serverErr";
        });
    }

    $scope.prepareToEditRoomType = function(roomTypeId, index) {
        $http({
            url: sharedData.getLinks().https + '/roomTypes/' + roomTypeId,
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        }).then(function(data) {
            console.log(data);
            $scope.indexForOperation = index;
            $scope.idForOperation = roomTypeId;
            $scope.roomType = data.data;
            $scope.stage = "editingRoomType";
            $scope.mode = "addOrEditRoomType";
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }

    var editRoomType = function() {
        console.log({
            roomTypeTitle :     $scope.roomType.roomTypeTitle,
            roomTypeContent :   $scope.roomType.content
        });
        $http({
            url: sharedData.getLinks().https + '/roomTypes/' + $scope.idForOperation,
            method: 'PUT',
            data: {
                roomTypeTitle :     $scope.roomType.roomTypeTitle,
                roomTypeContent :   $scope.roomType.content
            },
            headers: { 'Content-Type' : 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.listOfRoomTypes[$scope.indexForOperation].roomTypeTitle = $scope.roomType.roomTypeTitle;
            $scope.listOfRoomTypes[$scope.indexForOperation].content = $scope.roomType.content;
            $scope.stage = "updated";
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
            $scope.errMessage = "serverErr";
        });
    }


    $scope.prepareToDeleteRoomType = function(roomTypeId, index) {
        $scope.indexForOperation = index;
        $scope.idForOperation = roomTypeId;
        $scope.stage = "deletingRoomType"
    }

    var deleteRoomType = function() {
        $http({
            url: sharedData.getLinks().https + '/roomTypes/' + $scope.idForOperation,
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.listOfRoomTypes.splice($scope.indexForOperation, 1);
            $scope.stage = "deleted";
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
            $scope.errMessage = "serverErr";
        });
    }


    $scope.prepareToAddRate = function(roomTypeId) {
        $scope.idForOperation = roomTypeId;
        $scope.rate= {};
        $scope.stage = "addingRate";
        $scope.mode = "addOrEditRate";
    }

    var addRate = function() {
        resetFlags();
        if ($scope.rate.rateFromDate.getTime() < $scope.rate.rateToDate.getTime()
            && $scope.rate.rateFromDate.getTime() >= new Date().getTime()
            && $scope.rate.rateToDate.getTime() <= $scope.maxDate.getTime()
            && $scope.rate.priceForOneDlr >= 0
            && $scope.rate.priceForOneCent >= 0
            && $scope.rate.priceForOneCent <= 99
            && $scope.rate.priceForTwoDlr >= 0
            && $scope.rate.priceForTwoCent >= 0
            && $scope.rate.priceForTwoCent <= 99
            && $scope.rate.priceForThreeDlr >= 0
            && $scope.rate.priceForThreeCent >= 0
            && $scope.rate.priceForThreeCent <= 99) {
            $http({
                url: sharedData.getLinks().https + '/rates',
                method: 'POST',
                data: {
                    roomTypeId:     $scope.idForOperation,
                    rateFromDate:   $scope.rate.rateFromDate.getTime(),
                    rateToDate:     $scope.rate.rateToDate.getTime(),
                    priceForOne:    ($scope.rate.priceForOneDlr * 100) + $scope.rate.priceForOneCent,
                    priceForTwo:    ($scope.rate.priceForTwoDlr * 100) + $scope.rate.priceForTwoCent,
                    priceForThree:  ($scope.rate.priceForThreeDlr * 100) + $scope.rate.priceForThreeCent
                },
                headers: { 'Content-Type' : 'application/json' }
            }).then(function(data) {
                getAllRoomTypes();
                $scope.prepareToAddRate($scope.idForOperation);
                $scope.stage = "rateDone";
            }, function(response) {
                console.log("Smth wrong!!");
                console.log(response);
                $scope.errMessage = "serverErr";
            });
        } else {
            $scope.errMessage = "invalidInputData";
        }
    }

    $scope.prepareToEditRate = function(roomTypeId, rateId) {
        $http({
            url: sharedData.getLinks().https + '/rates/' + rateId,
            method: 'GET',
            headers: {'Content-Type' : 'application/json'}
        }).then(function(data) {
            console.log(data);
            $scope.idForOperation = roomTypeId;
            $scope.rate = {
                rateFromDate:   new Date(data.data.rateFromDate),
                rateToDate:     new Date(data.data.rateToDate)
            }
            data.data.prices.forEach(function(price) {
                if (price.numberOfPeople == 1) {
                    $scope.rate.priceForOneDlr = Math.floor(price.rate / 100);
                    $scope.rate.priceForOneCent = price.rate % 100;
                }
                if (price.numberOfPeople == 2) {
                    $scope.rate.priceForTwoDlr = Math.floor(price.rate / 100);
                    $scope.rate.priceForTwoCent = price.rate % 100;
                }
                if (price.numberOfPeople == 3) {
                    $scope.rate.priceForThreeDlr = Math.floor(price.rate / 100);
                    $scope.rate.priceForThreeCent = price.rate % 100;
                }
            });
            $scope.stage = "editingRate";
            $scope.mode = "addOrEditRate";
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
