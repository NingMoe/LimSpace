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
    'text!../templates/pickup/index.html',
    'text!../templates/pickup/pickupshoplist.html'
], function(_, $, scooch, ui, config, services, historyBack, indexTpl,pickupshoplistTpl) {
    var pickupController = {
      pickupaddress: function() {
        // 从 编辑页 后退来的，继续后退到 订单确认页
        if (historyBack.target) {
          if (historyBack.target === 'pickupaddress') {
            historyBack.target = 'confirm';
            history.back();
            return;
          } else {
            historyBack.reset();
          }
        }
        services.setTitle('选择自提点');
        var compiled = _.template(pickupshoplistTpl);
        services.http.get('pickupaddress').then(function(resp) {
          var rendered = compiled(resp); 
          var $body = $('.viewport .body');
          $body.off();
          $body.html(rendered);
          $('.viewport .body').on('click', '.pickuplist ul li', events.creataddress);
        }); 
        var events = {
          creataddress: function() {
            console.log(3);
            historyBack.target = 'confirm';
            historyBack.data = {
              pickupaddressId: $(this).data('id'),
            };
            history.back();
          }
        };
      }
    };
    return pickupController;
});