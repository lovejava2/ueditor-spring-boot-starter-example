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
            '</label><i class="el-icon-close delimg" ></i>' +
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
            '</label><i class="el-icon-close delimg" ></i>' +
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
            '</label><i class="el-icon-close delimg"></i>' +
            "<i class='el-icon-close-tip'>按 delete 键可删除</i>" +
            "</li>"
        );

    }

});


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
