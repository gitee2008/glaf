<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>信息管理</title>
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
				url:'${contextPath}/base/infoMgr/json?serviceKey=${serviceKey}',
				remoteSort: false,
				singleSelect:true,
				idField:'id',
				columns:[[
	                {title:'序号', field:'startIndex', width:60,sortable:false},
					{title:'主题', field:'subject', width:350,sortable:false},
					{title:'发布单位', field:'unitName', width:120,sortable:false},
					{title:'发布日期', field:'createDate', width:90,sortable:false},
					{title:'发布状态', field:'publishFlag', width:90,sortable:false, formatter:formatterPublishFlag},
					{title:'浏览次数', field:'viewCount', width:90,sortable:false},
				]],
				rownumbers:false,
				pagination:true,
				pageSize: 10,
				pageList: [10,15,20,25,30,40,50,100],
				onDblClickRow: onRowClick 
			});
	});

	function formatterPublishFlag(val, row){
         if(val == 1){
			  return '<span style="color:green; font: bold 13px 宋体; ">已发布</span>';
		 }
		 return "未发布";
	}

	function addNew(){
		var nodeId = jQuery("#nodeId").val();
		var link="${contextPath}/base/infoMgr/edit?nodeId="+nodeId;
	    var x=20;
        var y=20;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link, self, x, y, 980, 580);
	}

	function onRowClick(rowIndex, row){
	    var link = '${contextPath}/base/infoMgr/edit?id='+row.id;
		var x=20;
        var y=20;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link, self, x, y, 980, 580);
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
		    var link = "${contextPath}/base/infoMgr/edit?id="+selected.id;
	        var x=20;
			var y=20;
			if(is_ie) {
				x=document.body.scrollLeft+event.clientX-event.offsetX-200;
				y=document.body.scrollTop+event.clientY-event.offsetY-200;
			}
			openWindow(link, self, x, y, 980, 580);
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
			openWindow(link, self, x, y, 980, 580);
		}
	}


	function deleteSelections(){
		var ids = [];
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		for(var i=0;i<rows.length;i++){
			ids.push(rows[i].id);
		}
		if(ids.length > 0 && confirm("数据删除后不能恢复，确定删除吗？")){
		    var str = ids.join(',');
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/base/infoMgr/delete?ids='+str,
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
					   jQuery('#mydatagrid').datagrid('reload');
				   }
			 });
		} else {
			alert("请选择至少一条记录。");
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
<input type="hidden" id="rowId" name="rowId" value="" >
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'center'">  
     <div class="easyui-layout" data-options="fit:true"> 
	   <div data-options="region:'north', split:false, border:true" style="height:38px" class="toolbar-backgroud"> 
		<div style="margin:4px;"> 
			&nbsp;<img src="${contextPath}/static/images/window.png">
			&nbsp;<span class="x_content_title">信息列表</span>
			<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
			   onclick="javascript:addNew();">新增</a>  
			<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
			   onclick="javascript:editSelected();">修改</a> 
			<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
			   onclick="javascript:deleteSelections();">删除</a> 
			<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-view'"
			   onclick="javascript:viewSelected();">预览</a> 
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
