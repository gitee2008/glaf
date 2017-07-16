 <!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户列表</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">
   var contextPath="${contextPath}";

   jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:1000,
				height:480,
				fit:true,
				fitColumns: true,
				nowrap: false,
				striped: true,
				collapsible: true,
				url: '${contextPath}/sys/treePermission/userjson',
				remoteSort: false,
				singleSelect: true,
				idField: 'userId',
				columns:[[
				        {title:'序号', field:'startIndex', width:80, sortable:false},
						{title:'用户名', field:'userId', width:120},
						{title:'用户姓名', field:'name', width:120},
						{title:'电话', field:'mobile', width:120},
						{title:'邮箱', field:'email', width:120},
						{title:'最后登录时间', field:'lastLoginTime', width:120},
						{title:'登录IP', field:'loginIP', width:120},
						{title:'最后更新时间', field:'updateDate', width:120},
					    {title:'功能键', field:'functionKey', align:'center', width:120, formatter:formatterKeys}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 100,
				pageList: [10,15,20,25,30,40,50,100,200,500,1000],
				pagePosition: 'both',
				onDblClickRow: onMyRowClick 
			});
	});

    function formatterKeys(val, row){
		var str = "<a href='javascript:viewUser(\""+row.userId+"\");'>查看</a>";
	    return str;
	}
 
   function onMyRowClick(rowIndex, row){
	    var link = '${contextPath}/sys/treePermission/privilege?privilege=rw&userId='+row.userId;
		jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "用户权限设置",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['680px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
			});
	}

	function privileges(){
        var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    if(rows == null || rows.length !=1){
		    alert("请选择其中一条记录。");
		    return;
	    }
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected){
		  var link = "${contextPath}/sys/treePermission/privilege?parentId=${parentId}&privilege=rw&userId="+selected.userId;
		  jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "用户权限设置",
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
  	 		 
</script>
</head>
<body style="margin:1px;">
<div style="margin:2;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north',split:true,border:true" style="height:40px"> 
    <div class="toolbar-backgroud"  > 
	<img src="${contextPath}/static/images/window.png">
	&nbsp;<span class="x_content_title">用户列表</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-actor'"
	   onclick="javascript:privileges();">用户授权</a>
   </div> 
  </div> 
  <div data-options="region:'center',border:true">
	 <table id="mydatagrid"></table>
  </div>  
</div>
</body>
</html>