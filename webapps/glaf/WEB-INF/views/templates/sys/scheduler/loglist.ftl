<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>调度历史查询</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

   jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:1000,
				height:480,
				fit:true,
				fitColumns:true,
				nowrap: false,
				striped: true,
				collapsible:true,
				url:'${contextPath}/sys/schedulerLog/json?taskId=${taskId}',
				remoteSort: true,
				checkbox: true, 
				idField:'id',
				columns:[[
					{field:'ck', checkbox:true, width:60},
	                {title:'任务名称',field:'taskName',width:240},
					{title:'开始时间',field:'startDate_datetime', width:120},
					{title:'结束时间',field:'endDate_datetime', width:120},
					{title:'运行时长',field:'runTimes', width:90},
					{title:'创建人',field:'createUserName', width:90},
					{title:'创建时间',field:'createDate_datetime', width:120},
					{title:'状态',field:'status', width:80, formatter:formatterStatus }
				]],
				rownumbers:true,
				pagination:true,
				pageSize:10,
				pageList: [10,15,20,25,30,40,50,100],
				onDblClickRow: onRowClick 
			});

			var p = jQuery('#mydatagrid').datagrid('getPager');
			jQuery(p).pagination({
				onBeforeRefresh:function(){
					//alert('before refresh');
				}
		    });
	});


	function formatterStatus(val, row){
       if(val == 0){
			return '等待运行';
	   } else if(val == 1){
			return '正在运行';
	   } else if(val == 2){
			return '<span style="color:green; font: bold 13px 宋体;">运行成功</span>';
	   } else if(val == 3){
			return '<span style="color:red; font: bold 13px 宋体;">运行失败</span>';
	   } else  {
			return '未知';
	   }  
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


	function clearSelections(){
	    jQuery('#mydatagrid').datagrid('clearSelections');
	}

	function loadGridData(url){
		  jQuery.post(url,{qq:'xx'},function(data){
		      var text = JSON.stringify(data); 
              alert(text);
			  jQuery('#mydatagrid').datagrid('loadData', data);
		  },'json');
	  }


	function deleteRows(){
	    var ids = [];
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		for(var i=0;i<rows.length;i++){
			ids.push(rows[i].id);
		}
		if(ids.length > 0 ){
			if(confirm("确定删除这些调度历史吗？")){
			  jQuery.ajax({
					   type: "POST",
					   url: '${contextPath}/sys/schedulerLog/delete?ids='+ids,
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
						   reloadGrid();
					   }
				 });
			}
		}
	}

	function deleteAll(){
	    if(confirm("确定删除该任务的全部调度历史吗？")){
			  jQuery.ajax({
					   type: "POST",
					   url: '${contextPath}/sys/schedulerLog/deleteAll?taskId=${taskId}',
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
						   reloadGrid();
					   }
				 });
			}
	}


	function onRowClick(rowIndex, row){
	    var link = '${contextPath}/sys/schedulerLog/view?q=1&id='+row.id;
	    art.dialog.open(link, { height: 425, width: 680, title: "调度详细信息", lock: true, scrollbars:"auto" }, false);
	}
	 

    function search(){
		 var params = jQuery("#iForm").formSerialize();
         jQuery.ajax({
					   type: "POST",
					   url: '${contextPath}/sys/schedulerLog/json?taskId=${taskId}',
					   dataType: 'json',
					   data: params,
					   error: function(data){
						   alert('服务器处理错误！');
					   },
					   success: function(data){
                           jQuery('#mydatagrid').datagrid({
							  queryParams: {
								  startDate: jQuery("#startDate").datebox('getValue'),
								  endDate:jQuery("#endDate").datebox('getValue'),
								  runStatus:document.getElementById("runStatus").value
							  }
						   });

						   jQuery('#mydatagrid').datagrid('loadData', data);
					   }
				 });
	}

</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north', split:false, border:true" style="height:42px;" class="toolbar-backgroud"> 
    <div style="margin-top:4px;"> 
	 <form id="iForm" name="iForm" method="post" action="">  
      <table>
		   <tr>
		   <td>
            <img src="${contextPath}/static/images/window.png">
	        &nbsp;<span class="x_content_title">调度历史列表</span>
		   </td>
		   <td>
			   <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
				  onclick="javascript:deleteRows();">删除</a> 
			   <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
				  onclick="javascript:deleteAll();">全部删除</a> 
			   <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-back'"
				  onclick="javascript:location.href='${contextPath}/sys/scheduler';">返回</a>
		   </td>
			<td>运行状态&nbsp;</td>
			<td>
			 <select id="runStatus" name="runStatus">
			    <option value=""></option>
				<option value="0">未运行</option>
				<option value="1">运行中</option>
				<option value="2">运行成功</option>
				<option value="3">运行失败</option>
			 </select>
			</td>
		    <td>开始日期&nbsp;</td>
			 <td>
			  <input id="startDate" name="startDate" size="20"
			         type="text"  class="x-searchtext easyui-datetimebox" >
			</td>
			<td>结束日期&nbsp;</td>
			 <td>
			  <input id="endDate" name="endDate" size="20"
			         type="text"  class="x-searchtext easyui-datetimebox" />
			</td>
			<td>
			  <input type="button" value="查找" class="  btnGray" onclick="javascript:search();">
			</td>
		   </tr>
		</table>
		<input type="hidden" id="ids" name="ids">
       </form>
     
   </div> 
  </div> 
  <div data-options="region:'center',border:true">
	 <table id="mydatagrid"></table>
  </div>  
</div>
 
</body>
</html>