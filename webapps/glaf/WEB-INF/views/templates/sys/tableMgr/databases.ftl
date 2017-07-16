<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据库列表信息</title>
<#include "/inc/init_easyui_import.ftl"/>
<style>

.subject { font-size: 13px; text-decoration: none; font-weight:normal; font-family:"宋体"}
.table-border { background-color:#e6e6e6; height: 32px; font-family:"宋体"}
.table-head { background-color:#e6e6e6; height: 30px; font-weight:bold; font-size: 13px; font-family:"宋体"}
.table-content { background-color:#ffffff; height: 28px; font-size: 12px; font-family:"宋体"}

</style>
<script type="text/javascript">

	function update(databaseId){
	  if(confirm("数据表字段生成后不能修改类型，确定生成物理表结构吗？")){
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/tableMgr/updateAllSchema?databaseId='+databaseId,
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
					       window.parent.location.reload();
					   } 
				   }
			 });
	    }
	}

    function submitReq(){
        document.getElementById("iForm").submit();
    }

    function changeXY(personId){
	    var status_ = document.getElementById("status_"+personId).value;
	    if(status_ == 0){
            jQuery("#div_remark_"+personId).hide();
			jQuery("#div_treat_"+personId).hide();
	    } else {
		    jQuery("#div_remark_"+personId).show();
			jQuery("#div_treat_"+personId).show();
	    }
    }

</script>
</head>

<body>
<div style="margin:0px;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:true,border:true" style="height:40px;margin-top:0px;"> 
    <div class="toolbar-backgroud"> 
	<span class="x_content_title">数据库列表</span>
	<!-- <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	   onclick="javascript:saveData();" > 确 定 
	</a>  -->
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <table class="table-border" cellspacing="1" cellpadding="4" width="99%" nowrap align="center">
    <thead>
	  <tr>
	  <td class="table-head" width="40%" align="left">标题</td>
	  <td class="table-head" width="40%" align="left">数据库名称</td>
	  <td class="table-head" width="40%" align="left">&nbsp;</td>
	  </tr>
	</thead>
    <tbody>
	<#list databases as db>
	<#if db.useType == "GENERAL">
	<tr>
		<td  class="table-content" align="left"> ${db.title} </td>
		<td  class="table-content" align="left"> ${db.dbname} </td>
		<td  class="table-content" align="left">
		     <input type="button" value="更新" class="btnGrayMini" onclick="javascript:update(${db.id});">
		</td>
	</tr>
	</#if>
	</#list>
	<tr><td colspan="7">&nbsp;<br></td></tr>
    </tbody>
  </table>
 </form>
</div>
</div>
<div style="margin-top:10px;"><br></div>
<br>
</body>
</html>