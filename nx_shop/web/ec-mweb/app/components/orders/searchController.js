angular
    .module('app')
    .controller('OrdersSearchController', OrdersSearchController);

OrdersSearchController.$inject = ['$scope', '$rootScope', '$location', '$api', '$param', '$downloadRoot','$sys'];
function OrdersSearchController($scope, $rootScope, $location, $api, $param, $downloadRoot,$sys) {
    console.log('OrdersSearchController');
    $scope.page = {"pageSize":50,"currentPage":1};
    $rootScope.sideBarVisible = true;
    $scope.configSys = $sys;
    if($location.$$path.indexOf("pickup")!=-1){
        $scope.detailPath = "pickup";
    }else{
        $scope.detailPath = "orders";
    }
    $scope.pickupOrderStatusBus={
        ob_0:"",
        ob_1:"商家待收货",
        ob_2:"用户待提货",
        ob_3:"已自提",
        ob_4:"商家待审",
        ob_5:"商家已驳回",
        ob_6:"管理端待收",
        ob_7:"已取消",
        ob_8:"已退款"
    };
    var statusMap = [
        {status: 1, name: '未付款'},
        {status: 2, name: '已付款'},
        {status: 3, name: '已确认'},
        {status: 4, name: '已制单'},
        {status: 5, name: '已发货'},
        {status: 6, name: '已签收'},
        {status: 9, name: '已取消'}
    ];
    var typeMap = [
        {type: 1, name: '全款'},
        {type: 2, name: '分期'}
    ];
	var cancelStatusMap = [
		{type: 0, name: '已提交'},
        {type: 1, name: '已确认'},
        {type: 2, name: '已拒绝'},
        {type: 3, name: '待收货'},
        {type: 4, name: '已收货'},
        {type: 5, name: '完成'}
    ];
    function getPickups(){
        $api.get('pickups').then(function(resp) {
            $scope.businessList = resp.data.pickupPoint;
        });
    }
    getPickups();
    var searchInput= {};
    // 根据 URL 初始化表单
    function updateSearchInput() {
        searchInput = {
            skuName: '',
            no: '',
            mobile: '',
            startTime: '',
            endTime: '',
            status: '',
            payMethod: '',
			cancelStatus:'',
            pickupPointId:''
        }
        var params = {},
            search = $location.search();

        // 对参数值做 filter
        var hasInvalidParam = false;
        angular.forEach(search, function(value, key) {
            if (searchInput.hasOwnProperty(key)) {
                if (key === 'status') {
                    value = parseInt(value);
                    if (isNaN(value) || value < 0 || value > 9) {
                        hasInvalidParam = true;
                        value = '';
                    }
                }
                 
                if (key === 'page') {
                    value = parseInt(value);
                    if (isNaN(value) || value < 1) {
                        hasInvalidParam = true;
                        value = '';
                    }
                }
				 
                if (value!=''&&value!=undefined) {
                    searchInput[key] = value;
                    params[key] = value;
                }
            }
        });

        // 修正 URL
        if (hasInvalidParam) {
            $location.search(params);
            return;
        }

        // 检查是否点击快速连接，而非在表单操作导致的查询
        // 条件是参数只有 status, page的组合
        // 如果是，不要更新表单（会把大部分字段初始化）
        if (checkIfQuickSearch(params)) {
            return;
        }
    }

    /**
     * 查询订单列表
     * @param  {Object} params 如果有参数，则不从搜索框拼装查询参数
     * @scope $scope.params $scope.isQuickSearch $scope.pagination
     */
    function search(os,lm) {
    	var params = {"offset": os,"limit":lm};
        // 从 URL 读取查询参数
        angular.forEach($location.search(), function(value, key) {
            if (searchInput.hasOwnProperty(key) && value !== '') {
                params[key] = value;
            }
        });

        // 是否快速查询（只有 status, page的组合）
        $scope.params = params;
        $scope.isQuickSearch = checkIfQuickSearch(params);
        if($scope.detailPath == "pickup"){
            params.pickup = "1";
        }
        // 发送请求
        $api.get('orders', params).then(function(resp) {
            var orders = resp.data.data.rows;
            $scope.orders = orders;
            $scope.page.totalCount = resp.data.data.total;
            
            //if($scope.detailPath == "pickup"){   //自提订单状态
            //    angular.forEach($scope.orders, function(order) {
            //        order.statusText = orderStatusBus["ob_"+order.pickupStatus];
            //    });
            //
            //}
            changed();
            $("#checkall").prop('checked',false);
        });
    }

    /**
     * 获取选中行的id
     */
    function getCheckedIds() {
        var ids = [];
        angular.forEach($scope.orders, function(order) {
            if (order.checked) {
                ids.push(order.id);
            }
        });
        return ids;
    }

    /**
     * 选中行变化，选中行为，或者数据变化会调用此函数
     * @scope 改变 $scope.status
     */
    function changed() {
        // 所有选中行
        var checkedOrders = [];

        // 重置变量
        $scope.status = 0;

        angular.forEach($scope.orders, function(order) {
            if (order.checked) {
                checkedOrders.push(order);
            }
        });

        //根据是否已经全部勾选，判断全选checkbox的勾选状态
        if(checkedOrders.length !== $scope.orders.length || checkedOrders.length ===0){
        	$("#checkall").prop("checked",false);
        }
        else{
        	$("#checkall").prop("checked",true);
        }
        
        if ($scope.orders.length <= 0) {
            return;
        }
        
        // 没有选中行
        if (checkedOrders.length <= 0) {
            return;
        }

        // 检查选中行是否一致
        var statusSame = true;
        angular.forEach(checkedOrders, function(order) {
            if (order.status != checkedOrders[0].status && order.cancelStatus != checkedOrders[0].cancelStatus) {
                statusSame = false;
            }
            if (!statusSame) {
                return;
            }
        });

        // 如果选中行状态一致
        if (statusSame) {
            $scope.status = checkedOrders[0].status;
			if(checkedOrders[0].cancelStatus==undefined)
			{
				$scope.cancelStatus = '';
			}else {
				$scope.cancelStatus =checkedOrders[0].cancelStatus;
			}
        }

    }

    /**
     * 是否快速查询（只有 status, page的组合）
     * @return {Boolean}
     */
    function checkIfQuickSearch(params) {
        return !(params.skuName ||
            params.no ||
            params.mobile ||
            params.startTime ||
            params.endTime ||
            params.payMethod);
    }

    /**
     * 修改状态机的状态
     */
    function transferState(toState) {
        var ids = getCheckedIds();
		
        if (ids.length <= 0) {
            return;
        }
		if(toState == 3){
			 $api.putJson('orders/enter', ids).then(function(resp) {
            // 成功，刷新数据
            if (resp.data.code == 200) {
            	alert('操作成功');
                search(0,$scope.page.pageSize);
            }else{
				alert(resp.data.exception)
			}
        });
		}
       if(toState==5){
		$api.putJson('orders/send', ids).then(function(resp) {
            // 成功，刷新数据
            if (resp.data.code == 200) {
            	alert('操作成功');
                search(0,$scope.page.pageSize);
            }else{
				alert(resp.data.exception)
			}
        });
	   }
    }

  	/**
	 * 制单导出
	 */
	function exportOrder() {
		var ids = getCheckedIds();
		$api.get('excelsMessage', {
			excelId: 'uporders',
			ids: ids
		}).then(function(resp) {
			// 成功，刷新数据
			if(resp.data.code == 200) {
				alert('操作成功');
				// 打开Excel
				window.open($downloadRoot + "/excels?excelId=uporders&ids=" + ids);
			} else {
				alert('操作失败');
				console.log(resp.data)
			}
		});
	}

    /**
     * 查询结果导出
     */
    function exportAll() {
		$scope.params['excelId'] = 'order';
        $api.get('excelsMessage', $scope.params).then(function(resp) {
            if (resp.data.code == 200) {
            	
				$scope.params.offset = 0;
				$scope.params.limit = $scope.page.totalCount;
                // 打开Excel
               var urlStr ='';
				for(i in $scope.params){
					urlStr+=i+'='+$scope.params[i]+'&';
				}
				window.open($downloadRoot+ "/excels?"+urlStr) ;
				
            }else{
            	alert('导出出错,请联系管理员');
            }
        });
    }

    /**
     * 提交表单，会更新地址栏
     */
    function updateLocation() {
    	var params = {};
        angular.forEach(searchInput, function(value, key) {
            if (value !== '' && value !== undefined && value !== null) {
                params[key] = value;
            }
        });
        $location.search(params);
    }

    function init() {
        // 初次访问页面，URL 参数填入表单，并发起搜索
        updateSearchInput();
        search(($scope.page.currentPage-1)*$scope.page.pageSize,$scope.page.pageSize);

        // URL 变化，如果只有 stauts 字段，不更新查询表单，为快速查询
        $scope.$on('$locationChangeSuccess', function(event, newValue, oldValue) {
            if ($location.path() != '/orders' && $location.path() != '/pickup') {
                return;
            }

            search(($scope.page.currentPage-1)*$scope.page.pageSize,$scope.page.pageSize);
        });
    }

    init();

    // 所有选中行一致的状态，如果没有选中，或者不一致则为0
    $scope.status = 0;
	$scope.cancelStatus = '';
    // 提交表单
    $scope.updateLocation = updateLocation;
    $scope.exportOrder = exportOrder;
    $scope.transferState = transferState;
    $scope.searchInput = searchInput;
    $scope.statusMap = statusMap;
	$scope.cancelStatusMap = cancelStatusMap;
    $scope.typeMap = typeMap;
    $scope.changed = changed;
    $scope.exportAll = exportAll;

    //分页
	//教程 http://www.cnblogs.com/pilixiami/p/5634405.html
	$scope.pageChanged = function(){
		search(($scope.page.currentPage-1)*$scope.page.pageSize,$scope.page.pageSize);
	}
	
	//全选
	$scope.checkAll = function (){
	    $("input[type='checkbox']").prop('checked', $("#checkall").prop('checked'));
	    angular.forEach($scope.orders, function(entity) {
	    	if($("#checkall").prop('checked')){
	    		entity.checked = true;
	    	}
	    	else{
	    		entity.checked = false;
	    	}
        });
	    changed();
	}
}
