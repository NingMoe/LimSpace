//定时上下架
angular
	.module('app')
	.controller('ShelvesAddController', ShelvesAddController);

ShelvesAddController.$inject = ['$rootScope', '$scope', '$api'];

function ShelvesAddController($rootScope, $scope, $api) {
	$scope.skutype = 3;
	$scope.changedSku = changedSku;
	$scope.tagId = 0;
	$scope.level = 1;
	
	var pagination = {
		pageSize: 10,
		zIndex: 1,
		totalPage: 1
	};
	$scope.pagination = pagination;
	
	//时间控件
	$("#onTime").datetimepicker({
		format: "Y-m-d H:i",
		step: 1
	});
	$("#offTime").datetimepicker({
		format: "Y-m-d H:i",
		step: 1
	});

	//加载页面初始化参数
	$scope.Inits = function(index) {
		var index = parseInt(index);
		if(index < 1 || index > $scope.pagination.totalPage || !index) {
			alert("输入正确的页数");
			return false;
		}
		$scope.pagination.zIndex = index;
		var params = {
			limit: pagination.pageSize,
			offset: (pagination.zIndex - 1) * pagination.pageSize
		};
		// 发送请求
		$api.get('timingShelves', params).then(function(resp) {
			$scope.skus = resp.data.data;
			$scope.pagination.totalPage = Math.ceil(resp.data.total / $scope.pagination.pageSize) || 1;
		   
		});
	}

	//标签
	$api.get("/tags", {
		"parentId": 0
	}).then(function(resp) {
		$scope.tagsArray1 = resp.data.data;
	});

	$scope.changeTags = function(level, tagId) {
		if(tagId) {
			$scope.tagId = tagId;
		} else {
			tagId = $scope.tagId;
		}
		if(level) {
			$scope.level = level;
		} else {
			level = $scope.level;
		}
		var data = $scope["tagsArray" + level];
		var tagL = parseInt(level) + 1;
		if(!tagId) {
			for(var i = tagL; i < 5; i++) {
				$scope["tagsArray" + i] = [];
				$scope["tagType" + i] = "";
				$scope.skuList = [];
			}
			return;
		}
		angular.forEach(data, function(value, key) {
			if(value.id == tagId) {
				$scope["tagsArray" + (tagL)] = value.nodes;
				$scope["tagType" + tagL] = "";
				for(var i = tagL + 1; i < 5; i++) {
					$scope["tagType" + i] = "";
					$scope["tagsArray" + i] = [];
				}
				if(level == 1) {
					$scope.deepth = value.treeDepth || 4;
				}
			}
		});

		var skutype = $scope.skutype;

		$api.get("/TimeSku/skus/" + tagId).then(function(resp) {
			var skus = [];
			angular.forEach(resp.data.data, function(sku) {
				switch(parseInt(skutype)) {
					//上架
					case 1:
						if(!sku.status) {
							skus.push(sku);
						}
						break;
						//下架
					case 2:
						if(sku.status) {
							skus.push(sku);
						}
						break;
					case 3:
						skus.push(sku);
						break;
				}

			});
			$scope.skuList = skus;

		});
	};

	$scope.raioChange = function(index) {
		for(var i = 1; i < 3; i++) {
			$scope["tagType" + i] = "";
			$scope.skuList = [];
			$scope.deepth = 1;
			$scope.onTime = "";
			$scope.offTime = "";
			$scope.errorDateMess1 = "";
			$scope.errorDateMess2 = "";
			$scope.errorDateMess3 = "";
		}
		$scope.changeTags();
	};

	//选中行变化，选中行为，或者数据变化会调用此函数
	function changedSku() {
		// 所有选中行
		var checkedSkus = [];
		angular.forEach($scope.skuList, function(item) {
			if(item.checked) {
				checkedSkus.push(item);
			}
		});
		//根据是否已经全部勾选，判断全选checkbox的勾选状态
		if(checkedSkus.length !== $scope.skuList.length || checkedSkus.length === 0) {
			$("#checkallSkus").prop("checked", false);
		} else {
			$("#checkallSkus").prop("checked", true);
		}
	}

	//全选sku
	$scope.checkAllSku = function() {
		$("input[type='checkbox'].cbSkus").prop('checked', $("#checkallSkus").prop('checked'));
		angular.forEach($scope.skuList, function(entity) {
			if($("#checkallSkus").prop('checked')) {
				entity.checked = true;
				$scope.errorDateMess3 = "";
			} else {
				entity.checked = false;
			}
		});
		changedSku();
	}

	//获取选中行的id
	function getCheckedIds(list) {
		var ids = [];
		angular.forEach(list, function(item) {
			if(item.checked) {
				ids.push(item.id);
			}
		});
		return ids;
	}

	//添加上下架
	$scope.addSkuTime = function() {
		var isCheck = true;
		var timeType = parseInt($scope.skutype);
		var offTime = "";
		var onTime = "";
		switch(timeType) {
			case 1:
				if(!$scope.onTime || $scope.onTime === "") {
					$scope.errorDateMess1 = "日期不能为空";
					isCheck = false;
				} else {
					onTime = $scope.onTime;
					$scope.errorDateMess1 = "";
				}
				break;
			case 2:
				if(!$scope.offTime || $scope.offTime === "") {
					$scope.errorDateMess2 = "日期不能为空";
					isCheck = false;
				} else {
					offTime = $scope.offTime;
					$scope.errorDateMess2 = "";
				}
				//下架
				break;
			case 3:
				if(!$scope.onTime) {
					$scope.errorDateMess1 = "日期不能为空";
					isCheck = false;
				} else {
					$scope.errorDateMess1 = "";
				}
				if(!$scope.offTime) {
					$scope.errorDateMess2 = "日期不能为空";
					isCheck = false;
				} else {
					$scope.errorDateMess2 = "";
				}
				if($scope.onTime && $scope.offTime) {
					if($scope.onTime >= $scope.offTime) {
						$scope.errorDateMess1 = "上架时间不能小于下架时间";
						isCheck = false;
					} else {
						onTime = $scope.onTime;
						offTime = $scope.offTime;
						$scope.errorDateMess1 = "";
					}
				}
				//上下架
				break;
		}
		var skuIds = getCheckedIds($scope.skuList);
		if(!skuIds || skuIds == undefined || skuIds.length < 1) {
			$scope.errorDateMess3 = "请选择sku";
			isCheck = false;
		} else {
			$scope.errorDateMess3 = "";
		}
		//验证数据通过
		if(isCheck) {
			var params = {};
			params.offTime = offTime;
			params.onTime = onTime;
			params.skuIdList = skuIds;
			// 发送请求
			$api.postJson('timingShelves', params).then(function(resp) {
				if(resp.data.code === 200) {
					alert('操作成功！');
					$scope.changeTags();
					$scope.Inits(1);
				}
			});
		}
	}

	//删除单个上下架
	$scope.delSSS = function(sssm) {
		if(confirm("确定要删除:" + sssm.sku.name + "么？")) {
			$api.delete('timingShelves/del/' + sssm.id).then(function(resp) {
				if(resp.data.code === 200) {
					alert('操作成功！');
					$scope.Inits(1);
					$scope.changeTags();
				}
			});
		}
	}

	$scope.Inits(1);

}