<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择食谱</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

	function saveData(){
		if(document.getElementById("dateString").value==""){
			alert("请选择日期。");
			document.getElementById("dateString").focus();
			return;
		}
		if(document.getElementById("week").value==""){
			alert("请选择周次。");
			document.getElementById("week").focus();
			return;
		}

		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/dietary/saveBatch',
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
					   } 
				   }
			 });
	}

</script>
</head>

<body>
<div style="margin:0;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:true,border:true" style="height:40px"> 
    <div class="toolbar-backgroud"> 
	<span class="x_content_title">&nbsp;选择食谱</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >确定</a>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="objectIds" name="objectIds" value="${objectIds}"/>
  <table class="easyui-form" style="width:600px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">日期</td>
		<td align="left">
            <input id="dateString" name="dateString" type="text" 
			       class="easyui-datebox x-text" style="width:120px">
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">周次</td>
		<td align="left">
          <select id="week" name="week">
			<#list weeks as week>
			<option value="${week}">${week}</option>
			</#list>
		  </select>
		  <script type="text/javascript">
		      document.getElementById("week").value="${maxWeek}";
		  </script>
		</td>
	</tr>

   </tbody>
  </table>
 </form>
</div>
</div>
</body>
</html>