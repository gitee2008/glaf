<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${chartCombination.title}</title>
<#include "/inc/init_highcharts_import.ftl"/>
<script type="text/javascript">


 $(function () {

	Highcharts.getOptions().colors = Highcharts.map(Highcharts.getOptions().colors, function(color) {
		return {
			radialGradient: { cx: 0.5, cy: 0.3, r: 0.7 },
			stops: [
				[0, color],
				[1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
			]
		};
	});

<#list charts as chart>
	<#if chart.chartType == 'column'>
    $('#container_${chart.id}').highcharts({
        chart: {
            type: 'column'
			<#if chart.enable3DFlag == '1'>
			,margin: 75
			,options3d: {
                enabled: true,
                alpha: 15,
                beta: 15,
                depth: 40,
                viewDistance: 25
            }
		    </#if>
        },
        title: {
            text: '${chart.chartTitle}'
        },
		<#if chart.chartSubTitle?exists>
        subtitle: {
            text: '${chart.chartSubTitle}'
        },
		</#if>
        xAxis: {
            categories: ${chart.categoriesScripts},
            title: {
                text: '${chart.coordinateX}'
            }
        },
        yAxis: {
            min: 0,
            title: {
                text: '${chart.coordinateY}',
                align: 'high'
            },
            labels: {
                overflow: 'justify'
            }
        },
        tooltip: {
            valueSuffix: ' '
        },
        plotOptions: {
            bar: {
                dataLabels: {
                    enabled: true
                }
            }
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            x: -40,
            y: 100,
            floating: true,
            borderWidth: 1,
            backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
            shadow: true
        },
        credits: {
            enabled: false
        },
        series: ${chart.seriesDataJson}
    });
    </#if>


    <#if chart.chartType == 'pie'>
	$('#container_${chart.id}').highcharts({
        chart: {
            type: 'pie'
			<#if chart.enable3DFlag == '1'>
			,options3d: {
                enabled: true,
                alpha: 35,
				 beta: 0
            }
		    </#if>
        },
        title: {
            text: '${chart.chartTitle}'
        },
		<#if chart.chartSubTitle?exists>
        subtitle: {
            text: '${chart.chartSubTitle}'
        },
		</#if>
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                depth: 45,
                dataLabels: {
                    enabled: true,
                    format: '{point.name}'
                }
            }
        },
        series: [{
            name: '${chart.chartTitle}',
            data: ${chart.pieData}
        }]
 
    });

  </#if>


  <#if chart.chartType == 'donut'>
	  $('#container_${chart.id}').highcharts({
        chart: {
            type: 'pie'
			<#if chart.enable3DFlag == '1'>
			,options3d: {
                enabled: true,
                alpha: 35,
				 beta: 0
            }
		    </#if>
        },
        title: {
            text: '${chart.chartTitle}'
        },
		<#if chart.chartSubTitle?exists>
        subtitle: {
            text: '${chart.chartSubTitle}'
        },
		</#if>
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                innerSize: 100,
                depth: 45
            }
        },
        series: [{
            name: '${chart.chartTitle}',
            data: ${chart.pieData}
        }]
    });
  </#if>


  <#if chart.chartType == 'area'>
    $('#container_${chart.id}').highcharts({
        chart: {
            type: 'area'
        },
        title: {
            text: '${chart.chartTitle}'
        },
		
		<#if chart.chartSubTitle?exists>
        subtitle: {
            text: '${chart.chartSubTitle}'
        },
		</#if>

        xAxis: {
            categories: ${chart.categoriesScripts},
            tickmarkPlacement: 'on',
            title: {
                enabled: false
            }
        },
        yAxis: {
            title: {
                text: '${chart.coordinateY}'
            },
            labels: {
                formatter: function () {
                    return this.value / 1000;
                }
            }
        },
        tooltip: {
            shared: true,
            valueSuffix: ' '
        },
        plotOptions: {
            area: {
                lineColor: '#666666',
                lineWidth: 1,
                marker: {
                    lineWidth: 1,
                    lineColor: '#666666'
                }
            }
        },
        series: ${chart.seriesDataJson}
    });

  </#if>


  <#if chart.chartType == 'radarLine'>
    $('#container_${chart.id}').highcharts({

        chart: {
            polar: true,
            type: 'line'
        },

        title: {
            text: '${chart.chartTitle}',
            x: -80
        },

		<#if chart.chartSubTitle?exists>
        subtitle: {
            text: '${chart.chartSubTitle}'
        },
		</#if>

        pane: {
            size: '80%'
        },

        xAxis: {
            categories: ${chart.categoriesScripts},
            tickmarkPlacement: 'on',
            lineWidth: 0
        },

        yAxis: {
            gridLineInterpolation: 'polygon',
            lineWidth: 0,
            min: 0
        },

        tooltip: {
            shared: true,
            pointFormat: '<span style="color:{series.color}">{series.name}: <b>{point.y:,.0f}</b><br/>'
        },

        legend: {
            align: 'right',
            verticalAlign: 'top',
            y: 70,
            layout: 'vertical'
        },

        series: ${chart.seriesDataJson}

    });
  </#if>


  <#if chart.chartType == 'bar'>
    $('#container_${chart.id}').highcharts({
        chart: {
            type: 'bar'
			<#if chart.enable3DFlag == '1'>
			,margin: 75
			,options3d: {
                enabled: true,
                alpha: 15,
                beta: 15,
                depth: 40,
                viewDistance: 25
            }
		    </#if>
        },
        title: {
            text: '${chart.chartTitle}'
        },
		<#if chart.chartSubTitle?exists>
        subtitle: {
            text: '${chart.chartSubTitle}'
        },
		</#if>
        xAxis: {
            categories: ${chart.categoriesScripts},
            title: {
                text: null
            }
        },
        yAxis: {
            min: 0,
            title: {
                text: '${chart.coordinateY}',
                align: 'high'
            },
            labels: {
                overflow: 'justify'
            }
        },
        tooltip: {
            valueSuffix: ' '
        },
        plotOptions: {
            bar: {
                dataLabels: {
                    enabled: true
                }
            }
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            x: -40,
            y: 100,
            floating: true,
            borderWidth: 1,
            backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
            shadow: true
        },
        credits: {
            enabled: false
        },
        series: ${chart.seriesDataJson}
    });
  </#if>

  <#if chart.chartType == 'line'>
    $('#container_${chart.id}').highcharts({
        title: {
            text: '${chart.chartTitle}',
            x: -20 //center
        },
		<#if chart.chartSubTitle?exists>
        subtitle: {
            text: '${chart.chartSubTitle}'
        },
		</#if>
        xAxis: {
            categories: ${chart.categoriesScripts}
        },
        yAxis: {
            title: {
                text: '${chart.coordinateY}'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true
                },
                enableMouseTracking: false
            }
        },
        tooltip: {
            valueSuffix: ''
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
        },
        series: ${chart.seriesDataJson}
    });

  </#if>

  
  <#if chart.chartType == 'stacked_area'>
	 $('#container_${chart.id}').highcharts({
        chart: {
            type: 'area'
        },
        title: {
            text: '${chart.chartTitle}'
        },
		<#if chart.chartSubTitle?exists>
        subtitle: {
            text: '${chart.chartSubTitle}'
        },
		</#if>
        xAxis: {
            categories: ${chart.categoriesScripts},
            tickmarkPlacement: 'on',
            title: {
                enabled: false
            }
        },
        yAxis: {
            title: {
                text: '${chart.coordinateY}'
            },
            labels: {
                formatter: function () {
                    return this.value / 1000;
                }
            }
        },
        tooltip: {
            shared: true,
            valueSuffix: ' '
        },
        plotOptions: {
            area: {
                stacking: 'normal',
                lineColor: '#666666',
                lineWidth: 1,
                marker: {
                    lineWidth: 1,
                    lineColor: '#666666'
                }
            }
        },
        series: ${chart.seriesDataJson}
    });
  </#if>


  <#if chart.chartType == 'stackedbar'>
    $('#container_${chart.id}').highcharts({
        chart: {
            type: 'column'
			<#if chart.enable3DFlag == '1'>
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
		<#if chart.chartSubTitle?exists>
        subtitle: {
            text: '${chart.chartSubTitle}'
        },
		</#if>
        xAxis: {
            categories: ${chart.categoriesScripts}
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
        legend: {
            align: 'right',
            x: -30,
            verticalAlign: 'top',
            y: 25,
            floating: true,
            backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
            borderColor: '#CCC',
            borderWidth: 1,
            shadow: false
        },
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
        series: ${chart.seriesDataJson}
    });
  </#if>

</#list>

});

</script>
</head>
<body style=" margin: 0">
 <table>
   <#if chartCombination.direction == 'H'>
   <tr>
     <#list charts as chart>
	   <td>
         <div id="container_${chart.id}" 
              style="min-width: 250px; max-width: ${chart.chartWidth}px; height: ${chart.chartHeight}px; width: ${chart.chartWidth}px; ">
		 </div>
	   </td>
	 </#list>
   </tr>
   <#else>
     <#list charts as chart>
	   <tr>
	     <td>
          <div id="container_${chart.id}" 
               style="min-width: 250px; max-width: ${chart.chartWidth}px; height: ${chart.chartHeight}px; width: ${chart.chartWidth}px; ">
		  </div>
		 </td>
	   </tr>
	 </#list>
   </#if>
 </table>
</body>
</html>