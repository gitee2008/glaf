<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>疾病信息</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

	var setting = {
		async: {
				enable: true,
				url:"${contextPath}/heathcare/gradeInfo/treeJson"
			},
			callback: {
				beforeClick: zTreeBeforeClick,
				onClick: zTreeOnClick
			}
	};

	function zTreeBeforeClick(treeId, treeNode, clickFlag) {
           
	}

	function zTreeOnClick(event, treeId, treeNode, clickFlag) {
		if(treeNode.type == "person"){
		    jQuery("#nodeId").val(treeNode.id);
		    loadData('${contextPath}/heathcare/personSickness/json?personId='+treeNode.id);
		} else {
			jQuery("#nodeId").val("");
			loadData('${contextPath}/heathcare/personSickness/json?gradeId='+treeNode.id);
		}
	}

	function loadData(url){
		$.post(url,{qq:'xx'},function(data){
		      //var text = JSON.stringify(data); 
              //alert(text);
			  $('#mydatagrid').datagrid('loadData', data);
		},'json');
	}


	$(document).ready(function(){
		$.fn.zTree.init($("#myTree"), setting);
	});


   jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width: x_width,
				height: x_height,
				fit: false,
				fitColumns: true,
				nowrap: false,
				striped: true,
				collapsible: true,
				url: '${contextPath}/heathcare/personSickness/json?gradeId=${gradeId}&personId=${personId}&infectiousFlag=${infectiousFlag}&year=${year}&month=${month}',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        {title:'序号', field:'startIndex', width:80, sortable:false},
						{title:'姓名',field:'name', width:120},
						{title:'病名',field:'sickness', width:120},
						{title:'发现时间',field:'discoverTime', width:120},
						{title:'报告时间',field:'reportTime', width:120},
						{title:'确认时间',field:'confirmTime', width:120},
						{title:'就诊日期',field:'clinicTime', width:120},
						{title:'排查结果',field:'result', width:120},
						{field:'functionKey',title:'功能键',width:120, formatter:formatterKeys}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 10,
				pageList: [10,15,20,25,30,40,50,100],
				pagePosition: 'both',
				onDblClickRow: onMyRowClick 
			});

			var pgx = $("#mydatagrid").datagrid("getPager");
			if(pgx){
			   $(pgx).pagination({
				   onBeforeRefresh:function(){
					   //alert('before refresh');
				   },
				   onRefresh:function(pageNumber,pageSize){
					   //alert(pageNumber);
					   //alert(pageSize);
					   loadGridData(getLink()+"&page="+pageNumber+"&rows="+pageSize);
					},
				   onChangePageSize:function(){
					   //alert('pagesize changed');
					   loadGridData(getLink());
					},
				   onSelectPage:function(pageNumber, pageSize){
					   //alert(pageNumber);
					   //alert(pageSize);
					   loadGridData(getLink()+"&page="+pageNumber+"&rows="+pageSize);
					}
			   });
			}
	});

	function formatterKeys(val, row){
		var str = "<a href='javascript:editRow(\""+row.id+"\");'>修改</a>&nbsp;<a href='javascript:deleteRow(\""+row.id+"\");'>删除</a>";
	    return str;
	}

	function onMyRowClick(rowIndex, row){
	    var link = '${contextPath}/heathcare/personSickness/edit?id='+row.id;
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
			area: ['880px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

   function editRow(id){
	    var link = '${contextPath}/heathcare/personSickness/edit?id='+id;
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
			area: ['880px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function deleteRow(id){
		if(confirm("数据删除后不能恢复，确定删除吗？")){
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/personSickness/delete?id='+id,
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

	function addNew(){
		var personId = jQuery("#nodeId").val();
		if(personId == ""){
			alert("请选择学生。");
			return;
		}
	    var link="${contextPath}/heathcare/personSickness/edit?personId="+personId;
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
	    var link = '${contextPath}/heathcare/personSickness/edit?id='+row.id;
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
			area: ['880px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function searchWin(){
	    jQuery('#dlg').dialog('open').dialog('setTitle','学生疾病信息查询');
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
		  var link = '${contextPath}/heathcare/personSickness/edit?id='+selected.id;
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
		    var link='${contextPath}/heathcare/personSickness/edit?readonly=true&id='+selected.id;
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
				   url: '${contextPath}/heathcare/personSickness/delete?ids='+str,
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
                    url: '${contextPath}/heathcare/personSickness/json',
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
       document.iForm.submit();
	}

	function doSearch(){
       document.iForm.submit();
	}

	function exportXls(){
		var gradeId = jQuery("#gradeId").val();
		var year = jQuery("#year").val();
        var month = jQuery("#month").val();
		var infectiousFlag = jQuery("#infectiousFlag").val();
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=InfectiousDiseaseList";
		if(gradeId != ""){
			link = link + "&gradeId=" + gradeId;
		}
		if(year != ""){
			link = link + "&year=" + year;
		}
		if(month != ""){
			link = link  + "&month=" + month;
		}
		if(infectiousFlag != ""){
			link = link  + "&infectiousFlag=" + infectiousFlag;
		}
        window.open(link);
    }
		 		 
</script>
</head>
<body>  
<input type="hidden" id="nodeId" name="nodeId" value="" >
<div style="margin:0px;"></div>  
<div class="easyui-layout" data-options="fit:true">  
    <div data-options="region:'west', split:true" style="width:160px;">
	  <div class="easyui-layout" data-options="fit:true">  
			 <div data-options="region:'center', border:false">
			    <ul id="myTree" class="ztree"></ul>  
			 </div> 			 
        </div>  
	</div> 
	
    <div data-options="region:'center'">  
        <div class="easyui-layout" data-options="fit:true">  
          <div data-options="region:'center', split:true, border:true, fit:true">
		   <div data-options="region:'north', split:true, border:true" style="height:42px" class="toolbar-backgroud"> 
		   <div style="margin-top:4px;">
			<form id="iForm" name="iForm" method="post" action="">
			<table>
			  <tr>
				<td>
					<img src="${contextPath}/static/images/window.png">
					&nbsp;<span class="x_content_title">
					<#if gradeInfo?exists>${gradeInfo.name}</#if><#if person?exists>${person.name}</#if>疾病信息列表</span>
					<#if privilege_write == true>
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
					   onclick="javascript:addNew();">新增</a>
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
					   onclick="javascript:editSelected();">修改</a>  
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
					   onclick="javascript:deleteSelections();">删除</a> 
					</#if>
					<!-- <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-search'"
					   onclick="javascript:searchWin();">查找</a> -->
				</td>
				<td>&nbsp;&nbsp;
					<select id="gradeId" name="gradeId" onchange="switchXY();">
						<option value="">--请选择--</option>
						<#list gradeInfos as grade>
						<option value="${grade.id}">${grade.name}</option>
						</#list> 
					</select>
					<script type="text/javascript">
						document.getElementById("gradeId").value="${gradeId}";
					</script>
				</td>
				<td>年份&nbsp;&nbsp;
					<select id="year" name="year">
						<#list years as year>
						<option value="${year}">${year}</option>
						</#list> 
					</select>
					<script type="text/javascript">
						document.getElementById("year").value="${year}";
					</script>
				</td>
				<td>月份&nbsp;&nbsp;
					<select id="month" name="month">
						<option value="">--请选择--</option>
						<#list months as month>
						<option value="${month}">${month}</option>
						</#list> 
					</select>
					<script type="text/javascript">
						document.getElementById("month").value="${month}";
					</script>
				</td>
				<td>是否传染病&nbsp;&nbsp;
					<select id="infectiousFlag" name="infectiousFlag">
						<option value="">--请选择--</option>
						<option value="N">否</option>
						<option value="Y">是</option>
					</select>
					<script type="text/javascript">
						document.getElementById("infectiousFlag").value="${infectiousFlag}";
					</script>
				</td>
				<td>
				    <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-search'" 
					   onclick="javascript:doSearch();">查找</a>
				    &nbsp;
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'" 
					   onclick="javascript:exportXls();">导出Excel</a>
				</td>
			 </tr>
			</table>
			</form>
			</div> 
		   </div> 
		   <div data-options="region:'center', border:true" data-options="fit:true">
			 <table id="mydatagrid" style="width:100%; height:100%"></table>
		   </div>  
      </div>  
    </div>  
  </div>  
</div>
</body>  
</html>