<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看用户信息</title>
<#include "/inc/init_easyui_import.ftl"/>
<script language="javascript">

    var contextPath = "${contextPath}";

	function setValue(obj){
	  obj.value=obj[obj.selectedIndex].value;
	}
</script>
</head>
<body>
<form id="iForm" name="iForm" action="" method="post"  > 
<input type="hidden" name="userId" value="${userId_encode}">
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="box">
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr class="box">
        <td class="box-lt">&nbsp;</td>
        <td class="box-mt">&nbsp;</td>
        <td class="box-rt">&nbsp;</td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="box-mm"><table width="95%" align="center" border="0" cellspacing="0" cellpadding="5">
      <#if tenant?exists>
      <tr>
        <td width="20%" class="input-box2">租户名称</td>
        <td width="80%">${tenant.name}</td>
      </tr>
	  </#if>
      <tr>
        <td width="20%" class="input-box2">用户名</td>
        <td width="80%">${user.userId}</td>
      </tr>
      <!-- <tr>
        <td class="input-box2" valign="top">机 构</td>
        <td>
		<select id="organizationId" name="organizationId" >
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
      </tr> -->
      <tr>
        <td class="input-box2" valign="top">姓　名</td>
        <td>${user.name}</td>
      </tr>

	  <tr>
        <td class="input-box2" valign="top">最近登录时间</td>
        <td>
		  <#if user.lastLoginTime?exists>
		    ${user.lastLoginTime?string('yyyy-MM-dd HH:mm:ss')}
		  </#if>
		</td>
      </tr>

      <tr>
        <td class="input-box2" valign="top">性　别</td>
        <td>
          <#if user.sex==0>女</#if>
          <#if user.sex==1>男</#if>
		</td>
      </tr>

      <tr>
        <td class="input-box2" valign="top">手　机</td>
        <td>${user.mobile}</td>
      </tr>

      <tr>
        <td class="input-box2" valign="top">邮　件</td>
        <td>${user.email}</td>
      </tr>

      <tr>
        <td class="input-box2" valign="top">办公电话</td>
        <td>${user.telephone}</td>
      </tr>
	 
      <tr>
        <td class="input-box2" valign="top">是否有效</td>
        <td>
		<#if user.locked==0>是</#if>
		<#if user.locked==1>否</#if>
		</td>
      </tr>

    </table></td>
  </tr>
  <tr>
    <td>
	 <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr class="box">
        <td class="box-lb">&nbsp;</td>
        <td class="box-mb">&nbsp;</td>
        <td class="box-rb">&nbsp;</td>
      </tr>
    </table>
	</td>
  </tr>
</table>
</form>
</body>
</html>
