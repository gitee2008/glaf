<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>手机短信找回密码</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/jsencrypt.min.js"></script>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">
    var contextPath = "${contextPath}";

	function submitRequest(){
		if(jQuery("#mobile").val()=="" || jQuery("#mobile").val().length != 11){
		   alert("请输入合法的手机号码。");
		   document.getElementById("mobile").focus();
		   return;
	    }

		if(jQuery("#verificationCode").val()=="" || jQuery("#verificationCode").val().length != 6){
		   alert("请输入短信验证码。");
		   document.getElementById("verificationCode").focus();
		   return;
	    }

		var mobile = document.getElementById("mobile").value;
		jQuery.ajax({
					type : "POST",
					url : "${contextPath}/userSmsResetPassword/getKey?mobile="+mobile,
					dataType : 'json',
					error : function(data) {
										
					},
					success : function(data) {
						if (data != null && data.public_key != null) {
							var verificationCode = document.getElementById("verificationCode").value;
							var crypt2 = new JSEncrypt();
		                    crypt2.setKey(data.public_key);
		                    var str = crypt2.encrypt(verificationCode);
                            document.getElementById("verificationCode").value=str;
                            document.iForm.submit();
					    } else {
                            alert("不能取得登录密锁，请稍候再试。");
						}
					}
		        });

	}

	function sendSms(){
        if(jQuery("#mobile").val()=="" || jQuery("#mobile").val().length != 11){
		   alert("请输入合法的手机号码。");
		   document.getElementById("mobile").focus();
		   return;
	    }
	    var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sms/smsVerifyMessage/createLoginSms',
				   data: params,
				   dataType: 'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } else {
						    
					   }
					   if(data.statusCode == 200){
					       alert('验证码已经发送到您手机，请注意查收！');
					   } 
				   }
			 });
	}

</script>
</head>
<body>
<div class="content-block" style="width: 80%;"><br>
<div class="x_content_title"><img
	src="${contextPath}/static/images/window.png"
	alt="手机短信找回密码">&nbsp;手机短信找回密码
</div>
<br>
<form id="iForm" name="iForm" method="post" action="${contextPath}/userSmsResetPassword/changePwd"> 
  <table width="880" border="0" align="center" cellpadding="0" cellspacing="0" class="box">
	  <tr>
        <td height="40"><span class="x_green_name">手机号码</span>&nbsp;<span class="required">*</span></td>
        <td>
          <input id="mobile" name="mobile" type="text" size="20" class="easyui-validatebox x-text" style="width:250px"
		         data-options="required:false" datatype="string" 
		         nullable="yes" maxsize="50" chname="手机"  maxlength="20">
		   &nbsp;<input name="btn_send2" type="button" value=" 发送 " class="btnGray" onclick="javascript:sendSms();">
		</td>
      </tr>
 	  <tr>
        <td height="40"><span class="x_green_name">验证码</span>&nbsp;<span class="required">*</span></td>
        <td>
          <input id="verificationCode" name="verificationCode" type="text" size="20" class="easyui-validatebox x-text" style="width:250px"
		         data-options="required:false" datatype="string" 
		         nullable="yes" maxsize="50" chname="短信验证码"  maxlength="20">          
		</td>
      </tr>
      <tr>
        <td class="input-box2"  height="30">&nbsp;</td>
        <td  align="left" height="40">&nbsp;
		   <br>
           <input name="btn_submit" type="button" value=" 确 定 " class="btnGray" onclick="javascript:submitRequest();">
	    </td>
      </tr>
  </table>
</form>
</body>
</html>