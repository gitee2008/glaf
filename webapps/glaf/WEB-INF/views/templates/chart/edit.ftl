<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>图表定义</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

    function initData(){
        	<#if  chart?exists >
           // $('#iForm').form('load','${contextPath}/rs/chart/view/${chart.id}');
        	</#if>
    }

    function checkForm(){
        if(jQuery("#subject").val() == ''){
            alert('主题是必须的！');
            document.getElementById("subject").focus();
            return false;
        }
        if(jQuery("#chartName").val() == ''){
            alert('图表名称是必须的！');
            document.getElementById("chartName").focus();
            return false;
        }
        if(jQuery("#chartTitle").val() == ''){
            alert('图表主题是必须的！');
            document.getElementById("chartTitle").focus();
            return false;
        }
        if(jQuery("#chartType").val() == ''){
            alert('图表类型是必须的！');
            document.getElementById("chartType").focus();
            return false;
        }
        if(jQuery("#chartFont").val() == ''){
            alert('图表字体是必须的！');
            document.getElementById("chartFont").focus();
            return false;
        }
        if(jQuery("#chartFontSize").val() == ''){
            alert('图表字体大小是必须的！');
            document.getElementById("chartFontSize").focus();
            return false;
        }
        if(jQuery("#chartTitleFont").val() == ''){
            alert('图表标题栏字体是必须的！');
            document.getElementById("chartTitleFont").focus();
            return false;
        }
        if(jQuery("#chartTitleFontSize").val() == ''){
            alert('图表标题栏字体大小是必须的！');
            document.getElementById("chartTitleFontSize").focus();
            return false;
        }
        if(jQuery("#chartWidth").val() == ''){
            alert('图表宽度是必须的！');
            document.getElementById("chartWidth").focus();
            return false;
        }
        if(jQuery("#chartHeight").val() == ''){
            alert('图表高度是必须的！');
            document.getElementById("chartHeight").focus();
            return false;
        }

        return true;
    }

    function saveData(){
        if(checkForm()){
            var params = jQuery("#iForm").formSerialize();
            jQuery.ajax({
				type: "POST",
				url: '${contextPath}/rs/chart/saveChart',
				data: params,
				dataType: 'json',
				error: function(data){
					   alert('服务器处理错误！');
				},
				success: function(data){
					   if(data.message != null){
						   alert(data.message);
					   } else {
						   alert('操作成功完成！');
					   }
				}
           });
        }
    }

    function saveAsData(){
        if(checkForm()){
            document.getElementById("id").value="";
            document.getElementById("chartId").value="";
            var params = jQuery("#iForm").formSerialize();
            jQuery.ajax({
				type: "POST",
				url: '${contextPath}/rs/chart/saveChart',
				data: params,
				dataType: 'json',
				error: function(data){
					   alert('服务器处理错误！');
				},
				success: function(data){
					   if(data.message != null){
						  alert(data.message);
					   } else {
						  alert('操作成功完成！');
					   }
				}
           });
         }
    }

    function viewChart(){
        var databaseId = jQuery("#databaseId").val();
        window.open('${contextPath}/chart/showChart?chartId=${chart.id}&databaseId='+databaseId);
    }

    function viewHighCharts(){
        var databaseId = jQuery("#databaseId").val();
        window.open('${contextPath}/chart/highcharts/showChart?chartId=${chart.id}&chooseThemes=true&databaseId='+databaseId);
    }

    function viewECharts(){
       var databaseId = jQuery("#databaseId").val();
        window.open('${contextPath}/chart/echarts/showChart?chartId=${chart.id}&chooseThemes=true&databaseId='+databaseId);
    }

    function viewKendoChart(){
       var databaseId = jQuery("#databaseId").val();
       window.open('${contextPath}/chart/kendo/showChart?chartId=${chart.id}&chooseThemes=true&databaseId='+databaseId);
    }

    function openQx(){
        var selected = jQuery("#queryIds").val();
        var link = '${contextPath}/sys/sql/definition/choose?elementId=queryIds&elementName=queryNames&nodeCode=report_category&selected='+selected;
        var x=100;
        var y=100;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link,self,x, y, 695, 480);
    }

    function openDataSet(){
        var link = '${contextPath}/chart/chartDatasetList?elementId=datasetIds&elementName=datasetNames&chartId=${chart.id}&xtype=1';
        var x=100;
        var y=100;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-100;
        	y=document.body.scrollTop+event.clientY-event.offsetY-100;
        }
       // openWindow(link,self,x, y, 995, 560);
	   jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "选择数据集",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['920px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
    }

    function openDataSet2(){
        var link = '${contextPath}/chart/chartDatasetList?elementId=secondDatasetIds&elementName=dataset2Names&chartId=${chart.id}&xtype=2';
        var x=100;
        var y=100;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-100;
        	y=document.body.scrollTop+event.clientY-event.offsetY-100;
        }
        //openWindow(link,self,x, y, 995, 560);
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "选择第二维度数据集",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['920px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
    }

    function openQx2(){
        var link = '${contextPath}/chart/chooseQuery?chartId=${chart.id}&elementId=queryIds&elementName=queryNames';
        var x=100;
        var y=100;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link,self,x, y, 695, 480);
    }

	function check_double(xx){
		var berr = true;
		if (!((event.keyCode>=45 && event.keyCode<47 ) || (event.keyCode>=48 && event.keyCode<=57))) {
            alert("该字段只能输入数字！");
            return false;
		 }
		 var x = xx.value;
		 if(x.length >20){
             return false;
		 }
		 return true;
	 }

    function checkInputFloat(elementId){
		return check_double(document.getElementById(elementId).value)
	}
</script>
</head>

<body>
 <br/>
	<div class="x_content_title"><img
	src="${contextPath}/static/images/window.png"
	alt="图表定义"> &nbsp;图表定义
	</div>
 <br/>
	 <form id="iForm" name="iForm" method="post">
	    <input type="hidden" id="id" name="id" value="${chart.id}"/>
	    <input type="hidden" id="chartId" name="chartId" value="${chart.id}"/>
	    <table  style="width:900px;height:250px" align="center">
		<tbody>
		<#if treeModels?exists>
        <tr>
        	<td width="12%" align="left"><label for="nodeId" >分类</label></td>
        	<td width="38%" align="left">
        	  <select id="nodeId" name="nodeId">
        		   <option value="">----请选择----</option>
        		   <#list  treeModels as treeModel >
        		   <option value="${treeModel.id}">${treeModel.name}</option>
					<#list  treeModel.children as child>
					<option value="${child.id}">&nbsp;&nbsp;---->${child.name}</option>
					  <#list  child.children as cd >
					  <option value="${cd.id}">&nbsp;&nbsp;&nbsp;&nbsp;-------->${cd.name}</option>
						<#list  cd.children as c>
						<option value="${c.id}">&nbsp;&nbsp;&nbsp;&nbsp;------------>${c.name}</option>
						  <#list c.children as x>
							<option value="${x.id}">&nbsp;&nbsp;&nbsp;&nbsp;---------------->${x.name}</option>
						  </#list>
						</#list>
					  </#list>
					</#list>
        		   </#list>
        	  </select>
        	  <script type="text/javascript">
        		   document.getElementById("nodeId").value="${chart.nodeId}";
        	  </script>
        	</td>
        	<td width="12%" align="left">&nbsp;</td>
        	<td width="38%" align="left">&nbsp;</td>
        </tr>
        </#if>

        <tr>
        	 <td height="28"><label for="subject" >标题</label></td>
        	 <td colspan="3" height="28">
              <input id="subject" name="subject" class="span8 x-text" type="text"
                     value="${chart.subject}" size="102"></input>
        	 </td>
         </tr>

         <tr>
        	 <td height="28"><label for="chartName" >图表名称</label></td>
        	 <td height="28">
              <input id="chartName" name="chartName" class="span3 x-text" type="text"
                value="${chart.chartName}" size="30"></input>
        	 <td height="28">别名</td>
        	 <td height="28">
        	 <input id="mapping" name="mapping" class="span3 x-text" type="text"
                value="${chart.mapping}" size="30"></input>
        	 </td>
        </tr>

        <tr>
        	 <td height="28">X坐标标签</td>
        	 <td height="28">
              <input id="coordinateX" name="coordinateX" class="span3 x-text" type="text"
                     value="${chart.coordinateX}" size="30"></input>
        	 </td>
        	 <td height="28">Y坐标标签</td>
        	 <td height="28">
              <input id="coordinateY" name="coordinateY" class="span3 x-text" type="text"
                     value="${chart.coordinateY}" size="30"></input>
        	 </td>
        </tr>

        <tr>
        	 <td height="28">第二轴X坐标标签</td>
        	 <td height="28">
              <input id="secondCoordinateX" name="secondCoordinateX" class="span3 x-text" type="text"
                     value="${chart.secondCoordinateX}" size="30"></input>
        	 </td>
        	 <td height="28">第二轴Y坐标标签</td>
        	 <td height="28">
              <input id="secondCoordinateY" name="secondCoordinateY" class="span3 x-text" type="text"
                     value="${chart.secondCoordinateY}" size="30"></input>
        	 </td>
        </tr>

        <tr>
        	 <td height="28">图表主题</td>
        	 <td height="28">
              <input id="chartTitle" name="chartTitle" class="span3 x-text" type="text"
                     value="${chart.chartTitle}" size="30"></input>
        	 </td>
           <td height="28">图表次标题</td>
        	 <td height="28">
              <input id="chartSubTitle" name="chartSubTitle" class="span3 x-text" type="text"
                     value="${chart.chartSubTitle}" size="30"></input>
        	 </td>
        </tr>

        <tr>
        	 <td height="28">图表宽度</td>
        	 <td height="28">
                 <select id="chartWidth" name="chartWidth" value="${chart.chartWidth}" 
               	        class="span2" style="height:20px; width:90px;">
                     <#list scales as scale>
					  <option value="${scale}">${scale}</option>
					 </#list>
        		 </select>
        		 <script type="text/javascript">
                   $('#chartWidth').val('${chart.chartWidth}');
        		 </script>
        	 </td>
        	 <td height="28">图表高度</td>
        	 <td height="28">
                 <select id="chartHeight" name="chartHeight" value="${chart.chartHeight}" 
               	         class="span2" style="height:20px; width:90px;">
				    <#list scales as scale>
					 <option value="${scale}">${scale}</option>
					</#list>
        		 </select>
        		 <script type="text/javascript">
                   $('#chartHeight').val('${chart.chartHeight}');
        		 </script>
        	 </td>
        </tr>

        <tr>
        	 <td height="28">图表标题栏字体</td>
        	 <td height="28">
        		 <select id="chartTitleFont"  name="chartTitleFont" value="${chart.chartTitleFont}" 
               	         class="span2" style="height:20px; width:90px;">
				   <option value="宋体">宋体</option>
				   <option value="新宋体">新宋体</option>
				   <option value="微软雅黑">微软雅黑</option>
				   <option value="Arial">Arial</option>
				   <option value="Consolas">Consolas</option>
				   <option value="Courier">Courier</option>
				   <option value="MS Serif">MS Serif</option>
        		 </select>
        		 <script type="text/javascript">
                     $('#chartTitleFont').val('${chart.chartTitleFont}');
        		 </script>
        	 </td>
        	 <td height="28">图表标题栏字体大小</td>
        	 <td height="28">
        		 <select id="chartTitleFontSize"  name="chartTitleFontSize" value="${chart.chartTitleFontSize}" 
               	 class="span2" style="height:20px; width:90px;">
				   <option value="9">9</option>
				   <option value="10">10</option>
				   <option value="12">12</option>
				   <option value="13">13</option>
				   <option value="14">14</option>
				   <option value="15">15</option>
				   <option value="16">16</option>
				   <option value="18">18</option>
				   <option value="20">20</option>
				   <option value="24">24</option>
				   <option value="32">32</option>
				   <option value="36">36</option>
				   <option value="48">48</option>
        		 </select>
        		 <script type="text/javascript">
                    $('#chartTitleFontSize').val('${chart.chartTitleFontSize}');
        		 </script>
        	 </td>
        </tr>

        <tr>
        	<td height="28">图表类型</td>
        	 <td height="28">
        		 <select id="chartType" name="chartType" value="${chart.chartType}" class="span2" style="height:20px">
				   <option value="pie">饼图</option>
				   <option value="donut">环形图</option>
				   <option value="line">线形图</option>
				   <!-- <option value="line_sum">曲线汇总图</option> -->
				   <option value="radarLine">雷达图</option>
				   <option value="bar">条形图</option>
				   <option value="bar_line">条形曲线图</option>
				   <option value="bar_line_sum">条形曲线汇总图</option>
				   <option value="column">柱状图</option>
				   <option value="column_line">双Y轴柱状曲线图</option>
				   <option value="column_line_sum">柱状曲线汇总图</option>
				   <option value="funnel">漏斗图</option>
				   <option value="gauge">仪表盘</option> 
				   <option value="area">面积图</option>
				   <option value="stacked_area">堆叠面积图</option>
				   <option value="stackedbar">堆叠柱状图</option>
				   <option value="stackedbar_line">堆叠柱状曲线图</option>
				   <option value="stackedbar_line_sum">堆叠柱状曲线汇总图</option>
        		 </select>
        		 <script type="text/javascript">
                    $('#chartType').val('${chart.chartType}');
        		 </script>
        	 </td>
        	 <td height="28">图表次标题字体大小</td>
        	 <td height="28">
        		 <select id="chartSubTitleFontSize"  name="chartSubTitleFontSize" value="${chart.chartSubTitleFontSize}" 
               	 class="span2" style="height:20px; width:90px;">
				   <option value="9">9</option>
				   <option value="10">10</option>
				   <option value="12">12</option>
				   <option value="13">13</option>
				   <option value="14">14</option>
				   <option value="15">15</option>
				   <option value="16">16</option>
				   <option value="18">18</option>
				   <option value="20">20</option>
				   <option value="24">24</option>
				   <option value="32">32</option>
				   <option value="36">36</option>
				   <option value="48">48</option>
        		 </select>
        		 <script type="text/javascript">
                     $('#chartSubTitleFontSize').val('${chart.chartSubTitleFontSize}');
        		 </script>
        	 </td>
        </tr>

        <tr>
        	 <td height="28">图表字体</td>
        	 <td height="28">
        	  <select id="chartFont"  name="chartFont" value="${chart.chartFont}" 
               	 class="span2" style="height:20px; width:90px;">
				   <option value="宋体">宋体</option>
				   <option value="新宋体">新宋体</option>
				   <option value="微软雅黑">微软雅黑</option>
				   <option value="Arial">Arial</option>
				   <option value="Consolas">Consolas</option>
				   <option value="Courier">Courier</option>
				   <option value="MS Serif">MS Serif</option>
        	  </select>
        	  <script type="text/javascript">
                 $('#chartFont').val('${chart.chartFont}');
        	  </script>
        	 </td>
        	 <td height="28">图表字体大小</td>
        	 <td height="28">
        	  <select id="chartFontSize"  name="chartFontSize" value="${chart.chartFontSize}" 
               	 class="span2" style="height:20px; width:90px; ">
				   <option value="9">9</option>
				   <option value="10">10</option>
				   <option value="12">12</option>
				   <option value="13">13</option>
				   <option value="14">14</option>
				   <option value="15">15</option>
				   <option value="16">16</option>
				   <option value="18">18</option>
				   <option value="20">20</option>
				   <option value="24">24</option>
				   <option value="32">32</option>
				   <option value="36">36</option>
				   <option value="48">48</option>
        		 </select>
        		 <script type="text/javascript">
                    $('#chartFontSize').val('${chart.chartFontSize}');
        		 </script>
        	 </td>
        </tr>
        
        <tr>
        	 <td height="28">渲染类型</td>
        	 <td height="28">
        	   <select id="imageType" name="imageType" value="${chart.imageType}" class="span2" 
                 style="height:20px; width:90px;">
        		<option value="png">JFreeCharts</option>
        		<option value="kendo">KendoCharts</option>
        		<option value="echarts">ECharts</option>
        		<option value="highcharts">Highcharts</option>
        	   </select>
        	   <script type="text/javascript">
                   $('#imageType').val('${chart.imageType}');
        	   </script>
        	 </td>
        	 <td height="28">显示图例</td>
        	 <td height="28">
        	   <select id="legend" name="legend" class="span2" style="height:20px; width:90px;">
        		<option value="Y">是</option>
        		<option value="N">否</option>
        	   </select>
        	   <script type="text/javascript">
                   $('#legend').val('${chart.legend}');
        	   </script>
        	 </td>
        </tr>

        <tr>
        	 <td height="28">是否启用</td>
        	 <td height="28">
        	  <select id="enableFlag" name="enableFlag" class="span2" style="height:20px; width:90px;">
        		<option value="1">是</option>
        		<option value="0">否</option>
        	  </select>
        	  <script type="text/javascript">
                 $('#enableFlag').val('${chart.enableFlag}');
        	  </script>
        	 </td>
        	 <td height="28">启用3D效果</td>
        	 <td height="28">
        	  <select id="enable3DFlag" name="enable3DFlag" class="span2" style="height:20px ;width:90px;">
        		<option value="1">是</option>
        		<option value="0">否</option>
        	  </select> (如果支持3D效果)
        	  <script type="text/javascript">
                $('#enable3DFlag').val('${chart.enable3DFlag}');
        	  </script>
        	 </td>
        </tr>

        <tr>
        	 <td height="28">最大刻度</td>
        	 <td height="28">
        	  <input id="maxScale" name="maxScale" class="span3 x-text" type="text"
                 value="${chart.maxScale}" size="10" onKeyPress="return checkInputFloat('maxScale');"></input>
        	 </td>
        	 <td height="28">最小刻度</td>
        	 <td height="28">
        	  <input id="minScale" name="minScale" class="span3 x-text" type="text"
                 value="${chart.minScale}" size="10" onKeyPress="return checkInputFloat('maxScale');"></input>
        	 </td>
        </tr>

        <tr>
        	 <td height="28">刻度间隔</td>
        	 <td height="28">
             <input id="stepScale" name="stepScale" class="span3 x-text" type="text"
                 value="${chart.stepScale}" size="10" onKeyPress="return checkInputFloat('maxScale');"></input>
        	 </td>
        	 <td height="28">最大显示记录条数</td>
        	 <td height="28">
              <select id="maxRowCount" name="maxRowCount" class="span2" style="height:20px; width:90px;">
        		<option value="5">5</option>
        		<option value="10">10</option>
        		<option value="15">15</option>
        		<option value="20">20</option>
        		<option value="99">不限制</option>
              </select> (超过限制的记录将不显示)
              <script type="text/javascript">
                 $('#maxRowCount').val('${chart.maxRowCount}');
              </script>
        	 </td>
        </tr>

        <tr>
        	 <td height="28">启用渐变效果</td>
        	 <td height="28">
        	  <select id="gradientFlag" name="gradientFlag" class="span2" style="height:20px; width:90px;">
        		<option value="1">是</option>
        		<option value="0">否</option>
        	  </select>
        	  <script type="text/javascript">
                 $('#gradientFlag').val('${chart.gradientFlag}');
        	  </script>
        	 </td>
        	 <td height="28">样式</td>
        	 <td height="28">
              <select id="theme" name="theme" style="height:20px; width:90px;">
        	   <option value="">----请选择----</option>
               <option value="default">默认</option>
               <option value="red">红色</option>
               <option value="gray">灰色</option>
               <option value="blue">蓝色</option>
               <option value="dark">dark</option>
               <option value="grid">网格 (grid)[Highcharts]</option>
               <option value="grid-light">grid-light[Highcharts]</option>
               <option value="skies">天空 (skies)[Highcharts]</option>
               <option value="dark-blue">深蓝 (dark-blue)[Highcharts]</option>
               <option value="dark-green">深绿 (dark-green)[Highcharts]</option>
               <option value="dark-unica">dark-unica[Highcharts]</option>
               <option value="sand-signika">sand-signika[Highcharts]</option>
               <option value="helianthus">helianthus[ECharts]</option>
               <option value="infographic">infographic[ECharts]</option>
               <option value="macarons">macarons[ECharts]</option>
               <option value="macarons2">macarons2[ECharts]</option>
               <option value="mint">mint[ECharts]</option>
               <option value="sakura">sakura[ECharts]</option>
               <option value="shine">shine[ECharts]</option>
               <option value="green">green[ECharts]</option>
             </select>
             <script type="text/javascript">
               document.getElementById("theme").value="${chart.theme}";
             </script>
        	 </td>
        </tr>

        <tr>
        	 <td height="28">数据源</td>
        	 <td height="28">
              <select id="databaseId" name="databaseId" style="height:20px; width:90px;">
               <option value="0" selected>系统默认</option>
               <#list  databases as db>
               <option value="${db.id}">${db.title}</option>
               </#list> 
              </select>
              <script type="text/javascript">
                 document.getElementById("databaseId").value="${chart.databaseId}";
              </script>
        	 </td>
        	 <td height="28">&nbsp;</td>
        	 <td height="28">&nbsp;</td>
        </tr>

         <!-- <tr>
        	 <td height="98">数据集</td>
              <td colspan="3" height="98">
                <input type="hidden" id="datasetIds" name="datasetIds" value="${chart.datasetIds}">
                <textarea id="datasetNames" name="datasetNames" rows="12" cols="68" class="x-textarea"
                          style="width:580px;height:80px;" onclick="javascript:openDataSet();"  
                          readonly="true" >${datasetNames}</textarea>&nbsp;
                <a href="#" onclick="javascript:openDataSet();">
                <img src="${contextPath}/static/images/search_results.gif" border="0"
                     title="如果图表需要多个数据集组成一个图表，请先建好数据集再选择。">
                </a>
        	 </td>
        </tr> -->

        <!-- <tr>
        	 <td height="98">第二轴数据集</td>
              <td colspan="3" height="98">
				<input type="hidden" id="secondDatasetIds" name="secondDatasetIds" value="${chart.secondDatasetIds}">
				<textarea id="dataset2Names" name="dataset2Names" rows="12" cols="68" class="x-textarea"
				          style="width:580px;height:80px;" onclick="javascript:openDataSet2();"  
				          readonly="true" >${dataset2Names}</textarea>&nbsp;
				  <a href="#" onclick="javascript:openDataSet2();">
				    <img src="${contextPath}/static/images/search_results.gif" border="0"
				         title="如果图表需要多个数据集组成一个图表，请先建好数据集再选择。">
				  </a>
        	 </td>
        </tr> -->

        <tr>
        	 <td height="98">组合查询</td>
              <td colspan="3" height="98">
				<input type="hidden" id="queryIds" name="queryIds" value="${chart.queryIds}">
				<textarea id="queryNames" name="queryNames" rows="12" cols="68" class="x-textarea"
				          style="width:580px;height:80px;" onclick="javascript:openQx();"  
				          readonly="true" >${queryNames}</textarea>&nbsp;
				  <a href="#" onclick="javascript:openQx();">
				     <img src="${contextPath}/static/images/search_results.gif" border="0"
				          title="如果图表需要多个查询组成一个图表，请先建好查询数据再选择。">
				  </a>
        	 </td>
        </tr>

	    <tr>
		<td colspan="4" align="center">
		<br><br>
		<input type="button" name="save" value="测试" class="btn btnGray" onclick="javascript:testSql();" />
		&nbsp;
		<input type="button" name="save" value="JFreeChart图表" class="btn btnGray" onclick="javascript:viewChart();" />
		&nbsp;
		<input type="button" name="save" value="HighCharts图表" class="btn btnGray" onclick="javascript:viewHighCharts();" />
        &nbsp;
		<input type="button" name="save" value="保存" class="btn btnGray" onclick="javascript:saveData();" />
		&nbsp;
		<input type="button" name="saveAs" value="另存" class="btn btnGray" onclick="javascript:saveAsData();" />
		&nbsp;
		<!-- <input name="btn_back" type="button" value=" 返回 " class=" btn" onclick="javascript:history.back(0);"> -->
		</td>
	 </tr>
	</tbody>
   </table>
  </form>
  <br>
  <br>
  <br>
  <script type="text/javascript">
        initData();
  </script>
</body>
</html>