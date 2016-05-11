angular.module('credentials', ['ngRoute'])
    .controller('credentials', function($scope, $http, $window) {

        $scope.username = "Navya";
        $scope.password = "secret";
        $scope.reminder = false;

        $scope.gosubmit = function() {

            var request = {
                method: 'POST',
                url: '/credentials.html',
                data: $.param({username: $scope.username, password: $scope.password}),
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
            };

            $http(request).then(
                function (success) {
                    console.log("success", success);
                    $window.location.href = "/index.html";  },  // rather heavy handed
                function (failure) {
                    console.log("failure", failure); });
        };
    });