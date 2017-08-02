var userMobile = xigou.getQueryString("i");
$(function() {
 	shareShop();	
});
/**
 * 店铺分享
 * @returns
 */
function  shareShop(){
	xigou.shareScanPromoter({
		requestBody: {'withQR': true,'userMobile':userMobile},
		callback:function(response,status){
			if (status == xigou.dictionary.success) {
				if(response != null && response != "undefined"){
//					$(".div_share_image").html("<img style='margin:15px 0 10px;' src='data:image/png;base64,"+response.img+"'/>");
					$(".div_share_image").html("<img style='margin:0;' src='"+response.img+"'/>");
				}
				else {
					window.location.href="home.html";
				}
			}
		}
	});
	
}


