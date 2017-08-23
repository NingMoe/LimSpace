define([
  'underscore',
  'zepto',
  'md5',
  'weixinjs',
  'config'
], function (_, $, md5,weixinjs, config) {
  'use strict';


   var newRoot =config.napi;
  //var newRoot ="http://192.168.73.7:8080/api/";

  function getUrl(path) {
    return newRoot + path;
  }

  function cmpBySpuAttributeId(a, b) {
    return a.spuAttributeId - b.spuAttributeId;
  }

  function paddingZero(num) {
    return num >= 10 ? num : '0' + num;
  }

  function formatTime(time) {
    if (!time) {
      return '';
    }
    var date = new Date(parseInt(time));
    var result = date.getFullYear() + '-' + paddingZero(date.getMonth() + 1) + '-' + paddingZero(date.getDate()) + ' ' + paddingZero(date.getHours()) + ':' + paddingZero(date.getMinutes());
    return result;
  }
  function formatHour(time) {
    if (!time) {
      return '';
    }
    var date = new Date(parseInt(time));
    var result = paddingZero(date.getHours()) + ':' + paddingZero(date.getMinutes());
    return result;
  }

  function timeStamp(time) {
    if (!time) {
      return '';
    }
    var date = (new Date(time)).valueOf();
    return date;
  }

  var admins ={
    details:function(id){//商品详情
      var url = [newRoot,'sku/',id].join('');
      var options = {
        url: url,
        type: 'get',
        headers: {
          'Content-Type': 'application/json'
        }
      };

      return $.ajax(options).then(function(resp) {
      // 按 spuAttributeId 排序
      var defaultAttrs = {};
      _.each(resp.sku.attrs.sort(cmpBySpuAttributeId), function(attr) {
        defaultAttrs[attr.spuAttributeId] = attr.attributeValue;
      });
      // 聚合出其他可跳转的 SKU ，key 为 skuId，value 为 spuAttributeId => attributeValue 的 Map
      var otherSkus = {};
        _.each(resp.otherSkuAttrs, function(otherSkuAttr) {
          var otherSku = otherSkus[otherSkuAttr.skuId] || {};
          otherSku[otherSkuAttr.spuAttributeId] = otherSkuAttr.attributeValue;
          otherSkus[otherSkuAttr.skuId] = otherSku;
      });
      // 组合成一个 Map
      var otherSkuMap = {};
      _.each(otherSkus, function(otherSku, skuId) {
        otherSkuMap[JSON.stringify(otherSku)] = skuId;
      });
      // 列举出每个属性的每个值，其是否选中，
      // 非选中时显示 skuId
      // 选中时，以及无此 SKU 或者无库存，则 skuId 为 null ，前端置灰
      _.each(resp.spuAttrs, function(spuAttr) {
        var allValues = {};
        _.each(spuAttr.valueConstraintExpression.split(','), function(attributeValue) {
          var newAttr = {};
          newAttr[spuAttr.id] = attributeValue;
          var mapKey = JSON.stringify(_.extend({}, defaultAttrs, newAttr));
          allValues[attributeValue] = {
            skuId: otherSkuMap[mapKey] || null,
            active: defaultAttrs[spuAttr.id] == attributeValue
          };
        });
        spuAttr.attrValues = allValues;
      });
      var maxpric;
      _.each(resp.sku.installments, function(item,name) {
          if(resp.sku.installments.length-1 == name){
            maxpric = item;
          }

        resp.maxpric = maxpric;

      })
      var maximg=[];
        _.each(resp.sku.imagesOriginal.split(','),function(item,name){
          maximg[name] = item;
        });
        resp.maximg = maximg;
        console.log(resp);
        return resp;
      });
    }
  };

  var orders ={
    listOrder:function(){
      var url = [newRoot,'orders'].join('');
      var options = {
        url: url,
        type: 'get',
        headers:{
          'Content-Type': 'application/json'
        }
      };
      return $.ajax(options).then(function(resp) {
      	if (resp.code != 200) {
          console.error('加载订单列表出错：', resp);
          return {
            all: [],
            toPay: [],
            toReceive: []
          };
        }
	      var data = resp.data;
	      // 待付款 -> 未付款 1
	      var toPay = [];
	      // 待收货 -> 已制单 4、已发货 5
	      var toReceive = [];
	      var allValues ;
	      _.each(data, function(item, index) {
	        if (item.status ==1) {
	          toPay.push(item);
	        } else if ( item.status == 5) {
	          toReceive.push(item);
	        }
	        switch(item.status)
	        {
	        case 1:
	          allValues = '待付款';
	          break;
	        case 2:
	          allValues ='已付款';
	          break;
	        case 3:
	          allValues ='待发货';
	          break;
	        case 4:
	          allValues ='待发货';
	          break;
	        case 5:
	          allValues = '已发货';
	          break;
	        case 6:
	          allValues = '已完成';
	          break;
	        case 7:
	          allValues = '已失效';
	          break;
	        case 8:
	          allValues = '已退货';
	          break;
	        case 9:
	          allValues = '已取消';
	          break;
	        }
	        //自提商品
	        if (item.status == 5) {
	          if (item.pickupStatus == 1 || item.pickupStatus == 2) {
	              allValues ='待提货';
	          }
	        }
	        //第二状态
	        if(item.cancellationType == 1)
	        {
	          if(item.secondStatus === 0 || item.secondStatus == 1){
	            allValues ='取消中';
	          }else if(item.secondStatus == 2){
	            allValues ='已取消';
	          }
	        }
	        if(item.cancellationType == 2)
	        {
	          if(item.secondStatus === 0 || item.secondStatus== 1 || item.secondStatus == 3 || item.secondStatus == 4){
	            allValues ='退货中';
	          }else if(item.secondStatus== 2){
	            allValues ='已取消';
	          }
	        }
	        if(item.cancellationType == 3)
	        {
	          if(item.secondStatus === 0 || item.secondStatus == 1){
	            allValues ='取消中';
	          }else{
	            allValues ='已取消';
	          }
	        }
	
	
	        item.allValues = allValues;
	      });
	      return {
	          all: data,
	          toPay: toPay,
	          toReceive: toReceive
	      };
      });
    },
    listreturn:function(){
      var url = [newRoot,'orders/','return'].join('');
      var options = {
        url: url,
        type: 'get',
        headers:{
          'Content-Type': 'application/json'
        }
      };
      return $.ajax(options).then(function(resp) {
        var allValues ;
        if(resp.data.length > 0){
            _.each(resp.data, function(item, index) {
              switch(item.status)
              {
              case 0:
                allValues = '待确认';
                break;
              case 1:
                allValues ='待退货';
                break;
              case 2:
                allValues ='已取消';
                break;
              case 3:
                allValues ='待退货';
                break;
              case 4:
                allValues = '待退款';
                break;
              case 5:
                allValues = '完成';
                break;
              }
              if (item.status == 3 && item.order.pickupStatus == 6) {
                allValues = '待退款';
              }
               item.allValues = allValues;
               item.createTime =formatTime(item.createTime);
            });
            return resp;
        }else{
          return [];
        }
      });
    },
    detail:function(id){
      var url = [newRoot,'orders/',id].join('');
      var options = {
        url: url,
        type: 'get',
        headers:{
          'Content-Type': 'application/json'
        }
      };

      return $.ajax(options).then(function(resp) {
        resp.data.createTime =formatTime(resp.data.createTime);
        var allValues;
        switch(resp.data.status)
        {
        case 1:
          allValues = '待付款';
          break;
        case 2:
          allValues ='已付款';
          break;
        case 3:
          allValues ='待发货';
          break;
        case 4:
          allValues ='待发货';
          break;
        case 5:
          allValues = '已发货';
          break;
        case 6:
          allValues = '已完成';
          break;
        case 7:
          allValues = '已失效';
          break;
        case 8:
          allValues = '已退货';
          break;
        case 9:
          allValues = '已取消';
          break;
        }
        //自提商品
        if (resp.data.status == 5) {
          if (resp.data.pickupStatus == 1 || resp.data.pickupStatus == 2) {
              allValues ='待提货';
          }
        }
        //第二状态
        if(resp.data.cancellationType == 1)
        {
          if(resp.data.secondStatus === 0 || resp.data.secondStatus == 1){
            allValues ='取消中';
          }else if(resp.data.secondStatus == 2){
            allValues ='已取消';
          }
        }
        if(resp.data.cancellationType == 2)
        {
          if(resp.data.secondStatus === 0 || resp.data.secondStatus== 1 || resp.data.secondStatus == 3 || resp.data.secondStatus == 4){
            allValues ='退货中';
          }else if(resp.data.secondStatus== 2){
            allValues ='已取消';
          }
        }
        resp.data.allValues =  allValues;
        return resp;
      });
    },
    returndDetail:function(id){
      var url = [newRoot,'orders/','return/',id].join('');
      var options = {
        url: url,
        type: 'get'
      };

      return $.ajax(options).then(function(resp) {
        resp.data.createTime =formatTime(resp.data.createTime);
        var allValues;
        switch(resp.data.status)
        {
        case 0:
          allValues = '待确认';
          break;
        case 1:
          allValues ='待退货';
          break;
        case 2:
          allValues ='已取消';
          break;
        case 3:
          allValues ='待退货';
          break;
        case 4:
          allValues = '待退款';
          break;
        case 5:
          allValues = '完成';
          break;
        }
        if (resp.data.status == 3 && resp.data.order.pickupStatus == 6) {
          allValues = '待退款';
        }
        resp.data.allValues =  allValues;
        return resp;
      });
    }
  };

  var address = {
    save: function(data) {
      var options = {
        url: [newRoot, 'address'].join(''),
        type: data.id ? 'put' : 'post',
        headers: {
          'Content-Type': 'application/json'
        },
        data: JSON.stringify(data)
      };

      return $.ajax(options).then(function(resp) {
        return resp;
      });
    }
  };

  var spike ={
    rushBuy:function(){
      var url = [newRoot,'rushBuy'].join('');
      var options = {
        url: url,
        type: 'get'
      };
      return $.ajax(options).then(function(resp) {
        //时间戳处理
        var currentFormat,createFormat,endFormat,startFormat;
        resp.currentFormat = formatTime(resp.currentTime);
        _.each(resp.data,function(item){
          item.createFormat = formatTime(item.createTime);
          item.endFormat = formatTime(item.endTime);
          item.startFormat = formatHour(item.startTime);
        });
        return resp;
      });
    },
    rushBuySku:function(id,start,end){
      var url = [newRoot,'rushBuy/','sku?','rushBuyId=',id].join('');
      var options = {
        url: url,
        type: 'get'
      };
      return $.ajax(options).then(function(resp) {
        //时间戳处理
        var currentFormat,createFormat,startTime,endTime;
        resp.startTime = start;
        resp.endTime = end;
        resp.currentFormat = formatTime(resp.currentTime);
        _.each(resp.data,function(item){
          item.createFormat = formatTime(item.createTime);
        });
        return resp;
      });
    }
  };
  /**
   * 传入接口的path和参数，返回完整URL。
   */
  function getUrl(path, data) {
    var search = '';
    if (typeof data === 'object') {
      search = $.param(data);
      if (search) {
        search = '?' + search;
      }
    }
    var napi = config.napi;
    // 去掉前缀最后一个 /
    if (napi.substr(-1) === '/') {
      napi = napi.substr(0, napi.length - 1);
    }
    // 去掉 path 开始的 /
    if (path.substr(0, 1) === '/') {
      path = path.substr(1);
    }
    return (napi + '/' + path + search);
  }

  var http = {
    get: function(url, data) {
      var options = {
        url: getUrl(url, data)
      };
      return $.ajax(options);
    },
    post: function(url, data) {
      data = data || {};
      var options = {
        url: getUrl(url),
        type: 'post',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        data: $.param(data)
      };
      return $.ajax(options);
    },
    postAsync: function(url, data) {
      data = data || {};
      var options = {
        url: getUrl(url),
        type: 'post',
        async: false,
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        data: $.param(data)
      };
      return $.ajax(options);
    },
    postJson: function(url, data) {
      var options = {
        url: getUrl(url),
        type: 'post',
        headers: {
          'Content-Type': 'application/json'
        },
        data: JSON.stringify(data)
      };
      return $.ajax(options);
    },
    putJson: function(url, data) {
      var options = {
        url: getUrl(url),
        type: 'put',
        headers: {
          'Content-Type': 'application/json'
        },
        data: JSON.stringify(data)
      };
      return $.ajax(options);
    },
    delete: function(url, data) {
      var options = {
        url: getUrl(url),
        type: 'delete',
        headers: {
          'Content-Type': 'application/json'
        },
        data: JSON.stringify(data)
      };
      return $.ajax(options);
    }
  };
  function setTitle(title) {
    var body = document.getElementsByTagName('body')[0];
    document.title = title;
    var iframe = document.createElement('iframe');
    iframe.setAttribute('src', 'img/iframeloading.jpg');
    iframe.style.display = 'none';
    iframe.addEventListener('load', function() {
        setTimeout(function() {
            document.body.removeChild(iframe);
        }, 2000)
    });
    document.body.appendChild(iframe);
  }

  function PromiseAll(args) {
    this.promise = $.Deferred();
    this.data = {};
    this.count = 0;
    var self = this;

    for (var name in args) {
      (function(name) {
        args[name].then(function(resp) {
          self.data[name] = resp;
          self.count--;
          if (self.count === 0) {
            self.promise.resolve(self.data);
          }
        });
      })(name);
      this.count++;
    }

    return self.promise;
  }
  function stageRate() {
   	var url = [newRoot,'stageRate',''].join('');
	  var options = {
	    url: url,
	    type: 'get'
	  };
	  return $.ajax(options).then(function(stage) {
	    var stageRateData = {};
	    $.each(stage,function(index,item) {
	      stageRateData[index] = item ;
	    });
	
	    return stageRateData;
	  });
	}

  return {
    http: http,
    admins: admins,
    orders: orders,
    address: address,
    spike: spike,
    formatTime:formatTime,
    setTitle: setTitle,
    PromiseAll: PromiseAll,
    stageRate: stageRate
  };
});
