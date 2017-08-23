define([
  'underscore',
  'zepto',
  '../services',
  'text!../templates/spike/spike.html'
], function(_, $, services, spikeTpl) {
  var SpikeController = {
    spike:function(){
      document.title = '秒杀首页';
      var $body = $('.viewport .body');
      var compiled = _.template(spikeTpl);
      var rushBuy = services.spike.rushBuy(); 
      var thisindex;
      loading();
      
      var events = {
        navclick:function(){
          thisindex =$(this).index();
          var id = $(this).data('id');
          var start = $(this).data('start');
          var end = $(this).data('end');
          var thisclick = true;
          loading(id,start,end,thisclick);
        }
      }

      function render(data){
        var rendered = compiled(data);
        $body.off();
        $body.html(rendered); 
        $body.on('click', '.nav a', events.navclick);
        $('.nav a').eq(0).addClass('state');
        for(var i=0;i<$('.nav a').length;i++){
          if($('.nav a').eq(i).data('start') <= $('.nav a').eq(i).data('currenttime') && $('.nav a').eq(i).data('currenttime') < $('.nav a').eq(i).data('end')){
            $('.nav a').eq(i).addClass('state').siblings().removeClass('state');
          }
        }
        $('.nav a').eq(thisindex).addClass('state').siblings().removeClass('state');         
      }

      function loading (id,start,end,thisclick){
        rushBuy.then(function(rushbuy){
          if(rushbuy.code !=200 ){
            alert(rushbuy.message);
            return;
          }else if(rushbuy.data.length == 0){
            alert('数据异常，请稍后再试！');
            return;
          }
          if(!id){
            id = rushbuy.data[0].id;
            start = rushbuy.data[0].startTime;
            end = rushbuy.data[0].endTime;
          }

          if(!thisclick){
            _.each(rushbuy.data,function(item){
              if(item.startTime <= rushbuy.currentTime && rushbuy.currentTime < item.endTime){
                id = item.id;
                start = item.startTime;
                end = item.endTime;
              }
            });
          }
          services.spike.rushBuySku(id,start,end).then(function(rushbuysku){
            if(rushbuysku.code == 404 ){
              alert(rushbuysku.message)
              render({ 
                rushbuy: rushbuy,
                rushbuysku: []
              });
              return;
            }
            render({ 
              rushbuy: rushbuy,
              rushbuysku: rushbuysku
            });
          });
        });  
      }

    }
  };

  return SpikeController;
});
