<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SQL条件配置</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">
 
   var setting = {
			async: {
				enable: true,
				url:"${contextPath}/sys/sqlCriteria/treeJson?moduleId=${moduleId}&businessKey=${businessKey}",
				dataFilter: filter
			},
			callback: {
				onClick: zTreeOnClick
			}
		};
  
  	function filter(treeId, parentNode, childNodes) {
		if (!childNodes) return null;
		for (var i=0, l=childNodes.length; i<l; i++) {
			childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
			childNodes[i].icon="${contextPath}/static/images/basic.gif";
		}
		return childNodes;
	}


    function zTreeOnClick(event, treeId, treeNode, clickFlag) {
		jQuery("#nodeId").val(treeNode.id);
		var link = "${contextPath}/sys/sqlCriteria/json?moduleId=${moduleId}&businessKey=${businessKey}&parentId="+treeNode.id;
		loadGridData(link);
 	}

    jQuery(document).ready(function(){
		jQuery.fn.zTree.init(jQuery("#myTree"), setting);
	});


   jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:1000,
				height:480,
				fit:true,
				fitColumns: true,
				nowrap: false,
				striped: true,
				collapsible: true,
				url: '${contextPath}/sys/sqlCriteria/json?moduleId=${moduleId}&businessKey=${businessKey}',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        {title:'序号', field:'startIndex', width:60, sortable:false},
					    {title:'主题', field:'name', width:180, sortable:false},
					    {title:'参数名称', field:'paramName', width:180, sortable:false},
					    {title:'列名', field:'columnName', width:120, sortable:false},
					    {title:'创建日期', field:'createTime_date', width:90, sortable:false},
				        {title:'是否启用', field:'locked', width:90, align:"center", sortable:false, formatter:formatterActive},
					    {title:'功能键', field:'functionKey', width:120, formatter:formatterKeys }
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 10,
				pageList: [10,15,20,25,30,40,50,100],
				pagePosition: 'both'
			});

			var p = jQuery('#mydatagrid').datagrid('getPager');
			jQuery(p).pagination({
				onBeforeRefresh:function(){
					//alert('before refresh');
				}
		    });

			$('#mydatagrid').datagrid({
				onDblClickRow: function(index, row){
					 editRow(row.id);
				}
			});

	});

		 
	function addNew(){
		var nodeId = jQuery("#nodeId").val();
		link="${contextPath}/sys/sqlCriteria/edit?moduleId=${moduleId}&businessKey=${businessKey}&parentId="+nodeId+"&tableName=${tableName}";
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
			area: ['680px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function onRowClick(rowIndex, row){
	    var link = '${contextPath}/sys/sqlCriteria/edit?id='+row.id+'&moduleId=${moduleId}&businessKey=${businessKey}';
		location.href=link;
	}

	function formatterActive(val, row){
		if(val == 0){
			return "<font color='green'>启用</font>";
		} else {
            return "<font color='red'>禁用</font>";
		}
	}

	function formatterKeys(val, row){
		var str = "<a href='javascript:editRow(\""+row.id+"\");'>修改</a>&nbsp;<a href='javascript:deleteRow(\""+row.id+"\");'>删除</a>";
	    return str;
	}

	function editRow(id){
		link="${contextPath}/sys/sqlCriteria/edit?moduleId=${moduleId}&businessKey=${businessKey}&id="+id;
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

	function deleteRow(id){
		if(confirm("删除数据不能恢复，确定删除吗？")){
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/sqlCriteria/deleteById?id='+id,
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
					   //jQuery('#mydatagrid').datagrid('reload');
					   if(data.statusCode == 200){
						   window.location.reload();
					   }
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
		  var link = '${contextPath}/sys/sqlCriteria/edit?id='+selected.id;
		  editRow(selected.id);
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
        //var params = jQuery("#iForm").formSerialize();
	    jQuery.ajax({
			type: "POST",
			url:  url,
			//data: params,
			dataType:  'json',
			error: function(data){
				alert('服务器处理错误！');
			},
			success: function(data){
				jQuery('#mydatagrid').datagrid('loadData', data);
			}
		});
	}

 
  function sortSqlCriteria(){
	  var nodeId = jQuery("#nodeId").val();
	  var link = "${contextPath}/sys/sqlCriteria/showSort?moduleId=${moduleId}&businessKey=${businessKey}&parentId="+nodeId;
	  var width=680;
	  var height=430;
	  var scroll="yes";
	  jQuery.layer({
					type: 2,
					maxmin: true,
					shadeClose: true,
					title: "节点排序",
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
<body>
<input type="hidden" id="nodeId" name="nodeId" value="${nodeId}" >
<div class="easyui-layout" data-options="fit:true">  
    <div data-options="region:'west',split:true" style="width:240px;">
	  <div class="easyui-layout" data-options="fit:true">  
           
			 <div data-options="region:'center',border:false">
			    <ul id="myTree" class="ztree"></ul>  
			 </div> 
			 
        </div>  
	</div> 
   <div data-options="region:'center'"> 
	<div class="easyui-layout" data-options="fit:true">  
	  <div data-options="region:'center',border:false" class="toolbar-backgroud" style="height:42px">
	    <div style="margin:4px;"> 
			<img src="${contextPath}/static/images/window.png">
			&nbsp;<span class="x_content_title">查询条件列表</span>
			<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
			   onclick="javascript:addNew();">新增</a>  
			<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
			   onclick="javascript:editSelected();">修改</a> 
			<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-sys'"
			   onclick="javascript:sortSqlCriteria();">同级排序</a>
			<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-reload'"
			   onclick="javascript:reloadGrid();">全部</a> 
		</div> 
		<table id="mydatagrid"></table>
	  </div>  
	</div>
  </div>
</div>
</body>
</html>
