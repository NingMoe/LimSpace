/**
 *这里是用户列表
 **/
angular
	.module('app')
	.controller('UsersController', UsersController);

UsersController.$inject = ['$scope', '$api', '$stateParams', '$downloadRoot', '$imgHost', '$rootScope', '$http', '$location'];

function UsersController($scope, $api, $stateParams, $downloadRoot, $imgHost, $rootScope, $http, $location) {
	console.log("UsersController");
	console.log("===================$imgHost:" + $imgHost)
	var errorDateMess = '';

	//绑定时间插件
	$("#startTime").datetimepicker({
		format: "Y-m-d",
		step: 1
	});
	$("#endTime").datetimepicker({
		format: "Y-m-d",
		step: 1
	});

	$rootScope.sideBarVisible = true;
	var pagination = {
		pageSize: 10,
		zIndex: 1,
		totalPage: 1
	};
	$scope.pagination = pagination;
	$scope.searchInput = {
		limit: pagination.pageSize,
		offset: (pagination.zIndex - 1) * pagination.pageSize
	};

	$scope.getData = function(index) {
		var index = parseInt(index);
		if (index < 1 || index > $scope.pagination.totalPage || !index) {
			alert("输入正确的页数");
			return false;
		}
		var userInut = $scope.searchInput;
		if (userInut.birthdayStartTime > userInut.birthdayEndTime) {
			alert("开始日期不能大于结束日期");
			return false;
		}
		$scope.pagination.zIndex = index;
		$scope.searchInput.offset = (pagination.zIndex - 1) * pagination.pageSize;
		$api.get('users/list', $scope.searchInput).then(function(resp) {
			$scope.userList = resp.data.data.rows;
			$scope.pagination.totalPage = Math.ceil(resp.data.data.total / $scope.pagination.pageSize) || 1;
		});
	}
	
	$scope.getData(1);
	$scope.JSON = JSON;
	$scope.imgHost = $imgHost;
	$scope.errorDateMess = errorDateMess;
}