define([
  'underscore',
  'zepto',
  '../services',
  'fx',
  'text!../templates/lottery/lottery.html',
  'text!../templates/lottery/succ.html',
  'text!../templates/lottery/prizelist.html'
], function(_, $, services, fx, lotteryTpl, succTpl, prizelistTpl) {
  var lotteryController = {
    lottery: function() {
      var compiled = _.template(lotteryTpl);
      var id = 61;
      var start = 0;
      var prizeList = services.http.get('reward/getPrizeList/'+id);
      var promiseAll = $.Deferred();
      promiseAll.then(function(data) {
        render(data);
      });
      function render(data) {
        var rendered = compiled(data);
        $('.viewport .body').off();
        $('.viewport .body').html(rendered);
        $('title').text('幸运连连转，惊喜享不断');

        var thisulheight = $('.prize ul').height() - 90;
        var ttcss  = '@-webkit-keyframes gogogo {0%{ margin-top: 0;} 100%{ margin-top: -' + thisulheight + 'px}}';
        $('body').append('<style>' + ttcss + '</style>');
        time = parseInt(thisulheight/10);
        $('.prize ul').css({'-webkit-animation':'gogogo ' + time + 's infinite linear'});
        var bfloag = false;
        var events = {
          clilottery: function() {
            if (bfloag) {
              return false;
            }
            bfloag = true;
            services.http.get('reward/lottery').then(function(resp) {
              if (resp.code == 6) {
                 b(9, 0, '活动未开始，活动时间：2016年8月22日10时！','先逛逛');
              } else if (resp.code == 7 || resp.code == 2) {
                 b(10, 0, '主人，你来晚了，活动已结束，请关注后续商城活动！ ','回首页');
              } else if (resp.code == 200) {
                var prizeId = resp.data.raffleId;
                var id = resp.data.id;
                if ( id == 1 ) {
                  b(6, 2880, '哇哦，人品大爆发呦！恭喜您中了免费试吃！','大米马上到你家'); 
                } else if ( id == 2 ) {
                  b(7, 2952, '运气太好了，最高级年卡优惠券已到账！','去使用');
                } else if ( id == 3 ) {
                  b(8, 3024, '终于等到你，半年卡优惠券归你支配了！','去使用');
                } else if ( id == 4 ) {
                  b(9, 3096, '主人，季卡优惠券前来报到！','去使用');
                } else if ( id == 5 ) {
                  b(10, 3168, '恭喜您，获得4kg装优惠券！','去使用');
                }
              } else if (resp.code == 8) {
                b(10, 0, '嘿，种大米不易，发福利心疼，给个红包犒劳犒劳吧，支付就抽奖哦！','1元支付');
              } else if (resp.code == 9) {
                b(11, 0, '您已参加过抽奖，请关注后续商城活动！','回首页');
              } else if (resp.code == 401) {
                var url = location.href;
                location.href = '#connects?fwd=' + encodeURIComponent(url);
              } else {
                alert(resp.message);
              }
            	function b(b, a, d, m) {
		            console.log(a);
		            if (a > 0) {
		              var timer = 5E3;
		            } else {
		              var timer = 0;
		            }
                $('#lotteryBtn').animate({'rotate':'0deg'},1,'ease',function() {
                  $('#lotteryBtn').animate({'rotate':a + 'deg'},timer,'ease',function() {
                    console.log(d);
                    $('body').append('<div class="bglog"></div>');
                    if (m == '') {
                    $('body').append('<div class="dilog"><span class="dihi"></span><p>' + d + '</p></div>');
                    } else {
                    $('body').append('<div class="dilog"><span class="dihi"></span><p>' + d + '</p><button class="prompt">'+ m+ '</button></div>');   
                    }
                    prizemessage(m,b,prizeId);
                  });
                });   
		          }
              function prizemessage(m,b,prizeId) {
		            $('.dihi').click(function() {
		              $('.dilog').hide();
		              $('.bglog').hide();
		              bfloag = false;
		            });
		            $('.prompt').click(function() { 
                  bfloag = false;
		              if (m == '回首页') {
                    location.href = '/?#home/';
                    $('.dilog').hide();
                    $('.bglog').hide();
		              } else if (m == '先逛逛' || m == '去使用') {
		                location.href = 'https://cebshop.tyiti.com/topic//2016/08/12/wozhidaodami/';
		                $('.dilog').hide();
		                $('.bglog').hide();
		              } else if (m == '1元支付') {
		                location.href = '/?#order/create/604?m=1&n=1';
		                $('.dilog').hide();
		                $('.bglog').hide();
		              }
		            });
		          }              
            });
          }
        };
        $('.viewport .body').on('click', '.lotteryBtn', events.clilottery);
      }
      prizeList.then(function(prizeList) {
        var date = {
          lastCount: 3
        };
        promiseAll.resolve({
          resp: date,
          prizeList: prizeList
        });
      });
    },
    prizelist: function() {
      var id = 61;
      var compiled = _.template(prizelistTpl);
      var configsys = "http://tyiti.img-cn-beijing.aliyuncs.com";
      services.http.get('reward/getMyPrizeList/'+id).then(function(resp) {
        _.each(resp.data, function(item) {
          item.createTime = services.formatTime(item.createTime);
        });
        resp.config = configsys;
        console.log(resp);
        var rendered = compiled(resp);
        $('.viewport .body').off();
        $('.viewport .body').html(rendered);
        $('title').text('我的中奖记录');
        if (resp.data.length <= 0) {
          $('.prizelist').html('<P style="text-align:center;display:block;margin:0 auto;margin-top:20px;font-size:14px;">您暂无中奖记录！</p>');
        }
      }).fail(function(jqXHR, textStatus, errorThrown) {
        if (jqXHR.status == 401) {
          if (localStorage.getItem('newfwd')) {
            localStorage.removeItem('newfwd');
            location.href = '#lottery';
          } else {
            var url = location.href;
            location.href = '#connects?fwd=' + encodeURIComponent(url);
          }
        } else {
          ui.makeToast('登录异常，响应码为：' + jqXHR.status + '，原因：' + errorThrown);
        }
      });
    }
  };
  return lotteryController;
});
