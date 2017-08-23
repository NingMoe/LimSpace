define([
  'underscore',
  'zepto',
  'scooch',
  'ui',
  'config',
  '../services',
  '../history-back',
  'text!../templates/product/index.html',
  'text!../templates/product/orientalindex.html',
  'text!../templates/product/purchase.html',
  'text!../templates/product/notice.html',
], function(_, $, scooch, ui, config, services, historyBack, indexTpl, orientalindexTpl,purchaseTpl,noticeTpl) {
	// 收藏
	function favourite(skuid ,favoriteindex) {
    services.http.postJson('favorite/' + skuid).then(function(resp) {
      if (resp.data) {
        if (favoriteindex % 2 == 0) {
          $('.Attribute .show_pies > div:first-child > span.favorite').addClass('isFavorite').removeClass('favorite').text('已收藏');
        } else {
          $('.Attribute .show_pies > div:first-child > span.isFavorite').addClass('favorite').removeClass('isFavorite').text('收藏');
        }
      } else {
        ui.makeToast('resp.message');
      }
    }).fail(function(jqXHR, textStatus, errorThrown) {
      // 未登录，跳转到登录页面
      if (jqXHR.status == 401) {
        location.href = '/?#connects?fwd=' + encodeURIComponent(location.href);
      } else {
        ui.makeToast(errorThrown);
      }
    });
	}
  var productController = {
    /**
     * 商品详情页
     * @param  int id skuId
     */
    index: function(id,calback) {
      services.setTitle('商品详情');

      var compiled = _.template(indexTpl);
      var $body = $('.viewport .body');

      var isVali = true;
      var favoriteindex = 0;
      var ActivitySku = ''; // 限购信息数据
      var flag = true;

      function paddingZero(num) {
        return num >= 10 ? num : '0' + num;
      }

      var request = {
        stageRate: services.stageRate(),
        detail: services.admins.details(parseFloat(id)),
        findActivity: services.http.postJson('findActivitySkuId', {
          skuId: parseFloat(id)
        })
      };
      function render(data) {
        var rendered = compiled(data);
        $body.off();
        $body.html(rendered);

        // 图文详情滚动到不同位置的样式
        var height = $('.typeTab').offset().top;
        window.onscroll = function() {
          console.log(document.body.scrollTop);
          console.log(height);
          if (document.body.scrollTop >= (height)) {
            $('div.typeTab').addClass('fixed').removeClass('static');
          } else {
            $('div.typeTab').addClass('static').removeClass('fixed');
          }
        };
				// 轮播
        $('.m-scooch').scooch();
      }

      var events = {
      	// 键盘弹出的时候，立即购买按钮不 fixed
        adjustWithKeyboard: function() {
          if (document.activeElement === $('.model .text_box')[0] ||
            document.activeElement === $('.model .firstpay')[0]) {
            $('.pj-action-add').addClass('keyboard');
          } else {
            $('.pj-action-add').removeClass('keyboard');
          }
        },
        submint: function() {
          if (!isVali) {
            return false;
          }
          var stages = parseInt($('.model .installment a.clspan').text());
          if (!stages) {
            stages = 0;
          }
          var url = '/?#order/create/' + id + '?m=' + stages + '&n=' + $('.model .text_box').val();
          services.http.get('users').then(function(data) {
            location.href = url;
          }).fail(function(jqXHR, textStatus, errorThrown) {
            // 未登录，跳转到登录页面
            if (jqXHR.status == 401) {
              location.href = '/?#connects?fwd=' + encodeURIComponent(location.href);
            } else {
              ui.makeToast(errorThrown);
            }
          });
        },
        // 比较购买数量和库存
        selectLast: function() {
          var price = $('.Attribute .show_pies .unitprice').data('price');
          var stages = parseInt($('.model .installment a.clspan').text());
          var number = $('.model .text_box').val();
          var skuNum;
          // 有促销活动
          if (ActivitySku) {
            skuNum = parseInt($('.text_box').val());
          } else {
            number = parseInt($('.text_box').val());
          }
          if (ActivitySku.reservedInventory < 0) {
            ActivitySku.reservedInventory = 0;
          }
          if (ActivitySku) {
            if (ActivitySku.activityType == 2) { // 限购
              if (ActivitySku.activityMode == 1) { // 允许超卖
              } else if (ActivitySku.activityMode == 2) { // 不允许超卖
                if (skuNum > ActivitySku.reservedInventory) {
                  $('.text_box').val(ActivitySku.reservedInventory);
                  ui.makeToast('库存不足');
                }
                if (skuNum > (ActivitySku.purchaseNum - ActivitySku.buyNum)) {
                  $('.model .text_box').val(ActivitySku.purchaseNum - ActivitySku.buyNum);
                  ui.makeToast('最多购买' + ActivitySku.purchaseNum + '件');
                }
              }
            } else if (ActivitySku.activityType == 1) { // 秒杀处理
              if (ActivitySku.activityMode == 2) { // 不允许超卖
                if (skuNum > ActivitySku.reservedInventory) {
                  $('.text_box').val(ActivitySku.reservedInventory);
                  ui.makeToast('库存不足');
                }
                if (skuNum > (ActivitySku.purchaseNum - ActivitySku.buyNum)) {
                  if (ActivitySku.purchasefalse) {
                    $('.model .text_box').val(ActivitySku.purchaseNum - ActivitySku.buyNum);
                    ui.makeToast('最多购买' + ActivitySku.purchaseNum + '件');
                  }
                }
              }
            }
          } else {
            // 没有促销活动
            if (number != 1 && number != 2 && number != 3 && number != 4 && number != 5 && number != 6 && number != 7 && number != 8 && number != 9) {
              $('.model .text_box').val('1');
              if (number < 1) {
                ui.makeToast('最小购买数量为1件');
                $('.model .text_box').val('1');
              }
              if (number > 9) {
                ui.makeToast('最大购买数量为9件');
                $('.model .text_box').val('9');
              }
            }
          }
          isVali = true;
          number = $('.model .text_box').val();
          var dataRate = ((price * number) / stages).toFixed(2);
          $('.month').text('月供：￥' + dataRate + '*' + stages + '期');

          return isVali;
        },
        // 切换不同分期数
        selectFirst: function() {
          var price = $('.Attribute .show_pies .unitprice').data('price');
          var number = $('.model .text_box').val();
          if (!$(this).hasClass('clspan')) {
            $(this).not('.firstpay').addClass('clspan').siblings().removeClass('clspan');
          } else {
            $(this).siblings().removeClass('clspan');
          }
          var stages = parseInt($('.model .installment a.clspan').text());
          $('.month').text('月供：￥' + (((price * number) / stages)).toFixed(2) + '*' + stages + '期');
        },
        // 减数量
        min: function() {
          if (($(this).next().val()) <= 1) {
            ui.makeToast('最小购买数量为1件');
            return;
          } else {
            $(this).next().val(parseInt($(this).next().val()) - 1);
          }
        }, 
        // 加数量 
        add: function() {
          $(this).prev().val(parseInt($(this).prev().val()) + 1);
        },
        // 收藏
        favorite: function() {
          var skuid = $(this).data('skuid');
          if (flag) {
            favoriteindex = $(this).data('favoriteindex');
            flag = false;
          }
          favourite(skuid, favoriteindex);
          favoriteindex++;
        }
      };

      function bindEvents() {
        $body.on('blur focus click', '.model .text_box', events.adjustWithKeyboard);
        $body.on('click', '.index_but .submint', events.submint);
        $body.on('click', '.model .installment a', events.selectFirst);
        // 点击数量加 减 包括数量输入框自身改变
        $body.on('click', '.model .add', events.add);
        $body.on('click', '.model .min', events.min);
        $body.on('click', '.model .add', events.selectLast);
        $body.on('click', '.model .min', events.selectLast);
        $body.on('blur', '.model .text_box', events.selectLast);
        $body.on('change', '.model .text_box', events.selectLast);
        $body.on('click', '.Attribute .show_pies > div:first-child > span:last-child', events.favorite);
      }

      // 加载商品详情
      var detailPromise = request.detail.then(function(detail) {
        if (detail.code != 200) {
          console.error('加载商品详情出错：', detail);
          return null;
        }
        return detail;
      });
      var stageRatePromise = request.stageRate.then(function(stage) {
        return stage;
      });
      var findActivityPromise = request.findActivity.then(function(Activity) {
        if (Activity.code == 401) {
          location.href = '/?#connects?fwd=' + encodeURIComponent(location.href);
          return false;
          Activity = false;
          ActivitySku = false;
        }

        return Activity;
      });

      var promiseList = {
        resp: detailPromise,
        Activity: findActivityPromise
      };

      // 如果是分期，并且有分期手续费
      if (config.installment) {
        // 获取分析利率
        promiseList.stageRateData = stageRatePromise;
      }

      var promiseAll = new services.PromiseAll(promiseList).then(function(data) {
        console.log(data);

        // 当前skuid 没有参加限购活动
        if (data.Activity.code != 400) {
        	// 处理能够购买的数量
          if (data.Activity.data.length === 0) {
            console.log('当前skuid 没有参加活动');
            data.Activity = false;
            ActivitySku = false;
          } else {
            ActivitySku = data.Activity.data[0];
            data.respSku = $.extend(true, {}, data.resp.sku);
            data.resp.sku.price = data.Activity.data[0].activityPrice;
            data.resp.displayspan = false;
            ActivitySku.purchasefalse = true;
            if (!data.Activity.data[0].purchaseNum) {
              data.Activity.data[0].purchaseNum = data.Activity.data[0].reservedInventory + 1;
              data.resp.displayspan = true;
              ActivitySku.purchasefalse = false;
            }
            data.resp.sku.purchaseNum = data.Activity.data[0].purchaseNum;
            data.resp.sku.noo = data.Activity.data[0].purchaseNum - data.Activity.data[0].buyNum;
          }
			    // 秒杀商品显示倒计时
          if (ActivitySku.activityType == 1 ) { // 秒杀
            if (data.Activity.data[0].activityStatus == 1 && data.Activity.data[0].activityStatus == 1) {
              var time_start = data.Activity.data[0].nowTime; // 设定当前时间
              var time_end = data.Activity.data[0].endTime; // 设定目标时间
              var time_distance = time_end - time_start;
            }
            var clearval = setInterval(function() {
              if (location.href.split('?')[1].split('/')[0] == '#product') {
                time_distance = parseFloat(time_distance - 1000);
                if (time_distance > 0) {
                  var int_second = Math.floor((time_distance / 1000) % 3600 % 60);
                  var int_minute = Math.floor((time_distance / 60000) % 60);
                  var int_hour = Math.floor((time_distance / 3600000));
                  // 显示时间
                  $('.Countdown .seckillTimer .hour').text(paddingZero(int_hour));
                  $('.Countdown .seckillTimer .min').text(paddingZero(int_minute));
                  $('.Countdown .seckillTimer .second').text(paddingZero(int_second));
                } else {
                  clearInterval(clearval);
                  location.reload(true);
                }
              } else {
                clearInterval(clearval);
              }
            }, 1000);
          }
        } else {
          console.log('当前skuid 没有参加活动');
          data.Activity = false;
          ActivitySku = false;
	    	}
        
        data.config = config;
        render(data);
        bindEvents();
      });
    },
    // 新东方
    orientalindex: function(id,calback) {
      services.setTitle('商品详情');

      var compiled = _.template(orientalindexTpl);
      var $body = $('.viewport .body');

      var favoriteindex = 0;
      var isVali = true;
      var flag = true;

      var params = {};
      var request = {
        stageRate: services.stageRate(),
        detail: services.admins.details(parseFloat(id)),
        detailextend: services.http.get('skuExt/' + id),
        kooCourses: services.http.get('kooCourses/' + params.productid)
      };

      function render(data) {
        var rendered = compiled(data);
        $body.off();
        $body.html(rendered);

        var height = $('.typeTab').offset().top;
        window.onscroll = function() {
          if (document.body.scrollTop >= (height)) {
            $('div.typeTab').addClass('fixed').removeClass('static');
            $('.typeTabs').show();
          } else {
            $('div.typeTab').addClass('static').removeClass('fixed');
            $('.typeTabs').hide();
          }
        };

        // 轮播
        $('.m-scooch').scooch();
      }

      var events = {
        // 切换菜单（简介、图文详情、课程表）
        typeTab: function() {
          var thislength = $(this).index();
          $(this).addClass('clickspan').siblings().removeClass('clickspan');
          $('.type_list>div').eq(thislength).show().siblings().hide();
        },
        // 立即购买
        submint: function() {
          if (!isVali) {
            return false;
          }

          var stages = parseInt($('.model .installment a.clspan').text());
          if (!stages) {
            stages = 0;
          }
          var url = '/?#order/create/' + id + '?m=' + stages + '&n=1';
          services.http.get('users').then(function(data) {
            location.href = url;
          }).fail(function(jqXHR, textStatus, errorThrown) {
            // 未登录，跳转到登录页面
            if (jqXHR.status == 401) {
              location.href = '/?#connects?fwd=' + encodeURIComponent(location.href);
            } else {
              ui.makeToast(errorThrown);
            }
          });
        },
        // 收藏
        favorite: function() {
          var skuid = $(this).data('skuid');
          if (flag) {
            favoriteindex = $(this).data('favoriteindex');
            flag = false;
          }
          favourite(skuid, favoriteindex);
          favoriteindex++;
        },
        // 切换不同期数
        selectFirst: function() {
          var price = $('.Attribute .show_pies .unitprice').data('price');
          if (!$(this).hasClass('clspan')) {
            $(this).not('.firstpay').addClass('clspan').siblings().removeClass('clspan');
          } else {
            $(this).siblings().removeClass('clspan');
          }
          var stages = parseInt($('.model .installment a.clspan').text());
          $('.month').text('月供：￥' + ((price / stages)).toFixed(2) + '*' + stages + '期');
        },
        // 课程切换
        slide: function() {
          if ($(this).siblings('ul').css('display') == 'none') {
            $(this).parent('li').siblings('li').removeClass('inactives');
            $(this).addClass('inactives');
            $(this).parent('li').siblings('li').removeClass('close');
            $(this).addClass('spread');
            $(this).siblings('ul').css('display', 'block');

            if ($(this).parents('li').siblings('li').children('ul').css('display') == 'block') {
              $(this).parents('li').siblings('li').children('ul').parent('li').children('div').removeClass('inactives');
              $(this).parents('li').siblings('li').children('ul').css('display', 'none');
              $(this).parents('li').siblings('li').children('ul').children('li').children('ul').css('display', 'none');
              $(this).parents('li').siblings('li').children('div').removeClass('spread');
            }
          } else {
            // 控制自身变成+号
            $(this).removeClass('inactives');
            $(this).removeClass('spread');
            // 控制自身菜单下子菜单隐藏
            $(this).siblings('ul').css('display', 'none');
            // 控制自身子菜单变成+号
            $(this).siblings('ul').children('li').children('ul').parent('li').children('div').addClass('inactives');
            // 控制自身菜单下子菜单隐藏
            $(this).siblings('ul').children('li').children('ul').css('display', 'none');
            // 控制同级菜单只保持一个是展开的（-号显示）
            $(this).siblings('ul').children('li').children('div').removeClass('inactives');
          }
        },
        // 退课需知
        notice: function() {
          location.href = '#notice';
        }
      };

      function bindEvents() {
        $body.on('click', '.typeTab span', events.typeTab);
        $body.on('click', '.index_but .submint', events.submint);
        $body.on('click', '.courselist li>div', events.slide);
        $body.on('click', '.model .installment a', events.selectFirst);
        $body.on('click', '.notice', events.notice);
        $body.on('click', '.Attribute .show_pies > div:first-child > span:last-child', events.favorite);
      }

      // 加载商品详情
      request.detail.then(function(resp) {
        if (resp.code != 200) {
          console.error('加载商品详情出错：', resp);
          return;
        }
        request.detailextend.then(function(data) {
          var productid = data.message.field1;
          services.http.get('kooCourses/' + productid).then(function(courses) {
            request.stageRate.then(function(stageRateData) {
              promiseAll.resolve({
                resp: resp,
                content: data,
                courses: courses,
                stageRateData: stageRateData
              });
            });
          });
        });
      });

      var promiseAll = $.Deferred();
      promiseAll.then(function(data) {
        data.config = config;
        render(data);
        bindEvents();
      });
    },
    /** 
     * 限购专题页
     **/
    purchase: function(activityId) {
      services.setTitle('限购专题页');

      var compiled = _.template(purchaseTpl);
      var $body = $('.viewport .body');

      var request = {
        findActivitySku: services.http.postJson('findActivitySku', {
          activityId: activityId
        })
      };

      function render(data) {
        var rendered = compiled(data);
        $body.off();
        $body.html(rendered);
      }

      var findActivitySkuPromise = request.findActivitySku.then(function(resp) {
        return resp;
      });

      var promiseList = {
        findActivitySku: findActivitySkuPromise
      };
      
      var promiseAll = new services.PromiseAll(promiseList).then(function(data) {
        if (data.findActivitySku.data[0].startTime > new Date().getTime()) {
          ui.makeToast('活动尚未开始,开始时间为: ' + services.formatTime(data.findActivitySku.data[0].startTime));
        }

        data.config = config;
        render(data);
      });
    },
    /** 
     * 退换货原则
     **/
    notice: function() {
      services.setTitle('退换货规则');
      var compiled = _.template(noticeTpl);
      var $body = $('.viewport .body');
      var rendered = compiled();
      $body.off();
      $body.html(rendered);
    }
  };
  return productController;
});
