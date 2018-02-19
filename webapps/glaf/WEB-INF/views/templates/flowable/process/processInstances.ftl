<html>
<head>
<title>流程实例</title>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/plugins/yui/build/datatable/assets/skins/sam/datatable.css"> 
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/plugins/yui/build/paginator/assets/skins/sam/paginator.css">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/plugins/yui/build/button/assets/skins/sam/button.css">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/plugins/yui/build/menu/assets/skins/sam/menu.css" />
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/css/core.css"/>
<script type="text/javascript"	src="${request.contextPath}/static/scripts/jquery.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/plugins/yui/yui-2.8.1-min.js"></script>
</head>
<body  class="yui-skin-sam" leftmargin="0" topmargin="0" marginheight="0" marginwidth="0" style="padding-top:10px;padding-left:20px;"> 
 
<div class="x_content_title">
<img
	src="${request.contextPath}/static/images/window.png"
	alt="流程实例列表">&nbsp;流程实例列表
</div>
<br>

  <table id="screen" width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
          <td>
			<table  border="0" cellspacing="0" cellpadding="0" >
				<tr> 
				 <td width="95%" height="25" align="left" > 
					<div id="paginationContainerTop"  width="100%"></div>
					<div id="menu_line" class="menu_line2" width="100%"></div>
					<div id="DataTableYUI"  width="100%">
					
					</div>
					<div id="paginationContainerBottom"  width="100%"></div>
				 </td>
			  </tr>
			  <tr> 
				 <td width="95%" height="25" align="left" > 
				 <div>
 				  <span id="view-button-2"> </span>
  				 </div>
				</td>
			  </tr>
			</table>			    
		</td>
  </tr>
</table>

 <script type="text/javascript"> 

  YAHOO.example.DynamicData = ( function() {
    
    var x_app = '&deploymentId=${deploymentId}&processDefinitionId=${processDefinitionId}&x_complex_query=${x_complex_query}'

    var s_link = "${request.contextPath}/flowable/process/processInstanceJson?q=1"+x_app;
 

    var viewbutton2 = new YAHOO.widget.Button({ label:"查看流程", id:"view-button-2", container:"view-button-2" });
    viewbutton2.on("click", onViewProcessButtonClick);


    function onViewButtonClick(e) {
    	var selectRows = myDataTable.getSelectedRows();
    	 if(selectRows.length!=null && selectRows.length==1){
          oRecord = myDataTable.getRecordSet().getRecord(selectRows[0]);
          var processInstanceId = YAHOO.lang.dump(oRecord.getData("processInstanceId"));
          window.open("${request.contextPath}/flowable/task?q=1&processInstanceId="+processInstanceId);
        } else{
          alert('请选择其中一条记录');
    	}
    }

	function onViewProcessButtonClick(e) {
    	var selectRows = myDataTable.getSelectedRows();
    	 if(selectRows.length!=null && selectRows.length==1){
          oRecord = myDataTable.getRecordSet().getRecord(selectRows[0]);
          var processInstanceId = YAHOO.lang.dump(oRecord.getData("processInstanceId"));
		  if(processInstanceId != null && processInstanceId != "null"){
              window.open("${request.contextPath}/flowable/task?processInstanceId="+processInstanceId);
		  } else {
              alert('流程实例不存在');
		  }
        } else{
          alert('请选择其中一条记录');
    	}
    }

    
	 function onMyClickRow(oArgs) {
	    myDataTable.onEventSelectRow(oArgs);
		var elTarget = oArgs.target;
        var elTargetRow = myDataTable.getTrEl(elTarget);
        if(elTargetRow) {
              var oRecord = myDataTable.getRecord(elTargetRow);
              var status = YAHOO.lang.dump(oRecord.getData("status"));			 
              if(status <= 10 ){                  
				     editbutton.removeAttribute("disabled");
					 editbutton.removeStateCSSClasses("disabled");
               } else {
                     editbutton.setAttribute("disabled", "disabled");
					 editbutton.addStateCSSClasses("disabled");
			   }
	    }
	}

	function onMyDblClickRow(oArgs) {
	    myDataTable.onEventSelectRow(oArgs);
		var elTarget = oArgs.target;
        var elTargetRow = myDataTable.getTrEl(elTarget);
        if(elTargetRow) {
              var oRecord = myDataTable.getRecord(elTargetRow);
              var processInstanceId = YAHOO.lang.dump(oRecord.getData("processInstanceId"));
			  window.open("${request.contextPath}/flowable/task?q=1&processInstanceId="+processInstanceId);
	    }
	}

	 var formatFunKey = function(elCell, oRecord, oColumn, oData) {
        var processInstanceId = YAHOO.lang.dump(oRecord.getData("processInstanceId"));
		var processDefinitionId = YAHOO.lang.dump(oRecord.getData("processDefinitionId"));
        var link ='${request.contextPath}/flowable/task?processInstanceId='+processInstanceId;
		var link2 ='${request.contextPath}/flowable/image/viewImage?processDefinitionId='+processDefinitionId;
		var msg = '<a href=\" '+link+'  " target=_blank><img src="${request.contextPath}/static/images/view.gif"/>&nbsp;查看</a>';
		msg += '&nbsp;&nbsp;<a href=\" '+link2+'  " target=_blank><img src="${request.contextPath}/static/images/process.gif"/>&nbsp;流程图</a>';
        elCell.innerHTML = msg ;
      }

	  var formatRow = function(elCell, oRecord, oColumn, oData) {
        var processInstanceId = YAHOO.lang.dump(oRecord.getData("processInstanceId"));
        var link = "${request.contextPath}/flowable/task?processInstanceId="+processInstanceId+x_app;
        elCell.innerHTML = "<a href=\""+link+"\" target='_blank'>"+oData+"</a>";
      }

	  var formatStatus = function(elCell, oRecord, oColumn, oData) {
        var status = YAHOO.lang.dump(oRecord.getData("isEnded"));
    	var msg = "";
        if (status == true) {
            msg = "<span style='color: red; font-weight: bold; text-align: center;'> 已完成 </span>";
    	}  else {
            msg = "<span style='color: green; font-weight: bold; text-align: center;'> 运行中  </span>";
    	}
        elCell.innerHTML = msg;
      }


 	 var imgFormatter = function(elCell, oRecord, oColumn, oData) {
           var imginfo = oData; 
           elCell.innerHTML = "<img width=12 height=12 src=" + imginfo + "></img>";
        };

 	var myRowFormatter = function(elTr, oRecord) {
		if (oRecord.getData('status') == -1) {
			Dom.addClass(elTr, 'mark');
		} else if (oRecord.getData('status') == 50) {
			Dom.addClass(elTr, 'greenmark');
		}
		return true;
	}; 

      YAHOO.widget.DataTable.Formatter.imginfoFormatter = imgFormatter;
	  YAHOO.widget.DataTable.Formatter.formatFunKey = formatFunKey;
	  YAHOO.widget.DataTable.Formatter.formatStatus = formatStatus;
	  YAHOO.widget.DataTable.Formatter.formatRow = formatRow;

	   myDataSource = new YAHOO.util.DataSource(s_link);
       myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
       myDataSource.connXhrMode = "queueRequests";
       myDataSource.responseSchema = { 
	       resultsList : "records",  
	       fields : [ 
						{key: 'id'},
						{key: 'processDefinitionId'},
						{key: 'processInstanceId'},
						{key: 'businessKey'},
						{key: 'isEnded'} 
                     ],
           metaFields: {
                totalRecords: "totalRecords"
          }
	   }; 

	  var myColumnDefs = [ 
		{label: '流程实例编号', key: 'processInstanceId', width: 180, resizeable: true, formatter: formatRow},
	    {label: '流程定义编号', key: 'processDefinitionId', resizeable: true, width: 180},
        {label: '业务编号', key: 'businessKey', resizeable: true, width: 180},
  		{label: '状态', key: 'isEnded', width: 120, resizeable: true, align: 'center' , formatter: formatStatus},
        {label: '功能键', key: 'functionKey', resizeable: true, width: 130, align: 'center', formatter: formatFunKey}
       ];

    var t0="{FirstPageLink}&nbsp;&nbsp;{PreviousPageLink}&nbsp;&nbsp;{NextPageLink}&nbsp;&nbsp;{LastPageLink}&nbsp;&nbsp; {RowsPerPageDropdown}&nbsp;&nbsp;{CurrentPageReport}";

    var t2 = '&nbsp;&nbsp;第&nbsp;{currentPage}&nbsp;页&nbsp;&nbsp;总页数:&nbsp;{totalPages}&nbsp;&nbsp;总记录数:&nbsp;{totalRecords}&nbsp;&nbsp;';

	var myConfigs = { 
     initialRequest:"sort=id&dir=asc&startIndex=0&results=10",
     dynamicData: true, 
     MSG_LOADING: '<center>数据加载中......</center>',
     MSG_ERROR: '数据加载错误。',
     MSG_EMPTY: '数据为空。',
     MSG_SORTASC: '升序',
     MSG_SORTDESC: '降序',
     paginator: new YAHOO.widget.Paginator({
            rowsPerPage: 10,
			rowsPerPageOptions: [10,15,20,25,50,100,200,500],
        	containers : 'paginationContainerBottom',
        	PageLinks : YAHOO.widget.Paginator.VALUE_UNLIMITED,
            firstPageLinkLabel : '第一页', 
            lastPageLinkLabel : '最后一页', 
            previousPageLinkLabel:'前一页',
            nextPageLinkLabel:'后一页',
        	template: t0,
         	pageReportTemplate: t2
        })
	}; 

      
    this.myDataTable = new YAHOO.widget.DataTable("DataTableYUI", myColumnDefs, myDataSource, myConfigs);
	this.myDataTable.subscribe("rowMouseoverEvent", this.myDataTable.onEventHighlightRow);
    this.myDataTable.subscribe("rowMouseoutEvent", this.myDataTable.onEventUnhighlightRow);
    this.myDataTable.subscribe("rowClickEvent", onMyClickRow);
	this.myDataTable.subscribe("rowDblclickEvent", onMyDblClickRow);

 	myDataTable.selectRow(myDataTable.getTrEl(0));
 	myDataTable.focus();

    this.myDataTable.handleDataReturnPayload = function(oRequest, oResponse, oPayload) {
        oPayload.totalRecords = oResponse.meta.totalRecords;
        return oPayload;
    }    

	return {
    	ds: myDataSource,
    	dt: myDataTable
    };

   })();
 </script> 
 <br><br>
</body>
</html>