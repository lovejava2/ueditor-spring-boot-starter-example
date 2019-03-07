debugger;
var fileUrls = [];
var obj = window.parent.editObj;
var token = $.cookie('accesstoken');
$(document).ready(function(){
    $("#title").val(obj.title);
    $("#xwid").val(obj.id);
    if(null != obj.cover_url_1 && 0 < obj.cover_url_1.length){
        var  cover_url = "cover_url_1";
        fileUrls.push(obj.cover_url_1);
        $("#spsxList").append(
            "<li tabindex='0' class='el-upload-list__item is-success' style='width: 25%;margin-left: 25%' id='cover_url_1'>" +
            "<img src='"+obj.cover_url_1+"' alt=\"\" class='el-upload-list__item-thumbnail'>" +
            "<a class=\"el-upload-list__item-name\"><i class=\"el-icon-document\"></i>上传成功<input type='hidden' name='fjIds'  value='"+obj.cover_url_1+"'></a>" +
            "<label class=\"el-upload-list__item-status-label\">" +
            "<i class=\"el-icon-upload-success el-icon-check\"></i>" +
            '</label><i class="el-icon-close delimg" onclick="delFileUrl(\''+obj.cover_url_1+'\',\''+cover_url+'\')"></i>' +
            "<i class='el-icon-close-tip'>按 delete 键可删除</i>" +
            "</li>"
        );
    }

    if(null != obj.cover_url_2 && 0 < obj.cover_url_2.length){
        var  cover_url = "cover_url_2";
        fileUrls.push(obj.cover_url_2);
        $("#spsxList").append(
            "<li tabindex='0' class='el-upload-list__item is-success' style='width: 25%;margin-left: 25%' id='cover_url_2'>" +
            "<img src='"+obj.cover_url_2+"' alt=\"\" class='el-upload-list__item-thumbnail'>" +
            "<a class=\"el-upload-list__item-name\"><i class=\"el-icon-document\"></i>上传成功<input type='hidden' name='fjIds'  value='"+obj.cover_url_1+"'></a>" +
            "<label class=\"el-upload-list__item-status-label\">" +
            "<i class=\"el-icon-upload-success el-icon-check\"></i>" +
            '</label><i class="el-icon-close delimg" onclick="delFileUrl(\''+obj.cover_url_2+'\',\''+cover_url+'\')"></i>' +
            "<i class='el-icon-close-tip'>按 delete 键可删除</i>" +
            "</li>"
        );
    }

    if(null != obj.cover_url_3 && 0 < obj.cover_url_3.length){
        var  cover_url = "cover_url_3";
        fileUrls.push(obj.cover_url_3);
        $("#spsxList").append(
            "<li tabindex='0' class='el-upload-list__item is-success' style='width: 25%;margin-left: 25%' id='cover_url_3'>" +
            "<img src='"+obj.cover_url_3+"' alt=\"\" class='el-upload-list__item-thumbnail'>" +
            "<a class=\"el-upload-list__item-name\"><i class=\"el-icon-document\"></i>上传成功<input type='hidden' name='fjIds'  value='"+obj.cover_url_1+"'></a>" +
            "<label class=\"el-upload-list__item-status-label\">" +
            "<i class=\"el-icon-upload-success el-icon-check\"></i>" +
            '</label><i class="el-icon-close delimg" onclick="delFileUrl(\''+obj.cover_url_3+'\',\''+cover_url+'\')"></i>' +
            "<i class='el-icon-close-tip'>按 delete 键可删除</i>" +
            "</li>"
        );

    }

});

function delFileUrl(url,cover_url) {
    if(fileUrls != null && 0 < fileUrls.length){
        for(var i = 0; i < fileUrls.length;i++){
            if(fileUrls[i] == url){
                fileUrls.slice(i,1);
            }
        }
    }
    $( '#'+cover_url ).remove();

}
//初始化ueditor  建议放到最开头
var ue = UE.getEditor('editor');
//获取后台传过来的filmVo对象的filmDesc属性的内容(html字符串内容(数据库查到的))
//这个监听器是亮点
ue.addListener('ready',function () {
    var params = {};
    params.url = obj.url;
    $.ajax({
        url: "/file/getContent",
        method: "post",
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        data:JSON.stringify(params),
        success: function (res) {
            debugger;
            um.execCommand('inserthtml', res.con);
        },
    });

});

var fileUrls = [];
function save() {
    var tmTime = obj.tm;
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
    var id = $("#xwid").val();
    var c = UE.getEditor('editor');
    var content = c.getContent();

    var appendCon = '<p style="text-align: center;"><span style="box-sizing: border-box; font-weight: 700; font-family: &quot;Helvetica Neue&quot;, Helvetica, Tahoma, Arial, sans-serif; font-size: 14px; background-color: rgb(248, 250, 254); color: rgb(165, 165, 165);">免责声明：本文来自星座吧官网自媒体，不代表星座吧的观点和立场。</span></p>'
    if(null != content && 0 < content.length && content.indexOf("免责声明") == -1){
        content = content + appendCon;
    }
    c.setContent(content);
    var params = {};
    params.id = id;
    params.title = title;
    params.contentHtml = c.getAllHtml();
    params.content = c.getContent();
    params.token = token;
    params.tm = tmTime;
    params.flag = obj.flag;//发送标识
    params.cover_url_1 = cover_url_1;//
    params.cover_url_2 = cover_url_2;//发送标识
    params.cover_url_3 = cover_url_3;//发送标识
    params.url = obj.url;//
    $.ajax({
        url: "/xingwen/edit",
        method: "post",
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        data:JSON.stringify(params),
        success: function (res) {
            debugger;
            if(res.code == 0 || res.code == '0'){
                layer.alert('修改成功！', function() {
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
    if(r.code == 0){
        fileUrls.push(r.fileUrl);
        var fid = "\""+file.id+"\"";
        var str =
            "<img src='"+r.fileUrl+"' alt=\"\" class='el-upload-list__item-thumbnail'>" +
            "<a class=\"el-upload-list__item-name\"><i class=\"el-icon-document\"></i>上传成功<input type='hidden' name='fjIds'  value='"+r.fileUrl+"'></a>" +
            "<label class=\"el-upload-list__item-status-label\">" +
            "<i class=\"el-icon-upload-success el-icon-check\"></i>" +
            "</label><i class='el-icon-close delimg' onclick='delFile("+fid+")'></i>" +
            "<i class='el-icon-close-tip'>按 delete 键可删除</i>" ;
        $( '#'+file.id ).html(str);
    }else{
        $( '#'+file.id ).html("<img alt='image' src='/img/no.png' /> 上传失败");
    }
});

function delFile(fileId){
    upldts.removeFile(fileId);
    $( '#'+fileId ).remove();
}