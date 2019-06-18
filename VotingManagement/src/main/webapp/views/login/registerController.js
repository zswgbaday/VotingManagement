angular
    .module('VotingApp', ['ui.bootstrap', 'VotingApp.commonService'])
    .controller('registerController', function ($scope, $http, $uibModal, commonService) {

        var vm = this;
        var s = $scope;

        s.thisUrl = null;

        s.doRegister = function () {
            if (!s.user) {
                commonService._modalAlert(false, "页面异常");
                return ;
            }else if (!s.user.email) {
                commonService._modalAlert(false, "用户邮箱不能为空");
                return;
            }else if (!s.user.username) {
                commonService._modalAlert(false, "用户名不能为空");
                return;
            }else if (!s.user.password) {
                commonService._modalAlert(false, "密码不能为空");
                return;
            }else if (s.user.password !== s.user.repassword) {
                commonService._modalAlert(false, "请重新输入确认密码与密码");
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
                commonService._modalAlert(true, res.msg + "\n请登录", function () {
                    window.location.href = "/login";
                });
            }, function (err) {
                commonService._modalAlert(err.success, "注册失败，请联系管理员");
            });
        }
        
    });