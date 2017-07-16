<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>content</title>
<link href="${contextPath}/static/html/home/${homeTheme}/css/style.css" rel="stylesheet" type="text/css" />
<link href="${contextPath}/static/html/home/${homeTheme}/css/select.css" rel="stylesheet" type="text/css" />
<style type="text/css">
  #cdiv {
	position:absolute;
	width:30px;height:30px;
	z-index:9999;
	top:50%;
	left:10px;
	}
	.removea{
	font-weight: bolder;
	color: red;
	cursor: pointer;
	}
	.removediv{
	position:absolute;
	width:6px;height:6px;
	z-index:200;
	float: left;
  }
</style>
<script type="text/javascript" src="${contextPath}/static/scripts/jquery.min.js"></script>
<script type="text/javascript" src="${contextPath}/static/html/home/home.js"></script>
<script type="text/javascript" src="${contextPath}/static/html/home/${homeTheme}/js/jquery.idTabs.min.js"></script>
<script type="text/javascript" src="${contextPath}/static/html/home/${homeTheme}/js/select-ui.min.js"></script>
<script type="text/javascript">
	$(document).ready(function(e) {
		$(".select1").uedSelect({
			width : 345
		});
		$(".select2").uedSelect({
			width : 167
		});
		$(".select3").uedSelect({
			width : 100
		});
		settingTabHeight();
		$("#usual1 ul").idTabs();
	});
	function addTabEx(id, name, url) {
		//检查选项目是否存在
		if ($("#" + id).length==1&&$("#" + id).attr("id")==id) {
			$('#link_' + id).trigger('click');
		} else {
			//获取当前tabs数量
			var tabsnum = $(".tabson").length;
			var tabmax = '${tabmax}' || 10 ;
			if (tabsnum > tabmax) {
                //删除最后一个
                $(".tabson:last").remove();
			    $(".itab ul li:last").remove();
			}
			var lidom = $("<li onmouseover=\"MM_over(this)\" onmouseout=\"MM_out(this)\"></li>");
				var adom = $("<a></a>");
				adom.attr("href", "#" + id);
				adom.attr("id", "link_" + id);
				//$(".selected").removeClass("selected");
				//adom.addClass("selected");
				adom.append(name);
				lidom.append(adom);
				var lidivdom="<div class=\"removediv\" style=\"display: none;\"><a class=\"removea\" href=\"#\" onclick=\"closePage(this)\" style=\"color:#66C8F2;font-weight: bolder;font-size: 18px;\">×</a></div>";
				lidom.append(lidivdom);
				$(".itab ul").append(lidom);
				var divdom = $("<div></div>");
				divdom.attr("id", id);
				divdom.addClass("tabson");
				var iframedom = $("<iframe></iframe>");
				iframedom.attr("src", url);
				iframedom.css("width", "100%");
				iframedom.css("height", "100%");
				divdom.append(iframedom);
				$("#usual1").append(divdom);
				$("#usual1 ul").idTabs();
				$('#link_' + id).trigger('click');
			    settingTabHeight();
		}
	}
	function settingTabHeight() {
		var winHeight = $(window).height();
		$(".usual1").height(winHeight);
		$(".tabson").height(winHeight - $(".itab").height() - 48);
		$(".tabson iframe").height(winHeight - $(".itab").height() - 48);
	}
	$(document).ready(function(){
	$("body").append("<div id=\"cdiv\">"+
	   "<img id=\"expd_img\"  src=\"${contextPath}/static/html/home/${homeTheme}/images/expand.gif\" width=\"18\" style=\"text-align:center;vertical-align:middle;margin: auto;cursor: pointer;display: none;\" onclick=\"expand()\"/>"+"</div>");
	});
	function showExpd(){
	   $("#expd_img").css("display","");
	}
	function expand(){
          window.parent.document.getElementsByTagName("frameset")[1].cols="187,*";
          $("#expd_img").css("display","none");
        }
    function MM_over(mmObj) {
    var mmObjLeft=$(mmObj).offset().left;
    var mmObjTop=$(mmObj).offset().top;
    $(mmObj).find("div").css("top",mmObjTop-$(mmObj).height()/2-25+"px");
    $(mmObj).find("div").css("left",mmObjLeft+$(mmObj).width()-20+"px");
	$(mmObj).find("div").show();
}
	function MM_out(mmObj) {
		$(mmObj).find("div").hide();
		
	}
	function closePage(obj){
	  var liobj= $(obj).closest("li");
	  var aobj=liobj.find("a")[0];
	  var divid=$(aobj).attr("href");
	  $(liobj).remove();
	  $(divid).remove();
	  $("#usual1 ul").idTabs();
	}
</script>
</head>

<body>
<div>
    	<div class="place">
   	    <div class="topmenu">
		<ul>
	
		</ul>
		</div>
		<script type="text/javascript">
        var _mxm_ = {
				  "children":  ${scripts}
		    };
		function initLeftMenu(node){
           if(node.url!=undefined&&node.url!=''){
                openUrl(node.id,node.text,node.url,node.showmenu);
		   }
		   var nodes =_mxm_.children.children;
		   self.parent.leftFrame.menudata=_mxm_.children;
		   self.parent.leftFrame.initLeftTree(nodes,node.id);
		}
	    function openUrl(id,name,url,showmenu){
		   //addTab
		   //if(!(url.startsWith("http://") || url.startsWith("https://"))){
		   //   url="${contextPath}"+url;
		   //}
		   if(showmenu&&showmenu==2){
		      window.open(url,"_blank");
		   }else{
		       self.parent.rightFrame.addTabEx(id,name,url);
		    }
		}
		var defaultOpenNode;
		function initMenuData(){
			var nodes =_mxm_.children.children ;	
			for (var i = 0; i < nodes.length; i++) {
				var node = nodes[i];
				if(i==0)
				{
				  defaultOpenNode=node;
				}
				createNode(node);
			}
		}
		function createNode(node){
			var lidom=$("<li></li>");
			var adom=$("<a></a>");
			adom.attr("href","#");
			//adom.attr("onclick","javascript:initLeftMenu("+node.id+")");
			adom.attr("target","rightFrame");
			adom.unbind("click");
		    adom.bind("click",function(){initLeftMenu(node)});
			var imgdom=$("<img></img>");
			if(node.icon&&node.icon!=""){
			    imgdom.attr("src","${contextPath}"+node.icon);
			    imgdom.attr("title",node.text);
			    imgdom.css("vertical-align","middle");
			    imgdom.css("margin-right","3px");
			    adom.append(imgdom);
			}
			//else{
			 //   imgdom.attr("src","${contextPath}/static/images/star.png");
			//}
			adom.append(node.text);
			lidom.append(adom);
			$(".topmenu ul").append(lidom);
			//console.log($(".topmenu ul"));
		}
		initMenuData();
		//打开默认模块
		if(defaultOpenNode!=null&&defaultOpenNode!="")
		initLeftMenu(defaultOpenNode);
        </script>
	    </div>
	    <div class="formbody">
		<div id="usual1" class="usual">
			<div class="itab">
				<ul>
					<li>
					  <a id="home" href="#tab1" class="selected">首页</a>
					</li>
				</ul>
			</div>
			<div id="tab1" class="tabson">
				<iframe src="${indexUrl}" style="width: 100%;height:100%" />
			</div>
		</div>
	</div>
</div>
</body>

</html>
