var token = $.cookie('accesstoken');


function save() {
    debugger;
    var platform = $("#platform").val();
    var ver = $("#ver").val();
    var force = $("#force").val();//更新方式
    var prod_name = $("#prod_name").val();//产品名称
    var bundle_id = $("#bundle_id").val();//包名
    var instructions = $("#instructions").val();//更新内容
    var url = $("#url").val();//更新地址

    var params = {};
    params.token = token;
    debugger;
    params.platform = platform;
    params.ver = ver;
    params.force = force;
    params.prod_name = prod_name;
    params.bundle_id = bundle_id;
    params.instructions = instructions;
    params.url = url;
    $.ajax({
        url: "/version/save",
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
