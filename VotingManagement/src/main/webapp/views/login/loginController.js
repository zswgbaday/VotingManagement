angular
    .module('VotingApp', ['ui.bootstrap', 'VotingApp.commonService'])
    .controller('loginController', function ($scope, $http, $location, $uibModal, commonService) {

        var vm = this;
        var s = $scope;

        s.thisUrl = null;

        s.doLogin = function (_username, _password) {
            var username = _username ? _username : s.username;
            var password = _password ? _password : s.password;
            if (username == null || password == null) {
                return;
            }
            commonService._post('login',
                {
                    loginEmail: username,
                    password: password  
                },
                function (res) {
                    var uri = res.uri;
                    commonService._modalAlert(res.success, res.msg, function () {
                        window.location.href = "/views/user/home.html";
                        // $location.path("/views/user/home.html");
                    });
                },
                function (err) {
                    console.log(err);
                    commonService._modalAlert(err.success, err.msg);
                });
        }

        // s.getUrl = function() {
        //     return $location.absUrl();
        // }
        // s.thisUrl = s.getUrl();
        
        s.openAlert = function () {
            // commonService._modalAlert(false, "测试");
            commonService._get('user/user-info-download');
        };
        
        s.checkLogin = function () {
            commonService._get('user/get-user-info',{}, function (res) {
                if (res.success = true) {
                    window.location.href = "/views/user/home.html";
                }
            }, function (err) {
                
            });
        };
        
        /***************初始化操作************/
        // s.checkLogin();
    });