function commonService($http, $uibModal) {
    return {
        
        
        //post请求
        _post: function (uri, input, success, error) {
            $http({
                method: 'POST',
                url: 'http://localhost:8585/' + uri,
                data: input,
            }).then(function (res) {
                if (res.data.success == true) {
                    success(res.data);
                } else {
                    error(res.data);
                }
            }, function (err) {
                console.log(err);
                if (status == 401) {
                    var msg = data ? data.msg ? data.msg : '您没有权限' : '您没有权限';
                    // this._modalAlert(false, msg, function () {
                    //     window.location.href = '/';
                    // } );
                    alert(msg);
                    window.location.href = '/';
                }else {
                    alert("error!");
                }
            })
        },

        //get请求
        _get: function (uri, param, successrb, errorrb) {
            var url = 'http://localhost:8585/' + uri;
            if (param && JSON.stringify(param) != "{}") {
                url += '?';
                for (var i in param) {
                    url += i + '=' + param[i] + '&';
                }
                url = url.substr(0, url.length - 1);
            }

            $http.get(url).success(function (data, status, headers, config) {
                if (data.success == true) {
                    successrb(data);
                } else {
                    errorrb(data);
                }
            }).error(function (data, status, headers, config) {
                console.log(data);
                if (status == 401) {
                    var msg = data ? data.msg ? data.msg : '您没有权限' : '您没有权限';
                    // this._modalAlert(false, msg, function () {
                    //     window.location.href = '/';
                    // } );
                    alert(msg);
                    window.location.href = '/';
                }else {
                    alert("error!");
                }
            });
        },
        
        //图片
        _upLoadImage: function (uri, data, succ, error) {
            $http({
                method: 'POST',
                url: 'http://localhost:8585/' + uri,
                data: data,
                headers: {'Content-Type': undefined},
                // headers: { 'content-type' : 'application/x-www-form-urlencoded'},
                transformRequest: angular.identity
            }).success(function(res) {
                if (res.success == true) {
                    succ();
                }
                if(res.success == false){
                    error();
                }
            }).error(function (err) {
                console.log(err);
                alert("error!");
            });
        },

        //模态框--提示框
        _modalAlert: function (success, msg, successFun) {
            var model = $uibModal.open({
                animation: true,
                templateUrl: '/views/common/html/alertModel.html',
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
                if (successFun  instanceof Function) {
                    successFun();
                }
            }, function (reason) {
                console.log("模态框-点击取消-回调函数" + reason);
            });
        },


        //模态框--确认框
        _modalConfirm: function (msg, okFn, cannelFn) {
            var model = $uibModal.open({
                animation: true,
                templateUrl: '/views/common/html/alertConfirm.html',
                controller: function ($scope, $uibModalInstance, data) {
                    var s = $scope;
                    s.data = data;
                    s.ok = function () {
                        $uibModalInstance.close("ok");  //括号内是传递给回调函数的参数
                    };
                    s.cancel = function () {
                        $uibModalInstance.dismiss("cancel");
                    };
                    s.msg = data.msg;
                },
                resolve: {
                    data: function () {
                        return {
                            msg: msg,
                        };
                    }
                },
            });
            model.opened.then(function () {
                console.log("模态框已经打开");//后执行的函数
            });
            model.result.then(function (value) {
                console.log("模态框-点击确认-回调函数" + value);
                if (okFn instanceof Function) {
                    okFn();
                }
            }, function (reason) {
                console.log("模态框-点击取消-回调函数" + reason);
                if (cannelFn  instanceof Function) {
                    cannelFn();
                }
            });
        },
    }


};


(function () {
    var commonJs = {
        $get: ['$http', '$uibModal', function ($http, $uibModal) {
            return new commonService($http,  $uibModal);
        }],
    };
    // angular.module('VotingApp.commonService', []).provider('commonService', commonJs);

    /***************************************第二部分***/
    angular.module('VotingApp.commonService', [])
        .config(function($httpProvider) {
            //设置公用属性
            return $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
        }).provider('commonService', commonJs);
}());




