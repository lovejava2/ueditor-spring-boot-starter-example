var editObj = null;
var uid = $.cookie('loginUid');
var token = $.cookie('accesstoken');
$(function(){
    // laydate.render({
    //     elem: '#ts',
    //     type: 'datetime'
    // });
    var h=xhrH();
    $.jgrid.defaults.styleUI = 'Bootstrap';
    $("#jqGrid").jqGrid({
        url: '/gift/getList',
        datatype: "json",
        postData:{'token':token,"flag":1},
        colModel: [
            { label: '礼物id', name: 'gid', index: 'gid', width: 50, key: true },
            { label: '礼物图标', name: 'icon', index: 'icon', width: 80,sortable:false, formatter:function(cellvalue, options, rowObject){
                debugger;
                var str = ""
                    if(rowObject.icon != null && rowObject.icon != ""){
                        str = str + "<img width=80px height=80px  src="+rowObject.icon+">";
                    }
                    return str;
                }},
            { label: '礼物名称', name: 'gname', index: 'gname', width: 180,sortable:false},
            { label: '礼物类型', name: 'type_name', index: 'type_name', width: 80,sortable:false },
            { label: '礼物价格', name: 'price', index: 'price', width: 80,sortable:false },
            { label: '系统收益', name: 'sys_getgold', index: 'sys_getgold', width: 80,sortable:false },
            { label: '对方收益', name: 'anchor_getgold', index: 'anchor_getgold', width: 80,sortable:false },
            { label: '用户积分', name: 'exp', index: 'exp', width: 80,sortable:false },
            { label: '客户端排序', name: 'pos', index: 'pos', width: 80,sortable:false },
            // { label: '是否启用', name: 'visible', index: 'visible', width: 80,sortable:false },

            { label: '是否启用', name: 'visible', index: 'visible', width: 80,sortable:false ,formatter:function (cellvalue, options, rowObject) {
                debugger;
                    var html = "";
                    var id = "\""+rowObject.id+"\"";
                    var obj=JSON.stringify(rowObject);
                    if(cellvalue == 1){
                        //说明已经开启，
                        html = html + " <button  onclick='closeFlag("+obj+")'>禁用</button>";
                    }else{
                        html = html + " <button  onclick='openFlag("+obj+")'>启用</button>";
                    }
                    return html;
                }},

            { label: '备注', name: 'remark', index: 'remark', width: 80,sortable:false },
            // { label: '发布时间', name: 'ts', index: 'ts', width: 80,sortable:false , formatter:"date",formatoptions: {srcformat:'u',newformat:'Y-m-d H:i:s'}},
            // { label: '创建人', name: 'nick', index: 'nick', width: 80 },
            { label: '操作', name: 'id', index: 'id', width: 80 ,formatter:function(cellvalue, options, rowObject){
                var html = "";
                var edit ="修改";
                var del ="删除";
                var obj=JSON.stringify(rowObject);
                debugger;
                html = html + " <a href='javascript:void(0)' onclick='edit("+obj+")'>"+edit+"</a><br/> " +
                    "<a href='javascript:void(0)' onclick='del("+obj+")'>"+del+"</a><br/>";

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
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
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
function edit(obj) {
    editObj = obj;
    debugger;
    layer.open({
        type : 2,
        title : '修改',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '80%', '90%' ],
        content : '/gift/toEdit?token='+token // iframe的url
    });
}

function showEdit(obj) {
    editObj = obj;
    debugger;
    layer.open({
        type : 2,
        title : '查看',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '80%', '90%' ],
        content : '/xingwen/toShow' // iframe的url
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
        p.gid = obj.gid;
        p.token = token;
        $.ajax({
            url: "/gift/del?gid="+obj.gid,
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
    var keyword=$("#keyword").val();
    $("#jqGrid").jqGrid('setGridParam',{
        postData:{'keyword':keyword},
        page:page,
    }).trigger("reloadGrid");
}

function add() {
    layer.open({
        type : 2,
        title : '增加',
        maxmin : true,
        shadeClose : false, // 点击遮罩关闭层
        area : [ '80%', '90%' ],
        content : '/gift/toAdd?token='+token // iframe的url
    });
}

function closeFlag(obj) {
    debugger;
    layer.confirm('确定要禁用吗？', {
        title:'禁用确认',
        btn : [ '确定', '取消' ]//按钮
    }, function(index) {
        layer.close(index);
        var params = {};
        params.gid = obj.gid;
        params.gift_type = obj.gift_type;
        params.gname = obj.gname;
        params.icon = obj.icon;
        params.price = obj.price;
        params.Anieffectaddress = obj.Anieffectaddress;
        params.exp = obj.exp;
        params.anchor_exp = obj.anchor_exp;
        params.anchor_getgold = obj.anchor_getgold;
        params.user_getgold = obj.user_getgold;
        params.sys_getgold = obj.sys_getgold;
        params.pos = obj.pos;
        params.country = obj.country;
        params.showarea = obj.showarea;
        params.remark = obj.remark;
        params.visible = 0;
        params.token = token;
        $.ajax({
            url: "/gift/update",
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


function openFlag(obj) {
    layer.confirm('确定要启用吗？', {
        title:'启用确认',
        btn : [ '确定', '取消' ]//按钮
    }, function(index) {
        layer.close(index);
        var params = {};
        params.gid = obj.gid;
        params.gift_type = obj.gift_type;
        params.gname = obj.gname;
        params.icon = obj.icon;
        params.price = obj.price;
        params.Anieffectaddress = obj.Anieffectaddress;
        params.exp = obj.exp;
        params.anchor_exp = obj.anchor_exp;
        params.anchor_getgold = obj.anchor_getgold;
        params.user_getgold = obj.user_getgold;
        params.sys_getgold = obj.sys_getgold;
        params.pos = obj.pos;
        params.country = obj.country;
        params.showarea = obj.showarea;
        params.remark = obj.remark;
        params.visible = 1;
        params.token = token;
        $.ajax({
            url: "/gift/update",
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
