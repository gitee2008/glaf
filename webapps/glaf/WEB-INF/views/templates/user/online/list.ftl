<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>在线用户</title>
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
				url: '${contextPath}/user/online/json',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        {title:'序号', field:'startIndex', width:80, sortable:false},
						{title:'用户名',field:'actorId', width:120},
						{title:'用户姓名',field:'name', width:120, formatter:formatterUser},
						{title:'登录时间',field:'loginDate_datetime', width:120},
						{title:'登录IP',field:'loginIP', width:120},
						{title:'最后更新时间',field:'checkDate_datetime', width:120},
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
 
   function onMyRowClick(rowIndex, row){
	    var link = '${contextPath}/user/view?actorId='+row.actorId_enc;
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
		var str = "<a href='javascript:viewUser(\""+row.actorId_enc+"\");'>查看</a>";
		<#if permission == "SystemAdministrator">
            str+="&nbsp;<a href='javascript:kickOut(\""+row.actorId+"\");'>注销</a>";
        </#if>
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

    function kickOut(actorId){
		if(confirm("在线用户可能正在进行业务办理，将影响用户正常使用，确定注销吗？")){
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/user/online/doKickOut?actorId='+actorId,
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
		}  
	}

	function resize(){
		jQuery('#mydatagrid').datagrid('resize', {
			width:800,
			height:400
		});
	}
  

	function doKickOut(){
		var ids = [];
		var selected = jQuery('#mydatagrid').datagrid('getSelected');
		if(selected && confirm("在线用户可能正在进行业务办理，将影响用户正常使用，确定注销吗？")){
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/user/online/doKickOut?actorId='+selected.actorId,
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
		} 
	}

	function reloadGrid(){
	    jQuery('#mydatagrid').datagrid('reload');
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

	function loadGridData(url){
        jQuery.post(url,{qq:'xx'},function(data){
            //var text = JSON.stringify(data); 
            //alert(text);
            jQuery('#mydatagrid').datagrid('loadData', data);
        },'json');
	}

    function searchData(){
		var searchWord = document.getElementById("searchWord2").value.trim();
        document.getElementById("searchWord").value = searchWord;
		var params = jQuery("#iForm").formSerialize();
        jQuery.ajax({
                    type: "POST",
                    url: '${contextPath}/user/online/json',
                    dataType:  'json',
				    data: params,
                    error: function(data){
                            alert('服务器处理错误！');
                    },
                    success: function(data){
                            jQuery('#mydatagrid').datagrid('loadData', data);
                    }
               });
	}
		 		 
</script>
</head>
<body style="margin:1px;"> 

<div style="margin:2px;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<img src="${contextPath}/static/images/window.png">
	&nbsp;<span class="x_content_title">在线用户列表</span>
	<#if permission == "SystemAdministrator">
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
	   onclick="javascript:doKickOut();">注销</a> 
	</#if>
	<input id="searchWord2" name="searchWord2" type="text" 
	       class="x-searchtext" style="width:125px;" size="20" maxlength="200"/>
    
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-search'"
	   onclick="javascript:searchData();">查找</a>
   </div> 
  </div> 
  <div data-options="region:'center',border:true">
	 <table id="mydatagrid"></table>
  </div>  
</div>
<form id="iForm" name="iForm" method="post">
   <input type="hidden" id="searchWord" name="searchWord">
</form> 
</body>
</html>
