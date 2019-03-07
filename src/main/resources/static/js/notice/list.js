var editObj = null;
var uid = $.cookie('loginUid');
var token = $.cookie('accesstoken');
$(function(){
        var h=xhrH();
        $.jgrid.defaults.styleUI = 'Bootstrap';
        $("#jqGrid").jqGrid({
            url: '/notice/getList',
            datatype: "json",
            postData:{'token':token,"flag":1},
            colModel: [
                { label: 'id', name: 'id', index: 'id', width: 50, key: true,hidden:true },
                { label: '标题', name: 'title', index: 'title', width: 80,sortable:false },
                { label: '内容', name: 'content', index: 'content', width: 80,sortable:false },
                // { label: '显示位置', name: 'showarea', index: 'showarea', width: 80,sortable:false },
               // { label: 'url', name: 'url', index: 'title', width: 80,sortable:false },
                { label: '开始时间', name: 'begintime', index: 'begintime', width: 80,sortable:false },
                { label: '结束时间', name: 'endtime', index: 'endtime', width: 80,sortable:false },
                { label: '创建时间', name: 'createtime', index: 'createtime', width: 80 },
                /*            { label: '操作', name: 'id', index: 'id', width: 80 ,formatter:function(cellvalue, options, rowObject){
                                debugger;
                                    var id = "\""+rowObject.id+"\"";
                                    var html = "";
                                    // var edit ="修改";
                                    var del = "删除";
                                    var obj=JSON.stringify(rowObject);
                                    debugger;
                                    // html = html + " <a href='javascript:void(0)' onclick='edit("+obj+")'>"+edit+"</a><br/> <a href='javascript:void(0)' onclick='del("+obj+")'>"+del+"</a><br/>";
                                    // html = html + "<a href='javascript:void(0)' onclick='del("+obj+")'>"+del+"</a><br/>";

                                    return html;
                                }},*/

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
        content : '/notice/toEdit' // iframe的url
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
        content : '/notice/toShow' // iframe的url
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
            url: "/notice/del?id="+obj.id,
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
    var ts=$("#ts").val();
    var nick=$("#nick").val();
    $("#jqGrid").jqGrid('setGridParam',{
        postData:{'ts':ts,"nick":nick},
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
        content : '/notice/toAdd' // iframe的url
    });
}
