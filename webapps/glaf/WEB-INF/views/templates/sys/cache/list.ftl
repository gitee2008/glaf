<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>缓存管理</title>
<#include "/inc/init_easyui_import.ftl"/>
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
				url:'${contextPath}/sys/cacheMgr/json',
				remoteSort: false,
				singleSelect:true,
				idField:'key',
				columns:[[
	                {title:'序号', field:'index', width:60, sortable:true},
					{title:'区域', field:'region', width:120, sortable:true},
					{title:'名称', field:'name', width:280, sortable:true},
					{title:'大小（字节）', field:'size', width:90, sortable:true},
					{title:'创建日期', field:'date', width:120, sortable:true}
				]],
				rownumbers:false,
				pagination:false
			});

			var p = jQuery('#mydatagrid').datagrid('getPager');
			jQuery(p).pagination({
				onBeforeRefresh:function(){
					//alert('before refresh');
				}
		    });
	});

 

	function resize(){
		jQuery('#mydatagrid').datagrid('resize', {
			width:800,
			height:400
		});
	}

	function viewSelected(){
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		if(rows == null || rows.length !=1){
			alert("请选择其中一条记录。");
			return;
		}
		var selected = jQuery('#mydatagrid').datagrid('getSelected');
		if (selected ){
			//location.href="${contextPath}/sys/cacheMgr/view?key="+selected.name;
			//alert('${contextPath}/sys/cacheMgr/detail?key='+selected.name+'&region='+selected.region);
            jQuery.ajax({
				   type: "GET",
				   url: '${contextPath}/sys/cacheMgr/detail?key='+selected.name+'&region='+selected.region,
				   dataType:  'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data.value != null){
						   alert(data.value);
					   } else {
						   alert('不能取得缓存值！');
					   }
				   }
			 });
		}
	}

	function deleteSelections(){
		var ids = [];
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		for(var i=0;i<rows.length;i++){
			ids.push(rows[i].key);
		}
		if(ids.length > 0 && confirm("确定删除吗？")){
		    var rowIds = ids.join(',');
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/cacheMgr/delete?keys='+rowIds,
				   dataType:  'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data.message != null){
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

	function getSelected(){
		var selected = jQuery('#mydatagrid').datagrid('getSelected');
		if (selected){
			alert(selected.code+":"+selected.name+":"+selected.addr+":"+selected.col4);
		}
	}

	function getSelections(){
		var ids = [];
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		for(var i=0;i<rows.length;i++){
			ids.push(rows[i].key);
		}
		alert(ids.join(':'));
	}

	function clearCache(){
		if(confirm("确定清空缓存吗？")){
            location.href="${contextPath}/sys/cacheMgr/clearAll";
		}
	}

 
</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
		<img src="${contextPath}/static/images/window.png">
		&nbsp;<span class="x_content_title">缓存信息列表</span>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-view'"
		   onclick="javascript:viewSelected();">查看</a>  
		<!-- <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
		   onclick="javascript:deleteSelections();">删除</a> --> 
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-clear'"
		   onclick="javascript:clearCache();">清空</a> 
   </div> 
  </div> 
  <div data-options="region:'center',border:true">
	 <table id="mydatagrid"></table>
  </div>  
</div>

</body>
</html>
