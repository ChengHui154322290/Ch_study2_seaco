/**
 * 会员查询页面
 */
$(function(){
	$(document.body).on('click','#btnChangeStatus', function() {
		var current_btn = $(this);
		var loginName=$("#username").val();
		var curStatus = $(this).attr('status');
		var changeStatus = curStatus == 0?1:0;
		$.ajax({
			url:domain + '/mem/updateMemberInfoStatus/?loginName='+loginName+'&status=' + changeStatus ,
			type:'post',
			cache: false,
			success:function(result){
				if(result.success){
					current_btn.attr('status',curStatus==0?1:0);
					current_btn.attr('class',curStatus==0?'btnuser btn82 btn_recycle':'btnuser btn82 btn_add2');
					current_btn.attr('value',curStatus==0?'冻结':'开启');
					$("#statusText").html(curStatus==0?'正常':'冻结');
					
				}
			}
		});
	});
});