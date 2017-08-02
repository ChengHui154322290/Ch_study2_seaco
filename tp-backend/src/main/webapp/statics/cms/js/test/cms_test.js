var TODAY_TOPIC = domain +"/cmstest/todayTopic.htm"

$(document).ready(function() {
	$("#today").on("click",todayClick);
});

function todayClick(){
	$("#submitTest")[0].action=TODAY_TOPIC;
	$("#submitTest").submit();
}
