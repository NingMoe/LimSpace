/**
 *这里是用户留言模块
 **/
angular
	.module('app')
	.controller('LeMsgCtrl', LeMsgCtrl);

LeMsgCtrl.$inject = ['$scope', '$api', '$stateParams', '$downloadRoot', '$imgHost', '$rootScope', '$http', '$location'];

function LeMsgCtrl($scope, $api, $stateParams, $downloadRoot, $imgHost, $rootScope, $http, $location) {
	console.log("LeMsgCtrl");
	console.log("===================$imgHost:"+$imgHost)
	function getData() {
		$api.get('leaveMessage', '').then(function(resp) {
			$scope.leaveMessages = resp.data.data.rows;
		});
	}
	getData();
	
	function replyMsg(id){
		$api.get('leaveMessage/'+id,'').then(function(resp) {
			if(resp.data.code==200){
				getData();
			}
			alert(resp.data.message);
		});
	}
	
	var searchInput = {
		limit:'',
		offset:''
	}
	/**
     * 提交表单，会更新地址栏
     */
    function updateLocation() {
        var params = {};
        angular.forEach(searchInput, function(value, key) {
            if (value !== '' && value !== undefined && value !== null) {
                params[key] = value;
            }
        });
        // 发送请求
        $api.get('leaveMessage', params).then(function(resp) {
			$scope.leaveMessages = resp.data.data.rows;
		});
    }

	$scope.JSON = JSON;
	$scope.imgHost = $imgHost;
	$scope.replyMsg = replyMsg;
	$scope.updateLocation = updateLocation;
	$scope.searchInput = searchInput;
}