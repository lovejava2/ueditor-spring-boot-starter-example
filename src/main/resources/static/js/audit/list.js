var token = $.cookie('accesstoken');
$(function(){
    var h=xhrH();
    $.jgrid.defaults.styleUI = 'Bootstrap';
    $("#jqGrid").jqGrid({
        url: '/audit/getList',
        datatype: "json",
        postData:{'token':token},
        colModel: [
            { label: 'id', name: 'id', index: 'id', width: 50, key: true,hidden:true },
            { label: '版本号', name: 'audit_version', index: 'audit_version', width: 80,sortable:false },
            { label: 'boundID', name: 'bundle_id', index: 'bundle_id', width: 180,sortable:false},
            { label: '添加时间', name: 'create_time', index: 'create_time', width: 80,sortable:false },
            { label: '审核开关', name: 'flag', index: 'flag', width: 80,sortable:false ,formatter:function (cellvalue, options, rowObject) {
                var html = "";
                var id = "\""+rowObject.id+"\"";
                var obj=JSON.stringify(rowObject);
                if(cellvalue){
                    //说明已经开启，
                    html = html + " <button  onclick='closeFlag("+obj+")'>关闭</button>";
                }else{
                    html = html + " <button  onclick='openFlag("+obj+")'>开启</button>";
                }
                    return html;
                }},
            { label: '操作人', name: 'nick', index: 'nick', width: 80 },
            { label: '操作', name: 'id', index: 'id', width: 80 ,formatter:function(cellvalue, options, rowObject){
                    var id = "\""+rowObject.id+"\"";
                    var html = "";
                    var del = "删除";
                    var obj=JSON.stringify(rowObject);
                    html = html + " <a href='javascript:void(0)' onclick='del("+obj+")'>"+del+"</a>";

                    return html;
                }},

        ],
        viewrecords: true,
        height: h,
        rowNum: 10,
        rowList : [10,30,50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "list",
            page: "currPage",
            total: "totalPage",
            records: "totalCount"
        },
        prmNames : {
            page:"page",
            rows:"limit",
            order: "order"
        },
        gridComplete:function(){
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    });


    // Add responsive to jqGrid
    $(window).bind('resize', function () {
        var width = $('.jqGrid_wrapper').width();
        $('#jqGrid').setGridWidth(width);
        $('#jqGrid').setGridWidth(width);
    });

});

function resetForm(){
    $("#role_main_query_form")[0].reset();
}

function openFlag(obj) {
    debugger;
    layer.confirm('确定要开启吗？', {
        title:'开启确认',
        btn : [ '确定', '取消' ]//按钮
    }, function(index) {
        layer.close(index);
        var params = {};
        params.ver = obj.audit_version;
        params.boundID = obj.bundle_id;
        params.switchType = 1;
        params.token = token;
        $.ajax({
            url: "/audit/save",
            method: "post",
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            data:JSON.stringify(params),
            success: function (res) {
                debugger;
                if(res.code == 0){
                    alert("操作成功", function (index) {
                        $("#jqGrid").trigger("reloadGrid");
                    });
                }
            },
        });

    });

}


function closeFlag(obj) {
    debugger;
    layer.confirm('确定要关闭吗？', {
        title:'关闭确认',
        btn : [ '确定', '取消' ]//按钮
    }, function(index) {
        layer.close(index);
        var params = {};
        params.ver = obj.audit_version;
        params.boundID = obj.bundle_id;
        params.switchType = 0;
        params.token = token;
        $.ajax({
            url: "/audit/save",
            method: "post",
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            data:JSON.stringify(params),
            success: function (res) {
                debugger;
                if(res.code == 0){
                    alert("操作成功", function (index) {
                        $("#jqGrid").trigger("reloadGrid");
                    });
                }
            },
        });

    });

}

function del(obj) {
    debugger;
    layer.confirm('确定要删除吗？', {
        title:'删除确认',
        btn : [ '确定', '取消' ]//按钮
    }, function(index) {
        layer.close(index);
        var p = {};
        p.id = obj.id;
        p.token = token;
        $.ajax({
            url: "/audit/del?id="+obj.id,
            method: "post",
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            data:JSON.stringify(p),
            success: function (res) {
                debugger;
                if(res.code == 0){
                    alert("删除成功", function (index) {
                        $("#jqGrid").trigger("reloadGrid");
                    });
                }


            },
        });

    });
    
}

function reloadGrid(){
    var page = $("#jqGrid").jqGrid('getGridParam','page');
    $("#jqGrid").jqGrid('setGridParam',{
        postData:{},
        page:page,
    }).trigger("reloadGrid");
}

function add() {
    layer.open({
        type : 2,
        title : '增加',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '60%', '80%' ],
        content : '/audit/toAdd' // iframe的url
    });
}
