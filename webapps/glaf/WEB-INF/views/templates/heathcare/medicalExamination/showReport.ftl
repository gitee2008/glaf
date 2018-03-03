<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>体格检查报告</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

    function exportXls(){
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=MedicalExaminationSicknessPositiveSign&checkId=${checkId}";
        window.open(link);
    }


	function exportXls2(){
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=MedicalExaminationCount&checkId=${checkId}";
        window.open(link);
    }

</script>
</head>

<body>
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
    <div data-options="region:'north',split:false,border:true" style="height:42px"  class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">体格检查报告</span>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <table class="easyui-form" style="width:600px;margin:20px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left" height="38">&nbsp;</td>
		<td align="left"  height="38">
           <input type="button" value="儿童健康体检疾病及阳性体征统计" class="btn btnGray" onclick="javascript:exportXls();">
		</td>
	</tr>
	<tr>
		<td width="20%" align="left"  height="38">&nbsp;</td>
		<td align="left"  height="38">
           <input type="button" value="儿童体格发育检查统计" class="btn btnGray" onclick="javascript:exportXls2();">
		</td>
	</tr>
    </tbody>
  </table>
  </form>
</div>
</div>
</body>
</html>