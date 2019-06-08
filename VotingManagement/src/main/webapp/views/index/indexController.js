angular
    .module('VotingApp', ['ui.bootstrap', 'VotingApp.commonService', 'infinite-scroll'])
    .controller('indexController', function ($scope, $http, $uibModal, commonService) {

        var vm = this;
        var s = $scope;
        
        s.voteArrays = [];
        s.search = {
            defaultSize : 9,
            defaultThisPage : 1,    //请求参数
            isLoad : true,          //  是否还有数据的标志
            isBusy : false,
            keyword : ""
        };
        
        //查询
        s.searchKeyWord = function() {
            s.voteArrays = [];
            s.search.isLoad = true;
            s.search.defaultThisPage = 1;
            s.searchVote();
        };

        //获取随机数
        s.getRandom = function (value) {
            var base = 9 | value;
            return Math.ceil(Math.random()* base);
        };
        
        //获取随机的图片高度
         function getRandomImageHeight (param) {
             parma = param || 1;
            var initImageHeight = [200, 299, 214, 333];
            var index = Math.ceil(Math.random()* initImageHeight.length);
            var height ;
            if (param % 2 == 0) {
                height = initImageHeight[index - 1];
            } else {
                height = initImageHeight[initImageHeight.length - index + 1];
            }
            return height;
        };
        
        //加载投票项
        s.searchVote = function (page, size) {
            // if (s.isBusy) return;
            // s.isBusy = true;
            if (!s.search.isLoad) return;
            
            var sPage = page || s.search.defaultThisPage;
            var sSize = size || s.search.defaultSize;
            s.search.defaultThisPage += 1;
            var input = {
                page : sPage,
                size : sSize,
                keyWord : s.search.keyword
            };
            commonService._post('/vote/find-votes-page', input, function (res) {
                if (res.obj.length < sSize) {
                    s.search.isLoad = false;
                } 
                for (var i in res.obj) {
                    var tmpObj = res.obj[i].voteContent;
                    // tmpObj.liWidth = getRandomImageHeight(thisPage); //图片高度
                    tmpObj.liWidth = 225; //图片高度
                    s.voteArrays.push(tmpObj);
                }
            }.bind(this), 
            function (err) {
                commonService._modalAlert(err.success, msg);
            }.bind(this));
        };

        //页面跳转
        s.localHref = function (url) {
            window.location.href = url;
        }; 
        
        /***初始化***********测试********/
    
    })