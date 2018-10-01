<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据导出</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">
   var contextPath="${request.contextPath}";

   var setting = {
			async: {
				enable: true,
				url:"${contextPath}/matrix/xmlExport/treeJson?parentNodeId=${parentNodeId}",
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
		var link = "${contextPath}/matrix/xmlExport/json?nodeParentId="+treeNode.id;
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
				url: '${request.contextPath}/matrix/xmlExport/json',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        {title:'序号', field:'startIndex', width:80, sortable:false},
						{title:'标题', field:'title', width:280},
						{title:'XMLTag', field:'xmlTag', width:150},
						{title:'创建人', field:'createBy', width:90},
						{title:'创建时间', field:'createTime', width:90},
					    {title:'是否有效', field:'active', width:90, formatter:formatterActive},
						{title:'功能键', field:'functionKey', width:120, formatter:formatterKeys}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 20,
				pageList: [10,15,20,25,30,40,50,100],
				pagePosition: 'bottom',
				onDblClickRow: onRowClick 
			});
	});

	function formatterActive(val, row){
        var str = "";
		if(val == "Y"){
             str = "<font color='green'>有效</font>";
		} else {
             str = "<font color='red'>无效</font>";
		}
	    return str;
	}

	function formatterKeys(val, row){
		var str = "<a href='javascript:editRow(\""+row.id+"\");'>修改</a>&nbsp;<a href='javascript:subList(\""+row.id+"\");'>导出项</a>";
	    return str;
	}
	 
    function subList(expId){
	    var link = '${request.contextPath}/matrix/xmlExportItem?expId='+expId;
	    jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "同步项",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['1080px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		}); 
	}
 
	function editRow(id){
	    var link="${request.contextPath}/matrix/xmlExport/edit?id="+id;
	    jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "修改记录",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['1080px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function addNew(){
		var nodeId = jQuery("#nodeId").val();
	    var link="${request.contextPath}/matrix/xmlExport/edit?nodeParentId="+nodeId;
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
			area: ['1080px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function onRowClick(rowIndex, row){
	    var link = '${request.contextPath}/matrix/xmlExport/edit?id='+row.id;
	    jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "修改记录",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['1080px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		}); 
	}

	function searchWin(){
	    jQuery('#dlg').dialog('open').dialog('setTitle','数据导出查询');
	    //jQuery('#searchForm').form('clear');
	}

	function resize(){
		jQuery('#mydatagrid').datagrid('resize', {
			width:800,
			height:400
		});
	}

	function editSelected(){
	    var nodeId = jQuery("#nodeId").val();
        var link = '${contextPath}/matrix/xmlExport/edit?nodeId='+nodeId;
		jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "修改记录",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['980px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
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
		    var link = '${request.contextPath}/matrix/xmlExport/edit?id='+selected.id;
			jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "修改记录",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['1080px', (jQuery(window).height() - 50) +'px'],
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
				   url: '${request.contextPath}/matrix/xmlExport/delete?ids='+str,
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
					   jQuery('#mydatagrid').datagrid('reload');
				   }
			 });
		  }
		} else {
			alert("请选择至少一条记录。");
		}
	}

	function exportData(){
		var nodeId = jQuery("#nodeId").val();
        var link = '${contextPath}/matrix/xmlExport/showExport?nodeId='+nodeId;
		jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "选择导出库",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['680px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
			  });
	}

	function exportXml(expId){
		var link = '${contextPath}/matrix/xmlExport/showExport?expId='+expId;
		//window.open(link);
		jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "选择导出库",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['680px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
			  });
	}

	function showImportDef(){
		var nodeId = jQuery("#nodeId").val();
		var link = '${contextPath}/matrix/xmlExport/showImportDef?nodeId='+nodeId;
		//window.open(link);
		jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "导入定义",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['980px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
			  });
	}

	function exportDef(){
		var nodeId = jQuery("#nodeId").val();
        var link = '${contextPath}/matrix/xmlExport/exportDef?nodeId='+nodeId;
		window.open(link);
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

		 
</script>
</head>
<body style="margin:1px;">  
<input type="hidden" id="nodeId" name="nodeId" value="${nodeId}" >
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'west',split:true" style="width:385px;">
	  <div class="easyui-layout" data-options="fit:true">  
           
			 <div data-options="region:'center',border:false">
			    <ul id="myTree" class="ztree"></ul>  
			 </div> 
			 
        </div>  
	</div> 
   <div data-options="region:'north', split:false, border:true" style="height:40px;" class="toolbar-backgroud"> 
    <div style="margin-top:4px;"> 
		<img src="${request.contextPath}/static/images/window.png">
		&nbsp;<span class="x_content_title">XML导出节点列表</span>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
		   onclick="javascript:addNew();">新增</a>  
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
		   onclick="javascript:editSelected();">修改</a>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-exp'"
		   onclick="javascript:exportData();">导出数据</a>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-exp'"
		   onclick="javascript:exportDef();">导出定义</a>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-imp'"
		   onclick="javascript:showImportDef();">导入定义</a>
   </div> 
  </div> 
  <div data-options="region:'center',border:true">
	 <table id="mydatagrid"></table>
  </div>  
</div>
</body>
</html>
