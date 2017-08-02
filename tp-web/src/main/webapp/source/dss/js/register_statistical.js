/*
* @Author: dongqian
* @Date:   2017-05-09 10:42:31
* @Last Modified by:   dongqian
* @Last Modified time: 2017-05-12 13:19:56
*/

'use strict';
var min_date = new Date(Date.parse('2016/10/01'));
$(function() {
    removeQueryParams();
    xigou.getLoginUserInfo({
        callback:function(userinfo,status){
            if(status==xigou.dictionary.success){
                initDate();
                initQueryBtn();
            }else{
                window.location.href="index.html";
            }
        }
    });

    $(".list_time").click(function(){
        $(this).addClass('current').siblings().removeClass('current');
        initDateByTime($(this).attr('datavalue'));
    });
    $(".dates").click(function(){
        $(".list_time").removeClass('current');
    });

    if(isWeiXin()){
        $(".header1").hide();
        $('title').html('注册统计');
    };
    $('#beginquerydate').click(function (argument) {
        var $date = $('#beginquerydate');
        $date.change(function () {
            var val = $('#beginquerydate').val();
            if (val != '' && val != ' ' && val != null && val != undefined) {
                $('.date-mask').html($('#beginquerydate').val());
            }
        });

    });
    $('#endquerydate').click(function (argument) {
        var $date = $('#endquerydate');
        $date.change(function () {
            var val = $('#endquerydate').val();
            if (val != '' && val != ' ' && val != null && val != undefined) {
                $('.date-mask1').html($('#endquerydate').val());
            }
        });
    });

});


/**查询*/
function initQueryBtn(){
    var querybtn = $('.ask-btn');
    querybtn[xigou.events.click](function(){
        var begindate = new Date($('#beginquerydate').val());
        var beginquerydate = formatDate(begindate,'yyyy-MM-dd 00:00:01');
        var enddate = new Date($('#endquerydate').val());
        var endquerydate = formatDate(enddate,'yyyy-MM-dd 23:59:59');
        if( (new Date(beginquerydate.replace(/\-/g,'/')).getTime())>(new Date(endquerydate.replace(/\-/g,'/')).getTime())){
            $.tips({
                content:'起始时间应小于结束时间',
                stayTime:2000,
                type:"warn"
            });
            return;
         }
        xigou.setSessionStorage('beginquerydate',beginquerydate);
        xigou.setSessionStorage('endquerydate',endquerydate);
        window.location.href='register_ask.html?type=2';
    });
}


function initDate(){
    var currentDate = formatDate1(new Date());
    var beginDate = formatDate1((new Date().getTime()-1000*60*60*24*7));
    var $date = $('#beginquerydate');
    var $date1 = $('#endquerydate');
    $('#beginquerydate').attr('max', formatDate1(beginDate).toString());
    var $mask = $('<div class="date-mask"></div>');
    var $mask1 = $('<div class="date-mask1"></div>');
        $mask.html(beginDate);
        $mask1.html(currentDate);
        $date.val(beginDate);
        $date1.val(currentDate);
    $date.after($mask);
    $date1.after($mask1);
}
/**点击日期段设置日期*/
function initDateByTime(dateType){
    var endDate = new Date();
    var beginDate = new Date();
    if(dateType=='month'){
        beginDate.setMonth(beginDate.getMonth()-1);
    }else if(dateType=='quarter'){
        beginDate.setMonth(beginDate.getMonth()-3);
    }else{
        beginDate.setDate(beginDate.getDate()-7);
    }
    if(min_date.getTime()>beginDate.getTime()){
        beginDate = min_date;
    }
    $('#beginquerydate').val(formatDate1(beginDate));
    $('.date-mask').html(formatDate1(beginDate));
    $('#endquerydate').val(formatDate1(endDate));
    $('.date-mask1').html(formatDate1(endDate));

}
function removeQueryParams(){
    xigou.removeSessionStorage('beginquerydate');
    xigou.removeSessionStorage('endquerydate');
}
function formatDate1(date){
    var data = {};
    data.year =  new Date(date).getFullYear();
    data.month =  new Date(date).getMonth()+1;
    data.day =  new Date(date).getDate();
    var date = data.year +'-'+ (data.month < 10 ? '0'+data.month:data.month)+'-'+(data.day < 10 ? '0'+data.day:data.day);
    return date
}