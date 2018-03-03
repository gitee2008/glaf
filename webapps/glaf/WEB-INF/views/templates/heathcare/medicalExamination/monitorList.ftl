<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>儿童生长发育暨健康监测记录</title>
<#include "/inc/init_easyui_layer3_import.ftl"/>
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
			jQuery("#personId").val(treeNode.id);
			jQuery("#div_person_print").show();
		    loadData('${contextPath}/heathcare/medicalExamination/json?checkId=${checkId}&type=${type}&personId='+treeNode.id);
		} else {
			jQuery("#nodeId").val("");
			jQuery("#personId").val("");
			jQuery("#div_person_print").hide();
		    loadData('${contextPath}/heathcare/medicalExamination/json?checkId=${checkId}&type=${type}&gradeId='+treeNode.id);
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
				url: '${contextPath}/heathcare/medicalExamination/json?gradeId=${gradeId}&type=${type}&personId=${personId}&checkId=${checkId}',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        {title:'序号', field:'startIndex', width:60, sortable:false},
						{title:'姓名', field:'name', width:80, align:"center"},
						{title:'性别', field:'sex', width:60, formatter:formatterSex, align:"center"},
						{title:'身高(厘米)&nbsp;&nbsp;', field:'height', width:90, align:"right"},
						{title:'&nbsp;身高评价', field:'heightEvaluate', width:120, align:"center"},
						{title:'体重(千克)&nbsp;&nbsp;', field:'weight', width:90, align:"right"},
						{title:'&nbsp;体重评价', field:'weightEvaluate', width:120, align:"center"},
						{title:'出生日期', field:'birthday', width:90, align:"center"},
					    {title:'体检日期', field:'checkDate', width:90, align:"center"},
						{title:'月龄&nbsp;', field:'checkAgeOfMonth', width:70, align:"center"},
						{title:'功能键', field:'functionKey',width:120, formatter:formatterKeys}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 10,
				pageList: [10,15,20,25,30,40,50,100],
				pagePosition: 'both',
				onDblClickRow: onMyRowClick 
			});
	});


	function formatterSex(val, row){
		if(val == "1"){
			return "男";
		} else {
            return "女";
		}
	}

	function formatterKeys(val, row){
		<#if hasWritePermission>
		var str = "<a href='javascript:editRow(\""+row.id+"\");'>修改</a>&nbsp;<a href='javascript:deleteRow(\""+row.id+"\");'>删除</a>";
	    return str;
		<#else>
		return "<a href='javascript:editRow(\""+row.id+"\");'>查看</a>&nbsp;";
		</#if>
	}
	

	function addNew(){
        var personId = jQuery("#nodeId").val();
		if(personId == ""){
			alert("请选择学生。");
			return;
		}
	    var link="${contextPath}/heathcare/medicalExamination/monitorEdit?personId="+personId+"&checkId=${checkId}&type=${type}";
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "新增记录",
		  area: ['980px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	}


	function onMyRowClick(rowIndex, row){
	    var link = '${contextPath}/heathcare/medicalExamination/monitorEdit?id='+row.id;
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "编辑记录",
		  area: ['980px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	}

    function editRow(id){
	    var link = '${contextPath}/heathcare/medicalExamination/monitorEdit?id='+id;
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "编辑记录",
		  area: ['980px', (jQuery(window).height() - 50) +'px'],
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
				   url: '${contextPath}/heathcare/medicalExamination/delete?id='+id,
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
	    var link = '${contextPath}/heathcare/medicalExamination/monitorEdit?id='+row.id;
	    layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "编辑记录",
		  area: ['980px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
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
		  var link = '${contextPath}/heathcare/medicalExamination/monitorEdit?id='+selected.id;
		  layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "编辑记录",
			  area: ['980px', (jQuery(window).height() - 50) +'px'],
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
		    var link='${contextPath}/heathcare/medicalExamination/monitorEdit?id='+selected.id;
		    layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "编辑记录",
			  area: ['980px', (jQuery(window).height() - 50) +'px'],
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
				   url: '${contextPath}/heathcare/medicalExamination/delete?ids='+str,
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
                    url: '${contextPath}/heathcare/medicalExamination/json',
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

	function changeCheck(){
		var checkId = jQuery("#checkId").val();
		location.href="${contextPath}/heathcare/medicalExamination?checkId="+checkId;
	}

	function exportXls(){
		var gradeId = jQuery("#gradeId").val();
		var year = jQuery("#year").val();
        var month = jQuery("#month").val();
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=MedicalExaminationSicknessPositiveSign&checkId=${checkId}";
		if(gradeId != ""){
			link = link + "&gradeId=" + gradeId;
		}
		if(year != ""){
			link = link + "&year=" + year;
		}
		if(month != ""){
			link = link  + "&month=" + month;
		}
        window.open(link);
    }

	function exportXls2(){
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=Growth&personId=${personId}&checkId=${checkId}";
        window.open(link);
    }
	
	function exportXls3(){
		var personId = document.getElementById("personId").value;
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=Growth&personId="+personId+"&checkId=${checkId}";
        window.open(link);
    }	
</script>
</head>
<body>  
<input type="hidden" id="nodeId" name="nodeId" value="" > 
<div class="easyui-layout" data-options="fit:true">  
    <div data-options="region:'west', split:true" style="width:225px;">
	  <div class="easyui-layout" data-options="fit:true"> 
		  <div data-options="region:'center', border:false">
			  <ul id="myTree" class="ztree"></ul>  
		  </div> 			 
        </div>  
	</div> 
	
    <div data-options="region:'center'">  
        <div class="easyui-layout" data-options="fit:true">  
          <div data-options="region:'center', split:true, border:true, fit:true" style="margin-top:-4px;">
		   <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
		    <div style="margin-top:4px;">
			<form id="iForm" name="iForm" method="post" action="">
			<input type="hidden" id="personId" name="personId">
			<input type="hidden" id="type" name="type" value="${type}">
			<table>
			  <tr>
				<td>
					<img src="${contextPath}/static/images/window.png">
					&nbsp;<span class="x_content_title">儿童生长发育暨健康监测记录</span>
					<#if hasWritePermission>
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
					   onclick="javascript:addNew();">新增</a>
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
					   onclick="javascript:editSelected();">修改</a>  
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
					   onclick="javascript:deleteSelections();">删除</a> 
					</#if>
				</td>
				<td>
					&nbsp;
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
				<td>&nbsp;
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'" 
					   onclick="javascript:exportXls();">统计结果</a>
				</td>
				<td>&nbsp;
				  <#if personId?exists>
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'" 
					   onclick="javascript:exportXls2();">打印</a>
				  <#else>
				    <a id="div_person_print" style="display:none" href="#" class="easyui-linkbutton" 
					   data-options="plain:true, iconCls:'icon_export_xls'" 
					   onclick="javascript:exportXls3();">打印</a>
				  </#if>
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