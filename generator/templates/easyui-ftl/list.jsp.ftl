<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${tableDefinition.title}</title>
<# include "/inc/init_easyui_layer3_import.ftl"/>
<script type="text/javascript">


    function getLink(){
	    var link_ = "#F{contextPath}/${tableDefinition.moduleName}/${modelName}/json?q=1";
		var namePinyinLike = jQuery("#namePinyinLike").val();
		if(namePinyinLike != "" && namePinyinLike != "undefined" ){
		    link_ = link_ + "&namePinyinLike="+namePinyinLike;
		}
		return link_;
    }


    jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:1000,
				height:480,
				fit:true,
				fitColumns: true,
				nowrap: false,
				striped: true,
				collapsible: true,
				url: getLink(),
				remoteSort: false,
				singleSelect: true,
				idField: '${idField.name}',
				columns:[[
				        {title:'���', field:'startIndex', width:80, sortable:false},
					<#if pojo_fields?exists>
					<#list  pojo_fields as field>
					 <#if field.displayType == 4>
					{title:'${field.title?if_exists}',field:'${field.name}', width:120, sortable:true<#if field.type?exists && (field.type== 'Integer' || field.type== 'Long' || field.type== 'Double' ) >, align:'right'</#if>},
					 </#if>
					</#list>
					</#if>	 
					{field:'functionKey',title:'���ܼ�',width:120, formatter:formatterKeys}
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
		var str = "<a href='javascript:editRow(\""+row.id+"\");'>�޸�</a>&nbsp;<a href='javascript:deleteRow(\""+row.id+"\");'>ɾ��</a>";
	    return str;
	}
	

	function addNew(){
	    var link="#F{contextPath}/${tableDefinition.moduleName}/${modelName}/edit";
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "������¼",
		  area: ['1080px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //���̶�
		  shadeClose: true,
		  content: [link, 'no']
		});
	}


	function onMyRowClick(rowIndex, row){
	    var link = '#F{contextPath}/${tableDefinition.moduleName}/${modelName}/edit?id='+row.id;
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "�༭��¼",
		  area: ['1080px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //���̶�
		  shadeClose: true,
		  content: [link, 'no']
		});
	}

    function editRow(id){
	    var link = '#F{contextPath}/${tableDefinition.moduleName}/${modelName}/edit?id='+id;
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "�༭��¼",
		  area: ['1080px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //���̶�
		  shadeClose: true,
		  content: [link, 'no']
		});
	}

	
	function deleteRow(id){
		if(confirm("����ɾ�����ָܻ���ȷ��ɾ����")){
			jQuery.ajax({
				   type: "POST",
				   url: '#F{contextPath}/${tableDefinition.moduleName}/${modelName}/delete?id='+id,
				   dataType: 'json',
				   error: function(data){
					   alert('�������������');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } else {
						   alert('�����ɹ���ɣ�');
					   }
					   if(data.statusCode == 200){
					       jQuery('#mydatagrid').datagrid('reload');
					   }
				   }
			 });
		  }
	}

	
	function onRowClick(rowIndex, row){
	    var link = '#F{contextPath}/${tableDefinition.moduleName}/${modelName}/edit?${idField.name}='+row.id;
	    layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "�༭��¼",
		  area: ['1080px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //���̶�
		  shadeClose: true,
		  content: [link, 'no']
		});
	}

	function searchWin(){
	    jQuery('#dlg').dialog('open').dialog('setTitle','${tableDefinition.title}��ѯ');
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
		  alert("��ѡ������һ����¼��");
		  return;
	    }
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected ){
		  var link = '#F{contextPath}/${tableDefinition.moduleName}/${modelName}/edit?${idField.name}='+selected.id;
		  layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "�༭��¼",
			  area: ['1080px', (jQuery(window).height() - 50) +'px'],
			  shade: 0.8,
			  fixed: false, //���̶�
			  shadeClose: true,
			  content: [link, 'no']
		  });
	    }
	}

	function viewSelected(){
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		if(rows == null || rows.length !=1){
			alert("��ѡ������һ����¼��");
			return;
		}
		var selected = jQuery('#mydatagrid').datagrid('getSelected');
		if (selected ){
		    var link='#F{contextPath}/${tableDefinition.moduleName}/${modelName}/edit?readonly=true&${idField.name}='+selected.id;
		    layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "�༭��¼",
			  area: ['1080px', (jQuery(window).height() - 50) +'px'],
			  shade: 0.8,
			  fixed: false, //���̶�
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
		  if(confirm("����ɾ�����ָܻ���ȷ��ɾ����")){
		    var str = ids.join(',');
			jQuery.ajax({
				   type: "POST",
				   url: '#F{contextPath}/${tableDefinition.moduleName}/${modelName}/delete?${idField.name}s='+str,
				   dataType: 'json',
				   error: function(data){
					   alert('�������������');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } else {
						   alert('�����ɹ���ɣ�');
					   }
					   if(data.statusCode == 200){
					       jQuery('#mydatagrid').datagrid('reload');
					   }
				   }
			 });
		  }
		} else {
			alert("��ѡ������һ����¼��");
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
			url: url,
			dataType: 'json',
			error: function(data){
				alert('�������������');
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
                    url: '#F{contextPath}/${tableDefinition.moduleName}/${modelName}/json',
                    dataType: 'json',
                    data: params,
                    error: function(data){
                              alert('�������������');
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
   <div data-options="region:'north',split:false, border:true" style="height:48px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	  <table width="100%" align="left">
		<tbody>
		 <tr>
		    <td width="55%" align="left">
				<img src="#F{contextPath}/static/images/window.png">
				&nbsp;<span class="x_content_title">${tableDefinition.title}�б�</span>
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
				   onclick="javascript:addNew();">����</a>  
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
				   onclick="javascript:editSelected();">�޸�</a>  
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
				   onclick="javascript:deleteSelections();">ɾ��</a> 
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-search'"
				   onclick="javascript:searchWin();">����</a>
			</td>
			<td width="45%" align="left">&nbsp;</td>
		</tr>
	   </tbody>
	  </table>
   </div> 
  </div> 
  <div data-options="region:'center',border:true">
	 <table id="mydatagrid"></table>
  </div>  
</div>
</div>
</body>
</html>