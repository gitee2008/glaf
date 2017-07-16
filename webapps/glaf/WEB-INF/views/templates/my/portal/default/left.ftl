<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>left</title>
<link href="${contextPath}/static/html/home/${homeTheme}/css/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
  #cdiv {
	position:absolute;
	width:30px;height:30px;
	z-index:100;
	top:50%;
	left:90%;
  }
</style>
<script type="text/javascript" src="${contextPath}/static/scripts/jquery.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){	
	//导航切换
	$(".menuson .header").click(function(){
		var $parent = $(this).parent();
		$(".menuson>li.active").not($parent).removeClass("active open").find('.sub-menus').hide();
		$parent.addClass("active");
		if(!!$(this).next('.sub-menus').size()){
			if($parent.hasClass("open")){
				$parent.removeClass("open").find('.sub-menus').hide();
			} else {
				$parent.addClass("open").find('.sub-menus:first').show();	
			}
		}
	});
	
	// 三级菜单点击
	$('.sub-menus li').click(function(e) {
        $(".sub-menus li.active").removeClass("active");
        $(".has-child li.active").removeClass("active")
		$(this).addClass("active");
    });
	
	$('.title').click(function(){
		var $ul = $(this).next().next();
		$('dd').find('.menuson').slideUp();
		if($ul.is(':visible')){
			$(this).next('.menuson').slideUp();
		}else{
			$(this).next('.menuson').slideDown();
		}
	});
});
</script>
</head>
<body>
	<div class="lefttop">
		<span></span>系统菜单
	</div>
	<div id="cdiv">
	    <img id="coll_img" src="${contextPath}/static/html/home/${homeTheme}/images/nav.gif" width="18" style="text-align:center;vertical-align:middle;margin: auto;cursor: pointer;" onclick="collapse()"/>
	</div>
	<dl class="leftmenu">
		
	</dl>
	<script type="text/javascript">
		String.prototype.startsWith = function(s) {
			if (s == null || s == "" || this.length == 0 || s.length > this.length){
				return false;
			}
			if (this.substr(0, s.length) == s){
				return true;
			}
			else {
				 return false;
			}
			return true;
		};
	  var menudata;
      function initLeftTree(nodes,nodeId){
            $(".leftmenu").empty();
			var selectNode = retSelectNode(nodes,nodeId);
			if(selectNode!=null){
			    $(".lefttop").html("<span></span>"+selectNode.text);
				initMenuson(selectNode);
			}
			else{
			    console.log(menudata);
			    initMenuson(menudata);
			}
			//导航切换
			$(".menuson .header").unbind("click");
			$(".menuson .header").click(function(){
				var $parent = $(this).parent();
				$(this).parent().parent().find("li").not($(this).parent()).removeClass("active open");
				$(this).parent().addClass("active");
				//获取临近的ul
				var ul=$(this).next().next();
				if(ul&&ul.length>0){
				    if($(this).parent().hasClass("open")){
				       $(this).parent().removeClass("open");
				       ul.hide();
				    }
				    else{
				       $(this).parent().addClass("open");
				       ul.show();
				    }
				}
			});
	$(".sub-menus li").unbind("click");
	$(".has-child li").unbind("click");
	// 三级菜单点击
	$('.sub-menus li').click(function(e) {
        $(".sub-menus li.active").removeClass("active");
        $(".has-child li.active").removeClass("active");
		$(this).addClass("active");
    });
	$(".title").unbind("click");
	$('.title').click(function(){
		var $ul = $(this).next('ul');
		$('dd').find('.menuson').slideUp();
		if($ul.is(':visible')){
			$(this).next('.menuson').slideUp();
		}else{
			$(this).next('.menuson').slideDown();
		}
	});
		}
		//查找匹配节点
		function retSelectNode(nodes,nodeId){
			for (var i = 0; i < nodes.length; i++) {
				var node = nodes[i];
				if(node.id ==  nodeId){
					return node ;
				}
			}
			return null ;
		}
		function initMenuson(selectNode){
			if("children" in selectNode){
				createMenu(selectNode.children,true,0)
			}
		}
		function createMenu(nodes,bol,count,menusonId){
			var t;
			for(var i=0;i<nodes.length;i++){
			    t = count ;
				var node = nodes[i];
				var obj = {} ;
				var lidom;
				obj.text = node.text ;
				var imagePath = "${contextPath}/static/html/home/${homeTheme}/images/leftico01.png" ;
				if(node.icon){
					imagePath ="${contextPath}"+node.icon;
				}
				obj.imageUrl = imagePath ;
				if(bol){
					if(i==0){
						obj.expanded = true ;
					}
				}
				
				//创建第一层
				if(count == 0){
				 	   //创建菜单项
						var dddom=$("<dd></dd>");
						var divtitledom=$("<div></div>");
						divtitledom.addClass("title");
						 var adom=$("<a></a>");
						 adom.attr("href","#");
						 if(node.url&&node.url!="")
						 {
						      adom.attr("onclick","openUrl("+node.id+",\""+node.name+"\",\""+node.url+"\",\""+node.showmenu+"\")");
				         }
				        //adom.attr("target","rightFrame");
						var spantitledom=$("<span></span>");
						var imgtitledom=$("<img></img>");
						imgtitledom.attr("src",imagePath);
						spantitledom.append(imgtitledom);
						adom.append(spantitledom);
						adom.append(node.text);
						divtitledom.append(adom);
						dddom.append(divtitledom);
						var menusondom=$("<ul id=\""+node.id+"\" class=\"menuson\"></ul>");
						dddom.append(menusondom);
						$(".leftmenu").append(dddom);
						
				}
				else if(count == 1){
				         lidom=$("<li id=\""+node.id+"_li\"></li>");
				         var divdom=$("<div></div>");
				         divdom.addClass("header");
				         var citedom=$("<cite></cite>");
				         divdom.append(citedom);
				         var adom=$("<a></a>");
				          adom.attr("href","#");
						 if(node.url&&node.url!="")
						 {
						      adom.attr("onclick","openUrl("+node.id+",\""+node.name+"\",\""+node.url+"\",\""+node.showmenu+"\")");
				         }
				         adom.append(node.text);
				         divdom.append(adom);
				         var splitlidom=$("<li></li>");
				         divdom.append(splitlidom);
				         lidom.append(divdom);
				         lidom.append("<i></i>");
				         $("#"+menusonId).append(lidom);
				}
				else{
						 var lidom=$("<li></li>");
						 var adom=$("<a></a>");
						  adom.attr("href","#");
						 if(node.url&&node.url!="")
						 {
						      adom.attr("onclick","openUrl("+node.id+",\""+node.name+"\",\""+node.url+"\",\""+node.showmenu+"\")");
				         }
				         adom.append(node.text);
				         lidom.append(adom);
						 $("#"+menusonId).append(lidom);
				 }
				if(node.url){
					obj.url = "javascript:openUrl(\""+node.url+"\")" ;
					if(count == 1 && i==0){
						//openUrl(node.id,node.name,node.url);
					}
					//只有一级菜单
					if(count == 0 && !node.children && i==0){
						//openUrl(node.id,node.name,node.url);
				    }
				   }
				if("children" in node){
				    if(t>=1){
				       var uldom=$("<ul id=\""+node.id+"\" class=\"sub-menus\"></ul>");
				       $("#"+node.id+"_li").append(uldom);
				    }
					createMenu(node.children,false,++t,node.id);
				}
			}
		}
		function openUrl(id,name,url,showmenu){
		   //addTab
		   //if(!(url.startsWith("http://") || url.startsWith("https://"))){
		   //   url="${contextPath}"+url;
		   //}
		   if(showmenu&&showmenu==2){
		      window.open(url,"_blank");
		   
		   } else {
		       self.parent.rightFrame.addTabEx(id,name,url);
		    }
		}
		function collapse(){
          window.parent.document.getElementsByTagName("frameset")[1].cols="0,*";
          self.parent.rightFrame.showExpd();
        }
    </script>
</body>
</html>
