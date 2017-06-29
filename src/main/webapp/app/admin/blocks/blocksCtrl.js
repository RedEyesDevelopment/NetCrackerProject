app.controller('blocksCtrl', ['$scope', '$http', '$location', 'sharedData', 'util',
    function($scope, $http, $location, sharedData, util) {

		/* Функция на получения всех комнат и типов комнат, вызываются сразу */
        (function() {
            $http({
                url: sharedData.getLinks().https + '/blocks/',
                method: 'GET',
                headers: { 'Content-Type' : 'application/json' }
            }).then(function(data) {
                console.log(data);
                $scope.listOfBlocks = data.data;
            }, function(response) {
                console.log("Smth wrong!!");
                console.log(response);
            });
        }());

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
		/* редирект на главную если не админ */
        (function() {
            if (!sharedData.getIsAdmin()) { $location.path('/') };
        }());

		/* Инициализация служебных переменных */
        function resetFlags() {
            $scope.added = false;
            $scope.updated = false;
            $scope.deleted = false;
        }

        $scope.block = {};
        resetFlags();

        $scope.stage = "looking";

        $scope.pager = {
            startPaging : 0,
            objectsOnPage : 6
        }


		/* Функции подготовки запросов */
        $scope.prepareToAddBlock = function() {
            $scope.indexForOperation = "";
            $scope.block.idForOperation = "";
            $scope.block.blockStartDate = "";
            $scope.block.blockFinishDate = "";
            $scope.block.reason = "";
            $scope.block.roomId = "";
            resetFlags();
            $scope.stage = "adding";
            $scope.modificationMode = true;
        }

        $scope.prepareToEditBlock = function(blockId, index) {
            $http({
                url: sharedData.getLinks().https + '/blocks/' + blockId,
                method: 'GET',
                headers: {'Content-Type': 'application/json'}
            }).then(function(data) {
                console.log(data);
                $scope.indexForOperation = index;
                $scope.block.idForOperation = blockId;
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
            });
        }

        $scope.prepareToDeleteBlock = function(blockId, index) {
            $scope.indexForOperation = index;
            $scope.block.idForOperation = blockId;
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
            resetFlags();
            console.log($scope.block.blockStartDate.getTime())
            console.log($scope.block.blockFinishDate.getTime())
            console.log($scope.block.reason)
            console.log($scope.block.roomId)
            $http({
                url: sharedData.getLinks().https + '/blocks',
                method: 'POST',
                data: {
                    blockStartDate : $scope.block.blockStartDate.getTime(),
                    blockFinishDate : $scope.block.blockFinishDate.getTime(),
                    reason : $scope.block.reason,
                    roomId : $scope.block.roomId
                },
                headers: { 'Content-Type' : 'application/json' }
            }).then(function(data) {
                console.log(data);
                $scope.listOfBlocks.push({
                    idForOperation : data.data.objectId,
                    blockStartDate : new Date($scope.block.blockStartDate),
                    blockFinishDate : new Date($scope.block.blockFinishDate),
                    reason : $scope.block.reason,
                    room : util.getObjectInArrayById($scope.listOfRooms, $scope.block.roomId)
                });
                $scope.prepareToAddBlock();
                $scope.added = true;
            }, function(response) {
                console.log("Smth wrong!!");
                console.log(response);
            });
        }

        var editBlock = function() {
            resetFlags();
            console.log($scope.block.blockStartDate.getTime())
            console.log($scope.block.blockFinishDate.getTime())
            console.log($scope.block.reason)
            console.log($scope.block.roomId)
            $http({
                url: sharedData.getLinks().https + '/blocks/' + $scope.block.idForOperation,
                method: 'PUT',
                data: {
                    blockStartDate : $scope.block.blockStartDate.getTime(),
                    blockFinishDate : $scope.block.blockFinishDate.getTime(),
                    reason : $scope.block.reason,
                    roomId : $scope.block.roomId
                },
                headers: { 'Content-Type' : 'application/json' }
            }).then(function(data) {
                console.log(data);
                $scope.listOfBlocks[$scope.indexForOperation].blockStartDate = new Date($scope.block.blockStartDate);
                $scope.listOfBlocks[$scope.indexForOperation].blockFinishDate = new Date($scope.block.blockFinishDate);
                $scope.listOfBlocks[$scope.indexForOperation].reason = $scope.block.reason;
                $scope.listOfBlocks[$scope.indexForOperation].room = util.getObjectInArrayById($scope.listOfRooms, $scope.block.roomId);
                $scope.updated = true;
            }, function(response) {
                console.log("Smth wrong!!");
                console.log(response);
            });
        }

        var deleteBlock = function() {
            resetFlags();
            $http({
                url: sharedData.getLinks().https + '/blocks/' + $scope.block.idForOperation,
                method: 'DELETE',
                headers: { 'Content-Type' : 'application/json' }
            }).then(function(data) {
                console.log(data);
                $scope.listOfBlocks.splice($scope.indexForOperation, 1);
                $scope.deleted = true;
            }, function(response) {
                console.log("Smth wrong!!");
                console.log(response);
            });
        }


		/* Для листания страниц с объектами */
        $scope.nextBlocks = function() {
            $scope.pager.startPaging = util.nextEntities($scope.listOfBlocks.length, $scope.pager.startPaging, $scope.pager.objectsOnPage);
        }
        $scope.previousBlocks = function() {
            $scope.pager.startPaging = util.previousEntities($scope.pager.startPaging, $scope.pager.objectsOnPage);
        }
    }]);