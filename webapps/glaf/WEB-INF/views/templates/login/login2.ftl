<!doctype html>
<html lang="en">
 <head>
  <meta charset="UTF-8">
  <title>登录</title>
  <script type="text/javascript" src="${request.contextPath}/static/scripts/jquery.min.js"></script>
  <script type="text/javascript" src="${request.contextPath}/static/scripts/jquery.form.js"></script>
  <script type="text/javascript" src="${request.contextPath}/static/scripts/map.js"></script>
  <script type="text/javascript">
       var contextPath="${request.contextPath}";
	   var map = new Map();
  </script>
 </head>
 <body>
 <center>
 <br><br><br>
  <form id="iForm" name="iForm" method="post" action="${request.contextPath}/login/doLogin">
    <input type="hidden" id="y" name="y">
	<table>
	<tr>
		<td>用户名</td>
		<td><input type="text" id="x" name="x" value="admin" style="width:120px;"></td>
	</tr>
	<tr>
		<td>密码</td>
		<td><input type="password" id="yy" name="yy" value="888888" style="width:120px;"></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><br>
		<input id="loginBt" type="button" value="确定" onclick="return false;">
		&nbsp;
		<input type="reset" value="重置" onclick="">
		</td>
	</tr>
	</table>
  </form>
  </center>
  <script type="text/javascript" src="${request.contextPath}/static/scripts/login.js"></script>  
 </body>
</html>