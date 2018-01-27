<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>组合数据表</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">
   var contextPath="${request.contextPath}";

   jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:1000,
				height:480,
				fit:true,
				fitColumns: true,
				nowrap: false,
				striped: true,
				collapsible: true,
				url: '${request.contextPath}/sys/combinationApp/json',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        {title:'序号', field:'startIndex', width:80, sortable:false},
						{title:'标题', field:'title', width:280},
					    {title:'目标表', field:'targetTableName', width:180},
						{title:'创建人', field:'createBy', width:90},
						{title:'创建时间', field:'createTime', width:90},
					    {title:'是否有效', field:'active', width:90, formatter:formatterActive},
						{title:'功能键', field:'functionKey', width:180, formatter:formatterKeys}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 10,
				pageList: [10,15,20,25,30,40,50,100],
				pagePosition: 'both',
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
		var str = "<a href='javascript:editRow(\""+row.id+"\");'>修改</a>&nbsp;<a href='javascript:syncList(\""+row.id+"\");'>同步项</a>&nbsp;<a href='javascript:dropTable(\""+row.id+"\");'>重建表</a>&nbsp;<a href='javascript:dataHandler(\""+row.id+"\");'>处理器</a>&nbsp;<a href='javascript:columnList(\""+row.id+"\");'>列定义</a>&nbsp;<a href='javascript:historyList(\""+row.id+"\");'>日志</a>";
	    return str;
	}

	function columnList(id){
		var link = '${request.contextPath}/sys/tableMgr/extColumns?targetId=combination_app_'+id;
	    jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "列信息",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['980px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		}); 
	}


	function dataHandler(id){
        var link = '${request.contextPath}/sys/syntheticFlow/edit?currentStep=combination_app_'+id+'&code=combination_app';
	    jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "处理器",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['980px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		}); 
	}

    function dropTable(id){
	  if(confirm("数据删除后不能恢复,确定重建目标表吗?")){
		var link = "${request.contextPath}/sys/combinationApp/dropTable?id="+id;
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
	                          //window.location.reload();
	                       }
                       }
		 });
	  }
	}

    function syncList(syncId){
	    var link = '${request.contextPath}/sys/combinationItem?syncId='+syncId;
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
			area: ['980px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		}); 
	}


	function historyList(syncId){
	    var link = '${request.contextPath}/sys/combinationHistory?syncId='+syncId;
	    jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "历史记录",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['880px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		}); 
	}

		 
	function editRow(id){
	    var link="${request.contextPath}/sys/combinationApp/edit?id="+id;
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
			area: ['880px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function addNew(){
	    var link="${request.contextPath}/sys/combinationApp/edit";
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
	    var link = '${request.contextPath}/sys/combinationApp/edit?id='+row.id;
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
			area: ['880px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		}); 
	}

	function searchWin(){
	    jQuery('#dlg').dialog('open').dialog('setTitle','组合数据表查询');
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
		  var link = '${request.contextPath}/sys/combinationApp/edit?id='+selected.id;
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
			area: ['880px', (jQuery(window).height() - 50) +'px'],
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
		    var link = '${request.contextPath}/sys/combinationApp/edit?id='+selected.id;
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
				area: ['880px', (jQuery(window).height() - 50) +'px'],
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
				   url: '${request.contextPath}/sys/combinationApp/delete?ids='+str,
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

	function execute(){
	    var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    if(rows == null || rows.length !=1){
		    alert("请选择其中一条记录。");
		    return;
	    }
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected && confirm("确定要在目标库上执行同步操作吗？")){
			var syncId = selected.id;
		    jQuery.ajax({
				   type: "POST",
				   url: '${request.contextPath}/sys/combinationApp/execute?syncId='+syncId,
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

	function searchData(){
        var params = jQuery("#searchForm").formSerialize();
        jQuery.ajax({
                    type: "POST",
                    url: '${request.contextPath}/sys/combinationApp/json',
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
		 
</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north', split:false, border:true"  style="height:38px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	&nbsp;<img src="${request.contextPath}/static/images/window.png">
	&nbsp;<span class="x_content_title">组合数据表列表</span>
    <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
	   onclick="javascript:addNew();">新增</a>  
    <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
	   onclick="javascript:editSelected();">修改</a>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-sys'"
	   onclick="javascript:execute();">执行</a>
   </div> 
  </div> 
  <div data-options="region:'center',border:true">
	 <table id="mydatagrid"></table>
  </div>  
</div>

</body>
</html>
