$(function(){
	$(".time").click(function(){
		$(this).addClass('time_change').siblings().removeClass('time_change');
	});
	$(".dates").click(function(){
		$(".time").removeClass('time_change');
	});
});