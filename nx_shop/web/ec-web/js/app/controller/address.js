define([
  'underscore',
  'zepto',
  '../services',
  'ui',
  'config',
  '../history-back',
  'text!../templates/address/list.html',
  'text!../templates/address/edit.html'
], function(_, $, services, ui, config, historyBack, listTpl, editTpl) {
  var flag;
  var stateIdflag;
  var cityIdflag;
  var countyIdflag;
  var fullTextflag;
  var addressController = {
    /* 地址列表*/
    list: function(selectedId,urlArray) {
      // 从 编辑页 后退来的，继续后退到 订单确认页
      if (historyBack.target) {
        if (historyBack.target === 'address') {
          historyBack.target = 'confirm';
          history.back();
          return;
        } else {
          historyBack.reset();
        }
      }

      var self = this,
      compiled = _.template(listTpl),
      $body = $('.viewport .body');
      services.setTitle('选择收货地址');
      // 是否有处于删除中的条目
      var deleting = false;
      var events = {
        createAddress: function() {
          if($('.add ul').length > 0  && $('.add ul li').length > 0){
            localStorage.setItem('switch', false);
          }else{
            localStorage.setItem('switch', true);
          }
          location.href = '/?#address/edit';
        },
        deletingAddress: function(event) {
          var $this = $(this);
          deleting = true;
          if (event.type == 'swipeLeft') {
            $this.siblings().addClass('delderansform').removeClass('shantransform');
            $this.addClass('shantransform').removeClass('delderansform');
          } else {
            $this.addClass('delderansform').removeClass('shantransform');
          }
        },
        deleteAddress: function() {
          services.http.delete('address/'+$(this).data('id')).then(function(resp) {
            deleting = false;
            services.http.get('address').then(render);
          });
        },
        abortDeletigAddress: function(event) {
          var $lis = $('.add li');
          // event.eventPhase = Event.BUBBLING_PHASE (3)
          if (event.target.innerText === '删除' || !deleting) {
            return true;
          }

          $lis.addClass('delderansform').removeClass('shantransform');
          deleting = true;
          return false;
        },
        editAddress: function() {
          location.href = '/?#address/edit/';
        },
        useAddress: function() {
          historyBack.target = 'confirm';
          historyBack.data = {
            addressId: $(this).data('id'),
          };
          history.back();
        }
      };
      services.http.get('address').then(render);
      function render(resp) {
        resp.selectedId = selectedId;
        var rendered = compiled(resp);
        $body.off();
        // 无法在 $body[0] 上 removeEventListener ，因为每次的 events.abortDeletigAddress 都是新函数
        // $add[0].removeEventListener('click', events.abortDeletigAddress, true);
        $body.html(rendered);

        if (config.sys === 'thb') {
          $('.button').css('background','#f32f30');
        }
        var lastHistory = urlArray[urlArray.length - 2].Hash;
        if (lastHistory.indexOf('order/create')!= -1) {
          $body.on('click', '.add dd', events.useAddress);
        }
        _.each(resp.data, function(item) {
          if (item.flag == 1) {
            flag = item.flag;
            stateIdflag = item.stateId;
            cityIdflag = item.cityId;
            countyIdflag = item.countyId;
            fullTextflag = item.fullText.replace(item.street,'');
          }
        });
        $body.on('click', '.button', events.createAddress);
        $body.on('click', '.delete', events.deleteAddress);
        $body.on('swipeLeft', '.add li', events.deletingAddress);
        $body.on('swipeRight', '.add li', events.deletingAddress);
        // 事件捕获阶段
        // $body.on('click', events.abortDeletigAddress, true); // .on() 不支持指定捕获
        $('.add', $body)[0].addEventListener('click', events.abortDeletigAddress, true); // OK，但是不方便 off()
        // function add(element, events, fn, data, selector, delegator, capture){
        // $.event.add($body[0], 'click', events.abortDeletigAddress, undefined, '.add', undefined, true);
      }
    },
    /* 地址编辑页（新增、修改）*/
    edit: function(id,urlArray) {
      services.setTitle('编辑地址');
      var provinces;
      var regions = [];
      var self = this;
      var compiled = _.template(editTpl);
      var $body = $('.viewport .body');
      var disabledbtn = true;

      document.title = (id ? '编辑' : '添加') + '收货地址';

      var events = {
        /**
         * 开始三级联动
         */
        showRegionSelection: function() {
          // 重置
          $('.addres li').hide();
          $('.addres-cont ul').empty();
          // 初始化一级
          provinces = provinces || services.http.get('area/0&1');
          provinces.then(function(resp) {
            renderRegion(resp.data, 1);
            $('.select-region').show();
            $('.addres-cont').css({'height':$(window).height() - 44});
            $('.addedit').hide();
          });
        },
        /**
         * 三级联动，选中某级行政区域
         */
        regionSelected: function() {
          var $this = $(this),
            id = $(this).data('id'),
            name = $(this).text(),
            level = $this.parent().data('level');

          $this.addClass('active');
          // 更新当前级别的标题为选中行政区的名字
          $('.addres [data-level="' + level + '"]').text(name).removeClass('active');
          $('.addres [data-level="' + level + '"]').attr('data-id',id);
          // 加载下级
            regions = [];
            for (var i = 0;i < $('.addres ul li').length;i++) {
              regions.push({
                id:$('.addres ul li').eq(i).data('id'),
                name:$('.addres ul li').eq(i).text()
              });
            }
          if (level < 3) {
            services.http.get('area/'+id+'&'+(level + 1)).then(function(resp) {
              renderRegion(resp.data, level + 1);
            });
          // 结束
          } else {
            var city = regions[1].name === '市辖区' || regions[1].name === '市辖县' ? '' : regions[1].name;
            var fullText = regions[0].name + city + regions[2].name;
            $('.addresdit i').text(fullText);
            if (localStorage.getItem('PersonalInformation')) {
              location.href='#PersonalInformation';
              localStorage.removeItem('PersonalInformation');
            }
            $('.select-region').hide();
            $('.addedit').show(); 
          }
        },
        /**
         * 三级联动，切换到地级或省级行政区域
         */
        changeRegionLevel: function() {
          var $this = $(this),
            level = $this.data('level');
          if (level == 1) {
            events.showRegionSelection();
          } else if (level < 3) {
            $('.addres li[data-level="3"]').hide();
            var id = regions[level - 2].id;
            services.http.get('area/'+id+'&'+level).then(function(resp) {
              renderRegion(resp.data, level);
            });
          }
        },
        /**
         * 保存地址
         */
        submit: function() {
          if (disabledbtn) {
            disabledbtn = false ;
          } else {
            return;
          }
          setTimeout(function() {
            disabledbtn = true ;
          },3000);
          
          var regut = /^[1][0-9]{10}$/;
          var zipregut = /^[0-9]{6}$/;
          var username = $('[name="username"]').val().replace(/\s+/g, '');
          var mobile = $('[name="mobile"]').val().replace(/\s+/g, '');
          var address = $('.addedit ul li').eq(2).find('i').text();
          var street = $('[name="street"]').val().replace(/\s+/g, '');
          var zip = $('[name="zip"]').val().replace(/\s+/g, '');
          
          if (!username) {
            ui.makeToast('请输入收件人姓名');
            $('.addedit ul li').eq(0).find('input').css({'border-color':'red'});
            return false;
          } else if (!regut.test(mobile)) {
            ui.makeToast('请输入正确的11位手机号');
            $('.addedit ul li').eq(1).find('input').css({'border-color':'red'});
            return false;
          } else if (!address) {
            ui.makeToast('请填写所在地区');
            $('.addedit ul li').eq(2).find('i').css({'border-color':'red'});
            return false;
          } else if (!street ) {
            ui.makeToast('请填写详细地址');
            $('.addedit ul li').eq(3).find('textarea').css({'border-color':'red'});
            return false;
          } else if (!(zipregut.test(zip) || zip == '')) {
            ui.makeToast('请填写正确的邮编');
            $('.addedit ul li').eq(4).find('input').css({'border-color':'red'});
            return false;
          }

          var data = {
            username: $('[name="username"]').val().replace(/\s+/g, ''),
            mobile: $('[name="mobile"]').val(),
            stateId: regions[0].id,
            cityId: regions[1].id,
            countyId: regions[2].id,
            street: $('[name="street"]').val(),
            zip: $('[name="zip"]').val(),
            isDefault: $('[name="isDefault"]')[0].checked ? 1 : 0
          };

          if (id) {
            data.id = id;
          }

          services.address.save(data).then(function(resp) {
            var lastHistory = urlArray[urlArray.length - 2].Hash;
            var last2History = urlArray[urlArray.length - 3].Hash;
            if(lastHistory.indexOf('selectedId') != -1 || last2History.indexOf('order/create') != -1) {
              historyBack.target = 'address';
              historyBack.data = {
                addressId: resp.data.id,
              };
            }
            history.back();
          });
        },
        focusclick: function() {
          $(this).css({'border':'1px solid #b9b9b9'});
        }
      };

      function renderRegion(data, level) {
        var html = '';
        _.each(data, function(region) {
          html += '<li data-id=' + region.id + '>' + region.name + '</li>';
        });

        $('.addres li').removeClass('active');
        $('.addres [data-level="' + level + '"]').text('请选择').addClass('active').show();
        $('.addres-cont > div').attr('data-active-level', level);
        $('.addres-cont div [data-level="' + level + '"]').html(html);
        $('.addres-cont').css({'height': $(window).height() - 44});
        $('.addres-cont > div').css({'height': $(window).height() - 44});
        $('.addres-cont div [data-level="' + level + '"]').css({'height': data.length * 35});
        console.log('计算地址列表高度:'+ data.length * 35 + 'px');
        if (data.length * 35  <= $(window).height() - 44) {
          $('.addres-cont > div').css({'overflow': 'inherit'});
        } else {
          $('.addres-cont > div').css({'overflow': 'auto'});
        }
        $('.addres-cont div').scrollTop(0);
      }

      var promise;
      if (id) {
        promise = services.http.get('address/'+id);
      } else {
        promise = new $.Deferred();
        promise.resolve(null);
      }

      promise.then(function(resp) {
        if (resp) {
          regions = [
            {id: resp.data.stateId},
            {id: resp.data.cityId},
            {id: resp.data.countyId}
          ];
        } else {
          resp = {
            data: {}
          };
        }
        if (flag == 1) {
          regions = [
            {id: stateIdflag},
            {id: cityIdflag},
            {id: countyIdflag}
          ];
        } 
        rendered = compiled(resp);
        $body.off();
        $body.html(rendered);
        if (localStorage.getItem('switch') == 'true') {
          $('.checkboxThree input').attr('checked','checked')
        }
        if (config.sys === 'thb') {
          $('.button').css('background','#f32f30');
        }
        if (flag == 1) {
          $('.addresdit i').text(fullTextflag);
        }
        if (!(resp.data.flag == 1 || flag == 1)) {
          $body.on('click', '.addresdit', events.showRegionSelection);
          $body.on('click', '.addres-cont li', events.regionSelected);
          $body.on('click', '.addres li', events.changeRegionLevel);
        }
        $body.on('click', '.button', events.submit);
        $('.viewport .body').on('focus', '.addedit input', events.focusclick);
        $('.viewport .body').on('click', '.addedit .addresdit i', events.focusclick);
        $('.viewport .body').on('focus', '.addedit .boxfive textarea', events.focusclick);
      });
    }
  };
  return addressController;
});
