<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>每月膳食费</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/monthlyMealFee/saveMonthlyMealFee',
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
				   url: '${contextPath}/heathcare/monthlyMealFee/saveMonthlyMealFee',
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
	<img src="${contextPath}/static/images/window.png"><span class="x_content_title">&nbsp;编辑每月膳食费</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${monthlyMealFee.id}"/>
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
			     document.getElementById("year").value="${monthlyMealFee.year}";
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
			     document.getElementById("month").value="${monthlyMealFee.month}";
			  </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">年级</td>
		<td align="left">
             <select id="classType" name="classType">
			    <option value="">----请选择----</option>
				<#list dictoryList as d>
				<option value="${d.code}">${d.name}</option>
				</#list> 
             </select>
			 <script type="text/javascript">
			     document.getElementById("classType").value="${monthlyMealFee.classType}";
			 </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">总人数</td>
		<td align="left">
			<input id="personTotal" name="personTotal" type="text" 
			       class="easyui-numberbox x-text"  precision="0"  maxLength="5"
				   increment="1" style="width:80px; text-align:right;"  
				   value="${monthlyMealFee.personTotal}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">实收伙食费</td>
		<td align="left">
			<input id="receiptFund" name="receiptFund" type="text" style="width:80px; text-align:right;"
			       class="easyui-numberbox  x-text"  precision="2"  maxLength="9"
				  value="${monthlyMealFee.receiptFund}"/>&nbsp;(单位：元)
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">缺勤人数</td>
		<td align="left">
			<input id="absentPerson" name="absentPerson" type="text" style="width:80px; text-align:right;"
			       class="easyui-numberbox x-text" precision="0"  maxLength="4" 
				   increment="1"  
				   value="${monthlyMealFee.absentPerson}"/>&nbsp;(单位：人/次)
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">缺勤天数</td>
		<td align="left">
			<input id="absentDay" name="absentDay" type="text" style="width:80px; text-align:right;" 
			       class="easyui-numberbox x-text" precision="0"  maxLength="2"
				   increment="1"  
				   value="${monthlyMealFee.absentDay}"/>&nbsp;(单位：天)
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">缺勤退款</td>
		<td align="left">
			<input id="absentRefund" name="absentRefund" type="text" style="width:80px; text-align:right;"
			       class="easyui-numberbox  x-text"  precision="2"  maxLength="9"
				   value="${monthlyMealFee.absentRefund}"/>&nbsp;(单位：元)
		</td>
	</tr>
 
    </tbody>
  </table>
  </form>
</div>
</div>
</body>
</html>