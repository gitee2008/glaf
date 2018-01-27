<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>信息列表</title>
<#include "/inc/init_easyui_layer3_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">
 
    jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:1000,
				height:480,
				fit:true,
				fitColumns:true,
				nowrap: false,
				striped: true,
				collapsible:true,
				url:'${contextPath}/base/info/json?serviceKey=${serviceKey}',
				remoteSort: false,
				singleSelect:true,
				idField:'id',
				columns:[[
	                {title:'序号', field:'startIndex', width:60,sortable:false},
					{title:'主题', field:'subject', width:350,sortable:false},
					{title:'发布单位', field:'unitName', width:120,sortable:false},
					{title:'发布日期', field:'createDate', width:90,sortable:false},
					{title:'浏览次数', field:'viewCount', width:90,sortable:false},
				]],
				rownumbers:false,
				pagination:true,
				pageSize: 10,
				pageList: [10,15,20,25,30,40,50,100],
				onClickRow: onRowClick 
			});
	});


	function onRowClick(rowIndex, row){
	    var link = '${contextPath}/base/info/view?id='+row.id;
	    var x=20;
	    var y=20;
		if(is_ie) {
			x=document.body.scrollLeft+event.clientX-event.offsetX-200;
			y=document.body.scrollTop+event.clientY-event.offsetY-200;
		}
		openWindow(link, self, x, y, 1280, 650);
	}

	function searchWin(){
	    jQuery('#dlg').dialog('open').dialog('setTitle','信息查询');
	   
	}

	function resize(){
		jQuery('#mydatagrid').datagrid('resize', {
			width:800,
			height:400
		});
	}

	function editSelected(){
	    var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    if(rows == null || rows.length !=1){
		  alert("请选择其中一条记录。");
		  return;
	    }
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected ){
		    var link = "${contextPath}/base/info/view?id="+selected.id;
	        var x=20;
			var y=20;
			if(is_ie) {
				x=document.body.scrollLeft+event.clientX-event.offsetX-200;
				y=document.body.scrollTop+event.clientY-event.offsetY-200;
			}
			openWindow(link, self, x, y, 1280, 650);
	    }
	}


	function viewSelected(){
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		if(rows == null || rows.length !=1){
			alert("请选择其中一条记录。");
			return;
		}
		var selected = jQuery('#mydatagrid').datagrid('getSelected');
		if (selected){
		    var link ="${contextPath}/base/info/view?id="+selected.id;
	        var x=20;
			var y=20;
			if(is_ie) {
				x=document.body.scrollLeft+event.clientX-event.offsetX-200;
				y=document.body.scrollTop+event.clientY-event.offsetY-200;
			}
			openWindow(link, self, x, y, 1280, 650);
		}
	}

 
	function reloadGrid(){
	    jQuery('#mydatagrid').datagrid('reload');
	}


	function searchData(){
	 
	}
		 
</script>
</head>
<body style="margin:1px;">  
<input type="hidden" id="nodeId" name="nodeId" value="" >
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'center'">  
     <div class="easyui-layout" data-options="fit:true"> 
	   <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
		<div style="margin:4px;"> 
			&nbsp;<img src="${contextPath}/static/images/window.png">
			&nbsp;<span class="x_content_title">信息列表</span>
			<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-view'"
			   onclick="javascript:editSelected();">查看</a> 
	   </div> 
	  </div> 
	  <div data-options="region:'center',border:true">
		 <table id="mydatagrid"></table>
	  </div>  
    </div>
  </div>
</div>
</body>
</html>