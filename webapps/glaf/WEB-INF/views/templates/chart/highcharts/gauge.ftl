<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${chart.subject}</title>
<#include "/inc/init_highcharts_import.ftl"/>
<script type="text/javascript">

 $(function () {

    <#if chart.gradientFlag == '1' >
	Highcharts.getOptions().colors = Highcharts.map(Highcharts.getOptions().colors, function(color) {
		return {
			radialGradient: { cx: 0.5, cy: 0.3, r: 0.7 },
			stops: [
				[0, color],
				[1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
			]
		};
	});
	</#if>

    $('#container').highcharts({
 
		  chart: {
            type: 'gauge',
            plotBackgroundColor: null,
            plotBackgroundImage: null,
            plotBorderWidth: 0,
            plotShadow: false
        },

        title: {
            text: '${chart.chartTitle}'
        },

        pane: {
            startAngle: -150,
            endAngle: 150,
            background: [{
                backgroundColor: {
                    linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
                    stops: [
                        [0, '#FFF'],
                        [1, '#333']
                    ]
                },
                borderWidth: 0,
                outerRadius: '109%'
            }, {
                backgroundColor: {
                    linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
                    stops: [
                        [0, '#333'],
                        [1, '#FFF']
                    ]
                },
                borderWidth: 1,
                outerRadius: '107%'
            }, {
                // default background
            }, {
                backgroundColor: '#DDD',
                borderWidth: 0,
                outerRadius: '105%',
                innerRadius: '103%'
            }]
        },

        // the value axis
        yAxis: {
            min: ${chart.minScale},
            max: ${chart.maxScale},

            minorTickInterval: 'auto',
            minorTickWidth: 1,
            minorTickLength: ${chart.stepScale},
            minorTickPosition: 'inside',
            minorTickColor: '#666',

            tickPixelInterval: 60,
            tickWidth: 2,
            tickPosition: 'inside',
            tickLength: 10,
            tickColor: '#666',
            labels: {
                step: 2,
                rotation: 'auto'
            },
            title: {
                text: '${chart.coordinateY}'
            },
            plotBands: [{
                from: 0,
                to: 120,
                color: '#55BF3B' // green
            }, {
                from: 120,
                to: 160,
                color: '#DDDF0D' // yellow
            }, {
                from: 160,
                to: 200,
                color: '#DF5353' // red
            }]
        },

        series:  ${seriesDataJson}
	 
   });
 });

<#if chooseThemes == 'true'>
 
$(document).ready(function(){
	$("button.btn").click(function(){
		var theme = $(this).attr("theme");
		if(theme != null) {
		    window.location.href="${contextPath}/chart/highcharts/showChart?chartId=${chart.id}&charts_theme="+theme+"&chooseThemes=${chooseThemes}";
		}
    });
});
</#if>
</script>
</head>
<body>
<#if chooseThemes == 'true'>
 <p>
	图表主题：
	<button class="btn btnGray" theme="default">默认</button>
	<button class="btn btnGray" theme="grid">网格 (grid)</button>
	<button class="btn btnGray" theme="grid-light">grid-light</button>
	<button class="btn btnGray" theme="skies">天空 (skies)</button>
	<button class="btn btnGray" theme="gray">灰色 (gray)</button>
	<button class="btn btnGray" theme="dark-blue">深蓝 (dark-blue)</button>
	<button class="btn btnGray" theme="dark-green">深绿 (dark-green)</button>
	<button class="btn btnGray" theme="dark-unica">dark-unica</button>
	<button class="btn btnGray" theme="sand-signika">sand-signika</button>
  </p>
 </#if>
 <div id="container" style="min-width: 80px; max-width: ${chart.chartWidth}px; height: ${chart.chartHeight}px; margin: 0 auto"></div>
</body>
</html>