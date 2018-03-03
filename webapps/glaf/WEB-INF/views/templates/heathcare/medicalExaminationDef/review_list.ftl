<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>体格检查主题</title>
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
				url: '${contextPath}/heathcare/medicalExaminationDef/reviewJson?tenantId=${tenantId}',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        {title:'序号', field:'startIndex', width:60, sortable:false},
					    {title:'主题', field:'title', width:320},
					    {title:'类型', field:'type', width:120, formatter:formatterType},
					    {title:'检查日期', field:'checkDate', width:120},
					    {title:'功能键', field:'functionKey', width:150, formatter:formatterKeys}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 10,
				pageList: [10,15,20,25,30,40,50,100],
				pagePosition: 'both' 
			});
	});


    function formatterType(val, row){
		 if(val == "3"){
          return "开学体检";
		} else if(val == "5"){
          return "定期体检";
		} else if(val == "6"){
          return "常规体检";
		} else if(val == "7"){
          return "专项体检";
		} 
		return "";
	}

	function formatterKeys(val, row){
		var str = "<a href='javascript:checkList(\""+row.checkId+"\", \""+row.type+"\");'>体检列表</a>&nbsp;<a href='javascript:showReport(\""+row.checkId+"\");'>统计报告</a>";
	    return str;
	}
	 

	function checkList(checkId, type){
        var link="${contextPath}/heathcare/medicalExamination/reviewlist?tenantId=${tenantId}&type="+type+"&checkId="+checkId;
        location.href=link;
	}


	function showReport(checkId){
	    var link = '${contextPath}/heathcare/medicalExamination/showTenantReport?tenantId=${tenantId}&checkId='+checkId;
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "统计报告",
		  area: ['680px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	}

	
	function searchWin(){
	    jQuery('#dlg').dialog('open').dialog('setTitle','体格检查主题查询');
	    //jQuery('#searchForm').form('clear');
	}

	function resize(){
		jQuery('#mydatagrid').datagrid('resize', {
			width:800,
			height:400
		});
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
		 
</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
		<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">体格检查主题列表</span>
   </div> 
  </div> 
  <div data-options="region:'center',border:true">
	 <table id="mydatagrid"></table>
  </div>  
</div>
</div>
</body>
</html>