<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>物品入库单</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
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
			alert("请填写入库数量。");
			return;
		}

		if(document.getElementById("quantity").value*1.0 <= 0.0){
			document.getElementById("quantity").focus();
			alert("入库数量必须大于0。");
			return;
		}

        if(document.getElementById("price").value != ""){
			if(document.getElementById("price").value*1.0 <= 0.0){
				document.getElementById("price").focus();
				alert("入库单价必须大于0。");
				return;
			}
		} else {
			alert("入库单价必须大于0。");
			return;
		}

        if(document.getElementById("totalPrice").value != ""){
			if(document.getElementById("totalPrice").value*1.0 <= 0.0){
				document.getElementById("totalPrice").focus();
				alert("入库总价必须大于0。");
				return;
			}
		} else {
			alert("总价必须大于0。");
			return;
		}

		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/goodsInStock/saveGoodsInStock',
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
			alert("请填写入库数量。");
			return;
		}

		if(document.getElementById("quantity").value*1.0 <= 0.0){
			document.getElementById("quantity").focus();
			alert("入库数量必须大于0。");
			return;
		}

        if(document.getElementById("price").value != ""){
			if(document.getElementById("price").value*1.0 <= 0.0){
				document.getElementById("price").focus();
				alert("入库单价必须大于0。");
				return;
			}
		} else {
			alert("入库单价必须大于0。");
			return;
		}

        if(document.getElementById("totalPrice").value != ""){
			if(document.getElementById("totalPrice").value*1.0 <= 0.0){
				document.getElementById("totalPrice").focus();
				alert("入库总价必须大于0。");
				return;
			}
		} else {
			alert("总价必须大于0。");
			return;
		}

		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/goodsInStock/saveGoodsInStock',
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
				   url: '${contextPath}/heathcare/goodsInStock/audit',
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


	function calMTotal(){
		//alert("");
        var quantity = document.getElementById("quantity").value*1.0;
		var price = document.getElementById("price").value*1.0;
		if(quantity*1.0 > 0 && price*1.0 > 0){
			var total = quantity * price;
			//alert("total:" + total);
			//alert(document.getElementById("totalPrice").value);
			total = Math.round(total * 100.00) / 100.00;
            document.getElementById("totalPrice").value=total;
			//jQuery("#totalPrice").val(total);
			//document.getElementById("cal_totalPrice").html(total);
			//document.getElementById("totalPrice").text=total;
			//alert(document.getElementById("totalPrice").value);
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
	<img src="${contextPath}/static/images/window.png"><span class="x_content_title">&nbsp;编辑物品入库单</span>
	<#if audit == true>
	  <#if goodsInStock.businessStatus == 0>
	   <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	      onclick="javascript:audit();" >确认通过</a> 
	  <#elseif goodsInStock.businessStatus == 9>
	  &nbsp;<span style="font:bold 13px 宋体; color:#ff0000;">已审核</span>&nbsp;
	  </#if>
	<#else>
	  <#if goodsInStock.businessStatus == 9>
	  &nbsp;<span style="font:bold 13px 宋体; color:#ff0000;">已审核</span>&nbsp;
	  <#else>
	  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	     onclick="javascript:saveData();" >保存</a> 
	  </#if>
	</#if>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${goodsInStock.id}">
  <input type="hidden" id="goodsName" name="goodsName" value="${goodsInStock.goodsName}">
  <table class="easyui-form" style="width:850px;" align="center">
    <tbody>
	<tr>
		<td width="15%" align="left">物品名称</td>
		<td align="left" colspan="3">
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
			   document.getElementById("goodsId").value="${goodsInStock.goodsId}";
			</script>
			&nbsp;
		    <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-search'"
	           onclick="javascript:searchGoods();">查找</a>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">计量单位</td>
		<td align="left" colspan="3">
            <select id="unit" name="unit">
			    <option value="KG" selected>千克</option>
				<option value="L">升</option>
			</select>
			<script type="text/javascript">
				document.getElementById("unit").value="${goodsInStock.unit}";
			</script> 
			(提示：1千克=1000克 1克=0.001千克，计量单位请统一换算成千克或升)
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">数量</td>
		<td align="left" colspan="3">
			<input id="quantity" name="quantity" type="text" style="width:60px;text-align:right;"
			       class="x-small-text" precision="1" onkeyup="javascript:calMTotal();"
				   value="${goodsInStock.quantity}"/>
		    &nbsp;单价&nbsp; 
			<input id="price" name="price" type="text" style="width:60px;text-align:right;"
			       class="x-small-text" precision="2" onkeyup="javascript:calMTotal();"
				   value="${goodsInStock.price}"/>
	        &nbsp;总价格&nbsp; 
			<input id="totalPrice" name="totalPrice" type="text" style="width:80px;text-align:right;"
			       class="x-small-text" precision="2" 
				   value="${goodsInStock.totalPrice}"/>
			&nbsp;<span id="cal_totalPrice"></span>
			<input type="button" value="计算" onclick="javascript:calMTotal();" class="btnGrayMini">
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">入库日期</td>
		<td align="left" colspan="3">
			<input id="inStockTime" name="inStockTime" type="text" 
			       class="easyui-datebox x-text" style="width:100px"
			       <#if goodsInStock.inStockTime?exists>
				   value="${goodsInStock.inStockTime?string('yyyy-MM-dd')}"
				   </#if>
				   />
		    &nbsp;&nbsp;有效期&nbsp; 
			<input id="expiryDate" name="expiryDate" type="text" 
			       class="easyui-datebox x-text" style="width:100px"
			       <#if goodsInStock.expiryDate?exists>
				   value="${goodsInStock.expiryDate?string('yyyy-MM-dd')}"
				   </#if>
				   />
			&nbsp;&nbsp;包装规格&nbsp;
			<select id="standard" name="standard">
			    <option value="1">袋</option>
				<option value="2">包</option>
				<option value="3">盒</option>
				<option value="4">桶</option>
				<option value="5">瓶</option>
			</select>
			<script type="text/javascript">
				document.getElementById("standard").value="${goodsInStock.standard}";
			</script> 
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">供货单位</td>
		<td align="left" colspan="3">
			<input id="supplier" name="supplier" type="text" style="width:180px;text-align:left;"
			       class=" x-text" value="${goodsInStock.supplier}"/>
		    &nbsp;联系方式&nbsp; 
			<input id="contact" name="contact" type="text" style="width:180px;text-align:left;"
			       class=" x-text" value="${goodsInStock.contact}"/>
		</td>
	</tr>
    <tr>
		<td width="15%" align="left">产地</td>
		<td align="left" colspan="3">
		    <input id="address" name="address" type="text" style="width:180px;text-align:left;"
				   class=" x-text" value="${goodsInStock.address}"/>
			&nbsp;生产批号&nbsp;
			<input id="batchNo" name="batchNo" type="text" style="width:180px;text-align:left;"
				   class=" x-text" value="${goodsInStock.batchNo}"/>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">备注</td>
		<td align="left" colspan="3">
		    <textarea id="remark" name="remark" type="text" 
			          class="easyui-validatebox  x-text"  
			          style="width:425px;height:150px">${goodsInStock.remark}</textarea>
			<br><br>
		</td>
	</tr>
	
    </tbody>
  </table>
  </form>
</div>
</div>
</body>
</html>