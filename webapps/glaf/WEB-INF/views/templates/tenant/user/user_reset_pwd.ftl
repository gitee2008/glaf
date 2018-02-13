<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>重置密码</title>
<#include "/inc/init_easyui_import.ftl"/>
<script language="javascript">

    var contextPath = "${contextPath}";

    function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/tenant/user/resetPwd',
				   data: params,
				   dataType:  'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } else {
						   alert('操作成功完成！');
					   }
					   if(data.statusCode == 200){
						   window.parent.location.reload();
					       window.close();
					   }  
				   }
			 });
	}

	function setValue(obj){
	  obj.value=obj[obj.selectedIndex].value;
	}
</script>
</head>
<body>
<form id="iForm" name="iForm" action="${contextPath}/tenant/user/resetPwd" method="post" > 
<input type="hidden" name="userId" value="${userId_encode}"> 
   <table width="95%" align="center" border="0" cellspacing="0" cellpadding="5">
	  <tr>
        <td width="20%" class="input-box2">用户名</td>
        <td width="80%">${user.userId}</td>
      </tr>
      <tr>
        <td class="input-box2" valign="top">姓　名</td>
        <td>${user.name}</td>
      </tr>
      <tr>
        <td class="input-box2" valign="top">新密码*</td>
        <td><input name="newPwd" type="password" size="30" class="x-text" value="" datatype="string" nullable="no" minsize="6" maxsize="20" chname="密码"></td>
      </tr>
      <tr>
        <td class="input-box2" valign="top">确认密码*</td>
        <td><input name="password2" type="password" size="30" class="x-text" value=""  datatype="string" nullable="no" minsize="6" maxsize="20" chname="确认密码"></td>
      </tr>
	  <tr>
        <td width="20%" class="input-box2"></td>
        <td width="80%">
		    &nbsp;&nbsp;&nbsp;&nbsp;<input name="btn_save2" type="button" value="修改密码" class="btnGray" onclick="javascript:saveData();">
        </td>
      </tr>
    </table>
</form>
</body>
</html>