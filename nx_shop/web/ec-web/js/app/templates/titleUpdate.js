function titleUpdate(title){
  var body = document.getElementsByTagName('body')[0];
    document.title = title;
    var iframe = document.createElement("iframe");
    iframe.setAttribute("src", "loading.png");
    iframe.addEventListener('load', function() {
      setTimeout(function() {
        iframe.removeEventListener('load');
          document.body.removeChild(iframe);
        }, 0);
    });
    document.body.appendChild(iframe);
}
return titleUpdate;