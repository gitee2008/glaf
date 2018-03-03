<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择食谱</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

	function saveData(){
		
		if(document.getElementById("week").value==""){
			alert("请选择周次。");
			document.getElementById("week").focus();
			return;
		}

        var week = document.getElementById("week").value;
        if(confirm("确定要将第${suitNo}套模板的食谱配置到第"+week+"周所选日期吗？")){
			var params = jQuery("#iForm").formSerialize();
			jQuery.ajax({
					   type: "POST",
					   url: '${contextPath}/heathcare/dietary/batchAdd',
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
							   //window.parent.location.reload();
						   } 
					   }
				 });
			 }
	}

</script>
</head>

<body>
<div style="margin:0;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:true,border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<span class="x_content_title">&nbsp;选择食谱</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >确定</a>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="suitNo" name="suitNo" value="${suitNo}"/>
  <input type="hidden" id="sysFlag" name="sysFlag" value="${sysFlag}"/>
  <table class="easyui-form" style="width:600px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">周次</td>
		<td width="80%" align="left">
          <select id="week" name="week">
		    <option value="">----请选择----</option>
			<#list weeks as week>
			<option value="${week}">${week}</option>
			</#list>
		  </select>
		  <script type="text/javascript">
		      document.getElementById("week").value="${maxWeek}";
		  </script>
		</td>
	</tr>

	<tr>
		<td width="20%" align="left">日期1</td>
		<td width="80%" align="left">
            <input id="dateString1" name="dateString1" type="text" 
			       class="easyui-datebox x-text" style="width:120px">
		</td>
	</tr>

	<tr>
		<td width="20%" align="left">日期2</td>
		<td width="80%" align="left">
            <input id="dateString2" name="dateString2" type="text" 
			       class="easyui-datebox x-text" style="width:120px">
		</td>
	</tr>

	<tr>
		<td width="20%" align="left">日期3</td>
		<td width="80%" align="left">
            <input id="dateString3" name="dateString3" type="text" 
			       class="easyui-datebox x-text" style="width:120px">
		</td>
	</tr>

	<tr>
		<td width="20%" align="left">日期4</td>
		<td width="80%" align="left">
            <input id="dateString4" name="dateString4" type="text" 
			       class="easyui-datebox x-text" style="width:120px">
		</td>
	</tr>

	<tr>
		<td width="20%" align="left">日期5</td>
		<td width="80%" align="left">
            <input id="dateString5" name="dateString5" type="text" 
			       class="easyui-datebox x-text" style="width:120px">
		</td>
	</tr>

   </tbody>
  </table>
 </form>
</div>
</div>
</body>
</html>