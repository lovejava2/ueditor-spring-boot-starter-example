var token = $.cookie('accesstoken');
$(function () {
    debugger;

    laydate.render({
        elem: '#startTime',
        type: 'datetime'
    });
    laydate.render({
        elem: '#endTime',
        type: 'datetime'
    });


});

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

function save() {
    var startTime = $("#startTime").val();
    if(startTime == null || startTime.length <= 0){
        layer.alert("请选择开始时间");
        return;
    }
    var endTime = $("#endTime").val();
    if(endTime == null || endTime.length <= 0){
        layer.alert("请选择结束时间");
        return;
    }
    var files = [];//静态地址
    if($("input[name^='fjIds']") != null && $("input[name^='fjIds']") != undefined ){
        for(var i = 0; i < $("input[name^='fjIds']").length; i++){
            var url = $("input[name^='fjIds']")[i].value;
            files.push(url)
        }
    }
    var img = files[0];//礼物图标
    debugger;
    var type = $("#type").val();
    var title = $("#title").val();
    var disporder = $("#disporder").val();
    var url = $("#url").val();
    var params = {};
    params.token = token;
    params.startTime = startTime;
    params.endTime = endTime;
    debugger;
    params.type = type;
    params.title = title;
    params.url = url;
    params.img = img;
    params.disporder = disporder;
    $.ajax({
        url: "/operate/save",
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
