angular
    .module('app')
    .controller('ProductSpuEditController', ProductSpuEditController);

ProductSpuEditController.$inject = ['$rootScope', '$location', '$scope', '$stateParams', '$api', '$imgHost', '$state', 'ngToast'];
function ProductSpuEditController($rootScope, $location, $scope, $stateParams, $api, $imgHost, $state, ngToast) {
    $rootScope.sideBarVisible = true;

    // 从模板获取到的属性数组
    var attrArr = [];
    var specArr = [];
    var showTableBody = false;
    var spu = {
        categoryId: $stateParams.categoryId,
        id: $stateParams.spuId
    };
    // SKU Map，操作的时候是按数组，增减行的时候需要靠此 Map 重构数组
    var skuMap = {};
    var specMap = {};
    var events = {};

    var TYPE_SPEC = 1;
    var TYPE_ATTR = 2;

    var skuTemplate = {
        upload: {
            files: []
        },
        uploadDesc: {
            files: []
        }
    };

    /**
     * 选中、不选中属性值
     */
    events.toggleAttrValue = function(value) {
        value.selected = !value.selected;

        if (!($scope.showTableBody = checkAllAttrHasSelected())) {
            return;
        }

        // 将选中属性按 SKU 组成二维数组
        var skuAttrMatrix = [];
        $.each(attrArr, function(i, attr) {
            if (skuAttrMatrix.length === 0) {
                $.each(attr.values, function(j, value) {
                    if (value.selected) {
                        skuAttrMatrix.push([value.text]);
                    }
                });
            } else {
                var newSkuAttrMatrix = [];
                $.each(skuAttrMatrix, function(j, skuAttr) {
                    $.each(attr.values, function(k, value) {
                        var newSkuAttr = $.extend([], skuAttr);
                        if (value.selected) {
                            newSkuAttr.push(value.text);
                            newSkuAttrMatrix.push(newSkuAttr);
                        }
                    });
                });
                skuAttrMatrix = newSkuAttrMatrix;
            }
        });


        // 重构 skuList 属性
        var skuList = [];
        $.each(skuAttrMatrix, function(i, skuAttr) {
            var mapKey = skuAttr.join('_');
            var sku;
            if (skuMap[mapKey]) {
                sku = skuMap[mapKey];
            } else {
                sku = {};
                // 初始化默认值
                sku.name = spu.name ? (spu.name + skuAttr.join(' ')) : '';
                sku.skuAttributeList = [];
                $.each(skuAttr, function(i, attrValue) {
                    sku.skuAttributeList.push({
                        attribute: {
                            id: attrArr[i].attrId
                        },
                        attributeValue: attrValue
                    });
                });
            }
            if (!sku.upload) {
                sku.upload = {
                    files: []
                };
            }
            if (!sku.uploadDesc) {
                sku.uploadDesc = {
                    files: []
                };
            }
            skuList.push(sku);
        });
        spu.skuList = skuList;
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
    };

    /**
     * 删除一个 SKU
     */
    events.remove = function(sku, $index) {
        spu.skuList.splice($index, 1);
        // 不再缓存这个 SKU 的数据
        var attrValues = [];
        $.each(sku.skuAttributeList, function(i, skuAttr) {
            attrValues.push(skuAttr.attributeValue);
        });
        var mapKey = attrValues.join('_');
        // delete skuMap[mapKey];

        // 反向联动修改属性值的选中状态
        var selectedValues = [];
        $.each(spu.skuList, function(i, sku) {
            var values = [];
            $.each(sku.skuAttributeList, function(j, skuAttr) {
                values.push(skuAttr.attributeValue);
            });
            selectedValues = selectedValues.concat(values);
        });
        $.each(attrArr, function(i, attr) {
            $.each(attr.values, function(j, value) {
                value.selected = selectedValues.indexOf(value.text) !== -1;
            });
        });
    };

    /**
     * 清空 SKU 的字段值
     */
    events.reset = function(sku) {
        // 不再缓存这个 SKU 的数据
        sku.name = sku.price = sku.originalPrice = sku.erpCode = sku.inventory = sku.installment = sku.imagesOriginal = '';
        sku.installments = {};
        // 未上架
        sku.status = 0;
        // 上传的图片也清空
        sku.upload.files = [];
        sku.uploadDesc.files = [];
    };

    function prepareForCopying(skuTemplate) {
        var copied = $.extend(true, {}, skuTemplate);
        // 无值的不复制
        $.each(copied, function(i, item) {
            if (!item && i !== 'status') {
                delete copied[i];
            }
        });

        if (!copied.notCopyInstallment) {
            copied.installments = copied.installments || {};
            angular.forEach([1, 3, 6, 12], function(month) {
                copied.installments[month] = copied.installments[month] || false;
            });
        } else {
            delete copied.installments;
        }

        if (copied.notCopyStatus) {
            delete copied.status;
        }

        delete copied.notCopyInstallment;
        delete copied.notCopyStatus;

        return copied;
    }

    /**
     * SKU模板复制到所有SKU
     */
    events.copyToAll = function() {
        var copied = prepareForCopying(skuTemplate);
        $.each(spu.skuList, function(i, sku) {
            sku.upload.files = [];
            // TODO 正在上传中的，需要放弃
            $.extend(true, sku, copied);
        });
    };

    /**
     * SKU模板复制到当前SKU
     */
    events.copy = function(sku) {
        var copied = prepareForCopying(skuTemplate);
        // 把上传完的图片数组 copy 过来
        if (copied.upload.files.length > 0) {
            sku.upload.files = $.extend([], copied.upload.files);
        }
        if (copied.uploadDesc.files.length > 0) {
            sku.uploadDesc.files = $.extend([], copied.uploadDesc.files);
        }
        delete copied.upload;
        delete copied.uploadDesc;
        $.extend(true, sku, copied);
        console.log(sku);
    };

    function getInstallments(installments) {
        var months = [];
        $.each(installments, function(month, checked) {
            if (checked) {
                months.push(month);
            }
        });
        return months;
    }

    function getImageGroup(upload) {
        var images = [];
        $.each(upload.files, function(i, file) {
            if (file.path) {
                images.push(file.path);
            }
        });
        return images;
    }

    /**
     * 保存SPU
     */
    events.save = function() {
        var spu = $.extend(true, {}, $scope.spu);
        // spu 必须有 spuAttributeList 属性
        // TODO 应该没有用处，未来取消此功能
        spu.spuAttributeList = [];
        $.each(attrArr, function(i, attr) {
            var selectedValues = [];
            var allValues = [];
            $.each(attr.values, function(j, value) {
                if (value.selected) {
                    selectedValues.push(value.text);
                }
                allValues.push(value.text);
            });
            spu.spuAttributeList.push({
                attributeId: attr.attrId,
                attributeValues: allValues.join(','),
                valueConstraintType: 1,
                valueConstraintExpression: selectedValues.join(',')
            });
        });

        // spu 必须有 spuSpecList
        spu.spuSpecList = [];
        // 获取所有的规则值
        var specValuesMap = {};
        $.each(specArr, function(i, spec) {
            var texts = [];
            spec.values.every(function(value) {
                texts.push(value.text);
                return true;
            });
            specValuesMap[spec.specId] = texts.join(',');
        });

        $.each(specMap, function(specId, specValue) {
            spu.spuSpecList.push({
                specId: specId,
                specValue: specValue,
                specValues: specValuesMap[specId]
            });
        });

        var invalidMsg = '';
        if (!spu.name) {
            invalidMsg = 'SPU 标题不能为空';
        } else if (!$scope.showTableBody) {
            invalidMsg = 'SPU 至少包含一个SKU';
        }
        if (invalidMsg) {
            ngToast.create({
                content: invalidMsg,
                className: 'danger',
                timeout: 2000
            });
            return false;
        }

        // skuList 处理
        for (var i = 0; i < spu.skuList.length; i++) {
            var sku = spu.skuList[i];
            if (sku.upload.files.length > 0) {
                var images = [];
                // sku.upload.files
                $.each(sku.upload.files, function(i, file) {
                    if (file.path) {
                        images.push(file.path);
                    }
                });
                sku.imagesOriginal = images.join(',');
            } else {
                // delete sku.imagesOriginal;
                delete sku.imagesThumbnail;
                delete sku.headThumbnail;
            }
            sku.installment = (getInstallments(sku.installments) || []).join(',');
            delete sku.installments;

            // SKU商品详情
            if (sku.uploadDesc.files.length > 0) {
                var description = '';
                $.each(sku.uploadDesc.files, function(i, file) {
                    if (file.path) {
                        description += '<p><img src="' + $imgHost + '/' + file.path + '"></p>';
                    }
                });
                sku.description = description;
            }

            // SKU组图
            if (sku.upload.files.length > 0) {
                var imagesOriginal = [];
                $.each(sku.upload.files, function(i, file) {
                    if (file.path) {
                        imagesOriginal.push(file.path);
                    }
                });
                sku.imagesOriginal = imagesOriginal.join(',');
            }
			
            if (!sku.name) {
                invalidMsg = 'SKU 标题不能为空';
            } else if (!sku.price || sku.price <= 0) {
                invalidMsg = 'SKU 价格非法';
            } else if (!sku.originalPrice || sku.originalPrice <= 0) {
                invalidMsg = 'SKU 市场价非法';
            } else if (parseFloat(sku.originalPrice)<parseFloat(sku.price)){
            	invalidMsg = '价格不应该大于市场价格';
            } else if (!sku.erpCode) {
                invalidMsg = 'SKU ERP码不能为空';
            } else if (!sku.inventory || sku.inventory < 0) {
                invalidMsg = 'SKU 库存非法';
            } else if (!sku.imagesOriginal) {
                invalidMsg = 'SKU 组图不能为空';
            } else if (!sku.description) {
                invalidMsg = 'SKU 详情不能为空';
            } else if (!sku.imagesOriginal) {
                invalidMsg = 'SKU 组图不能为空';
            }

            if (invalidMsg) {
                ngToast.create({
                    content: '第' + (i + 1) + '个SKU：' + invalidMsg,
                    className: 'danger',
                    timeout: 2000
                });
                return false;
            }

            delete sku.upload;
            delete sku.$$hashKey;
            delete sku.visible;
            delete sku.createTime;
            delete sku.updateTime;
            angular.forEach(sku.skuAttributeList, function(i) {
                delete i.$$hashKey;
            });
        }

        if (spu.id) {
            $api.putJson('spus', spu).then(function(resp) {
                $scope.saveRes = resp.data;
                $('#saveOkModal').modal('show');
            }, function(resp) {
                alert('报错了');
            });
        } else {
            $api.postJson('spus', spu).then(function(resp) {
                $scope.saveRes = resp.data;
                $('#saveOkModal').modal('show');
            }, function(resp) {
                alert('报错了');
            });
        }
    };

    events.hideModal = function() {
        $('#saveOkModal').modal('hide');
        /*
        setTimeout(function() {
            location.href = '#/product/spu?categoryId=' + spu.categoryId;
        }, 500);
        */
    };

    events.addSpu = function() {
        $('#saveOkModal').modal('hide');
        setTimeout(function() {
            $state.go('addSpu', {
                categoryId: spu.categoryId
            }, {
                reload: true
            });
        }, 500);
    };

    events.listSpu = function() {
        $('#saveOkModal').modal('hide');
        setTimeout(function() {
            $state.go('listSpu', {
                categoryId: spu.categoryId
            }, {
                reload: true
            });
        }, 500);
    };
    events.listsku = function() {
        $('#saveOkModal').modal('hide');
        setTimeout(function() {
            $state.go('listKooSku', {
                spuId: spu.id
            }, {
                reload: true
            });
        }, 500);
    };

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
            attr.values.push({
                text: attr.attrValue,
                selected: false
            });
            attr.attrValue = '';
        } else {
            alert('重复添加')
        }

    };

    function initCreate() {
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

    function initUpdate() {
        $api.get('/spus/' + spu.id).then(function(resp) {
            var data = resp.data.data;
            // skuList 转换成 skuMap
            var selectedValues = [];
            $.each(data.skuList, function(i, sku) {
                var values = [];
                $.each(sku.skuAttributeList, function(j, skuAttr) {
                    values.push(skuAttr.attributeValue);
                });
                selectedValues = selectedValues.concat(values);
                if (!sku.upload) {
                    sku.upload = {
                        files: []
                    };
                }
                if (!sku.uploadDesc) {
                    sku.uploadDesc = {
                        files: []
                    };
                }
                sku.installments = {};
                if (sku.installment) {
                    angular.forEach((sku.installment || '').split(','), function(month) {
                        sku.installments[month] = true;
                    });
                }
                console.log(sku.installments);

                skuMap[values.join('_')] = sku;
            });

            // spuAttributeList 转换成 attrArr
            $.each(data.spuAttributeList, function(i, spuAttr) {
                var values = [];
                $.each(spuAttr.attributeValues.split(','), function(i, attrValue) {
                    values.push({
                        text: attrValue,
                        selected: selectedValues.indexOf(attrValue) != -1
                    });
                });
                /*
                $.each(spuAttr.valueConstraintExpression.split(/\s+/), function(j, value) {
                    values.push({
                        text: value,
                        selected: true
                    });
                });
                */
                attrArr.push({
                    attrId: spuAttr.attributeId,
                    attrName: spuAttr.attribute.name,
                    values: values
                });
            });

            $scope.showTableBody = true;
            $scope.spu = spu = data;

            // spuSpecList 转换成 specArr 和 specMap
            $.each(data.spuSpecList, function(i, spec) {
                console.log(spec);
                // values
                specMap[spec.specId] = spec.specValue;
                var values = [];
                $.each((spec.specValues || spec.specValue).split(','), function(j, value) {
                    values.push({
                        text: value,
                        selected: spec.specValue === value
                    });
                });
                specArr.push({
                    specName: spec.specName,
                    specId: spec.specId,
                    specValue: spec.specValue,
                    values: values
                });
            });

            if (spu.description) {
                $('.description.old').html(spu.description);
            }
            console.log('specArr:', specArr, specMap);
        });
    }

    $scope.plupload = plupload;

    function init() {
        if (spu.id) {
            initUpdate();
        } else if (spu.categoryId) {
            initCreate();
        } else {
            alert("非法参数");
        }
    }

    init();

    $scope.skuTemplate = skuTemplate;
    $scope.imgHost = $imgHost;
    $scope.attrArr = attrArr;
    $scope.specArr = specArr;
    $scope.spu = spu;
    $scope.skuMap = skuMap;
    $scope.specMap = specMap;
    $scope.events = events;
    $scope.showTableBody = showTableBody;
}
