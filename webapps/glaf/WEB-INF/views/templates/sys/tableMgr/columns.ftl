<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>字段列表</title>
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
				url: '${contextPath}/sys/tableMgr/columnsJson?tableId=${tableId}',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				    {title:'序号', field:'startIndex', align:'left', width:60, sortable:false},
					{title:'标题', field:'title', align:'left', width:180},
				    {title:'字段名', field:'columnName', align:'left', width:180},
					{title:'数据类型', field:'javaType', align:'center', width:100, formatter:formatterType},
					{title:'字段长度', field:'length', align:'right', width:100, formatter:formatterLength},
					{title:'取数位置', field:'colIndex', align:'right', width:90},
					{title:'启用', field:'locked', align:'center', width:80, formatter:formatterLocked},
					{title:'功能键', field:'functionKey', align:'left', width:120, formatter:formatterKeys}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 100,
				pageList: [10,15,20,25,30,40,50,100],
				pagePosition: 'both',
				onDblClickRow: onMyRowClick 
			});

			var p = jQuery('#mydatagrid').datagrid('getPager');
			jQuery(p).pagination({
				onBeforeRefresh:function(){
					//alert('before refresh');
				}
		    });
	});

   function formatterLength(val, row){
	   if(row.javaType == "String"){
		if(val == 0){
			return "默认";
		}  
		return val;
	   }
	   return "";
	}

	function formatterLocked(val, row){
		if(val == 1){
			return "<font color='red'>无效</font>";
		}
		return "<font color='green'>有效</font>";
	}

	function formatterType(val, row){
		if(val == "Integer"){
			return "<font color='#0066ff'>整数型</font>";
		} else if(val == "Long"){
			return "<font color='#0066ff'>长整数型</font>";
		} else if(val == "Double"){
			return "<font color='#0066ff'>数值型</font>";
		} else if(val == "Date"){
            return "<font color='#222222'>日期型</font>";
		} else if(val == "Clob"){
            return "<font color='#222233'>长文本型</font>";
		} else {
            return "<font color='#222233'>字符串型</font>";
		}
	}


   function formatterKeys(val, row){
		var str = "<a href='javascript:editRow(\""+row.id+"\");'><img src='${contextPath}/static/images/edit.gif' border='0'>修改</a>&nbsp;<a href='javascript:deleteRow(\""+row.id+"\");'><img src='${contextPath}/static/images/remove.png' border='0'>删除</a>";
	    return str;
	}

		 
	function addNew(){
	    var link="${contextPath}/sys/tableMgr/editColumn?tableId=${tableId}";
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
			area: ['780px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

    
	function editRow(id){
		link="${contextPath}/sys/tableMgr/editColumn?tableId=${tableId}&id="+id;
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
			area: ['780px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function onMyRowClick(rowIndex, row){
	    var link = '${contextPath}/sys/tableMgr/editColumn?tableId=${tableId}&id='+row.id;
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
			area: ['780px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function deleteRow(id){
		if(confirm("删除数据不能恢复，确定删除吗？")){
			if(confirm("该表相关的列定义数据都将删除且不能恢复，确实要删除吗？")){
			  jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/tableMgr/deleteColumn?tableId=${tableId}&id='+id,
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
					   if(data.statusCode == 200){
						   //window.location.reload();
						   jQuery('#mydatagrid').datagrid('reload');
					   }
				   }
			 });
			}
		}
	}

	function searchWin(){
	    jQuery('#dlg').dialog('open').dialog('setTitle','查询');
	    //jQuery('#searchForm').form('clear');
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
		  var link = '${contextPath}/sys/tableMgr/editColumn?tableId=${tableId}&id='+selected.id;
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
			area: ['780px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
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
		    var link='${contextPath}/sys/tableMgr/editColumn?tableId=${tableId}&id='+selected.id;
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
			area: ['780px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		   });
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
				   url: '${contextPath}/sys/tableMgr/deleteColumn?tableId=${tableId}&ids='+str,
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
					   if(data.statusCode == 200){
					       jQuery('#mydatagrid').datagrid('reload');
					   }
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
                    url: '${contextPath}/sys/tableMgr/columnsJson?tableId=${tableId}',
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

  function sortColumns(){
        link="${contextPath}/sys/tableMgr/showSort?tableId=${tableId}";
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "字段排序",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['680px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
    }		 
</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
		<img src="${contextPath}/static/images/window.png">
		&nbsp;<span class="x_content_title">字段列表</span>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
		   onclick="javascript:addNew();">新增</a>  
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
		   onclick="javascript:editSelected();">修改</a>  
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
		   onclick="javascript:deleteSelections();">删除</a> 
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-sort'"
		   onclick="javascript:sortColumns();">排序</a>
		<!-- <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-search'"
		   onclick="javascript:searchWin();">查找</a> -->
   </div> 
  </div> 
  <div data-options="region:'center',border:true">
	 <table id="mydatagrid"></table>
  </div>  

  <form id="iForm" name="iForm" method="post" action="">
	<input type="hidden" id="expression" name="expression" value="">
  </form>
</div>
</div>
</body>
</html>