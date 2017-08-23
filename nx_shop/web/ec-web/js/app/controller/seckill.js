define([
  'underscore',
  'zepto',
  '../services',
  'ui',
  'config',
  'swiper',
  'text!../templates/product/SecKillList.html'
], function(_, $, services,ui,config,swiper,SeckillListTpl) {
  var first = '';
  var second = ''; 
  var SeckillController = {
	  //	秒杀专题页
	  seckillList: function(id,callback){
      services.setTitle('秒杀');
		  var $body = $('.viewport .body');
      var compiled = _.template(SeckillListTpl);
      var SCREEN_WIDTH = document.body.offsetWidth;
      var bannerimg = services.http.get('adverts', {
        group: 'activity',
        code: 'seckill'
      });
      function paddingZero(num) {
        return num >= 10 ? num : '0' + num;
      }
      function formatTime(time) {
        if (!time) {
          return '';
        }
        var date = new Date(parseInt(time));
        var result = paddingZero(date.getHours()) + ':' + paddingZero(date.getMinutes());
        return result;
      }
      /**
       * 当前应该默认显示的秒杀
       * @return {Int} 从1开始计数的数组下标
       */
      function findCurrent(flashSaleList) {
        var x = 0;
        var y = 0;
        var z = 0;
        // 找到需要默认显示的那个
        $.each(flashSaleList, function(index, item) {
          // 进行中
          if (item.activity_status == 1 && !x) {
            x = index + 1;
          }
          // 未开始
          if (item.activity_status == 0 && !y) {
            y = index + 1;
          }
          // 已结束
          if (item.activity_status == 2 && !z) {
            z = index + 1;
          }
        });
        return x || y || z;
      }
      /**
       * 倒计时
       */
      clearTimeout(window.countDownTimer);
      function countDown(flashSale) {
        var deltaSeconds;
        // 未开始
        if (flashSale.activity_status == 0) {
          deltaSeconds = flashSale.start_time - flashSale.now_time;
        } else {
          deltaSeconds = flashSale.end_time - flashSale.now_time;
        }
        flashSale.now_time += 1000;
        if (deltaSeconds <= 0) {
          // 重新渲染页面
          setTimeout(function() {
            init(flashSale.index);
          }, 1000);
          return;
        }
        var second = Math.floor((deltaSeconds / 1000) % 3600 % 60);
        var minute = Math.floor((deltaSeconds / 60000) % 60);
        var hour = Math.floor((deltaSeconds / 3600000));
        $('.surplus .seckill-timer .hour').text(paddingZero(hour));
        $('.surplus .seckill-timer .min').text(paddingZero(minute));
        $('.surplus .seckill-timer .second').text(paddingZero(second));
        clearTimeout(window.countDownTimer);
        window.countDownTimer = setTimeout(function() {
          countDown(flashSale);
        }, 1000);
      }
      var events = {
        activeclick: function() {
          var id = $(this).find('a').data('id');
          // 点击的是前面和末尾的空白格子
          if (!id) {
            return;
          }
          var index = $(this).data('index');
          init(index);
        },
        // 购买事件
        buy: function() {
          location.href = '#product/' + $(this).data('skuid');
        },
        // 进入商品详情页面事件
        toDetail: function() {
          location.href = '#product/' + $(this).data('skuid');
        }
      };
      function render(data) {
        bannerimg.then(function(resp) {
          data.banner = resp.data.seckill[0] ? resp.data.seckill[0].picUrl : '';
        });
        var rendered = compiled(data);
        $body.off();
        $body.html(rendered);
        // 初始化可 x 轴滚动的秒杀列表样式
        var gridWidth = SCREEN_WIDTH / 5;
        $('div.seckill .nav ul li').css({
          width: SCREEN_WIDTH / 5
        });
        $('div.seckill .nav ul').css({
          width: (data.findActivity.data.length + 4) * (SCREEN_WIDTH / 5)
        });
        // 重置 秒杀列表 x 轴滚动的位置
        $('#nav')[0].scrollLeft = gridWidth * (data.index - 1);
        // 处理滚动事件
        var scrolling = false;
        $('#nav').on('scroll', function() {
          scrolling = true;
        });
        $('#nav').on('touchend', function() {
          if (!scrolling) {
            return;
          }
          scrollEnd();
          scrolling = false;
        });
        function scrollEnd() {
          var nav = $('#nav')[0];
          var index = Math.round(nav.scrollLeft / gridWidth) + 1;
          // 滚动了一点点，或者滚来滚去还是高亮同一个，那么只需要挪一点点位移，使得高亮一个完整的格子就行
          if (index == data.index) {
            nav.scrollLeft = Math.round(nav.scrollLeft / gridWidth) * gridWidth;
            return;
          }
          events.activeclick.apply($('#nav li[data-index="' + index + '"]'));
        }
        $body.on('click', '.nav ul .clickLi', events.activeclick);
        $body.on('click', '.shopp li .stock button', events.buy);
        $body.on('click', '.seckill .shopp li', events.toDetail);
      }
      // 入口函数，调用接口获取数据，然后调用 render 渲染
      function init(index) {
        // 获取秒杀列表
        services.http.get('findActivity/' + 1).then(function(findActivity) {
          // 当前活动秒杀的数组下标
          index = index || findCurrent(findActivity.data);
          // 当前的秒杀
          var flashSale = findActivity.data[index - 1];
          flashSale.index = index;
          // 获取当前秒杀的 SKU 列表
          services.http.postJson('findActivitySku/', {
            activityId: flashSale.id
          }).then(function(findActivitySku) {
            render({
              findActivitySku: findActivitySku,
              index: index, // 当前秒杀的下标，从 1 开始计数
              flashSale: flashSale, // 当前秒杀
              findActivity: findActivity,
              imgHost: config.imgHost,
              formatTime: formatTime
            });
            // 未开始、进行中
            if (flashSale.activity_status != 2) {
              countDown(flashSale);
            }
          });
        });
      }
  	  init();
  	},
  };
 	return SeckillController; 
});