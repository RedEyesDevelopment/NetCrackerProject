app.controller('bookCrtl', ['$scope', '$http', '$location', function ($scope, $http, $location) {

    /* Сразу получить все категории */
    getAllСategories = function() {
        $http({
            url: 'http://localhost:8080/categories',
            method: 'GET',
            headers: {
                'Content-Type' : 'application/json'
            }
        }).then(function(data) {
            console.log(data);
            $scope.listOfCategories = data.data;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }();

    $scope.book = {}
    $scope.auth = window.auth;

    $scope.stage = "booking";
    $scope.doesNeedToShowBookForm = true;


    $scope.searchRoomTypes = function () {
        console.log($scope.book);
        $http({
            url: 'http://localhost:8080/orders/searchavailability',
            method: 'POST',
            data: {
                arrival :       $scope.book.from.getTime(),
                departure :     $scope.book.till.getTime(),
                livingPersons : parseInt($scope.book.adults),
                categoryId :    parseInt($scope.book.category)
            },
            headers: {
                'Content-Type' : 'application/json'
            }
        }).then(function(data) {
            console.log(data);
            $scope.listOfRoomTypes = data.data;
            $scope.stage = "choosingRoomType";
            window.scrollTo(0,1000);
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }

    $scope.bookFormChange = function() {
        $scope.stage = "booking";
    }

    $scope.issueOder = function (roomTypeId) {
        $http({
            url: 'http://localhost:8080/orders/book/' + roomTypeId,
            method: 'GET',
            headers: {
                'Content-Type' : 'application/json'
            }
        }).then(function(data) {
            console.log(data);
            $scope.finishOrder = data.data;
            $scope.doesNeedToShowBookForm = false;
            $scope.stage = "finishOrdering";
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }

    $scope.acceptOrder = function() {
        $http({
            url: 'http://localhost:8080/orders/accept',
            method: 'GET',
            headers: {
                'Content-Type' : 'application/json'
            }
        }).then(function(data) {
            console.log(data);
            $scope.stage = "thanking";
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }

    $scope.cancelOrder = function() {
        $http({
            url: 'http://localhost:8080/orders/cancel',
            method: 'GET',
            headers: {
                'Content-Type' : 'application/json'
            }
        }).then(function(data) {
            console.log(data);
            $scope.stage = "choosingRoomType";
            $scope.doesNeedToShowBookForm = true;
        }, function(response) {
            console.log("We are in response");
        });
    }

    $scope.thanks = function() {
        $scope.stage = "booking";
        $scope.doesNeedToShowBookForm = true;
    }

}]);

// function eventFire(el, etype){
//   if (el.fireEvent) {
//     el.fireEvent('on' + etype);
//   } else {
//     var evObj = document.createEvent('Events');
//     evObj.initEvent(etype, true, false);
//     el.dispatchEvent(evObj);
//   }
// }

