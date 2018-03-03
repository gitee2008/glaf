<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>缴费信息</title>
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
				url: '${contextPath}/heathcare/personPayment/json?personId=${personId}&gradeId=${gradeId}',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        <#if audit == true>
						{title:'选择', field:'chk', width:60, align:'center', formatter:formatterKey},
						</#if>
				        {title:'序号', field:'startIndex', width:60, sortable:false},
						{title:'班级', field:'grade', width:100},
						{title:'姓名', field:'person', width:100},
						{title:'缴费金额', field:'money', width:100},
						{title:'缴费时间', field:'payTime', width:120},
						{title:'费用类型', field:'type', width:120, formatter:formatterType},
						{title:'备注', field:'remark', width:150},
						{title:'状态', field:'businessStatus', width:90, formatter:formatterStatus},
						{title:'创建人', field:'createByName', width:90},
						{title:'创建日期', field:'createTime', width:120},
						{title:'确认人', field:'confirmByName', width:90},
						{title:'确认时间', field:'confirmTime', width:120},
						{title:'功能键', field:'functionKey',width:120, formatter:formatterKeys}
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


	function formatterStatus(val, row){
		if(val == 9){
			return "<font color='green'>已确认</font>";
		}
		return "未确认";
	}

	function formatterType(val, row){
        if(val == "1"){
			return "学杂费";
		} else if(val == "2"){
			return "伙食费";
		} else if(val == "3"){
			return "服装费";
		} else if(val == "4"){
			return "保险费";
		} else if(val == "5"){
			return "其他";
		}
		return "";
	}

    function formatterKey(value, row, index) {
		if(row.businessStatus == 0){
		  var s = '<input name="isCheck" type="checkbox" value="'+row.id+'" > ';
		  return s;
		}
		return "";
	}

	function formatterKeys(val, row){
		if(row.businessStatus == 9){
			return "<font color='green'>已确认</font>";
		}
		var str = "";
		<#if audit == true>
		  str = "<a href='javascript:auditRow(\""+row.id+"\");'><img src='${contextPath}/static/images/audit.png' border='0'>&nbsp;审核</a>&nbsp;";
		<#else>
		  str = "<a href='javascript:editRow(\""+row.id+"\");'>修改</a>&nbsp;<a href='javascript:deleteRow(\""+row.id+"\");'>删除</a>";
		</#if>
	    return str;
	}
	

	function addNew(){
	    var link="${contextPath}/heathcare/personPayment/edit?personId=${personId}&gradeId=${gradeId}";
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
	    var link = '${contextPath}/heathcare/personPayment/edit?id='+row.id;
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
	    var link = '${contextPath}/heathcare/personPayment/edit?id='+id;
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
				   url: '${contextPath}/heathcare/personPayment/delete?id='+id,
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
	    var link = '${contextPath}/heathcare/personPayment/edit?id='+row.id;
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
	    jQuery('#dlg').dialog('open').dialog('setTitle','缴费信息查询');
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
		  var link = '${contextPath}/heathcare/personPayment/edit?id='+selected.id;
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
		    var link='${contextPath}/heathcare/personPayment/edit?readonly=true&id='+selected.id;
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
				   url: '${contextPath}/heathcare/personPayment/delete?ids='+str,
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
                    url: '${contextPath}/heathcare/personPayment/json',
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

	function checkAll() {
		//jQuery("input[name='isCheck']").attr("checked", true); 
		jQuery("[name = isCheck]:checkbox").attr("checked", true);
	}

	function auditSelected(){
	    var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    if(rows == null || rows.length !=1){
		  alert("请选择其中一条记录。");
		  return;
	    }
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected ){
		  var link = '${contextPath}/heathcare/personPayment/edit?audit=true&id='+selected.id;
		  jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "审核记录",
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

	function auditBatch(){
	  var objectIds = jQuery("input[name='isCheck']:checked").map(function () {
               return jQuery(this).val();
           }).get().join(',');
	  if(objectIds == ""){
           alert("请选择至少一条记录。");
		   return;
	  }
	  if(confirm("确定批量通过审核吗？")){
		if(confirm("审核通过后的数据不能再修改或删除，确定吗？")){
		  jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/personPayment/audit?ids='+objectIds,
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
					       jQuery('#mydatagrid').datagrid('reload');
					   }
				   }
			 });
		  }
	    }
	}

	function refundList(){
		location.href="${contextPath}/heathcare/personRefund?personId=${personId}&gradeId=${gradeId}&audit=${audit}";
	}
	
</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
		<img src="${contextPath}/static/images/window.png">
		&nbsp;<span class="x_content_title">缴费信息列表</span>
		<#if audit == true>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-checkall'"
		   onclick="javascript:checkAll();">全选</a>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-audit'"
		   onclick="javascript:auditSelected();">审核</a>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-audit'"
		   onclick="javascript:auditBatch();">批量审核</a>
		<#else>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
		   onclick="javascript:addNew();">新增</a>  
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
		   onclick="javascript:editSelected();">修改</a>  
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
		   onclick="javascript:deleteSelections();">删除</a> 
		</#if>
		<button type="button" id="searchButton" class="btn btnGrayMini" style="width: 90px" 
				onclick="javascript:refundList();">退费信息</button>
   </div> 
  </div> 
  <div data-options="region:'center',border:true">
	 <table id="mydatagrid"></table>
  </div>  
</div>
</div>
</body>
</html>