<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>食谱成分汇总表</title>
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
				url: '${contextPath}/heathcare/dietaryCount/json?season=${season}&typeId=${typeId}&nameLike_enc=${nameLike_enc}&selected=${selected}&week=${week}&dayOfWeek=${dayOfWeek}&year=${year}&month=${month}&dateString=${dateString}',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        {title:'序号', field:'startIndex', width:60, sortable:false},
						{title:'日期',field:'fullDay', width:60, align:"center"},
						{title:'星期',field:'wname', width:60, align:"center"},
						{title:'大类',field:'catName', width:120, align:"left"},
						{title:'热能(千卡)',field:'heatEnergy', width:90, align:"right"},
						{title:'蛋白质(克)',field:'protein', width:90, align:"right"},
						{title:'脂肪(克)',field:'fat', width:90, align:"right"},
						{title:'碳水化合物(克)',field:'carbohydrate', width:120, align:"right"},
						{title:'微生素A(μgRE)',field:'vitaminA', width:120, align:"right"},
						{title:'微生素B1(毫克)',field:'vitaminB1', width:120, align:"right"},
						{title:'微生素B2(毫克)',field:'vitaminB2', width:120, align:"right"},
						{title:'微生素C(毫克)',field:'vitaminC', width:120, align:"right"},
						{title:'胡萝卜素(微克)',field:'carotene', width:120, align:"right"},
						{title:'视黄醇(微克)',field:'retinol', width:90, align:"right"},
						{title:'尼克酸(毫克)',field:'nicotinicCid', width:90, align:"right"},
						{title:'钙(毫克)',field:'calcium', width:90, align:"right"},
						{title:'铁(毫克)',field:'iron', width:90, align:"right"},
						{title:'锌(毫克)',field:'zinc', width:90, align:"right"},
						{title:'碘(毫克)',field:'iodine', width:90, align:"right"},
						{title:'磷(毫克)',field:'phosphorus', width:90, align:"right"}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 100,
				pageList: [10,15,20,25,30,40,50,100,200,500],
				pagePosition: 'both'
			});

			var p = jQuery('#mydatagrid').datagrid('getPager');
			jQuery(p).pagination({
				onBeforeRefresh:function(){
					//alert('before refresh');
				}
		    });
	});

    function cal(){
		var link='${contextPath}/heathcare/dietary/batchCal';
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "统计条件",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['680px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		   });
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
			<input type="hidden" id="elementId" name="elementId" value="${elementId}">
			<input type="hidden" id="elementName" name="elementName" value="${elementName}">
			<input type="hidden" id="selected" name="selected" value="${selected}">
			<table>
			<tr>
				<td>
				<img src="${contextPath}/static/images/window.png">
				 &nbsp;<span class="x_content_title">食谱成分汇总列表</span>
				</td>
				<td>
					日期&nbsp;<input id="dateString" name="dateString" type="text" 
							class="easyui-datebox x-text" style="width:120px" value="${dateString}">
				</td>
				<td>
					<button type="button" id="searchButton" class="btn btnGrayMini" style="width: 90px" 
							onclick="javascript:searchData();">查找</button>
				</td>
						<td>
					<button type="button" id="calButton" class="btn btnGrayMini" style="width: 90px" 
							onclick="javascript:cal();">统计</button>
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