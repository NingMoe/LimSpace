angular
	.module('app')
	.controller('popularsearchController', popularsearchController);

popularsearchController.$inject = ['$scope', '$api'];

function popularsearchController($scope, $api) {
	var events = {};
	$scope.events = events;
	$scope.init = init;
	init();
	//数据初始化
	function init() {
		$api.get('popularsearch', {}).then(function(resp) {
			$scope.popularsearchList = resp.data.data;
		});
	}
	//显示添加框
	events.add = function() {
			$scope.popularsearch = {status:"false"};
			$('#addModal').modal('show');
			$("input[name='queryText']").focus();
	}
	//保存
	events.save = function(form) {
		form.$submitted = true;
		console.log($scope.popularsearch)
		if (form.$invalid) {
			// invalid为false表示通过验证
			return false;
		} else {
			// 添加
			$api.postJson('popularsearch', $scope.popularsearch).then(saveSuccess);
		}
	}
	//停用,启用
	events.enable = function(item) {
		$api.putJson('popularsearch/status/' + item.id, {}).then(function(resp) {
			if (resp.data.code == 200) {
				init();
			} else {
				alert(resp.data.message);
			}
		});
	}
	//删除
	events.del = function(item) {
		$api.delete('popularsearch/' + item.id).then(function(resp) {
			if (resp.data.code == 200) {
				init();
			} else {
				alert(resp.data.message);
			}
		});
	}
	//显示排序div
	events.rank = function(item) {
        $('#rankModal').modal('show');
        $api.get('popularsearch/' + item.id).then(function(resp) {
			$scope.popularsearch = resp.data.data;
		});
    };
    
    //保存排序
	events.saveRank = function(rankForm) {
		rankForm.$submitted = true;
		console.log($scope.popularsearch)
		if (rankForm.$invalid) {
			// invalid为false表示通过验证
			return false;
		} else {
			$api.putJson('popularsearch/rank', $scope.popularsearch).then(saveSuccess);
		}
	}
	
	function saveSuccess(resp) {
		$('#addModal').modal('hide');
		$('#rankModal').modal('hide');
		if (resp.data.code == 200) {
			init();
		} else {
			alert(resp.data.message);
		}
	}
}