<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>计划用量表</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		if(document.getElementById("goodsId").value==""){
			document.getElementById("nodeId").focus();
			alert("请选择物品。");
			return;
		}

		if(document.getElementById("unit").value==""){
			document.getElementById("unit").focus();
			alert("请选择计量单位。");
			return;
		}

		if(document.getElementById("quantity").value==""){
			document.getElementById("quantity").focus();
			alert("请填写使用数量。");
			return;
		}

		if(document.getElementById("quantity").value*1.0 <= 0.0){
			document.getElementById("quantity").focus();
			alert("使用数量必须大于0。");
			return;
		}

		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/goodsPlanQuantity/saveGoodsPlanQuantity',
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
		if(document.getElementById("goodsId").value==""){
			document.getElementById("nodeId").focus();
			alert("请选择物品。");
			return;
		}

		if(document.getElementById("unit").value==""){
			document.getElementById("unit").focus();
			alert("请选择计量单位。");
			return;
		}

		if(document.getElementById("quantity").value==""){
			document.getElementById("quantity").focus();
			alert("请填写使用数量。");
			return;
		}

		if(document.getElementById("quantity").value*1.0 <= 0.0){
			document.getElementById("quantity").focus();
			alert("使用数量必须大于0。");
			return;
		}

		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/goodsPlanQuantity/saveGoodsPlanQuantity',
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

   function switchFood(){
       var nodeId = document.getElementById("nodeId").value;
	   if(nodeId != ""){
		   var link = "${contextPath}/heathcare/foodComposition/json?nodeId="+nodeId+"&rows=1000";
		   jQuery.getJSON(link, function(data){
			  var food = document.getElementById("goodsId");
			  food.options.length=0;
			  jQuery.each(data.rows, function(i, item){
				 food.options.add(new Option(item.name, item.id));
			  });
			});
	   }
   }


   function audit(){
	   if(confirm("审核通过后不能再修改数据，确定通过吗？")){
		 var params = jQuery("#iForm").formSerialize();
		 jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/goodsPlanQuantity/audit',
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
	}


</script>
</head>

<body>
<div style="margin-top:2px;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <!-- <div data-options="region:'north',split:false,border:true" style="height:40px"  class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">编辑计划用量表</span>
	<#if audit == true>
	  <#if goodsPlanQuantity.businessStatus == 0>
	   <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	      onclick="javascript:audit();" >确认通过</a> 
	  <#elseif goodsPlanQuantity.businessStatus == 9>
	  &nbsp;<span style="font:bold 13px 宋体; color:#ff0000;">已审核</span>&nbsp;
	  </#if>
	<#else>
	  <#if goodsPlanQuantity.businessStatus == 9>
	  &nbsp;<span style="font:bold 13px 宋体; color:#ff0000;">已审核</span>&nbsp;
	  <#else>
	  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	     onclick="javascript:saveData();" >保存</a> 
	  </#if>
	</#if>
    </div> 
  </div> -->

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${goodsPlanQuantity.id}">
  <input type="hidden" id="goodsName" name="goodsName" value="${goodsPlanQuantity.goodsName}">
  <table class="easyui-form" style="width:650px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">物品名称</td>
		<td align="left">
			<select id="nodeId" name="nodeId" onchange="javascript:switchFood();">
				<option value="0">----请选择----</option>
				<#list foodCategories as tree>
				<option value="${tree.id}">${tree.name}</option>
				</#list> 
			</select>
			<script type="text/javascript">
			   document.getElementById("nodeId").value="${nodeId}";
			</script>
			&nbsp;
			<select id="goodsId" name="goodsId" onchange="chooseGoodsName();" >
				<option value="">----请选择----</option>
				<#list foods as f>
				<option value="${f.id}">${f.name}</option>
				</#list> 
			</select>
			<script type="text/javascript">
			   document.getElementById("goodsId").value="${goodsPlanQuantity.goodsId}";
			</script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">计量单位</td>
		<td align="left">
            <select id="unit" name="unit">
			    <option value="KG" selected>千克</option>
				<option value="L">升</option>
			</select>
			<script type="text/javascript">
				document.getElementById("unit").value="${goodsPlanQuantity.unit}";
			</script> 
			(提示：1千克=1000克 1克=0.001千克，计量单位请统一换算成千克或升)
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">数量</td>
		<td align="left">
			<input id="quantity" name="quantity" type="text" style="width:90px;text-align:right;"
			       class="easyui-numberbox x-text" precision="1" 
				   value="${goodsPlanQuantity.quantity}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">备注</td>
		<td align="left">
		    <textarea id="remark" name="remark" type="text" 
			          class="easyui-validatebox  x-text"  
			          style="width:350px;height:120px">${goodsPlanQuantity.remark}</textarea>
		</td>
	</tr>
    </tbody>
  </table>
  </form>
</div>
</div>
</body>
</html>