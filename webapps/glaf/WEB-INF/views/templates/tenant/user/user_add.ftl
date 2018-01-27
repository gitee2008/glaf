<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加用户</title>
<#include "/inc/init_easyui_import.ftl"/>
<script language="javascript">

    var contextPath = "${contextPath}";

    function saveData(){
		if(jQuery("#name").val()==""){
			alert("用户姓名不能为空");
            document.getElementById("name").foucs();
			return;
		}

		if(jQuery("#password").val()==""){
			alert("密码不能为空");
            document.getElementById("password").foucs();
			return;
		}

		if(jQuery("#password2").val()==""){
			alert("确认密码不能为空");
            document.getElementById("password2").foucs();
			return;
		}

		if(jQuery("#password").val()!=jQuery("#password2").val()){
			alert("两次密码输入不一致");
            document.getElementById("password2").foucs();
			return;
		}
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/tenant/user/saveAdd',
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
<div class="nav-title"><span class="Title">用户管理</span>&gt;&gt;增加用户</div>
<form id="iForm" name="iForm" action="${contextPath}/tenant/user/saveAdd" method="post"  > 
<input type="hidden" id="tenantId" name="tenantId" value="${tenantId}">
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="box">
  <tr>
    <td class="box-mm">
	<table width="95%" align="center" border="0" cellspacing="0" cellpadding="5">
	  <tr>
        <td class="input-box2" valign="top">机  构</td>
        <td>
		<select id="organizationId" name="organizationId">
           <option value="0">/根节点</option>
           <#list  trees as tree>
			<#if tree.locked == 0>
              <option value="${tree.id}">${tree.blank}${tree.name}</option>
			</#if>
		  </#list>
        </select>
		<script language="javascript">								
          document.all.organizationId.value="${user.organizationId}";	
	    </script>	
	   </td>
      </tr>
	  <tr>
        <td class="input-box2" valign="top">姓　名*</td>
        <td>
		<input id="name" name="name" type="text" size="30" class="x-text" datatype="string" nullable="no" maxsize="20" chname="姓名">
		</td>
      </tr>
      <tr>
        <td class="input-box2" valign="top">密　码*</td>
        <td>
		<input id="password" name="password" type="password" size="30" class="x-text" datatype="string" nullable="no" minsize="6" maxsize="20" chname="密码">
		</td>
      </tr>
      <tr>
        <td class="input-box2" valign="top">确认密码*</td>
        <td>
		<input id="password2" name="password2" type="password" size="30" class="x-text"  datatype="string" nullable="no" minsize="6" maxsize="20" chname="确认密码">
		</td>
      </tr>
      <tr>
        <td class="input-box2" valign="top">性　别</td>
        <td>
          <input type="radio" name="sex" value="0">男
          <input type="radio" name="sex" value="1" checked>女
		</td>
      </tr>
      <tr>
        <td class="input-box2" valign="top">手　机</td>
        <td>
          <input name="mobile" type="text" size="30" class="x-text" datatype="string" nullable="yes" maxsize="12" chname="手机"> </td>
      </tr>
      <tr>
        <td class="input-box2" valign="top">邮　件</td>
        <td>
          <input name="email" type="text" size="30" class="x-text" datatype="email" nullable="yes" maxsize="50" chname="邮件">
		  </td>
      </tr>
      <tr>
        <td class="input-box2" valign="top">办公电话</td>
        <td>
          <input name="telephone" type="text" size="30" class="x-text" datatype="string" nullable="yes" maxsize="20" chname="办公电话">        
		  </td>
      </tr>
	  <tr>
        <td class="input-box2" valign="top">职 位</td>
        <td>
		    <select id="headship" name="headship">
			  <option value="0">----请选择----</option>
			  <#list  dictories as a>
				<option value="${a.code}">${a.name} [${a.code}]</option>
			  </#list>
		   </select>
		   <script type="text/javascript">
		        document.getElementById("headship").value="${bean.headship}";
		   </script>
		</td>
      </tr>
      <tr>
        <td colspan="2" align="center" valign="bottom" height="30">&nbsp;
         <input name="btn_save" type="button" value=" 确定 " class="btnGray" onclick="javascript:saveData();">
		</td>
      </tr>
    </table></td>
  </tr>
</table>
</form>
</body>
</html>
