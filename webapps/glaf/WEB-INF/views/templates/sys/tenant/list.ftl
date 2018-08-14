<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>租户信息</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">
   var contextPath = "${contextPath}";

   jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:1000,
				height:480,
				fit:true,
				fitColumns: true,
				nowrap: false,
				striped: true,
				collapsible: true,
				url: '${contextPath}/sys/tenant/json?provinceId=${provinceId}&cityId=${cityId}&areaId=${areaId}&level=${level}&property=${property}',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        {title:'序号', field:'startIndex', width:50, sortable:false},
						{title:'编号', field:'id', width:60},
						{title:'名称', field:'name', width:180},
						{title:'代码', field:'code', width:60},
						{title:'等级', field:'level', width:90, formatter:formatterLevel},
						{title:'性质', field:'property', width:90, formatter:formatterProperty},
						{title:'负责人', field:'principal', width:80},
						{title:'电话', field:'telephone', width:98},
						{title:'创建人', field:'createBy', width:80},
						{title:'创建日期', field:'createTime', width:90},
						{title:'是否有效', field:'locked', width:90, formatter:formatterStatus},
						{title:'功能键', field:'functionKey', width:180, formatter:formatterKeys}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 100,
				pageList: [10,15,20,25,30,40,50,100,200,500,1000],
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

	function formatterKeys(val, row){
		var str = "<a href='javascript:editRow(\""+row.id+"\");'>修改</a>&nbsp;<a href='javascript:users(\""+row.tenantId+"\");'>用户</a>&nbsp;<a href='javascript:apps(\""+row.tenantId+"\",\""+row.type+"\");'>模块</a>&nbsp;<a href='javascript:tcImage(\""+row.tenantId+"\");'>营业执照</a>&nbsp;<a href='javascript:exp(\""+row.tenantId+"\");'>导出</a>&nbsp;";
	    return str;
	}

	function exp(tenantId){
		window.open('${contextPath}/sys/tenantExport/export?tenantId='+tenantId);
	}
	
	function formatterLevel(val, row){
        if(val == 9999){
			return "省级示范";
		} else if(val == 999){
			return "市级示范";
		} else if(val == 99){
			return "县/区级示范";
		}
		return "非示范";
	}

	function formatterProperty(val, row){
        if(val == "Public"){
			return "公立";
		} else if(val == "Private"){
			return "私立";
		} else if(val == "Gov_Ent"){
			return "政企联办";
		} else if(val == "Gov_Pri"){
			return "民办公助";
		} else if(val == "Collectivity"){
			return "集体";
		} else if(val == "Enterprise"){
			return "企业";
		}
		return "其他";
	}

	function formatterStatus(val, row){
        if(val == 0){
			return "<font color='green'>有效</font>";
		}
		return "<font color='red'>无效</font>";
	}

	function addNew(){
	    var link="${contextPath}/sys/tenant/edit";
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


	function users(tenantId){
	    var link="${contextPath}/tenant/user?tenantId="+tenantId;
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "用户列表",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['980px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}


	function apps(tenantId, type){
	    var link="${contextPath}/sys/application/privilege?tenantId="+tenantId+"&type="+type+"&privilege=rw";
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "租户模块设置",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['980px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}


	function tcImage(tenantId){
        var link="${contextPath}/tenant/tcFile?tenantId="+tenantId;
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "营业执照",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['980px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}


	function onMyRowClick(rowIndex, row){
	    var link = '${contextPath}/sys/tenant/edit?id='+row.id;
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

   function editRow(id){
	    var link = '${contextPath}/sys/tenant/edit?id='+id;
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

	function deleteRow(id){
		if(confirm("数据删除后不能恢复，确定删除吗？")){
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/tenant/delete?id='+id,
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
	    var link = '${contextPath}/sys/tenant/edit?id='+row.id;
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
	    jQuery('#dlg').dialog('open').dialog('setTitle','租户信息查询');
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
		  var link = '${contextPath}/sys/tenant/edit?id='+selected.id;
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

	function users(){
	    var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    if(rows == null || rows.length !=1){
		    alert("请选择其中一条记录。");
		    return;
	    }
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected ){
		  var link = '${contextPath}/tenant/user?tenantId='+selected.tenantId;
		  jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "用户列表",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['980px', (jQuery(window).height() - 50) +'px'],
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
		    var link='${contextPath}/sys/tenant/edit?readonly=true&id='+selected.id;
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
		if(ids.length > 0 ){
		  if(confirm("数据删除后不能恢复，确定删除吗？")){
		    var str = ids.join(',');
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/tenant/delete?ids='+str,
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

	function searchData(){
        var params = jQuery("#searchForm").formSerialize();
        jQuery.ajax({
                    type: "POST",
                    url: '${contextPath}/sys/tenant/json',
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
	
	function searchData(){
        document.iForm.submit();
	}	 

	function loadGridData(url){
	    jQuery.ajax({
			type: "POST",
			url:  url,
			dataType: 'json',
			error: function(data){
				alert('服务器处理错误！');
			},
			success: function(data){
				jQuery('#mydatagrid').datagrid('loadData', data);
			}
		});
	}

	function searchXY(namePinyinLike){
        var link = "${contextPath}/sys/tenant/json?namePinyinLike="+namePinyinLike;
		loadGridData(link);
	}

</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north', split:false, border:true" style="height:72px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<form id="iForm" name="iForm" method="post" action="">
    <table>
      <tr>
	    <td>
			<img src="${contextPath}/static/images/window.png">
			&nbsp;<span class="x_content_title">租户信息列表</span>
			<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
			   onclick="javascript:addNew();">新增</a>  
			<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
			   onclick="javascript:editSelected();">修改</a>  
			<!-- <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
			   onclick="javascript:deleteSelections();">删除</a> --> 
			<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-user'"
			   onclick="javascript:users();">用户</a>
	    </td>
		<td>
		   等级&nbsp;
		   <select id="level" name="level">
			    <option value="">----请选择----</option>
				<option value="9999">省级示范</option>
				<option value="999">市级示范</option>
				<option value="99">县/区级示范</option>
				<option value="1">非示范</option>
			</select>
            <script type="text/javascript">
                document.getElementById("level").value="${level}";
            </script>
		</td>
		<td>
		  性质&nbsp;
		  <select id="property" name="property">
			    <option value="">----请选择----</option>
				<option value="Public">公立</option>
				<option value="Private">私立</option>
				<option value="Gov_Ent">政企联办</option>
				<option value="Gov_Pri">民办公助</option>
				<option value="Collectivity">集体</option>
				<option value="Enterprise">企业</option>
				<option value="Other">其他</option>
			</select>
            <script type="text/javascript">
                document.getElementById("property").value="${property}";
            </script>
		</td>
		<td>
		  省份&nbsp;
		  <select id="provinceId" name="provinceId" onchange="javascript:selectDistrict('provinceId', 'cityId');">
			    <option value="">----请选择----</option>
				<#list provinces as province>
				<option value="${province.id}">${province.name}</option>
			    </#list>
			</select>
            <script type="text/javascript">
			    //selectProvince("provinceId");
                document.getElementById("provinceId").value="${provinceId}";
            </script>
		</td>
		<td>
		  市&nbsp;
		  <select id="cityId" name="cityId" onchange="javascript:selectDistrict('cityId', 'areaId');">
			    <option value="">----请选择----</option>
				<#list citys as city>
				<option value="${city.id}">${city.name}</option>
			    </#list>
			</select>
            <script type="text/javascript">
			    <#if citys?exists>
				  document.getElementById("cityId").value="${cityId}";
				<#else>
				  selectDistrict("cityId", document.getElementById("provinceId").value);
				</#if>
            </script>
		</td>
		<td>
		  区县&nbsp;
		  <select id="areaId" name="areaId">
			    <option value="">----请选择----</option>
				<#list areas as area>
				<option value="${area.id}">${area.name}</option>
			    </#list>
			</select>
            <script type="text/javascript">
			    <#if areas?exists>
				  document.getElementById("areaId").value="${areaId}";
				<#else>
				  selectDistrict("areaId", document.getElementById("cityId").value);
				</#if>
            </script>
		</td>
		<td>
		    <button type="button" id="searchButton" class="btn btnGrayMini" style="width:60px" 
	                onclick="javascript:searchData();">查找</button>
		</td>
      </tr>
	  <tr>
		<td colspan="8">
			&nbsp;&nbsp;&nbsp;&nbsp;
			<#list charList as item>
			&nbsp;<span class="x_char_name" onclick="javascript:searchXY('${item}');">${item}</span>&nbsp;
			</#list>
		</td>
	  </tr>
    </table>
	</form>
   </div> 
  </div> 
  <div data-options="region:'center',border:true">
	 <table id="mydatagrid"></table>
  </div>  
</div>
</div>
</body>
</html>