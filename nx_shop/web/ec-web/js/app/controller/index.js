define([
  'underscore',
  'zepto',
  'config',
  '../services'
], function(_, $, config,services) {
  var indexController = {
    index: function() {
      var openId = localStorage.getItem('openId');
      if (!openId) {
        openId = 'wx' + +new Date();
        localStorage.setItem('openId', openId);
      }

      var form = document.createElement('form');
      form.method = 'post';
      form.action = config.napi + 'index';
      var openIdInput = document.createElement('input');
      openIdInput.name = 'openId';
      openIdInput.type = 'hidden';
      openIdInput.value = openId;
      var openUrlInput = document.createElement('input');
      openUrlInput.name = 'openUrl';
      openUrlInput.type = 'hidden';
      openUrlInput.value = location.origin + '/?#home';
      form.appendChild(openIdInput);
      form.appendChild(openUrlInput);

      form.submit();
    }
  };

  return indexController;
});
