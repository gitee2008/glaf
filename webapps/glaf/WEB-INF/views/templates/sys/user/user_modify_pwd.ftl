<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改密码</title>
<#include "/inc/init_easyui_import.ftl"/>
<script language="javascript">

    var contextPath = "${contextPath}";

    function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/user/savePwd',
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
<br><br>
<form id="iForm" name="iForm" action="${contextPath}/sys/user/savePwd" method="post" > 
 
<table width="500" border="0" align="center" cellpadding="0" cellspacing="0" class="box">
  <tr>
    <td>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr class="box">
        <td class="box-lt">&nbsp;</td>
        <td class="box-mt">&nbsp;</td>
        <td class="box-rt">&nbsp;</td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="box-mm"><table width="95%" align="center" border="0" cellspacing="0" cellpadding="5">
	  <tr>
        <td class="input-box2" valign="top">原密码*</td>
        <td><input name="oldPwd" type="password" size="30" class="input" value="" datatype="string" nullable="no" minsize="6" maxsize="20" chname="密码"></td>
      </tr>
      <tr>
        <td class="input-box2" valign="top">新密码*</td>
        <td><input name="newPwd" type="password" size="30" class="input" value="" datatype="string" nullable="no" minsize="6" maxsize="20" chname="密码"></td>
      </tr>
      <tr>
        <td class="input-box2" valign="top">确认密码*</td>
        <td><input name="password2" type="password" size="30" class="input" value=""  datatype="string" nullable="no" minsize="6" maxsize="20" chname="确认密码"></td>
      </tr>
      <tr>
        <td colspan="2" align="center" valign="bottom" height="30">&nbsp;
           <input name="btn_save2" type="button" value="修改密码" class="button" onclick="javascript:saveData();">
		</td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr class="box">
        <td class="box-lb">&nbsp;</td>
        <td class="box-mb">&nbsp;</td>
        <td class="box-rb">&nbsp;</td>
      </tr>
    </table></td>
  </tr>
</table>
</form>
</body>
</html>
