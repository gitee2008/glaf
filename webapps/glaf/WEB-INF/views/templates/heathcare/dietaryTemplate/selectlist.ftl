<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>食谱模板</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

    var x_height = Math.floor(window.screen.height * 0.68);
	var x_width = Math.floor(window.screen.width * 0.80);

	if(window.screen.height <= 768){
        x_height = Math.floor(window.screen.height * 0.66);
	}

	if(window.screen.width < 1200){
        x_width = Math.floor(window.screen.width * 0.82);
	} else if(window.screen.width > 1280){
        x_width = Math.floor(window.screen.width * 0.72);
	}  


   jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:1950,
				height:x_height,
				fit:false,
				fitColumns: true,
				nowrap: false,
				striped: true,
				collapsible: true,
				url: '${contextPath}/heathcare/dietaryTemplate/json?season=${season}&typeId=${typeId}&nameLike_enc=${nameLike_enc}&selected=${selected}&suitNo=${suitNo}&sysFlag=${sysFlag}&dayOfWeek=${dayOfWeek}',
				remoteSort: false,
				idField: 'id',
				columns:[[
						{title:'选择',field: 'chk', width: 60, align: 'center', formatter: formatterKey},
				        {title:'序号',field:'startIndex', width:60, sortable:false},
						{title:'名称',field:'name', width:200, align:"left"},
						{title:'餐点',field:'typeName', width:130, align:"center"},
						{title:'模板',field:'suitNo', width:60, align:"center"},
						{title:'日期',field:'dayOfWeekName', width:60, align:"center"},
						{title:'季节',field:'seasonName', width:60, align:"center"},
						{title:'热能',field:'heatEnergy', width:90, align:"right", sortable:true},
						{title:'蛋白质',field:'protein', width:90, align:"right", sortable:true},
						{title:'碳水化合物',field:'carbohydrate', width:120, align:"right", sortable:true},
						{title:'微生素A',field:'vitaminA', width:120, align:"right", sortable:true},
						{title:'微生素B1',field:'vitaminB1', width:120, align:"right", sortable:true},
						{title:'微生素B2',field:'vitaminB2', width:120, align:"right", sortable:true},
						{title:'微生素C',field:'vitaminC', width:120, align:"right", sortable:true},
						{title:'胡萝卜素',field:'carotene', width:120, align:"right", sortable:true},
						{title:'视黄醇',field:'retinol', width:90, align:"right", sortable:true},
						{title:'尼克酸',field:'nicotinicCid', width:90, align:"right", sortable:true},
						{title:'钙',field:'calcium', width:90, align:"right", sortable:true},
						{title:'铁',field:'iron', width:90, align:"right", sortable:true},
						{title:'锌',field:'zinc', width:90, align:"right", sortable:true},
						{title:'碘',field:'iodine', width:90, align:"right", sortable:true},
						{title:'磷',field:'phosphorus', width:90, align:"right", sortable:true}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 100,
				pageList: [10,15,20,25,30,40,50,100,200],
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


	function formatterKey(value, row, index) {
		var s = '<input name="isCheck" type="checkbox" value="'+row.id+'" /> ';
	    return s;
	}


	function onMyRowClick(rowIndex, row){
	    var link = '${contextPath}/heathcare/dietaryItem/datalist?templateId='+row.templateId;
	    jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "食品组成列表",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['698px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function searchData(){
       document.iForm.submit();
	}

	function addBatch(){
         var objectIds = $("input[name='isCheck']:checked").map(function () {
               return $(this).val();
           }).get().join(',');
		//alert(objectIds);
        var link = '${contextPath}/heathcare/dietary/batchEdit?objectIds='+objectIds;
	    jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "批量加入食谱",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['698px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}


	function checkAll() {
		//jQuery("input[name='isCheck']").attr("checked", true); 
		jQuery("[name = isCheck]:checkbox").attr("checked", true);
	}

	function uncheckAll() {
		//jQuery("input[name='isCheck']").attr("checked", false); 
		jQuery("[name = isCheck]:checkbox").attr("checked", false);
	}

	$("#checkButton1").click(function(){ 
	  $("input[name='isCheck']").attr("checked","true"); 
	})

	$("#checkButton2").click(function(){ 
	  $("input[name='isCheck']").removeAttr("checked"); 
	})


    $("#checkButton11").bind("click", function () {
        $("[name = isCheck]:checkbox").attr("checked", true);
    });

    // 全不选
    $("#checkButton12").bind("click", function () {
        $("[name = isCheck]:checkbox").attr("checked", false);
    });

</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north',split:false,border:true" style="height:72px"  class="toolbar-backgroud"> 
    <div style="margin:4px;">
	<form id="iForm" name="iForm" method="post" action="">
	<input type="hidden" id="elementId" name="elementId" value="${elementId}">
	<input type="hidden" id="elementName" name="elementName" value="${elementName}">
	<input type="hidden" id="selected" name="selected" value="${selected}">
    <table>
      <tr>
	    <td colspan="8">
		<img src="${contextPath}/static/images/window.png">
	       &nbsp;<span class="x_content_title">食谱库列表</span>
		   &nbsp;名称&nbsp;
		   <input id="nameLike" name="nameLike" type="text" class="x-searchtext"  
	              style="width:185px;" value="${nameLike}">
		</td>
	  </tr>
	  <tr>
		<td>
		    &nbsp;餐点&nbsp;
			<#if typeDict?exists>
			<span style="color:#0066ff;font-weight:bold;">${typeDict.name}</span>
			<input type="hidden" id="typeId" name="typeId" value="${typeDict.id}">
			<#else>
			<select id="typeId" name="typeId">
				<option value="">----请选择----</option>
				<#list dictoryList as d>
				<option value="${d.id}">${d.name}</option>
				</#list> 
		    </select>
		    <script type="text/javascript">
			   document.getElementById("typeId").value="${typeId}";
		    </script>
			</#if>
		</td>
		<td>
          &nbsp;模板类型&nbsp;
		  <select id="sysFlag" name="sysFlag">
			<option value="">----请选择----</option> 
			<option value="Y">系统内置</option>
			<option value="N">我自己的</option>
		  </select>
		  <script type="text/javascript">
			   document.getElementById("sysFlag").value="${sysFlag}";
		  </script>
		</td>
		<td>
          &nbsp;模板序号&nbsp;
		  <select id="suitNo" name="suitNo">
			<option value="">----请选择----</option>
			<#list suitNos as suitNo>
			<option value="${suitNo}">第${suitNo}套</option>
			</#list>  
		  </select>
		  <script type="text/javascript">
			   document.getElementById("suitNo").value="${suitNo}";
		  </script>
		</td>
		<td>
		  &nbsp;日期&nbsp;
		  <select id="dayOfWeek" name="dayOfWeek">
			<option value="0">----请选择----</option>
			<option value="1">星期一</option>
			<option value="2">星期二</option>
			<option value="3">星期三</option>
			<option value="4">星期四</option>
			<option value="5">星期五</option>
			<option value="6">星期六</option>
			<option value="7">星期日</option>
		  </select>
		  <script type="text/javascript">
			   document.getElementById("dayOfWeek").value="${dayOfWeek}";
		  </script>
	    </td>
		<!-- <td>
            &nbsp;省份&nbsp;
			<select id="province" name="province">
			 <option value="">----请选择----</option>
			 <#list districts as district>
			 <option value="${district.name}">${district.name}</option>
			 </#list>
		    </select>
		    <script type="text/javascript">
			   document.getElementById("province").value="${province}";
		    </script>    
		</td> -->
		<td>
		    <button type="button" id="searchButton" class="btn btnGrayMini" style="width: 60px" 
	                onclick="javascript:searchData();">查找</button>

			<button type="button" id="checkButton111" class="btn btnGrayMini" style="width: 60px" 
	                onclick="javascript:checkAll();">全选</button>
			
			<!-- <button type="button" id="checkButton112" class="btn btnGrayMini" style="width: 80px" 
	                onclick="javascript:uncheckAll();">全不选</button> -->
            <#if sysFlag?exists && typeId?exists && suitNo?exists && dayOfWeek?exists && dayOfWeek &gt; 0>
		    <button type="button" id="batchButton" class="btn btnGrayMini" style="width: 90px" 
	                onclick="javascript:addBatch();">批量加入</button>
			</#if>
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