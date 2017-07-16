var userAgent = navigator.userAgent.toLowerCase();
var is_safari = userAgent.indexOf("safari") >= 0;
var is_opera = userAgent.indexOf('opera') != -1 && opera.version();
var is_moz = (navigator.product == 'Gecko')
		&& userAgent.substr(userAgent.indexOf('firefox') + 8, 3);
var is_chrome = userAgent.indexOf("chrome") >= 0;
var is_ie = (userAgent.indexOf('msie') != -1 && !is_opera)
		&& userAgent.substr(userAgent.indexOf('msie') + 5, 3);

var x_height = Math.floor(window.screen.height * 0.62);
var x_width = Math.floor(window.screen.width * 0.82);

if (window.screen.height <= 768) {
	x_height = Math.floor(window.screen.height * 0.60);
}

if (window.screen.width < 1200) {
	x_width = Math.floor(window.screen.width * 0.82);
} else if (window.screen.width > 1280) {
	x_width = Math.floor(window.screen.width * 0.72);
}

function getOpener() {
	if (is_moz) {
		return window.opener;
	} else if (is_chrome) {
		return window.opener;
	} else {
		return parent.dialogArguments;
	}
}

String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
}

String.prototype.startsWith = function(str) {
	if (str == null || str == "" || this.length == 0
			|| str.length > this.length) {
		return false;
	}
	if (this.substr(0, str.length) == str) {
		return true;
	} else {
		return false;
	}
	return true;
};

String.prototype.endsWith = function(str) {
	if (str == null || str == "" || this.length == 0
			|| str.length > this.length) {
		return false;
	}
	if (this.substring(this.length - str.length) == str) {
		return true;
	} else {
		return false;
	}
	return true;
};

function openWindow(URL, parent, x, y, width, height) {
	if (is_ie) {
		window.showModalDialog(URL, parent,
				"edge:raised;scroll:0;status:0;help:0;resizable:1;dialogWidth:"
						+ width + "px;dialogHeight:" + height + "px;dialogTop:"
						+ y + "px;dialogLeft:" + x + "px", true);
	} else {
		var f = "height="
				+ height
				+ ",width="
				+ width
				+ ",status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top="
				+ y
				+ ",left="
				+ x
				+ ",resizable=yes,modal=yes,dependent=yes,dialog=yes,minimizable=yes";
		window.open(URL, parent, f, true);
	}
}

function openMaxWin(link, parent) {
	var x = 50;
	var y = 50;
	if (is_ie) {
		// x=document.body.scrollLeft+event.clientX-event.offsetX-50;
		// y=document.body.scrollTop+event.clientY-event.offsetY-50;
	}
	var x_height = Math.floor(window.screen.height * 0.80);
	var x_width = Math.floor(window.screen.width * 0.96);
	openWindow(link, parent, x, y, x_width, x_height);
}

function openMMWin(link, parent) {
	var x = 50;
	var y = 50;
	if (is_ie) {
		// x=document.body.scrollLeft+event.clientX-event.offsetX-50;
		// y=document.body.scrollTop+event.clientY-event.offsetY-50;
	}
	// var x_height = Math.floor(window.screen.height * 0.72);
	// var x_width = Math.floor(window.screen.width*0.58);
	x_height = 595;
	x_width = 715;
	openWindow(link, parent, x, y, x_width, x_height);
}

function selectProvince(elementId) {
	var link = contextPath + "/district/json?parentId=0";
	jQuery.getJSON(link, function(data) {
		var province = document.getElementById(elementId);
		province.options.length = 0;
		province.options.add(new Option("----请选择----", ""));
		jQuery.each(data, function(i, item) {
			province.options.add(new Option(item.name, item.id));
		});
	});
}

function selectProvinceName(elementId) {
	var link = contextPath + "/district/json?parentId=0";
	jQuery.getJSON(link, function(data) {
		var province = document.getElementById(elementId);
		province.options.length = 0;
		province.options.add(new Option("----请选择----", ""));
		jQuery.each(data, function(i, item) {
			province.options.add(new Option(item.name, item.name));
		});
	});
}

function chooseDistrict(elementId, parentId) {
	var link = contextPath + "/district/json?parentId="+parentId;
	jQuery.getJSON(link, function(data) {
		var child = document.getElementById(elementId);
		child.options.length = 0;
		child.options.add(new Option("----请选择----", ""));
		jQuery.each(data, function(i, item) {
			child.options.add(new Option(item.name, item.name));
		});
	});
}

function selectDistrictByName(elementId, name) {
	var link = contextPath + "/district/json?name="+name;
	jQuery.getJSON(link, function(data) {
		var child = document.getElementById(elementId);
		child.options.length = 0;
		child.options.add(new Option("----请选择----", ""));
		jQuery.each(data, function(i, item) {
			child.options.add(new Option(item.name, item.name));
		});
	});
}

function selectDistrict(parentElement, childElement) {
	var parentId = document.getElementById(parentElement).value;
	if (parentId != "") {
		//alert(parentId);
		var link = contextPath + "/district/json?parentId=" + parentId;
		jQuery.getJSON(link, function(data) {
			var child = document.getElementById(childElement);
			child.options.length = 0;
			child.options.add(new Option("----请选择----", ""));
			jQuery.each(data, function(i, item) {
				child.options.add(new Option(item.name, item.id));
			});
		});
	}
}

function selectDistrictName(elementL1, elementL2) {
	var node = document.getElementById(elementL1).value;
	if (node != "") {
		var link = contextPath + "/district/json?parentId=" + node;
		jQuery.getJSON(link, function(data) {
			var child = document.getElementById(elementL2);
			child.options.length = 0;
			child.options.add(new Option("----请选择----", ""));
			jQuery.each(data, function(i, item) {
				child.options.add(new Option(item.name, item.name));
			});
		});
	}
}


function checkInteger(x){
  if(x.length==0){
    return true;
  }else{
    var pattern = /^-?\d+$/;
    if(x.match(pattern)==null){
      return false;
    }else{
     return true;
    }
  }
} 

function checkDouble(x){
  if(x.length==0) {
    return true;
  } else {
    var pattern=/^(-?\d+)(\.\d+)?$/;
    if(x.match(pattern)==null){
      return false;
    }else{
      return true;
    }
  }
} 



 function check_short(xx){
	 if (!(event.keyCode==45 || (event.keyCode>=48 && event.keyCode<=57))) {
		alert("该字段只能输入数字！");
		return false;
	 }
	 if(xx.value>65535 || xx.value <-65536){
		 return false;
	 }
	 return true;
 }

 function check_integer(xx){
	 if (!(event.keyCode==45 || (event.keyCode>=48 && event.keyCode<=57))) {
		alert("该字段只能输入数字！");
		return false;
	 }
	var x = xx.value*1;
	if(x >2147483647 || x <-2147483648){
		 return false;
	 }
	 return true;
 }

 function check_long(xx){
	 if (!(event.keyCode==45 || (event.keyCode>=48 && event.keyCode<=57))) {
		alert("该字段只能输入数字！");
		return false;
	 }
	var x = xx.value*1;
    if(x>9223372036854775807 || x <-9223372036854775808){
		 return false;
	 }
	 return true;
 }

 function check_double(xx){
    var berr = true;
	//alert(event.keyCode);
	if (!((event.keyCode>=45 && event.keyCode < 47 ) || (event.keyCode>=48 && event.keyCode<=57) || event.keyCode == 8 || event.keyCode == 190)) {
		alert("该字段只能输入数字！");
		return false;
	 }
	 return berr;
	 var x = xx.value;
	 if(x.length > 20){
		 return false;
	 }
	 return true;
 }
