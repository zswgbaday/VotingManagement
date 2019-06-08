angular
    .module('VotingApp', ['ui.bootstrap', 'VotingApp.commonService'])
    .controller('registerController', function ($scope, $http, $uibModal, commonService) {

        var vm = this;
        var s = $scope;

        s.thisUrl = null;

        s.doRegister = function () {
            if (!s.user) {
                s.openAlert(false, "页面异常");
                return ;
            }else if (!s.user.username) {
                s.openAlert(false, "用户名不能为空");
                return;
            }else if (!s.user.password) {
                s.openAlert(false, "密码不能为空");
                return;
            }else if (s.user.password !== s.user.repassword) {
                s.openAlert(false, "请重新输入确认密码与密码");
                return;
            }
            
            var input = {
                username : s.user.username,
                password : s.user.password,
                email : s.user.email,
                phone : s.user.phone,
                age : s.user.age,
                address : s.user.address,
            }
            commonService._post('register',input, function (res) {
                s.openAlert(res.success, res.msg + "\n请登录");
                window.location.href = "/login";
            }, function (err) {
                s.openAlert(res.success, "注册失败，请联系管理员");
            });
        }
        
        

        // s.getUrl = function() {
        //     return $location.absUrl();
        // }
        // s.thisUrl = s.getUrl();


        s.openAlert = function (success, msg, successFun) {
            var model = $uibModal.open({
                animation: true,
                templateUrl: '../common/html/alertModel.html',
                controller: function ($scope, $uibModalInstance, data) {
                    var s = $scope;
                    s.data = data;
                    s.ok = function () {
                        $uibModalInstance.close("ok");  //括号内是传递给回调函数的参数
                    };
                    s.cancel = function () {
                        $uibModalInstance.dismiss("cancel");
                    };
                    s.type = data.success == false ? '错误' : '欢迎';
                    s.msg = data.msg;
                },
                resolve: {
                    data: function () {
                        return {
                            msg: msg,
                            success: success,
                        };
                    }
                },
            });
            model.opened.then(function () {
                console.log("模态框已经打开");//后执行的函数
            });
            model.result.then(function (value) {
                console.log("模态框-点击确认-回调函数" + value);
                successFun();
            }, function (reason) {
                console.log("模态框-点击取消-回调函数" + reason);
            });
        }

    });