// var token = window.parent.parent.$("#token").val();
var token = $.cookie('accesstoken');
$(function () {

    var name1 = $("#gift_type").find("option:selected").text();
    if(name1.indexOf("奢侈礼物") != -1){
        $("#type_scli").css("visibility","visible");
       // $("#anieffecttimes").css("visibility","visible");
    }else{
        $("#type_scli").css("visibility","hidden");
       // $("#anieffecttimes").css("visibility","hidden");
    }

    $("#gift_type").change(function () {
        var type = $("#gift_type").val();
        var name = $("#gift_type").find("option:selected").text();
        if(name.indexOf("奢侈礼物") != -1){
            $("#type_scli").css("visibility","visible");
          //  $("#anieffecttimes").css("visibility","visible");
        }else{
            $("#type_scli").css("visibility","hidden");
          //  $("#anieffecttimes").css("visibility","hidden");
        }

    })

});

function save() {
    var files = [];//静态地址
    if($("input[name^='fjIds']") != null && $("input[name^='fjIds']") != undefined ){
        for(var i = 0; i < $("input[name^='fjIds']").length; i++){
            var url = $("input[name^='fjIds']")[i].value;
            files.push(url)
        }
    }
    var icon = files[0];//礼物图标
    var files2 = [];//动态地址
    if($("input[name^='Anieffectaddress']") != null && $("input[name^='Anieffectaddress']") != undefined ){
        for(var i = 0; i < $("input[name^='Anieffectaddress']").length; i++){
            var url = $("input[name^='Anieffectaddress']")[i].value;
            files2.push(url)
        }
    }
    debugger;
    var Anieffectaddress = files2[0];//动态地址
    debugger;
    var gname = $("#gname").val();//礼物名称
    var gift_type = $("#gift_type").val();//礼物类型
    var visible = $("#visible").val();//是否启用
    var price = $("#price").val();//礼物价格
    var sys_getgold = $("#sys_getgold").val();//系统收益
    var user_getgold = $("#user_getgold").val();//对方收益
    var exp = $("#exp").val();//用户积分
    var remark = $("#remark").val();//备注
    var Anieffecttimes = $("#upload_report_fj_dttm").val();//动态时长


    var params = {};
    params.icon = icon;
    params.token = token;
    params.Anieffectaddress = Anieffectaddress;
    params.gname = gname;
    params.visible = visible;
    params.price = price;
    params.sys_getgold = sys_getgold;
    params.user_getgold = user_getgold;
    params.exp = exp;
    params.remark = remark;
    params.gift_type = gift_type;
    params.Anieffecttimes=Anieffecttimes;
    $.ajax({
        url: "/gift/save",
        method: "post",
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        data:JSON.stringify(params),
        success: function (res) {
            debugger;
            if(res.code == 0 || res.code == '0'){
                layer.alert('新增成功！', function() {
                    parent.location.reload();
                    parent.reloadGrid();
                    parent.layer.closeAll();
                });
            }else{
                layer.alert(res.msg);
            }
        },
    });
}
var upldts = WebUploader.create({
    // swf文件路径
    swf:'/js/plugins/webuploader/Uploader.swf',
    // 文件接收服务端。
    server: '/file/upload?time='+new Date().getTime(),
    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
    pick: '#upload_report_fj',
    resize: true,
    auto: true,
    fileNumLimit: 1,
});
// 当有文件被添加进队列的时候
upldts.on('fileQueued', function(file) {
    $("#spsxList").append(
        "<li tabindex='0' class='el-upload-list__item is-success' style='width: 25%;margin-left: 25%' id='" + file.id + "'>" +
        "<img src='' alt=\"\" class='el-upload-list__item-thumbnail'>" +
        "<a class=\"el-upload-list__item-name\"><i class=\"el-icon-document\"></i>上传中...</a>" +
        "<label class=\"el-upload-list__item-status-label\">" +
        "<i class=\"el-icon-upload-success el-icon-check\"></i>" +
        "</label>" +
        "" +
        "" +
        "</li>"

    );
});

upldts.on( 'uploadSuccess', function(file,r) {
    debugger;
    if(r.code == 0){
        var fid = "\""+file.id+"\"";
        var url = "\""+r.fileUrl+"\"";
        var str =
            "<img src='"+r.fileUrl+"' alt=\"\" class='el-upload-list__item-thumbnail'>" +
            "<a class=\"el-upload-list__item-name\"><i class=\"el-icon-document\"></i>上传成功<input type='hidden' name='fjIds'  value='"+r.fileUrl+"'></a>" +
            "<label class=\"el-upload-list__item-status-label\">" +
            "<i class=\"el-icon-upload-success el-icon-check\"></i>" +
            "</label><i class='el-icon-close delimg' onclick='delFile("+fid+","+url+")'></i>" +
            "<i class='el-icon-close-tip'>按 delete 键可删除</i>" ;
        $( '#'+file.id ).html(str);
    }else{
        $( '#'+file.id ).html("<img alt='image' src='/img/no.png' /> 上传失败");
    }
});
function delFile(fileId,url){
    upldts.removeFile(fileId);
    $( '#'+fileId ).remove();
    //删除图片
    var params = {};
    params.url = url;
    $.ajax({
        url: "/file/del",
        method: "post",
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        data:JSON.stringify(params),
        success: function (res) {
        },
    });

}
var upldts2 = WebUploader.create({
    // swf文件路径
    swf:'/js/plugins/webuploader/Uploader.swf',
    // 文件接收服务端。
    server: '/file/upload?time='+new Date().getTime(),
    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
    pick: '#upload_report_fj_dt',
    resize: true,
    auto: true,
    fileNumLimit: 1,
});

// 当有文件被添加进队列的时候
upldts2.on('fileQueued', function(file) {
    $("#spsxList2").append(
        "<li tabindex='0' class='el-upload-list__item is-success' style='width: 25%;margin-left: 25%' id='" + file.id + "'>" +
        "<img src='' alt=\"\" class='el-upload-list__item-thumbnail'>" +
        "<a class=\"el-upload-list__item-name\"><i class=\"el-icon-document\"></i>上传中...</a>" +
        "<label class=\"el-upload-list__item-status-label\">" +
        "<i class=\"el-icon-upload-success el-icon-check\"></i>" +
        "</label>" +
        "" +
        "" +
        "</li>"

    );
});

upldts2.on( 'uploadSuccess', function(file,r) {
    debugger;
    if(r.code == 0){
        var fid = "\""+file.id+"\"";
        var url = "\""+r.fileUrl+"\"";
        var str =
            "<img src='"+r.fileUrl+"' alt=\"\" class='el-upload-list__item-thumbnail'>" +
            "<a class=\"el-upload-list__item-name\"><i class=\"el-icon-document\"></i>上传成功<input type='hidden' name='Anieffectaddress'  value='"+r.fileUrl+"'></a>" +
            "<label class=\"el-upload-list__item-status-label\">" +
            "<i class=\"el-icon-upload-success el-icon-check\"></i>" +
            "</label><i class='el-icon-close delimg' onclick='delFile("+fid+","+url+")'></i>" +
            "<i class='el-icon-close-tip'>按 delete 键可删除</i>" ;
        $( '#'+file.id ).html(str);
    }else{
        $( '#'+file.id ).html("<img alt='image' src='/img/no.png' /> 上传失败");
    }
});


