<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${tableDefinition.title}</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<# include "/inc/init_layui_import.ftl"/>
<script type="text/javascript">

  function getLink(){
	var link_ = "#F{contextPath}/${tableDefinition.moduleName}/${modelName}/json?q=1";
	var namePinyinLike = jQuery("#namePinyinLike").val();
	if(namePinyinLike != "" && namePinyinLike != "undefined" ){
		link_ = link_ + "&namePinyinLike="+namePinyinLike;
	}
	return link_;
  }

  layui.use('table', function(){
	var table = layui.table;
	  
	table.render({
		elem: '#mydatagrid',
		//toolbar: '#toolbar',
        title: '${tableDefinition.title}',
		url: getLink(),
		parseData: function(res){ //res 即为原始返回的数据
		 return {
		  "code": res.code, //解析接口状态
		  "msg": res.message, //解析提示文本
		  "count": res.total, //解析数据长度
		  "data": res.rows //解析数据列表
		 };
	    },
		totalRow: true,
		cols: [[
		    {type:'checkbox', fixed: 'left'},
		    {field:'startIndex', title:'序号', width:68, sort:true},
		    <#if pojo_fields?exists>
			<#list  pojo_fields as field>
			<#if field.displayType == 4>
			{field:'${field.name}', title:'${field.title?if_exists}', width:120, sort:true<#if field.type?exists && (field.type== 'Integer' || field.type== 'Long' || field.type== 'Double' ) >, fixed:'right'</#if>},
			</#if>
			</#list>
			</#if>
			{fixed:'right', title:'功能键', toolbar: '#tool_function', width:120}
		]],
		page: true
	});
 

    //监听行单击事件（单击事件为：rowDouble）
    table.on('row(mydatagrid)', function(obj){
      var data = obj.data;
    
      layer.alert(JSON.stringify(data), {
        title: '当前行数据：'
      });
    
     //标注选中样式
      obj.tr.addClass('layui-table-click').siblings().removeClass('layui-table-click');
    });

    //头工具栏事件
    table.on('toolbar(mydatagrid)', function(obj){
      var checkStatus = table.checkStatus(obj.config.id);
      switch(obj.event){
        case 'getCheckData':
          var data = checkStatus.data;
          layer.alert(JSON.stringify(data));
        break;
        case 'getCheckLength':
          var data = checkStatus.data;
          layer.msg('选中了：'+ data.length + ' 个');
        break;
        case 'isAll':
          layer.msg(checkStatus.isAll ? '全选': '未全选');
        break;
      };
    });

    //监听行工具事件
    table.on('tool(mydatagrid)', function(obj){
      var data = obj.data;
      //console.log(obj)
      if(obj.event === 'del'){
        layer.confirm('数据删除后无法恢复，确定删除吗？', function(index){
		  jQuery.ajax({
				   type: "POST",
				   url: '#F{contextPath}/${tableDefinition.moduleName}/${modelName}/delete?id='+data.id,
				   dataType: 'json',
				   error: function(data){
					   layer.msg('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   layer.msg(data.message);
					   }
					   if(data.statusCode == 200){
						   layer.msg('操作成功完成！');
					       obj.del();
                           layer.close(index);
					   }
				   }
			 });
        });
      } else if(obj.event === 'edit'){
        var link = '#F{contextPath}/${tableDefinition.moduleName}/${modelName}/edit?id='+data.id;
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "编辑记录",
		  area: ['880px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
      }
    });

  });


  function addRow(){
    var link = '#F{contextPath}/${tableDefinition.moduleName}/${modelName}/edit';
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "编辑记录",
		  area: ['880px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
  }

</script>
</head>
<body style="margin:1px;">  
<div class="layui-container">  
   <div style="height:48px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	  <table width="100%" align="left">
		<tbody>
		 <tr>
		    <td width="55%" align="left">
				<img src="#F{contextPath}/static/images/window.png">
				&nbsp;<span class="x_content_title">${tableDefinition.title}列表</span>
				 
			</td>
			<td width="45%" align="right">
			  <button class="layui-btn layui-btn-normal" onclick="javascript:addRow();" >新增</button>
			  &nbsp;&nbsp;&nbsp;&nbsp;
			</td>
		</tr>
	   </tbody>
	  </table>
   </div> 
  </div> 

  <table id="mydatagrid" lay-filter="mydatagrid"></table>
 
  <script type="text/html" id="tool_function">
    <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
  </script>

</div>
</body>
</html>