<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>物品出库单</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

   jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:1000,
				height:480,
				fit:true,
				fitColumns: true,
				nowrap: false,
				striped: true,
				collapsible: true,
				url: '${contextPath}/heathcare/goodsOutStock/stockJson?nodeId=${nodeId}',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				    {title:'序号', field:'startIndex', width:80, sortable:false},
					{title:'物品名称', field:'name', align:'left', width:350, formatter:formatterName},
					{title:'数量', field:'quantity', align:'right', width:120},
					{title:'计量单位', field:'unit', align:'center', width:120, formatter:formatterUnit},
					{title:'最近有效期', field:'expiryDate', align:'center', width:120},
					{title:'上次出库时间', field:'latestOutStockTime', align:'center', width:120},
				]],
				rownumbers: false,
				pagination: false
			});

			var p = jQuery('#mydatagrid').datagrid('getPager');
			jQuery(p).pagination({
				onBeforeRefresh:function(){
					//alert('before refresh');
				}
		    });
	});


	function formatterName(val, row){
		return "<a href='#' onclick=javascript:showName('"+row.goodsId+"')>"+val+"</a>";
	}

	function showName(id){
	    var link="${contextPath}/heathcare/foodComposition/view?id="+id;
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "查看食物成分",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['480px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function formatterUnit(val, row){
		if(val == 'KG'){
			return "千克";
		} else if(val == 'G'){
			return "克"
		} else if(val == 'L'){
			return "升"
		} else if(val == 'ML'){
			return "毫升"
		}
		return "千克";
	}

  	function doSearch(){
        document.iForm.submit();
	}

</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north',split:false,border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<form id="iForm" name="iForm" method="post" action="${contextPath}/heathcare/goodsOutStock/stocklist">
	<table>
	 <tr>
	 <td>
	    <img src="${contextPath}/static/images/window.png">
	    &nbsp;<span class="x_content_title">库存物品列表</span>
	    &nbsp;类别&nbsp;
	    <select id="nodeId" name="nodeId" onchange="javascript:doSearch();">
			<option value="">全部</option>
			<#list foodCategories as tree>
			<option value="${tree.id}">${tree.name}</option>
			</#list> 
		</select>&nbsp;
		<script type="text/javascript">
	       document.getElementById("nodeId").value="${nodeId}";
	  </script>
	   </td>
	  </tr>
	 </table>
	</form>
   </div> 
  </div> 
  <div data-options="region:'center',border:true">
	 <table id="mydatagrid"></table>
  </div>  
</div>
</div>
</body>
</html>