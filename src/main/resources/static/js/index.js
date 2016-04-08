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

    $scope.plusbutton = function () {
        $http.get('resource/create/').then(function(success) {
            newitem = success.data;
            $scope.listofitems.push(newitem);
            $scope.toggleContent(newitem.id);
            $scope.goedit(newitem);
        });
    };

    $scope.godelete = function (item) {
        $http.post('/resource/delete/' + item.id + '/').then(
            function (success) {
                // in the future we should get an update from the server, now we just
                // remove the item ourselves
                listofitems = $scope.listofitems;
                for (i = listofitems.length - 1; i >= 0; i--) {
                    if (listofitems[i].id == item.id) {
                        listofitems.splice(i, 1);
                    }
                }
            });
    };
});