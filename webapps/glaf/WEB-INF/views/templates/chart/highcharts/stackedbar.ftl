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
            type: 'column'
			<#if chart.enable3DFlag == '1' >
			,margin: 75
			,options3d: {
                enabled: true,
                alpha: 15,
                beta: 15,
                depth: 50,
                viewDistance: 25
            }
		    </#if>
        },
        title: {
            text: '${chart.chartTitle}'
        },
		<#if chart.chartSubTitle?exists >
        subtitle: {
            text: '${chart.chartSubTitle}'
        },
		</#if>
        xAxis: {
            categories: ${categories_scripts}
        },
        yAxis: {
            min: 0,
            title: {
                text: '${chart.coordinateY}'
            },
            stackLabels: {
                enabled: true,
                style: {
                    fontWeight: 'bold',
                    color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                }
            }
        },
        <#if chart.legend == 'Y'>
        legend: {
            align: 'center',
            verticalAlign: 'bottom',
            floating: false,
            borderWidth: 1,
            backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
            shadow: true
        },
		</#if>
        tooltip: {
            formatter: function () {
                return '<b>' + this.x + '</b><br/>' +
                    this.series.name + ': ' + this.y + '<br/>' +
                    'Total: ' + this.point.stackTotal;
            }
        },
        plotOptions: {
            column: {
                stacking: 'normal',
                dataLabels: {
                    enabled: true,
                    color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white',
                    style: {
                        textShadow: '0 0 3px black'
                    }
                }
            }
        },
        series: ${seriesDataJson}
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