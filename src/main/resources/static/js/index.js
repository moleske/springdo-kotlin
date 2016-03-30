angular.module('index', []).
    controller('home', function($scope, $http) {
        $http.get('resource/list/').then(function(success) {
            $scope.listofitems = success.data;
        })
    });
