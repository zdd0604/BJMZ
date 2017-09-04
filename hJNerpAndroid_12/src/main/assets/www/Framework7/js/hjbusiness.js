
cordova.define("cordova/plugins/hjBusiness",
function(require,exports,module){
var exec = require('cordova/exec');
var HjBusiness = function() {};
//var isAndroid = (/android/gi).test(navigator.appVersion);
//if(isAndroid){
//	alert("android");
//}
HjBusiness.prototype.onhjcapture = function(successCallback, errorCallback) {
        if (errorCallback == null) { errorCallback = function() {}}
    
        if (typeof errorCallback != "function")  {
            console.log("BarcodeScanner.scan failure: failure parameter not a function");
            return
        }
    
        if (typeof successCallback != "function") {
            console.log("BarcodeScanner.scan failure: success callback parameter must be a function");
            return
        }
    
        exec(successCallback, errorCallback, 'HjBusiness', 'cameraaction', []);
    };
    
    HjBusiness.prototype.onhjscancode = function(successCallback, errorCallback) {
        if (errorCallback == null) { errorCallback = function() {}}
    
        if (typeof errorCallback != "function")  {
            console.log("BarcodeScanner.scan failure: failure parameter not a function");
            return
        }
    
        if (typeof successCallback != "function") {
            console.log("BarcodeScanner.scan failure: success callback parameter must be a function");
            return
        }
    
        exec(successCallback, errorCallback, 'HjBusiness', 'scanaction', []);
    };
    
    //保存数据
     HjBusiness.prototype.onhjsavectlm1347 = function(successCallback, errorCallback,str) {
        if (errorCallback == null) { errorCallback = function() {}}
    
        if (typeof errorCallback != "function")  {
            console.log("BarcodeScanner.scan failure: failure parameter not a function");
            return
        }
     
        if (typeof successCallback != "function") {
            console.log("BarcodeScanner.scan failure: success callback parameter must be a function");
            return
        }
    
        exec(successCallback, errorCallback, 'HjBusiness', 'saveaction',str);
    };
    //获取一组数据
     HjBusiness.prototype.onhjreadctlm1347 = function(successCallback, errorCallback,str) {
        if (errorCallback == null) { errorCallback = function() {}}
    
        if (typeof errorCallback != "function")  {
            console.log("BarcodeScanner.scan failure: failure parameter not a function");
            return
        }
    
        if (typeof successCallback != "function") {
            console.log("BarcodeScanner.scan failure: success callback parameter must be a function");
            return
        }
    
        exec(successCallback, errorCallback, 'HjBusiness', 'getoneaction',[{"key":str}]);
    };
     //获取位置
     HjBusiness.prototype.getLocation = function(successCallback, errorCallback) {
        if (errorCallback == null) { errorCallback = function() {}}
    
        if (typeof errorCallback != "function")  {
            console.log("BarcodeScanner.scan failure: failure parameter not a function");
            return
        }
    
        if (typeof successCallback != "function") {
            console.log("BarcodeScanner.scan failure: success callback parameter must be a function");
            return
        }
    
        exec(successCallback, errorCallback, 'HjBusiness', 'getlocationaction',[]);
    };
    //上传文件
     HjBusiness.prototype.onhjupload = function(successCallback, errorCallback,json,file) {
        if (errorCallback == null) { errorCallback = function() {}}
    
        if (typeof errorCallback != "function")  {
            console.log("BarcodeScanner.scan failure: failure parameter not a function");
            return
        }
    
        if (typeof successCallback != "function") {
            console.log("BarcodeScanner.scan failure: success callback parameter must be a function");
            return
        }

        exec(successCallback, errorCallback, 'HjBusiness', 'fieluploadaction',[{"key":json,"value":file}]);
    };
    //上传表单
     HjBusiness.prototype.onhjuploadctlm1347 = function(successCallback, errorCallback,str) {
        if (errorCallback == null) { errorCallback = function() {}}
    
        if (typeof errorCallback != "function")  {
            console.log("BarcodeScanner.scan failure: failure parameter not a function");
            return
        }
    
        if (typeof successCallback != "function") {
            console.log("BarcodeScanner.scan failure: success callback parameter must be a function");
            return
        }
    
        exec(successCallback, errorCallback, 'HjBusiness', 'billnouploadaction',[]);
    };
        //获取数据onhjbackmain
     HjBusiness.prototype.hjloaddata = function(successCallback, errorCallback,key,value,datasetmode) {
        if (errorCallback == null) { errorCallback = function() {}}
    
        if (typeof errorCallback != "function")  {
            console.log("BarcodeScanner.scan failure: failure parameter not a function");
            return
        }
    
        if (typeof successCallback != "function") {
            console.log("BarcodeScanner.scan failure: success callback parameter must be a function");
            return
        }
    
        exec(successCallback, errorCallback, 'HjBusiness', 'getdataaction',[{ "key": key, "value": value,"datasetmode": datasetmode}]);
    };
    //返回主界面
     HjBusiness.prototype.onhjbackmain = function(successCallback, errorCallback) {
        if (errorCallback == null) { errorCallback = function() {}}
    
        if (typeof errorCallback != "function")  {
            console.log("BarcodeScanner.scan failure: failure parameter not a function");
            return
        }
    
        if (typeof successCallback != "function") {
            console.log("BarcodeScanner.scan failure: success callback parameter must be a function");
            return
        }
    
        exec(successCallback, errorCallback, 'HjBusiness', 'backhomeaction',[]);
    };
    
    //f4 扫描二维码
     HjBusiness.prototype.onf4scancode = function( ) {
         
    
        exec(function(){}, function(){}, 'HjBusiness', 'f4keyscanaction',[]);
    };
    
    //获取本地图片地址
     HjBusiness.prototype.imgfilepath = function(successCallback, errorCallback,pic) {
        if (errorCallback == null) { errorCallback = function() {}}
    
        if (typeof errorCallback != "function")  {
            console.log("BarcodeScanner.scan failure: failure parameter not a function");
            return
        }
    
        if (typeof successCallback != "function") {
            console.log("BarcodeScanner.scan failure: success callback parameter must be a function");
            return
        }
    
        exec(successCallback, errorCallback, 'HjBusiness', 'getpicpath',[{"pic":pic}]);
    };
    
    //获取相册图片
     HjBusiness.prototype.imgalbum = function(successCallback, errorCallback) {
        if (errorCallback == null) { errorCallback = function() {}}
    
        if (typeof errorCallback != "function")  {
            console.log("BarcodeScanner.scan failure: failure parameter not a function");
            return
        }
    
        if (typeof successCallback != "function") {
            console.log("BarcodeScanner.scan failure: success callback parameter must be a function");
            return
        }
    
        exec(successCallback, errorCallback, 'HjBusiness', 'piccameraaction',[]);
    }; 
    
    //删除1347
     HjBusiness.prototype.delete1347 = function(successCallback, errorCallback,where) {
        if (errorCallback == null) { errorCallback = function() {}}
    
        if (typeof errorCallback != "function")  {
            console.log("BarcodeScanner.scan failure: failure parameter not a function");
            return
        }
    
        if (typeof successCallback != "function") {
            console.log("BarcodeScanner.scan failure: success callback parameter must be a function");
            return
        }
    
        exec(successCallback, errorCallback, 'HjBusiness', 'delete1347action',[{"key":where}]);
    };
    
    //修改1347
     HjBusiness.prototype.update1347 = function(successCallback, errorCallback,where) {
        if (errorCallback == null) { errorCallback = function() {}}
    
        if (typeof errorCallback != "function")  {
            console.log("BarcodeScanner.scan failure: failure parameter not a function");
            return
        }
    
        if (typeof successCallback != "function") {
            console.log("BarcodeScanner.scan failure: success callback parameter must be a function");
            return
        }
    
        exec(successCallback, errorCallback, 'HjBusiness', 'update1347action',[{"key":where}]);
    };
    var barcodeScanner = new HjBusiness();
    module.exports = barcodeScanner;
});

var hjbusiness = cordova.require("cordova/plugins/hjBusiness");

//if(!window.plugins) {
//    window.plugins = {};
//}
//if (!window.plugins.hjBusiness) {
//    window.plugins.hjBusiness = cordova.require("cordova/plugins/hjBusiness");
//}