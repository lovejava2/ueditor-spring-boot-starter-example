// var token = window.parent.parent.$("#token").val();
var token = $.cookie('accesstoken');
$(function () {
    debugger;
    //开工时间
    laydate.render({
        elem: '#tmTime',
        type: 'datetime'
    });

});

//发布星文
function save(flag) {
    var tmTime = $("#tmTime").val();
    if(flag == 2){
        //说明是定时发送
        if(tmTime == null || tmTime.length <= 0){
            layer.alert("请选择发布时间");
            return;
        }
    }
    var files = [];
    if($("input[name^='fjIds']") != null && $("input[name^='fjIds']") != undefined ){
        for(var i = 0; i < $("input[name^='fjIds']").length; i++){
            var url = $("input[name^='fjIds']")[i].value;
            files.push(url)
        }
    }
    if(null == files || files.length <= 0){
        layer.alert("请选择图片");
        return;
    }else if(files.length > 3){
        layer.alert("最多选择3张图片");
        return;
    }
    var cover_url_1 = "";
    var cover_url_2 = "";
    var cover_url_3 = "";
    for(var i = 0; i < files.length;i++){
        if(i == 0){
            cover_url_1 = files[i];
        }
        if(i == 1){
            cover_url_2 = files[i];
        }
        if(i == 2){
            cover_url_3 = files[i];
        }
    }
    debugger;
    var title = $("#title").val();
    var c = UE.getEditor('editor');
    var content = c.getContent();
    var appendCon = '<p style="text-align: center;"><span style="box-sizing: border-box; font-weight: 700; font-family: &quot;Helvetica Neue&quot;, Helvetica, Tahoma, Arial, sans-serif; font-size: 14px; background-color: rgb(248, 250, 254); color: rgb(165, 165, 165);">免责声明：本文来自星座吧官网自媒体，不代表星座吧的观点和立场。</span></p>'
    if(null != content && 0 < content.length && content.indexOf("免责声明") == -1){
        content = content + appendCon;
    }
    c.setContent(content);
    var params = {};
    params.title = title;
    params.contentHtml = c.getAllHtml();
    params.content = c.getContent();
    params.token = token;
    params.tm = tmTime;
    params.flag = flag;//发送标识
    params.cover_url_1 = cover_url_1;//
    params.cover_url_2 = cover_url_2;//发送标识
    params.cover_url_3 = cover_url_3;//发送标识
    $.ajax({
        url: "/xingwen/save",
        method: "post",
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        data:JSON.stringify(params),
        success: function (res) {
            debugger;
            if(res.code == 0 || res.code == '0'){
                if(type =='lay'){
                    layer.alert('新增成功！', function() {
                        parent.location.reload();
                        parent.reloadGrid();
                        parent.layer.closeAll();
                    });
                }else{
                    layer.alert('新增成功！', function() {
                        location.reload();
                    });

                }
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
        // "<div style='height: 100%;width: 240px' id='" + file.id + "'><img src='/img/ok.png' height='300' width='100'></div>" +
        // "<div><p>"+file.name+"</p><button type='button'  class='btn btn-danger xhr-up-del'>删除</button></div>" +
        // "</li>"
    );
    // $("#spsxList").append("<li><div style='height: 200px;width: 150px;'  id='" + file.id + "' >" +
    //     "<img  src='/img/ok.png' height='200' width='150'/> 上传中..</div>" +
    //     " <div > <p >"+file.name+"</p></div>" +
    //     "<div > <button type='button'  class='btn btn-danger xhr-up-del'>删除</button></div></li>");

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

// $('body').on('click', '.delimg ', function() {
//     //点击删除按钮，删除文件
//
//     $(this).parent().remove();
// });
