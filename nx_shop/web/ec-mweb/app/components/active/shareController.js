angular
	.module('app')
	.controller('shareController', shareController);
shareController.$inject = ['$scope', '$api', '$downloadRoot'];

function shareController($scope, $api, $downloadRoot) {
	var pagination = {
		pageSize: 5,
		zIndex: 1,
		totalPage: 1
	};
	//分页
	$scope.pagination = pagination;
	$("#startTime").datetimepicker({
		format: "Y-m-d",
		step: 1
	});
	$("#endTime").datetimepicker({
		format: "Y-m-d",
		step: 1
	});

	$scope.init = function(index) {
		var index = parseInt(index);
		if(index < 1 || index > $scope.pagination.totalPage || !index) {
			alert("输入正确的页数");
			return false;
		}
		$scope.pagination.zIndex = index;
		$scope.offset = (pagination.zIndex - 1) * pagination.pageSize;
		var params = {
				dateType: $scope.dateType || "",
				mobile: $scope.mobile || "",
				stratTime: $scope.startTime || "",
				endTime: $scope.endTime || "",
				offset: $scope.offset,
				limit: pagination.pageSize
			}
			//访问接口
		$api.get('share', params).then(function(resp) {
			// $scope.shareUser = resp.data.date;
			$scope.shareList = resp.data.date;
			$scope.pagination.totalPage = Math.ceil(resp.data.shareNum / $scope.pagination.pageSize) || 1;
			$scope.shareall = resp.data;
			console.log(resp);
			$scope.src = "img/jiantou.png";
		});
	}
	//清除部分数据
	$scope.clearDate = function(type) {
		if(type === "date") {
			$scope.dateType = "";
		} else {
			if(type) {
				$scope.startTime = "";
				$scope.endTime = "";
				$scope.dateType = type;
				$scope.init(1);
			}
		}
	}
	$scope.derive = function() {
		$scope.dateType = $scope.dateType || "";
		$scope.mobile = $scope.mobile || "";
		$scope.startTime = $scope.startTime || "";
		$scope.endTime = $scope.endTime || "";
		window.open($downloadRoot + "/share/execl?dateType=" + $scope.dateType + "&mobile=" + $scope.mobile + "&stratTime=" + $scope.startTime + "&endTime=" + $scope.endTime);
	}
	$scope.hide = 'display:none';
	$scope.status = function() {
		if(this.hide == "") {
			this.src = 'img/jiantou.png';
			this.hide = "display:none";
		} else {
			this.src = 'img/jiantou1.png';
			this.hide = "";
		}
	}
	//初始化
	$scope.init(1);
	$scope.clearDate();

}
