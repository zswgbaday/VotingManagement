var RestfulService = Class.extend({

    _get : function(url, successcb, errorcb, cached) {
        var isCached = cached || false;
        httpPromise = this.$http.get(encodeURI(url), {cache: isCached});
        return this._thenFactoryMethod(httpPromise, successcb, errorcb);
    },

    _post : function(url, data, successcb, errorcb, options) {
        data = data || {}
        var postOptions = options || {}
        httpPromise = this.$http.post(encodeURI(url), data, postOptions);
        return this._thenFactoryMethod(httpPromise, successcb, errorcb);
    },

    _put : function(url, data, successcb, errorcb) {
        data = data || {}
        httpPromise = this.$http.put(encodeURI(url), data);
        return this._thenFactoryMethod(httpPromise, successcb, errorcb);
    },

    _delete : function(url, successcb, errorcb) {
        httpPromise = this.$http.delete(encodeURI(url));
        return this._thenFactoryMethod(httpPromise, successcb, errorcb);
    },

    _thenFactoryMethod : function (httpPromise, successcb, errorcb) {
        var scb = successcb || angular.noop;
        var ecb = errorcb || angular.noop;


        return httpPromise.success(function(data, status, headers, config) {

            if(typeof data['success'] == "undefined"){
                window.location = "login";
            }

            if(data['success'] == true) {
                scb(data['obj'], status, headers, config);
            } else {
                ecb(data['obj'], status, headers, config);
            }
        }).error(function(data, status, headers, config) {
            var errorMessage = "内部服务器故障，请稍后再试！"
            ecb(errorMessage, status, headers, config);
        });
    }
});