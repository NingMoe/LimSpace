angular
    .module('app')
    .controller('TopicController', TopicController);

TopicController.$inject = ['$scope', '$api', '$stateParams', '$imgHost', '$apiHost', '$webHost'];
function TopicController($scope, $api, $stateParams, $imgHost, $apiHost, $webHost) {
    var params = {
        id: $stateParams.id
    };

    function getData() {
        $api.get('topics').then(function(resp) {
            $scope.topics = resp.data.data;
            // $scope.logs = resp.data.logs;
        });
    }
    getData();

    var events = {};

    /**
     * 进行退款操作
     */
    events.upload = function(refund) {
        console.log(refund);
        $api.putJson('refunds/enter',refund.id).then(function(resp) {
            // 成功，刷新数据
            if (resp.data.code == 200) {
                getData();
            } else {
                alert(resp.data.exception);
            }
        });
    };

    events.fileSelected = function(event) {
        var files = event.target.files;
        var form = event.target.form;
        var file = files[0];
        /*
        lastModified: 1426143594000
        lastModifiedDate: Thu Mar 12 2015 14:59:54 GMT+0800 (CST)
        name: "CameraApp.zip"
        size: 35012
        type: "application/zip"
        webkitRelativePath: ""
        */
        form.newPath && form.removeChild(newPath);
        var match = form.path && form.path.value.match(/[^/]+$/);
        if (match) {
            var originalPath = match[0];
            var newPath = file.name.replace(/\.zip$/, '');
            if (newPath !== originalPath) {
                var replace = window.confirm('路径不一致，是否用新的zip包名【' + newPath + '】替换线上的路径【' + originalPath + '】？');
                if (replace) {
                    var newPathInput = document.createElement('input');
                    newPathInput.type = 'hidden';
                    newPathInput.name = 'newPath';
                    newPathInput.value = form.path.value.replace(/[^/]+$/, newPath);
                    form.appendChild(newPathInput);
                }
            }
        }
        form.action = $apiHost + '/topics';
        form.target = 'upload-iframe';
        form.submit();
    };

    var uploadDone = function(event) {
        console.log(event);
        if (event.data[1] != 0) {
            alert(event.data[2]);
        }
        // 刷新数据
        getData();
    };
    window.addEventListener("message", uploadDone, false);

    $scope.events = events;
    $scope.imgHost = $imgHost;
    $scope.$webHost = $webHost;
}
