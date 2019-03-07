var token = $.cookie('accesstoken');
function save() {
    debugger;
    var ver = $("#ver").val();
    var boundID = $("#boundID").val();
    if(null == ver || 0 >= ver.length){
        layer.alert("请输入版本号");
        return;
    }
    if(null == boundID || 0 >= boundID.length){
        layer.alert("请输入boundID");
        return;
    }
    var switchType = $('#switchType option:selected') .val();
    var params = {};
    params.ver = ver;
    params.boundID = boundID;
    params.switchType = switchType;
    params.token = token;
    $.ajax({
        url: "/audit/save",
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

// $('body').on('click', '.delimg ', function() {
//     //点击删除按钮，删除文件
//
//     $(this).parent().remove();
// });
