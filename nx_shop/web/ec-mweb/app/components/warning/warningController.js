angular
	.module('app')
	.controller('warningController', warningController);

warningController.$inject = ['$scope', '$api', '$log'];

function warningController($scope, $api, $log) {
	$scope.param = 0;
	$scope.insert = insert;
	$scope.showForm1 = showForm1;
	$scope.showForm2 = showForm2;
	$scope.addWarningSku = addWarningSku;
	$scope.delWarningSku = delWarningSku;
	$scope.changedSku = changedSku;
	$scope.changedwSku = changedwSku;
	$scope.searchWarningUser = searchWarningUser;
	showForm1();
	$scope.fchat = new Object();
	$scope.fchat.replies = [{
		key: 0,
		value: ""
	}];
	//初始化时由于只有1条，所以不允许删除
	$scope.fchat.canDescReply = false;
	//增加联系人输入框
	$scope.fchat.incrReply = function($index) {
			$scope.fchat.replies.splice($index + 1, 0, {
				key: new Date().getTime(),
				value: ""
			}); // 用时间戳作为每个item的key
			// 增加新的回复后允许删除
			$scope.fchat.canDescReply = true;
		}
		//减少联系人输入框
	$scope.fchat.decrReply = function($index, warningType) {
			// 如果回复数大于1，删除被点击回复
			if($scope.fchat.replies.length > 0) {
				var params = {};
				params.warningType = warningType;
				var user = [];
				var User = {};
				User.id = $scope.fchat.replies[$index].key;
				user.push(User);
				params.userList = user;
				$api.postJson('/warning/user', params).then($scope.fchat.replies.splice($index, 1));
			}
			// 如果回复数为1，不允许删除
			if($scope.fchat.replies.length == 0) {
				//删除完最后一条之后添加一个输入框
				$scope.fchat.incrReply(0);
				$scope.fchat.canDescReply = false;
			}
		}
		//增加联系人到数据库
	function insert(warningType) {
		var params = {};
		params.warningType = warningType;
		params.inventory = $scope.param;
		var isCheck = true;
		var user = [];
		var mobliereg = /^(1[3|4|5|7|8])\d{9}$/
		for(var i = 0; i < $scope.fchat.replies.length; i++) {
			var User = {};
			var mobile = $scope.fchat.replies[i].value;
			if(mobliereg.test(mobile)) {
				User.mobile = mobile;
				user.push(User);
				$scope["error" + i] = "";
			} else {
				$scope["error" + i] = "请输入正确的手机号";
				isCheck = false;
			}
		}
		if(isCheck) {
			params.userList = user;
			$api.postJson('warning', params).then(function(resp) {
				saveSuccess(resp, warningType);
				//$("#form1 input[type='text']").attr("disabled", true);
			});
		}

	}
	//显示表格1
	function showForm1() {
		$('#form1').css("display", "inline");
		$('#form2').css("display", "none");
		$('#btnAll').css("background-color", "gray")
		$('#btnAll').attr('disabled', 'disabled');
		$('#btnPart').css("background-color", "#337ab7").removeAttr("disabled");
		searchWarningUser("sku_warning_all");

	}
	//显示表格2
	function showForm2() {
		$('#form2').css("display", "inline");
		$('#form1').css("display", "none");
		$('#btnPart').css("background-color", "gray").attr('disabled', "true");
		$('#btnAll').css("background-color", "#337ab7").removeAttr("disabled");
		searchWarningUser("sku_warning_part");
		//searchSku();
		searchWarningSku();
	}
	//查询全部Sku
	function searchSku() {
		var params = {
			"code": "home",
			"limit": "999",
			"level": "1"
		};
		// 发送请求
		var array = [];
		$api.get('tags/skus', params).then(function(resp) {
			var list = resp.data.list;
			angular.forEach(list, function(tag) {
				angular.forEach(tag.skus, function(sku) {
					array.push(sku);
				});
			});
		});
		$scope.skus = array;
		//$scope.skus.total = array.length ;
		//console.log($scope.skus);
	}
	//查询warningSku
	function searchWarningSku() {
		var params = {};
		// 发送请求
		$api.get('warning/sku', params).then(function(resp) {
			var warningSkus = resp.data.data;
			$scope.warningSkus = warningSkus;
			$scope.warningSkus.total = resp.data.data.length;
		});

	}
	//查询warningUser
	function searchWarningUser(warningType) {
		var params = {};
		params.warningType = warningType;
		var array = [];
		// 发送请求
		$api.get('warning/user', params).then(function(resp) {
			console.log(resp);
			var warningUsers = resp.data.data;
			if(warningUsers.length < 1) {
				array.push({
					key: 0,
					value: ""
				});
			} else {
				angular.forEach(warningUsers, function(item) {
					if(item) {
						var kv = {};
						kv.key = item.id;
						kv.value = item.mobile;
						array.push(kv);
					}
				});
			}
			if(array.length < 1) {
				array.push({
					key: 0,
					value: ""
				});
			}
			$scope.fchat.replies = array;
			//显示删除那妞
			$scope.fchat.canDescReply = true;
			$scope.param = resp.data.warningInventory;
		});

	}
	//添加产品的预警数
	function addWarningSku() {
		var params = {};
		params.warningType = "sku_warning_part";
		params.inventory = $scope.param;
		var skuIds=getCheckedIds($scope.skus);
		//params.skuIdList = getCheckedIds($scope.skus);
		if(!params.inventory || parseInt(params.inventory) < 1) {
			alert("请输入有效预警值！");
		} else {
			if(!skuIds || skuIds.length < 1) {
				alert("请选择sku！");
			} else {
				params.skuIdList=skuIds;
				// 发送请求
				$api.postJson('warning', params).then(saveSuccess);
				//wyy
				$scope.changeTags();
			}
		}
	}
	//删除产品的预警数
	function delWarningSku(id) {
		var params = {};
		if(id !== undefined) {
			var ids = [];
			ids.push(id);
			params.skuIdList = ids;
		} else {
			var ids=getCheckedIds($scope.warningSkus);
			if(ids.length<1){
				alert("请选择要删除的sku！");
				return false;
			}
			params.skuIdList = ids;
		}
		console.log(ids);
		// 发送请求
		$api.postJson('warning/sku', params).then(saveSuccess);
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

	function saveSuccess(resp, warningType) {
		if(resp.data.code == 200) {
			alert("操作成功！");
			if(warningType === "sku_warning_all") {
				showForm1();
			} else {
				showForm2();
			}
		} else {
			alert(resp.data.message);
		}
	}

	/*********************************/
	$scope.tagId = 0;
	$scope.level = 1;
	//标签
	$api.get("tags", {
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
		$api.get("Warning/skus/" + tagId).then(function(resp) {
			$scope.skus = resp.data.data;
		});
	};
	/*********************************/

	//全选sku
	$scope.checkAllSku = function() {
			$("input[type='checkbox'].cbSkus").prop('checked', $("#checkallSkus").prop('checked'));
			angular.forEach($scope.skus, function(entity) {
				if($("#checkallSkus").prop('checked')) {
					entity.checked = true;
				} else {
					entity.checked = false;
				}
			});
			changedSku();
		}
		//选中行变化，选中行为，或者数据变化会调用此函数
	function changedSku() {
		// 所有选中行
		var checkedSkus = [];
		angular.forEach($scope.skus, function(item) {
			if(item.checked) {
				checkedSkus.push(item);
			}
		});
		//根据是否已经全部勾选，判断全选checkbox的勾选状态
		if(checkedSkus.length !== $scope.skus.length || checkedSkus.length === 0) {
			$("#checkallSkus").prop("checked", false);
		} else {
			$("#checkallSkus").prop("checked", true);
		}
	}
	//全选warningSku
	$scope.checkAllwSku = function() {
			$("input[type='checkbox'].cbwSkus").prop('checked', $("#checkallwSkus").prop('checked'));
			angular.forEach($scope.warningSkus, function(entity) {
				if($("#checkallwSkus").prop('checked')) {
					entity.checked = true;
				} else {
					entity.checked = false;
				}
			});
			changedwSku();
		}
		//选中行变化，选中行为，或者数据变化会调用此函数
	function changedwSku() {
		// 所有选中行
		var checkedwSkus = [];
		angular.forEach($scope.warningSkus, function(item) {
			if(item.checked) {
				checkedwSkus.push(item);
			}
		});
		//根据是否已经全部勾选，判断全选checkbox的勾选状态
		if(checkedwSkus.length !== $scope.warningSkus.length || checkedwSkus.length === 0) {
			$("#checkallwSkus").prop("checked", false);
		} else {
			$("#checkallwSkus").prop("checked", true);
		}
	}

	//$scope.fchat.incrReply(0);
}