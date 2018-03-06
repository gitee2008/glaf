<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${chart.subject}</title>
<#include "/inc/init_highcharts_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/plugins/highcharts/themes/grid-light.js"></script>
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

		Highcharts.chart('container', {

            colors:[ 
			      '#333333', 
                  '#ff6666', 
				  '#ffcc00',
				  '#00cc33',
                  '#ffcc00',  
                  '#ff6699',  
				  '#333333',
				  '#0066ff'
                ],

			title: {
				text: '${chartTitle}'
			},

            <#if chartSubTitle?exists>
			subtitle: {
				text: '${chartSubTitle}'
			},
			</#if>

			xAxis: {
					categories: ${categories}
				},

			yAxis: {
				title: {
					text: '${yAxisTitle}'
				}
			},
			legend: {
				layout: 'vertical',
				align: 'right',
				verticalAlign: 'middle'
			},

			plotOptions: {
					spline: {
						marker: {
							enable: false
						}
					}
			},

			series: ${seriesData},

			responsive: {
				rules: [{
					condition: {
						maxWidth: 1280
					},
					chartOptions: {
						legend: {
							layout: 'horizontal',
							align: 'center',
							verticalAlign: 'bottom'
						}
					}
				}]
			}

		});

    });
</script>
</head>
<body>
 <div id="container" style="min-width:80px; max-width:1480px; max-height:720px; margin: 0 auto; height:708px;"></div>
</body>
</html>