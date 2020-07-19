<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="container-fluid">
    <div class="row">
        <div class="col">
            <div class="card card-stats">
                <div class="card-header card-header-warning card-header-icon">
                    <a href="#" style="color:white">
                        <div class="card-icon">
                            <i class="material-icons">${stat0}</i>
                        </div>
                    </a>
                    <p class="card-category">Samples Created but IOM(s) to be generated</p>
                    <h3 class="card-title">${stat0}</h3>
                </div>
                <div class="card-footer">
                    <div class="stats">
                        <c:if test="${stat0 ne 0}">
                            <i class="material-icons text-danger">Alert (Please Create IOM(s))</i>
                        </c:if>
                        <c:if test="${stat0 eq 0}">
                            <i class="material-icons text-success">All Done !!!</i>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card card-stats">
                <div class="card-header card-header-rose card-header-icon">
                    <a href="#" style="color:white">
                        <div class="card-icon">
                            <i class="material-icons">${stat1}</i>
                        </div>
                    </a>
                    <p class="card-category">IOM(s) Generated but not received by Lab</p>
                    <h3 class="card-title">${stat1}</h3>
                </div>
                <div class="card-footer">
                    <div class="stats">
                        <c:if test="${stat1 ne 0}">
                            <i class="material-icons text-warning">Please Follow-Up with Lab</i>
                        </c:if>
                        <c:if test="${stat1 eq 0}">
                            <i class="material-icons text-success">All Done !!!</i>
                        </c:if>

                    </div>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card card-stats">
                <div class="card-header card-header-success card-header-icon">
                    <a href="#" style="color:white">
                        <div class="card-icon">
                            <i class="material-icons">${stat2}</i>
                        </div>
                    </a>
                    <p class="card-category">Received by Lab, Test result not Entered</p>
                    <h3 class="card-title">${stat2}</h3>
                </div>
                <div class="card-footer">
                    <div class="stats">
                        <c:if test="${stat2 ne 0}">
                            <i class="material-icons text-warning">Please Follow-Up with Lab</i>
                        </c:if>
                        <c:if test="${stat2 eq 0}">
                            <i class="material-icons text-success">All Done !!!</i>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card card-stats">
                <div class="card-header card-header-info card-header-icon">
                    <a href="#" style="color:white">
                        <div class="card-icon">
                            <i class="material-icons">${stat3}</i>
                        </div>
                    </a>
                    <p class="card-category">Sent by Lab to Tse</p>
                    <h3 class="card-title">${stat3}</h3>
                </div>
                <div class="card-footer">
                    <div class="stats">
                        <c:if test="${stat3 ne 0}">
                            <i class="material-icons text-danger">Alert (Take necessary Action)</i>
                        </c:if>
                        <c:if test="${stat3 eq 0}">
                            <i class="material-icons text-success">All Done !!!</i>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card card-stats">
                <div class="card-header card-header-info card-header-icon">
                    <a href="#" style="color:white">
                        <div class="card-icon">
                            <i class="material-icons">${stat4}</i>
                        </div>
                    </a>
                    <p class="card-category">Finalized and Send to Customer</p>
                    <h3 class="card-title">${stat4}</h3>
                </div>
                <div class="card-footer">
                    <div class="stats">
                        <c:if test="${stat4 ne 0}">
                            <i class="material-icons text-warning">Please Follow-Up with Customer</i>
                        </c:if>
                        <c:if test="${stat4 eq 0}">
                            <i class="material-icons text-success">All Done !!!</i>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-6 col-xl-4">
            <div class="card card-chart" data-count="4">
                <div class="card-header card-header-info" data-header-animation="true">
                    <div id="last10Graph" class="highcharts-container last10Graph" style="height: 400px"></div>
                </div>
                <div class="card-body">
                    <h4 class="card-title">Sample(s) </h4>
                    <p class="card-category">Last 10 Sample Creation Events (Day Wise)</p>
                </div>
                <div class="card-footer">
                    <div class="stats">
                        <i class="material-icons glyphicon glyphicon-time"></i> 10 days ago
                    </div>
                </div>
            </div>
        </div>
        <div class="col-lg-6 col-xl-4">
            <div class="card card-chart" data-count="4">
                <div class="card-header card-header-success" data-header-animation="true">
                    <div id="noOfTanksGraph" class="highcharts-container noOfTanksGraph" style="height: 400px"></div>
                </div>
                <div class="card-body">
                    <div class="card-actions">
                    </div>
                    <h4 class="card-title">Your Tanks</h4>
                    <p class="card-category">
                        Tanks Handled by You
                    </p>
                </div>
                <div class="card-footer">
                    <div class="stats">
                        Updated Now
                    </div>
                </div>
            </div>
        </div>
        <div class="col-lg-6 col-xl-4">
            <div class="card card-chart" data-count="3">
                <div class="card-header card-header-info" data-header-animation="true">
                    <div id="upcomingGraph" class="upcomingGraph" style="min-width: 310px; height: 400px; max-width: 600px; margin: 0px auto; overflow: hidden;"></div>
                </div>
                <div class="card-body">
                    <div class="card-actions">
                    </div>
                    <h4 class="card-title">Pending Events</h4>
                    <p class="card-category">Pending Events within 10 Days</p>
                </div>
                <div class="card-footer">
                    <div class="stats">
                        <i class="material-icons glyphicon glyphicon-time"></i> Pending Events
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <div class="card card-chart" data-count="4">
                <div class="card-header card-header-info">
                    <i class="glyphicon glyphicon-time"></i><i class="material-icons"> Recent Activity</i>
                </div>
                <div class="card-body">
                    <h4 class="card-title">Your Recent Activity Status</h4>
                    <div class="table-responsive table-sales">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Sample Id</th>
                                    <th>Customer Name</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${recActivity ne null}">
                                    <c:forEach items="${recActivity}" var="data">
                                        <tr>
                                            <td>${data.sampleId}</td>
                                            <td>${data.custName}</td>
                                            <td>${data.statusName}</td>
                                        </tr>
                                    </c:forEach>
                                </c:if>
                                <c:if test="${empty recActivity}">
                                    <tr>
                                        <td colspan="3" class="text-center" style="border-bottom: 1px solid lightgray"><strong>No Recent Activity</strong></td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="card-footer">
                    <div class="stats">
                        <i class="material-icons glyphicon glyphicon-time"></i> Activity since ${TseDashDaysLimit} Days ago
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card card-chart" data-count="4" style="height: 442px">
                <div class="card-header card-header-rose">
                    <i class="glyphicon glyphicon glyphicon-list-alt"></i><i class="material-icons"> News</i>
                </div>
                <div class="card-body">
                    <h4 class="card-title">Important Information</h4>
                    <marquee direction="down"  style="height:65%" scrollDelay="300" onmouseover="this.stop();" onmouseout="this.start();">
                        <div class="panel-group">
                            <c:forEach items="${news}" var="data">
                                <div class="panel panel-warning">
                                    <div class="panel-heading"><b> ${data.msgTitle}</b></div>
                                    <div class="panel-body">${data.msgBody}</div>
                                </div>

                            </c:forEach>
                        </div>
                    </marquee>
                </div>
                <div class="card-footer">
                    <div class="stats">
                        <i class="material-icons glyphicon glyphicon-time"></i> Updated 0 days ago
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    function UpcomingGraph(output, DDoutput) {
        Highcharts.setOptions({
            colors: ['#6AF9C4', '#35D5C0', '#11B0B2', '#00DDCD', '#1D8C9B', '#0081B7', '#2C697C', '#6AF9C4']
        });
//        
// Make monochrome colors
        Highcharts.setOptions({
            colors: Highcharts.map(Highcharts.getOptions().colors, function (color) {
                return {
                    radialGradient: {
                        cx: 0.5,
                        cy: 0.3,
                        r: 0.7
                    },
                    stops: [
                        [0, color],
                        [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
                    ]
                };
            })
        });
// Build the chart

        Highcharts.chart('upcomingGraph', {
            chart: {
                backgroundColor: 'transparent',
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: 'pie'
            },
            title: {
                text: 'Top 10 Upcoming Data By customer',
                style: {
                    color: '#fff'
                }
            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.y}</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    borderColor: 'transparent',
                    dataLabels: {
                        enabled: true,
                        format: '<i>{point.name}<br>(<label>Percentage</label>:{point.percentage:.0f}%)</i>',
                        style: {
                            textOutline: 'none',
                            textDecoration: 'none'
                        },
                        connectorColor: 'black'
                    }
                }
            },
            series: [{
                    name: 'Total_Notifications',
                    data: output
//                        [{name: "Stevenfirst", y: 1, "drilldown": "Stevenfirst"},
//                        {name: "FernandesSteven", y: 1, "drilldown" :"FernandesSteven"},
//                        {name: "FernandesNick", y: 1, "drilldown":"FernandesNick"}]
                }],
            drilldown: {
                "series":
                        DDoutput
//                        [{       "name": "Stevenfirst",
//                                "id": "Stevenfirst",
//                                "data": [
//                                    ["v60.0",0.56]
//                                ]
//                            },
//                            {
//                                "name": "FernandesSteven",
//                                "id": "FernandesSteven",
//                                "data": [
//                                    [ "v58.0",1.02 ],
//                                    [ "v57.0",7.36 ]
//                                ]
//                            }]
            }
        });
    }

    function Last10DataGraph(output, DDoutput) {
        Highcharts.setOptions({
            colors: ['#6AF9C4', '#35D5C0', '#11B0B2', '#00DDCD', '#1D8C9B', '#0081B7', '#2C697C', '#6AF9C4']
        });

        Highcharts.setOptions({
            colors: Highcharts.map(Highcharts.getOptions().colors, function (color) {
                return {
                    radialGradient: {
                        cx: 0.5,
                        cy: 0.3,
                        r: 0.7
                    },
                    stops: [
                        [0, color],
                        [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
                    ]
                };
            })
        });
// Build the chart

        Highcharts.chart('last10Graph', {
            chart: {
                backgroundColor: 'transparent',
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: 'pie'
            },
            legend: {
                align: 'right',
                verticalAlign: 'top',
                layout: 'vertical',
                x: 0,
                y: 100
            },
            title: {
                text: 'Top 10 Pending Data By customer',
                style: {
                    color: '#fff'
                }
            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.y}</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    borderColor: 'transparent',
                    dataLabels: {
                        enabled: true,
                        format: '<i>{point.name}<br>(<label>Percentage</label>:{point.percentage:.0f}%)</i>',
                        color: 'black',
                        style: {
                            color: 'black', //(Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black',
                            font: '12px arial, monospace;',
                            textOutline: 'none',
                            textDecoration: 'none'
                        },
                        connectorColor: 'black'
                    }
                }
            },
            series: [{
                    name: 'Total_Notifications',
                    data: output
                }],
            drilldown: {
                "series":
                        DDoutput
            }
        });
    }
    function sliceAndDice(data, uniques) {
        var b = _(data).map((objs, key) => ({
                'name': objs.name,
                'y': objs.y,
                'date': objs.date
            }))
                .value();

        var arrNew = [];
        _.forEach(uniques, function (v) {
            var name = v.name;
            var newObj = {
                "name": v.name,
                "id": v.name,
                "data": []
            };
            console.log(name);
            var arrr = [];
            _.forEach(data, function (v) {
                if (v.name == name) {
                    var d = (_.trimEnd(v.date, ' 00:00:00.0'));
                    arrr.push([(d.split('-')[2] + '/' + d.split('-')[1] + '/' + d.split('-')[0]), parseInt(v.y)]);
                }
            });
            newObj.data = arrr;
            arrNew.push(newObj);
        });
        return arrNew;
    }

    function formatJSONArray1(data) {
        var arr1 = _(data["myArrayList"]).map((objs, key) => ({
                'name': objs.map["name"],
                'y': parseInt(objs.map["y"]),
                'date': objs.map["date"],
                'custId': objs.map["custId"]
            })).value();
        return arr1;
    }

    $(document).ready(function () {
        $.ajax({
            async: false,
            url: '/ServoCMP/LabAdmin/redirectController?url=getUpcomingData',
            type: 'POST',
            dataType: 'JSON',
            success: function (data) {
                data = formatJSONArray1(data);
                console.log(data);
                var seriesData = _(data)
                        .groupBy('name')
                        .map((objs, key) => ({
                                'name': key,
                                'y': parseInt(_.sumBy(objs, 'y')),
                                'drilldown': key
                            }))
                        .value();
                var uniques = _.uniqBy(data, function (e) {
                    return e.name;
                });
                var drilldownData = sliceAndDice(data, uniques);

                console.log(seriesData);
                console.log(drilldownData);
                UpcomingGraph(seriesData, drilldownData);

                $('body > div.modal-backdrop.in').remove();
            },
            error: function (error) {
                console.log(error);
            }
        });
        $.ajax({
            async: false,
            url: '/ServoCMP/LabAdmin/redirectController?url=getLast10Data',
            type: 'POST',
            dataType: 'JSON',
            success: function (data) {
//                console.log(data);
                data = formatJSONArray1(data);
                console.log(data);
                var seriesData = _(data)
                        .groupBy('name')
                        .map((objs, key) => ({
                                'name': key,
                                'y': parseInt(_.sumBy(objs, 'y')),
                                'drilldown': key
                            }))
                        .value();
                var uniques = _.uniqBy(data, function (e) {
                    return e.name;
                })
                var drilldownData = sliceAndDice(data, uniques);
                console.log(seriesData);
                console.log(drilldownData);
                Last10DataGraph(seriesData, drilldownData);
            },
            error: function (error) {
                console.log(error);
            }
        });
    });</script>
<!--<script>
    var last10Data = [${last10Data}];
    last10DataSeries = [{
            name: 'Sample',
            data: [${last10Data}]
        }];
    Highcharts.chart('last10Graph', {
        credits: {
            enabled: false
        },
        chart: {
            backgroundColor: 'transparent',
            type: 'line'
        },
        title: {
            text: 'Activity of Last 10 Events',
            style: {
                color: '#fff'
            }
        },
        yAxis: {
            title: {
                text: 'Number of Sample(s)',
                style: {
                    color: '#fff'
                }
            },
            labels: {
                style: {'color': '#fff'}
            }
        },
        legend: {
            layout: 'vertical',
            align: 'centre',
            verticalAlign: 'bottom',
            itemStyle: {
                color: '#fff'
            }
        },
        plotOptions: {
            line: {
                color: '#fff',
                label: {
                    connectorAllowed: false,
                    style: {'color': '#fff'}
                }
            }
        },
        tooltip: {
            formatter: function () {
                return Highcharts.dateFormat('%e-%b-%Y', new Date(this.x)) + ', <br/>' + this.y + ', <b>' + this.series.name + '</b><br/>';
            }
        },
        xAxis: {
            labels: {
                format: '{value:%d-%m-%Y}',
                style: {
                    color: '#fff'
                }
            }
        },
        series: last10DataSeries,
        responsive: {
            rules: [{
                    chartOptions: {
                        legend: {
                            layout: 'horizontal',
                            align: 'center',
                            verticalAlign: 'bottom'
                        }
                    }
                }]
        }
    });</script>-->
<script>
    Highcharts.chart('noOfTanksGraph', {
        credits: {
            enabled: false
        },
        chart: {
            backgroundColor: 'transparent',
            type: 'column'
        },
        title: {
            text: 'Individual Industry with its Customers',
            style: {
                color: '#fff'
            }
        },
        yAxis: {
            title: {
                text: 'Total Number of Customer in Industry',
                style: {
                    color: '#fff'
                }
            },
            labels: {
                style: {'color': '#fff'}
            }
        },
        legend: {
            enabled: false
        },
        plotOptions: {
            series: {
                color: '#fff',
                borderWidth: 0,
                label: {
                    connectorAllowed: false,
                    style: {'color': '#fff'}
                },
                dataLabels: {
                    enabled: true,
                    format: '{point.y:.1f}',
                    style: {
                        textOutline: 'none'
                    },
                }
            }
        },
        xAxis: {
            type: 'category',
            labels: {
                style: {
                    color: '#fff'
                }
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
            pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}</b> of total<br/>'
        },
        series: [${IndustryGraph}],
        drilldown: {${indCust}}
    });
</script>


<!--<script>
    var upcomingDataSeries = [{
            name: 'Sample',
            data: [${upcomingData}]
        }];

    Highcharts.chart('upcomingGraph', {
        credits: {
            enabled :false
        },
        xAxis: {
            labels: {
                format: '{value:%d-%m-%Y}',
                style: {
                    color: '#fff'
                }
            }
        },
        tooltip: {
            formatter: function () {
                return Highcharts.dateFormat('%e-%b-%Y', new Date(this.x)) + ', <br/>' + this.y + ', <b>' + this.series.name + '</b><br/>';
            }
        },
        chart: {
            backgroundColor: 'transparent',
            type: 'line'
        },
        title: {
            text: 'Upcoming Events',
            style: {
                color: '#fff'
            }
        },
        yAxis: {
            title: {
                text: 'Number of Sample(s)',
                style: {
                    color: '#fff'
                }
            },
            labels: {
                style: {'color': '#fff'}
            }
        },
        legend: {
            layout: 'vertical',
            align: 'centre',
            verticalAlign: 'bottom',
            itemStyle: {
                color: '#fff'
            }
        },
        plotOptions: {
            series: {
                color: '#fff',
                label: {
                    connectorAllowed: false,
                    style: {'color': '#fff'}
                }
            }
        },
        series: upcomingDataSeries,
        responsive: {
            rules: [{
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
</script>-->