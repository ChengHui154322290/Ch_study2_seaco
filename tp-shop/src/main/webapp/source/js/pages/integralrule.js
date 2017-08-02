var page = 1;
var totalpagecount=1;
$(function() {
	xigou.getLoginUserInfo({
		callback:function(userinfo,status){
			if(status==xigou.dictionary.success){
				pointinfolist();  // 积分详情
			}else{
				window.location.href="index.html";
			}
		}
	});
	$('.pointdetaillist').dropload({
		scrollArea : window,
		loadDownFn : function(me){
			me.resetload();
			pointinfolist(page++,me);
		}
	});
});

//用户-积分详情
function pointinfolist(){
	var params = {
		'token': xigou.getToken(),
		'curpage': page
	};
	if(totalpagecount==0 || page>(totalpagecount+1)){
		return;
	}
	xigou.activeUser.pointlist({
		requestBody: params,
		callback: function(response, status) { //回调函数
			if (status == xigou.dictionary.success) {
				if (null == response) {
					$.tips({
                            content:xigou.dictionary.chineseTips.server.value_is_null,
                            stayTime:2000,
                            type:"warn"
                        })
				} else {
					switch (response.code) {
						case "0":
							setHtml(response);
							break;
						default:
							$.tips({
	                            content:response.msg||"获取金币信息失败",
	                            stayTime:2000,
	                            type:"warn"
	                        });
							break;
					}
				}
			} else {
				$.tips({
					content:response.msg||"获取金币信息失败",
	                stayTime:2000,
	                type:"warn"
	            });
			}
		}
	});
}; 

function setHtml(response){
	var data = response.data;
	var pointDetailPage =data.pointDetailPage;
	if(pointDetailPage!=null){
		var list = pointDetailPage.list;	
		if(list!=null && list.length>=0){
			totalpagecount = data.pointDetailPage.totalpagecount;
			if(totalpagecount>0 || $(".pointdetaillist .product-item").length>0){
				if(list.length>0){
					$('.div_xgcoin_nmb').text(data.count);
					$("div.cartnull").remove();
					var html = [];
					for(var i=0;i<list.length;i++){
						html.push('<div class="product-item">');
						html.push('<div class="product-left-name">'+list[i].title+'</div>');
						html.push('<div><span class="product-left-nmb">'+list[i].bizTypeName+"&nbsp;&nbsp;"+list[i].bizId+'</span><span class="'+(list[i].pointTypeName == '+'?'xg_plus':'xg_minus')+'">'+list[i].pointTypeName+list[i].point+'</span></div>');
						html.push('<div class="product-left-date">'+list[i].createTime+'</div>');
						html.push('</div>');
					}
					$('.pointdetaillist').append(html.join(''));
					$('.dropload-down').hide();
				}
			}else{
				$(".pointdetaillist").empty();
				var htmlData = [];
				htmlData.push('<div class="cartnull">');
				htmlData.push('<img src="img/xgcoin2.png" style="width: 27%;margin: 0 auto;display: block;margin-top: 70px;" />');
				htmlData.push('<p  style="font-size:14px;text-align: center;color: #ccc;margin-top: 25px;" >还没有使用过金币哟~</p>');
				htmlData.push('</div>');
				$('.pointdetaillist').append(htmlData.join(''));
			}
		}			
	}
}
