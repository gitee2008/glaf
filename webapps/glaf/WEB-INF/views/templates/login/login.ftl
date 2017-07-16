<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>登录</title>
<link rel="shortcut icon" href="${request.contextPath}/static/html/login/img/jadp.ico">
<link rel="stylesheet" href="${request.contextPath}/static/html/login/css/style.css">
<script type="text/javascript">
	
</script>
</head>
<body class="p-login p-login-vip188" id="pagebody">
	<div class="g-hd">
		<div class="m-hd">
			<span class="u-logo" style="overflow:hidden;background: url('${contextPath}/static/images/logo.png') no-repeat;"></span>
			<p class="nav">
				 <a href="#">产品中心</a> 
				 <a href="#">科技研发</a> 
				 <a href="${request.contextPath}/register" class="serviceIcon">新用户注册</a>
			</p>
		</div>
	</div>
	<div class="g-scroll">
		<div class="g-loginbox">
			<div class="g-bd">
				<div class="m-loginbg">
					<img id="bg" draggable="false"
						src="${request.contextPath}/static/html/login/img/bg4.jpg"
						style="margin-left: 0px; margin-top: -50px;"  width="100%">
				</div>
				<div class="m-bgwrap"></div>
				<div class="m-loginboxbg"></div>
				<div class="m-loginbox">
					<div id="pervent" class="m-photoframe" title="更换防伪图片" hidefocus=""
						style="display: none;">
						<img class="perImg" src="about:blank" alt="">
					</div>
					<div class="lbinner" id="mailbox">
						<form  id="iForm" name="iForm" method="post" action="">
						    <input type="hidden" id="y" name="y">
						    <input type="hidden" id="x_y" name="x_y">
							<input type="hidden" id="x_z" name="x_z">
							<div class="line1 f-cb">
								<span class="domain"><img
									src="${request.contextPath}/static/html/login/img/user.png" width="16"
									style="vertical-align: middle;" />&nbsp;&nbsp;用户名</span> <input
									type="text" name="x" class="ipt ipt-user" id="x"
									autocomplete="off" value="" maxlength="40" style=""
									onclick="hiddenUserWarn()" onblur="validUser()"> <input
									type="text" class="ipt ipt-user ipt-replace" id="replaceun"
									autocomplete="off" value="用户名" maxlength="40"
									style="display: none;">
							</div>
							<div class="line1 f-cb">
								<span class="domain"><img
									src="${request.contextPath}/static/html/login/img/lock.png" width="16"
									style="vertical-align: middle;" />&nbsp;&nbsp;密&nbsp;&nbsp;码</span> <input
									type="password" name="yy" class="ipt ipt-user" id="yy"
									autocomplete="off" value="" maxlength="40" style=""
									onclick="hiddenPswWarn()" onblur="validPsw()"> <input
									type="password" class="ipt ipt-pwd" id="replacepw"
									autocomplete="off" value="" name="replacepw" maxlength="40"
									style="display:none;">
							</div>
							<div class="line3 f-cb">
								 
							</div>
							<div class="line4">
								<a class="u-loginbtn" id="loginBt" href="#"
									onclick="return false;">登 录</a>
							</div>
						</form>
					</div>
					<div class="m-popup m-popup-warn m-popup-warn-block">
						<div class="inner">
							<div class="tt">
								<span class="u-ico u-ico-warn"></span>帐号或密码错误
							</div>
							<div class="ct">
								<p>提示：</p>
								<p>1. 请检查帐号拼写，是否输入有误</p>
								<p>
									2. 若帐号长期未登录，可能已被注销，请<a 
										href="#"
										id="reglink">重新注册</a>
								</p>
								<p>
									3. 若您忘记密码，请<a 
										href="#">找回密码</a>
								</p>
								<p>
									4. 若您需要锁定此帐号，请<a 
										href="#">点击这里</a>
								</p>
							</div>
						</div>
						<div class="arrow"></div>
					</div>
					<div class="m-popup m-popup-warn m-popup-warn-username">
						<div class="inner">
							<div class="tt">
								<span class="u-ico u-ico-warn"></span>帐号输入错误，请重新输入
							</div>
						</div>
						<div class="arrow"></div>
					</div>
					<div class="m-popup m-popup-warn m-popup-warn-password">
						<div class="inner">
							<div class="tt">
								<span class="u-ico u-ico-warn"></span>请输入密码
							</div>
						</div>
						<div class="arrow"></div>
					</div>
				</div>
			</div>
			<a href=".g-more" class="u-jumpNext J_jump" data-jump="1" style="display:none">
				 
			</a>
		</div>
			</div>
			
			<div class="g-ft1" style="position: absolute; bottom: 0px;z-index: 9999;width: 100%;">
				<div class="m-bd">
				   
					<div class="m-flink" style="margin-bottom: -30px;">
						<a  href="#"></a>&nbsp;&nbsp;
						<a  href="#"></a>&nbsp;&nbsp;
						<a  href="#"></a>&nbsp;&nbsp;
						<a 
							href="#"></a>&nbsp;&nbsp;
						<a  href="#"
							style="position: relative;display:inline-block"><i
							class="u-new"
							style="position: absolute;display;block;width:20px;height:13px;top:-12px;right:-13px;background:url(${request.contextPath}/static/html/login/img/new.png) no-repeat;"></i></a><br>
						<a  href="#"></a>&nbsp;&nbsp;
						 
					</div>
				</div>
			</div>
		</div>
	<!--</div>-->
      
	<a href=".g-scroll" class="J_jump J_top u-top" style="display: none;">
		<img src="${request.contextPath}/static/html/login/img/nav.png" class="J_lazy"> <i
		class="cover">返 回<br>登 录
	</i>
	</a>
  <script type="text/javascript" src="${request.contextPath}/static/scripts/jquery.min.js"></script>
  <script type="text/javascript" src="${request.contextPath}/static/scripts/jquery.form.js"></script>
  <script type="text/javascript" src="${request.contextPath}/static/scripts/map.js"></script>
  <script type="text/javascript" src="${request.contextPath}/static/html/login/spread.js"></script>
  <script type="text/javascript">
	   var map = new Map();
	   var contextPath="${request.contextPath}";
  </script>
  <script type="text/javascript">
		//向上滚动效果 
		$(".u-top").click(function() {
			var href = $(this).attr("href");
			var pos = $(href).offset().top;
			$(href).animate({
				scrollTop : pos - 50
			}, 1000);
			return false;
		});
		//向下滚动效果
		$(".u-jumpNext").click(function() {
			var href = $(this).attr("href");
			var pos = $(href).offset().top;
			$(".g-scroll").animate({
				scrollTop : pos + 50
			}, 1000);
			return false;
		});
		function hiddenUserWarn() {
			$(".m-popup-warn-username").removeClass("m-popup-warn-show");
			$(".m-popup-warn-block").removeClass("m-popup-warn-show");
		}
		function validUser() {
			if ($("#x").val() == "")
				$(".m-popup-warn-username").addClass("m-popup-warn-show");
		}
		function hiddenPswWarn() {
			$(".m-popup-warn-password").removeClass("m-popup-warn-show");
			$(".m-popup-warn-block").removeClass("m-popup-warn-show");
		}
		function validPsw() {
			if ($("#yy").val() == "")
				$(".m-popup-warn-password").addClass("m-popup-warn-show");
		}
		
	</script>
    <script type="text/javascript" src="${request.contextPath}/static/scripts/login.js"></script> 
</body>
</html>