var TODAY_TOPIC = "/topicTest/todayTopic";
var MTT_TOPIC = "/topicTest/mtTopic";
var MTB_TOPIC = "/topicTest/mtBrand";
var MTI_TOPIC = "/topicTest/mtItem";

$(document).ready(function() {
	$("#today").on("click",todayClick);
	$("#mtt").on("click",mttClick);
	$("#mtb").on("click",mtbClick);
	$("#mti").on("click",mtiClick);
});

function todayClick(){
	$("#submitTest")[0].action=TODAY_TOPIC;
	$("#submitTest").submit();
}
function mttClick(){
	$("#submitTest")[0].action=MTT_TOPIC;
	$("#submitTest").submit();
}
function mtbClick(){
	$("#submitTest")[0].action=MTB_TOPIC;
	$("#submitTest").submit();
}
function mtiClick(){
	$("#submitTest")[0].action=MTI_TOPIC;
	$("#submitTest").submit();
}
