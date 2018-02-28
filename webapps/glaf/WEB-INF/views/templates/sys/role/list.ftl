<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>角色列表</title>
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
				url:'${contextPath}/sys/role/json',
				remoteSort: false,
				singleSelect: true,
				idField:'id',
				columns:[[
	                {title:'序号',field:'startIndex',width:80,sortable:true},
					{title:'角色名称',field:'name', width:120},
					{title:'编码',field:'code', width:120},
					{title:'描述',field:'content', width:120},
					{title:'创建人',field:'createBy', width:120},
					{title:'创建日期',field:'createDate', width:120},
					{field:'functionKey',title:'功能键',width:120}
				]],
				rownumbers:false,
				pagination:true,
				pageSize: 20,
				pageList: [20,25,30,40,50,100,200,500],
				onDblClickRow: onMyRowClick 
			});
	});

		 
	function addNew(){
		var url="${contextPath}/sys/role/prepareAdd";
	    var width=450;
	    var height=280;
	    var scroll="no";
	    openWindow(url, width, height, scroll);
	}

	function onMyRowClick(rowIndex, row){
		var link='${contextPath}/sys/role/edit?id='+row.id;
		jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "修改角色",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['680px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
			});
	}

	function searchWin(){
		jQuery('#dlg').dialog('open').dialog('setTitle','角色查询');
		//jQuery('#searchForm').form('clear');
	}

	function editData(){
		var link='${contextPath}/sys/role/edit';
		jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "添加角色",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['680px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
			});
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
			//alert("请选择其中一条记录。");
			jQuery.messager.alert('Info', '请选择其中一条记录。', 'info');
			return;
		}
		var selected = jQuery('#mydatagrid').datagrid('getSelected');
		if (selected ){
		  var link='${contextPath}/sys/role/edit?id='+selected.id;
		  jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "修改角色",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['680px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
			});
		}
	}

	function roleUsers(){
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		if(rows == null || rows.length !=1){
			//alert("请选择其中一条记录。");
			jQuery.messager.alert('Info', '请选择其中一条记录。', 'info');
			return;
		}
		var selected = rows[0];
		if (selected ){
			 var link='${contextPath}/sys/role/roleUsers?roleId='+selected.id;
			 jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "角色用户设置",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['880px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
			});
		}
	}

	function roleMenus(){
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		if(rows == null || rows.length !=1){
			//alert("请选择其中一条记录。");
			jQuery.messager.alert('Info', '请选择其中一条记录。', 'info');
			return;
		}
		var selected = rows[0];
		if (selected ){
			 var link='${contextPath}/sys/role/roleMenus?roleId='+selected.id;
			 jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "角色模块设置",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['880px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
			});
		}
	}

	function editSelected2(){
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		if(rows == null || rows.length !=1){
			//alert("请选择其中一条记录。");
			jQuery.messager.alert('Info', '请选择其中一条记录。', 'info');
			return;
		}
		var selected = jQuery('#mydatagrid').datagrid('getSelected');
		if (selected ){
			 jQuery('#edit_dlg').dialog('open').dialog('setTitle','角色编辑');
			 jQuery('#editForm').form('load', '${contextPath}/sys/role/viewJson?roleId='+selected.id);
		}
	}


	function saveData(){
		var res = 'saveAdd';
		var params = jQuery("#editForm").formSerialize();
		var id = jQuery("#id").val();
		if(id != ''){
			res = 'saveModify';
		}
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/role/'+res,
				   data: params,
				   dataType:  'json',
				   error: function(data){
					   //alert('服务器处理错误！');
					   jQuery.messager.alert('Info', '服务器处理错误！', 'info');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   //alert(data.message);
						   jQuery.messager.alert('Info', data.message, 'info');
					   } else {
						   //alert('操作成功完成！');
						   jQuery.messager.alert('Info', '操作成功完成！', 'info');
					   }
					   jQuery('#mydatagrid').datagrid('reload');
					   jQuery('#edit_dlg').dialog('close');
				   }
			 });
	}

	function deleteSelections(){
		var ids = [];
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		for(var i=0;i<rows.length;i++){
			ids.push(rows[i].id);
		}
		if(ids.length > 0 && confirm("数据删除后不能恢复，确定删除吗？")){
		    var roleIds = ids.join(',');
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/role/batchDelete?roleIds='+roleIds,
				   dataType:  'json',
				   error: function(data){
					   if(data != null && data.message != null){
						   //alert(data.message);
						   jQuery.messager.alert('Info', data.message, 'info');
					   } else{
					       //alert('服务器处理错误！');
						   jQuery.messager.alert('Info', '服务器处理错误！', 'info');
					   }
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   //alert(data.message);
						   jQuery.messager.alert('Info', data.message, 'info');
					   } else {
						   //alert('操作成功完成！');
						   jQuery.messager.alert('Info', '操作成功完成！', 'info');
					   }
					   jQuery('#mydatagrid').datagrid('reload');
				   }
			 });
		} else {
			//alert("请选择至少一条记录。");
			jQuery.messager.alert('Info', '请选择至少一条记录。', 'info');
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
			ids.push(rows[i].code);
		}
		alert(ids.join(':'));
	}

	function clearSelections(){
		jQuery('#mydatagrid').datagrid('clearSelections');
	}

	function searchData(){
		var params = jQuery("#searchForm").formSerialize();
		var queryParams = jQuery('#mydatagrid').datagrid('options').queryParams;
		jQuery('#mydatagrid').datagrid('reload');	
		jQuery('#dlg').dialog('close');
	}

	function displayField(){
		var preCtr = document.getElementById("preCtr").value
        var field = document.getElementById("field").value;
        layerId = "searchForm_"+field+"_div";
		if(preCtr !=""){
           preCtr = preCtr+"_div";
           jQuery('#'+preCtr).hide();
		}
		document.getElementById("preCtr").value="searchForm_"+field;
		jQuery('#'+layerId).show();
	}
		 
</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
		<img src="${contextPath}/static/images/window.png">
		&nbsp;<span class="x_content_title">角色列表</span>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
		   onclick="javascript:editData();">新增</a>  
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
		   onclick="javascript:editSelected();">修改</a>  
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
		   onclick="javascript:deleteSelections();">删除</a> 
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-user'"
		   onclick="javascript:roleUsers();">角色用户</a>  
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-menu'"
		   onclick="javascript:roleMenus();">角色菜单</a>  
		<!-- <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-search'"
		   onclick="javascript:searchWin();">查找</a> -->
   </div> 
  </div> 
  <div data-options="region:'center',border:true">
	 <table id="mydatagrid"></table>
  </div>  
</div>

</body>
</html>
