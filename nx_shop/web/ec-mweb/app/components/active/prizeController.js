angular
	.module('app')
	.controller('PrizeController', PrizeController);

PrizeController.$inject = ['$scope', '$api', '$stateParams', '$imgHost', '$uploader'];

function PrizeController($scope, $api, $stateParams, $imgHost, $uploader) {
	var events = {};
	$scope.events = events;
	$scope.init = init;

	init();

	function init() {
		if($stateParams.promotionId == undefined){
			alert("请在活动中编辑奖项");
			window.location.href="#/promotion"
			return false ;
		}
		$api.get('promotions/' + $stateParams.promotionId, {}).then(function(resp) {
			$scope.promotionName = resp.data.data.name;
		});
		$api.get('promotions/' + $stateParams.promotionId + '/prize', {}).then(function(resp) {
			$scope.prizeList = resp.data.data;
		});
	}

	//添加
	events.add = function() {
			$scope.prize = {};
			$('#addModal').modal('show');
		}
		//修改
	events.modify = function(item) {
		$('#addModal').modal('show');
		$api.get('promotions/prize/' + item.id).then(function(resp) {
			$scope.prize = resp.data.data;
		});
	}
	events.save = function(form) {
		form.$submitted = true;
		if ('' == $scope.prize.virtualType) {
			$scope.prize.virtualType = null;
		}
		if (form.$invalid) {
			// invalid为false表示通过验证
			return false;
		} else {
			if ($scope.prize.upload != undefined && $scope.prize.upload != "") {
				$scope.prize.imageUrl = $scope.prize.upload.files[$scope.prize.upload.files.length - 1].path;
			}
			if ($scope.prize.imageUrl == "") {
				alert("请上传图片");
				return false;
			}
			$scope.prize['promotionId'] = $stateParams.promotionId;
			// 修改
			if ($scope.prize.id) {
				$api.putJson('promotions/prize/', $scope.prize).then(saveSuccess);
				// 添加
			} else {
				$api.postJson('promotions/prize/', $scope.prize).then(saveSuccess);
			}
		}
	}

	events.del = function(item) {
		$api.delete('promotions/prize/' + item.id).then(function(resp) {
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

	$scope.upload = {
		percent: 0,
		files: [],
		onFileUploaded: function(up, file) {
			$scope.prize.imageUrl = file.path;
			$('.upload-progress[data-id="' + file.id + '"]').remove();
			up.removeFile(file);
			this.files = [];
		}
	};
	$scope.imgHost = $imgHost;
	$scope.plupload = plupload;
}