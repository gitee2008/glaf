<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>物品采购单</title>
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
			alert("请填写采购数量。");
			return;
		}

		if(document.getElementById("quantity").value*1.0 <= 0.0){
			document.getElementById("quantity").focus();
			alert("采购数量必须大于0。");
			return;
		}

        if(document.getElementById("price").value != ""){
			if(document.getElementById("price").value*1.0 <= 0.0){
				document.getElementById("price").focus();
				alert("采购单价必须大于0。");
				return;
			}
		} else {
			alert("采购单价必须大于0。");
			return;
		}

        if(document.getElementById("totalPrice").value != ""){
			if(document.getElementById("totalPrice").value*1.0 <= 0.0){
				document.getElementById("totalPrice").focus();
				alert("采购总价必须大于0。");
				return;
			}
		} else {
			alert("总价必须大于0。");
			return;
		}

		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/goodsPurchase/saveGoodsPurchase',
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
			alert("请填写采购数量。");
			return;
		}

		if(document.getElementById("quantity").value*1.0 <= 0.0){
			document.getElementById("quantity").focus();
			alert("采购数量必须大于0。");
			return;
		}

        if(document.getElementById("price").value != ""){
			if(document.getElementById("price").value*1.0 <= 0.0){
				document.getElementById("price").focus();
				alert("采购限定单价必须大于0。");
				return;
			}
		}

        if(document.getElementById("totalPrice").value != ""){
			if(document.getElementById("totalPrice").value*1.0 <= 0.0){
				document.getElementById("totalPrice").focus();
				alert("采购限定总价必须大于0。");
				return;
			}
		}

		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/goodsPurchase/saveGoodsPurchase',
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
				   url: '${contextPath}/heathcare/goodsPurchase/audit',
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

	function openImage(){
		var link = '${contextPath}/matrix/image/imagelist?businessKey=goods_purchase_${goodsPurchase.id}&serviceKey=goods_purchase';
		link = link+"&status_enc=${status_enc}&upload=true";
        var x=80;
        var y=80;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link, self, x, y, 980, 580);
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

</script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:5px;"> 
	<span class="x_content_title"><img src="${contextPath}/static/images/window.png">&nbsp;编辑物品采购单</span>
	<#if audit == true>
	  <#if goodsPurchase.businessStatus == 0>
	   <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	      onclick="javascript:audit();" >确认通过</a> 
	  <#elseif goodsPurchase.businessStatus == 9>
	  &nbsp;<span style="font:bold 13px 宋体; color:#ff0000;">已审核</span>&nbsp;
	  </#if>
	<#else>
	  <#if goodsPurchase.businessStatus == 9>
	  &nbsp;<span style="font:bold 13px 宋体; color:#ff0000;">已审核</span>&nbsp;
	  <#else>
	  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	     onclick="javascript:saveData();" >保存</a> 
	  <#if tenant?exists && goodsPurchase?exists && tenant.ticketFlag == "Y">
	  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_money'" 
	     onclick="javascript:openImage();" >相关票据</a> 
	  </#if>
	  </#if>
	</#if>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${goodsPurchase.id}">
  <input type="hidden" id="goodsName" name="goodsName" value="${goodsPurchase.goodsName}">
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
			   document.getElementById("goodsId").value="${goodsPurchase.goodsId}";
			</script>
			&nbsp;
		    <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-search'"
	           onclick="javascript:searchGoods();">查找</a>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">数量/重量</td>
		<td align="left">
			<input id="quantity" name="quantity" type="text" style="width:60px;text-align:right;"
			       class="easyui-numberbox x-text" precision="1" onkeyup="javascript:calMTotal();"
				   value="${goodsPurchase.quantity}"/>
		    &nbsp;&nbsp;采购单价&nbsp;&nbsp;
			<input id="price" name="price" type="text" style="width:60px;text-align:right;"
			       class="easyui-numberbox x-text" precision="2" onkeyup="javascript:calMTotal();"
				   value="${goodsPurchase.price}"/>
		    &nbsp;&nbsp;采购总价格&nbsp;&nbsp;
			<input id="totalPrice" name="totalPrice" type="text" style="width:60px;text-align:right;"
			       class="easyui-numberbox x-text" precision="2" 
				   value="${goodsPurchase.totalPrice}"/>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">包装规格</td>
		<td align="left">
            <select id="standard" name="standard">
			    <option value="1">袋</option>
				<option value="2">包</option>
				<option value="3">盒</option>
				<option value="4">桶</option>
				<option value="5">瓶</option>
			</select>
			<script type="text/javascript">
				document.getElementById("standard").value="${goodsPurchase.standard}";
			</script> 
		    &nbsp;计量单位&nbsp;
            <select id="unit" name="unit">
			    <option value="KG" selected>千克</option>
				<option value="L">升</option>
			</select>
			<script type="text/javascript">
				document.getElementById("unit").value="${goodsPurchase.unit}";
			</script> 
			&nbsp;&nbsp;(提示：计量单位请统一换算成千克或升)
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">采购日期</td>
		<td align="left">
		   <input id="purchaseTime" name="purchaseTime" type="text" 
			       class="easyui-datebox x-text" style="width:100px"
			       <#if goodsPurchase.purchaseTime>
				   value="${goodsPurchase.purchaseTime?string('yyyy-MM-dd')}"
				   </#if>
				   />
		   &nbsp;&nbsp;有效期&nbsp;
			<input id="expiryDate" name="expiryDate" type="text" 
			       class="easyui-datebox x-text" style="width:100px"
			       <#if goodsPurchase.expiryDate?exists>
				   value="${goodsPurchase.expiryDate?string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">供货单位</td>
		<td align="left" colspan="3">
			<input id="supplier" name="supplier" type="text" style="width:180px;text-align:left;"
			       class=" x-text" value="${goodsPurchase.supplier}"/>
		    &nbsp;联系方式&nbsp; 
			<input id="contact" name="contact" type="text" style="width:180px;text-align:left;"
			       class=" x-text" value="${goodsPurchase.contact}"/>
		</td>
	</tr>
    <tr>
		<td width="15%" align="left">产&nbsp;&nbsp;地</td>
		<td align="left" colspan="3">
		    <input id="address" name="address" type="text" style="width:180px;text-align:left;"
				   class=" x-text" value="${goodsPurchase.address}"/>
			&nbsp;生产批号&nbsp;
			<input id="batchNo" name="batchNo" type="text" style="width:180px;text-align:left;"
				   class=" x-text" value="${goodsPurchase.batchNo}"/>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">是否取得发票</td>
		<td align="left">
            <select id="invoiceFlag" name="invoiceFlag">
			    <option value="Y">是</option>
				<option value="N">否</option>
			</select>
			<script type="text/javascript">
				document.getElementById("invoiceFlag").value="${goodsPurchase.invoiceFlag}";
			</script> 
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">备&nbsp;&nbsp;注</td>
		<td align="left">
		    <textarea id="remark" name="remark" type="text" 
			          class="easyui-validatebox  x-text"  
			          style="width:420px; height:120px">${goodsPurchase.remark}</textarea>
		</td>
	</tr>

    <#if tenant?exists && tenant.ticketFlag == "Y">
	<tr>
		<td align="left" colspan="4" align="left">
		    <iframe id="newFrame" name="newFrame" width="100%" height="550" border="0" frameborder="0"
		          src="${contextPath}/matrix/image/imagelist?businessKey=goods_purchase_${goodsPurchase.id}&serviceKey=goods_purchase&status_enc=${status_enc}"></iframe>
		</td>
	</tr>
    </#if>

    </tbody>
  </table>
  </form>
 </div>
</div>
</body>
</html>