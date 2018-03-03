<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>复制出库单</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

	function saveData(){
		var dateTime = document.getElementById("dateTime").value;
		if(document.getElementById("dateTime").value == ""){
			alert("请选择日期。");
			document.getElementById("dateTime").focus();
			return;
		}
	    if(confirm("确定将"+dateTime+"的出库单复制到用量表中吗？")){
		  var params = jQuery("#iForm").formSerialize();
		  jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/goodsActualQuantity/copyOutStock',
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
					       window.parent.location.reload();
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
  <div data-options="region:'north',split:false,border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">复制出库单</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >确定</a>
    </div> 
  </div>
  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <br><br>
  <table class="easyui-form" style="width:650px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">采购日期</td>
		<td width="30%" align="left">
            <input id="dateTime" name="dateTime" type="text" class="easyui-datebox x-text" style="width:120px">
		</td>
		<td width="20%" align="left">是否关联到计划用量</td>
		<td width="30%" align="left">
             <select id="includePlan" name="includePlan">
			    <option value="true">关联</option>
				<option value="false">不关联</option>
			 </select>
		</td>
	</tr>
	<tr>
	  <td colspan="4">
	     <div style="font-size: 14px; color:red; font-weight:bold; font-family: 微软雅黑; margin-top:20px;margin-left:80px;">
		   （提示：如果不关联到计划用量，采购单中所有的食品都加入到用量表中。）
		 </div>
	  </td>
	</tr>
   </tbody>
  </table>
 </form>
</div>
</div>
</body>
</html>