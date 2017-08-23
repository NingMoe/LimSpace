angular
    .module('app')
    .controller('BillController', BillController);

BillController.$inject = ['$scope', '$rootScope', '$location', '$api', '$param','$sys'];
function BillController($scope, $rootScope, $location, $api, $param,$sys) {
   $("#receiveStartTime").datetimepicker({format:"Y-m-d H:i:m",step:1});
   $("#receiveendTime").datetimepicker({format:"Y-m-d H:i:m",step:1});
    console.log('BillController');
     $scope.page = {"pageSize":50,"currentPage":1};
    var events = {};
    var stateMaps = [
        {id: 3, name: '请选择'},
        {id: 1, name: '购买'},
        {id: 0, name: '退单'}
    ];
    var param={
    	startCloseTime:'',
    	endCloseTime:'',
    	startReturnTime:'',
    	endReturnTime:'',
    	offset: 0,
    	limit:50
    	}
    /* 默认选中订单日期 */
    $scope.selected=stateMaps[0];
    function init(){
    	$api.get("findBillList",param).then(function(resp){
   		    var bills = resp.data.rows;
   		    $scope.bills = bills;
			$scope.page.totalCount = resp.data.total;
   	    });
    }
    init();
    events.search = function(os,limt){
    	var searchTime = {};
    	var searchInput = {};
	    	if($scope.searchInput!=undefined&&(($scope.searchInput.startTime!=undefined&&$scope.searchInput.startTime!="")
	    	||($scope.searchInput.endTime!=undefined&&$scope.searchInput.endTime!=""))&&($scope.selected==stateMaps[0])){
	    		alert("请选择类型！");
	    		return;
	    	}
    		if($scope.selected==stateMaps[1]){
	    		param.startReturnTime="";
	    		param.endReturnTime="";
	    		if($scope.searchInput!=undefined&&$scope.searchInput.startTime!=undefined){
	    			param.startCloseTime=$scope.searchInput.startTime;
	    		}
		    	
		    	if($scope.searchInput!=undefined&&$scope.searchInput.endTime!=undefined){
		    		param.endCloseTime=$scope.searchInput.endTime;
		    	}
		    	if($scope.searchInput==undefined||$scope.searchInput.startTime==undefined||$scope.searchInput.startTime==""){
		    		alert("请选择购买开始时间！");
		    		return;
		    	}
		    	if($scope.searchInput==undefined||$scope.searchInput.endTime==undefined||$scope.searchInput.endTime==""){
		    		alert("请选择购结束始时间！");
		    		return;
		    	}
	    	}else if($scope.selected==stateMaps[2]){
	    		param.startCloseTime="";
	    		param.endCloseTime="";
	    		if($scope.searchInput!=undefined&&$scope.searchInput.startTime!=undefined){
	    			param.startReturnTime=$scope.searchInput.startTime;
	    		}
		    	if($scope.searchInput!=undefined&&$scope.searchInput.endTime!=undefined){
		    		param.endReturnTime=$scope.searchInput.endTime;
		    	}
		    	if($scope.searchInput==undefined||$scope.searchInput.startTime==undefined||$scope.searchInput.startTime==""){
		    		alert("请选择退单开始时间！");
		    		return;
		    	}
		    	if($scope.searchInput==undefined||$scope.searchInput.endTime==undefined||$scope.searchInput.endTime==""){
		    		alert("请选择退单结束时间！");
		    		return;
		    	}
	    	}else{
    		  if($scope.selected!=stateMaps[0]){
					alert("请选择时间");
					return;
				}
	    	}
//  	}else{
//  		if($scope.selected!=stateMaps[0]){
//			alert("请选择时间");
//			return;
//		}
//  	}
		
    	if(os==undefined){
    		param.offset=0
    	}else{
    		param.offset=os;
    	}
    	if(limt==undefined){
    		param.limit=50
    	}else{
    		param.limit=limt;
    	}
	    $api.get('findBillList',param).then(function(resp){
	    	console.log(resp);
   		    var bills = resp.data.rows;
   		    $scope.bills = bills;
			$scope.page.totalCount = resp.data.total;
   		    
	    });		
	} 
	$scope.pageChanged = function(){
		events.query(($scope.page.currentPage-1)*$scope.page.pageSize,$scope.page.pageSize);
	}
	events.query=function(os,limt){
		if(os==undefined){
    		param.offset=0
    	}else{
    		param.offset=os;
    	}
    	if(limt==undefined){
    		param.limit=50
    	}else{
    		param.limit=limt;
    	}
		$api.get('findBillList',param).then(function(resp){
	    	console.log(resp);
   		    var bills = resp.data.rows;
   		    $scope.bills = bills;
			$scope.page.totalCount = resp.data.total;
   		    
	    });	
	}
	events.selectChanage=function(){
		if((select!=$scope.selected)||$scope.selected!=stateMaps[0]){
			if($scope.searchInput!=undefined&&($scope.searchInput.startTime!=""||$scope.searchInput.endTime!="")){
				$scope.searchInput.startTime="";
			    $scope.searchInput.endTime="";
			    param.startReturnTime="";
	    		param.endReturnTime="";
	    		param.startCloseTime="";
	    		param.endCloseTime="";
			}
		}
		
		var select = $scope.selected
	}
    $scope.stateMaps = stateMaps;
    $scope.events = events;
    
}
