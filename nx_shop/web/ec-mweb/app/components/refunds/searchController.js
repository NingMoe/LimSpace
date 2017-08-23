angular
    .module('app')
    .controller('RefundsSearchController', RefundsSearchController);

RefundsSearchController.$inject = ['$scope', '$rootScope', '$location', '$api', '$param','$sys'];
function RefundsSearchController($scope, $rootScope, $location, $api, $param,$sys) {
    console.log('RefundsSearchController');
    $scope.page = {"pageSize":50,"currentPage":1};
    $rootScope.sideBarVisible = true;
    $scope.configSys = $sys;
    // 退款状态
    var statusMap = [
        {status: 1, name: '已退款'},
        {status: 0, name: '未退款'}
    ];

    // 退款类型
    var cancelStatusMap = [
        {cancelStatus: 1, name: '订单退款'},
        {cancelStatus: 2, name: '申请退货'}
    ];

    var searchInput= {};
    // 根据 URL 更新表单
    function updateSearchInput() {
        searchInput = {
            orderNo: '',
            status: '',
            cancelStatus: '',
            startTime: '',
            endTime: '',
            page: ''
        }

        var params = {},
            search = $location.search();

        // 对参数值做 filter
        var hasInvalidParam = false;
        angular.forEach(search, function(value, key) {
            if (searchInput.hasOwnProperty(key)) {
                if (key === 'status') {
                    if (value != 0 && value != 1) {
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

                if (value!=undefined&& value !='') {
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
     */
    function search(os,lm) {
    	var params = {"offset": os,"limit":lm};
        // 从 URL 读取查询参数
        angular.forEach($location.search(), function(value, key) {
            if (searchInput.hasOwnProperty(key) && value!=="") {
                params[key] = value;
            }
        });

        // 是否快速查询（只有 status, page的组合）
        $scope.params = params;
        $scope.isQuickSearch = checkIfQuickSearch(params);

        // 发送请求
        $api.get('/refunds', params).then(function(resp) {
            var refunds = resp.data.data;
            $scope.refunds = refunds.rows;
            $scope.page.totalCount = resp.data.data.total;
            changed();
        });
    }

    /**
     * 获取选中行的id
     */
    function getCheckedIds() {
        var ids = [];
        angular.forEach($scope.refunds, function(item) {
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
        var checkedRefunds = [];

        // 重置变量
        $scope.status = 0;

        angular.forEach($scope.refunds, function(item) {
            if (item.checked) {
                checkedRefunds.push(item);
            }
        });

        //根据是否已经全部勾选，判断全选checkbox的勾选状态
        if(checkedRefunds.length !== $scope.refunds.length || checkedRefunds.length ===0){
        	$("#checkall").prop("checked",false);
        }
        else{
        	$("#checkall").prop("checked",true);
        }
        
        if ($scope.refunds.length <= 0) {
            return;
        }
        
        // 没有选中行
        if (checkedRefunds.length <= 0) {
            return;
        }

        // 检查选中行是否一致
        var statusSame = true;
        angular.forEach(checkedRefunds, function(item) {
            if (item.status != checkedRefunds[0].status) {
                statusSame = false;
            }
            if (!statusSame) {
                return;
            }
        });

        // 如果选中行状态一致
        if (statusSame) {
            $scope.status = checkedRefunds[0].status;
        }
    }

    /**
     * 是否快速查询（只有 status ）
     * @return {Boolean}
     */
    function checkIfQuickSearch(params) {
        return !(params.orderNo ||
            params.cancelStatus ||
            params.startDate ||
            params.endDate);
    }

    /**
     * 进行退款操作
     */
    function doRefund(refund) {
        $api.putJson('refunds/enter',refund.id 
         ).then(function(resp) {
            // 成功，刷新数据
            if (resp.data.code == 200) {
                search(0,$scope.page.pageSize);
            } else {
                alert('退款失败');
				alert(resp.data.exception)
            }
        });
    }

    /**
     * 提交表单，会更新地址栏
     */
    function updateLocation() {
        var params = {};
        angular.forEach(searchInput, function(value, key) {
            if (value!=="") {
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
            if ($location.path() != '/refunds') {
                return;
            }

            search(($scope.page.currentPage-1)*$scope.page.pageSize,$scope.page.pageSize);
        });
    }

    init();

    $scope.updateLocation = updateLocation;
    $scope.searchInput = searchInput;
    $scope.statusMap = statusMap;
    $scope.cancelStatusMap = cancelStatusMap;
    $scope.changed = changed;
    $scope.doRefund = doRefund;
    $scope.Math = Math;
    
    //分页
	//教程 http://www.cnblogs.com/pilixiami/p/5634405.html
	$scope.pageChanged = function(){
		search(($scope.page.currentPage-1)*$scope.page.pageSize,$scope.page.pageSize);
	}
	
	//全选
	$scope.checkAll = function (){
	    $("input[type='checkbox']").prop('checked', $("#checkall").prop('checked'));
	    angular.forEach($scope.refunds, function(entity) {
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

