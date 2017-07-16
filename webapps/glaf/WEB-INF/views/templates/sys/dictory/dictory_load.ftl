<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据字典</title>
<#include "/inc/init_easyui_import.ftl"/> 
</head>
<body>
<form action="${contextPath}/sys/dictory/reloadDictory" method="post" onsubmit="return verifyAll(this);"> 
<input type="hidden" name="nodeId" value="${nodeId}">
<table width="100%" height="258"  border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <th> 
	<table width="200" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr> 
          <td height="50" align="center">
		  </td>
        </tr>
        <tr> 
          <td align="center"><img src="${contextPath}/static/images/icon_6.jpg" width="6" height="7">重新装载基础数据</td>
        </tr>
        <tr> 
          <td align="center">&nbsp; 
		  <br>
		  <input name="btn_save" type="submit" value="确定" class="btnGray"> 
          </td>
        </tr>
      </table>
	  </th>
  </tr>
</table>
</form> 
</body>
</html>
