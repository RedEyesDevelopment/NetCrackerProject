app.controller('messageToAdminCtrl', ['$scope', '$http', '$location', 'sharedData',
	function($scope, $http, $location, sharedData) {


	$scope.mail = {}
	$scope.stage = "looking";

	$scope.prepareToSend = function() {
		$scope.errMessage = false;
		$scope.stage = "sending"
	}

	$scope.query = function() {
		switch ($scope.stage) {
			case 'sending': sendMessage();
				break;
		}
	}

	var sendMessage = function() {
		$scope.errMessage = false;
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
			$scope.errMessage = response.data.message;
		});
	}





}]);