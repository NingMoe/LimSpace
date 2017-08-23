/**
 *这里是优惠券列表模块
 **/
angular
	.module('app')
	.controller('CouponListCtrl', CouponListCtrl);

CouponListCtrl.$inject = ['$scope', '$api', '$stateParams', '$downloadRoot', '$imgHost', '$rootScope', '$http', '$location'];

function CouponListCtrl($scope, $api, $stateParams, $downloadRoot, $imgHost, $rootScope, $http, $location) {
	$rootScope.sideBarVisible = true;
	var pagination = {pageSize:10,
						zIndex:1,
						totalPage:1
					};
    $scope.pagination = pagination;
	$scope.searchInput =  {type:"",limit:pagination.pageSize,offset:(pagination.zIndex-1)*pagination.pageSize};

	$scope.getData = function(index) {
		var index = parseInt(index);
		if(index<1||index>$scope.pagination.totalPage||!index){
			alert("输入正确的页数");
			return false;
		}
		$scope.pagination.zIndex = index;
		$scope.searchInput.offset = (pagination.zIndex-1)*pagination.pageSize;
		$api.get('coupons', $scope.searchInput).then(function(resp) {
			$scope.couponInfos = resp.data.data.rows;
			$scope.pagination.totalPage = Math.ceil(resp.data.data.total/$scope.pagination.pageSize)||1;
		});
	}
	$scope.getData(1);

	//结束
	$scope.overCouponInfo = function(id){
		if (window.confirm('是否确定结束？')) {
			$api.put('coupons/stop/' + id).then(function(resp) {
				if (resp.data.code == 200) {
					alert("结束成功！");
					$scope.getData($scope.pagination.zIndex);
				}else{
					alert("结束失败！");
				}
			});
		}
	};
	/**
	 * 删除优惠券
	 */
	function deleteCouponInfo(id) {
		if (window.confirm('是否确定删除？')) {
			$api.delete('coupons/' + id, '').then(function(resp) {
				if (resp.data.code == 200) {
					alert("删除成功！");
					$scope.getData(1);
				}else{
					alert("删除失败！");
				}
			});
		}
	}
	$scope.exportCode = function(id){
		var params = {excetId:"couponRecord",couponId:id};
		window.open($downloadRoot+ "/excels?excelId=couponRecord&couponId="+id);
	};

    //活动状态
	$scope.couActivStas = [{
		id: 0,
		name: "未开始"
	}, {
		id: 1,
		name: "活动中"
	},{
		id: 2,
		name: "已结束"
	}];
	//优惠券类别
	$scope.couponType =[{
		id:1,
		name:"优惠券"
	},{
		id: 2,
		name: "优惠码"
	}, {
		id: 3,
		name: "代金券"
	}, {
		id: 4,
		name: "代金码"
	}];

	$scope.JSON = JSON;
	$scope.imgHost = $imgHost;
	$scope.deleteCouponInfo = deleteCouponInfo;

}

/**
 * 这里是优惠券详情模块
 * 
 */
angular
	.module('app')
	.controller('CouponInfoDetailCtrl', CouponInfoDetailCtrl);

CouponInfoDetailCtrl.$inject = ['$scope', '$api', '$stateParams', '$downloadRoot', '$imgHost', '$rootScope'];

function CouponInfoDetailCtrl($scope, $api, $stateParams, $downloadRoot, $imgHost, $rootScope) {
	$rootScope.sideBarVisible = true;
	var pagination = {pageSize:10,
		zIndex:1,
		totalPage:1
	};
	$scope.pagination = pagination;
	$scope.searchParams = {
		couponId: $stateParams.couponInfoId,
		limit:pagination.pageSize,
		offset:(pagination.zIndex-1)*pagination.pageSize
	};
	$scope.getData = function(index){
		var index = parseInt(index);
		if(index<1||index>$scope.pagination.totalPage||!index){
			alert("输入正确的页数");
			return false;
		}
		$scope.pagination.zIndex = index;
		$scope.searchParams.offset = (pagination.zIndex-1)*pagination.pageSize;
		$api.get('couponRecords', $scope.searchParams).then(function(resp) {
			$scope.couponRecords = resp.data.data.rows;
			$scope.pagination.totalPage = Math.ceil(resp.data.data.total/$scope.pagination.pageSize)||1;
		});
	}
	$scope.getData(1);
}
/**
 * 这里是添加优惠券模块
 * 
 */
angular
	.module('app')
	.controller('AddCouponCtrl', AddCouponCtrl);

AddCouponCtrl.$inject = ['$scope', '$api', '$stateParams', '$downloadRoot', '$imgHost', '$rootScope','$state','$filter'];

function AddCouponCtrl($scope, $api, $stateParams, $downloadRoot, $imgHost, $rootScope,$state,$filter) {
	console.log("AddCouponCtrl");
	$rootScope.sideBarVisible = true;
	var newCoupon = {};
	$scope.newCoupon = newCoupon;
	newCoupon.timeType = 2;
	newCoupon.type = 1;
	newCoupon.scope = 1;

     $("#receiveStartTime").datetimepicker({format:"Y-m-d H:i",step:1});
     $("#receiveEndTime").datetimepicker({format:"Y-m-d H:i",step:1});
     $("#couStartTime").datetimepicker({format:"Y-m-d H:i",step:1});
     $("#couEndTime").datetimepicker({format:"Y-m-d H:i",step:1});
	//标签
	$api.get("/tags",{"parentId":0}).then(function(resp){
		$scope.tagsArray1 = resp.data.data;
	});

	$scope.changeTags = function(level,tagId){
		var data = $scope["tagsArray"+level];
		var tagL = parseInt(level)+1;
		if(!tagId){
			for(var i = tagL;i<5;i++){
				$scope["tagsArray"+i] = [];
				$scope["tagType"+i] = "";
				$scope.skuList = [];
			}
			$scope.newCoupon.refId = "";
			return;
		}
		angular.forEach(data, function(value, key) {
			if(value.id==tagId ){
				$scope["tagsArray"+(tagL)] =value.nodes;
				$scope["tagType"+tagL]="";
				for(var i = tagL+1;i<5;i++){
					$scope["tagType"+i]="";
					$scope["tagsArray"+i] = [];
				}
				if(level==1){
					$scope.deepth = value.treeDepth||4;
				}

			}
		});
		if(newCoupon.scope=="2" && level==$scope.deepth){
			$api.get("/tags/"+tagId+"/skus").then(function(resp){
				$scope.skuList = resp.data.data;
				if($scope.skuList.length>0){
					$scope.newCoupon.refId = $scope.skuList[0].id;
				}
			});
		}else{
			$scope.skuList = [];
			$scope.newCoupon.refId = "";
		}
	};
	$scope.raioChange = function(){
		for(var i = 1;i<5;i++){
			$scope["tagType"+i] = "";
			$scope.skuList = [];
			$scope.deepth = 1;
		}
	};

	$scope.addCouponInfo = function() {
		var newCoupon = $scope.newCoupon;
		var errorDateMess1 ="";
		var errorDateMess2 ="";
		var errorMessage3="";
		$scope.scopeNowTime = $filter('date')(new Date(),'yyyy-MM-dd HH:mm');
		if(!newCoupon.receiveStartTime||!newCoupon.receiveEndTime){
			errorDateMess1 = "日期不能为空"
		}else if(newCoupon.receiveStartTime>newCoupon.receiveEndTime){
			errorDateMess1 = "开始日期不能大于结束日期";
		}else if(newCoupon.receiveStartTime<$scope.scopeNowTime){
			errorDateMess1 = "活动开始时间不能小于当前时间";
		}
		if(newCoupon.timeType==1){
			   if(!newCoupon.startTime||!newCoupon.expireTime){
				   errorDateMess2 = "日期不能为空";
			   }else if(newCoupon.startTime>newCoupon.expireTime){
				   errorDateMess2 = "开始日期不能大于结束日期";
			   }else if(newCoupon.startTime<newCoupon.receiveStartTime){
				   errorDateMess2 = "有效期开始时间不能早于活动开始时间";
			   }else if(newCoupon.expireTime<newCoupon.receiveEndTime){
				   errorDateMess2 = "有效期结束时间不能早于活动结束时间";
			   }

		}
		if(newCoupon.type==1||newCoupon.type==2){
			if(parseFloat(newCoupon.discount||0) >=parseFloat(newCoupon.threshold||0)){
				$scope.thresholdMess = true;
			}else{
				$scope.thresholdMess = false;
			}
		}
		$scope.errorDateMess1 = errorDateMess1;
		$scope.errorDateMess2 = errorDateMess2;


		if(newCoupon.scope =="3"){
			$scope.tagType1?newCoupon.refId = $scope.tagType1:"";
			$scope.tagType2?newCoupon.refId = $scope.tagType2:"";
			$scope.tagType3?newCoupon.refId = $scope.tagType3:"";
			$scope.tagType4?newCoupon.refId = $scope.tagType4:"";
		}
		if(!newCoupon.refId && newCoupon.scope !="1"){
			$scope.errorMessage3=errorMessage3= "error";
			return false;
		}
		if($scope.form.$invalid || errorDateMess1||errorDateMess2||errorMessage3||$scope.thresholdMess){
			return false;
		}else{
			$scope.form.$submitted = false;
		}

		$api.postJson('coupons', $scope.newCoupon).then(function(resp) {
			// 成功，刷新数据
			if (resp.data.code == 200) {
				console.log("success!!");
				alert("添加成功！");
				$state.go('coupon');
			}else{
				alert(resp.data.message+":"+resp.data.exception);
			}
		});
	}


}
/**
 * 这里是修改优惠券模块
 * 
 */
angular
	.module('app')
	.controller('EditCouponCtrl', EditCouponCtrl);

EditCouponCtrl.$inject = ['$scope', '$api', '$stateParams', '$downloadRoot', '$imgHost', '$rootScope','$state'];

function EditCouponCtrl($scope, $api, $stateParams, $downloadRoot, $imgHost, $rootScope,$state) {
	console.log("EditCouponCtrl");
	$rootScope.sideBarVisible = true;
	var orignCount;
	var params = {
		id: $stateParams.couponInfoId
	};

	function getData() {
		$api.get('coupons/' + params.id, {}).then(function(resp) {
			$scope.newCoupon = resp.data.data;
			orignCount = $scope.newCoupon.count;
            if($scope.newCoupon.scope==3){
				var tags = resp.data.tag;
				tags.reverse();
                //标签
				$api.get("/tags",{"parentId":0}).then(function(resp){
					$scope.tagsArray1 = resp.data.data;
					angular.forEach(tags,function(value,key){
						$scope["tagType"+(key+1)] =value+"";
						changeTags(key+1,value);
					});
				});
			}else if($scope.newCoupon.scope==2){
				var sku = [resp.data.sku];
				$scope.skuList = sku;
			}

			$(".group_create input,.group_create select,.group_create textarea").prop("disabled",true);
            if($scope.newCoupon.type==1||$scope.newCoupon.type==3){
				$(".group_create input[name=count]").prop("disabled",false);
			}

		});
	}
	function changeTags(level,tagId){
		var data = $scope["tagsArray"+level];
		var tagL = parseInt(level)+1;
		if(!tagId){
			for(var i = tagL;i<5;i++){
				$scope["tagType"+i]="";
				$scope["tagsArray"+i] = [];
			}
			return;
		}
		angular.forEach(data, function(value, key) {
			if(value.id==tagId ){
				$scope["tagsArray"+(tagL)] =value.nodes;
				$scope["tagType"+tagL]="";
				for(var i = tagL+1;i<5;i++){
					$scope["tagType"+i]="";
					$scope["tagsArray"+i] = [];
				}
				if(level==1){
					$scope.deepth = value.treeDepth||4;
				}

			}
		});

	}
	getData();


	function updateCouponInfo(){
        if(orignCount>$scope.newCoupon.count){
			alert("发放数量不能比原始数量小");
			return false;
		}
		$api.putJson('coupons', $scope.newCoupon).then(function(resp) {
			// 成功，刷新数据
			if (resp.data.code == 200) {
				console.log("update success!!!");
				alert("修改成功！");
				//跳转页面
				$state.go('coupon');
			}else{
				alert(resp.data.message);
			}
		});
	}

	$scope.updateCouponInfo = updateCouponInfo;
}

/**
 * 发送优惠券模块
 */
angular
	.module('app')
	.controller('SendCoupCtrl', SendCoupCtrl);

SendCoupCtrl.$inject = ['$scope', '$api', '$stateParams', '$downloadRoot', '$imgHost', '$rootScope','$state'];

function SendCoupCtrl($scope, $api, $stateParams, $downloadRoot, $imgHost, $rootScope,$state) {
	console.log("SendCoupCtrl");
	$rootScope.sideBarVisible = true;
	var params = {
		id: $stateParams.couponInfoId,
		couponName:$stateParams.couponInfoName
	};
	function sendCoup(){
		$api.get('sendCoup/'+params.id+'&'+params.sendMobile,'').then(function(resp) {
			if(resp.data.code==200){
				alert("发送成功");
			}else{
				alert(resp.data.message);
			}
		});
	}
	$scope.tempParam = params;
	$scope.sendCoup = sendCoup;
}