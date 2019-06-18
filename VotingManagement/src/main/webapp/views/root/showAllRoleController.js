angular
    .module('VotingApp', ['ui.bootstrap', 'VotingApp.commonService'])
    .controller('showAllRoleController', function ($scope, $http, $uibModal, commonService) {

        var vm = this;
        var s = $scope;

        
        
        s.loginUser = {};
        s.roleArrays = [];
        s.search = {
            keyWord : ""
        };
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
        
        //得到用户信息
        s.getUserInfo = function () {
            commonService._get('user/get-user-info', null, function (res) {
                s.loginUser = res.obj;
            }, function (err) {
                // alert("页面加载失败，请验证用户身份后重试");
                commonService._modalAlert(false,err.msg);
                console.log(err);
            });
        }; 
        
        s.getAllRole = function (page) {
            var input = {
                page: page || 1,
                size: 10,
                keyWord: s.search.keyWord || ""
            };
            commonService._get('role/find-all-role', input, function (res) {
                s.roleArrays = res.obj;
            }, function (err) {
                console.log(err);
                alert(err.msg);
            })
        };
        
        s.deleteBtn = function (delId) {
            commonService._modalConfirm( "确认删除此角色吗？", function () {
                commonService._get('role/remove-role-by-id', {id : delId}, function (res) {
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
        
        s.editResBtn = function (roleId) {
            //模态框
            var model = $uibModal.open({
                animation: true,
                templateUrl: 'showAllRole/editRoleRes.html',
                controller: function ($scope, $uibModalInstance, $filter, data, commonService) {
                    var s = $scope;
                    s.resArrays = [];
                    s.role = {
                        id : data.roleId,
                        name : "",
                        icon : ""
                    };
                    s.resStatus = data.resStatus;

                    //加载数据
                    commonService._get('role/find-role-by-id',{id : roleId},
                        function (res) {
                            s.role = res.obj;
                            getRes();
                        }, function (err) {
                            commonService._modalAlert(false, err.msg);
                        });
                    
                    function getRes () {
                        commonService._get('res/find-all-res', {page: 1, size: 9999},
                            function (res) {
                                s.resArrays = res.obj;
                                initSelected();
                            }, function (err) {
                                commonService._modalAlert(false, err.msg);
                            });
                    }
                    
                    function initSelected () {
                        s.resArrays.map(function (value, index, array) { 
                            s.role.resources.map(function (v, i, arr) {
                                if (v.id == value.id) {
                                    value.selected = true;
                                    return;
                                }
                            })
                        });
                    }
                    s.ok = function () {
                        function getUpdateInput () {
                            var roleId = s.role.id;
                            var resIds = [];
                            s.resArrays.forEach(function (value, index, array) { 
                                if (value.selected) {
                                    resIds.push(value.id);
                                }
                            });
                            return {roleId : roleId, resIds: resIds};
                        }
                        //更新资源
                        commonService._post(
                            'role/update-role-res',
                            getUpdateInput(),
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
                            roleId: roleId,
                            resStatus : s.resStatus
                        };
                    }
                }
            });
        };
        
        s.updateBtn = function (updateId) {
            //模态框
            var model = $uibModal.open({
                animation: true,
                templateUrl: 'showAllRole/showRole.html',
                controller: function ($scope, $uibModalInstance, $filter, data, commonService) {
                    var s = $scope;
                    s.add = true;
                    s.updateId = data.updateId;
                    //加载数据
                    commonService._get('role/find-role-by-id',{id : s.updateId},
                        function (res) {
                            s.role = res.obj;
                        }, function (err) {
                            commonService._modalAlert(false, err.msg);
                        });
                    s.ok = function () {
                        commonService._post(
                            'role/update-role',
                            s.role,
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

        s.createBtn = function (){
            //模态框
            var model = $uibModal.open({
                animation: true,
                templateUrl: 'showAllRole/showRole.html',
                controller: function ($scope, $uibModalInstance, $filter, data, commonService) {
                    var s = $scope;
                    s.add = true;
                    s.role = {
                      id : null,
                      name : "",
                      icon : ""  
                    };
                    
                    s.ok = function () {
                        commonService._post(
                            'role/create-new-role',
                            s.role,
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
                        };
                    }
                }
            });
        };

        s.downLoad = function (){
            window.open('/role/download');
        };
        
        /***************************************************************/
        //页面加载初始化
        s.getUserInfo();
        s.getAllRole();

    });