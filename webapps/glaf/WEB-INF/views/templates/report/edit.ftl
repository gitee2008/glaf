<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>报表定义</title>
<#include "/inc/init_easyui_import.ftl"/> 
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

		function initData(){
			// $('#iForm').form('load','${contextPath}/rs/report/view/${rowId}');
		}

		function saveData(){
			//document.getElementById("textContent").value=editor1.html();
             var params = jQuery("#iForm").formSerialize();
             jQuery.ajax({
             type: "POST",
             url: '${contextPath}/rs/report/saveReport',
             data: params,
             dataType:  'json',
             error: function(data){
	             alert('服务器处理错误！');
             },
             success: function(data){
	             if(data.message != null){
		             alert(data.message);
	             } else {
		             alert('操作成功完成！');
	             }
                 <#if report?exists>
	             parent.window.location.reload();
				</#if>
             }
			 });
		}

		function saveAsData(){
             document.getElementById("id").value="";
             document.getElementById("reportId").value="";
             //document.getElementById("textContent").value=editor1.html();
             var params = jQuery("#iForm").formSerialize();
             jQuery.ajax({
             type: "POST",
             url: '${contextPath}/rs/report/saveReport',
             data: params,
             dataType:  'json',
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

		function createReport(){
			 window.open('${contextPath}/report/createReport?reportId=${report.id}');
		}

		function createHtmlReport(){
			 window.open('${contextPath}/report/outputHtml?reportId=${report.id}');
		}

		function sendTestMail(){
            jQuery.ajax({
             type: "POST",
             url: '${contextPath}/rs/report/sendMail?reportId=${report.id}',
             dataType:  'json',
             error: function(data){
	             alert('服务器处理错误！');
             },
             success: function(data){
	             if(data.message != null){
		             alert(data.message);
	             } else {
		             alert('操作完成！');
	             }
             }
			});
		}


		function openQx(){
            var selected = jQuery("#queryIds").val();
            var link = '${contextPath}/sys/sql/sqlTree?elementId=queryIds&elementName=queryNames&nodeCode=report_category&selected='+selected;
			var x=100;
			var y=100;
			if(is_ie) {
				x=document.body.scrollLeft+event.clientX-event.offsetX-200;
				y=document.body.scrollTop+event.clientY-event.offsetY-200;
			 }
			openWindow(link,self,x, y, 495, 480);
		}

		function openTable(){
            var selected = jQuery("#tansferTables").val();
            var link = '${contextPath}/report/chooseTable?elementId=tansferTables&elementName=tableNames&nodeCode=report_category&selected='+selected;
			var x=100;
			var y=100;
			if(is_ie) {
				x=document.body.scrollLeft+event.clientX-event.offsetX-200;
				y=document.body.scrollTop+event.clientY-event.offsetY-200;
			 }
			openWindow(link,self,x, y, 695, 480);
		}
		

        function openQx2(){
                var link = '${contextPath}/report/chooseQuery?reportId=${report.id}&elementId=queryIds&elementName=queryNames';
				var x=100;
				var y=100;
				if(is_ie) {
					x=document.body.scrollLeft+event.clientX-event.offsetX-200;
					y=document.body.scrollTop+event.clientY-event.offsetY-200;
				 }
				openWindow(link,self,x, y, 695, 480);
		}


		function openChart(){
            var selected = jQuery("#chartIds").val();
            var link = '${contextPath}/chart/chartTree?elementId=chartIds&elementName=chartNames&nodeCode=report_category&selected='+selected;
			var x=100;
			var y=100;
			if(is_ie) {
				x=document.body.scrollLeft+event.clientX-event.offsetX-200;
				y=document.body.scrollTop+event.clientY-event.offsetY-200;
			 }
			openWindow(link,self,x, y, 495, 480);
		}

        function openChart2(){
                var link = '${contextPath}/report/chooseChart?reportId=${report.id}&elementId=chartIds&elementName=chartNames';
				var x=100;
				var y=100;
				if(is_ie) {
					x=document.body.scrollLeft+event.clientX-event.offsetX-200;
					y=document.body.scrollTop+event.clientY-event.offsetY-200;
				 }
				openWindow(link,self,x, y, 695, 480);
		}

	    function openFile(){
			var link = "${contextPath}/report/chooseFile?reportId=${report.id}&elementId=reportTemplate&elementName=reportTemplate";
			var x = 100;
			var y = 100;
			if (is_ie) {
				x = document.body.scrollLeft + event.clientX - event.offsetX - 200;
				y = document.body.scrollTop + event.clientY - event.offsetY - 200;
			}
			openWindow(link, self, x, y, 745, 480);
	    }
</script>
</head>
<body>
	<br>
	<div class="x_content_title">
		<img src="${contextPath}/static/images/window.png" alt="报表定义">
		&nbsp;报表定义
	</div>
	<form id="iForm" name="iForm" method="post">
		<input type="hidden" id="id" name="id" value="${report.id}" /> <input
			type="hidden" id="nodeId" name="nodeId" value="${nodeId}" /> <input
			type="hidden" id="reportId" name="reportId" value="${report.id}" />
		<table class="easyui-form" style="width: 920px;" align="center">
			<tbody>
				<tr>
					<td width="180" height="28">名称</td>
					<td height="28">
					  <input id="name" name="name" class="span7 x-text" type="text" value="${report.name}" size="80"></input>
					</td>
				</tr>

				<tr>
					<td height="28">标题</td>
					<td height="28">
					  <input id="subject" name="subject"
						     class="span7 x-text" type="text" value="${report.subject}"
						     size="80"></input>
					</td>
				</tr>

				<tr>
					<td height="28">报表名称</td>
					<td height="28">
					  <input id="reportName" name="reportName"
						     class="span7 x-text" type="text" value="${report.reportName}"
						     size="80"></input>
					</td>
				</tr>

				<tr>
					<td height="28">模板文件</td>
					<td height="28">
					<input id="reportTemplate"
						name="reportTemplate" class="span7 x-text" type="text"
						value="${report.reportTemplate}" size="80"></input>  
					</td>
				</tr>

				<tr>
					<td height="28">报表标题日期</td>
					<td height="28">
					    <input id="reportTitleDate"
						       name="reportTitleDate" class="span7 x-text" type="text"
						       value="${report.reportTitleDate}" size="80"></input>
					</td>
				</tr>

				<tr>
					<td height="28">报表年月</td>
					<td height="28">
					    <input id="reportMonth" name="reportMonth"
						       class="span7 x-text" type="text" value="${report.reportMonth}"
						       size="80"></input>
					</td>
				</tr>

				<tr>
					<td height="28">报表年月日参数</td>
					<td height="28">
					    <input id="reportDateYYYYMMDD"
						       name="reportDateYYYYMMDD" class="span7 x-text" type="text"
						       value="${report.reportDateYYYYMMDD}" size="80"></input>
					</td>
				</tr>

				<tr>
					<td height="56">报表类型</td>
					<td height="56">
					  <select id="type" name="type" class="span2"	style="height: 20px">
						<option value="jasper">JasperReport</option>
						<option value="jxls">JXLS</option>
						<option value="jxls2">JXLS2</option>
						<option value="ftl">Freemarker</option>
					  </select> 
					  <script type="text/javascript">
						$('#type').val('${report.type}');
					  </script> &nbsp;&nbsp;&nbsp;&nbsp;
					  生成格式&nbsp; 
					  <select id="reportFormat" name="reportFormat" class="span2" style="height: 20px">
						<option value="pdf">PDF</option>
						<option value="xls">Office Excel 97-2003</option>
						<option value="xlsx">Office Excel 2007</option>
						<option value="html">HTML</option>
					  </select> 
					  <script type="text/javascript">
						$('#reportFormat').val('${report.reportFormat}');
					  </script>
					  <br>
					（提示：如果模板格式为xls,输出格式应该是Office Excel 97-2003；模板格式为xlsx,输出格式应该是Office Excel 2007。）
				</tr>

				<tr>
					<td height="28">是否启用&nbsp;</td>
					<td height="28">
						<select id="enableFlag" name="enableFlag"
							    class="span2" style="height: 20px">
								<option value="1">启用</option>
								<option value="0">不启用</option>
						</select> 
						<script type="text/javascript">
							$('#enableFlag').val('${report.enableFlag}');
						</script> 
						&nbsp;是否公开访问&nbsp;
                        <select id="publicFlag" name="publicFlag"
							class="span2" style="height: 20px">
								<option value="1">公开</option>
								<option value="0">私有</option>
						</select> 
						<script type="text/javascript">
							$('#publicFlag').val('${report.publicFlag}');
						</script> 
						&nbsp;（提示：公开访问则有无权限都可以访问，即不做权限控制。）
					</td>
				</tr>

				<tr>
					<td height="98">后置处理程序</td>
					<td height="98">
					<textarea id="postProcessor"
							  name="postProcessor" class="x-textarea" rows="5" cols="58"
							  style="width: 535px; height: 60px;">${report.postProcessor}</textarea>
					 <br>（提示：多个处理程序之间用半角的逗号","隔开。）
					</td>
				</tr>

				<tr>
					<td height="98">JSON格式参数</td>
					<td height="98"><textarea id="jsonParameter"
							name="jsonParameter" class="x-textarea" rows="5" cols="58"
							style="width: 535px; height: 90px;">${report.jsonParameter}</textarea>
					</td>
				</tr>

				<!-- <tr>
					<td height="98">数据集</td>
					<td height="98"><input type="hidden" id="queryIds"
						name="queryIds" value="${report.queryIds}"> <textarea
							type="textarea" id="queryNames" name="queryNames"
							value="${queryNames}" readonly="true" class="x-textarea"
							onclick="javascript:openQx();"
							style="width: 535px; height: 90px;">${queryNames}</textarea>
						&nbsp; <a href="#" onclick="javascript:openQx();"> <img
							src="${contextPath}/static/images/search_results.gif"
							border="0" title="如果报表需要多个查询数据集组成，请先建好查询数据再选择。">
					</a></td>
				</tr> -->

				<tr>
					<td height="98">相关图表</td>
					<td height="98"><input type="hidden" id="chartIds"
						name="chartIds" value="${report.chartIds}"> <textarea
							type="textarea" id="chartNames" name="chartNames"
							value="${chartNames}" readonly="true" class="x-textarea"
							onclick="javascript:openChart();"
							style="width: 535px; height: 90px;">${chartNames}</textarea>
						&nbsp; <a href="#" onclick="javascript:openChart();"> <img
							src="${contextPath}/static/images/chart.png" border="0">
					</a></td>
				</tr>

				<tr>
					<td colspan="4" align="center">
					<br> <br> 
					<input type="button" name="save" value=" 保 存 " class=" btn btnGray"
		                   onclick="javascript:saveData();" /> &nbsp;&nbsp; 
                    <#if report?exists>
					<input type="button" name="saveAs" value=" 另 存 " class=" btn btnGray"
		                   onclick="javascript:saveAsData();" /> &nbsp;&nbsp; 
					<input type="button" name="createRpt" value=" 生成报表 " class=" btn btnGray"
		                   onclick="javascript:createReport();" /> &nbsp;&nbsp;
					<input type="button" name="outputHtml" value=" 输出HTML " class=" btn btnGray"
		                   onclick="javascript:createHtmlReport();" />
					</#if>
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