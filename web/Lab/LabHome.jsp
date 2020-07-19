<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="container-fluid">
    <div class="row">
        <div class="col">
            <div class="card card-stats">
                <div class="card-header card-header-rose card-header-icon">
                    <a href="${pageContext.request.contextPath}/Lab/SampleSendByTSE?csrftoken=${sessionScope.csrfToken}" style="color:white">
                        <div class="card-icon">
                            <i class="material-icons">${stat1}</i>
                        </div>
                    </a>
                    <p class="card-category">Yet to be Acknowledged</p>
                    <h3 class="card-title">${stat1}</h3>
                </div>
                <div class="card-footer">
                    <div class="stats">
                        <c:if test="${stat1 ne 0}">
                            <i class="material-icons text-warning">Please Acknowledge IOM(s)</i>
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
                    <a href="${pageContext.request.contextPath}/Lab/GetReceivedSample?status=2&csrftoken=${sessionScope.csrfToken}" style="color:white">
                        <div class="card-icon">
                            <i class="material-icons">${stat2}</i>
                        </div>
                    </a>
                    <p class="card-category">Acknowledged but Test Results need to be Entered</p>
                    <h3 class="card-title">${stat2}</h3>
                </div>
                <div class="card-footer">
                    <div class="stats">
                        <c:if test="${stat2 ne 0}">
                            <i class="material-icons text-warning">Please Complete Testing</i>
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
                    <a href="${pageContext.request.contextPath}/Lab/GetReceivedSample?status=3&csrftoken=${sessionScope.csrfToken}" style="color:white">
                        <div class="card-icon">
                            <i class="material-icons">${stat3}</i>
                        </div>
                    </a>
                    <p class="card-category">Test Result Finalized and sent to Tse</p>
                    <h3 class="card-title">${stat3}</h3>
                </div>
                <div class="card-footer">
                    <div class="stats">
                        <c:if test="${stat3 ne 0}">
                            <i class="material-icons text-danger">Follow Up with Tse</i>
                        </c:if>
                        <c:if test="${stat3 eq 0}">
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
                    <div id="last10Graph" class="highcharts-container"></div>
                </div>
                <div class="card-body">
                    <h4 class="card-title">Sample(s) </h4>
                    <p class="card-category">Samples acknowlegded and pending for process(Priority Wise)</p>
                </div>
                <div class="card-footer">
                    <div class="stats">
                        <i class="material-icons glyphicon glyphicon-time"></i> Pending Samples
                    </div>
                </div>
            </div>
        </div>
        <div class="col-lg-6 col-xl-4">
            <div class="card card-chart" data-count="4">
                <div class="card-header card-header-info" data-header-animation="true">
                    <div id="noOfTanksGraph" class="highcharts-container"></div>
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
                    <div id="upcomingGraph" class="highcharts-container"></div>
                </div>
                <div class="card-body">
                    <div class="card-actions">
                    </div>
                    <h4 class="card-title">Lab Sample(s)</h4>
                    <p class="card-category">Samples acknowlegded and pending for process(Industry Wise)</p>
                </div>
                <div class="card-footer">
                    <div class="stats">
                        <i class="material-icons glyphicon glyphicon-time"></i> Pending Samples
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-6">
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
                        <i class="material-icons glyphicon glyphicon-time"></i> Activity since ${LabDashDaysLimit} Days ago
                    </div>
                </div>
            </div>
        </div>
        <div class="col-lg-6">
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
    });
</script>-->
<script>
    function PendingLabSample(priorityData, DDoutput) {

        Highcharts.setOptions({
            colors: ['#6AF9C4', '#35D5C0', '#11B0B2', '#00DDCD', '#1D8C9B', '#0081B7', '#2C697C', '#6AF9C4']
        });
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

        Highcharts.chart('last10Graph', {
            chart: {
                backgroundColor: 'transparent',
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: 'pie'
            },
            title: {
                text: 'Priority Wise Lab Samples',
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
                        enabled: false,
                        format: '<i>{point.name}<br>(<label>Percentage</label>:{point.percentage:.0f}%)</i>',
                        style: {
                            textOutline: 'none',
                            textDecoration: 'none'
                        },
                        connectorColor: 'black'
                    },
                    showInLegend: true
                }
            },
            series: [{
                    name: 'Total_Samples',
                    data: priorityData
                }]
            ,
            drilldown: {
                "series": DDoutput
            }
        });
    }

    function GraphByIndustry(output, DDOutput) {
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

        Highcharts.chart('upcomingGraph', {
            chart: {
                backgroundColor: 'transparent',
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: 'pie'
            },
            title: {
                text: 'Lab Samples to be processed',
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
                        enabled: false,
                        format: '<i>{point.name}<br>(<label>Percentage</label>:{point.percentage:.0f}%)</i>',
                        color: 'black',
                        style: {
                            color: 'black', //(Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black',
                            font: '12px arial, monospace;',
                            textOutline: 'none',
                            textDecoration: 'none'
                        },
                        connectorColor: 'black',
                    },
                    showInLegend: true
                }
            },
            legend: {
                title: {
                    text: 'Industry<br/><span style="font-size: 9px; color: #666; font-weight: normal">(Click to hide)</span>',
                    style: {
                        fontStyle: 'italic'
                    }
                },
                layout: 'horizontal',
                align: 'center'
            },
            series: [{
                    name: 'Total_Samples',
                    data: output
                }],
            drilldown: {
                "series": DDOutput
            }
        });
    }
    function sliceAndDice1(data) {
        var arrNew = [];
        var uniques = _.uniqBy(data, function (e) {
            return e.mstInd.indName;
        });

        var seriesData = [];
        _.forEach(uniques, function (t) {
            var newObj = {
                "name": t.mstInd.indName,
                "id": t.mstInd.indName,
                "data": []
            };
            var inds = _.filter(data, function (m) {
                return m.mstInd.indName == t.mstInd.indName;
                
            });
            seriesData = _(inds)
                    .groupBy(function (v) {
                        return v.mstDept.mstCustomer.customerName;
                    })
                    .map((objs, key) => ({
                            'name': key,
                            'y': _.countBy(inds, function (v) {
                                return v.mstDept.mstCustomer.customerName == key;
                            }).true
                        }))
                    .value();
            var newSeriesData = [];
            for (var item in seriesData) {
                newSeriesData.push([seriesData[item].name, seriesData[item].y]);
            }
            console.log(newSeriesData);
            newObj.data = newSeriesData;
            arrNew.push(newObj);
        });
        return arrNew;
    }

    function sliceAndDice2(data) {
        var uniques = _.uniqBy(data, function (e) {
            return e.samplepriorityId;
        });
        var arrNew = [];
        _.forEach(uniques, function (v) {
            var samplepriorityName = v.samplepriorityName;
            var newObj = {
                "name": v.mstDept.mstCustomer.customerName,
                "id": v.samplepriorityName,
                "data": []
            };
            var arrr = [];
            _.forEach(data, function (a) {
                if (a.samplepriorityName == samplepriorityName) {
                    arrr.push([_.trimEnd(a.stringExptestresultDate, ' 00:00:00.0'), 1]);
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
            url: '/ServoCMP/Lab/redirectController?url=getUpcomingData',
            type: 'POST',
            dataType: 'JSON',
            success: function (data) {
                console.log(data);
                if (data.length > 0) {
                    var seriesData = _(data)
                            .groupBy(function (v) {
                                return v.mstInd.indName;
                            })
                            .map((objs, key) => ({
                                    'name': key,
                                    'y': _.countBy(data, function (v) {
                                        return v.mstInd.indName == key;
                                    }).true,
                                    'drilldown': key
                                }))
                            .value();

                    var drilldownData = sliceAndDice1(data);
                    console.log(seriesData);
                    console.log(drilldownData);
                    GraphByIndustry(seriesData, drilldownData);

                    $('body > div.modal-backdrop.in').remove();
                } else {
                    var seriesData = [
                        {"name": "No samples", "y": 1, "drilldown": null, "color": "grey"},
                    ];
                    var drilldownData = [];
//                            [{
//                                "name": "None Drawn",
//                                "id": "NA",
//                                "data": ["None available",ni]
//                            }];
                    GraphByIndustry(seriesData, drilldownData);

                    $('body > div.modal-backdrop.in').remove();
                }
            },
            error: function (error) {
                console.log(error);
            }
        });
        //Sample By Priority
        $.ajax({
            async: false,
            url: '/ServoCMP/Lab/redirectController?url=getLast10Data',
            type: 'POST',
            dataType: 'JSON',
            success: function (data) {
                console.log(data);

                var high = {
                    radialGradient: {cx: 0.5, cy: 0.5, r: 0.5},
                    stops: [
                        [0, '#FF9F72'],
                        [1, '#de6d38']
                    ]
                };
                var low = {
                    radialGradient: {cx: 0.5, cy: 0.5, r: 0.5},
                    stops: [
                        [0, '#6AF9C4'],
                        [1, '#3dcb97']
                    ]
                }
                var medium = {
                    radialGradient: {cx: 0.5, cy: 0.5, r: 0.5},
                    stops: [
                        [0, '#81BEFF'],
                        [1, '#4689d2']
                    ]
                }

                if (data.length > 0) {
                    var seriesData = _(data)
                            .groupBy('samplepriorityName')
                            .map((objs, key) => ({
                                    'name': key + ' Priority',
                                    'y': _.countBy(data, function (v) {
                                        return v.samplepriorityName == key;
                                    }).true,
                                    'drilldown': key,
                                    'color': (key == "Medium" ? medium : (key == "High" ? high : low))
                                }))
                            .value();
                    var drilldownData = sliceAndDice2(data);
//                  console.log(seriesData);
//                  console.log(drilldownData);
                    PendingLabSample(seriesData, drilldownData);
                } else {
                    var seriesData = [
                        {"name": "No samples", "y": 1, "drilldown": null, "color": "grey"},
                    ];
                    var drilldownData = [];
                    PendingLabSample(seriesData, drilldownData);

                }
            },
            error: function (error) {
                console.log(error);
            }
        });
    });</script>
<script>
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
                borderWidth: 0,
                label: {
                    connectorAllowed: false,
                    style: {'color': '#fff'}
                },
                dataLabels: {
                    enabled: true,
                    format: '{point.y:.1f}'
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
            enabled: false
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