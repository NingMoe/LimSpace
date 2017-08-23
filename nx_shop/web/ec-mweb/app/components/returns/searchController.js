angular
    .module('app')
    .controller('ReturnsSearchController', ReturnsSearchController);
ReturnsSearchController.$inject = ['$scope', '$rootScope', '$location', '$api', '$param','$sys'];
function ReturnsSearchController($scope, $rootScope, $location, $api, $param,$sys) {
    console.log('ReturnsSearchController');
    $scope.page = {"pageSize":50,"currentPage":1};
    $rootScope.sideBarVisible = true;
    $scope.configSys = $sys;
    $scope.pickupOrderStatus={
        ob_0:"",
        ob_1:"商家待收货",
        ob_2:"用户待提货",
        ob_3:"已自提",
        ob_4:"商家待审",
        ob_5:"商家已驳回",
        ob_6:"管理端待收",
        ob_7:"已取消",
        ob_8:"已退款"};
    var statusMap1 = [
        {status: '0', name: '待确认'},
        {status: '2', name: '已取消'},
        {status: '3', name: '待收货'},
        {status: '4', name: '已收货'},
        {status: '5', name: '完成'}
    ];
	 var statusMap2 = [
        {status: '0', name: '待确认'},
		{status: '1', name: '已确认'},
        {status: '2', name: '已取消'}
    ];
    var searchInput= {};
    // 根据 URL 更新表单
    function updateSearchInput() {
        searchInput = {
            orderNo: '',
            cancelNo: '',
            startTime: '',
            endTime: '',
            status: '',
            page: ''
        }
        var params = {},
            search = $location.search();
        // 对参数值做 filter
        var hasInvalidParam = false;
        angular.forEach(search, function(value, key) {
            if (searchInput.hasOwnProperty(key)) {
                if (key === 'status') {
                    if (value != 0 && value != 3) {
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
				
                if (value) {
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
            if (searchInput.hasOwnProperty(key) && value) {
                params[key] = value;
            }
        });

        // 是否快速查询（只有 status, page的组合）
        $scope.params = params;
        $scope.isQuickSearch = checkIfQuickSearch(params);
		
		params.cancelType = $location.path() === '/returns' ? 2 : 1;

        // 发送请求
        $api.get('orders/returns', params).then(function(resp) {
            var returns = resp.data.data;
            $scope.returns = returns.rows;
            $scope.page.totalCount = resp.data.data.total;
            changed();
        });
    }

    /**
     * 获取选中行的id
     */
    function getCheckedIds() {
        var ids = [];
        angular.forEach($scope.returns, function(item) {
            if (item.checked) {
                ids.push(item.id);
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
        var checkedReturns = [];

        // 重置变量
        $scope.status = -1;

        angular.forEach($scope.returns, function(item) {
            if (item.checked) {
                checkedReturns.push(item);
            }
        });

        //根据是否已经全部勾选，判断全选checkbox的勾选状态
        if(checkedReturns.length !== $scope.returns.length || checkedReturns.length ===0){
        	$("#checkall").prop("checked",false);
        }
        else{
        	$("#checkall").prop("checked",true);
        }

        if ($scope.returns.length <= 0) {
            return;
        }
        
        // 没有选中行
        if (checkedReturns.length <= 0) {
            return;
        }

        // 检查选中行是否一致
        var statusSame = true;
        angular.forEach(checkedReturns, function(item) {
            if (item.status != checkedReturns[0].status) {
                statusSame = false;
            }
            if (!statusSame) {
                return;
            }
        });

        // 如果选中行状态一致
        if (statusSame) {
            $scope.status = checkedReturns[0].status;
        }
    }

    /**
     * 是否快速查询（只有 status, page的组合）
     * @return {Boolean}
     */
    function checkIfQuickSearch(params) {
        return !(params.skuName ||
            params.orderNo ||
            params.code ||
            params.startTime ||
            params.endTime);
    }

    /**
     * 修改状态机的状态
     */
    function transferState(toState) {
        var ids = getCheckedIds();
        if (ids.length <= 0) {
            return;
        }
	if(toState==1){
		$api.putJson('orders/returns/enter', ids).then(function(resp) {
            // 成功，刷新数据
            if (resp.data.code == 200) {
                search(0,$scope.page.pageSize);
            }else{
				alert(resp.data.exception)
			}
        });	
	}else if(toState==2){
        $api.putJson('orders/returns/refuse', ids).then(function(resp) {
            // 成功，刷新数据
            if (resp.data.code == 200) {
                search(0,$scope.page.pageSize);
            }else{
				alert(resp.data.exception)
			}
        });	
    }else if(toState==4){
        $api.putJson('orders/returns/delivery', ids).then(function(resp) {
            // 成功，刷新数据
			
            if (resp.data.code == 200) {
                search(0,$scope.page.pageSize);
            }else{
				alert(resp.data.exception)
			}
        });	
	}
	}
	
    /**
     * 提交表单，会更新地址栏
     */
    function updateLocation() {
        var params = {};
        angular.forEach(searchInput, function(value, key) {
            if (value) {
                params[key] = value;
            }
        });
        $location.search(params);
    }
	
	var path = $location.path();
    function init() {
        // 初次访问页面，URL 参数填入表单，并发起搜索
        updateSearchInput();
        search(($scope.page.currentPage-1)*$scope.page.pageSize,$scope.page.pageSize);

        // URL 变化，如果只有 stauts 字段，不更新查询表单，为快速查询
        $scope.$on('$locationChangeSuccess', function(event, newValue, oldValue) {
            if ($location.path() != path) {
                return;
            }

            search(($scope.page.currentPage-1)*$scope.page.pageSize,$scope.page.pageSize);
        });
    }

    init();
    $scope.updateLocation = updateLocation;
    $scope.transferState = transferState;
    $scope.searchInput = searchInput;
    $scope.statusMap1 = statusMap1;
	$scope.statusMap2 = statusMap2;
    $scope.changed = changed;
    
    //分页
	//教程 http://www.cnblogs.com/pilixiami/p/5634405.html
	$scope.pageChanged = function(){
		search(($scope.page.currentPage-1)*$scope.page.pageSize,$scope.page.pageSize);
	}
	
	//全选
	$scope.checkAll = function (){
	    $("input[type='checkbox']").prop('checked', $("#checkall").prop('checked'));
	    angular.forEach($scope.returns, function(entity) {
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
