angular
    .module('VotingApp', ['ui.bootstrap', 'VotingApp.commonService'])
    .controller('singleController', function ($scope, $http, commonService) {

        var vm = this;
        var s = $scope;

        //获取url中的投票id
        s.urlVoteId = '';
        //投票项
        s.vote = null;

        s.answerType = {
            radio: {
                name: "单选",
                type : "radio"
            },
            multiple: {
                name: "多选",
                type : "multiple"
            },
            // text: {
            //     name: "文字",
            //     alert: "请输入答案"
            // }
        };
        
        /************Fun********/
        s.initFun = function () {
            var urlParam = window.location.search.substr(1);
            s.urlVoteId = urlParam.substr(urlParam.indexOf('=') + 1);
            s.findVoteById(s.urlVoteId );
        };

        //像后台请求获取vote
        s.findVoteById = function (param) {
            var id = param || s.urlVoteId;
            commonService._get('vote/find-vote-by-id',{id: id}, function (res) {
                s.vote  = res.obj;
                console.log(res);
                //显示echart
                // s.changeChat(true);
            }, function (err) {
                alert("页面初始化失败，未能加载投票信息。");
                console.log(err);
            });
        };
        
        //投票
        s.submitVoteResult = function() {
            //统计投票结果
            var input = {voteId: s.vote.id, result: []};
            for (var i in s.vote.voteContent.options) {
                if (s.vote.voteContent.options[i].isSelected ) {
                    input.result.push(i);
                } 
            }
            if (input.result.length < 1) {
                commonService._modalAlert(false, "请选择一个投票项");
                return;
            }
            //发送给
            commonService._post('vote/do-vote', input, function (res) {
                commonService._modalAlert(true, res.msg, function () {
                    window.location.href = "/views/vote/showVote.html?id=" + res.obj.id;
                });
            }, function (err) {
                console.log(err);
                commonService._modalAlert(false, err.msg);
            });
        };
        
       
        
       
        
        
        /***初始化***********测试********/
        s.initFun()
    });