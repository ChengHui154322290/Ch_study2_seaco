$(function(){
	getChannelInfo();
});

//获取微信支付OPENID
//function getWXPayOpendId(){
//	//微信获取opendid
//	if(isWeiXin()){
//		var openid = xigou.getLocalStorage("openid");
//		if(!openid){
//			openid=xigou.getQueryString('openid');
//		}
//		if (!openid) {
//			xigou.getwxOpenid(1, true);
//			return;
//		}
//		xigou.setLocalStorage("openid",openid);
//	}
//}

//获取渠道信息
function getChannelInfo(){
	var channelInfo = xigou.getSessionStorage("channelInfo");
	if(channelInfo && channelInfo != "{}") return;
	xigou.getChannelInfo({
		requestBody: {
			'token':xigou.getToken()
		},
		callback: function(response, status) {
		if (status == xigou.dictionary.success) {
			if (response != null && response.data != null) {
				xigou.setSessionStorage("channelInfo", JSON.stringify(response.data));
//				xigou.setLocalStorage("channelCode", response.data.channelcode);
				xigou.setSessionStorage("promoterid",response.data.promoterinfo.promoterid);
				xigou.setSessionStorage("eshopName", response.data.eshopname);
				xigou.setSessionStorage("channelName", response.data.channelname);
				xigou.setSessionStorage("companyDssType", response.data.dsstype);
			}
		}
	}
	});
}

!function($){
	var channelCode = xigou.channelcode;
	// 默认模板
	var _dialogTpl='<div class="ui-dialog">'+
        '<div class="ui-dialog-cnt">'+
            '<div class="ui-dialog-bd">'+
                '<div>'+
                '<h4><%=title%></h4>'+
                '<div><%=content%></div></div>'+
            '</div>'+
            '<div class="ui-dialog-ft ui-btn-group">'+
            	'<% for (var i = 0; i < button.length; i++) { %>' +
				'<% if (i == select) { %>' +
				'<button type="button" data-role="button"  class="select" id="dialogButton<%=i%>"><%=button[i]%></button>' +
				'<% } else { %>' +
				'<button type="button" data-role="button" id="dialogButton<%=i%>"><%=button[i]%></div>' +
				'<% } %>' +
				'<% } %>' +
            '</div>'+
        '</div>'+        
    '</div>';
	// 默认参数
	var defaults={
		title:'',
		content:'',
		button:['确认'],
		select:0,
		allowScroll:false,
		callback:function(){}
	}
	// 构造函数
	var Dialog  = function (el,option,isFromTpl) {

		this.option=$.extend(defaults,option);
		this.element=$(el);
		this._isFromTpl=isFromTpl;
		this.button=$(el).find('[data-role="button"]');
		this._bindEvent();
		this.toggle();
	}
	Dialog.prototype={
		_bindEvent:function(){
			var self=this;
			self.button.on("click",function(){
				var index=$(self.button).index($(this));
				// self.option.callback("button",index);
				var e=$.Event("dialog:action");
				e.index=index;
				self.element.trigger(e);
				self.hide.apply(self);
			});
		},
		toggle:function(){
			if(this.element.hasClass("show")){
				this.hide();
			}else{
				this.show();
			}
		},
		show:function(){
			var self=this;
			// self.option.callback("show");
			self.element.trigger($.Event("dialog:show"));
			self.element.addClass("show");
			this.option.allowScroll && self.element.on("touchmove" , _stopScroll);
			/*$(".ui-dialog-bd").css("background-image","url('shop/"+channelCode+"_header.png')");*/
		},
		hide :function () {
			var self=this;
			// self.option.callback("hide");
			self.element.trigger($.Event("dialog:hide"));
			self.element.off("touchmove" , _stopScroll);
			self.element.removeClass("show");
			
			self._isFromTpl&&self.element.remove();
		}
	}
	// 禁止冒泡
	function _stopScroll(){
		return false;
	}
	function Plugin(option) {

		return $.adaptObject(this, defaults, option,_dialogTpl,Dialog,"dialog");
	}
	$.fn.dialog=$.dialog= Plugin;
}(window.Zepto)