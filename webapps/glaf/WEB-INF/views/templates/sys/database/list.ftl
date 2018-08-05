<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据库设置</title>
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
				url: '${contextPath}/sys/database/json?nodeId=${nodeId}&keywordsLike_base64=${keywordsLike_base64}',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
					    {title:'编号', field:'id', width:60, sortable:false},
					    {title:'主题', field:'title', width:150, sortable:false},
					    {title:'别名', field:'mapping', width:90, sortable:false},
					    {title:'主机', field:'host', width:240, sortable:false},
					    {title:'端口', field:'port', width:60, sortable:false},
					    {title:'库名', field:'dbname', width:120, sortable:false},
					    {title:'数据库类型', field:'type', width:90, sortable:false},
					    {title:'是否验证', field:'verify', width:90, sortable:false, formatter:formatterVerify},
					    {title:'是否有效', field:'active', width:90, sortable:false, formatter:formatterActive},
					    {field:'functionKey',title:'功能键',width:130, formatter:formatterKeys }
				]],
				rownumbers: true,
				pagination: true,
				pageSize: 10,
				pageList: [10,15,20,25,30,40,50,100,200,500,1000],
				pagePosition: 'bottom'
			});

			var p = jQuery('#mydatagrid').datagrid('getPager');
			jQuery(p).pagination({
				onBeforeRefresh:function(){
					
				}
		    });
	});

		 
	function addNew(){
		link="${contextPath}/sys/database/edit?nodeId=${nodeId}";
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "新增记录",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['880px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function onRowClick(rowIndex, row){
	    var link = '${contextPath}/sys/database/edit?id='+row.id+'&fromUrl=${fromUrl}';
		location.href=link;
	}

	function formatterLevel(val, row){
		if(val == 1){
			return "主库";
		} else {
            return "从库";
		}
	}

	function formatterActive(val, row){
        if(val == '1'){
			return "<font color='green'><b>有效</b></font>";
		} else {
            return "<font color='red'><b>无效</b></font>";
		}
	}

	function formatterVerify(val, row){
        if(val == 'Y'){
			return "<font color='green'><b>通过</b></font>";
		} else {
            return "<font color='red'><b>未通过</b></font>";
		}
	}

	function formatterKeys(val, row){
		var str = "<a href='javascript:editRow(\""+row.id+"\");'>修改</a>&nbsp;<a href='javascript:deleteRow(\""+row.id+"\");'>删除</a>&nbsp;<a href='javascript:verify(\""+row.id+"\");'>验证</a>&nbsp;<a href='javascript:exprtDB(\""+row.name+"\");'>导出</a>";
	    return str;
	}

	function initDB(id){
		if(confirm("初始化要执行很长一段时间，确定要初始化该库吗？")){
		  jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/database/initDB?id='+id,
				   dataType:  'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } else {
						   alert(data.message);
					   } 
				   }
			 });  
		}
	}

	function exprtDB(name){
		if(confirm("导出要执行很长一段时间，确定要导出该库到SQLite吗？")){
		   window.open('${contextPath}/sys/tableMgr/exportDB?systemName='+name);
		}
	}

	function dataList(id){
       var link="${contextPath}/database/data?databaseId="+id;
	   location.href=link;
	}

	function editRow(id){
		link="${contextPath}/sys/database/edit?id="+id;
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "编辑记录",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['880px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function searchWin(){
	    jQuery('#dlg').dialog('open').dialog('setTitle','数据库查询');
	}

	function resize(){
		jQuery('#mydatagrid').datagrid('resize', {
			width:800,
			height:400
		});
	}

	function deleteRow(id){
		if(confirm("删除数据库配置将同时删除历史统计结果，删除后数据不能恢复，确定删除吗？")){
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/rs/sys/database/delete?id='+id,
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

	function editSelected(){
	    var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    if(rows == null || rows.length !=1){
		  alert("请选择其中一条记录。");
		  return;
	    }
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected ){
		  var link = '${contextPath}/sys/database/edit?id='+selected.id+'&fromUrl=${fromUrl}';
		  editRow(selected.id);
	    }
	}

	function viewSelected(){
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		if(rows == null || rows.length !=1){
			alert("请选择其中一条记录。");
			return;
		}
		var selected = jQuery('#mydatagrid').datagrid('getSelected');
		if (selected ){
		    location.href='${contextPath}/sys/database/edit?readonly=true&id='+selected.id+'&fromUrl=${fromUrl}';
		}
	}

	function deleteSelections(){
		var ids = [];
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		for(var i=0;i<rows.length;i++){
			ids.push(rows[i].id);
		}
		if(ids.length > 0 ){
		  if(confirm("数据删除后不能恢复，确定删除吗？")){
		    var str = ids.join(',');
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/database/delete?ids='+str,
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
		} else {
			alert("请选择至少一条记录。");
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


	function updateHibernateDDL(){
		var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected){
		  if(confirm("确定更新数据库物理结构？")){
		     jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/table/updateHibernateDDL?systemName='+selected.name,
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
				   }
			 });
		  }
		}
	}

	function updateBizSchema(){
		var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected){
		  if(confirm("确定创建业务数据库的表结构吗？")){
		     jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/dietaryConfig/updateSchema?databaseId='+selected.id,
				   dataType: 'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } else {
						   alert('操作成功完成！');
					   }
				   }
			 });
		  }
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
	    jQuery.ajax({
			type: "POST",
			url:  url,
			dataType:  'json',
			error: function(data){
				alert('服务器处理错误！');
			},
			success: function(data){
				jQuery('#mydatagrid').datagrid('loadData', data);
			}
		});
	}

	function searchData(){
        var params = jQuery("#searchForm").formSerialize();
        jQuery.ajax({
                    type: "POST",
                    url: '${contextPath}/sys/database/json?nodeId=${nodeId}',
                    dataType:  'json',
                    data: params,
                    error: function(data){
                              alert('服务器处理错误！');
                    },
                    success: function(data){
                              jQuery('#mydatagrid').datagrid('loadData', data);
                    }
                  });

	    jQuery('#dlg').dialog('close');
	}
	
    function verify(id){
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/database/verify2?id='+id,
				   dataType:  'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } else {
						   alert("操作成功！");
					   } 
				   }
			 });  
	}

	function reloadDB(){
	  if(confirm("确定重载配置数据库吗？")){
         jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/database/reloadDB',
				   dataType:  'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } else {
						   alert("操作成功！");
					   } 
				   }
			 });  
	  }
	}

	function perm(){
		location.href="${contextPath}/sys/database/permission";
	}

	function showSort(){
		var link = "${contextPath}/sys/database/showSort";
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "数据库排序",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['580px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function showServerRef(){
			var link = "${contextPath}/sys/database/showServerRef";
			jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "服务器关联",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['880px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
			});
		}


    function searchData(){
        document.iForm.submit();
	}	
	
	function verifyAll(){
		var link = "${contextPath}/sys/database/verifyAll";
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "数据库验证",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['780px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north', split:false, border:true" style="height:42px;" class="toolbar-backgroud"> 
    <div style="margin-top:4px;"> 
		<form id="iForm" name="iForm" method="post" action="${contextPath}/sys/database">
		&nbsp;<img src="${contextPath}/static/images/window.png">
		&nbsp;<span class="x_content_title">数据库列表</span>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
		   onclick="javascript:addNew();">新增</a>  
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
		   onclick="javascript:editSelected();">修改</a>  
		<!-- <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-sort'"
		   onclick="javascript:showSort();">排序</a> -->
		<!-- <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-role'"
		   onclick="javascript:perm();">授权</a> -->
		<!-- <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-ok'"
		   onclick="javascript:verifyAll();">立即验证</a> -->
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-reload'"
		   onclick="javascript:reloadDB();">重载配置</a>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-package'"
		   onclick="javascript:updateHibernateDDL();">更新数据库结构</a>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-sys'"
		   onclick="javascript:updateBizSchema();">更新业务数据表</a>
		<input id="keywordsLike" name="keywordsLike" type="text" class="x-searchtext"  
			   style="width:185px;" value="${keywordsLike}">
		<button type="button" id="searchButton" class="btn btnGrayMini" style="width: 90px" 
				onclick="javascript:searchData();">查找</button>
		</form> 
   </div> 
  </div> 
  <div data-options="region:'center',border:true">
	 <table id="mydatagrid"></table>
	 <br/>
  </div>  
</div>
<br/>
<br/>
<br/>
<br/>
</body>
</html>
