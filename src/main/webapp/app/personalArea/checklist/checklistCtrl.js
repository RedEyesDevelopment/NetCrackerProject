app.controller('checklistCtrl', ['$scope', '$http', '$location', 'sharedData',
	function($scope, $http, $location, sharedData) {

	/* Функция на получения всех заказов, вызываются сразу */
    	(function() {
    		$http({
    			url: sharedData.getLinks().https + '/checklist',
    			method: 'GET',
    			headers: { 'Content-Type' : 'application/json' }
    		}).then(function(data) {
    			console.log(data);
    			$scope.listOfOrders = data.data;
    		}, function(response) {
    			console.log("Smth wrong!!");
    			console.log(response);
    		});
    	}());

        $scope.checklist = {}
        $scope.stage = "looking";

        $scope.prepareToSend = function() {
            $scope.stage = "sending"
        }

        $scope.query = function() {
            switch ($scope.stage) {
                case 'sending': sendMessage();
                    break;
            }
        }

        var sendMessage = function() {
            $http({
                url: sharedData.getLinks().https + '/clients/sendMessage',
                method: 'POST',
                data: {
                    themeMessage: $scope.mail.theme,
                    message: $scope.mail.message
                },
                headers: { 'Content-Type' : 'application/json' }
            }).then(function(data) {
                $scope.mail = {}
                $scope.stage = "sended";
            }, function(response) {
                console.log("Smth wrong!!");
                console.log(response);
            });
        }
















}]);