

$(function() {
	$('.ui-layer').click(function(){
		location.href='index.html'
    });
    countDown(5);
});
function countDown(secs){
	$('#endtime').html(secs);
	if(secs == 0){
		location.href='index.html' 
	 }
	 if(--secs>=0){
	  setTimeout("countDown("+secs+")",1000);
	 }
	 
}

