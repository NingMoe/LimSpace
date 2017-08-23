angular
	.module('app')
    .controller('OrderMgController', OrderMgController);
OrderMgController.$inject = ['$rootScope','$scope', '$api','$stateParams','$location','$sys'];
function OrderMgController($rootScope,$scope, $api, $stateParams, $location,$sys) {
   $("#startTime").datetimepicker({format:"Y-m-d H:i:m",step:1});
   $("#endTime").datetimepicker({format:"Y-m-d H:i:m",step:1});
   $scope.page = {"pageSize":50,"currentPage":1};
	console.log(OrderMgController);
	var events = {};
	var params = {
			id : "",
			erpCode : "koo",
		    skuName : "",
		    no : "",  
		    startTime : "",
		    endTime : "",
		    mobile : "",
		    status : "",
		    offset: 0,
    	    limit:50
		};
	$scope.isShow = true;
	function initOrders(){
		 params = {
			id : "",
			erpCode : "koo",
		    skuName : "",
		    no : "",  
		    startTime : "",
		    endTime : "",
		    mobile : "",
		    status : "",
		    offset: 0,
    	    limit:50
		};
		$api.postJson('findKooOrders',params).then(function(resp){
	    	console.log(resp.data);
			var orders  = resp.data.rows;
   		    $scope.orders = orders;
			$scope.page.totalCount = resp.data.total;
		});
	}
	initOrders();
	/* 搜索 */
	events.search = orderList;
	/* 全部 */
	events.showAll = function(){
		$scope.searchInput = {};
		var os=0;
		var limt=50;
		orderList(os,limt);
	} 
	
	function orderList(os,limt){
		var searchInput = {};
		params = {
				id : $scope.searchInput.id||"",
				erpCode : "koo",
			    skuName : $scope.searchInput.skuName||"",
			    no : $scope.searchInput.no||"",  
			    startTime : $scope.searchInput.startTime||"",
			    endTime : $scope.searchInput.endTime||"",
			    mobile : $scope.searchInput.mobile||"",
			    status : $scope.searchInput.status||"",
			    offset:os||0,
	    	    limit:limt||50
		};
		$scope.isShow = true;
	    $api.postJson('findKooOrders',params).then(function(resp){
	    	console.log(resp);
			var orders  = resp.data.rows;
   		    $scope.orders = orders;
			$scope.page.totalCount = resp.data.total;
	    })
	}
	/* 已退款 */
	events.refund = function(os,limt) {
        $scope.isShow = false;
        params.erpCode = "koo";
//      params['status'] = 1; 
        // 发送请求
        $api.get('refunds', params).then(function(resp) {
            var refunds = resp.data.data;
            $scope.refunds = refunds.rows;
            $scope.page.totalCount = refunds.total;
        });
    }
    $scope.pageChanged = function(){
    	if($scope.isShow ==true){
    		events.searchQuery(($scope.page.currentPage-1)*$scope.page.pageSize,$scope.page.pageSize);
    	}else{
    		events.refundsQuery(($scope.page.currentPage-1)*$scope.page.pageSize,$scope.page.pageSize);
    	}
	   
	}
    events.searchQuery=function(os,limt){
    	if(os==undefined){
    		params.offset=0
    	}else{
    		params.offset=os;
    	}
    	if(limt==undefined){
    		params.limit=50
    	}else{
    		params.limit=limt;
    	}
    	$api.postJson('findKooOrders',params).then(function(resp){
	    	console.log(resp);
			var orders  = resp.data.rows;
   		    $scope.orders = orders;
			$scope.page.totalCount = resp.data.total;
	    })
    }
    events.refundsQuery=function(os,limt){
    	if(os==undefined){
    		params.offset=0
    	}else{
    		params.offset=os;
    	}
    	if(limt==undefined){
    		params.limit=5
    	}else{
    		params.limit=limt;
    	}
    	$api.get('refunds', params).then(function(resp) {
            var refunds = resp.data.data;
            $scope.refunds = refunds.rows;
            scope.page.totalCount = refunds.total;
        });
    }
	 $scope.events = events;
}