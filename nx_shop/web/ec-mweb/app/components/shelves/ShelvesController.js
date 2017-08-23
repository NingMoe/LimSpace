//定时上下架
angular
	.module('app')
	.controller('ShelvesController', ShelvesController);

ShelvesController.$inject = ['$rootScope', '$scope', '$api'];

function ShelvesController($rootScope, $scope, $api) {
	console.log("ShelvesController");

	var pagination = {
		pageSize: 10,
		zIndex: 1,
		totalPage: 1
	};
	$scope.pagination = pagination;

	//加载页面初始化参数
	$scope.Inits = function(index) {
		var index = parseInt(index);
		if(index < 1 || index > $scope.pagination.totalPage || !index) {
			alert("输入正确的页数");
			return false;
		}		
		$scope.pagination.zIndex = index;		
		var params = {
			limit: pagination.pageSize,
			offset: (pagination.zIndex - 1) * pagination.pageSize
		};
		// 发送请求
		$api.get('timingShelves', params).then(function(resp) {
			$scope.sssList = resp.data.data;
			$scope.pagination.totalPage = Math.ceil(resp.data.total / $scope.pagination.pageSize) || 1;
		});
	}

	//结束上下架
	$scope.endskutime = function(sssm) {
		if(confirm("确定要" + (sssm.invalid == 0 ? '结束' : '删除') + ":" + sssm.sku.name + "?")) {
			$api.delete('timingShelves/' + sssm.id).then(function(resp) {
				if(resp.data.code === 200) {
					$scope.Inits($scope.pagination.zIndex);
					alert('操作成功！');
				}
			});
		}
	}

	$scope.Inits(1);
	$scope.JSON = JSON;
}