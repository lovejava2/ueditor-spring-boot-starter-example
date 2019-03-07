//工具集合Tools
window.T = {};

// 获取请求参数
// 使用示例
// location.href = http://localhost:8080/index.html?id=123
// T.p('id') --> 123;
var url = function(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
};
T.p = url;

//全局配置
$.ajaxSetup({
    // beforeSend: function (request, a) {
    //     request.setRequestHeader("Content-type", "application/json;charset=utf-8");
    //     request.setRequestHeader("token", token);
    // },
    complete:function(XMLHttpRequest,textStatus){
        if(XMLHttpRequest.responseJSON!=null &&XMLHttpRequest.responseJSON!='' &&XMLHttpRequest.responseJSON!='undefined'&&
            XMLHttpRequest.responseJSON.code==99999){
            /**//*alert('提示信息', "登陆超时！请重新登陆！", 'info',function(){
                window.location.href = '/sys/toInMain';
            });*/
            alert(XMLHttpRequest.responseJSON.msg, function () {
                window.location.href = "/sys/toInMain?time="+$.now();
            });
        }
    }
});

//重写alert
window.alert = function(msg, callback){
    parent.layer.alert(msg,{closeBtn: 0}, function(index){
        parent.layer.close(index);
        if(typeof(callback) === "function"){
            callback("ok");
        }
    });
}

//重写confirm式样框
window.confirm = function(msg, callback){
    parent.layer.confirm(msg, {btn: ['确定','取消']},
        function(){//确定事件
            if(typeof(callback) === "function"){
                callback("ok");
            }
        });
}

//选择一条记录
function getSelectedRow() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if(!rowKey){
        alert("请选择一条记录");
        return false;
    }

    var selectedIDs = grid.getGridParam("selarrrow");
    if(selectedIDs.length > 1){
        alert("只能选择一条记录");
        return false;
    }

    return selectedIDs[0];
}

function getSelectOrNull() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if(!rowKey){
    	alert("请选择一条记录！");
        return "one";
    }

    var selectedIDs = grid.getGridParam("selarrrow");
    if(selectedIDs.length > 1){
        alert("只能选择一条记录！");
        return "one";
    }
    return selectedIDs[0];
}

//选择多条记录
function getSelectedRows() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if(!rowKey){
        alert("请选择一条记录");
        return ;
    }

    return grid.getGridParam("selarrrow");
}

//判断是否为空
function isBlank(value) {
    return !value || !/\S/.test(value)
}
//获取浏览器高度
function xhrH(){
	  var h=$(window).height()-285-$(".xhr-seacher-select").height()-$(".xhr-com-ul").height();
	  return h;
}
//浏览器发生变化表格高度变化
$(window).resize(function () {
	var h=xhrH();
	$(".ui-jqgrid-bdiv").css("height",h+"px")
});
/*统计列表高度自适应*/
function tongjiH(){
	  var h=$(window).height()-$(".xhr-iboxxx").height()-120;
	  return h;
}

/*统计列表高度自适应new*/
function publicHeight(){
	  var h=$(window).height()-$("#xhr-publicH").height()-100;
	  if(h<400){
		  h=400
	  }
	  return h;
	 
}
//不知道加载层1和加层2 哪个管用，待测试后确定用哪一个
/*加载层1 系统管理-角色、部门、用户、字典（管理）     挂图作战-app问题直通车*/
function xhrDisabled(){
	  var index = layer.load(1, {shade: [0.1,'#000000']}); //0代表加载的风格，支持0-2
	 return index;
}
/*加载层2  问题咨询管理-企业问题上报-添加 promble.js*/
function xhrDisabled2(){
	debugger;
	 $(".layui-layer-btn0").css("pointer-events","none");
}
function xhrDisabled3(){
	layer.load();
}
/*返回*/
function xhrFh(){
	parent.layer.closeAll();      
}