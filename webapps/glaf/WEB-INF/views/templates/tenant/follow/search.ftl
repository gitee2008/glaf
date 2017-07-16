<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>单位信息</title>
<#include "/inc/init_easyui_import.ftl"/>
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
				url: '${contextPath}/tenant/follow/tenantJson?nameLike_enc=${nameLike_enc}',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        {title:'序号', field:'startIndex', width:80, sortable:false},
						{title:'编号', field:'id', width:120},
						{title:'名称', field:'name', width:120},
						{title:'代码', field:'code', width:120},
						{title:'省', field:'province', width:120},
						{title:'市', field:'city', width:120},
						{title:'负责人', field:'principal', width:120},
						{title:'创建人', field:'createBy', width:120},
						{title:'创建日期', field:'createTime', width:120},
						{title:'是否有效', field:'locked', width:120, formatter:formatterStatus},
						{title:'功能键', field:'functionKey', width:120, formatter:formatterKeys}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 10,
				pageList: [10,15,20,25,30,40,50,100],
				pagePosition: 'both' 
			});

			var p = jQuery('#mydatagrid').datagrid('getPager');
			jQuery(p).pagination({
				onBeforeRefresh:function(){
					//alert('before refresh');
				}
		    });
	});

	function formatterKeys(val, row){
		var str = "<a href='javascript:addFollow(\""+row.tenantId+"\", \""+row.name+"\");'>关注</a>";
	    return str;
	}
	
	function formatterStatus(val, row){
        if(val == 0){
			return "<font color='green'>有效</font>";
		}
		return "<font color='red'>无效</font>";
	}

	function addFollow(followTenantId, name){
		if(confirm("确定关注"+name+"吗？")){
			var link="${contextPath}/tenant/follow/saveTenantFollow?followTenantId="+followTenantId;
			jQuery.ajax({
				   type: "POST",
				   url: link,
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
					        
					   }
				   }
			 });
		  }
	}

	function doSearch(){
        document.iForm.submit();
	}

</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north',split:true,border:true" style="height:48px"> 
    <div class="toolbar-backgroud"  > 
	<form id="iForm" name="iForm" method="post" action="">
	<table>
	<tr>
	  <td>
		<img src="${contextPath}/static/images/window.png">
		&nbsp;<span class="x_content_title">机构信息列表</span>
	  </td>
	  <td>
        <input type="text" id="nameLike" name="nameLike" value="${nameLike}" class="x-searchtext">  
	  </td>
	  <td>&nbsp;
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-search'" size="30"
		   onclick="javascript:doSearch();">查找</a>
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