angular
	.module('app')
	.controller('headlineController', headlineController);

headlineController.$inject = ['$scope', '$api'];

function headlineController($scope, $api) {
	var events = {};
	$scope.events = events;
	$scope.init = init;
	init();
	//数据初始化
	function init() {
		$api.get('headline', {}).then(function(resp) {
			$scope.headlineList = resp.data.data;
		});
		$("#iptstartTime").datetimepicker({format:"Y-m-d H:i:s",step:1});
		$("#iptendTime").datetimepicker({format:"Y-m-d H:i:s",step:1});
	}
	//添加显示表单
	events.add = function() {
		$scope.headline={"h4":"新建"};
		$('#addModal').modal('show');
		$('form input').removeAttr("disabled");
		$("#btnSave").removeAttr("disabled");
	}
	//修改,查看时显示表单
	events.show = function(item) {
		$('#addModal').modal('show');
		var headline = {
            id: item.id,
            title:item.title,
            startTime:item.startTime,
            endTime:item.endTime,
            link:item.link,
            status:item.status
       };
		$scope.headline = headline;
		if(item.status=='已结束'){
			$scope.headline.h4="查看";
			$('form input').attr("disabled","disabled");
			$("#btnSave").attr("disabled","disabled");
		}
		else{
			$scope.headline.h4="修改";
			$('form input').removeAttr("disabled");
			$("#btnSave").removeAttr("disabled");
		}
	}	
	//修改,保存
	events.save = function(form) {
		form.$submitted = true;
		if (form.$invalid) {
			// invalid为false表示通过验证
			return false;
		} 
		else {					
			var item = $scope.headline;
			if(item.status=='未开始'||item.status=='进行中'){
				$api.putJson('headline',$scope.headline).then(saveSuccess);
			}
			else{
				
				// 添加
				$api.postJson('headline', $scope.headline).then(saveSuccess);
			}
		}
	}
	//结束，删除
	events.del = function(item) {
		//状态：（未开始---修改，结束）（进行中---修改，结束）（已结束---查看，删除）
		$api.delete('headline/'+item.id,{}).then(function(resp) {
			if (resp.data.code == 200) {
				init();
			} else {
				alert(resp.data.message);
			}
		});
	}
	function saveSuccess(resp) {
		$('#addModal').modal('hide');
		if (resp.data.code == 200) {
			init();
		} else {
			alert(resp.data.message);
		}
	}
}