angular.module('admin', []).controller('ad1', function($scope, $http) {
    $http.get('resource/admin/list/').then(function(success) {
        console.log("list received", success.data);
        $scope.listofitems = success.data;
    });

    $http.get('resource/admin/userlist/').then(function(success) {
        console.log("user list received", success.data);
        $scope.listofusers = success.data; });

    $scope.userQuery = {};
    $scope.userDetails = function (username) {
        $http.get('/resource/user/' + username + '/').then(
            function (success) {
                $scope.userQuery[success.data['username']] = success.data;
                console.log('result', $scope.userQuery);
            }
        );
    };

    $http.get("/who/").success(function(data) {
        $scope.springdoUserName = data["name"];
    });
});