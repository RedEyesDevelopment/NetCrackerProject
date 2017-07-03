app.controller('statisticCtrl', ['$scope', '$http', '$location', 'sharedData', 'util',
    function($scope, $http, $location, sharedData, util) {

        (function() {
            $http({
                url: sharedData.getLinks().https + '/pdf',
                method: 'GET',
                headers: { 'Content-Type' : 'application/json' }
            }).then(function(data) {
                console.log(data);
            }, function(response) {
                console.log("Smth wrong!!");
                console.log(response);
            });
        }());

    }]);
