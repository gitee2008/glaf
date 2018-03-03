<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>月度费用</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/monthlyFee/saveMonthlyFee',
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

	function saveAsData(){
		document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/monthlyFee/saveMonthlyFee',
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
  <div data-options="region:'north',split:false,border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<img src="${contextPath}/static/images/window.png"><span class="x_content_title">&nbsp;编辑月度费用</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${monthlyFee.id}"/>
  <table class="easyui-form" style="width:600px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">年</td>
		<td align="left">
             <select id="year" name="year">
				<#list years as year>
				<option value="${year}">${year}</option>
				</#list>
			  </select>
			  <script type="text/javascript">
			     document.getElementById("year").value="${monthlyFee.year}";
			  </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">月</td>
		<td align="left">
             <select id="month" name="month">
				<#list months as month>
				<option value="${month}">${month}</option>
				</#list>
			  </select>
			  <script type="text/javascript">
			     document.getElementById("month").value="${monthlyFee.month}";
			  </script>
		</td>
	</tr>
	
	<tr>
		<td width="20%" align="left">上月累计结余</td>
		<td align="left">
			<input id="lastMonthSurplus" name="lastMonthSurplus" type="text" 
			       class="easyui-numberbox x-text" precision="2"  maxLength="9"
				   increment="1" style="width:80px; text-align:right;"  
				   value="${monthlyFee.lastMonthSurplus}"/>&nbsp;(单位：元)
		</td>
	</tr>	 
	<tr>
		<td width="20%" align="left">本月结余</td>
		<td align="left">
			<input id="monthLeft" name="monthLeft" type="text" 
			       class="easyui-numberbox x-text" precision="2"  maxLength="9"
				   increment="1" style="width:80px; text-align:right;"  
				   value="${monthlyFee.monthLeft}"/>&nbsp;(单位：元)
		</td>
	</tr>	
	<tr>
		<td width="20%" align="left">本月累计结余</td>
		<td align="left">
			<input id="monthTotalLeft" name="monthTotalLeft" type="text" 
			       class="easyui-numberbox x-text" precision="2"  maxLength="9"
				   increment="1" style="width:80px; text-align:right;"  
				   value="${monthlyFee.monthTotalLeft}"/>&nbsp;(单位：元)
		</td>
	</tr>	
	<tr>
		<td width="20%" align="left">出勤天数</td>
		<td align="left">
			<input id="workDay" name="workDay" type="text" 
			       class="easyui-numberbox x-text" precision="0"  maxLength="2"
				   increment="1" style="width:80px; text-align:right;"  
				   value="${monthlyFee.workDay}"/>&nbsp;(单位：天)
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">本月实际就餐人数</td>
		<td align="left">
			<input id="totalRepastPerson" name="totalRepastPerson" type="text" 
			       class="easyui-numberbox x-text" precision="0"  maxLength="9"
				   increment="1" style="width:80px; text-align:right;"  
				   value="${monthlyFee.totalRepastPerson}"/>&nbsp;(单位：人/次)
		</td>
	</tr>	 
	<tr>
		<td width="20%" align="left">燃料费</td>
		<td align="left">
			<input id="fuelFee" name="fuelFee" type="text" style="width:80px; text-align:right;"
			       class="easyui-numberbox  x-text"  precision="2" maxLength="9" 
				   value="${monthlyFee.fuelFee}"/>&nbsp;(单位：元)
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">人工费</td>
		<td align="left">
			<input id="laborFee" name="laborFee" type="text" style="width:80px; text-align:right;"
			       class="easyui-numberbox  x-text"  precision="2" maxLength="9" 
				   value="${monthlyFee.laborFee}"/>&nbsp;(单位：元)
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">外购点心费用</td>
		<td align="left">
			<input id="dessertFee" name="dessertFee" type="text" style="width:80px; text-align:right;"
			       class="easyui-numberbox  x-text"  precision="2" maxLength="9" 
				   value="${monthlyFee.dessertFee}"/>&nbsp;(单位：元)
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">其他费</td>
		<td align="left">
			<input id="otherFee" name="otherFee" type="text" style="width:80px; text-align:right;"
			       class="easyui-numberbox  x-text"  precision="2" maxLength="9"
				   value="${monthlyFee.otherFee}"/>&nbsp;(单位：元)
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">备注</td>
		<td align="left">
            <textarea id="remark" name="remark" 
			          class="easyui-validatebox  x-text"  
			          style="width:350px;height:120px">${monthlyFee.remark}</textarea>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left"></td>
		<td align="left">
            <br><br><br><br>
		</td>
	</tr>
    </tbody>
  </table>
  </form>
</div>
</div>
</body>
</html>