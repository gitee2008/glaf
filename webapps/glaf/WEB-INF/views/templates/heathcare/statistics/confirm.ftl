<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>重新统计数据</title>
<#include "/inc/init_easyui_import.ftl"/> 
<script type="text/javascript">
 	function executeAll(){
	   if(confirm("数据统计需要比较长的时间，确定现在执行吗？")){
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/dietaryStatistics/executeAll',
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
					       window.location.reload();
					   } 
				   }
			 });
	   }
	}
</script>
</head>
<body>
<form name="iForm" action="" method="post" > 
<table width="100%" height="258"  border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <th> 
	<table width="200" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr> 
          <td height="50" align="center">
		  </td>
        </tr>
        <tr> 
          <td align="center">
		    <h3><img src="${contextPath}/static/images/formula.png" border="0">&nbsp;重新统计数据</h3>
		  </td>
        </tr>
        <tr> 
          <td align="center">&nbsp; 
		  <br>
		  <input name="button" type="button" value="确定" class="btnGray" onclick="javascript:executeAll();"> 
          </td>
        </tr>
      </table>
	  </th>
  </tr>
</table>
</form> 
</body>
</html>