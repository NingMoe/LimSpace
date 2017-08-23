angular
	.module('app')
	.controller('PromotionController', PromotionController);

PromotionController.$inject = ['$scope', '$api'];

function PromotionController($scope, $api) {
	var events = {};
	$scope.events = events;
	$scope.init = init;
	init();

	function init() {
		$api.get('promotions', {}).then(function(resp) {
			$scope.promotionList = resp.data.data.rows;
		});
		$("#promotionBeginTime").datetimepicker({format:"Y-m-d H:i",step:1});
		$("#promotionExpireTime").datetimepicker({format:"Y-m-d H:i",step:1});
	}

	//添加
	events.add = function() {
			$scope.promotion = {};
			$('#addModal').modal('show');

		}
		//修改
	events.modify = function(item) {
		$('#addModal').modal('show');
		$api.get('promotions/' + item.id).then(function(resp) {
			$scope.promotion = resp.data.data;
		});
	}
	events.save = function(form) {
		form.$submitted = true;
		console.log($scope.promotion)
		if (form.$invalid) {
			// invalid为false表示通过验证
			return false;
		} else {
			if ($scope.promotion.type==undefined) {
				alert("请选择抽奖方式");
				return false;
			}

			if ($scope.promotion.newcomer==undefined) {
				alert("请选择参与用户");
				return false;
			}
			// 修改
			if ($scope.promotion.id) {
				$api.putJson('promotions', $scope.promotion).then(saveSuccess);
				// 添加
			} else {
				$api.postJson('promotions', $scope.promotion).then(saveSuccess);
			}
		}
	}
	events.enable = function(item) {
		$api.putJson('promotions/' + item.id, {}).then(function(resp) {
			if (resp.data.code == 200) {
				init();
			} else {
				alert(resp.data.exception);
			}
		});
	}
	events.del = function(item) {
		$api.delete('promotions/' + item.id).then(function(resp) {
			if (resp.data.code == 200) {
				init();
			} else {
				alert(resp.data.exception);
			}
		});
	}

	function saveSuccess(resp) {
		$('#addModal').modal('hide');
		if (resp.data.code == 200) {
			init();
		} else {
			alert(resp.data.exception);
		}

	}
}