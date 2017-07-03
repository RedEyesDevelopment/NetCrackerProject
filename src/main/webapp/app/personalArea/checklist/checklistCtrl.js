app.controller('checklistCtrl', ['$scope', '$http', 'sharedData', 'util',
    function($scope, $http, sharedData, util) {

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

    // это для кнопок журнала использованных услуг (не удалять!)
    $scope.expand = {id : 0};

    $scope.editComment = {id : 0};

    $scope.order = {};

    $scope.pager = {
        startPaging : 0,
        objectsOnPage : 6
    }

    $scope.prepareToEditComment = function(index) {
        // body...
    }


    /* Для листания страниц с объектами */
    $scope.nextOrders = function() {
        $scope.pager.startPaging = util.nextEntities($scope.listOfOrders.length, $scope.pager.startPaging, $scope.pager.objectsOnPage);
    }
    $scope.previousOrders = function() {
        $scope.pager.startPaging = util.previousEntities($scope.pager.startPaging, $scope.pager.objectsOnPage);
    }
}]);