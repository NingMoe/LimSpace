angular
	.module('app')
	.controller('ProductController', ProductController);

ProductController.$inject = ['$rootScope', '$scope', '$stateParams', '$api'];
function ProductController($rootScope, $scope, $stateParams, $api) {
	$rootScope.sideBarVisible = true;

	// 从模板获取到的属性数组
	var attrArr = [];
	var specArr = [];
	var showTableBody = false;
	var spu = {
		categoryId: $stateParams.categoryId
	};
	// SKU数组
	var skus = [];
	var skuMap = {};
	var specMap = {};
	var events = {};

	var TYPE_SPEC = 1;
	var TYPE_ATTR = 2;

	/**
	 * 选中、不选中属性值
	 */
	events.toggleAttrValue = function(value) {
		value.selected = !value.selected;

		if (!($scope.showTableBody = checkAllAttrHasSelected())) {
			return;
		}

		// 展开 SKU 列表
		skus = [];
		$.each(attrArr, function(i, attr) {
			if (skus.length === 0) {
				$.each(attr.values, function(j, value) {
					if (value.selected) {
						skus.push([value.text]);
					}
				});
			} else {
				var newSkus = [];
				$.each(skus, function(j, sku) {
					$.each(attr.values, function(k, value) {
						var newSku = $.extend([], sku);
						if (value.selected) {
							newSku.push(value.text);
							newSkus.push(newSku);
						}
					});
				});
				skus = newSkus;
			}
		});
		$scope.skus = skus;
	};

	/**
	 * 判断是否每个属性都有至少一个选中的属性值
	 * @return {[type]} [description]
	 */
	function checkAllAttrHasSelected() {
		for (var i = attrArr.length - 1, hasSelected; i >= 0; i--) {
			hasSelected = false;
			for (var j = attrArr[i].values.length - 1; j >= 0; j--) {
				if (attrArr[i].values[j].selected) {
					hasSelected = true;
					break;
				}
			}
			if (!hasSelected) {
				return false;
			}
		}
		return true;
	}

	events.toggle = function(sku) {
		sku.visible = !sku.visible;
	}

	/**
	 * 保存SPU
	 */
	events.save = function() {
		// spu 必须有 spuAttributeList 属性
		// TODO 应该没有用处，未来取消此功能
		spu.spuAttributeList = [];
		$.each(attrArr, function(i, attr) {
			var selectedValues = [];
			$.each(attr.values, function(j, value) {
				if (value.selected) {
					selectedValues.push(value.text);
				}
			});
			spu.spuAttributeList.push({
				attributeId: attr.attrId,
				valueConstraintType: 1,
				valueConstraintExpression: selectedValues.join(',')
			});
		});

		// spu 必须有 spuSpecList
		spu.spuSpecList = [];
		$.each(specMap, function(specId, specValue) {
			spu.spuSpecList.push({
				specId: specId,
				specValue: specValue
			});
		});

		// spu 必须有 skuList
		// 每个元素下包含 skuAttributeList (attributeValue, Attribute attribute)
		// 转换 SKU 为数组，并生成属性列表
		var skuArr = [];
		$.each(skuMap, function(hash, sku) {
			sku.skuAttributeList = [];
			$.each(hash.split('_'), function(i, attrValue) {
				sku.skuAttributeList.push({
					attribute: {
						id: attrArr[i].attrId
					},
					attributeValue: attrValue
				});
			});
			skuArr.push(sku);
		});
		spu.skuList = skuArr;
		console.log(spu);

		$api.postJson('spus', spu).then(function(resp) {
			// 成功，刷新数据
			if (resp.data.code == 200) {
				alert("添加成功！");
			} else {
				alert(resp.data.exception)
			}
		});
	};

	function init() {
		// 获取模板
		$api.get('/category/templates/list/', {
			categoryId: spu.categoryId
		}).then(function(resp) {
			// 清空数组，不能直接重置，如：attrArr = [] ，重置导致 $scope.attrArr 也要重新赋值
			attrArr.splice(0, attrArr.length);
			specArr.splice(0, specArr.length);
			$.each(resp.data.data, function(index, item) {
				// 规格
				if (item.type == TYPE_SPEC) {
					var values = [];
					$.each(item.value.split(','), function(j, value) {
						values.push({
							text: value,
							selected: false
						});
					});
					specArr.push({
						specId: item.keyId,
						specName: item.specName,
						values: values
					});
				// 属性
				} else {
					// 将属性的 value 字段从 "红色,白色" 转换成对象数组
					// [{text: "红色", selected: false}, {text: "白色", selected: false}]
					var values = [];
					$.each(item.value.split(','), function(j, value) {
						values.push({
							text: value,
							selected: false
						});
					});
					attrArr.push({
						attrId: item.keyId,
						attrName: item.attrName,
						values: values
					});
				}
			});
		});
	}

	init();

	$scope.attrArr = attrArr;
	$scope.specArr = specArr;
	$scope.spu = spu;
	$scope.skus = skus;
	$scope.skuMap = skuMap;
	$scope.specMap = specMap;
	$scope.events = events;
	$scope.showTableBody = showTableBody;
}

angular
	.module('app')
	.controller('ProductListController', ProductListController);
ProductListController.$inject = ['$scope', '$rootScope', '$location', '$api', '$param', '$downloadRoot'];

function ProductListController($scope, $rootScope, $location, $api, $param, $downloadRoot) {
	$rootScope.sideBarVisible = true;
	$api.get('spus', {}).then(function(resp) {
		var spus = resp.data.data;
		$scope.spus = spus.rows;
		if (resp.data.code == 200) {

		} else {
			alert(resp.data.exception)
		}
	});
};




