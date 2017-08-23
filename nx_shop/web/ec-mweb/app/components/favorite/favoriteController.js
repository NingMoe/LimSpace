angular
	.module('app')
	.controller('favoriteController', favoriteController);

RaffleController.$inject = ['$scope', '$api', '$downloadRoot'];

function favoriteController($scope, $api, $downloadRoot) {
	var events = {};
	var param = {};
	$scope.type ="";
	$scope.events = events;
	$scope.init = init;
    $scope.page = {"pageSize":10,"currentPage":1};
	init();

	function init(){
		$scope.type = "today" ;
		var param = {"type":"today","offset": 0,"limit":$scope.page.pageSize};
		$api.get('favorite',param).then(function(resp) {
			$scope.favoriteList = resp.data.data.rows;
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
		$api.get('favorite',param).then(function(resp) {
			$scope.favoriteList = resp.data.data.rows;
			$scope.page.totalCount = resp.data.data.total;	
		});
	}
	
	events.exportAll = function () {
	 	var type = param.type;
	 	var sTime = param.startTime;
	 	var eTime = param.endTime;
	 	var downLoadURL = $downloadRoot+ "/excels?excelId=favorite&type="+type;
	 	if(sTime!=null&&sTime!=undefined&&eTime!=null&&eTime!=undefined){
	 		downLoadURL +="&startTime="+sTime+"&endTime="+eTime;
	 	}
		window.open(downLoadURL) ;
    }
	
	//分页
	//教程 http://www.cnblogs.com/pilixiami/p/5634405.html
	$scope.pageChanged = function(){
		events.search($scope.type,($scope.page.currentPage-1)*$scope.page.pageSize,$scope.page.pageSize);
	}
}