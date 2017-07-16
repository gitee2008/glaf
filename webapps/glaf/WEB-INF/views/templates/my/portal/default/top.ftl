<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>top</title>
<link rel="stylesheet" type="text/css" href="${contextPath}/static/html/home/${homeTheme}/css/style.css"  />
<script type="text/javascript" src="${contextPath}/static/scripts/jquery.min.js"></script>
<script type="text/javascript">
	$(function(){	
		//顶部导航切换
		$(".nav li a").click(function(){
			$(".nav li a.selected").removeClass("selected")
			$(this).addClass("selected");
		})	
	})

	function goHome() {
		self.parent.rightFrame.$("#home").trigger('click');
	}
</script>
</head>
<body
	style="background:url(${contextPath}/static/html/home/${homeTheme}/images/topbg.gif) repeat-x;">
	<div class="topleft">
		<a href="${contextPath}/my/main" target="_parent"><img
			src="${contextPath}/static/images/index_logo.png" title="系统首页" /></a>
	</div>
	<!--<ul class="nav">
    
    </ul>  -->
    <div class="user">
			<span>${username}，欢迎您！</span> 
	</div>
	<div class="systitle"> ${sys_title} </div>
	<div class="topright">
		<ul>
		    <!-- <li><span><img
					src="${contextPath}/static/html/home/${homeTheme}/images/palette.png" title="切换主题"
					/></span><a class="bounceIn dialog" href="#" onclick="javascript:openThemeWin();">切换主题</a></li> --> 
			<li><span><img
					src="${contextPath}/static/html/home/${homeTheme}/images/help.png" title="帮助"
					class="helpimg" /></span><a href="#">帮助</a></li>
			<li><a href="${contextPath}/login/logout" target="_parent">退出</a></li>
		</ul>
	</div>
	<script type="text/javascript">
	  function openThemeWin(){
		 parent.parent.openThemeDialog();
	}
</script>
</body>
</html>

