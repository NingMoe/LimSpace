define([
  'underscore',
  'zepto',
  'scooch',
  'ui',
  'config',
  '../services',
  '../history-back',
  'text!../templates/home/search.html'
], function(_, $, scooch, ui, config, services, historyBack, searchTpl) {
  var searchController = {
    /* 搜索 */
    searchPage: function() {
      services.setTitle('搜索');
      var compiled = _.template(searchTpl);
      var $body = $('.viewport .body');
      var events = {
		hotSearch:function() {
  		  var querytext = $(this).text();
  		  $('.listhistory input').val(querytext);
  		  searchText();
		},
		histSearch:function() {
		  var querytext = $(this).text();
		  $('.listhistory input').val(querytext);
		  searchText();
		},
		deleteAll:function(){
		  if (confirm('确定要删除吗?')) {
			localStorage.setItem('historySearch','');
			loadAll();
		 	render();
		  }
		},
		searchOne:function() {
		  //点击
		  searchText();
		},
		deletemove:function() {
		  var $this = $(this);
		  var deleting = true;
		  if (event.type == 'swipeLeft') {
		    $this.addClass('lefts').removeClass('rights');
		  } else {
		    $this.addClass('rights').removeClass('lefts');
		  }
		},
		deleteone:function() {
		  var eqs = $(this).parent('li').index();
		  var history = localStorage.getItem('historySearch');
		  history = JSON.parse(history);
		  console.log(history);
		  history = history.splice(eqs,1);
		  history = JSON.stringify(history);
		  localStorage.setItem('historySearch',history);
		  $(this).parent('li').remove();
		}
      };
      function searchText() {
		if ($('.listhistory input').val() === '') {
		  ui.makeToast('请输入正确的关键字');
		} else {
		  var querytext = $('.listhistory input').val();
		  if (querytext.length > 18) {
			ui.makeToast('字符长度超出');
		    return false;
		  } else {
			querytext = querytext.replace(/\;/g,'%3B').replace(/\//g,'%2F').replace(/\,/g,'%2C').replace(/\?/g,'%3F').replace(/\:/g,'%3A').replace(/\@/g,'%40').replace(/\=/g,'%3D').replace(/\+/g,'%2B').replace(/\#/g,'%23').replace(/\&/g,'%26').replace(/\$/g,'%24').replace(/\%/g,'%25');
			querytext = encodeURI($.trim(querytext));
			var params = {};
			params['querytext'] = querytext;
			params['sortfield'] = 'id';
			params['sorttype'] = 'asc';
			params['isfirstquery'] = 'true';
			var direction = 'jiage';
			services.http.get('skus/querytext='+params.querytext+'&sortfield='+params.sortfield+'&sorttype='+params.sorttype+'&isfirstquery='+params.isfirstquery).then(function(data) {
			  save();
			  if (data.code == 404) {
		     	ui.makeToast(data.messsge);
	     	  } else {
	     	  	location.href = '#searchlist/' + querytext;
	     	  }
			});
		  }
		}
      }
      render();
      function render() {
	    services.http.get('popularsearch',{}).then(function(resp) {
	      var hotwords = resp.data;
	      var rendered = compiled({hotwords:hotwords,config:config});
	      $body.off();
	      $body.html(rendered);
	      $('.s-search-wrap input').focus();
	      $('.viewport').removeClass('listWrap');
	      loadAll();
	      //键盘事件
	      $('.listhistory input').on('keydown',function(event) {
		    if (event.keyCode == 13) {
		      searchText();
		    }
		  });
		  $body.on('click', '.search_btn', events.searchOne);
		  $body.on('click', '.s-search-hot .s-search-keywords ul li', events.hotSearch);
		  $body.on('click', '.s-search-history .s-search-words #list li b', events.histSearch);
		  $body.on('click', '.s-search-history .delete', events.deleteAll);
          $body.on('swipeLeft', '#list li', events.deletemove);
          $body.on('swipeRight', '#list li', events.deletemove);
          $body.on('click','#list li .deletes',events.deleteone);
	    });
  	  }
      /* 本地存储搜索历史 */
	  function save(){
	    var contactstr = localStorage.getItem('historySearch') || '[]';
	    var contact = JSON.parse(contactstr);
	    var querytext = '';
	    querytext = $.trim($('.listhistory input').val());
	    for (var i = 0;i < contact.length;i++) {
	      if (querytext == contact[i]) {
	        contact.splice(i,1);
	        i--;
	      }
	    }
	    contact.push(querytext);
	    if (window.localStorage) {
	      if (contact.length < 21) {
		    localStorage.setItem('historySearch',JSON.stringify(contact));
		  } else {
		    contact.splice(0,contact.length - 20);
		    console.log(contact.length);
		    localStorage.setItem('historySearch',JSON.stringify(contact));
		  }
	    }
	    loadAll();
	  }
	  /* 加载搜索历史 */
	  function loadAll(){
	    var list = $('#list');
	    if (localStorage.length > 0) {
          var result;
          var contactstr = localStorage.getItem('historySearch');
          var contacts = JSON.parse(contactstr||'[]');
          $('#list li').remove();
		  if (contacts.length) {
	        for (var i = contacts.length - 1;i >= 0;i--) {
	          result = '<li class="rights"><b>' + contacts[i] + '</b><span class="deletes">删除</span></li>';
	          $('#list').append(result);
	        }
	      }
	    }
	  }
	  if (config.sys === 'thb') {
        $('.home_box  .states').addClass('hidden');
        $('.home_box  .statesthb').removeClass('hidden');
      }
    }
  };
  return searchController;
});