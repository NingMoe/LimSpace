angular
	.module('app')
	.controller('CategoryTemplateController', CategoryTemplateController);

CategoryTemplateController.$inject = ['$scope', '$api', '$stateParams', '$downloadRoot', '$imgHost', 'ngToast'];

function CategoryTemplateController($scope, $api, $stateParams, $downloadRoot, $imgHost, ngToast) {
	var events = {};
	var id = $stateParams.id;

	var promise = $api.get('category/templates/list', {
		categoryId: id
	}).then(function(resp) {
		var data = resp.data.data;
		var attrIdMap = {},
			specIdMap = {};
		$.each(data, function(index, item) {
			if (item.attrName == null || item.attrName == '') {
				specIdMap[item.keyId] = 1;
			} else {
				attrIdMap[item.keyId] = 1;
			}
			if (item.value) {
				item.value = item.value.split(',');
			}
		});
		var attrArr = [];
		var specArr = [];
		for (i in data) {
			if (data[i].attrName != undefined && data[i].attrName != null && data[i].attrName != '') {
				attrArr.push(data[i]);
			} else {
				specArr.push(data[i]);
			}
		}
		$scope.attrArr = attrArr;
		$scope.specArr = specArr;
		var idMap = [];
		idMap[0] = attrIdMap;
		idMap[1] = specIdMap
		return idMap;
	});

	/**
	 * 弹出选择属性的框
	 */
	events.select = function() {
		if (!$scope.attrList) {
			freshAttribute();
		}
		//
		$('#attrModal').modal('show');
	};
	/**
	 * 刷新属性
	 */
	function freshAttribute() {
		$api.get('attributes/list').then(function(resp) {
			promise.then(function(idMap) {
				var attrIdMap = idMap[0];
				$scope.attrList = resp.data.data;
				$.each($scope.attrList, function(index, item) {
					if (attrIdMap[item.id]) {
						item.selected = true;
					}
				});

			});
		});
	}
	events.toggleAttr = function(attr) {
		attr.selected = !attr.selected;
	}

	/**
	 * 添加属性
	 */
	events.addAttr = function() {
		if ($scope.attrName == null || $scope.attrName == '') {
			return false;
		}
		for (i in $scope.attrArr) {
			if ($scope.attrName == $scope.attrArr[i].attrName) {
				alert("重复添加");
				return false;
			}
		}
		$api.postJson('attributes', {
			name: $scope.attrName
		}).then(saveSuccess);
		$scope.attrName = null;
	}

	function saveSuccess(resp) {
		if (resp.data.code == 200) {
			freshAttribute();
		} else {
			alert(resp.data.message);
		}

	}
	/**
	 * 确定生成属性
	 */
	events.selectDone = function() {
		// 编辑前的数据，转换成 map
		var map = {};
		$.each($scope.attrArr, function(index, item) {
			map[item.keyId] = item;
		});

		// 重新选择的属性
		var newAttrArr = [];
		$.each($scope.attrList, function(index, item) {
			if (!item.selected) {
				return;
			}
			// 如果编辑后依然在，保留其属性值
			if (map[item.id]) {
				newAttrArr.push(map[item.id]);
				// 新增的，初始化
			} else {
				newAttrArr.push({
					attrName: item.name,
					keyId: item.id,
					type: 2,
					value: []
				});
			}
		});
		$scope.attrArr = newAttrArr;
		$('#attrModal').modal('hide');

	}

	/**
	 * 删除一个属性值
	 */
	events.removeValue = function(attr, value) {
		for (i in attr.value) {
			if (attr.value[i] == value) {
				attr.value.splice([i])
			}
		}
	}

	/**
	 * 增加一个属性值(到最后)
	 */
	events.addValue = function(attr) {
		if (attr.attrValue == null || attr.attrValue == '') {
			return;
		}
		if (attr.value == null || attr.value.indexOf(attr.attrValue) == -1) {
			if (attr.value == null || attr.value == '')
				attr.value = [];
			attr.value.push(attr.attrValue);
			attr.attrValue = '';
		} else {
			alert('重复添加')
		}

	}

	/**
	 * 选中、不选中一个属性值
	 */
	events.enable = function() {

		}
		/**
		 * 增加
		 */
	events.add = function() {
			//提交前 先验证不允许空值提交
			for (i in $scope.specArr) {
				if ($scope.specArr[i].value.length == 0) {
					alert("不允许提交空规格");
					return false;
				};
			}
			for (i in $scope.attrArr) {
				if ($scope.attrArr[i].value.length == 0) {
					alert("不允许提交空属性");
					return false;
				};
			}
			//拼接提交参数
			var params = new Array();
			for (i in $scope.attrArr) {
				params.push($.extend(true, {}, $scope.attrArr[i]));
			}
			for (i in $scope.specArr) {
				params.push($.extend(true, {}, $scope.specArr[i]));
			}
			console.log(params);
			for (i in params) {
				var parseValue = '';
				if (params[i].value.length == 0) {
					alert('不允许空值提交')
					return false;
				}
				for (j in params[i].value) {
					if (params[i].value.length > 1) {
						if (j == params[i].value.length - 1) {
							parseValue += params[i].value[j];
						} else {
							parseValue += params[i].value[j] + ",";
						}
					} else if (params[i].value.length == 1) {
						parseValue += params[i].value[0];
					}
				}
				if (parseValue != '') {
					params[i].value = parseValue;
				}
				params[i].categoryId = $stateParams.id;
			}
			console.log(params)
			$api.postJson('category/templates', params).then(function templatesAdd(resp) {

				if (resp.data.code == 200) {
					ngToast.create({
						content: '保存模板成功',
						// dismissOnTimeout: false,
						timeout: 2000
					});

					//提交成功刷新页面
					promise = $api.get('category/templates/list', {
						categoryId: id
					}).then(function(resp) {
						var data = resp.data.data;
						var attrIdMap = {},
							specIdMap = {};
						$.each(data, function(index, item) {
							if (item.attrName == null || item.attrName == '') {
								specIdMap[item.keyId] = 1;
							} else {
								attrIdMap[item.keyId] = 1;
							}
							if (item.value) {
								item.value = item.value.split(',');
							}
						});
						var attrArr = [];
						var specArr = [];
						for (i in data) {
							if (data[i].attrName != undefined && data[i].attrName != null && data[i].attrName != '') {
								attrArr.push(data[i]);
							} else {
								specArr.push(data[i]);
							}
						}
						$scope.attrArr = attrArr;
						$scope.specArr = specArr;
						var idMap = [];
						idMap[0] = attrIdMap;
						idMap[1] = specIdMap
						return idMap;
					});
				} else {
					ngToast.create({
						content: '保存模板失败：' + resp.data.message,
						className: 'danger',
						// dismissOnTimeout: false,
						timeout: 2000
					});
				}
			});
		}
		/**
		 * 规格弹出框
		 */
	events.selectSpec = function() {
		if (!$scope.specList) {
			$api.get('specs').then(freshSpec);
		}
		//
		$('#specModal').modal('show');
	};

	/**
	 * 添加规格
	 */
	events.addSpec = function() {
		if ($scope.specName == null || $scope.specName == '') {
			return false;
		}
		for (i in $scope.specArr) {
			if ($scope.specName == $scope.specArr[i].specName) {
				alert("重复添加");
				return false;
			}
		}

		$api.postJson('specs', {
			name: $scope.specName
		}).then(saveSpecSuccess);

		$scope.specName = null;
	}

	function saveSpecSuccess(resp) {
		if (resp.data.code == 200) {
			freshSpec();
		} else {
			alert(resp.data.message);
			console.log(resp.data.exception)
		}
	}
	/**
	 * 刷新规格
	 */
	function freshSpec() {
		$api.get('specs').then(function(resp) {
			promise.then(function(idMap) {
				var specIdMap = idMap[1];
				$scope.specList = resp.data.data;
				$.each($scope.specList, function(index, item) {
					if (specIdMap[item.id]) {
						item.selected = true;
					}
				});

			});
		});
	}
	/**
	 * 确定生成规格 列表
	 */
	events.selectSpecDone = function() {
		// 编辑前的数据，转换成 map
		var map = {};
		$.each($scope.specArr, function(index, item) {
			map[item.keyId] = item;
		});

		// 重新选择的属性
		var newSpecArr = [];
		$.each($scope.specList, function(index, item) {
			if (!item.selected) {
				return;
			}
			// 如果编辑后依然在，保留其属性值
			if (map[item.id]) {
				newSpecArr.push(map[item.id]);
				// 新增的，初始化
			} else {
				newSpecArr.push({
					specName: item.name,
					keyId: item.id,
					type: 1,
					value: []
				});
			}
		});
		$scope.specArr = newSpecArr;
		$('#specModal').modal('hide');

	}

	/**
	 * 增加一个规格值
	 */
	events.addSpecValue = function(spec) {
		if (spec.specValue == null || spec.specValue == '') {
			return;
		}

		if (spec.value == null || spec.value.indexOf(spec.specValue) == -1) {
			if (spec.value == null || spec.value == '')
				spec.value = [];
			spec.value.push(spec.specValue);
			spec.specValue = '';
		} else {
			alert('重复添加')
		}

	}
	$scope.events = events;
}
