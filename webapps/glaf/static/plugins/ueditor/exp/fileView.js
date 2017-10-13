/**
 * User: magoo
 * Date: 2015-12-21
 * 进度条控件
 */

(function(){

    window.onload = function(){
      //  $focus($G("videoUrl")); 
        initPlugs();
    };
	//初始化进度条信息
    function initPlugs(){
       // createAlignButton( ["videoFloat", "upload_alignment"] );
       // addUrlChangeListener($G("videoUrl"));
        //添加确定取消监听事件
		addOkListener();

        //编辑视频时初始化相关信息
        (function(){
            var target = editor.selection.getStart();
            if(target && target.className){
                var isPlugs = (target.className == "mt-fileView") || target.className.indexOf("mt-fileView")!=-1;
                if(isPlugs || isPlugs) {
                    var Adata = target.getAttribute("params");
                    if(Adata){
                        var data = JSON.parse(Adata.replace(/\'/gi,"\""));
                        $G("field").value = data.field.field;
                        $G("value").value = data.value.field;
                        $G("model").value = data.model;
                        changefunc($G("model"));
                        $G("ftpserver").value = data.ftp;
                        $G("ftpserverhidden").value = data.ftpHidden;
                        $G("keyField").value = data.key.field;
                        $G("urlField").value = data.url.field;
                        $G("splitField").value = data.split;

                        $G("soaserver").value = data.soa;
                        $G("soaserverhidden").value = data.soaHidden;
                        $G("soaFilePath").value = data.soaFilePath;
                    }
                }
            }
        })();
    }

    /**
     * 监听确认和取消两个按钮事件，用户执行插入或者清空正在播放的视频实例操作
     */
    function addOkListener(){
        dialog.onok = function(){
        	var value = "" , field = $G("field").value ;
        	if(parent.map.containsKey(field)){
        		value = parent.map.get(field).code ;
        	}else if(field){
        		value = field ;
        	}
        	
        	var valVlaue = "" , valField = $G("value").value ;
        	if(parent.map.containsKey(valField)){
        		valVlaue = parent.map.get(valField).code ;
        	}else if(valField){
        		valVlaue = valField ;
        	}
        	
        	var keyValue = "" , keyField = $G("keyField").value ;
            if(parent.map.containsKey(keyField)){
            	keyValue = parent.map.get(keyField).code ;
            }else if(keyField){
            	keyValue = keyField ;
            }
            
            var urlValue = "" , urlField = $G("urlField").value ;
            if(parent.map.containsKey(urlField)){
            	urlValue = parent.map.get(urlField).code ;
            }else if(urlField){
            	urlValue = urlField ;
            }

            var soaFilePathValue = "", soaFilePathField = $G("soaFilePath").value;
            if(parent.map.containsKey(soaFilePathField)){
                soaFilePathValue = parent.map.get(soaFilePathField).code ;
            }else if(soaFilePathField){
                soaFilePathValue = soaFilePathField ;
            }
            
			editor.execCommand('insertfileview', {
				field : {'field':field,'value':value },
				value : {'field':valField,'value':valVlaue },
				key : {'field':keyField,'value':keyValue },
				url : {'field':urlField,'value':urlValue },
                model : $G("model").value ,
                ftp : $G("ftpserver").value ,
                ftpHidden : $G("ftpserverhidden").value ,
                split : $G("splitField").value ,
                soa : $G("soaserver").value,
                soaHidden : $G("soaserverhidden").value ,
                soaFilePath :  soaFilePathValue,
			});
          return true ;
        };
        dialog.oncancel = function(){
           // $G("preview").innerHTML = "";
        };
    }
 

    /**
      * 检测传入的所有input框中输入的长宽是否是正数
      * @param nodes input框集合，
      */
     function checkNum( nodes ) {
         for ( var i = 0, ci; ci = nodes[i++]; ) {
             var value = ci.value;
             if ( !isNumber( value ) && value) {
                 alert( lang.numError );
                 ci.value = "";
                 ci.focus();
                 return false;
             }
         }
         return true;
     }

    /**
     * 数字判断
     * @param value
     */
    function isNumber( value ) {
        return /(0|^[1-9]\d*$)/.test( value );
    }
})();