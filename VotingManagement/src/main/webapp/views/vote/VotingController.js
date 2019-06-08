angular
    .module('VotingApp',  ['ui.bootstrap','VotingApp.commonService'])
    .controller('votingController', function ($scope, $uibModal, commonService) {

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
        s.voteType = s.voteTypes.ADD;    //编辑或新增 投票

        s.answerType = {
            one: {
                name: "单选",
            },
            more: {
                name: "多选",
            },
            text: {
                name: "文字",
                alert: "请输入答案"
            }
        };

        //问题
        s.questions = {
            title: "",
            describe: "",
            image: null,
            stopTime : "",
            answers: [],
        };

        //答案解构
        s.answer = {
            desText: "答案描述",
        }
        //问题构造器
        s.getAnswer = function (type) {
            function Answer() {
                this.desText = "";
                this.type = type || s.answerType.one;
                this.body = "";
            }
            return new Answer();
        }

        //增加答案行数
        s.addAnswerInput = function(type){
            var thisType = type || s.answerType.one;
            s.questions.answers.push( s.getAnswer(thisType));
        };

        //选项未填写,提示框
        s.tips = function (msg) {
            alert(msg);
        };

        s.toSaveVote = function () {
            console.log(s.questions);
            if (s.questions.title == null || s.questions.title == "") {
                s.tips("标题不能为空");
                return ;
            } else if (s.questions.answers.length < 2){
                s.tips("选项太少,请添加");
            }
            function createVote(title, content, status, stopTime) {
                return {
                    title : title,
                    content : content,
                    status : status,
                    stopTime : stopTime,
                }
            }
            var input = createVote(s.questions.title, s.questions, 'ACTIVE', s.end);
            $http({
                method : 'POST',
                url : 'http://localhost:8585/vote/create-new-vote',
                data : input,
            }).then(function (res) {

            },function (err) {
                console.log(err);
                alert("error!");
            })
        };


        // s.getUrl = function() {
        //     return $location.absUrl();
        // }
        // s.thisUrl = s.getUrl();


        s.openAlert = function (success, msg) {
            var model = $uibModal.open({
                animation: true,
                templateUrl: 'alertModel.html',
                controller: function ($scope, $uibModalInstance, data) {
                    var s = $scope;
                    s.data = data;
                    s.ok = function () {
                        $uibModalInstance.close("关闭");
                    };
                    s.cancel = function () {
                        $uibModalInstance.dismiss("cancel");
                    };
                    s.type = data.success == false ? '错误' : '欢迎';
                    s.msg = data.msg;

                },
                resolve : {
                    data : function () {
                        return {
                            msg : msg,
                            success : success,
                        };
                    }
                },
            });
            model.opened.then(function () {
                console.log("模态框已经打开");//后执行的函数
            });
            model.result.then(function (value) {

            }, function (reason) {

            });
        }


        //*****************初始化
        s.questions.answers.push(s.getAnswer());    //初始化question.answer

    });