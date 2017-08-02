// JavaScript Document
$(function () {
	 $("#send").click(function(){
	    	var channel = $("#channel").val();
	    	if(!channel){
	    		return false;
	    	}
	    	var type = $("input[name='type']:checked").val();
	    	$("#qrcode").html("");
	    	/*var qrcode = new QRCode(document.getElementById("qrcode"), {
	            width : 120,//设置宽高
	            height : 120
	        });
	    	qrcode.makeCode(url);*/
	    	 $.ajax({
				  url: "/qrcode/channel.htm",
				  type: "GET",
				  data: {channel:channel,type:type},
				  success: function(result) {
					 // $("#qrcode").html("<img src='data:image/png;base64,"+result+"' />");
					  $("#qrcode").html("<img id='qrcodeImg' src='"+result+"' width='150' height='150'/>");
					  $("#downDiv").show();
					  //var p = "/file/download.htm?channel="+channel+"&type="+type;
					  var p ="/file/download.htm?imgPath="+result;
					  $("#downDiv").html("<a href="+p+">下载</a>");
			      }
			});
	    	
	   }); 
	 
	 document.querySelector('#qrcode').onclick = function () {
        var imgList = [
                  $("#qrcodeImg")[0].src
        ];
        wx.previewImage({
            current: imgList[0],
            urls:  imgList
        });
    };
});
