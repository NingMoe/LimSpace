/**
 * Created by Administrator on 2016/5/24.
 */
define([
    'underscore',
    'zepto',
    'scooch',
    'ui',
    'config',
    '../services',
    '../history-back',
    'text!../templates/lottery/lot_prize.html',
    'text!../templates/lottery/lot_product.html',
    'text!../templates/lottery/trailer.html'
], function(_, $, scooch, ui, config, services, historyBack, prizeTpl,productTpl,trailerTpl) {
    var lotController = {
      prize: function(n) {
        services.setTitle('奖品区');
        var compiled = _.template(prizeTpl);
        var code='prize';
        var level=1;
        var limit=100;
        n= n==6?'0':n;
        var pLe= (n-6)||100;
        services.http.get('tags/skus?code='+code+'&level='+level+'&limit='+limit).then(function(data) {
          var configsys ='http://tyiti.img-cn-beijing.aliyuncs.com' ;
          var prizeId=localStorage.getItem('prizeId');
          if (prizeId) {
            services.http.post('reward/acceptPrize',{id:'prizeId'}).then(function(resp) {
              console.log(resp);
              if(resp.code ==200) {
                localStorage.setItem('prizeId','');
              } else {
                alert('领奖失败：'+resp.message);
              }
            });
          }
          _.each(data.list, function(item) {
            _.each(item.skus, function(items) {
              var id = items.id;
              services.admins.details(id).then(function(resp) {
                items.inventory = resp.sku.inventory ;
                var rendered = compiled({
                  resp:resp,
                  prizeList: data,
                  config:configsys,
                  pLe:pLe
                }); 
                var $body = $('.viewport .body');
                $body.off();
                $body.html(rendered);
              });
            });
          });
        });
      },
      product:function() {
        services.setTitle('淘惠帮农信淘惠日 全场2-5折');
        var compiled = _.template(productTpl);
        var code='712';
        var level=1;
        var limit=100;
        var startDate=new Date();
        var time=new Date();
        var endDate=new Date();
        startDate.setFullYear(2016,6,12);
        startDate.setHours(0,0,0);
        endDate.setFullYear(2016,6,13);
        endDate.setHours(0,0,0);
        var startTime=startDate.getTime();
        var now=time.getTime(); 
        var endtime=endDate.getTime();
        services.http.get('tags/skus?code='+code+'&level='+level+'&limit='+limit).then(function(data) {
          var configsys ='http://tyiti.img-cn-beijing.aliyuncs.com' ;
          var rendered = compiled({
            proList: data,
            config:configsys
          });
          var $body = $('.viewport .body');
          $body.off();
          $body.html(rendered);
          if (now < startTime) {
            $('.prizeItem .s3 a').css('background','#a7a7a7');
            $('.prizeItem .s3 a').attr('href','#lot_product');
            $('.bglog').show();
            $('.start').show();
            $('.dihi').click(function() {
              $('.start').hide();
              $('.bglog').hide();
            });
          }else if (now > endtime) {
            $('.prizeItem .s3 a').css('background','#a7a7a7');
            $('.prizeItem .s3 a').attr('href','#lot_product');
            $('.bglog').show();
            $('.end').show();
            $('.dihi').click(function() {
              $('.end').hide();
              $('.bglog').hide();
            });
          }
        });  
      },
      trailer:function() {
        services.setTitle('');
        var compiled = _.template(trailerTpl);
        var rendered = compiled();
        var $body = $('.viewport .body');
        $body.off();
        $body.html(rendered);    
      }
    };
    return lotController;
});
