var editObj = null;
var uid = $.cookie('loginUid');
var token = $.cookie('accesstoken');
$(function(){
    var h=xhrH();
    $.jgrid.defaults.styleUI = 'Bootstrap';
    $("#jqGrid").jqGrid({
        url: '/version/getList',
        datatype: "json",
        postData:{'token':token,"flag":1},
        colModel: [
            { label: 'id', name: 'id', index: 'id', width: 50, key: true,hidden:true },
            { label: '平台', name: 'platform', index: 'platform', width: 80,sortable:false,formatter:function(cellvalue, options, rowObject){
                    var html = "";
                    if(cellvalue == 'android'){
                        html = "安卓";
                    }else{
                        html = "IOS";
                    }
                    return html;
                } },
            { label: '版本号', name: 'ver', index: 'ver', width: 80,sortable:false },
            // { label: '显示位置', name: 'showarea', index: 'showarea', width: 80,sortable:false },
            { label: '更新方式', name: 'is_force', index: 'is_force', width: 80,sortable:false ,formatter:function(cellvalue, options, rowObject){
                debugger;
                var html = "";
                if(cellvalue == true || cellvalue == 'true'){
                    html = "强制更新";
                }else{
                    html = "推荐更新";
                }
                return html;
            }},
            { label: '产品名称', name: 'prod_name', index: 'prod_name', width: 80,sortable:false },
            { label: '包名', name: 'bundle_id', index: 'bundle_id', width: 80,sortable:false },
            { label: '更新内容', name: 'instructions', index: 'instructions', width: 80,sortable:false },
            { label: '更新地址', name: 'url', index: 'url', width: 80,sortable:false },
            { label: '创建时间', name: 'create_time', index: 'create_time', width: 80 },
            { label: '操作', name: 'id', index: 'id', width: 80 ,formatter:function(cellvalue, options, rowObject){
                debugger;
                    var id = "\""+rowObject.id+"\"";
                    var html = "";
                    var edit ="修改";
                    var obj=JSON.stringify(rowObject);
                    debugger;
                    html = html + " <a href='javascript:void(0)' onclick='edit("+obj+")'>"+edit+"</a>";

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
        content : '/version/toEdit' // iframe的url
    });
}

function reloadGrid(){
    var page = $("#jqGrid").jqGrid('getGridParam','page');
    var platform=$("#platform").val();
    $("#jqGrid").jqGrid('setGridParam',{
        postData:{'platform':platform},
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
        content : '/version/toAdd' // iframe的url
    });
}
