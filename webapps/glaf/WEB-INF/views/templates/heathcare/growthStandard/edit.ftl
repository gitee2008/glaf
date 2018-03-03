<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>生长标准值</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

<#if can_write == true>
	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/growthStandard/saveGrowthStandard',
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

	function saveAsData(){
		document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/growthStandard/saveGrowthStandard',
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
					       //window.parent.location.reload();
					   }
				   }
			 });
	}

</#if>
</script>
</head>

<body>
<div style="margin:0;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:true,border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
		<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">编辑生长标准值</span>
		<#if can_write == true>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveAsData();" >另存</a> 
		</#if>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${growthStandard.id}"/>
  <table class="easyui-form" style="width:600px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">标准</td>
		<td align="left">
          <select id="standardType" name="standardType">
			<option value="">----请选择----</option>
			<option value="CN">中国标准</option>
			<option value="ISO">国际标准</option>
		  </select>
		  <script type="text/javascript">
			   document.getElementById("standardType").value="${growthStandard.standardType}";
		  </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">年龄</td>
		<td align="left">
		  <select id="age" name="age">
			<option value="0">0岁</option>
			<option value="1">1岁</option>
			<option value="2">2岁</option>
			<option value="3">3岁</option>
			<option value="4">4岁</option>
			<option value="5">5岁</option>
			<option value="6">6岁</option>
		  </select>
		  <script type="text/javascript">
			   document.getElementById("age").value="${growthStandard.age}";
		  </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">月</td>
		<td align="left">
		  <select id="month" name="month">
			<#list months as month>
			<option value="${month}">${month}月</option>
			</#list> 
		  </select>
		  <script type="text/javascript">
			   document.getElementById("month").value="${growthStandard.month}";
		  </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">性别</td>
		<td align="left">
          <select id="sex" name="sex">
			<option value="">----请选择----</option>
			<option value="1">男生</option>
			<option value="0">女生</option>
		  </select>
		  <script type="text/javascript">
			   document.getElementById("sex").value="${growthStandard.sex}";
		  </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">类型</td>
		<td align="left">
           <select id="type" name="type">
			<option value="">----请选择----</option>
			<!-- <option value="1">年龄别头围标准差</option> -->
			<option value="2">H/A年龄别身高标准差</option>
			<option value="3">W/A年龄别体重标准差</option>
			<option value="4">W/H身高别体重标准差</option>
			<option value="5">体质指数（BMI）</option>
		  </select>
		  <script type="text/javascript">
			   document.getElementById("type").value="${growthStandard.type}";
		  </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">身高</td>
		<td align="left">
			<input id="height" name="height" type="text"
			       class="easyui-numberbox x-text" precision="1" 
			       style="width:60px; text-align:right;"
				   value="${growthStandard.height}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">体重</td>
		<td align="left">
			<input id="weight" name="weight" type="text"
			       class="easyui-numberbox x-text" precision="1" 
			       style="width:60px; text-align:right;"
				   value="${growthStandard.weight}"/>
		</td>
	</tr>
    <tr>
		<td width="20%" align="left">百分位数3</td>
		<td align="left">
			<input id="percent3" name="percent3" type="text"
			       class="easyui-numberbox x-text" precision="1" 
			       style="width:60px; text-align:right;"
				   value="${growthStandard.percent3}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">百分位数15</td>
		<td align="left">
			<input id="percent15" name="percent15" type="text"
			       class="easyui-numberbox x-text" precision="1" 
			       style="width:60px; text-align:right;"
				   value="${growthStandard.percent15}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">百分位数50</td>
		<td align="left">
			<input id="percent50" name="percent50" type="text"
			       class="easyui-numberbox x-text" precision="1" 
			       style="width:60px; text-align:right;"
				   value="${growthStandard.percent50}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">百分位数85</td>
		<td align="left">
			<input id="percent85" name="percent85" type="text"
			       class="easyui-numberbox x-text" precision="1" 
			       style="width:60px; text-align:right;"
				   value="${growthStandard.percent85}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">百分位数97</td>
		<td align="left">
			<input id="percent97" name="percent97" type="text"
			       class="easyui-numberbox x-text" precision="1" 
			       style="width:60px; text-align:right;"
				   value="${growthStandard.percent97}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">-4SD</td>
		<td align="left">
			<input id="negativeFourDSDeviation" name="negativeFourDSDeviation" type="text"
			       class="easyui-numberbox x-text" precision="1" 
			       style="width:60px; text-align:right;"
				   value="${growthStandard.negativeFourDSDeviation}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">-3SD</td>
		<td align="left">
			<input id="negativeThreeDSDeviation" name="negativeThreeDSDeviation" type="text"
			       class="easyui-numberbox x-text" precision="1" 
			       style="width:60px; text-align:right;"
				   value="${growthStandard.negativeThreeDSDeviation}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">-2SD</td>
		<td align="left">
			<input id="negativeTwoDSDeviation" name="negativeTwoDSDeviation" type="text"
			       class="easyui-numberbox x-text" precision="1" 
			       style="width:60px; text-align:right;"
				   value="${growthStandard.negativeTwoDSDeviation}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">-1SD</td>
		<td align="left">
			<input id="negativeOneDSDeviation" name="negativeOneDSDeviation" type="text"
			       class="easyui-numberbox x-text" precision="1" 
			       style="width:60px; text-align:right;"
				   value="${growthStandard.negativeOneDSDeviation}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">中位数</td>
		<td align="left">
			<input id="median" name="median" type="text"
			       class="easyui-numberbox x-text" precision="1" 
			       style="width:60px; text-align:right;"
				   value="${growthStandard.median}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">+1SD</td>
		<td align="left">
			<input id="oneDSDeviation" name="oneDSDeviation" type="text"
			       class="easyui-numberbox x-text" precision="1" 
			       style="width:60px; text-align:right;"
				   value="${growthStandard.oneDSDeviation}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">+2SD</td>
		<td align="left">
			<input id="twoDSDeviation" name="twoDSDeviation" type="text"
			       class="easyui-numberbox x-text" precision="1" 
			       style="width:60px; text-align:right;"
				   value="${growthStandard.twoDSDeviation}"/>
		</td>
	</tr>
    <tr>
		<td width="20%" align="left">+3SD</td>
		<td align="left">
			<input id="threeDSDeviation" name="threeDSDeviation" type="text"
			       class="easyui-numberbox x-text" precision="1" 
			       style="width:60px; text-align:right;"
				   value="${growthStandard.threeDSDeviation}"/>
		</td>
	</tr>
    <tr>
		<td width="20%" align="left">+4SD</td>
		<td align="left">
			<input id="fourDSDeviation" name="fourDSDeviation" type="text"
			       class="easyui-numberbox x-text" precision="1" 
			       style="width:60px; text-align:right;"
				   value="${growthStandard.fourDSDeviation}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">&nbsp;</td>
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