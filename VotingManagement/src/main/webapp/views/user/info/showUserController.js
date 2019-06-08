angular
    .module('VotingApp', ['ui.bootstrap', 'VotingApp.commonService'])
    .controller('showUserController', function ($scope, $uibModal, commonService) {

        var vm = this;
        var s = $scope;
        
        s.user = null;
        
        /****************FUN******************************/
        s.initFun = function () {
            s.findUser();
        }
        
        //像后台请求获取user
        s.findUser = function () {
            commonService._get('user/find-user',{}, function (res) {
               s.user  = res.obj;
            }, function (err) {
                commonService._modalAlert(false, err.msg);
                console.log(err);
            });
        };
        
        //更新用户
        s.toUpdateUserInfo = function () {
          var input = angular.copy(s.user);
          commonService._post('user/update-user', input, function (res) {
              commonService._modalAlert(true, "更新用户成功", function () {
                  window.location.href = "/views/user/info/showUser.html";
              });
          }, function (err) {
              console.log(err);
              commonService._modalAlert(false, err.msg);
          });
        };
        
        //更新密码模态框
        //模态框--确认框
        s.toUpdatePwd = function () {
            
            var model = $uibModal.open({
                animation: true,
                templateUrl: '/views/user/info/modalUpdatePwd.html',
                controller: function ($scope, $uibModalInstance, commonService, data) {
                    var s = $scope;
                    s.edit = {
                        oldPwd : "",
                        newPwd : ""
                    };
                    s.ok = function () {
                        var input = angular.copy(data.user);
                        input.oldPassword = s.edit.oldPwd;
                        input.newPassword = s.edit.newPwd;
                        commonService._post('user/update-user', input, function (res) {
                            commonService._modalAlert(true, "更新密码成功", function () {
                                window.location.href = "/logout";
                            });
                        }, function (err) {
                            console.log(err);
                            commonService._modalAlert(false, err.msg);
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
                            user: s.user,
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
        
        //上传头像
        s.uploadImage = function () {
            var data = document.getElementById('voteImage').files[0];
            if (data) {
                var formData = new FormData();
                formData.append("photo", data);
                commonService._upLoadImage('user/upload-photo', formData, function (res) {
                    commonService._modalAlert(true, "上传头像成功", function () {
                        window.location.reload();
                    });
                }, function (err) {
                    commonService._modalAlert(false, "上传头像失败", function () {
                    });
                });
            }else {
                commonService._modalAlert(false, "请选择图片");
            }
        };


       
        /***************Init*****************************/
        s.initFun()

    });
   