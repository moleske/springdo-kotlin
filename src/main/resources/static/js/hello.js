angular.module('hello', [])
    .controller('home', function($scope, $http) {
        $http.get('resource/hello').then(
            function(success) {
                $scope.greeting = success.data; });
        $scope.greeting = {id: 'xxx', content: 'Hello World!'};
});
