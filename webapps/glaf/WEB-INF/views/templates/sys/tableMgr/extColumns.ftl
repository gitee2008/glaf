<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>字段列表</title>
<#include "/inc/init_easyui_import.ftl"/>
<style>

.subject { font-size: 13px; text-decoration: none; font-weight:normal; font-family:"宋体"}
.table-border { background-color:#e6e6e6; height: 32px; font-family:"宋体"}
.table-head { background-color:#e6e6e6; height: 30px; font-weight:bold; font-size: 13px; font-family:"宋体"}
.table-content { background-color:#ffffff; height: 28px; font-size: 12px; font-family:"宋体"}

</style>
<script type="text/javascript">
	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/tableMgr/saveColumns?targetId=${targetId}',
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
					       
					   } 
				   }
			 });
	}


    function submitReq(){
        document.getElementById("iForm").submit();
    }

</script>
</head>

<body>
<div style="margin:0px;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:true,border:true" style="height:40px;margin-top:0px;"> 
    <div class="toolbar-backgroud"> 
	<span class="x_content_title">&nbsp;<img src="${contextPath}/static/images/window.png">&nbsp;字段列表</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	   onclick="javascript:saveData();" > 保 存 
	</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="tableId" name="tableId" value="${tableId}"/>

  <table class="table-border" cellspacing="1" cellpadding="4" width="99%" nowrap align="center">
    <thead>
	  <tr>
	  <td class="table-head" width="25%" align="left">字段名称</td>
	  <td class="table-head" width="25%" align="left">标题</td>
	  <td class="table-head" width="25%" align="left">类型</td>
	  <td class="table-head" width="25%" align="left">长度</td>
	  </tr>
	</thead>
    <tbody>
	<#list  columns as col>
	<tr>
		<td class="table-content" align="left">  
		  <input type="text" id="columnName_${col.id}" name="columnName_${col.id}" value="${col.columnName}" 
		         class="easyui-validatebox  x-text" size="30">
		</td>
		<td class="table-content" align="left">  
		  <input type="text" id="title_${col.id}" name="title_${col.id}" value="${col.title}" class="easyui-validatebox  x-text" size="30">
		</td>
		<td  class="table-content" align="left">
            <select id="javaType_${col.id}" name="javaType_${col.id}">
			    <option value="Integer">整数型</option>
				<option value="Long">长整数型</option>
				<option value="Double">数值型</option>
				<option value="Date">日期型</option>
				<option value="String">文本型</option>
				<option value="Clob">Clob长文本型</option>
				<option value="Blob">Blob二进制型</option>
            </select> 
			<script type="text/javascript">
		     document.getElementById("javaType_${col.id}").value="${col.javaType}";  
		 </script>
		</td>
		<td class="table-content" align="left">
		   <select id="length_${col.id}" name="length_${col.id}">
			    <option value="0">默认</option>
				<option value="50">50</option>
				<option value="100">100</option>
				<option value="150">150</option>
				<option value="200">200</option>
				<option value="250">250</option>
				<option value="500">500</option>
				<option value="1000">1000</option>
				<option value="2000">2000</option>
				<option value="4000">4000</option>
            </select>
			<script type="text/javascript">
			    document.getElementById("length_${col.id}").value="${col.length}";
			</script>
		</td>
	</tr>
	</#list>
    </tbody>
  </table>
  <br>
  <br>
  <br>
  <br>
 </form>
</div>
</div>
</body>
</html>