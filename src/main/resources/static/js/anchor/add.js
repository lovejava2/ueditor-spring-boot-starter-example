var token = $.cookie('accesstoken');


function save() {
    debugger;
    var uid = $("#uid").val();
    var anchortype = $("#anchortype").val();
    var nick = $("#nick").val();


    var params = {};
    params.token = token;
    debugger;
    params.uid = uid;
    params.anchortype = anchortype;
    params.nick = nick;

    $.ajax({
        url: "/anchor/save",
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
