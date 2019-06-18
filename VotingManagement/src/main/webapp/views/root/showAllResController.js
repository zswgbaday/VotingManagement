angular
    .module('VotingApp', ['ui.bootstrap', 'VotingApp.commonService'])
    .controller('showAllResController', function ($scope, $http, $uibModal, commonService) {

        var vm = this;
        var s = $scope;

        s.resStatus = {
            ACTIVE : {
                name : "启用",
                type: "ACTIVE"
            },
            DISABLE : {
                name : "停用",
                type: "DISABLE"
            }
        };
        
        s.search = {
            keyWord: ""
        };
        
        s.loginUser = {};
        s.resArrays = [];
        
        //得到用户信息
        s.getUserInfo = function () {
            commonService._get('user/get-user-info', null, function (res) {
                s.loginUser = res.obj;
                //加载投票信息
            }, function (err) {
                // alert("页面加载失败，请验证用户身份后重试");
                commonService._modalAlert(false,err.msg);
                console.log(err);
            });
        }; 
        
        s.getAllRes = function (page) {
            var input = {
                keyWord: s.search.keyWord || ""
            };
            commonService._get('res/find-res', input, function (res) {
                s.resArrays = res.obj;
            }, function (err) {
                console.log(err);
                alert(err.msg);
            })
        };
        
        s.deleteBtn = function (delId) {
            commonService._modalConfirm( "确认删除此资源吗？", function () {
                commonService._get('res/remove-res', {id : delId}, function (res) {
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
        
        s.createBtn = function() {
            //模态框
            var model = $uibModal.open({
                animation: true,
                templateUrl: 'showAllRes/showRes.html',
                controller: function ($scope, $uibModalInstance, $filter, data, commonService) {
                    var s = $scope;
                    s.res = {
                        id: null,
                        name: null,
                        status: 'ACTIVE',
                        icon: null,
                        url: null
                    };
                    s.add = true;
                    s.resStatus = data.resStatus;
                    s.ok = function () {
                        commonService._post(
                            'res/create-new-res',
                            {
                                id: null,
                                name: s.res.name,
                                status: s.res.status,
                                url : s.res.url,
                                icon : s.res.icon
                            },
                            function (res) {
                                commonService._modalAlert(res.success, res.msg, function () {
                                    window.location.reload();
                                });
                            },
                            function (err) {
                                console.log(err);
                                commonService._modalAlert(err.success, err.msg);
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
                            resStatus: s.resStatus
                        };
                    }
                }
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
        
        s.updateBtn = function (updateId) {
            //模态框
            var model = $uibModal.open({
                animation: true,
                templateUrl: 'showAllRes/showRes.html',
                controller: function ($scope, $uibModalInstance, $filter, data, commonService) {
                    var s = $scope;
                    s.updateId = data.updateId;
                    s.add = false;
                    s.resStatus = data.resStatus;
                    //加载数据
                    commonService._get('res/find-res-by-id',{id : updateId},
                        function (res) {
                            s.res = res.obj;  
                        }, function (err) {
                            commonService._modalAlert(false, err.msg);
                        });
                    s.ok = function () {
                        commonService._post(
                            'res/update-res',
                            {
                                id: s.res.id, 
                                name: s.res.name,
                                status: s.res.status,
                                url : s.res.url,
                                icon : s.res.icon
                            },
                            function (res) {
                                commonService._modalAlert(res.success, res.msg, function () {
                                    window.location.reload();
                                });
                            },
                            function (err) {
                                console.log(err);
                                commonService._modalAlert(err.success, err.msg);
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
                            resStatus: s.resStatus,
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
        s.downLoad = function (){
            window.open('/res/download');
        };
        
        /***************************************************************/
        //页面加载初始化
        s.getUserInfo();
        s.getAllRes();
        

    });