angular.module('index', []).controller('home', function($scope, $http) {

    $http.get('resource/list/').success(function(data) {
        //$scope.greeting = data;
        //$scope.greeting = {id: 'xxx', content: 'Hello World!'};
        $scope.listofitems = data; });

    $scope.onDoneClick = function (item) {
        $http.post('/resource/done/' + item.id + '/' + item.done + '/')
            .then(function (success) { console.log("backend received done state change", success) },
                function (failure) { console.log("backend error", failure) });
    };

    // hiding & showing content
    $scope.content = [];

    $scope.toggleContent = function(itemid) {
        $scope.editorEnabled = false;
        for (var key in $scope.content) {
            if (key != itemid) {
                $scope.content[key] = false; }}
        $scope.content[itemid] = !$scope.content[itemid]; };


    // editing content
    $scope.editorEnabled = false;

    $scope.goedit = function (item) {
        $scope.editorEnabled = true;
        item.titleField = item.title;
        item.contentField = item.content;
        $scope.currentEditItem = item.id;
    };

    $scope.saveedit = function (item) {
        item.title = item.titleField;
        item.content = item.contentField;
        $scope.editorEnabled = false;
        $http.post('/resource/save/' + item.id + '/' + item.title + '/' + item.content + '/' + item.done + '/');

    };



});