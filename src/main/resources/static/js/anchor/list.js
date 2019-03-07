var editObj = null;
var uid = $.cookie('loginUid');
var token = $.cookie('accesstoken');
$(function(){
    var h=xhrH();
    $.jgrid.defaults.styleUI = 'Bootstrap';
    $("#jqGrid").jqGrid({
        url: '/anchor/getList',
        datatype: "json",
        postData:{'token':token,"flag":1},
        colModel: [
            { label: '主播昵称', name: 'nick', index: 'nick', width: 50, sortable:false },
            { label: '用户ID', name: 'uid', index: 'uid', width: 80,sortable:false },
            { label: '权限', name: 'anchor_type', index: 'anchor_type', width: 80,sortable:false,formatter:function(cellvalue, options, rowObject){
                debugger;
                var str = ""
                if(rowObject.anchor_type == '3'){
                    str = "直播和1v1"
                }
                else if(rowObject.anchor_type == '2'){
                    str = "1v1"
                }else{
                    str = "直播";
                }
                return str;
            } },
            { label: '添加时间', name: 'update_time', index: 'update_time', width: 80,sortable:false },

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

function reloadGrid(){
    var page = $("#jqGrid").jqGrid('getGridParam','page');
    var uid=$("#nick").val();
    $("#jqGrid").jqGrid('setGridParam',{
        postData:{"uid":uid},
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
        content : '/anchor/toAdd' // iframe的url
    });
}
