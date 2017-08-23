define([
  'underscore',
  'zepto',
  '../services',
  'config',
  'text!../templates/category/category.html',
  'text!../templates/category/categorylist.html'
], function(_, $, services, config, categoryTpl, categorylistTpl) {
  var categoryController = {
    /* 分类*/
    category: function() {
      services.setTitle('分类');
      var $body = $('.viewport .body');
      var compiled = _.template(categoryTpl);
      var code = 'cat';
      var level = 3;
      var limit = 1;
      var category = services.http.get('tags/skus?code='+code+'&level='+level+'&limit='+limit);
      var bannerCat = {};
      var code = '';
      var events = {
        listLeft: function() {
          var left = $('.listLeft').scrollTop();
          var id = $(this).data('id');
          render(id,$(this).index());
          $('.listLeft').scrollTop(left);
        }
      };
      render();
      function render(id,index) {
        category.then(function(resp) {
          if (!id) {
            id = resp.list[0].id;
          }
          category.then(function(idList) {
            _.each(idList.list,function(item) {
              if (item.id == id) {
              	code = 'cat' + item.id;
                idList = item ;
                var configsys ='http://tyiti.img-cn-beijing.aliyuncs.com';
                services.http.get('adverts?group='+' '+'&code='+code).then(function(res) {
									if (res.code == 200) {
										bannerCat = res.data;
									}
	                var rendered = compiled({
	                  resp: resp,
	                  idList: idList,
	                  config: configsys,
	                  bannerCat: bannerCat[code],
	                  configer: config
	                });
	                $body.off();
	                $body.html(rendered);
	                $body.on('click', '.listLeft li ', events.listLeft);
	                $('div.clearfix').css({'height':$(window).height() - 101,'display' : 'block'});
	                $('.listRight ul').css({'clear':'both','display' : 'block'});
	                window.onscroll = function() {
	                  console.log($(window).height());
	                };
	                if (!index) {
	                  $('.listLeft li').eq(0).addClass('active');
	                  $('.listRight>div').eq(0).css('display','block');
	                  
	                } else {
	                  $('.listLeft li').eq(index).addClass('active');
	                  $('.listRight>div').eq(index).css('display','block');
	                }
	                if (config.sys === 'tyfq') {
	                  $('.pj-action-add > a.credit').show();
	                  $('.pj-action-add > a').css({'width':'25%'});
	                }
	              });
              }
            });
          });
        });
      }
    },
    /* 分类列表*/
    categorylist: function(id) {
      services.setTitle('商品列表页');
      var $body = $('.viewport .body');
      var compiled = _.template(categorylistTpl);
      services.http.get('tags/'+id+'/skus').then(function(resp) {
        var configsys = 'http://tyiti.img-cn-beijing.aliyuncs.com' ;
        var rendered = compiled({
          resp: resp,
          config: configsys
        });
        $body.off();
        $body.html(rendered);
        if (config.sys === 'thb') {
          $('.home_box  .states').addClass('hidden');
          $('.home_box  .statesthb').removeClass('hidden');
          // $('.header a').css('background-image','url(./img/thjiantou@2x.png)');
          // $('.header h2').css('background-image','url(./img/lthouce@2x.png)');
        }
      });
    }
  };
  return categoryController;
});
