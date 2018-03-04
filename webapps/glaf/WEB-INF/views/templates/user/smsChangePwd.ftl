<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改密码</title>
<#include "/inc/init_easyui_import.ftl"/> 
<script language="javascript">
 
    var contextPath = "${contextPath}";

    function saveData(){

		if(jQuery("#newPwd").val()==""){
			alert("新密码不能为空！");
            document.getElementById("newPwd").foucs();
			return;
		}

		if(jQuery("#password2").val()==""){
			alert("确认密码不能为空！");
            document.getElementById("password2").foucs();
			return;
		}

		if(jQuery("#newPwd").val()!=jQuery("#password2").val()){
			alert("两次密码输入不一致！");
            document.getElementById("password2").foucs();
			return;
		}
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/user/savePwd2',
				   data: params,
				   dataType: 'json',
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
						   window.location.href="${contextPath}/my/home";
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
<form id="iForm" name="iForm"  action="${contextPath}/user/savePwd2" method="post" > 
  <table width="500" align="center" border="0" cellspacing="0" cellpadding="5">
      <tr>
        <td height="28"class="input-box2" valign="top">新密码*</td>
        <td height="28"><input id="newPwd" name="newPwd" type="password" size="30"  class="input span3 x-text" value="" datatype="string" nullable="no" minsize="6" maxsize="20" chname="密码"></td>
      </tr>
      <tr>
        <td height="28"class="input-box2" valign="top">确认密码*</td>
        <td height="28"><input id="password2" name="password2" type="password" size="30"  class="input span3 x-text" value=""  datatype="string" nullable="no" minsize="6" maxsize="20" chname="确认密码"></td>
      </tr>
      <tr>
        <td height="30">&nbsp;</td>
		<td align="left" valign="bottom" height="30">&nbsp;
           <input name="btn_save" type="button" value=" 确定 " class="btnGray" onclick="javascript:saveData();">
		</td>
      </tr>
  </table> 
</form>
</body>
</html>
