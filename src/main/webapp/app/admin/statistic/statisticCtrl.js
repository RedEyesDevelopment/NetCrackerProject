app.controller('statisticCtrl', ['$scope', '$http', '$location', 'sharedData',
    function($scope, $http, $location, sharedData) {

    (function() {
        $http({
            url: 'http://localhost:8080/pdf',
            method: 'GET',
            headers: { 'Content-Type' : 'application/json' }
        }).then(function(data) {
            console.log(data);
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }());

    $scope.thanks = function() {
        $location.path("/");
    }
    
}]);
