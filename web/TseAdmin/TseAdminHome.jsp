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
                <div class="card-header card-header-rose" data-header-animation="true">
                    <div id="last10Graph" class="highcharts-container" style="height: 400px"></div>
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
                    <div id="noOfTanksGraph" class="highcharts-container" style="height: 400px"></div>
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
                    <div id="upcomingGraph" class="highcharts-container" style="height: 400px"></div>
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
                    <marquee direction="down" style="height:65%" scrollDelay="300" onmouseover="this.stop();" onmouseout="this.start();">
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
</script>
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
<script>
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
</script>