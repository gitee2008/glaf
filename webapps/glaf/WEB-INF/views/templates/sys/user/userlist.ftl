 <!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户列表</title>
<#include "/inc/init_easyui_layer3_import.ftl"/>
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
				url: '${contextPath}/sys/user/userjson',
				remoteSort: false,
				singleSelect: true,
				idField: 'userId',
				columns:[[
				        {title:'序号', field:'startIndex', width:60, sortable:false},
						{title:'用户名', field:'userId', width:120},
						{title:'用户姓名', field:'name', width:150, formatter:formatterUser},
						{title:'电话', field:'mobile', width:120},
						{title:'邮箱', field:'email', width:120},
						{title:'最后登录时间', field:'lastLoginTime', width:120},
						{title:'登录IP', field:'loginIP', width:120},
						{title:'最后更新时间', field:'updateDate', width:120}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 100,
				pageList: [10,15,20,25,30,40,50,100,200,500,1000],
				pagePosition: 'both' 
			});
	});

	function formatterUser(val, row){
		var str = "<a href='javascript:viewUser(\""+row.actorId_enc+"\");'>"+val+"</a>";
	    return str;
	}

	function viewUser(actorId){
		var link = '${contextPath}/user/view?actorId='+actorId;
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "查看用户信息",
		  area: ['820px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	}

    function formatterKeys(val, row){
		var str = "<a href='javascript:viewUser(\""+row.userId+"\");'>查看</a>";
	    return str;
	}
 

	function privileges(){
        var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    if(rows == null || rows.length !=1){
		    alert("请选择其中一条记录。");
		    return;
	    }
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected){
		  var link = "${contextPath}/sys/treePermission/privilege?parentId=${parentId}&type=dict&privilege=rw&userId="+selected.userId;
		  layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "字典树授权",
			  area: ['620px', (jQuery(window).height() - 50) +'px'],
			  shade: 0.8,
			  fixed: false, //不固定
			  shadeClose: true,
			  content: [link, 'no']
			});
	    }
	}
  	 
	function privileges2(){
        var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    if(rows == null || rows.length !=1){
		    alert("请选择其中一条记录。");
		    return;
	    }
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected){
		  var link = "${contextPath}/sys/district/privilege?parentId=0&type=district&privilege=rw&userId="+selected.userId;
		  layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "行政区域授权",
			  area: ['620px', (jQuery(window).height() - 50) +'px'],
			  shade: 0.8,
			  fixed: false, //不固定
			  shadeClose: true,
			  content: [link, 'yes']
			});
	    }
	}		 
</script>
</head>
<body style="margin:1px;">
<div style="margin:2;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
		<img src="${contextPath}/static/images/window.png">
		&nbsp;<span class="x_content_title">用户列表</span>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-actor'"
		   onclick="javascript:privileges();">字典树授权</a>
		&nbsp;
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-area'"
		   onclick="javascript:privileges2();">行政区域授权</a>
   </div> 
  </div> 
  <div data-options="region:'center',border:true">
	 <table id="mydatagrid"></table>
  </div>  
</div>
</body>
</html>