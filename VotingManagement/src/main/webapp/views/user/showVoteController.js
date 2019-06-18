angular
    .module('VotingApp', ['ui.bootstrap', 'VotingApp.commonService'])
    .controller('showVoteController', function ($scope, $uibModal, commonService) {

        var vm = this;
        var s = $scope;
        
        //初始化echarts实例
        var myChart = echarts.init(document.getElementById('e-main'));
        
        //获取url中的投票id
        s.urlVoteId = '';

        s.vote = null;
        
        s.initFun = function () {
            var urlParam = window.location.search.substr(1);
            s.urlVoteId = urlParam.substr(urlParam.indexOf('=') + 1);
            s.findVoteById(s.urlVoteId );
        }
        
        //像后台请求获取vote
        s.findVoteById = function (param) {
            var id = param || s.urlVoteId;
            commonService._get('vote/find-vote-by-id',{id: id}, function (res) {
               s.vote  = res.obj;
               console.log(res);
               //显示echart
               s.changeChat(true);
            }, function (err) {
                alert("页面初始化失败，未能加载投票信息。");
                console.log(err);
            });
        };


        //饼状图 显示vote
        function pieCharts() {
            function getButtomData() {
                var buttomOptArr = [];
                for (var opt in s.vote.voteContent.options) {
                    buttomOptArr.push(s.vote.voteContent.options[opt].optionDescribe);
                }
                return buttomOptArr;
            }

            function getData() {
                var optArr = [];
                for (var opt in s.vote.voteContent.options) {
                    optArr.push({
                        value: s.vote.voteContent.options[opt].count,
                        name: s.vote.voteContent.options[opt].optionDescribe
                    });
                }
                return optArr;
            }

            var pieOption = {
                title: {
                    text: s.vote.voteContent.title,
                    subtext: s.vote.voteContent.title,
                    left: 'center'
                },
                tooltip: {
                    trigger: 'item',
                    // formatter: "{a} <br/>{b} : {c} ({d}%)"
                    formatter: "{b} : {d}% <br/>数量 : {c} "
                },
                legend: {
                    orient: 'vertical',
                    top: 'top',
                    bottom: 10,
                    left: 'left',
                    //底部显示
                    data: getButtomData()
                },
                series: [
                    {
                        type: 'pie',
                        // roseType: 'angle',
                        radius: '65%',
                        center: ['50%', '50%'],
                        selectedMode: 'single',
                        data: getData(),
                        itemStyle: {
                            emphasis: {
                                shadowBlur: 30,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ]
            };

            //使用制定的配置项和数据显示图表
            myChart.setOption(pieOption, true);
        }

        //柱状图显示vote
        function barCharts() {
            var dataSource = [].concat(s.vote.voteContent.options);
            var dataAxis = []; //'选项1', '选项1', '选项1', '选项1', '选项1', '选项6'
            var data = [];
            var yMax = 1;   
            var dataShadow = [];
            //排序数据
            dataSource.sort(function (a, b) { return b.count - a.count });
            //初始化显示的数据
            for (var i = 0; i < dataSource.length; i++) {
                data.push(dataSource[i].count);
                dataAxis.push(dataSource[i].optionDescribe);
                yMax = yMax < dataSource[i].count ? dataSource[i].count : yMax;
            }
            //初始化阴影部分
            yMax = Math.ceil(yMax * 1.2);
            for (var i = 0; i < dataSource.length; i++) {
                dataShadow.push(yMax);
            }

            var barOption = {
                title: {
                    text: s.vote.voteContent.title,
                    subtext: s.vote.voteContent.title
                },
                tooltip: {
                    item: 'axis',
                    triggerOn:"mousemove",
                    showDelay:0,
                    formatter: "{b} <br/> 数量: {c} "
                },
                xAxis: {
                    data: dataAxis,
                    axisLabel: {
                        inside: true,
                        textStyle: {
                            color: '#000'
                        }
                    },
                    axisTick: {
                        show: true
                    },
                    axisLine: {
                        show: true
                    },
                    z: 10
                },
                yAxis: {
                    axisLine: {
                        show: true
                    },
                    axisTick: {
                        show: true
                    },
                    axisLabel: {
                        textStyle: {
                            color: '#999'
                        }
                    }
                },
                dataZoom: [
                    {
                        type: 'inside'
                    }
                ],
                series: [
                    { // For shadow
                        type: 'bar',
                        itemStyle: {
                            normal: {color: 'rgba(0,0,0,0.05)'}
                        },
                        barGap: '-100%',
                        barCategoryGap: '40%',
                        data: dataShadow,
                        animation: true
                    },
                    {
                        type: 'bar',
                        itemStyle: {
                            normal: {
                                color: new echarts.graphic.LinearGradient(
                                    0, 0, 0, 1,
                                    [
                                        {offset: 0, color: '#83bff6'},
                                        {offset: 0.5, color: '#188df0'},
                                        {offset: 1, color: '#188df0'}
                                    ]
                                )
                            },
                            emphasis: {
                                color: new echarts.graphic.LinearGradient(
                                    0, 0, 0, 1,
                                    [
                                        {offset: 0, color: '#2378f7'},
                                        {offset: 0.7, color: '#2378f7'},
                                        {offset: 1, color: '#83bff6'}
                                    ]
                                )
                            }
                        },
                        data: data
                    }
                ]
            };

            // Enable data zoom when user click bar.
            var zoomSize = 6;
            myChart.on('click', function (params) {
                console.log(dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)]);
                myChart.dispatchAction({
                    type: 'dataZoom',
                    startValue: dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)],
                    endValue: dataAxis[Math.min(params.dataIndex + zoomSize / 2, data.length - 1)]
                });
            });

            //使用制定的配置项和数据显示图表
                myChart.setOption(barOption, true);
        }

        s.showType = true;
        s.changeChat = function (isPie) {
            //设置显示饼图或者柱图
            if (isPie) {
                s.showType = true;
            }
            
            if (s.showType) {
                s.showType = false;
                pieCharts();
            } else {
                s.showType = true;
                barCharts();
            }
        };
        
        s.shareVote = function () {
            var msg = "请问复制连接后分享\n" + 'http://localhost:8585/views/index/single-page.html' + location.search;
            // input.value = "hello world"
            // input.select()
            // document.execCommand("copy")
          commonService._modalAlert(true, msg);
        };

        /********************************************/
        s.initFun()

    });
   