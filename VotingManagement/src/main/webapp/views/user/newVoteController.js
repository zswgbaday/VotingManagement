angular
    .module('VotingApp', ['ui.bootstrap', 'VotingApp.commonService'])
    .controller('newVoteController', function ($scope, $uibModal, commonService) {

        var vm = this;
        var s = $scope;

        s.voteTypes = {
            EDIT: {
                name: "编辑",
            },
            "ADD": {
                name: "新增",
            }
        }

        s.answerType = {
            radio: {
                name: "单选",
                type: "radio"
            },
            multiple: {
                name: "多选",
                type: "multiple"
            },
            // text: {
            //     name: "文字",
            //     alert: "请输入答案"
            // }
        };

        //问题
        s.questions = {
            voteContent: {
                title: "",
                describe: "",
                image: null,
                type: "",
                options: [{
                    optionDescribe: ""
                }, {
                    optionDescribe: ""
                }]
            },
            stopTime: "",
            status: "ACTIVE",
        };


        //增加答案行数
        s.addAnswerInput = function () {
            function Answer() {
                this.optionDescribe = "";          //答案描述
            }

            s.questions.voteContent.options.push(new Answer());
        };
        //减少答案行数
        s.subAnswerInput = function () {
            s.questions.voteContent.options.pop();
        };

        //选项未填写,提示框
        s.tips = function (msg) {
            commonService._modalAlert(false, msg);
        };

        s.uploadImage = function () {
            var data = document.getElementById('voteImage').files[0];
            if (data) {
                var formData = new FormData();
                formData.append("photo", data);
                formData.append("id", '1b71e08160924ed8adab248a34eab737');
                commonService._upLoadImage('vote/upload-photo', formData, function (res) {
                    commonService._modalAlert(true, "创建投票成功", function () {
                    });
                }, function (err) {
                    commonService._modalAlert(false, "图片上传失败", function () {
                    });
                });
            }else {
                commonService_._modalAlert(false, "请选择图片");
            }
        };

        s.toSaveVote = function () {
            console.log(s.questions);
            if (!s.questions.voteContent.title) {
                s.tips("标题不能为空");
                return;
            } else if (s.questions.voteContent.options.length < 2) {
                s.tips("选项太少,请添加");
            }
            s.questions.stopTime = getStopTime();
            s.questions.status = 'ACTIVE';
            var input = s.questions;

            commonService._post('vote/create-new-vote', input, function (res) {
                
                    //上传图片
                    var data = document.getElementById('voteImage').files[0];
                    if (data) {
                        var formData = new FormData();
                        formData.append("photo", data);
                        formData.append("id", res.obj.id);
                        commonService._upLoadImage('vote/upload-photo', formData, function (ress) {
                            commonService._modalAlert(false, "创建投票成功", function () {
                                window.location.href = "/views/user/showVote.html?id=" + ress.obj.id;
                            });
                        }, function (errr) {
                            commonService._modalAlert(false, "图片上传失败", function () {
                                window.location.href = "/views/user/showVote.html?id=" + errr.obj.id;
                            });
                        });

                    } else {
                        commonService._modalAlert(res.success, res.msg, function () {
                            window.location.href = "/views/user/showVote.html?id=" + res.obj.id;
                        });
                    }
                },function (err) {
                commonService._modalAlert(res.success, res.msg);
                console.log(err);
            });
        };


        //*****************初始化

        //搞权限
        // commonService._get('vote/find-vote-by-user',{});

    });