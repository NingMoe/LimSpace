define([
  'underscore',
  'zepto',
  '../services',
  'fx',
  'config',
  'text!../templates/favorite/favorite.html'
], function(_, $, services, fx, config, favoriteTpl) {
  var favoriteController = {
    favorite: function() {
      services.setTitle('我的收藏');
      var compiled = _.template(favoriteTpl);
      $('title').text('我的收藏');
      services.http.get('favorite').then(function(resp) {
        if (resp.data) {
          resp.config = 'http://tyiti.img-cn-beijing.aliyuncs.com';
          var rendered = compiled(resp);
          $('.viewport .body').off();
          $('.viewport .body').html(rendered);
          $('body').css({'background':'#fff'});
          if (config.sys === 'thb') {
            $('.home_box  .states').addClass('hidden');
            $('.home_box  .statesthb').removeClass('hidden');
          }
        }
      });
    },
  };
  return favoriteController;
});