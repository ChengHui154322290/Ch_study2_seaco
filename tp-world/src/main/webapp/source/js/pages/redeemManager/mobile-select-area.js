
;
(function(root, factory) {
	//amd
	if (typeof define === 'function' && define.amd) {
		define(['$', 'dialog'], factory);
	} else if (typeof define === 'function' && define.cmd) {
		define(function(require, exports, module) {
			var $ = require("$");
			var Dialog = require("dialog");
			return factory($, Dialog);
		});
	} else if (typeof exports === 'object') { //umd
		module.exports = factory();
	} else {
		root.MobileSelectArea = factory(window.Zepto || window.jQuery || $);
	}
})(this, function($, Dialog) {
	var MobileSelectArea = function() {
		var rnd = Math.random().toString().replace('.', '');
		this.id = 'scroller_' + rnd;
		this.scroller;
		this.data;
		this.index = 0;
		this.value = [0, 0, 0];
		this.oldvalue;
		this.oldtext = [];
		this.text = ['', '', ''];
		this.level = 3;
		this.mtop = 30;
		this.separator = ' ';
	};
	MobileSelectArea.prototype = {
		init: function(settings) {
			this.settings = $.extend({
				eventName: 'click'
			}, settings);
			this.trigger = $(this.settings.trigger);
			this.settings.default == undefined ? this.default = 1 : this.default = 0; //0为空,1时默认选中第一项
			level = parseInt(this.settings.level);
			this.level = level > 0 ? level : 3;
			this.trigger.attr("readonly", "readonly");
			this.value = (this.settings.value && this.settings.value.split(",")) || [0, 0, 0];
			this.text = this.settings.text || this.trigger.val().split(' ') || ['', '', ''];
			this.oldvalue = this.value.concat([]);
			this.oldtext = this.text.concat([]);
			this.clientHeight = document.documentElement.clientHeight || document.body.clientHeight;
			this.clientWidth = document.documentElement.clientWidth || document.body.clientWidth;
			// this.promise = this.getData();
			this.bindEvent();
		},
		getData: function() {
			var _this = this;
			var dtd = $.Deferred();
			if (typeof this.settings.data == "object") {
				//this.data = this.settings.data;
				dtd.resolve();
			} else {
				$.ajax({
					dataType: 'json',
					cache: true,
					url: this.settings.data,
					type: 'GET',
					success: function(result) {
						_this.data = result;
						dtd.resolve();
					},
					accepts: {
						json: "application/json, text/javascript, */*; q=0.01"
					}
				});
			}
			return dtd;
		},
		bindEvent: function() {
			var _this = this;
			this.trigger[_this.settings.eventName](function(e) {
				var dlgContent = '';
				for (var i = 0; i < _this.level; i++) {
					dlgContent += '<div></div>';
				};
				var settings, buttons;
				if (_this.settings.position == "bottom") {
					settings = {
						position: "bottom",
						width: "100%",
						className: "ui-dialog-bottom",
						animate: false
					}
					var buttons = [{
						'no': '取消'
					}, {
						'yes': '确定'
					}];
				}
				$.confirm('<div class="ui-scroller-mask"><div id="' + _this.id + '" class="ui-scroller">' + dlgContent + '<p></p></div></div>', buttons, function(t, c) {
					if (t == "yes") {
						_this.submit()
					}
					if (t == 'no') {
						_this.cancel();
					}
					this.dispose();
				}, $.extend({
					width: 320,
					height: 215
				}, settings));
				_this.scroller = $('#' + _this.id);
				_this.getData().done(function() {
					_this.format();
				});
				var start = 0,
					end = 0
				_this.scroller.children().bind('touchstart', function(e) {
					start = (e.changedTouches || e.originalEvent.changedTouches)[0].pageY;
				});
				_this.scroller.children().bind('touchmove', function(e) {
					end = (e.changedTouches || e.originalEvent.changedTouches)[0].pageY;
					var diff = end - start;
					var dl = $(e.target).parent();
					if (dl[0].nodeName != "DL") {
						return;
					}
					var top = parseInt(dl.css('top') || 0) + diff;
					dl.css('top', top);
					start = end;
					return false;
				});
				_this.scroller.children().bind('touchend', function(e) {
					end = (e.changedTouches || e.originalEvent.changedTouches)[0].pageY;
					var diff = end - start;
					var dl = $(e.target).parent();
					if (dl[0].nodeName != "DL") {
						return;
					}
					var i = $(dl.parent()).index();
					var top = parseInt(dl.css('top') || 0) + diff;
					if (top > _this.mtop) {
						top = _this.mtop;
					}
					if (top < -$(dl).height() + 60) {
						top = -$(dl).height() + 60;
					}
					var mod = top / _this.mtop;
					var mode = Math.round(mod);
					var index = Math.abs(mode) + 1;
					if (mode == 1) {
						index = 0;
					}
					_this.value[i] = $(dl.children().get(index)).attr('ref');
					var curLevel = $(dl.children().get(index)).attr('level');
					_this.value[i] == 0 ? _this.text[i] = "" : _this.text[i] = $(dl.children().get(index)).html();
					if (!$(dl.children().get(index)).hasClass('focus')) {
						for (var j = _this.level - 1; j > i; j--) {
							_this.value[j] = 0;
							_this.text[j] = "";
						}
						_this.format(_this.value[i],parseInt(curLevel)+1);
					}
					$(dl.children().get(index)).addClass('focus').siblings().removeClass('focus');
					dl.css('top', mode * _this.mtop);
					return false;
				});
				return false;
			});
		},
		format: function(pid,level) {
			var _this = this;
			if (level > _this.level - 1) {
				if(level>1){
					streetArea.value=[];
					streetArea.text=[];
					$.ajax({
						dataType: 'json',
						cache: true,
						url: this.settings.data+"?id="+pid,
						type: 'GET',
						success: function(result) {
							streetArea.data=result;
							$.Deferred().resolve();
						},
						accepts: {
							json: "application/json, text/javascript, */*; q=0.01"
						}
					});
				}
				return;
			}
			var child = _this.scroller.children();
			var area = {};
			area.data = this.data;
			area.pid = pid;
			area.level = level;
			if(pid==null || pid==undefined){
				area.pid=0;
				area.level = 0;
				this.f(area);
			}else{
				$.ajax({
					dataType: 'json',
					cache: true,
					url: this.settings.data+"?id="+pid,
					type: 'GET',
					success: function(result) {
						area.data = result;
						_this.f(area);
						$.Deferred().resolve();
					},
					accepts: {
						json: "application/json, text/javascript, */*; q=0.01"
					}
				});
			}
		},
		f: function(area) {
			var _this = this;
			var item = area.data;
			var curLevel = area.level;
			if (!item) {
				item = [];
			};
			var str = '<dl><dd ref="0" level="'+area.level+'">——</dd>';
			var focus = 0,
				childData, top = _this.mtop;
			if (curLevel !== 0 && _this.value[curLevel - 1] == "0" && this.default == 0) {
				str = '<dl><dd ref="0" class="focus" level="'+area.level+'">——</dd>';
				_this.value[curLevel] = 0;
				_this.text[curLevel] = "";
				focus = 0;
			} else {
				if (_this.value[curLevel] == "0") {
					str = '<dl><dd ref="0" class="focus" level="'+area.level+'">——</dd>';
					focus = 0;
				}
				if (item.length > 0 && this.default == 1) {
					str = '<dl>';
					var pid = item[0].pid || area.pid||0;
					var id = item[0].code || 0;
					focus = item[0].code;
					//childData = item[0].child;
					if (!_this.value[curLevel]) {
						_this.value[curLevel] = id;
						_this.text[curLevel] = item[0].name;
					}
					str += '<dd pid="' + pid + '" class="' + cls + '" ref="' + id + '" level="'+area.level+'">' + item[0].name + '</dd>';
				}
				for (var j = _this.default, len = item.length; j < len; j++) {
					var pid = item[j].pid || area.pid||0;
					var id = item[j].code || 0;
					var cls = '';
					if (_this.value[curLevel] == id) {
						cls = "focus";
						focus = id;
						//childData = item[j].child;
						top = _this.mtop * (-(j - _this.default));
					};
					str += '<dd pid="' + pid + '" class="' + cls + '" ref="' + id + '" level="'+area.level+'">' + item[j].name + '</dd>';
				}
			}
			str += "</dl>";
			var newdom = $(str);
			newdom.css('top', top);
			var child = _this.scroller.children();
			$(child[curLevel]).html(newdom);
			if (curLevel > _this.level - 1) {
				curLevel = 0;
				return;
			}
			_this.format(focus,curLevel+1);
		},
		submit: function() {
			this.oldvalue = this.value.concat([]);
			this.oldtext = this.text.concat([]);
			if (this.trigger[0].nodeType == 1) {
				//input
				this.trigger.val(this.text.join(this.separator));
				this.trigger.attr('data-value', this.value.join(','));
			}
			this.trigger.next(':hidden').val(this.value.join(','));
			this.settings.callback && this.settings.callback.call(this, this.scroller, this.text, this.value);
			if(this.level==3){
				$('#rela_street').val('').attr('data-value','').next(':hidden').val('');
			}
		},
		cancel: function() {
			this.value = this.oldvalue.concat([]);
			this.text = this.oldtext.concat([]);
		}
	};
	return MobileSelectArea;
});

