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
				url: '${contextPath}/tenant/user/json?tenantId=${tenantId}',
				remoteSort: false,
				singleSelect: true,
				idField: 'userId',
				columns:[[
				        {title:'序号', field:'startIndex', width:80, sortable:false},
						{title:'登录账号',field:'userId', width:120},
						{title:'用户姓名',field:'name', width:120},
						{title:'电话',field:'mobile', width:120},
						{title:'邮箱',field:'email', width:120},
						{title:'最后登录时间',field:'lastLoginTime', width:120},
						{title:'登录IP',field:'loginIP', width:120},
						{title:'最后更新时间',field:'updateDate', width:120},
					    {title:'功能键', field:'functionKey', align:'center', width:120, formatter:formatterKeys}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 10,
				pageList: [10,15,20,25,30,40,50,100,200,500,1000],
				pagePosition: 'both',
				onDblClickRow: onMyRowClick 
			});
	});

    function formatterKeys(val, row){
		var str = "<a href='javascript:viewUser(\""+row.userId_enc+"\");'>查看</a>";
	    return str;
	}
 
   function onMyRowClick(rowIndex, row){
	    var link = '${contextPath}/tenant/user/prepareModify?userId='+row.userId_enc;
		jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "修改用户",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['680px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
			});
	}

  function addNew(){
	    var link="${contextPath}/tenant/user/prepareAdd?tenantId=${tenantId}";
		jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "添加用户",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['680px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
			});
	}

	function viewUser(actorId){
		var link = '${contextPath}/user/view?actorId='+actorId;
		jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "查看用户",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['680px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
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
		  var link = "${contextPath}/tenant/user/prepareModify?userId="+selected.userId_enc;
		  jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "修改用户",
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

    function resetPwd(){
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    if(rows == null || rows.length !=1){
		    alert("请选择其中一条记录。");
		    return;
	    }
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected ){
		  var link = "${contextPath}/tenant/user/prepareResetPwd?userId="+selected.userId_enc;
		  jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "重置用户密码",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['580px', (jQuery(window).height() - 150) +'px'],
				iframe: {src: link}
			});
	    }
	}

	function resize(){
		jQuery('#mydatagrid').datagrid('resize', {
			width:800,
			height:400
		});
	}
 

	 function userRoles(){
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    if(rows == null || rows.length !=1){
		    alert("请选择其中一条记录。");
		    return;
	    }
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected ){
		  var link = "${contextPath}/tenant/user/showRole?userId="+selected.userId_enc;
		  jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "用户角色设置",
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
		 		 
</script>
</head>
<body style="margin:1px;"> 

<div style="margin:2;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
		<img src="${contextPath}/static/images/window.png">
		&nbsp;<span class="x_content_title">用户列表</span>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
		   onclick="javascript:addNew();">新增</a>  
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
		   onclick="javascript:editSelected();">修改</a>  
		<!-- <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
		   onclick="javascript:deleteSelections();">删除</a> -->
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-pwd'"
		   onclick="javascript:resetPwd();">重置密码</a>  
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-actor'"
		   onclick="javascript:userRoles();">用户角色</a>
   </div> 
  </div> 
  <div data-options="region:'center',border:true">
	 <table id="mydatagrid"></table>
  </div>  
</div>
</body>
</html>
