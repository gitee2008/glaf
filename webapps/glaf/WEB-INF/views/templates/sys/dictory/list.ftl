<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据字典</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

    var prevTreeNode;

    var setting = {
			async: {
				enable: true,
				url:"${contextPath}/sys/tree/treeJson?nodeCode=011",
				dataFilter: filter
			},
			callback: {
				onExpand: zTreeOnExpand,
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

	function zTreeOnExpand(treeId, treeNode){
		var zTree1 = $.fn.zTree.getZTreeObj("myTree");
        treeNode.icon="${contextPath}/static/scripts/ztree/css/zTreeStyle/img/diy8.png";
		if(prevTreeNode){
			prevTreeNode.icon="${contextPath}/static/scripts/ztree/css/zTreeStyle/img/diy/2.png";
			zTree1.updateNode(prevTreeNode);
		}
		
		zTree1.updateNode(treeNode);
		prevTreeNode = treeNode; 
	}

    function zTreeOnClick(event, treeId, treeNode, clickFlag) {
		jQuery("#nodeId").val(treeNode.id);
		reloadGrid();
		//loadData('${contextPath}/sys/dictory/json?nodeId='+treeNode.id);
	}

	function loadData(url){
		  jQuery.get(url,{qq:'xx'},function(data){
		      //var text = JSON.stringify(data); 
              //alert(text);
			  jQuery('#mydatagrid').datagrid('loadData', data);
		  },'json');
	  }

    jQuery(document).ready(function(){
			jQuery.fn.zTree.init(jQuery("#myTree"), setting);
	});

   jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:1000,
				height:480,
				fit:true,
				fitColumns:true,
				nowrap: false,
				striped: true,
				collapsible:true,
				url:'${contextPath}/sys/dictory/json?nodeId=',
				remoteSort: false,
				singleSelect:true,
				idField:'id',
				columns:[[
	                {title:'序号',field:'startIndex',width:80, sortable:false},
					{title:'名称',field:'name', width:150, sortable:true},
					{title:'代码',field:'code', width:150, sortable:true},
					{title:'属性值',field:'value', width:120, sortable:true},
					{title:'描述',field:'desc', width:180},
					{title:'是否有效',field:'locked', width:90, formatter:formatterStatus}
				]],
				rownumbers:false,
				pagination:true,
				pageSize: 10,
				pageList: [10,15,20,25,30,40,50,100,200,500],
				onDblClickRow: onRowClick 
			});
	});


		 
	function formatterStatus(val, row){
       if(val == 0){
			return '<span style="color:green; font: bold 13px 宋体;">是</span>';
	   } else  {
			return '<span style="color:red; font: bold 13px 宋体;">否</span>';
	   }  
	}

	function reloadGrid(){
		jQuery('#mydatagrid').datagrid({
			queryParams: {
				nodeId: jQuery("#nodeId").val(),
				typeId: jQuery("#nodeId").val()
			}
		});
	}


	function addNew(){
		var nodeId = jQuery("#nodeId").val();
		var link = "${contextPath}/sys/dictory/prepareAdd?nodeId="+nodeId;
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
		var nodeId = jQuery("#nodeId").val();
	    var link = '${contextPath}/sys/dictory/prepareModify?id='+row.id;
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


	function searchWin(){
	    jQuery('#dlg').dialog('open').dialog('setTitle','数据字典查询');
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
		  var link = "${contextPath}/sys/dictory/prepareModify?id="+selected.id;
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
	}
 

	function viewSelected(){
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		if(rows == null || rows.length !=1){
			alert("请选择其中一条记录。");
			return;
		}
		var selected = jQuery('#mydatagrid').datagrid('getSelected');
		if (selected ){
		    location.href="${contextPath}/sys/dictory/prepareModify?id="+selected.id;
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
	}

	function deleteSelections(){
		var ids = [];
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		for(var i=0;i<rows.length;i++){
			ids.push(rows[i].id);
		}
		if(ids.length > 0 && confirm("数据删除后不能恢复，确定删除吗？")){
		    var rowIds = ids.join(',');
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/dictory/delete?id='+rowIds,
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
		} else {
			alert("请选择至少一条记录。");
		}
	}

	function sortDict(){
	    var nodeId = jQuery("#nodeId").val();
		var link = "${contextPath}/sys/dictory/showSort?parentId="+nodeId;
		var width=680;
		var height=430;
		var scroll="yes";
		jQuery.layer({
					type: 2,
					maxmin: true,
					shadeClose: true,
					title: "字典排序",
					closeBtn: [0, true],
					shade: [0.8, '#000'],
					border: [10, 0.3, '#000'],
					offset: ['20px',''],
					fadeIn: 100,
					area: ['650px', (jQuery(window).height() - 50) +'px'],
					iframe: {src: link}
				});
   }

	function reloadGrid2(){
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

	function searchData(){
	    var params = jQuery("#searchForm").formSerialize();
	    var queryParams = jQuery('#mydatagrid').datagrid('options').queryParams;
	    jQuery('#mydatagrid').datagrid('reload');	
	    jQuery('#dlg').dialog('close');
	}

	function genJS(){
		if(confirm("原来的JS将会被替换，确定重新生成吗？")){
		    var link = "${contextPath}/sys/dictory/genJS";
	        var params = jQuery("#iForm").formSerialize();
		    jQuery.ajax({
				   type: "POST",
				   url: link,
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
					   if(data.statusCode == 200){
					     
					   }
				   }
			 });
		}
	}

	function genJSON(){
		if(confirm("原来的JSON将会被替换，确定重新生成吗？")){
		    var link = "${contextPath}/sys/dictory/genJSON";
	        var params = jQuery("#iForm").formSerialize();
		    jQuery.ajax({
				   type: "POST",
				   url: link,
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
					   if(data.statusCode == 200){
					     
					   }
				   }
			 });
		}
	}
	
</script>
</head>
<body style="margin:1px;">  
<input type="hidden" id="nodeId" name="nodeId" value="" >
<div class="easyui-layout" data-options="fit:true">  
    <div data-options="region:'west',split:true" style="width:195px;">
	  <div class="easyui-layout" data-options="fit:true">  
           
			 <div data-options="region:'center',border:false">
			    <ul id="myTree" class="ztree"></ul>  
			 </div> 
			 
        </div>  
	</div> 
   <div data-options="region:'center'">   
		<div class="easyui-layout" data-options="fit:true">  
		   <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
			<div style="margin:4px;"> 
				<img src="${contextPath}/static/images/window.png">
				&nbsp;<span class="x_content_title">数据字典列表</span>
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
				   onclick="javascript:addNew();">新增</a>  
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
				   onclick="javascript:editSelected();">修改</a>  
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
				   onclick="javascript:deleteSelections();">删除</a> 
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-sort'"
				   onclick="javascript:sortDict();">同级排序</a>
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-class'"
				   onclick="javascript:genJS();">生成JS</a>
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-class'"
				   onclick="javascript:genJSON();">生成JSON</a>
				<!-- <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-search'"
				   onclick="javascript:searchWin();">查找</a> -->
		   </div> 
		  </div> 
		  <div data-options="region:'center',border:true">
			 <table id="mydatagrid"></table>
		  </div>  
      </div>
	</div>
</div>
</body>
</html>
