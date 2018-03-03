<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>物品出库单</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

	function saveData(){
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
			alert("请填写数量。");
			return;
		}

		if(document.getElementById("quantity").value*1.0 <= 0.0){
			document.getElementById("quantity").focus();
			alert("数量必须大于0。");
			return;
		}

		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/goodsOutStock/saveGoodsOutStock',
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
			alert("请填写数量。");
			return;
		}

		if(document.getElementById("quantity").value*1.0 <= 0.0){
			document.getElementById("quantity").focus();
			alert("数量必须大于0。");
			return;
		}

		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/goodsOutStock/saveGoodsOutStock',
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

   function switchFood(){
       var nodeId = document.getElementById("nodeId").value;
	   if(nodeId != ""){
		   var link = "${contextPath}/heathcare/goodsOutStock/goodsJson?nodeId="+nodeId+"&rows=1000";
		   jQuery.getJSON(link, function(data){
			  var food = document.getElementById("goodsId");
			  food.options.length=0;
			  jQuery.each(data.rows, function(i, item){
				 food.options.add(new Option(item.name+" ("+item.quantity+")", item.id));
			  });
			});
	   }
   }


   function switchFood2(selected){
       var nodeId = document.getElementById("nodeId").value;
	   if(nodeId != ""){
		   var link = "${contextPath}/heathcare/foodComposition/json?nodeId="+nodeId+"&rows=1000";
		   //alert(link);
		   jQuery.getJSON(link, function(data){
			  var food = document.getElementById("goodsId");
			  food.options.length=0;
			  var selectedIndex = 0;
			  jQuery.each(data.rows, function(i, item){
				 if(item.id == selected){
					 selectedIndex = i;
				 }
				 food.options.add(new Option(item.name, item.id));
			  });
             food.selectedIndex = selectedIndex;
			 //alert(selectedIndex);
			});
	   }
   }

   function audit(){
	   if(confirm("审核通过后不能再修改数据，确定通过吗？")){
		 var params = jQuery("#iForm").formSerialize();
		 jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/goodsOutStock/audit',
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


   function searchGoods() {
	    var link = '${contextPath}/heathcare/foodComposition/goodslist';
        var x=80;
        var y=80;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link, self, x, y, 980, 500);
	}

</script>
</head>

<body>
<div style="margin:0;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:false,border:true" style="height:40px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">编辑物品出库单</span>
	<#if audit == true>
	  <#if goodsOutStock.businessStatus == 0>
	   <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	      onclick="javascript:audit();" >确认通过</a> 
	  <#elseif goodsOutStock.businessStatus == 9>
	  &nbsp;<span style="font:bold 13px 宋体; color:#ff0000;">已出库</span>&nbsp;
	  </#if>
	<#else>
	  <#if goodsOutStock.businessStatus == 9>
	  &nbsp;<span style="font:bold 13px 宋体; color:#ff0000;">已出库</span>&nbsp;
	  <#else>
	  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	     onclick="javascript:saveData();" >保存</a> 
	  </#if>
	</#if>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${goodsOutStock.id}">
  <input type="hidden" id="goodsName" name="goodsName" value="${goodsOutStock.goodsName}">
  <table class="easyui-form" style="width:850px;" align="center">
    <tbody>
	<tr>
		<td width="15%" align="left">物品名称</td>
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
			   document.getElementById("goodsId").value="${goodsOutStock.goodsId}";
			</script>
			&nbsp;
		    <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-search'"
	           onclick="javascript:searchGoods();">查找</a>
			&nbsp;(提示：未列出库存数量为0的物品。)
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">计量单位</td>
		<td align="left">
            <select id="unit" name="unit">
			    <option value="KG" selected>千克</option>
				<option value="L">升</option>
			</select>
			<script type="text/javascript">
				document.getElementById("unit").value="${goodsOutStock.unit}";
			</script> 
			(提示：1千克=1000克 1克=0.001千克，计量单位请统一换算成千克或升)
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">数量</td>
		<td align="left">
			<input id="quantity" name="quantity" type="text" style="width:90px;text-align:right;"
			       class="easyui-numberbox  x-text" precision="1" 
				   value="${goodsOutStock.quantity}"/>
			(提示：出库数量不能超过库存数量。)
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">出库日期</td>
		<td align="left">
			<input id="outStockTime" name="outStockTime" type="text" 
			       class="easyui-datebox x-text" style="width:100px"
			       <#if goodsOutStock.outStockTime?exists>
				   value="${goodsOutStock.outStockTime?string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">备注</td>
		<td align="left">
		    <textarea id="remark" name="remark" type="text" 
			          class="easyui-validatebox  x-text"  
			          style="width:350px;height:120px">${goodsOutStock.remark}</textarea>
		</td>
	</tr>
    </tbody>
  </table>
  </form>
</div>
</div>
</body>
</html>