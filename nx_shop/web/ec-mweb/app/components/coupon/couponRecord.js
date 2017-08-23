/**
 * 优惠券记录
 */
angular
	.module('app')
	.controller('CouponRecordCtrl', CouponRecordCtrl);

CouponRecordCtrl.$inject = ['$scope', '$api', '$stateParams', '$downloadRoot', '$imgHost', '$rootScope', '$http', '$location'];

function CouponRecordCtrl($scope, $api, $stateParams, $downloadRoot, $imgHost, $rootScope, $http, $location) {
	console.log("CouponRecordCtrl");
	$rootScope.sideBarVisible = true;

	var params = {
		id: $stateParams.id
	};
	//优惠方式
	$scope.couType = [{
		id: 1,
		name: "折扣"
	}, {
		id: 0,
		name: "满减"
	}];
	//优惠券类别
	$scope.couponCates =[{
		id:1,
		name:"SKU专用"
	}, {
		id: 3,
		name: "活动专用"
	}, {
		id: 4,
		name: "全场通用"
	}, {
		id: 5,
		name: "品牌专用"
	}, {
		id: 6,
		name: "类别专用"
	}];
	function getData() {
		$api.get('couponRecord', params).then(function(resp) {
			$scope.couponRecords = resp.data.data.rows;
		});
	}
	getData();
	
	var searchInput = {
		limit:'',
		offset:''
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
        // 发送请求
        $api.get('couponRecord', params).then(function(resp) {
			$scope.couponRecords = resp.data.data.rows;
		});
    }
    
	$scope.JSON = JSON;
	$scope.imgHost = $imgHost;
	$scope.updateLocation = updateLocation;
	$scope.searchInput = searchInput;
}

/**
 * 优惠券记录详细
 */
angular
	.module('app')
	.controller('CouponRecordDetailCtrl', CouponRecordDetailCtrl);

CouponRecordDetailCtrl.$inject = ['$scope', '$api', '$stateParams', '$downloadRoot', '$imgHost', '$rootScope', '$http', '$location'];

function CouponRecordDetailCtrl($scope, $api, $stateParams, $downloadRoot, $imgHost, $rootScope, $http, $location) {
	console.log("CouponRecordDetailCtrl");
	$rootScope.sideBarVisible = true;

	var params = {
		id: $stateParams.couponRecordId
	};
	function getData() {
		$api.get('couponRecordDetail/'+params.id, '').then(function(resp) {
			$scope.couponRecord = resp.data.data;
		});
	}
	getData();
	$scope.JSON = JSON;
	$scope.imgHost = $imgHost;
}