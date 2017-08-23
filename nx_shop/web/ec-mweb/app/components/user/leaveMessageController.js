angular
	.module('app')
	.controller('leaveMessageController', leaveMessageController);

leaveMessageController.$inject = ['$scope', '$api', '$stateParams', '$downloadRoot', '$imgHost', '$rootScope', '$http', '$location'];

function leaveMessageController($scope, $api, $stateParams, $downloadRoot, $imgHost, $rootScope, $http, $location) {
	
	var events = {};
	var params = {};
	$scope.type = "";
	$scope.events = events;
	$scope.init = init;
    $scope.page = {"pageSize":10,"currentPage":1};
    $scope.status = "";
    $scope.typeId = "";
	init();

	function init(){
		var param = {"offset":0,"limit":$scope.page.pageSize};
		$api.get('leaveMessage1',param).then(function(resp) {
			$scope.leaveMessage1 = resp.data.data.rows;
			$scope.page.totalCount = resp.data.data.total;			
		});
		$("#searchInputStartTime").datetimepicker({format:"Y-m-d",step:1});
		$("#searchInputEndTime").datetimepicker({format:"Y-m-d",step:1});
	}
	
	events.search = function(type,offset,limit){
		$scope.type = type ;
		var searchInput = $scope.searchInput ; 
		if(typeof offset === "undefined"){
			offset = ($scope.page.currentPage-1)*$scope.page.pageSize;
		}
		if(typeof limit === "undefined"){
			limit = $scope.page.pageSize;
		}
		var param = {"type":type,"offset": offset,"limit":limit};
		if($scope.status){
			param.status = $scope.status;	
		}
		if($scope.typeId){
			param.typeId = $scope.typeId;	
		}
		if(type=="timerange"){
			var sTime = searchInput.startTime;
			var eTime = searchInput.endTime;
			if (sTime==null||sTime==undefined||eTime==null||eTime==undefined) {
				alert('请输入完整的时间段进行查询');
				return;
			} 
			param.startTime =sTime;
			param.endTime =eTime;
		}
		$api.get('leaveMessage1',param).then(function(resp) {
			$scope.leaveMessage1 = resp.data.data.rows;
			$scope.page.totalCount = resp.data.data.total;	
		});
	};
//	导出
	events.exportAll = function () {
	 	var downLoadURL = $downloadRoot+ "/excels?excelId=leaveMessage";
		window.open(downLoadURL) ;
    }
//	根据状态刷新列表
	events.status = function (){
		var param = {"offset":0,"limit":$scope.page.pageSize};
		if($scope.type){
			param['type'] = $scope.type;
		}
		if($scope.typeId){
			param['typeId'] = $scope.typeId;
		}
		if($scope.searchInput){
			var sTime = $scope.searchInput.startTime;
			var eTime = $scope.searchInput.endTime;
			if (sTime==null||sTime==undefined||eTime==null||eTime==undefined) {
				alert('请输入完整的时间段进行查询');
				return;
			} 
			param.startTime =sTime;
			param.endTime =eTime;
		}
		if($scope.status){
		   param['status'] = $scope.status;
		}
		$api.get("leaveMessage1",param).then(function(resp){
		      if(resp.status ==200){
		        $scope.leaveMessage1 =resp.data.data.rows;
		        $scope.page.totalCount = resp.data.data.total;
		      }else{
		        alert(resp.statusText);
		      }
		    });
	};
//	根据类型刷新列表
	events.typeId = function (){
		var param = {"offset":0,"limit":$scope.page.pageSize};
		if($scope.type){
			param['type'] = $scope.type;
		}
		if($scope.status){
			param['status'] = $scope.status;
		}
		if($scope.searchInput){
			var sTime = $scope.searchInput.startTime;
			var eTime = $scope.searchInput.endTime;
			if (sTime==null||sTime==undefined||eTime==null||eTime==undefined) {
				alert('请输入完整的时间段进行查询');
				return;
			} 
			param['startTime'] =sTime;
			param['endTime'] =eTime;
		}
		if($scope.typeId){
	       param['typeId'] = $scope.typeId;
		}
		$api.get("leaveMessage1",param).then(function(resp){
		      if(resp.status ==200){
		        $scope.leaveMessage1 =resp.data.data.rows;
		        $scope.page.totalCount = resp.data.data.total;
		      }else{
		        alert(resp.statusText);
		      }
		    });
	}
	
//	回复
	events.reply = function(item) {
        $scope.addperm = {};
        $('#addModal').modal('show');
        var message = {
			mobile : item.mobile,
			name : item.name,
			content : item.content,
			id : item.id,
			replyContent : item.replyContent,
			replyTime : item.replyTime
		}
		$scope.message = message;
    };
//  回复保存
    events.save = function(form) {
        form.$submitted = true;
        if (form.$invalid) {
            return false;
        }
	   	$api.putJson('leaveMessage',{"replyContent":$scope.message.replyContent,"id":$scope.message.id}).then(function(resp){
	   		if(resp.data.data == true){
		  		$('#addModal').modal('toggle');
		  		init();
		  	}else{
		  		alert(resp.message);
		  	}
	   	});
    };
//  删除
    events.remove = function(item){
    	var r=confirm("确定要删除吗？");
		if (r==true){
		 	$api.delete('leaveMessage/'+item.id).then(function(resp) {
	   			if(resp.data.data == true){
			  		alert("删除成功！");
			  	}else{
			  		alert(resp.message);
			  	}
	   			init();
			});
		}else{
		    alert("未删除！");
		}
    };
//  查看
    events.show = function(item) {
		var message = {
			mobile : item.mobile,
			name : item.name,
			content : item.content,
			id : item.id,
			replyContent : item.replyContent,
			replyTime : item.replyTime
		}
		$scope.message = message;
		$('#addModal').modal('show');
	}	
	
	//分页
	//教程 http://www.cnblogs.com/pilixiami/p/5634405.html
	$scope.pageChanged = function(){
		events.search($scope.type,($scope.page.currentPage-1)*$scope.page.pageSize,$scope.page.pageSize);
	}

}