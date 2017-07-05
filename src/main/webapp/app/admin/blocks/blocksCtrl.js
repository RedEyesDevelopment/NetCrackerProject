app.controller('blocksCtrl', ['$scope', '$http', '$location', 'sharedData', 'util','$timeout',
    function($scope, $http, $location, sharedData, util, $timeout) {

	// All blocks
    (function() {
        $http({
            url: sharedData.getLinks().https + '/blocks/',
            method: 'GET',
            headers: { 'Content-Type' : 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.originListOfBlocks = data.data;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
            $scope.errMessage = response.data.message;
        });
    }());
    // All rooms
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
    /* редирект на главную если не админ и не рецепция */
    (function() {
        if (!sharedData.getIsAdmin() && !sharedData.getIsReception()) { $location.path('/') };
    }());

    $scope.isAdmin = sharedData.getIsAdmin();

	/* Инициализация служебных переменных */
    function resetFlags() {
        $scope.added = false;
        $scope.updated = false;
        $scope.deleted = false;
    }

    $scope.maxDate = new Date();
    $scope.maxDate.setFullYear(new Date().getFullYear() + 1);
    $scope.block = {};
    resetFlags();

    $scope.stage = "looking";

    $scope.pager = {
        startPaging : 0,
        objectsOnPage : 6
    }


	/* Функции подготовки запросов */
    $scope.prepareToAddBlock = function() {
        $scope.errMessage = false;
        $scope.indexForOperation = "";
        $scope.idForOperation = "";
        $scope.block.blockStartDate = "";
        $scope.block.blockFinishDate = "";
        $scope.block.reason = "";
        $scope.block.roomId = "";
        resetFlags();
        $scope.stage = "adding";
        $scope.modificationMode = true;
    }

    $scope.prepareToEditBlock = function(blockId, index) {
        $scope.errMessage = false;
        $http({
            url: sharedData.getLinks().https + '/blocks/' + blockId,
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        }).then(function(data) {
            console.log(data);
            $scope.indexForOperation = index;
            $scope.idForOperation = blockId;
            $scope.block.blockStartDate = new Date(data.data.blockStartDate);
            $scope.block.blockFinishDate = new Date(data.data.blockFinishDate);
            $scope.block.reason = data.data.reason;
            $scope.block.roomId = data.data.room.objectId;
            resetFlags();
            $scope.stage = "editing";
            $scope.modificationMode = true;

        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
            $scope.errMessage = response.data.message;
        });
    }

    $scope.prepareToDeleteBlock = function(blockId, index) {
        $scope.errMessage = false;
        $scope.indexForOperation = index;
        $scope.idForOperation = blockId;
        resetFlags();
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
            case 'adding': addBlock();
                break;
            case 'editing': editBlock();
                break;
            case 'deleting': deleteBlock();
                break;
        }
    }

	/* Функции, выполняющие запросы */
    var addBlock = function() {
        $scope.errMessage = false;
        resetFlags();
        if ($scope.block.blockStartDate.getTime() < $scope.block.blockFinishDate.getTime()
            && $scope.block.blockStartDate.getTime() >= new Date().getTime()
            && $scope.block.blockFinishDate.getTime() <= $scope.maxDate.getTime()) {
            $http({
                url: sharedData.getLinks().https + '/blocks',
                method: 'POST',
                data: {
                    blockStartDate: $scope.block.blockStartDate.getTime(),
                    blockFinishDate: $scope.block.blockFinishDate.getTime(),
                    reason: $scope.block.reason,
                    roomId: $scope.block.roomId
                },
                headers: {'Content-Type': 'application/json'}
            }).then(function (data) {
                console.log(data);
                $scope.originListOfBlocks.push({
                    objectId: data.data.objectId,
                    blockStartDate: new Date($scope.block.blockStartDate),
                    blockFinishDate: new Date($scope.block.blockFinishDate),
                    reason: $scope.block.reason,
                    room: util.getObjectInArrayById($scope.listOfRooms, $scope.block.roomId)
                });
                $scope.prepareToAddBlock();
                $scope.resetFilter();
                $scope.added = true;
            }, function (response) {
                console.log("Smth wrong!!");
                console.log(response);
                $scope.errMessage = response.data.message;
            });
        } else {
            $scope.errMessage = "Invalid dates! Pls fix and try again!";
        }
    }

    var editBlock = function() {
        $scope.errMessage = false;
        resetFlags();
        console.log($scope.maxDate);

        if ($scope.block.blockStartDate.getTime() < $scope.block.blockFinishDate.getTime()
            && $scope.block.blockStartDate.getTime() >= new Date().getTime()
            && $scope.block.blockFinishDate.getTime() <= $scope.maxDate.getTime()) {
            $http({
                url: sharedData.getLinks().https + '/blocks/' + $scope.idForOperation,
                method: 'PUT',
                data: {
                    blockStartDate: $scope.block.blockStartDate.getTime(),
                    blockFinishDate: $scope.block.blockFinishDate.getTime(),
                    reason: $scope.block.reason,
                    roomId: $scope.block.roomId
                },
                headers: {'Content-Type': 'application/json'}
            }).then(function (data) {
                console.log(data);
                $scope.originListOfBlocks[$scope.indexForOperation].blockStartDate = new Date($scope.block.blockStartDate);
                $scope.originListOfBlocks[$scope.indexForOperation].blockFinishDate = new Date($scope.block.blockFinishDate);
                $scope.originListOfBlocks[$scope.indexForOperation].reason = $scope.block.reason;
                $scope.originListOfBlocks[$scope.indexForOperation].room = util.getObjectInArrayById($scope.listOfRooms, $scope.block.roomId);
                $scope.updated = true;
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

    var deleteBlock = function() {
        $scope.errMessage = false;
        resetFlags();
        $http({
            url: sharedData.getLinks().https + '/blocks/' + $scope.idForOperation,
            method: 'DELETE',
            headers: { 'Content-Type' : 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.originListOfBlocks.splice($scope.indexForOperation, 1);
            $scope.deleted = true;
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
        $scope.filteredListOfBlocks = [];
    }   
    $scope.resetFilter();
    $scope.updateFilter = function() {
        $scope.filteredListOfBlocks = $scope.originListOfBlocks.filter(function(item) {
            return $scope.filter.roomId ? (item.room.objectId === $scope.filter.roomId) : true;
        }).filter(function(item) {
            return $scope.filter.onDate ? (item.blockStartDate <= $scope.filter.onDate && $scope.filter.onDate <= item.blockFinishDate) : true;
        });
    }


	/* Для листания страниц с объектами */
    $scope.nextBlocks = function() {
        $scope.pager.startPaging = util.nextEntities($scope.originListOfBlocks.length, $scope.pager.startPaging, $scope.pager.objectsOnPage);
    }
    $scope.previousBlocks = function() {
        $scope.pager.startPaging = util.previousEntities($scope.pager.startPaging, $scope.pager.objectsOnPage);
    }
}]);