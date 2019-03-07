var editObj = null;
var uid = $.cookie('loginUid');
var token = $.cookie('accesstoken');
$(function(){

    laydate.render({
        elem: '#begintime',
        type: 'datetime'
    });
    laydate.render({
        elem: '#endtime',
        type: 'datetime'
    });

    var h=xhrH();
    $.jgrid.defaults.styleUI = 'Bootstrap';
    $("#jqGrid").jqGrid({
        url: '/operate/getList',
        datatype: "json",
        postData:{'token':token,"flag":1},
        colModel: [
            { label: 'id', name: 'id', index: 'id', width: 50, key: true,hidden:true },
            { label: '标题', name: 'title', index: 'title', width: 80,sortable:false },
            { label: '图片', name: 'img', index: 'img', width: 80,sortable:false, formatter:function(cellvalue, options, rowObject){
                    debugger;
                    var str = ""
                    if(rowObject.img != null && rowObject.img != ""){
                        str = str + "<img width=80px height=80px  src="+rowObject.img+">";
                    }
                    return str;
                }},
            { label: '板块', name: 'banner_type', index: 'banner_type', width: 80,sortable:false,formatter:function(cellvalue, options, rowObject){
                    var html = "";
                    if(rowObject.banner_type == '1' || rowObject.banner_type == 1){
                        html = "直播板块";
                    }else{
                        html = "1v1";
                    }

                    return html;
                } },
            { label: '开始时间', name: 'begintime', index: 'begintime', width: 80,sortable:false },
            { label: '结束时间', name: 'endtime', index: 'endtime', width: 80,sortable:false},
            { label: '是否启用', name: 'enabled', index: 'enabled', width: 80,formatter:function(cellvalue, options, rowObject){
                    var html = "";
                    if(rowObject.enabled == '1' || rowObject.enabled == 1){
                        html = "是";
                    }else{
                        html = "否";
                    }

                    return html;
                } },
            { label: '操作', name: 'id', index: 'id', width: 80 ,formatter:function(cellvalue, options, rowObject){
                debugger;
                    var id = "\""+rowObject.id+"\"";
                    var html = "";
                    var edit ="修改";
                    var del = "删除";
                    var obj=JSON.stringify(rowObject);
                    debugger;
                    html = html + " " +
                        // "<a href='javascript:void(0)' onclick='edit("+obj+")'>"+edit+"</a><br/> " +
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
        content : '/operate/toEdit' // iframe的url
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
            url: "/operate/del?id="+obj.id,
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
    var begintime=$("#begintime").val();
    var endtime=$("#endtime").val();
    $("#jqGrid").jqGrid('setGridParam',{
        postData:{'begintime':begintime,"endtime":endtime},
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
        content : '/operate/toAdd?token='+token // iframe的url
    });
}
