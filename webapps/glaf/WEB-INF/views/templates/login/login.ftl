<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>登录</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <!-- Bootstrap 3.3.7 -->
  <link rel="stylesheet" href="${request.contextPath}/static/AdminLTE/bower_components/bootstrap/dist/css/bootstrap.min.css">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="${request.contextPath}/static/AdminLTE/bower_components/font-awesome/css/font-awesome.min.css">
  <!-- Ionicons -->
  <link rel="stylesheet" href="${request.contextPath}/static/AdminLTE/bower_components/Ionicons/css/ionicons.min.css">
  <!-- Theme style -->
  <link rel="stylesheet" href="${request.contextPath}/static/AdminLTE/dist/css/AdminLTE.min.css">
  <!-- iCheck -->
  <link rel="stylesheet" href="${request.contextPath}/static/AdminLTE/plugins/iCheck/square/blue.css">

  <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->

  <script type="text/javascript" src="${request.contextPath}/static/scripts/jquery.min.js"></script>
  <!-- Bootstrap 3.3.7 -->
  <script src="${request.contextPath}/static/AdminLTE/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
  <!-- iCheck -->
  <script src="${request.contextPath}/static/AdminLTE/plugins/iCheck/icheck.min.js"></script>

  <script type="text/javascript" src="${request.contextPath}/static/scripts/jquery.form.js"></script>
  <script type="text/javascript" src="${request.contextPath}/static/scripts/jsencrypt.min.js"></script>
  <script type="text/javascript" src="${request.contextPath}/static/scripts/map.js"></script>
  <script type="text/javascript">
	var map = new Map();
	var contextPath="${request.contextPath}";
  </script>
</head>
<body class="hold-transition login-page">
<div class="login-box">
  <div class="login-logo">
    <b>GLAF应用开发平台</b>
  </div>
  <!-- /.login-logo -->
  <div class="login-box-body">
    <p class="login-box-msg">请输入您的用户名及密码</p>
    <form id="iForm" name="iForm" action="${request.contextPath}/login/doLogin" method="post">
	  <input type="hidden" id="y" name="y">
	  <input type="hidden" id="x_y" name="x_y">
	  <input type="hidden" id="x_z" name="x_z">
      <div class="form-group has-feedback">
        <input type="text" id="x" name="x" class="form-control" placeholder="用户名">
        <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
      </div>
      <div class="form-group has-feedback">
        <input type="password" id="yy" name="yy" class="form-control" placeholder="密码">
        <span class="glyphicon glyphicon-lock form-control-feedback"></span>
      </div>
      <div class="row">
        <!-- <div class="col-xs-8">
          <div class="checkbox icheck">
            <label>
              <input type="checkbox"> 记住密码
            </label>
          </div>
        </div> -->
        <!-- /.col -->
		<div class="col-xs-8"></div>
        <div class="col-xs-4">
          <button type="submit" id="loginBt" onclick="return false;" class="btn btn-primary btn-block btn-flat">登录</button>
        </div>
        <!-- /.col -->
      </div>
    </form>
    <a href="#">忘记密码</a><br>
	<#if enableAutoReg == true>
    <a href="${request.contextPath}/register" class="text-center">注册</a>
    </#if>
  </div>
  <!-- /.login-box-body -->
</div>
<!-- /.login-box -->
<script type="text/javascript" src="${request.contextPath}/static/scripts/loginV2.js"></script>
</body>
</html>
