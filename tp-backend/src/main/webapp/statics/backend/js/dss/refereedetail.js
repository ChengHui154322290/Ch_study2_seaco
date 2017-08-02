$(function(){
$(document.body).on('click','.refereelistbtn',function(){
	var promoterid = $(this).attr('promoterid');
		pageii = $.layer({
			type : 2,
			title : '个人拉新佣金明细信息',
			shadeClose : true,
			maxmin : true,
			fix : false,
			area : [ '900px',600 ],
			iframe : {
				src : domain + '/dss/refereedetail/list?fixed=1&promoterId='+promoterid
			}
		});
})
var colM = [ 
		{name:'refereeDetailCode',index:'refereeDetailCode', align:"center",width:120},
	    {name:'promoterName',index:'promoterName',align:"center",width:80},
	    {name:'memberAccount',index:'memberAccount',align:"center",width:80},
		{name:'commision',index:'commision',align:"center",width:80, formatter:'currency', formatoptions:{decimalSeparator: '.'}},
		{name:'createTime',index:'createTime',align:"center",width:120,formatter:farmatTimeByNumber}
		];
var colN = ['拉新佣金编码','分销员名','新分销员','佣金', '创建时间'];
if($('#refereedetaillist').is('table')){
	$("#refereedetaillist").jqGrid({  
	    treeGridModel: 'adjacency',  
	    mtype:'post',
	    url: domain+'/dss/refereedetail/list.htm?m=' + Math.random(),
	    datatype: 'json', 
	    postData:{promoterId:$(':hidden[name=promoterId]').val(),memberId:$(':hidden[name=memberId]').val()},
	    viewrecords: true,
		loadonce:false,
	    height:'300',
	    width:"700",
	    colNames:colN,
		colModel:colM,
	    pager: "#gridpager",
	    multiselect : false,
	    pgbuttons:true,
	    pginput:true,
	    caption: "拉新明细信息", 
	    rowNum:10,
	    scrollrows:true,//使得选中的行显示到可视区域
	    sortname : 'refereeDetailCode',
	    rowList: [10,20,50], 
	    jsonReader: {  
	        root: "rows",
	        repeatitems: false  
	    }
	});
	jQuery("#refereedetaillist").jqGrid('navGrid', '#gridpager', {
		add : false,
		edit : false,
		del : false
	});
}
$("#refereedetaillist").setGridWidth($(window).width()*0.98);
$(document.body).on('click','.querypagebtn',function(){
	jQuery("#refereedetaillist").jqGrid(
			'setGridParam',
			{
				url : domain+'/dss/refereedetail/list.htm?m=' + Math.random(),
				postData:{promoterId:$(':hidden[name=promoterId]').val(),memberId:$(':hidden[name=memberId]').val()},
			}).trigger('reloadGrid');;
});
$("#withdrawauditlist").setGridWidth($(window).width()*0.98);
if($('.promotername').is('input')){
	$( ".promotername" ).autocomplete({
		source: function (request, response) {
	        var term = request.term;
	        request.promoterName = term;
	        $.post(domain+'/dss/promoterinfo/querypromoterlistbylikepromotername.htm', {
	        	promoterName:request.promoterName
	          }, response,'json');
	    },
		max:10,
		minLength: 2,
		select: function( event, ui ) {
			 $("input[name='promoterId']").val(ui.item.promoterId);
			 $("input.promotername").val(ui.item.promoterName);
			 return false;
		}
	}).on('blur',function(){
		if($(this).val()=='' || $(this).val()==null){
			$(":hidden[name='promoterId']").val('');
		}
	});
	$(".promotername").data("autocomplete")._renderItem = function (ul, item) {
	    return $("<li></li>")
	        .data("item.autocomplete", item)
	        .append("<a>" + item.promoterName + ",类型："+item.promoterTypeCn+",手机："+item.mobile+"</a>")
	        .appendTo(ul);
	};
}
if($( ".memberaccount" ).is('input')){
	$( ".memberaccount" ).autocomplete({
		source: function (request, response) {
	        $.post(domain+'/dss/commisiondetail/querymemberinfobylikememberaccount.htm', {
	        	promoterId:$(':hidden[name=promoterId]').val(),
	        	memberAccount:request.term
	          }, response,'json');
	    },
		max:10,
		minLength: 2,
		select: function( event, ui ) {
			 $("input[name='memberId']").val(ui.item.id);
			 $("input.memberaccount").val(ui.item.nickName);
			 return false;
		}
	}).on('blur',function(){
		if($(this).val()=='' || $(this).val()==null){
			$(":hidden[name='memberId']").val('');
		}
	});
	$(".memberaccount").data("autocomplete")._renderItem = function (ul, item) {
	    return $("<li></li>")
	        .data("item.autocomplete", item)
	        .append("<a>" + item.nickName + ",手机："+item.mobile+"</a>")
	        .appendTo(ul);
	};
}
});