<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改用户信息</title>
<#include "/inc/init_easyui_import.ftl"/> 
<script language="javascript">
 
    var contextPath = "${contextPath}";

    function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/user/saveModifyInfo',
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
				   }
			 });
	}
   
	function setValue(obj){
	  obj.value=obj[obj.selectedIndex].value;
	} 
</script>
</head>

<body>
<br><br>
<form id="iForm" name="iForm" action="${contextPath}/user/saveModifyInfo" method="post" > 
 <table width="500" align="center" border="0" cellspacing="0" cellpadding="5">
      <tr>
        <td width="20%" class="input-box" height="28">用户名</td>
        <td width="80%" height="28">${user.userId}</td>
      </tr>
      <tr>
        <td height="28" class="input-box2" valign="top">姓　名</td>
        <td height="28">${user.name}</td>
      </tr>
      <tr>
        <td height="28"class="input-box2" valign="top">手　机</td>
        <td height="28">
          <input name="mobile" type="text" size="30"  class="input span3 x-text" datatype="string" value="${user.mobile}" nullable="no" maxsize="12" chname="手机">        
		</td>
      </tr>
      <tr>
        <td height="28" class="input-box2" valign="top">邮　件</td>
        <td height="28">
          <input name="email" type="text" size="30"  class="input span3 x-text" datatype="email" value="${user.email}" nullable="no" maxsize="50" chname="邮件">        
		</td>
      </tr>
      <tr>
        <td height="28" class="input-box2" valign="top">办公电话</td>
        <td height="28">
          <input name="telephone" type="text" size="30"  class="input span3 x-text" datatype="string" value="${user.telephone}" nullable="no" maxsize="20" chname="办公电话">        
		</td>
      </tr>
       <tr>
	    <td >&nbsp;</td>
        <td align="left" valign="bottom" height="30">&nbsp;
           <br> 
           <input name="btn_save" type="button" value=" 确定 " class="btnGray" onclick="javascript:saveData();">
		</td>
      </tr>
    </table> 
</form>
</body>
</html>
