angular
    .module('VotingApp', ['ui.bootstrap', 'VotingApp.commonService'])
    .controller('homeController', function ($scope, $http, $uibModal, commonService) {

        var vm = this;
        var s = $scope;

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
        
        s.loginUser = {};
        s.voteArrays = [];
        
        //得到用户信息
        s.getUserInfo = function () {
            commonService._get('user/get-user-info', null, function (res) {
                s.loginUser = res.obj;
                //加载投票信息
                s.getVoteByUser();
            }, function (err) {
                // alert("页面加载失败，请验证用户身份后重试");
                commonService._modalAlert(false,err.msg);
                console.log(err);
            });
        }; 
        
        s.getVoteByUser = function () {
            var input = {
                id : s.loginUser.id
            }
            commonService._get('vote/find-votes-by-user', input, function (res) {
                s.voteArrays = res.obj;
            }, function (err) {
                console.log(err);
                alert(err.msg);
            })
        };
        
        s.deleteBtn = function (delId) {
            commonService._modalConfirm( "确认删除此投票吗？", function () {
                commonService._get('vote/delete-vote', {id : delId}, function (res) {
                    commonService._modalAlert(res.success, res.msg, function () {
                        window.location.reload();
                    });
                }, function (err) {
                    commonService._modalAlert(err.success, err.msg);
                })
            }, function () {
                //取消
            });
        };
        
        s.showBtn = function (showId) {
            window.location.href = '/views/user/showVote.html?id=' + showId;
        };
        
        s.updateBtn = function (updateId) {
            //模态框
            var model = $uibModal.open({
                animation: true,
                templateUrl: 'modalUpdateVote.html',
                controller: function ($scope, $uibModalInstance, $filter, data, commonService) {
                    var s = $scope;
                    s.updateId = data.updateId;
                    //加载数据
                    commonService._get('vote/find-vote-by-id',{id : updateId},
                        function (res) {
                            s.vote = res.obj;
                            //显示时间
                            setStopTime($filter('date')(s.vote.stopTime,'yyyy-MM-dd HH:mm:ss'));
                        }, function (err) {
                            commonService._modalAlert(false, err.msg);
                        });
                    s.ok = function () {
                        commonService._post(
                            'vote/update-vote',
                            {id: s.vote.id, status: s.vote.status, stopTime: getStopTime(), title: s.vote.voteContent.title },
                            function (res) {
                                commonService._modalAlert(res.success, res.msg, function () {
                                    window.location.reload();
                                });
                            },
                            function (err) {
                                console.log(err);
                                commonService._modalAlert(res.success, res.msg);
                            });
                        $uibModalInstance.close("ok");  //括号内是传递给回调函数的参数
                    };
                    s.cancel = function () {
                        $uibModalInstance.dismiss("cancel");
                    };
                },
                resolve: {
                    data: function () {
                        return {
                            updateId: updateId,
                        };
                    }
                },
            });
            model.opened.then(function () {
                console.log("模态框已经打开");//后执行的函数
            });
            model.result.then(function (value) {
                console.log("模态框-点击确认-回调函数" + value);
            }, function (reason) {
                console.log("模态框-点击取消-回调函数" + reason);
            });
        };


        //打开模态框
        s.openVoteModal = function (){
            
        };
        
        /***************************************************************/
        //页面加载初始化
        s.getUserInfo();
        

    });