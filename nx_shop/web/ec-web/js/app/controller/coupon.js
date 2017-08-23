define([
  'underscore',
  'zepto',
  '../services',
  'ui',
  'config',
  'text!../templates/coupon/coupon.html',
  'text!../templates/coupon/coupon_list.html',
  'text!../templates/coupon/coupon_own.html'
], function(_, $, services,ui,config, couponTpl,couponListTpl,couponOwnTpl) {
  var couponController = {
    /* 优惠券页*/
    coupon: function() {
      services.setTitle('优惠券首页');
      var $body = $('.viewport .body');
      var compiled = _.template(couponTpl);
      var rendered = compiled();
      $body.off();
      $body.html(rendered); 
    },
    couponList: function() {
      services.setTitle('我的优惠券');
      var flag = '1';
      this.couponListSerch(flag);
    },
    couponListSerch: function(flag) {
      var _t = this;
      var $body = $('.viewport .body');
      var compiled = _.template(couponListTpl);
      $body.off();
      var params = {};
      var flag = flag;
      switch(flag) {
        case '1': params = {isUsed : '0',isDue : '0'};break;
        case '2': params = {isUsed : '0',isDue : '1'};break;
        case '3': params = {isUsed : '1'};break;
      }
      services.http.get('coupons/mine',params).then(function(resp) {
        var couList = resp.data.rows;
        $.each(couList,function(i,item) {
          switch(flag) {
            case '1': _t.itemUnuseClass(item);break;
            case '2': _t.itemGuoqiClass(item);break;
            case '3': _t.itemUseClass(item);break;
          }
          item.startTime = services.formatTime(item.startTime);
          item.expireTime = services.formatTime(item.expireTime);
          item.receiveTime = services.formatTime(item.receiveTime);
        });
        var rendered = compiled({
          couList: couList,
          flag: flag,
          config: config
        });
        $body.html(rendered);
        $('.couponL li').click(function() {
         _t.couponListSerch($(this).attr('flag'));
        });
        $('.bindB').click(function() {
          var el = $(this).parent().find('input');
          if (!el.val()) {
            ui.makeToast('请输入正确的优惠码');
            return false;
          }
          services.http.post('coupons/code/receive',{code:el.val()}).then(function(resp) {
            if (resp.code == 200) {
               ui.makeToast('绑定成功');
              $('.couponL li[flag="'+flag+'"]').trigger('click');
            } else {
              ui.makeToast(resp.exception);
            }
          });
        });
        $('.couponL li[flag="'+flag+'"]').addClass('change').siblings().removeClass('change');
        $('div[flag="'+flag+'"]').show().siblings().hide();

      });
    },
    itemUnuseClass: function (item) {
      var prx = (config.sys == 'ceb' || config.sys == 'tyfq') ? 'ceb' : 'thb';
      if (item.type == '1' || item.type == '2') {
        item.class = 'coup_youhui_' + prx;
      } else {
        item.class = 'coup_daijin_' + prx;
      }
    },
    itemGuoqiClass: function(item) {
      if (item.type == '1' || item.type == '2') {
        item.class = 'coup_youhui_guoqi';
      } else {
        item.class = 'coup_daijin_guoqi';
      }
    },
    itemUseClass: function(item) {
      if (item.type == '1' || item.type == '2') {
        item.class = 'coup_youhui_use';
      } else {
        item.class = 'coup_daijin_use';
      }
    },
    getUserCouPonCount: function(produId,num) {
      services.http.get('coupons/skus/' + produId + '&' + num).then(function(resp) {
        var avail = [];
        $.each(resp.data,function(i,obj) {
          if (obj.available == '1') {
            avail.push(obj);
          }
        });
        $('.useNum').html(avail.length + '张可用');
      });
    },
    couponSelect: function(produId,ActivitySku,num,productHash,installmentMonths,payData) {
      var _t = this;
      localStorage.setItem('pagePath','couponAddPage');
      _t.produId = produId,_t.num = num,_t.productHash = productHash,_t.installmentMonths = installmentMonths,_t.payData = payData;
      _t.render(ActivitySku);
    },
    render: function(ActivitySku) {
      var _t = this;
      var $body = $('.viewport .body');
      $('#coupon_select')?$('#coupon_select').remove():'';
      $body.append('<div id="coupon_select"></div>');
      var compiled = _.template(couponOwnTpl);
      var $coupon = $('#coupon_select');
      $coupon.off();
      $coupon.siblings().hide();
      services.http.get('coupons/skus/' + _t.produId + '&' + _t.num).then(function(resp) {
        var avail = [],unvail = [];
        $.each(resp.data,function(i,obj) {
          obj.startTime = services.formatTime(obj.startTime);
          obj.expireTime = services.formatTime(obj.expireTime);
          var prx = (config.sys == 'ceb' || config.sys == 'tyfq') ? 'ceb' : 'thb';
          if (obj.available == '1') {
            if (obj.coupon.type == '1' || obj.coupon.type == '2') {
              obj.class = 'coup_youhui_' + prx;
            } else {
              obj.class = 'coup_daijin_' + prx;
            }
            avail.push(obj);
          } else {
            if (obj.isDue == '0' && obj.isUsed == '0') {
              if (obj.coupon.type == '1' || obj.coupon.type == '2') {
                obj.class = 'coup_youhui_shixiao';
              } else {
                obj.class = 'coup_daijin_shixiao';
              }
              unvail.push(obj);
            }
          }
          if (ActivitySku) {
            obj.ActivitySku = ActivitySku;
            if (obj.coupon.type == '1' || obj.coupon.type == '2') {
              if (ActivitySku.coupon.split(',')[0] == 1 && ActivitySku.coupon.split(',')[1] == 2) {
                if (obj.coupon.threshold <= parseFloat(_t.payData.downPayment)) {
                } else {
                  obj.class = 'coup_youhui_shixiao';
                  obj.scopeText = '本活动不可使用';
                }
              } else if (ActivitySku.coupon.split(',')[0] != 1 && !ActivitySku.coupon.split(',')[1]) {
                 obj.class = 'coup_youhui_shixiao';
                 obj.scopeText = '本活动不可使用';
              } else if (parseFloat(_t.payData.downPayment) < obj.coupon.threshold ) {
                 obj.class = 'coup_youhui_shixiao';
                 obj.scopeText = '本活动不可使用';
              }
            } else if (obj.coupon.type == '3' || obj.coupon.type == '4') {
              if (ActivitySku.coupon.split(',')[0] ==1 && ActivitySku.coupon.split(',')[1] ==2) {
                if (obj.coupon.discount + parseFloat(10) <= parseFloat(_t.payData.downPayment)) {
                } else {
                  obj.class = 'coup_daijin_shixiao';
                  obj.scopeText = '本活动不可使用';
                }
              } else if (ActivitySku.coupon.split(',')[0] != 2 && !ActivitySku.coupon.split(',')[1]) {
                 obj.class = 'coup_daijin_shixiao';
                 obj.scopeText = '本活动不可使用';
              } else if (obj.coupon.discount + parseFloat(10)  >  parseFloat(_t.payData.downPayment)) {
                 obj.class = 'coup_daijin_shixiao';
                 obj.scopeText = '本活动不可使用';
              }
            }
          }          
        });
        console.log({avail:avail,unvail:unvail,config:config});
        var rendered = compiled({
          avail: avail,
          unvail: unvail,
          config: config
        });
        $coupon.html(rendered);
        $('.couponOwn .bindB').click(function() {
          if (!$('#code_bind').val()) {
            ui.makeToast('请输入正确的优惠码');
            return false;
          }
          services.http.post('coupons/code/receive',{code: $('#code_bind').val()}).then(function(resp) {
            if (resp.code == 200) {
              ui.makeToast('绑定成功');
              _t.render();
            } else {
              ui.makeToast(resp.exception);
            }
          });
        });
        $('.unuseB span').click(function() {
          localStorage.setItem('pagePath','');
          _t.clearCoupon();
          location.reload();
        });
        $('.coup_youhui_ceb,.coup_daijin_ceb,.coup_youhui_thb,.coup_daijin_thb').click(function() {
           _t.payData.couponRecordId = $(this).attr('couId');
          var couponDiscount = parseFloat($(this).attr('discount'));
          $('#couponMoney').text('￥-' + couponDiscount);
          $('#couponMoney').attr('couponMoney',couponDiscount);
          $('.useNum').text('-' + couponDiscount);
          localStorage.setItem('couPonId',$(this).attr('couId'));
          localStorage.setItem('couPonDiscount',couponDiscount);
          if (config.sys == 'tyfq') {
            var charge = parseFloat(_t.productHash.payment) - couponDiscount;
            if (charge > 0) {
              $('#shoufuMoney').text('￥' + charge);
              var actMoney = _t.productHash.totalMonths - parseFloat($(this).attr('discount'));
              actMoney = actMoney > 0 ? actMoney : 0;
              $('#couponActMoney').text('￥'+actMoney);
              localStorage.setItem('couponActMoney',actMoney);
              localStorage.setItem('shoufuMoney',charge);
            } else {
              var PaymentAmount_price =  _t.productHash.sku.price * parseFloat(_t.num) - couponDiscount ;
              if (PaymentAmount_price) {
                var ServiceCharge_price =  PaymentAmount_price * parseFloat(_t.productHash.stageRate) / 100;
                var TotalOrder_price    =  (_t.productHash.sku.price * parseFloat(_t.num) - couponDiscount) + ServiceCharge_price * parseFloat(_t.installmentMonths) ;
                var Month_price         =  PaymentAmount_price / parseFloat(_t.installmentMonths) + ServiceCharge_price;
              } else {
                var ServiceCharge_price =  0;
                var TotalOrder_price    =  0;
                var Month_price         =  0;
              }
              $('#shoufuMoney').text('￥' + 0);
              $('#couponActMoney').text('￥' + TotalOrder_price);
              localStorage.setItem('shoufuMoney',0);
              localStorage.setItem('couponActMoney',TotalOrder_price);
              if (_t.installmentMonths == 0) {
                $('#couponFenqi').text('￥' + 0);
                localStorage.setItem('couponFenqi',0);
              } else {
                $('#couponFenqi').text('￥' + Month_price.toFixed(2) + ' x ' + _t.installmentMonths);
                localStorage.setItem('couponFenqi',Month_price.toFixed(2) + ' x ' + _t.installmentMonths);
              }
            }
          } else {
            var stage = ((_t.productHash.sku.price * _t.num - couponDiscount) / _t.installmentMonths).toFixed(2) + ' x ' + _t.installmentMonths;
            $('#couponFenqi').text('￥' + stage);
            var actMoney = _t.productHash.totalMonths - parseFloat($(this).attr('discount'));
            actMoney = actMoney > 0 ? actMoney : 0;
            $('#couponActMoney').text('￥' + actMoney.toFixed(2));
            localStorage.setItem('couponFenqi',stage);
            localStorage.setItem('couponActMoney',actMoney.toFixed(2));
          }
          $('#coupon_select').siblings().show();
          $('#coupon_select').remove();
        });
      });
    },
    initOrderMoney: function(payData) {
      var couPonId = localStorage.getItem('couPonId');
      if (couPonId) {
        payData.couponRecordId = couPonId;
        var couponDiscount = localStorage.getItem('couPonDiscount') || 0;
        $('#couponMoney').text('￥-' + couponDiscount);
        $('#couponMoney').attr('couponMoney',couponDiscount);
        $('.useNum').text('-' + couponDiscount);
        var  charge = localStorage.getItem('shoufuMoney') || 0;
        var  TotalOrder_price = localStorage.getItem('couponActMoney') || 0;
        var  stage = localStorage.getItem('couponFenqi') || 0;
        $('#shoufuMoney').text('￥' + charge);
        $('#couponActMoney').text('￥' + TotalOrder_price);
        $('#couponFenqi').text('￥' + stage);
        return true;
      } else {
        return false;
      }
    },
    clearCoupon: function() {
      localStorage.setItem('couPonId','');
      localStorage.setItem('couPonDiscount','');
      localStorage.setItem('shoufuMoney','');
      localStorage.setItem('couponActMoney','');
      localStorage.setItem('couponFenqi','');
    }
  };
  return couponController;
});