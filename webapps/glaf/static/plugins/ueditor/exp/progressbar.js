/**
 * User: magoo
 * Date: 2015-12-21
 * 进度条控件
 */

(function(){

    window.onload = function(){
      //  $focus($G("videoUrl")); 
        initProgressBar();
    };
	//初始化进度条信息
    function initProgressBar(){
       // createAlignButton( ["videoFloat", "upload_alignment"] );
       // addUrlChangeListener($G("videoUrl"));
        //添加确定取消监听事件
		addOkListener();

        //编辑视频时初始化相关信息
        (function(){
			//获取选择的进度条控件
            var img = editor.selection.getRange().getClosedNode();
            if(img && img.className){
                var isprogress = (img.className == "mt-progressbar") || img.className.indexOf("mt-progressbar")!=-1;
                if(isprogress || isprogress) {
                    var Adata = img.getAttribute("data");
                    if(Adata){
                        var data = JSON.parse(Adata.replace(/\'/gi,"\""));
                        $G("field").value = data.field;
                        $G("model").value = data.model;
                        $G("max").value = data.max;
                        $G("min").value = data.min;
                        $G("showState").checked = data.showState;
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
        	var value = "" ;
            var field = $G("field").value ;
            if(parent.map.containsKey(field)){
                value = parent.map.get(field).code ;
            }else if(field){
                value = field ;
            }
			editor.execCommand('insertProgressBar', {
				field : field ,
                value : value ,
                model : $G("model").value ,
                max : $G("max").value ,
                min : $G("min").value ,
                showState : $G('showState').checked
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