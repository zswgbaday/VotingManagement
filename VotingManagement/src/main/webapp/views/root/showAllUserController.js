angular
    .module('VotingApp', ['ui.bootstrap', 'VotingApp.commonService'])
    .controller('showAllUserController', function ($scope, $http, $uibModal, commonService) {

        var vm = this;
        var s = $scope;

       
        s.loginUser = {};
        s.userArrays = [];
        s.keyWords = {
            email: "",
            name: ""
        };
        
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
        
        s.getAllUser = function (page) {
            var input = {
                page: page || 1,
                size: 10,
                name: s.keyWords.name,
                email: s.keyWords.name
                // email: s.keyWords.email
            };
            commonService._post('user/select-user', input, function (res) {
                s.userArrays = res.obj;
            }, function (err) {
                console.log(err);
                commonService._modalAlert(false, err.msg);
            })
        };
        
        s.deleteBtn = function (delId) {
            commonService._modalConfirm( "确认删除此用户吗？", function () {
                commonService._get('user/delete-user/', {id : delId}, function (res) {
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
            //模态框
            var model = $uibModal.open({
                animation: true,
                templateUrl: 'showAllUser/showUser.html',
                controller: function ($scope, $uibModalInstance, $filter, data, commonService) {
                    var s = $scope;
                    s.showId = data.showId;
                    //加载数据
                    commonService._get('user/find-user-by-id',{id : showId},
                        function (res) {
                            s.user = res.obj;
                        }, function (err) {
                            commonService._modalAlert(false, err.msg);
                        });
                    
                    s.ok = function () {
                        $uibModalInstance.dismiss("cancel");
                    };
                },
                resolve: {
                    data: function () {
                        return {
                            showId: showId,
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


        s.downUser = function (){
            window.open('/user/download','下载','_blank');
        };
        
        /***************************************************************/
        //页面加载初始化
        s.getUserInfo();
        s.getAllUser();
        

    });