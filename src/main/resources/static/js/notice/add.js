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



    debugger;
    var title = $("#title").val();
    var detail = $("#detail").val();
    var url = $("#url").val();
    var showarea = $("#showarea").val();//显示位置
    var params = {};
    params.token = token;
    params.startTime = startTime;
    params.endTime = endTime;
    debugger;
    params.title = title;
    params.content = detail;
    params.url = url;
    params.showarea = showarea;
    $.ajax({
        url: "/notice/save",
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
