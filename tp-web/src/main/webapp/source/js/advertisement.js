
var url = decodeURIComponent(xigou.getQueryString("url"));
$(function() {
	advertisementInit();
});
function advertisementInit() {
	var advertisementFrom = xigou.getQueryString("advertisementFrom");
	if (advertisementFrom != "" && advertisementFrom != undefined
			&& advertisementFrom != "undefined"&& advertisementFrom !=null) {
		xigou.setSessionStorage("advertisementFrom",advertisementFrom);
		xigou.recordAdverstLog({
			requestBody : {
				'advertPlatCode' : advertisementFrom,
				'url':url
			},
			callback : function(response, status) { 
				if (status == xigou.dictionary.success) {
					location.href=url;
				}
			}
		});

	}
}