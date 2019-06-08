var util = {
    autoResize : function() {
        $('.content').height(($(window).height() - 10) + 'px');
        $('.chart-height-full').height(($(window).height() - 149) + 'px');
        $('.chart-height-body').height(($(window).height() - 340) + 'px');
        $('.panel-height-full').height(($(window).height() - 147) + 'px');
        $('.block-height-full').height(($(window).height() - 240) + 'px');
        $('.block-height-half').height($(window).height()/2 + 'px');
        $('.block-article-height').height(($(window).height()/2 -36 -60) + 'px');
        $('.block-event-height').height(($(window).height()/2 -75) + 'px');
        $('.highchart-height').height($(window).width()/2 + 'px');
        $('.full-window-height').height($(window).height() + 'px');
        $(window).resize(function() {
            $('.content').height(($(window).height() - 10) + 'px');
            $('.chart-height-full').height(($(window).height() - 149) + 'px');
            $('.chart-height-body').height(($(window).height() - 340) + 'px');
            $('.panel-height-full').height(($(window).height() - 147) + 'px');
            $('.block-height-full').height(($(window).height() - 240) + 'px');
            $('.block-height-half').height($(window).height()/2 + 'px');
            $('.block-article-height').height(($(window).height()/2 -36 - 60) + 'px');
            $('.block-event-height').height(($(window).height()/2 -75) + 'px');
            $('.highchart-height').height($(window).width()/2 + 'px');
            $('.full-window-height').height($(window).height() + 'px');
        })
    },
    replaceAll : function(source, replaceWhat, replaceTo){
        replaceWhat = replaceWhat.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&');
        var re = new RegExp(replaceWhat, 'g');
        var result = source.replace(re,replaceTo);
        return result;
    },
    getDateDiff : function(startDate, endDate) {
        var startTime = new Date(Date.parse(startDate.replace(/-/g,   "/"))).getTime();
        var endTime = new Date(Date.parse(endDate.replace(/-/g,   "/"))).getTime();
        var dates = Math.abs((startTime - endTime))/(1000*60*60*24);
        return  dates;
    },
    initDateRangePicker : function (s) {
        //定义locale汉化插件
        var locale = {
            "format": 'YYYY-MM-DD',
            "separator": " -222 ",
            "applyLabel": "确定",
            "cancelLabel": "取消",
            "fromLabel": "起始时间",
            "toLabel": "结束时间'",
            "customRangeLabel": "自定义",
            "weekLabel": "W",
            "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
            "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
            "firstDay": 1
        };
        //初始化显示当前时间
        if(s.start&&s.end){
            $('#daterange-btn span').html(s.start + ' - ' + s.end);
        }else{
            $('#daterange-btn span').html(moment().subtract('days', s.initStart).format('YYYY-MM-DD') + ' - ' + moment().format('YYYY-MM-DD'));
            s.start = moment().subtract('days',s.initStart|| 1).format('YYYY-MM-DD');
            s.end = moment().format('YYYY-MM-DD');
        }
       
        //日期控件初始化
        $('#daterange-btn').daterangepicker(
            {
                'locale': locale,
                //汉化按钮部分
                ranges: {
                    // '今日': [moment(), moment()],
                    // '昨日': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                    '最近1日': [moment().subtract(1, 'days'), moment(), moment()],
                    '最近7日': [moment().subtract(6, 'days'), moment()],
                    '最近30日': [moment().subtract(29, 'days'), moment()],
                    '本月': [moment().startOf('month'), moment().endOf('month')],
                    '上月': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
                    '上半年': [moment().startOf('year'), moment().startOf('year').add(5, 'month').endOf('month')],
                    '今年': [moment().startOf('year'), moment().endOf('year')]
                },
                // startDate: moment().subtract(29, 'days'),
                startDate: s.initStart || moment().subtract(1, 'days'),
                endDate: s.initEnd || moment(),
                'alwaysShowCalendars': true,
                'showCustomRangeLabel': false
            },
            function (start, end) {
                $('#daterange-btn span').html(start.format('YYYY-MM-DD') + ' - ' + end.format('YYYY-MM-DD'));
                s.start = start.format('YYYY-MM-DD');
                s.end = end.format('YYYY-MM-DD');
            }
        );
    },

    prpScroll: function (doc) {
        var waitingPanel = doc.getElementById('waitingPanel');
        var wrap = doc.getElementById('dragList');
        var label = doc.getElementById('pullLabel');

        var isTouchPad = (/hp-tablet/gi).test(navigator.appVersion),
            hasTouch = 'ontouchstart' in window && !isTouchPad,
            START_EV = hasTouch ? 'touchstart' : 'mousedown',
            MOVE_EV = hasTouch ? 'touchmove' : 'mousemove',
            END_EV = hasTouch ? 'touchend' : 'mouseup',
            CANCEL_EV = hasTouch ? 'touchcancel' : 'mouseup';

        var qScroll = function (callBack, obj) {
            var that = this;
            that.callBack = callBack;
            that.obj = obj;
            that._bind(START_EV, wrap);

        };
        qScroll.prototype = {
            handleEvent: function (e) {
                var that = this;
                switch (e.type) {
                    case START_EV:
                        if (!hasTouch && e.button !== 0) return;
                        that._start(e);
                        break;
                    case MOVE_EV:
                        that._move(e);
                        break;
                    case END_EV:
                    case CANCEL_EV:
                        that._end(e);
                        break;
                }
            },
            _start: function (e) {
                var self = this, point = hasTouch ? e.touches[0] : e;
                //记录刚刚开始按下的时间
                self.startTime = new Date() * 1;
                //记录手指按下的坐标
                self.startY = point.pageY;
                //记录手指按下时容器顶端的坐标
                self.startTop = wrap.getBoundingClientRect().top;
                //记录手指按下时窗口高度
                self.iWindowHeight = util.getWindowHeight();
                //记录手指按下时文档总高度
                self.iScrollHeight = util.getScrollHeight();
                self.actionDir = '';
                self._bind(MOVE_EV, window);
                self._bind(END_EV, window);
                self._bind(CANCEL_EV, window);
            },
            _move: function (e) {
                var self = this, point = hasTouch ? e.touches[0] : e;
                //兼容chrome android，阻止浏览器默认行为
                // e.preventDefault();
                //计算手指的偏移量
                self.offsetY = point.pageY - self.startY;

                if (self.offsetY > 0) {//下拉
                    self.actionDir = 'down';
                    if (self.offsetY > 80) {
                        self.offsetY = 80;
                    }
                    waitingPanel.style.cssText = "margin-top:" + (self.offsetY - 50) + "px";
                    // wrap.style.cssText = "margin-top:" + self.offsetY + "px";
                    var endTime = new Date() * 1;
                    if (endTime - self.startTime > 2000) {
                        waitingPanel.style.cssText = "margin-top:-60px";
                    }
                } else {//上拉
                    self.actionDir = 'up';
                    // TODO
                }
            },
            _end: function (e) {
                var that = this;
                // e.preventDefault();
                if (that.actionDir == 'down') {
                    // label.innerHTML = "";
                    waitingPanel.style.cssText = "margin-top:-60px";
                    wrap.style.cssText = "margin-top:0px";
                }
                var wrapTop = wrap.getBoundingClientRect().top;
                if (that.callBack instanceof Function) {
                    if (that.offsetY > 0 && wrapTop > 0) {
                        that.callBack(that.obj);//执行回调函数
                    }
                }
                that._unbind(MOVE_EV, window);
                that._unbind(END_EV, window);
                that._unbind(CANCEL_EV, window);
            },

            _bind: function (type, el, bubble) {
                el.addEventListener(type, this, !!bubble);
            },

            _unbind: function (type, el, bubble) {
                el.removeEventListener(type, this, !!bubble);
            }
        };

        if (typeof exports !== 'undefined') {
            exports.qScroll = qScroll;
        } else {
            window.qScroll = qScroll;
        }

    },

    // 网页被卷去的高度
    getScrollTop: function () {
        var scrollTop, bodyScrollTop = 0, documentScrollTop = 0;
        if(document.body){
            bodyScrollTop = document.body.scrollTop;
        }
        if(document.documentElement){
            documentScrollTop = document.documentElement.scrollTop;
        }
        scrollTop = (bodyScrollTop - documentScrollTop > 0) ? bodyScrollTop : documentScrollTop;
        return scrollTop;
    },

    // 文档的总高度
    getScrollHeight: function () {
        var scrollHeight, bodyScrollHeight = 0, documentScrollHeight = 0;
        if (document.body) {
            bodyScrollHeight = document.body.scrollHeight;
        }
        if (document.documentElement) {
            documentScrollHeight = document.documentElement.scrollHeight;
        }
        scrollHeight = (bodyScrollHeight - documentScrollHeight > 0) ? bodyScrollHeight : documentScrollHeight;
        return scrollHeight;
    },

    // 网页可见区域高度
    getWindowHeight: function () {
        var windowHeight = 0;
        if (document.compatMode == "CSS1Compat") {
            windowHeight = document.documentElement.clientHeight;
        } else {
            windowHeight = document.body.clientHeight;
        }
        return windowHeight;
    }
}

Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

/**
 * 新浪微博mid与url互转实用工具
 * 作者: XiNGRZ (http://weibo.com/xingrz)
 */
var WeiboUtility = {};
/**
 * 62进制字典
 */
WeiboUtility.str62keys = [
    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
    "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
    "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
    "u", "v", "w", "x", "y", "z",
    "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
    "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
    "U", "V", "W", "X", "Y", "Z"
];
/**
 * 62进制值转换为10进制
 * @param {String} str62 62进制值
 * @return {String} 10进制值
 */
WeiboUtility.str62to10 = function(str62) {
    var i10 = 0;
    for (var i = 0; i < str62.length; i++)
    {
        var n = str62.length - i - 1;
        var s = str62[i];
        i10 += this.str62keys.indexOf(s) * Math.pow(62, n);
    }
    return i10;
};
/**
 * 10进制值转换为62进制
 * @param {String} int10 10进制值
 * @return {String} 62进制值
 */
WeiboUtility.int10to62 = function(int10) {
    var s62 = '';
    var r = 0;
    while (int10 != 0)
    {
        r = int10 % 62;
        s62 = this.str62keys[r] + s62;
        int10 = Math.floor(int10 / 62);
    }
    return s62;
};
/**
 * URL字符转换为mid
 * @param {String} url 微博URL字符，如 "wr4mOFqpbO"
 * @return {String} 微博mid，如 "201110410216293360"
 */
WeiboUtility.url2mid = function(url) {
    var mid = '';
    for (var i = url.length - 4; i > -4; i = i - 4) //从最后往前以4字节为一组读取URL字符
    {
        var offset1 = i < 0 ? 0 : i;
        var offset2 = i + 4;
        var str = url.substring(offset1, offset2);
        str = this.str62to10(str);
        if (offset1 > 0) //若不是第一组，则不足7位补0
        {
            while (str.length < 7)
            {
                str = '0' + str;
            }
        }
        mid = str + mid;
    }
    return mid;
};
/**
 * mid转换为URL字符
 * @param {String} mid 微博mid，如 "201110410216293360"
 * @return {String} 微博URL字符，如 "wr4mOFqpbO"
 */
WeiboUtility.mid2url = function(mid) {
    if (typeof(mid) != 'string') return false; //mid数值较大，必须为字符串！
    var url = '';
    for (var i = mid.length - 7; i > -7; i = i - 7) //从最后往前以7字节为一组读取mid
    {
        var offset1 = i < 0 ? 0 : i;
        var offset2 = i + 7;
        var num = mid.substring(offset1, offset2);
        num = this.int10to62(num);
        url = num + url;
    }
    return url;
};

/*
 *	date utils
 */
var DateUtils = {};
DateUtils.getInterval = function(date, time) {
    var now = new Date();
    var month = now.getMonth() + 1;
    var day = now.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (day >= 0 && day <= 9) {
        day = "0" + day;
    }
    var nowString = now.getFullYear()+'-'+month+'-'+day;
    if (date==nowString) {
        if (time != null) {
            var t = time.split(':');
            return now.getHours()-t[0]+'小时前';
        }
        else {
            return '今天';
        }
    }
    else {
        var diff = this.daysBetween(date, nowString);
        return diff+'天前';
    }
};


DateUtils.getIntervalDate = function(date) {
    var now = new Date();
    var month = now.getMonth() + 1;
    var day = now.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (day >= 0 && day <= 9) {
        day = "0" + day;
    }
    var nowString = now.getFullYear()+'-'+month+'-'+day;
    if (date==nowString) {
            return '今天';
    }
    else {
        return date;
    }
};

DateUtils.getIntervalTime = function(date, time) {
    var now = new Date();
    var month = now.getMonth() + 1;
    var day = now.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (day >= 0 && day <= 9) {
        day = "0" + day;
    }
    var nowString = now.getFullYear()+'-'+month+'-'+day;
    if (date==nowString) {
        if (time != null) {
            var t = time.split(':');
            var h_t = now.getHours()-t[0];
            if (h_t < 1) {
                var m_t = now.getMinutes()-t[1];
                if (m_t < 1){
                    var s_t = now.getSeconds() - t[2];
                    return s_t + '秒钟前';
                } else {
                    return m_t + '分钟前';
                }
            } else if (h_t >= 1 && h_t < 8){
                return h_t + '小时前';
            } else if (h_t >= 8 && h_t <= 24){
                return '今日' + t[0] + ":" + t[1];
            }
        } else {
            return date;
        }
    } else {
        return date;
    }
};


DateUtils.getIntervalTimeForTopic = function(date, time) {
    var now = new Date();
    var month = now.getMonth() + 1;
    var day = now.getDate();
    var timeResult = new Date(date).getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (day >= 0 && day <= 9) {
        day = "0" + day;
    }
   
    var nowString = now.getFullYear()+'-'+month+'-'+day;
    var t = time.split(':');
    var h_t = now.getHours()-t[0];
    if (date==nowString) {
        if (time != null) {
            if (h_t < 1) {
                var m_t = now.getMinutes()-t[1];
                if (m_t < 1){
                    var s_t = now.getSeconds() - t[2];
                    return s_t + '秒钟前';
                } else {
                    return m_t + '分钟前';
                }
            } else if (h_t >= 1 && h_t < 8){
                return h_t + '小时前';
            } else if (h_t >= 8 && h_t <= 24){
                return '今日' + t[0] + ":" + t[1];
            }
        } else {
            return (new Date(date).getMonth() + 1) + '-' + new Date(date).getDate() + " " + t[0] + ":" + t[1];
        }
    } else {
        return (new Date(date).getMonth() + 1) + '-' + new Date(date).getDate() + " " + t[0] + ":" + t[1];
    }
};

DateUtils.daysBetween = function(DateOne, DateTwo)
{
    var OneMonth = DateOne.substring(5,DateOne.lastIndexOf ('-'));
    var OneDay = DateOne.substring(DateOne.length,DateOne.lastIndexOf ('-')+1);
    var OneYear = DateOne.substring(0,DateOne.indexOf ('-'));

    var TwoMonth = DateTwo.substring(5,DateTwo.lastIndexOf ('-'));
    var TwoDay = DateTwo.substring(DateTwo.length,DateTwo.lastIndexOf ('-')+1);
    var TwoYear = DateTwo.substring(0,DateTwo.indexOf ('-'));

    var cha=((Date.parse(OneMonth+'/'+OneDay+'/'+OneYear)- Date.parse(TwoMonth+'/'+TwoDay+'/'+TwoYear))/86400000);
    return Math.abs(cha);
};

DateUtils.convertDateFormat = function(date)
{
    return date.split(".")[0].replace(/-/g, '/');
};

DateUtils.getLastWorkDate = function(date)
{
    var yesterday = date;
    if(yesterday.getDay() == 0){
        yesterday.setDate(date.getDate()-2);
    }else if(yesterday.getDay() == 1){
        yesterday.setDate(date.getDate()-3);
    }else{
        yesterday.setDate(date.getDate()-1);
    }
    return yesterday;
};

/*
 *	websocket utils
 */
var WebSocketUtils = {
    //9.111.21.125
    baseUrl: 'ws://47.96.26.149:8047/websocket'
};
