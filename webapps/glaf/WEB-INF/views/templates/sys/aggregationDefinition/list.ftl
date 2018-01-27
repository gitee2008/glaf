<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇总定义</title>
<#include "/inc/init_easyui_layer3_import.ftl"/>
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
				url: '${contextPath}/sys/aggregationDefinition/json?serviceKey=${serviceKey}',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        {title:'序号', field:'startIndex', width:80, sortable:false},
						{title:'主题', field:'title', width:180},
						{title:'服务标识', field:'serviceKey', width:120},
						{title:'统计项名称', field:'name', width:120},
						{title:'目标表名称', field:'targetTableName', width:150},
						{title:'类型', field:'type', width:120},
						{title:'功能键', field:'functionKey', width:120, formatter:formatterKeys}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 10,
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


	function formatterKeys(val, row){
		var str = "<a href='javascript:editRow(\""+row.id+"\");'>修改</a>&nbsp;<a href='javascript:deleteRow(\""+row.id+"\");'>删除</a>";
	    return str;
	}
	

	function addNew(){
	    var link="${contextPath}/sys/aggregationDefinition/edit";
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "新增记录",
		  area: ['680px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	}


	function onMyRowClick(rowIndex, row){
	    var link = '${contextPath}/sys/aggregationDefinition/edit?id='+row.id;
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "编辑记录",
		  area: ['680px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	}

    function editRow(id){
	    var link = '${contextPath}/sys/aggregationDefinition/edit?id='+id;
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "编辑记录",
		  area: ['680px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	}

	
	function deleteRow(id){
		if(confirm("数据删除后不能恢复，确定删除吗？")){
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/aggregationDefinition/delete?id='+id,
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
	}

	
	function onRowClick(rowIndex, row){
	    var link = '${contextPath}/sys/aggregationDefinition/edit?id='+row.id;
	    layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "编辑记录",
		  area: ['680px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	}

	function searchWin(){
	    jQuery('#dlg').dialog('open').dialog('setTitle','汇总定义查询');
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
		  var link = '${contextPath}/sys/aggregationDefinition/edit?id='+selected.id;
		  layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "编辑记录",
			  area: ['680px', (jQuery(window).height() - 50) +'px'],
			  shade: 0.8,
			  fixed: false, //不固定
			  shadeClose: true,
			  content: [link, 'no']
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
		    var link='${contextPath}/sys/aggregationDefinition/edit?readonly=true&id='+selected.id;
		    layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "编辑记录",
			  area: ['680px', (jQuery(window).height() - 50) +'px'],
			  shade: 0.8,
			  fixed: false, //不固定
			  shadeClose: true,
			  content: [link, 'no']
			});
		}
	}

    function sortProject(){
	  var serviceKey = jQuery("#serviceKey").val();
	  if(serviceKey != ""){
	    var link = "${contextPath}/sys/aggregationDefinition/showSort?serviceKey="+serviceKey;
	    layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "节点排序",
			  area: ['680px', (jQuery(window).height() - 50) +'px'],
			  shade: 0.8,
			  fixed: false, //不固定
			  shadeClose: true,
			  content: [link, 'no']
			});
	  } else {
         alert("请选择其中一个分类。");
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
				   url: '${contextPath}/sys/aggregationDefinition/delete?ids='+str,
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
                    url: '${contextPath}/sys/aggregationDefinition/json',
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

	function switchXY(){
		document.getElementById("iForm").submit();
	}
		 
</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
		<form id="iForm" name="iForm" method="post" action="">
			<table>
			  <tr>
				<td>
					<img src="${contextPath}/static/images/window.png">
					&nbsp;<span class="x_content_title">汇总定义列表</span>
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
					   onclick="javascript:addNew();">新增</a>  
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
					   onclick="javascript:editSelected();">修改</a>  
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
					   onclick="javascript:deleteSelections();">删除</a> 
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-sort'"
					   onclick="javascript:sortProject();">同级排序</a>
				</td>
				<td>
				   <select id="serviceKey" name="serviceKey" onchange="javascript:switchXY();">
						<option value="">----请选择----</option>
						<#list dictoryList as d>
						<option value="${d.code}">${d.name}</option>
						</#list> 
					</select>
					<script type="text/javascript">
					   document.getElementById("serviceKey").value="${serviceKey}";
					</script>
				</td>
			  </tr>
			</table>
		</form>
   </div> 
  </div> 
  <div data-options="region:'center',border:true">
	 <table id="mydatagrid"></table>
  </div>  
</div>
</div>
</body>
</html>