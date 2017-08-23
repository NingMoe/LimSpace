angular
	.module('app')
	.controller('SpusController', SpusController);
SpusController.$inject = ['$scope', '$rootScope', '$location', '$stateParams', '$api', '$param', '$downloadRoot'];

function SpusController($scope, $rootScope, $location, $stateParams, $api, $param, $downloadRoot) {
	$rootScope.sideBarVisible = true;

	var selected = {};
	var categroyId = '';
	$scope.categroyId = categroyId;
	var params = {
		id: $stateParams.id
	};

	$api.get('spus/' + params.id).then(function(resp) {
		var spus = resp.data.data
		$scope.spus = spus;
		console.log(spus)
		categroyId = spus.categoryId;
		$scope.spuSpec.brands = spus.spuSpecList[0].specValue;
		$scope.spuSpec.suppliers = spus.spuSpecList[1].specValue;
		$.each(spus.spuAttributeList, function(index, item) {
			var values = item.valueConstraintExpression.split(',');

			for (i in values) {
				toggleAttr(item, values[i]);
			}

		});
	});

	var spuArry = {};
	$scope.spuArry = spuArry;
	$scope.totalChange = totalChange;
	var spuSpec = {}
	$scope.spuSpec = spuSpec;

	$("#txtEditor").Editor();
	//以下开始做spu添加
	var attrArr = [];
	var brands = [];
	var suppliers = [];
	var idNum = [];


	function toggleAttr(attr, value) {
		console.log("attr:", attr);
		console.log("value", value)
		selected[attr.id] = selected[attr.id] || {
			name: attr.attrName,
			values: []
		};
		if (selected[attr.id].values.indexOf(value) != -1) {
			selected[attr.id].values = remove(selected[attr.id].values, value);
		} else {
			selected[attr.id].values.push(value);
		}
		attr.selected = selected[attr.id];
		$scope.selected = selected;
		console.log(selected)
			//		console.log("selected:", $scope.selected);
			//		console.log("attrArr:", $scope.attrArr);

		var attrHeaders = [];
		var attrRows = [];

		$.each($scope.selected, function(attrId, attrItem) {

			attrHeaders.push(attrItem.name);
			if (attrRows.length === 0) {
				$.each(attrItem.values, function(i, v) {
					attrRows.push([v]);
				});
			} else {
				var newAttrRows = [];
				$.each(attrRows, function(j, d) {
					var newAttrRowsItem = [];
					$.each(attrItem.values, function(i, v) {
						var dd = $.extend([], d);
						dd.push(v);
						newAttrRowsItem.push(dd);
					});
					newAttrRows = newAttrRows.concat(newAttrRowsItem);
				});
				attrRows = newAttrRows;
			}

		});
		//		console.log("attrHeaders=", attrHeaders);
		//		console.log("attrRows=", attrRows);
		if (attrRows.length === 0 || attrRows[0].length < $scope.attrArr.length) {
			$scope.showTableBody = false;
			return;
		}

		var oldSkuList = $.extend({}, $scope.spuArry.skuList);
		var newSkuList = {};
		$.each(attrRows, function(index, attrRow) {
			// 使用旧数据
			if (oldSkuList[attrRow.join('_')]) {
				newSkuList[attrRow.join('_')] = oldSkuList[attrRow.join('_')];
				// 第一次新增的数据
			} else {
				newSkuList[attrRow.join('_')] = {
					name: $scope.spuArry.name ? $scope.spuArry.name + ' ' + attrRow.join(' ') : ''
				};
			}
		});
		$scope.spuArry.skuList = newSkuList;
		//		console.log("$scope.spuArry.skuList :", $scope.spuArry.skuList);

		$scope.showTableBody = true;
		$scope.attrHeaders = attrHeaders;
		$scope.attrRows = attrRows;

	}

	function remove(s, val) {
		var index = s.indexOf(val);
		if (index > -1) {
			s.splice(index, 1);
		}
		return s;
	};

	/*	$scope.filterSecId = function(items) {
	    var result = {};
	    angular.forEach(items, function(value, key) {
	        if (!value.hasOwnProperty('secId')) {
	            result[key] = value;
	        }
	    });
	    return result;
	}*/

	function save() {
		console.log(skuMap);
		var skuList = [];
		var submitData = $.extend({}, spuArry);
		var spuAttrs = $.extend({}, $scope.selected);
		var spuAttrList = []; //开始组装spuAttrList
		$.each(spuAttrs, function(ele, item) {
			var spuAttribute = {};
			spuAttribute["virtualId"] = 'v' + ele;
			spuAttribute["attributeId"] = ele;
			spuAttribute["valueConstraintType"] = 1;
			spuAttribute["valueConstraintExpression"] = item.values.join(',');
			spuAttrList.push(spuAttribute);
		});

		$.each(spuArry.skuList, function(hash, sku) {
			var skuAttrs = hash.split('_');
			var skuAttributeList = []; //组装sku属性列表
			$.each(skuAttrs, function(index, item) {
				var skuAttr = {};
				skuAttr["virtualId"] = 'v' + $scope.attrArr[index].id;
				skuAttr["attributeValue"] = item;
				skuAttributeList.push(skuAttr);
			});
			sku["skuAttributeList"] = skuAttributeList
			skuList.push(sku);
		});
		submitData["spuAttributeList"] = spuAttrList;
		var spuSpecList = []; //组装品牌供应商
		$.each(spuSpec, function(index, item) {
			var spec = {};
			spec["specId"] = item["id"];
			spec["specValue"] = item["name"];
			spuSpecList.push(spec);
		})
		submitData["spuSpecList"] = spuSpecList;
		submitData.skuList = skuList;
		submitData["categoryId"] = $scope.categroyId;
		submitData["mobileDescription"] = $(".Editor-editor").html();
		console.log(submitData)
			//		$api.postJson('spus', submitData).then(function(resp) {
			//			// 成功，刷新数据
			//			if (resp.data.code == 200) {
			//				alert("添加成功！");
			//			} else {
			//				alert(resp.data.exception)
			//			}
			//		});
	}

	function totalChange() {
		$("#inventory").show();
		var skuListInventory = $.extend([], spuArry.skuList); //计算商品总数
		var total = 0;
		for (i in skuListInventory) {
			if (skuListInventory[i]["inventory"] == undefined || skuListInventory[i]["inventory"] == '') {
				skuListInventory[i]["inventory"] = 0;
			}
			total = total + parseInt(skuListInventory[i]["inventory"]);
		}
		$scope.total = total;
	}
	$scope.toggleAttr = toggleAttr;
	$scope.save = save;

}