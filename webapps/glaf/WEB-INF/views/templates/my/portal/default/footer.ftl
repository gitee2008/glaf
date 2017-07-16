<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>footer</title>
<link href="${contextPath}/static/html/home/${homeTheme}/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${contextPath}/static/scripts/jquery.min.js"></script>
<script type="text/javascript">
	//显示当前时间
	function CurentTime() {
		var now = new Date();
		var hh = now.getHours();
		var mm = now.getMinutes();
		var ss = now.getTime() % 60000;
		ss = (ss - (ss % 1000)) / 1000;
		var clock = hh + ':';
		if (mm < 10)
			clock += '0';
		clock += mm + ':';
		if (ss < 10)
			clock += '0';
		clock += ss;
		return (clock);
	}
	function refreshCalendarClock() //
	{
		$("#clockTime").html(CurentTime());
	}

	setInterval('refreshCalendarClock()', 1000);//1秒钟刷新1次当前时间
	function findWeather() {
		var cityUrl = 'http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js';
		$
				.getScript(
						cityUrl,
						function(script, textStatus, jqXHR) {
							var citytq = remote_ip_info.city;// 获取城市
							var url = "http://php.weather.sina.com.cn/iframe/index/w_cl.php?code=js&city="
									+ citytq + "&day=0&dfc=3";
							$
									.ajax({
										url : url,
										dataType : "script",
										scriptCharset : "gbk",
										success : function(data) {
											var _w = window.SWther.w[citytq][0];
											var _f = _w.f1 + "_0.png";
											if (new Date().getHours() > 17) {
												_f = _w.f2 + "_1.png";
											}
											var img = "<img width='16px' height='16px' src='http://i2.sinaimg.cn/dy/main/weather/weatherplugin/wthIco/20_20/"+_f+"' />";
											var tq = "天气：" + citytq + "" + img
													+ "" + _w.s1 + "" + _w.t1
													+ "℃～" + _w.t2 + "℃"
													+ _w.d1 + _w.p1 + "级";
											$('#weather').html(tq);
										}
									});
						});
	}
	findWeather();
</script>
</head>

<body>
	<div class="footer" style="text-align: center;">
		<table style="width: 100%;height: 100%;">
			<tr>
				<td style="width:60%;text-align:right;">${res_copyright}</td>
				<td style="width:40%;text-align:right;">
				<table style="width: 100%; height:100%; text-align:right;">
				<tr>
				  <td width="350" align="right" style="vertical-align: middle;">
				    <img src="${contextPath}/static/html/home/${homeTheme}/images/d04.png"
					     width="18" style="vertical-align: middle;" /> ${curr_date}
					<div id="clockTime" style="font-size:14px;display: inline;"></div>
					&nbsp;&nbsp;
				  </td>
				  <!-- <td width="200" style="text-align:center;">
				    <div class="weather" id="weather"></div></td>
				  </td> -->
				</tr>
				</table>
				</td>
			</tr>
		</table>
	</div>
	<iframe id="newFrame" name="newFrame" width="0" height="0" src="${contextPath}/user/online/remain"></iframe>
</body>
</html>

