var streetArea = new MobileSelectArea();
var selectArea = new MobileSelectArea();
function initArea() {
	
	selectArea.init({
		trigger:'.address-select-input',
		value:$('#areaidlist').val(),
		level:3,
		data:xigou.activeHost + xigou.activeUrl.position.getareatree,
		position:"bottom"});
	streetArea.init({
		trigger:'.street-select-input',
		value:$('#streetId').val(),
		level:1,
		data:null,
		position:"bottom"});
};