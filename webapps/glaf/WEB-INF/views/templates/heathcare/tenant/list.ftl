<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>学校信息列表</title>
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
				url: '${contextPath}/tenant/json?provinceId=${provinceId}&cityId=${cityId}&areaId=${areaId}&level=${level}&property=${property}&type=district',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        {title:'序号', field:'startIndex', width:60, sortable:false},
						{title:'编号', field:'id', width:90},
						{title:'名称', field:'name', width:180},
						{title:'代码', field:'code', width:120},
						{title:'等级', field:'level', width:120, formatter:formatterLevel},
						{title:'性质', field:'property', width:120, formatter:formatterProperty},
						{title:'负责人', field:'principal', width:90},
						{title:'电话', field:'telephone', width:90},
						{title:'功能键', field:'functionKey', width:150, formatter:formatterKeys}
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

	function formatterKeys(val, row){
		var str = "<a href='javascript:info(\""+row.tenantId+"\");'>膳食信息</a>&nbsp;<a href='javascript:grades(\""+row.tenantId+"\");'>体检信息</a>&nbsp;";
	    return str;
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


	function info(tenantId){
	    var link="${contextPath}/heathcare/tenantReportMain/main?tenantId="+tenantId;
		window.open(link);
	}


	function grades(tenantId){
	    var link="${contextPath}/heathcare/tenantReportMain/grades?tenantId="+tenantId;
		window.open(link);
	}

	function onMyRowClick(rowIndex, row){
	    var link = '${contextPath}/heathcare/tenantReportMain/main?tenantId='+row.tenantId;
	    //window.open(link);
		var x=100;
        var y=100;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link,self,x, y, 1525, 680);
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
</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<form id="iForm" name="iForm" method="post" action="">
    <table>
      <tr>
	    <td>
			<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">学校信息列表</span>
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
		</td>
		<td>
		    <button type="button" id="searchButton" class="btn btnGrayMini" style="width:60px" 
	                onclick="javascript:searchData();">查找</button>
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