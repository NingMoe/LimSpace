define([
  'underscore',
  'zepto',
  'config',
  'weixinjs',
  'md5',
  '../services',
  'ui',
  '../history-back',
  './coupon',
  'text!../templates/order/create.html',
  'text!../templates/order/list.html',
  'text!../templates/order/detail.html',
  'text!../templates/order/return-create.html',
  'text!../templates/order/return-detail.html',
  'text!../templates/order/success.html',
  'text!../templates/order/fail.html',
  'text!../templates/order/installmentAgreement.html',
  'text!../templates/order/cebsucc.html',
  'text!../templates/order/cebfail.html',
  'text!../templates/order/bill.html',
  'text!../templates/order/coupon.html'
],function(_, $, config, wx, md5, services, ui, historyBack, couponController,
  createTpl, listTpl, detailTpl, returnCreateTpl, returnDetailTpl, successTpl, failTpl, installmentAgreementTpl, cebsuccTpl, cebfailTpl, billTpl, couponTpl) {
  function weixinpay(data) {
    services.http.postJson('weixin/unifiedorder', data).then(function(resp) {
      if (resp.wePayChatEntity) {
        resp = resp.wePayChatEntity;
        var params = {
          debug: true,
          'appId': resp.appId, // 公众号名称，由商户传入
          'timeStamp': '' + resp.timestamp, // 时间戳，自1970年以来的秒数
          'nonceStr': resp.nocestr, // 随机串
          'package': resp.strpackage,
          'signType': 'MD5', // 微信签名方式：
          'paySign': resp.sign // 微信签名
        };
        function onBridgeReady() {
          WeixinJSBridge.invoke(
            'getBrandWCPayRequest', params,
            function(res) {
              if (res.err_msg == 'get_brand_wcpay_request:ok') {
                location.href = '/?#order/type/0';
              }
              if (res.err_msg == 'get_brand_wcpay_request:cancel') {
                alert('您取消了支付！');
                location.href = '/?#order/type/0';
              }
              if (res.err_msg == 'get_brand_wcpay_request:fail') {
                alert('支付失败,请稍后再试！');
                location.href = '/?#order/type/0';
              }
            }
          );
        }
        if (typeof WeixinJSBridge == 'undefined') {
          if (document.addEventListener) {
            document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
          } else if (document.attachEvent) {
            document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
            document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
          }
        } else {
          onBridgeReady();
        }
      } else {
        ui.makeToast(resp.message);
        location.reload();
      }
    });
  }

  var pay = {};
  pay.ceb = {
    createOrder: function(payParams) {
      services.http.postJson('orders', payParams).then(function(resp) {
        if (resp.code != 200) {
          ui.makeToast(resp.message);
          return false;
        }
        pay[config.sys].doPay(resp.data.id);
      });
    },
    doPay: function(orderId) {
      services.http.post('creditCardPay', {
        id: orderId
      }).then(function(resp) {
        var $form = $('<form id="cebpay" method="post" action="' + resp.creditCardPay.url + '"><input type="hidden" name="signReqMsg" value="' + resp.creditCardPay.signReqMsg + '"></form>').appendTo('body');
        $form.submit();
      });
    }
  };
  pay.pingan = {
    createOrder: function(payParams) {
      services.http.postJson('orders', payParams.data).then(function(resp) {
        if (resp.code != 200) {
          ui.makeToast(resp.message);
          return false;
        }
        pay[config.sys].doPay();
      });
    },
    doPay: function(payParams) {
      services.http.post('pinganCardPay', {
        id: payParams.orderId
      }).then(function(resp) {
        var data = resp.pinganCardPay;
        var inputs = '';
        var dataSorted = {
          MechantNo: data.mechantNo,
          OrderNo: data.orderNo,
          OrderDate: data.orderDate,
          PayAmount: data.payAmount,
          Currency: data.currency,
          ReturnURL: data.returnURL,
          TermNo: data.termNo,
          JumpURL: data.jumpURL,
          ReservedField: data.reservedField,
          Version: data.version,
          Signature: data.signature,
          SecondMername: data.secondMername
        };
        for (var key in dataSorted) {
          inputs += '<input type="hidden" name="' + key + '" value="' + dataSorted[key] + '">';
        }
        var $form = $('<form id="pinganpay" method="post" action="' + data.url + '">' + inputs + '</form>').appendTo('body');
        $form.submit();
      });
    }
  };

  // 订单操作
  var operateOrder = {
    // 未支付时，取消订单
    refunds: function(orderId) {
      services.http.post('orders/refunds/' + orderId).then(function(resp) {
        if (resp.code != 200) {
          ui.makeToast(resp.message);
          return false;
        }
        location.reload();
      });
    },
    // 弹出遮罩
    showCancel: function() {
      $('.orderCanceBj').show();
      $('.orderCance').show();
    },
    // 已付款订单，取消订单
    cancelOrder: function(orderId) {
      // 申请取消订单备注
      var thisText = $('.orderCance textarea').val().replace(/\s+/g, '');
      // emoji表情正则
      var reText = /^[\u4E00-\u9FA5\w\d\P]|[\uFE30-\uFFA0]+$/;
      var RegularText  = /[\uD800-\uDBFF][\uDC00-\uDFFF]/g;

      if (!(reText.test(thisText) || thisText == '') || RegularText.test(thisText)) {
        alert('请勿输入表情，否则提交失败！');
        return;
      }
      services.http.postJson('orders/cancellations', {
        'orderId': orderId,
        'reason': thisText
      }).then(function(resp) {
        if (resp.code != 200) {
          $('.orderCanceBj').hide();
          $('.orderCances').hide();
          ui.makeToast(resp.message);
          return false;
        }
        location.reload();
      });
    },
    // 删除订单
    orderdelete: function(orderId) {
      layer.open({
        content: '确定要删除订单吗？',
        btn: ['确认', '取消'],
        shadeClose: false,
        yes: function() {
          layer.closeAll('loading');
          services.http.delete('orders/' + orderId).then(function(resp) {
            if (resp.code != 200) {
              ui.makeToast(resp.message);
              return false;
            }
            location.href = '?#order/type/0';
            location.reload();
          });
        },
        no: function() {
          layer.closeAll('loading');
        }
      });
    },
    // 确认收货
    confirmReceived: function(orderId) {
      services.http.putJson('orders/' + orderId).then(function(resp) {
        if (resp.code != 200) {
          ui.makeToast(resp.message);
          return false;
        }
        location.reload();
      });
    }
  };

  // 订单状态
  function statusOperate(data) {
    // status: 订单状态 1: 未付款 2: 已付款 3: 已确认 4: 已制单 5: 已发货 6: 已签收 7：已失效 9: 已取消'
    // cancellationType: 1: 申请取消的第二状态  2: 申请退货的第二状态
    // secondStatus: 1: 已确认 2: 已拒绝 3: 待收货 4: 已收货 5: 完成 (3、4、5只对退货)
    if (data.status == 1) { // 付款 取消，付款操作写在页面上
      data.operate = '取消订单';
      data.class = 'refunds';
    } else if (data.status == 2 || data.status == 3 || data.status == 4 || data.status == 6) {
      if (data.orderSku[0].skuErpCode === 'koo') {
        data.operate = '删除订单';
        data.class = 'orderDelet';
      } else if (!data.cancellationType) {
        if (data.status == 2) {
          data.operate = '申请退款';
          data.class = 'cance';
        } else if (data.status == 3 || data.status == 4) {
          data.operate = '申请退款';
          data.class = 'cance';
        } else if (data.status == 6) { // 删除 申请退货,申请退货操作写在页面上
          data.operate = '删除订单';
          data.class = 'orderDelet';
        }
      } else if (data.cancellationType && (data.secondStatus == 0 || data.secondStatus == 1 || data.secondStatus == 3 || data.secondStatus == 4)) {
        data.operate = '';
        data.class = 'none';
      } else if (data.cancellationType == 1 && data.secondStatus == 2) {
        data.operate = '申请退款';
        data.class = 'cance';
      } else if (data.cancellationType == 2 && data.secondStatus == 2) { // 删除 申请退货
        data.operate = '删除订单';
        data.class = 'orderDelet';
      }
    } else if (data.status == 5) {
      data.operate = '确认收货';
      data.class = 'butSign';
    } else if (data.status == 7 || data.status == 8) {
      data.operate = '删除订单';
      data.class = 'orderDelet';
    } else if (data.status == 9 && data.secondStatus != 1) {
      data.operate = '删除订单';
      data.class = 'orderDelet';
    }
  }

  var orderController = {
    /**
     * 下单页
     */
    create: function(id, calback) {
      services.setTitle('订单确认');

      var compiled = _.template(createTpl);
      var $body = $('.viewport .body');

      // 数量
      var match = location.hash.match(/n=(\d+)/);
      var count = match[1];
      // 期数 首付
      match = location.hash.match(/m=(\d+)/);
      var installmentMonths = match[1] || 0;
      // 地址id
      match = location.hash.match(/addressId=(\d+)/);
      var addressId = match !== null ? match[1] : null;
      // 优惠券
      match = location.hash.match(/couponId=(\d+)/);
      var couponId = match !== null ? match[1] : null;
      // 发票
      match = location.hash.match(/invoice=([^&#]+)/);
      var invoice = match !== null ? match[1] ? decodeURIComponent(match[1]) : null : null;
      var params = {
        id: id,
        count: count,
        installmentMonths: installmentMonths,
        addressId: addressId,
        couponId: couponId,
        invoice: invoice
      };
      var payData = {};

      var request = {
        product: services.admins.details(id),
        stageRate: function() {
          return services.http.get('stageRate', '');
        },
        skuActivityData: services.http.postJson('findActivitySkuId', {
          skuId: id
        }),
        address: function() {
          var promise = $.Deferred();
          // 如果是选择了地址后，返回此页
          if (addressId) {
            // 请求地址
            services.http.get('address/' + addressId).then(function(resp) {
              promise.resolve(resp.data);
            });
          } else {
            // 获取用户收获地址列表，并渲染收获地址模块
            services.http.get('address').then(function(resp) {
              // 选择默认地址
              for (var i = resp.data.length - 1, address; i >= 0; i--) {
                address = resp.data[i];
                // 存在默认地址
                if (address.isDefault) {
                  promise.resolve(address);
                  return;
                }
              }
              promise.resolve(null);
            });
          }

          return promise;
        },
        // 请求可用优惠券信息
        coupon: services.http.get('coupons/skus/' + id + '&' + count)
      };

      // 回退问题
      if (historyBack.target) {
        if (historyBack.target == 'confirm') {
          var newHash, addressId = historyBack.data.addressId;
          var pickupaddressId = historyBack.data.pickupaddressId;
          if (addressId) {
            if (location.hash.indexOf('addressId=') == -1) {
              newHash = location.hash + '&addressId=' + addressId;
            } else {
              newHash = location.hash.replace(/addressId=\d+/, 'addressId=' + addressId);
            }
          }
          history.replaceState(null, '', newHash);
          calback && calback();
          historyBack.reset();
          $(window).trigger('route');
          return;
        } else {
          historyBack.reset();
        }
      }
      function render(data) {
        var rendered = compiled(data);
        $body.off();
        $body.html(rendered);

        payData = {
          skuId: parseInt(id),
          skuCount: count,
          addressId: data.address ? data.address.id : '',
          downPayment: 0,
          installmentMonths: parseInt(installmentMonths),
          invoiceTitle: params.invoice ? params.invoice : data.address ? data.address.username : '',
          activityId: data.activity ? data.activity.activityId : '',
          couponRecordId: data.coupon ? data.coupon.selected ? data.coupon.selected.id : '' : ''
        };
        // 如果是新东方
        if (data.isKoo) {
          delete payData.addressId;
          payData.addressMobile = $('.reservemobile input').val();
        }
      }
      var events = {
        noticecheck: function() {
          var $checkbox = $('.noticecheckbox input[name="checkbox"]');
          if (!$checkbox.attr('checked')) {
            $checkbox.removeAttr('checked');
            $('.confirm .button').attr({
              'disabled': 'disabled'
            });
            ui.makeToast('请勾选退换货规则');
          } else {
            $checkbox.attr('checked');
            var mobile = $('.confirm .reservemobile input').val();
            var regut = /^[1][0-9]{10}$/;
            if (!regut.test(mobile)) {
              ui.makeToast('请输入正确的11位手机号');
            } else {
              $('.confirm .button').removeAttr('disabled');
            }
          }
        },
        coupon: function() {
          if ($('.confirm .order_coupon i').text() == '优惠不可用') {
            return;
          }
          var data = {
            referrer: location.href,
            data: [
              [id, count]
            ]
          };
          location.href = '/?#order/coupon?data=' + encodeURIComponent(JSON.stringify(data));
        },
        goInvoice: function() {
          var data = {
            referrer: location.href,
            data: invoice
          };
          location.href = '/?#bill?data=' + encodeURIComponent(JSON.stringify(data));
        },
        createOrderAndDoPay: function() {
          if ($('.reservemobile input')) {
            payData.addressMobile = $('.reservemobile input').val();
          }
          pay[config.sys].createOrder(payData);
        }
      };

      function bindEvents() {
        // 新东方
        $body.on('blur', '.confirm .reservemobile input', events.noticecheck);
        $body.on('click', '.noticecheckbox input', events.noticecheck);

        $body.on('click', '.confirm .button', events.createOrderAndDoPay);
        $body.on('click', '.confirm .order_coupon', events.coupon);
        $body.on('click', '.confirm .invoice-wrapper', events.goInvoice);
      }

      // 加载商品详情
      var productPromise = request.product.then(function(product) {
        if (product.code != 200) {
          console.error('加载商品详情出错：', product);
          return null;
        }

        // 把属性拼成一行描述性文字，放在“规格”下
        var spuAttrMap = {};
        _.each(product.spuAttrs, function(spuAttr) {
          spuAttrMap[spuAttr.id] = spuAttr.attr.name;
        });
        var specs = [];
        _.each(product.sku.attrs, function(attr) {
          specs.push(spuAttrMap[attr.spuAttributeId] + ':' + attr.attributeValue);
        });
        product.specs = specs.join(' ');

        return product;
      });

      // 查找秒杀/限购活动
      var activityPromise = request.skuActivityData.then(function(activity) {
        if (activity.code != 200 || !_.isArray(activity.data) || activity.data.length === 0) {
          return null;
        }
        return activity.data[0];
      });

      // 获取收货地址
      var addressPromise = request.address();

      // 获取优惠券
      var couponPromise = request.coupon.then(function(coupon) {
        if (coupon.code != 200 || !_.isArray(coupon.data) || coupon.data.length === 0) {
          return null;
        }
        // 如果已经选择优惠券
        var selected = null;
        var availableCount = 0;
        // 遍历每张优惠券，计算可用张数，以及根据URL参数中的couponId，查找使用的优惠券的金额
        _.each(coupon.data, function(couponItem) {
          if (couponItem.available == 1) {
            availableCount++;
            if (couponId == couponItem.id) {
              selected = couponItem;
            }
          }
        });
        var couponData = {
          availableCount: availableCount,
          selected: selected
        };

        return couponData;
      });

      var promiseList = {
        product: productPromise,
        activity: activityPromise,
        address: addressPromise,
        coupon: couponPromise
      };
      // 如果是分期，并且有分期手续费
      if (config.hasInstallmentInterest) {
        // 获取分析利率
        promiseList.stageRate = request.stageRate();
      }

      var promiseAll = new services.PromiseAll(promiseList).then(function(data) {
        console.log(data);
        var sku = data.product.sku;

        var order = {};
        // 商品总额
        order.totalSkuPrice = sku.price * count;

        // 根据活动修改订单的应付金额
        if (data.activity) {
          // 限购
          if (data.activity.activityType == 2) {
            // 未超过限购件数
            if (data.activity.purchaseNum >= count) {
              order.totalSkuPrice = data.activity.activityPrice * count;
            }
          // 秒杀
          } else if (data.activity.activityType == 1) {
            order.totalSkuPrice = data.activity.activityPrice * count;
          // 第二件半价
          } else if (data.activity.activityType == 3) {
            // 偶数个
            if (params.count % 2 === 0) {
              order.totalSkuPrice = order.totalSkuPrice * 3 / 4;
            // 奇数个(>=3)
            } else {
              order.totalSkuPrice = sku.price * (count - 1) * 3 / 4 + sku.price;
            }
          }
        }

        // 应付金额
        order.orderPrice = order.totalSkuPrice;

        order.discount = 0;
        if (data.coupon && data.coupon.selected) {
          // 优惠金额(只包括优惠券、代金券，不包括活动的折扣)
          order.discount = Math.min(order.orderPrice, data.coupon.selected.coupon.discount);
          // 应付金额 = 商品总额(已减掉了活动的折扣) - 优惠金额
          order.orderPrice = order.totalSkuPrice - order.discount;
        }

        data.order = order;
        data.config = config;
        data.params = params;
        data.isKoo = data.product.sku.erpCode === 'koo';
        render(data);
        bindEvents();
      });
    },
    /**
     * 下单页用户选择优惠券页面
     */
    coupon: function(data) {
      services.setTitle('选择优惠券');

      var compiled = _.template(couponTpl);
      var $body = $('.viewport .body');

      // 请求参数
      var id = data.data[0][0];
      var count = data.data[0][1];

      var request = {
        // 请求可用优惠券信息
        coupon: function() {
          return services.http.get('coupons/skus/' + id + '&' + count);
        }
      };

      function action() {
        request.coupon().then(function(response) {
          render({
            data: response.data,
            config: config,
            formatTime: services.formatTime
          });
          bindEvents();
        });
      }
      action();

      function render(data) {
        var rendered = compiled(data);
        $body.off();
        $body.html(rendered);
      }

      // 跳转到下单页
      function go(couponId) {
        location.href = data.referrer.replace(/&couponId=[^&#]*/g, '') + '&couponId=' + (couponId || '');
      }

      var events = {
        // 不使用优惠券/代金券
        noCoupon: function() {
          go();
        },
        // 选择优惠券
        selectCoupon: function() {
          go($(this).data('id'));
        },
        // 绑定优惠码/代金码
        couponCodeToCoupon: function() {
          var couponCode = $.trim($('#code_bind').val());
          if (!couponCode) {
            ui.makeToast('请输入正确的优惠码');
            return false;
          }

          services.http.post('coupons/code/receive', {
            code: couponCode
          }).then(function(resp) {
            // 绑定成功，需要重新渲染此页面
            if (resp.code == 200) {
              ui.makeToast('绑定成功');
              action();
            } else {
              ui.makeToast(resp.exception);
            }
          });
        }
      };

      function bindEvents() {
        $body.on('click', '.unuseB span', events.noCoupon); // 不使用优惠券/代金券
        $body.on('click', '.coupon-item[data-is-available="1"]', events.selectCoupon); // 选择优惠券
        $body.on('click', '.bindB', events.couponCodeToCoupon); // 绑定优惠码/代金码
      }
    },
    /**
     * 订单列表页
     */
    list: function(type) {
      services.setTitle('订单列表');

      var compiled = _.template(listTpl);
      var $body = $('.viewport .body');

      var request = {
        stageRate: services.stageRate(),
        listOrder: services.orders.listOrder(),
        listreturn: services.orders.listreturn()
      };

      function render(data) {
        var rendered = compiled(data);
        $body.off();
        $body.html(rendered);
      }

      var events = {
        // 立即支付
        toPay: function() {
          pay[config.sys].doPay($(this).data('id'));
        },
        // 未付款，取消订单
        closeOrder: function() {
          operateOrder.refunds($(this).data('id'));
        },
        // 显示取消订单页面
        toCancel: function() {
          $('.orderCance button').data('id', $(this).data('id'));
          operateOrder.showCancel();
        },
        // 操作取消订单
        doCancelOrder: function() {
          operateOrder.cancelOrder(Number($(this).data('id')));
        },
        // 删除订单
        deleteOrder: function() {
          operateOrder.orderdelete($(this).data('id'));
        },
        // 确认收货
        confirmReceive: function() {
          operateOrder.confirmReceived($(this).data('id'));
        }
      };

      function bindEvents() {
        $body.on('click', '.orders-cont .cebpay', events.toPay);
        $body.on('click', '.orders-cont .refunds', events.closeOrder);
        $body.on('click', '.orders-cont .cance', events.toCancel);
        $body.on('click', '.orderCance .cances', events.doCancelOrder);
        $body.on('click', '.orders-cont .orderDelet', events.deleteOrder);
        $body.on('click', '.orders-cont .butSign', events.confirmReceive);
      }

      // 加载订单列表页
      var listOrderPromise = request.listOrder.then(function(listOrder) {
        return listOrder;
      });
      var stageRatePromise = request.stageRate.then(function(stage) {
        return stage;
      });
      // 加载退货列表
      var listreturnPromise = request.listreturn.then(function(listreturn) {
        return listreturn;
      });

      var promiseList = {
        orders: listOrderPromise,
        returns: listreturnPromise
      };

      // 如果是分期，并且有分期手续费
      if (config.installment) {
        // 获取分析利率
        promiseList.stageRateData = stageRatePromise;
      }

      var promiseAll = new services.PromiseAll(promiseList).then(function(data) {
        // 存储订单数量
        var orderNumber = {};

        // 计算未付款订单数量
        if (data.orders.toPay.length > 0 && data.orders.toPay.length <= 9) {
          orderNumber.toPay = data.orders.toPay.length;
        } else if (data.orders.toPay.length > 9) {
          orderNumber.toPay = 9;
        }
        // 计算待收货订单数量
        if (data.orders.toReceive.length > 0 && data.orders.toReceive.length <= 9) {
          orderNumber.toReceive = data.orders.toReceive.length;
        } else if (data.orders.toReceive.length > 9) {
          orderNumber.toReceive = 9;
        }

        // 处理不同订单状态，需要哪些操作
        _.each(data.orders.all, function(item) {
          statusOperate(item);
        });

        data.type = type;
        data.orderNumber = orderNumber;
        data.config = config;
        render(data);
        bindEvents();
      });
    },
    /**
     * 发票详情页
     */
    bill:function(params) {
      services.setTitle('发票');

      var compiled = _.template(billTpl);
      var $body = $('.viewport .body');

      var rendered = compiled(params);
      $body.html(rendered);
      $body.off();

      $('.rise').children('input').val(params.data || '');
      $body.on('click', '.bill .sure', function() {
        var rise = $('.bill .rise input').val();
        location.href = params.referrer.replace(/&invoice=[^&#]*/g, '') + '&invoice=' + (rise || '');
      });
    },
    /**
     * 订单详情页
     */
    detail: function(id) {
      services.setTitle('订单详情');

      var compiled = _.template(detailTpl);
      var $body = $('.viewport .body');

      // 订单id
      match = location.hash.match(/(\d+)/);
      var orderId = match !== null ? match[1] : null;

      var request = {
        stageRate: services.stageRate(),
        orderDetail: services.orders.detail(id)
      };

      function render(data) {
        var rendered = compiled(data);
        $body.off();
        $body.html(rendered);
      }

      var events = {
        // 立即支付
        toPay: function() {
          pay[config.sys].doPay(orderId);
        },
        // 未付款，取消订单
        closeOrder: function() {
          operateOrder.refunds(orderId);
        },
        // 显示取消订单页面
        toCancel: function() {
          operateOrder.showCancel();
        },
        // 操作取消订单
        doCancelOrder: function() {
          operateOrder.cancelOrder(Number(orderId));
        },
        // 删除订单
        deleteOrder: function() {
          operateOrder.orderdelete(orderId);
        },
        // 确认收货
        confirmReceive: function() {
          operateOrder.confirmReceived(orderId);
        }
      };

      function bindEvents() {
        $body.on('click', '.detail .cebpay', events.toPay);
        $body.on('click', '.detail .refunds', events.closeOrder);
        $body.on('click', '.detail .cance', events.toCancel);
        $body.on('click', '.orderCance .cances', events.doCancelOrder);
        $body.on('click', '.detail .orderDelet', events.deleteOrder);
        $body.on('click', '.detail .butSign', events.confirmReceive);
      }

      // 加载订单详情
      var orderDetailPromise = request.orderDetail.then(function(orderDetail) {
        if (orderDetail.code != 200) {
          console.error('加载订单详情出错：', orderDetail);
          return null;
        }

        orderDetail.data.discount = 0;
        var payMentList = orderDetail.data.orderPaymentList || [];
        $.each(payMentList, function(i, item) {
          if (item.category == 3 && item.type == 11) {
            orderDetail.data.discount = item.amount;
          }
        });

        return orderDetail;
      });
      var stageRatePromise = request.stageRate.then(function(stage) {
        return stage;
      });

      var promiseList = {
        resp: orderDetailPromise
      };

      // 如果是分期，并且有分期手续费
      if (config.installment) {
        // 获取分析利率
        promiseList.stageRateData = stageRatePromise;
      }

      var promiseAll = new services.PromiseAll(promiseList).then(function(data) {
        statusOperate(data.resp.data);

        data.config = config;
        data.isKoo = data.resp.data.orderSku[0].skuErpCode === 'koo';
        render(data);
        bindEvents();
      });
    },
    /**
     * 退货申请页
     */
    createReturn: function(id) {
      services.setTitle('退货申请');

      var compiled = _.template(returnCreateTpl);
      var $body = $('.viewport .body');

      var returndata = {};

      var request = {
        orderDetail: services.orders.detail(id)
      };

      function render(data) {
        var rendered = compiled(data);
        $body.off();
        $body.html(rendered);
      }

      var events = {
        submit: function() {
          returndata.id = id;
          returndata.reasonType = $('.return .reason input:checked').val();
          returndata.orderText = $('.return textarea').val();
          // emoji表情正则
          var reText = /^[\u4E00-\u9FA5\w\d\P]|[\uFE30-\uFFA0]+$/;
          var RegularText  = /[\uD800-\uDBFF][\uDC00-\uDFFF]/g;

          if (!returndata.reasonType) {
            ui.makeToast('请选择退货原因');
            return;
          }
          if (!returndata.orderText) {
            ui.makeToast('请输入问题描述');
            return;
          }
          if (!reText.test(returndata.orderText) || RegularText.test(returndata.orderText)) {
            ui.makeToast('请勿输入表情，否则提交失败！');
            return;
          }
          services.http.postJson('orders/return', {
            orderId: returndata.id,
            reason: returndata.orderText,
            reasonType: returndata.reasonType
          }).then(function(resp) {
            $('.return button').attr('disabled', 'disabled');
            if (resp.code == 200) {
              history.back();
            } else {
              $('.return button').attr('disabled', '');
              alert(resp.message);
            }
          });
        }
      };

      function bindEvents() {
        $body.on('click', '.return .button', events.submit);
      }

      var orderDetailPromise = request.orderDetail.then(function(orderDetail) {
        if (orderDetail.code != 200) {
          console.error('加载订单详情出错：', orderDetail);
          return null;
        }

        orderDetail.data.discount = 0;
        var payMentList = orderDetail.data.orderPaymentList || [];
        $.each(payMentList, function(i, item) {
          if (item.category == 3 && item.type == 11) {
            orderDetail.data.discount = item.amount;
          }
        });

        return orderDetail;
      });

      var promiseList = {
        resp: orderDetailPromise
      };

      var promiseAll = new services.PromiseAll(promiseList).then(function(data) {
        data.config = config;
        render(data);
        bindEvents();
      });
    },
    /**
     * 退货详情页
     */
    returnDetail: function(id) {
      services.setTitle('退货详情');

      var compiled = _.template(returnDetailTpl);
      var $body = $('.viewport .body');

      var request = {
        returndDetail: services.orders.returndDetail(id)
      };

      function render(data) {
        var rendered = compiled(data);
        $body.off();
        $body.html(rendered);
      }

      var events = {
        submit: function() {
          services.http.putJson('cancelcancellation/' + id).then(function(data) {
            if (data.code != 200) {
              ui.makeToast(data.message);
            }
            ui.makeToast('取消成功！');
            location.reload();
          });
        }
      };

      function bindEvents() {
        $body.on('click', '.returns button', events.submit);
      }

      // 加载退货详情页
      var returnDetail = request.returndDetail.then(function(resp) {
        return resp;
      });

      var promiseList = {
        resp: returnDetail
      };

      var promiseAll = new services.PromiseAll(promiseList).then(function(data) {
        data.config = config;
        render(data);
        bindEvents();
      });
    },
    cebsucc: function() {
      services.setTitle('支付成功');
      var compiled = _.template(cebsuccTpl);
      var $body = $('.viewport .body');
      var rendered = compiled();
      $body.off();
      $body.html(rendered);
    },
    cebfail: function() {
      services.setTitle('支付失败');
      var compiled = _.template(cebfailTpl);
      var $body = $('.viewport .body');
      var rendered = compiled();
      $body.off();
      $body.html(rendered);
    }
  };

  return orderController;
});
