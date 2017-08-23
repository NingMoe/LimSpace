# 拼写
* 单词基本上使用完整单词

# HTML格式
* 缩进：2个空格
* 命名：id/class this-is-id 全小写、- 分隔，和 Bootstrap 一致，如 glyphicon glyphicon-exclamation-sign
* id/class 语义化，不建议: red one two
* 缩写
  按钮 btn
  容器 container
* 链接都用 a {display: block;} ，不用 <other onclick="">
* 所有图片都用 background ，不用 img
  background: url(/img/sanj1.jpg) no-repeat center left;
  background-size: 16px 18px;
* <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" /> 最小支持 320px 

# JS
* 缩进：2个空格
变量统一在顶部定义，多个变量的时候
      var deleting = false;
      var events;
* 空行，{}前后不需要
* 模块化
* 不要直接输出HTML、CSS，用模板、addClass/removeClass 代替
* 字符串''

# 术语表
省 state
市 city
县 county
订单 order
退货 return
抽奖 lottery

/////////////////////////////////////////////////////////////////////////////////////////////////

页头：header  如：#header{属性:属性值;}或.header{属性:属性值;},也许你需要了解class与id区别及用法
登录条：loginBar         标志：logo                侧栏：sideBar
广告：banner              导航：nav                 子导航：subNav
菜单：menu               子菜单：subMenu      搜索：search
滚动：scroll               页面主体：main         内容：content
标签页：tab                 文章列表：list            提示信息：msg
小技巧：tips                栏目标题：title            加入：joinus
指南：guild                服务：service              热点：hot
新闻：news                 下载：download         注册：regsiter
状态：status                按钮：btn                    投票：vote
合作伙伴：partner       友情链接：friendLink   页脚：footer
版权：copyRight
常用配合标签div、h1、h2、h3、h4、span、em、b、strong、font、u

1.CSS的 ID 的命名 也许你需要了解class与Id区别

外　套：wrap              主导航：mainNav        子导航：subnav
页　脚：footer             整个页面：content      页　眉：header
版    权：copyRight      商　标：label              标　题：title
主导航：mainNav(globalNav)                  顶导航：topnav
边导航：sidebar           左导航：leftsideBar   右导航：rightsideBar
旗　志：logo                标　语：banner         菜单内容1： menu1Content
菜单容量：　menuContainer                  子菜单：　　submenu
边导航图标：sidebarIcon                        注释：　　　note
面包屑：breadCrumb(即页面所处位置导航提示)
容器：container      内容：content          搜索：search
登陆：login             功能区：　　shop(如购物车，收银台)
当前：current
DIV+CSS命名小结：无论是使用“.”（小写句号）选择符号开头命名，还是使用“#”(井号)选择符号开头命名都无所谓，但我们最好遵循，主要的、重要的、特殊的、最外层的盒子用“#”
(井号)选择符号开头命名，
其它都用“.”（小写句号）选择符号开头命名，同时考虑命名的CSS选择器在HTML中重复使用调用。

通常我们最常用主要命名有：wrap（外套、最外层）、header（页眉、头部）、nav(导航条)、menu(菜单)、title(栏目标题、一般配合h1h2h3h4标签使用)
、content (内容区)、footer(页脚、底部)、logo（标志、可以配合h1标签使用）、banner（广告条，一般在顶部）、copyRight（版权）。其它可根据自己需要选择性使用。

DIVCSS5建议：主要的、重要的、最外层的盒子用“#”(井号)选择符号开头命名，其它都用“.”（小写句号）选择符号开头命名。
2.CSS样式文件命名如下

主要的 master.css
布局，版面 layout.css
专栏 columns.css
文字 font.css
打印样式 print.css
主题 themes.css

也许你需要了解一下css引用到html方法

以上为DIV+CSS的命名规则总结，相信通过规范的CSS命名给你以后网站网页的维护带来方便。