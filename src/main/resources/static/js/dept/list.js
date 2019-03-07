var editObj = null;
var uid = $.cookie('loginUid');
var token = $.cookie('accesstoken');

// Add responsive to jqGrid
    $(window).bind('resize', function () {
        var width = $('.jqGrid_wrapper').width();
        $('#jqGrid').setGridWidth(width);
        $('#jqGrid').setGridWidth(width);
    })
}