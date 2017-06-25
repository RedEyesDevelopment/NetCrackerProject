app.controller('headerCtrl', ['$scope', '$http', '$location', 'sharedData', 'ROLE',
    function ($scope, $http, $location, sharedData, ROLE) {

    $scope.auth = {};
    $scope.registration = {
        emailRegex : '[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}',
        passwordRegex : '(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!_%*#?&])[A-Za-z\d$@$!_%*#?&]{8,}',
        phoneRegex : '\+\d{1,2}\(\d{3}\)\d{2}-\d{2}-\d{3}|\+\d{12}|\d{7}|[0]\d{9}'
    }

    var setRoleFromSever = function() {
        $http({
            url: 'http://localhost:8080/auth/isauthorized',
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        }).then(function (data) {
            console.log(data);
            $scope.auth.role = data.data.rolename;
        }, function (response) {
            console.log("Smth wrong!!")
            console.log(response);
        });
    };

    var setLinksFromServer = function() {
        $http({
            url: 'http://localhost:8080/auth/links',
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        }).then(function (data) {
            console.log(data);
            $scope.auth.links = data.data;
        }, function (response) {
            console.log("Smth wrong!!")
            console.log(response);
        });
    };

    var setAuthDataFromServer = function() {
        setRoleFromSever();
        setLinksFromServer();
    }

    /* Получение с сервера данных об авторизации */
    sharedData.setAuth($scope.auth); // добавить в общую фабрику объект данных об авторизации
    setAuthDataFromServer();

    /* Следит за изменениями роли, и в соответствии с ней меняет значение $scope.auth.isAuthorized */
    $scope.$watch('auth.role', function(newRole) {
        if (    newRole === ROLE.ADMIN
             || newRole === ROLE.RECEPTION
             || newRole === ROLE.CLIENT
            ) {
            $scope.auth.isAuthorized = true;
        } else {
            $scope.auth.isAuthorized = false;            
        }
    });

    $scope.login = function() {
        $scope.hideFailAuthMessage();
        $http({
            url: 'http://localhost:8080/auth/login',
            method: 'POST',
            data: {
                "login" : $scope.auth.incoming.login,
                "password" : $scope.auth.incoming.password
            },
            headers: {'Content-Type' : 'application/json'}
        }).then(function (data) {
            console.log(data.data);
            setAuthDataFromServer();
            if (data.data) {
                // if authorized
                $('#loginFormClose').trigger('click');
            } else {
                // WARNING!! DRY VIOLATION!!!
                $scope.auth.incoming.failed = true;
            }
        }, function (response) {
            console.log(response);
            // WARNING!! DRY VIOLATION!!!
            $scope.auth.incoming.failed = true;
           setAuthDataFromServer();
        });
    };

    $scope.hideFailAuthMessage = function() {
        $scope.auth.incoming.failed = false;
    };

    $scope.hideNotTheSamePasswordsMessage = function() {
        $scope.registration.passwordsNotTheSame = false;
    };

    $scope.logout = function() {
        // тут запрос на сервер  ------------------------------------------------ TODO
        $scope.auth.role = ROLE.NA;
    };

    $scope.registration = function() {
        $scope.hideNotTheSamePasswordsMessage();
        if ($scope.registration.userPasswordRepeat === $scope.registration.userPassword) {
            $http({
                url: 'http://localhost:8080/users/registration',
                method: 'POST',
                data: {
                    "objectId": 0,
                    "email": $scope.registration.userEmail,
                    "password": $scope.registration.userPassword,
                    "firstName": $scope.registration.userName,
                    "lastName": $scope.registration.userSurname,
                    "additionalInfo": $scope.registration.userInfo,
                    "enabled": true,
                    "role": {
                        "objectId": 0,
                        "roleName": ""
                    },
                    "phones": [
                        {
                            "objectId": 0,
                            "userId": 0,
                            "phoneNumber": $scope.userPhone
                        }
                    ]
                },
                headers: {'Content-Type' : 'application/json'}
            }).then(function (data) {
                console.log(data);
                $('#registrationFormClose').trigger('click');
                setAuthDataFromServer();
            }, function (response) {
                console.log(response);
                // тут отобразить отказ от сервера
            });
        } else {
            $scope.registration.passwordsNotTheSame = true;
        }
    }

}]);