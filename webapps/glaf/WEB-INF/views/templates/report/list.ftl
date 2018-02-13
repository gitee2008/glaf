<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>报表定义</title>
<#include "/inc/init_easyui_import.ftl"/> 
<script type="text/javascript">

  /**
   var setting = {
			async: {
				enable: true,
				url:"${contextPath}/rs/tree/treeJson?nodeCode=report_category",
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
		loadMxData('${contextPath}/report/json?nodeId='+treeNode.id);
	}

	function loadMxData(url){
		  jQuery.get(url+'&randnum='+Math.floor(Math.random()*1000000),{qq:'xx'},function(data){
		      //var text = JSON.stringify(data); 
              //alert(text);
			  jQuery('#mydatagrid').datagrid('loadData', data);
			  //jQuery('#mydatagrid').datagrid('load',getMxObjArray(jQuery("#iForm").serializeArray()));
		  },'json');
	}

    jQuery(document).ready(function(){
			jQuery.fn.zTree.init(jQuery("#myTree"), setting);
	});

	**/

	jQuery(function(){
			jQuery('#mydatagrid').datagrid({
				width:980,
				height:480,
				fit:true,
				fitColumns:true,
				nowrap: false,
				striped: true,
				collapsible:false,
				singleSelect:true,
				url:'${contextPath}/report/json',
				remoteSort: false,
				idField:'id',
				pageSize:10,
				columns:[[
				    {field:'id', title:'编号', width:80, sortable:true},
					{field:'name', title:'名称', width:120},
					{field:'subject', title:'标题', width:240},
					{field:'reportName', title:'报表名称', width:320},
					{field:'type', title:'报表类型', width:90},
					{field:'createDate', title:'创建日期', width:90}
				]],
				pagination:true,
				rownumbers:false,
				onDblClickRow: onRowClick 
			});

			var p = jQuery('#mydatagrid').datagrid('getPager');
			jQuery(p).pagination({
				onBeforeRefresh:function(){
					alert('before refresh');
				}
			});
		});

		function searchWin(){
			jQuery('#dlg').dialog('open').dialog('setTitle','报表定义查询');
			//jQuery('#iForm').form('clear');
		}

		function resize(){
			jQuery('#mydatagrid').datagrid('resize', {
				width:800,
				height:400
			});
		}

		function create(){						 
			//location.href="${contextPath}/report/edit?nodeId="+jQuery("#nodeId").val();
			editRow("");
		}

		function editSelected(){
		    var rows = jQuery('#mydatagrid').datagrid('getSelections');
			if(rows == null || rows.length !=1){
			     alert("请选择其中一条记录。");
				 return;
			}
			var selected = jQuery('#mydatagrid').datagrid('getSelected');
			if (selected ){
			    //location.href="${contextPath}/report/edit?reportId="+selected.id;
				editRow(selected.id);
			}
		}

		function onRowClick(rowIndex, row){
            window.open('${contextPath}/report/edit?reportId='+row.id);
		}

		function editRow(id){
			var nodeId = jQuery("#nodeId").val();
			if(nodeId==null || nodeId=='' ){
			    // alert("请在左边选择分类类型！");
			    // return;
		    }
			var link = '${contextPath}/report/edit?reportId='+id+"&nodeId="+nodeId;
			jQuery.layer({
						type: 2,
						maxmin: true,
						shadeClose: true,
						title: "编辑报表信息",
						closeBtn: [0, true],
						shade: [0.8, '#000'],
						border: [10, 0.3, '#000'],
						offset: ['20px',''],
						fadeIn: 100,
						area: [(jQuery(window).width() - 80) +'px', (jQuery(window).height() - 50) +'px'],
						iframe: {src: link}
					});
		}

	
		function sendMail(){
			if(confirm("确定生成报表并发送邮件吗？")){
			var rows = jQuery('#mydatagrid').datagrid('getSelections');
			if(rows != null && rows.length ==1){
				  var selected = jQuery('#mydatagrid').datagrid('getSelected');
			      jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/rs/report/sendMail?reportId='+selected.id,
				   dataType:  'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data.message != null){
						   alert(data.message);
					   } else {
						 alert('操作完成！');
					   }
				   }
			     });
			} else {
                jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/rs/report/sendMailAllInOne',
				   dataType:  'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data.message != null){
						   alert(data.message);
					   } else {
						 alert('操作完成！');
					   }
				   }
			     });
			  }
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
			   location.href="${contextPath}/report/edit&reportId="+selected.id;
			}
		}

		function deleteSelections(){
			var ids = [];
			var rows = jQuery('#mydatagrid').datagrid('getSelections');
			for(var i=0;i<rows.length;i++){
				ids.push(rows[i].id);
			}
			if(ids.length > 0 && confirm("数据删除后不能恢复，确定删除吗？")){
			  var reportIds = ids.join(',');
			  jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/rs/report/deleteAll?reportIds='+reportIds,
				   dataType:  'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data.message != null){
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
		 
</script>
</head>
<body style="margin:0px;">  
<div style="margin:0;"></div>  
<input type="hidden" id="nodeId" name="nodeId" value="" >
<div class="easyui-layout" data-options="fit:true">  
    <!-- <div data-options="region:'west',split:true" style="width:180px;">
	  <div class="easyui-layout" data-options="fit:true">  
           
			 <div data-options="region:'center',border:false">
			    <ul id="myTree" class="ztree"></ul>  
			 </div> 
			 
        </div>  
	</div> --> 
   <div data-options="region:'center'"> 
	<div class="easyui-layout" data-options="fit:true">  
	   <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
		<div style="margin:4px;"> 
		<span class="x_content_title">&nbsp;<img src="${contextPath}/static/images/window.png">&nbsp;报表定义列表</span>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
		   onclick="javascript:create();">新增</a>  
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
		   onclick="javascript:editSelected();">修改</a>  
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
		   onclick="javascript:deleteSelections();">删除</a> 
	   </div> 
	  </div> 
	  <div data-options="region:'center',border:false">
		 <table id="mydatagrid"></table>
	  </div>  
	</div>
  </div>
</div>
</body>
</html>