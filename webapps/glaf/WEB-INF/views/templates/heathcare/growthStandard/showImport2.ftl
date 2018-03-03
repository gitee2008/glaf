<!DOCTYPE html>
<html>
<head>
<title>导入数据</title>
<#include "/inc/init_easyui_import.ftl"/>
<script language="JavaScript">

	function submitRequest(form){
		if(document.getElementById("standardType").value==""){
            alert("请选择标准！");
			return;
		}
		if(document.getElementById("type").value==""){
            alert("请选择类型！");
			return;
		}
		if(document.getElementById("sex").value==""){
            alert("请选择性别！");
			return;
		}
		if(document.getElementById("file").value==""){
            alert("请选择您要导入的文本文件！");
			return;
		}
        if(confirm("新的数据将覆盖旧数据且不能恢复，确定导入吗？")){
            document.iForm.submit();
		}
	}

</script>
</head>
<body leftmargin="0" topmargin="0" marginheight="0" marginwidth="0">
<br>
<center>
<form action="${request.contextPath}/heathcare/growthStandard/importData2" method="post"
	enctype="multipart/form-data" name="iForm" class="x-form">
<div class="content-block"  style=" width: 80%;" >
<br>
<div class="x_content_title"><img
	src="${request.contextPath}/static/images/window.png" alt="导入数据">&nbsp;导入数据
</div>
<br><br>
<div align="center">

    <table>
       <tr>
		<td>
		  &nbsp;&nbsp;标准
	      <select id="standardType" name="standardType" >
			<option value="">----请选择----</option>
			<option value="CN">中国标准</option>
			<option value="ISO">国际标准</option>
		  </select>
		  <script type="text/javascript">
			   document.getElementById("standardType").value="${standardType}";
		  </script>
		</td>
		<td>
		  &nbsp;&nbsp;类型
	      <select id="type" name="type" >
			<option value="">----请选择----</option>
			<!-- <option value="1">年龄别头围标准差</option> -->
			<option value="2">年龄别身高标准差</option>
			<option value="3">年龄别体重标准差</option>
			<option value="4">身高别体重标准差</option>
			<option value="5">体质指数（BMI）</option>
		  </select>
		  <script type="text/javascript">
			   document.getElementById("type").value="${type}";
		  </script>
		</td>
		<td>
		  &nbsp;&nbsp;性别
	      <select id="sex" name="sex" >
			<option value="">----请选择----</option>
			<option value="0">女</option>
			<option value="1">男</option>
		  </select>
		  <script type="text/javascript">
			   document.getElementById("sex").value="${sex}";
		  </script>
		</td>
       </tr>
	   <tr>
	     <td colspan="5" align="center">
		  <br>请选择要导入的数据，必须是文本格式&nbsp;<input type="file" id="file" name="file" size="50" class="input-file">
		 </td>
	   </tr>
	   <tr>
	     <td colspan="5" align="center">
		  <br><input type="button" name="bt01" value="确定" class="btn btnGray"
	             onclick="javascript:submitRequest(this.form);" /> 
		 </td>
	   </tr>
    </table>
</div>
</form>
</center>
</body>
</html>