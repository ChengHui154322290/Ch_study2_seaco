
var content = xigou.getLocalStorage("itemdetail");
if(typeof(content) != "undefined")
{
	$('.detail-content').html(content);
}