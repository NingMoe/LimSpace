define([
  'underscore',
  'zepto',
  'scooch',
  'ui',
  'config',
  '../services',
  '../history-back',
  'text!../templates/home/searchList.html'
], function(_, $, scooch, ui, config, services, historyBack, searchListTpl) {
  var searchListController = {
    /* 列表页*/
    searchlist: function(queryText) {
      services.setTitle('搜索列表页');
      var compiled = _.template(searchListTpl);
      var $body = $('.viewport .body');
	  var params = {};
	  var stageRateData = {};
	  var flag = true;
	  var direction = 'jiage';
	  params['querytext'] = queryText;
	  params['sortfield'] = 'id';
	  params['sorttype'] = 'asc';
	  params['isfirstquery'] = 'false';
      var stageRatePromise = services.http.get('stageRate','');
	  var events = {
		colligate: function() {
		  params['sortfield'] = 'id';
		  params['sorttype'] = 'asc';
		  direction = 'jiage';
		  flag = true;
		  render(params,direction);
		},			
		price: function() {
		  if(flag) {
			params['sortfield'] = 'price';
			params['sorttype'] = 'asc';
			flag = false;
			direction = 'Lleft';
			render(params,direction);
		  } else {
			params['sortfield'] = 'price';
			params['sorttype'] = 'desc';
			flag = true;
			direction = 'Lright';
			render(params,direction);
		  }
		},			
		newlist: function() {
		  params['sortfield'] = 'create_time';
		  params['sorttype'] = 'desc';
		  direction = 'jiage';
		  flag = true;
		  render(params,direction);
		}
	  };
	  function render(params,direction) {
		services.http.get('skus/querytext='+params.querytext+'&sortfield='+params.sortfield+'&sorttype='+params.sorttype+'&isfirstquery='+params.isfirstquery).then(function(data) {
		  console.log(data);
		  if (data.code == 404) {
		    ui.makeToast(data.messsge);	  
		  } else {
		    stageRatePromise.then(function(resp) {
		      $.each(resp,function(index,item) {
				stageRateData[index] = item ;
			  });
		      var configsys = 'http://tyiti.img-cn-beijing.aliyuncs.com';
		      var rendered = compiled({
		        list: data,
		        config: configsys,
		        stageRateData: stageRateData
		      });
		      var $body = $('.viewport .body');      
		      $body.off();
		      $body.html(rendered);      
		      $body.on('click', '.l-list-head .sortI', events.colligate);
		      $body.on('click', '.l-list-head .sortP', events.price);
		      $body.on('click', '.l-list-head .sortN', events.newlist);
		      $('.viewport').addClass('listWrap');
		      $('.body .l-list-head .sortP').css({'background':'url(../../../img/'+direction+'.png) no-repeat 48% 3px','background-size':'15px 15px'});
		      $('.m-scooch').scooch({
		        infinite: true,
		        autoplay: {
			      interval: 4000,
			      cancelOnInteraction: true
		        }
		      });
			  if (config.sys == 'thb') {
			    $('.home_box  .states').addClass('hidden');
			    $('.home_box  .statesthb').removeClass('hidden');
			  }
			  if(params['sortfield'] == 'id') {
			  	$('.l-list-head .sortI').addClass('change');
			  	$('.l-list-head .sortP,.l-list-head .sortN').removeClass('change');
			  }else if(params['sortfield'] == 'price') {
			  	$('.l-list-head .sortP').addClass('change');
			  	$('.l-list-head .sortI,.l-list-head .sortN').removeClass('change');
			  }else {
			  	$('.l-list-head .sortN').addClass('change');
			  	$('.l-list-head .sortP,.l-list-head .sortI').removeClass('change');
			  }
		    });
		  }
		});
	  }
	  render(params,direction);
    }
  };
  return searchListController;
});