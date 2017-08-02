var xigou =xigou||{};

;(function(N,$) {
	/**
	 * 简易表单验证 在需要验证的输入框的class当中添加formCheck值，代表当前输入框需要进行表单验证。
	 * 对于验证的格式可以在class当中添加methods当中已经定义的值， class中的值必须使用空格分开。示例（校验为必填且格式为日期格式）：
	 * <input type="text" class="formCheck required date"/>
	 */
	N.valiformdata = (function() {
		var methods = {};
		methods["required"] = function(p) {
			return p.value != "" ? "" : p.msg;
		};
		methods["date"] = function(p) {
			return (/^\d{4}[\/-]\d{1,2}[\/-]\d{1,2}$/.test(p.value)) ? "": p.msg;
		};
		methods["number"] = function(p) {
			return (/^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/.test(p.value.trim())) ? label
					|| null
					: p.msg;
		};
		methods["email"] = function(p) {
			return (/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))$/i
					.test(p.value)||!p.value) ? "" : p.msg;
		};
		methods["mobile"] = function(p) {
			return (/^(13|14|15|18|17)\d{9}$/.test(p.value.trim())||!p.value) ? "" : p.msg;
		};
		methods["emailormobile"] = function(p) {
			return (/^(13|14|15|18|17)\d{9}$|^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))$/
					.test(p.value)) ? "" : p.msg;
		};
		methods["phoneormobile"] = function(p) {
			return (/^\(?(0\d{2,3}-?)?\)?\d{7,8}$|^(13|14|15|18)\d{9}$/
					.test(p.value.trim())) ? "" : p.msg;
		};
		methods["string"] = function(p) {
			return (/^[a-zA-Z\u4E00-\u9FA5]+$/.test(p.value)||!p.value) ? ""
					: p.msg;
		};
		methods["licenseNo"] = function(p) {
			return (/(^[\u4E00-\u9FA5]{1}[A-Z0-9]{6}$)|(^[A-Z]{2}[A-Z0-9]{2}[A-Z0-9\u4E00-\u9FA5]{1}[A-Z0-9]{4}$)|(^[\u4E00-\u9FA5]{1}[A-Z0-9]{5}[挂学警军港澳]{1}$)|(^[A-Z]{2}[0-9]{5}$)|(^(08|38){1}[A-Z0-9]{4}[A-Z0-9挂学警军港澳]{1}$)/
					.test(p.value.trim())) ? "" : p.msg;
		};
		methods["chineseChracter"] = function(p) {
			return (/^[\u4e00-\u9fa5]+$/.test(p.value)) ? "" : p.msg;
		};
		methods["idcard"] = function(p) {
			return ((checkIdcard(p.value.trim()) == "验证通过!")||!p.value) ? "" : p.msg;
		};
		methods["address"] = function(p) {
			return (/^[0-9a-zA-Z\u4E00-\u9FA5]+$/.test(p.value)||!p.value) ? ""
					: p.msg;
		};
		methods["postCode"] = function(p) {
			return (/^[0-9]\d{5}$/.test(p.value)||!p.value) ? "" : p.msg;
		};
		methods["telephone"] = function(p) {
			return (/^\(?(0\d{2,3}-?)?\)?\d{7,8}$/.test(p.value)||!p.value) ? ""
					: p.msg;
		};
		methods["policyNo"] = function(p) {
			return (/^[A-z0-9]{22}$/.test(p.value)) ? "" : p.msg;
		};
		methods["checkPwd"] = function(p) {
			return (checkPwd(p.value) == true) ? "" : p.msg;
		};
		methods["password"] = function(p) {
			return (/^[a-zA-Z0-9_]{6,30}$/.test(p.value))? "" : p.msg;
		}
		methods["minLength"] = function(p) {
			return (p.value.length>=p.params.minLength) ? "" : p.msg;
		};
		methods["maxLength"] = function(p) {
			return (p.value.length<=p.params.maxLength) ? "" : p.msg;
		};
		methods["loginName"] = function(p) {
			return (/^[0-9a-zA-Z\_\-\u4e00-\u9fa5]+$/.test(p.value)||!p.value) ? ""
					: p.msg;
		};
		methods["lengthFromTo"] = function(p) {
			var reg=new RegExp("^.{"+p.params.from+","+p.params.to+"}$");
			return reg.test(p.value)? "": p.msg;
		};
		methods["cpicChineseChracter"] = function(p) {
			return (/^[\u4e00-\u9fa5|\u25cf|\u2022|%b7]+$/.test(p.value)) ? "" : p.msg;
		};
		methods["consistentTo"] = function(p) {
			var to=$(p.params.to);
			return p.value==to[0].value?"":p.msg;
		};
		methods["passwordFormat"] = function(p) {
			//返回空字符串 校验通过
			//返回非空字符串 校验不通过
			var regChinese = /^[^\u4e00-\u9fa5]{0,}$/; 
			return regChinese.test(p.value) ? "" : p.msg;
		};
		methods["licensePlateNumber"] = function(p) {
			//车牌号
			var reg = /^[A-Z0-9]{6}$/; 
			return reg.test(p.value) ? "" : p.msg;
		};
		methods["numberUppLetter"] = function(p) {
			//数字或大写字母
			var reg = /^[A-Z0-9]+$/; 
			return reg.test(p.value) ? "" : p.msg;
		};
		methods["chinese"] = function(p) {
			var min=p.params?(p.params.min||4):4,
				max=p.params?(p.params.max||30):30;
			//一个汉字算三个字符，所以全汉字的个数是2-10个汉字
			var _name=p.value.replace(/[\u4E00-\u9FA5]/g,'xxx');
			var len=_name.length;
			if(len>max || len<min){
				return p.msg;
			}else{
				return "";
			}
		};
		methods["checkXss"] = function(p){
//			var pattern = new RegExp("[%--`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#¥……&*（）——|{}【】‘；：”“'。，、？]")        //格式 RegExp("[在中间定义特殊过滤字符]")
			var pattern = new RegExp("[<>]");       //格式 RegExp("[在中间定义特殊过滤字符]")
			return pattern.test(p.value) ? p.msg : "";
		};
		/*function checkXss(s){
			if(s){
//				var pattern = new RegExp("[%--`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#¥……&*（）——|{}【】‘；：”“'。，、？]")        //格式 RegExp("[在中间定义特殊过滤字符]")
				var pattern = new RegExp("[<>]")        //格式 RegExp("[在中间定义特殊过滤字符]")
				if(pattern.test(s)){
					return true;
				}
				return false;
			}else{
				return false;
			}

		}*/
		
		return {
			check : function(parentNode,callBack) {
				parentNode=parentNode||$("body");
				var params = params || {};
				var pass = true,_methods,_errors;
				parentNode.find("[xigou-validata-enable='true']").each(function(){
					var _this=$(this);
					/*if(checkXss($(this).attr('value'))){
						var xssWarning = "";
						try{
							var xssWarnings = _this.attr("xigou-validata-errors").split("&")||[];
							xssWarning = xssWarnings[0] +  ",请输入正确的格式！";
						}catch(e){};
						$.isFunction(callBack)&&callBack(xssWarning, _this.attr("id"), "");
						pass = false;
						return;
					};*/
					
					_methods=_this.attr("xigou-validata-methods").split("&")||[];
					_errors=_this.attr("xigou-validata-errors").split("&")||[];
					for(var m=0;m<_methods.length;m++)
					{
						var methodName=_methods[m].split(":");
						if (!$.isFunction(methods[methodName[0]])) {
							continue;
						}
						var result = methods[methodName[0]]({
								value:_this.attr('value').replace(/\r/g, ""),
								msg:_errors[m],
								params:(methodName.length>1?JSON.parse(methodName.splice(1).join(":")):{})});
						result=result||"";
						if (result != "") {
							setTimeout(function(){
								$.isFunction(callBack)&&callBack(result,_this.attr("id"),methodName[0]);
							},30);
							pass = false;
							return pass;
						}
					}
				});
				return pass;
			},
			checkSingleNode:function(node,callBack,allCall){
				//allCall add 2014-12-30 控制是否验证不通过也调用回调函数 
				allCall=allCall||false;
				var pass = true;
				if(node)
				{
					var _this=node;
					_methods=_this.attr("xigou-validata-methods").split("&")||[];
					_errors=_this.attr("xigou-validata-errors").split("&")||[];
					for(var m=0;m<_methods.length;m++)
					{
						var methodName=_methods[m].split(":");
						if (!$.isFunction(methods[methodName[0]])) {
							continue;
						}
						var result = methods[methodName[0]]({
								value:_this.val().replace(/\r/g, ""), 
								msg:_errors[m],
								params:(methodName.length>1?JSON.parse(methodName.splice(1).join(":")):{})});
						result=result||"";
						if (result != "") {
							setTimeout(function(){
								$.isFunction(callBack)&&callBack(result,_this.attr("id"),methodName[0]);
							},30);
							pass = false;
							return pass;
						}
						if(allCall){
							setTimeout(function(){
								$.isFunction(callBack)&&callBack(result,_this.attr("id"),methodName[0]);
							},30);
						}
					}
				}
				return pass;
			},
			initValiData:function(params){
					var _methods=params.methods||{},
					_errors=params.errors||{};
				for(var m in _methods)
				{
					var _this=$("#"+m),ms=[],es=[];
					for(var md in _methods[m])
					{
						if(typeof _methods[m][md]=='object')
						{
							ms.push(md+":"+JSON.stringify(_methods[m][md]));
						}
						else{
							ms.push(md);
						}
						es.push(_errors[m][md]);
					}
					_this.attr("xigou-validata-methods",ms.join("&"))
						 .attr("xigou-validata-errors",es.join("&"))
						 .attr("xigou-validata-enable","true");
				}
			}
		};
	})();

	// 验证身份证
	function checkIdcard(idcard) {
		idcard = idcard.toUpperCase();
		var Errors = new Array("验证通过!", "身份证号码位数不对!", "身份证号码出生日期超出范围或含有非法字符!",
				"身份证号码校验错误!", "身份证地区非法!");
		var area = {
			11 : "北京",
			12 : "天津",
			13 : "河北",
			14 : "山西",
			15 : "内蒙古",
			21 : "辽宁",
			22 : "吉林",
			23 : "黑龙江",
			31 : "上海",
			32 : "江苏",
			33 : "浙江",
			34 : "安徽",
			35 : "福建",
			36 : "江西",
			37 : "山东",
			41 : "河南",
			42 : "湖北",
			43 : "湖南",
			44 : "广东",
			45 : "广西",
			46 : "海南",
			50 : "重庆",
			51 : "四川",
			52 : "贵州",
			53 : "云南",
			54 : "西藏",
			61 : "陕西",
			62 : "甘肃",
			63 : "青海",
			64 : "宁夏",
			65 : "新疆",
			71 : "台湾",
			81 : "香港",
			82 : "澳门",
			91 : "国外"
		};
		var Y, JYM;
		var S, M;
		var idcard_array = new Array();
		idcard_array = idcard.split("");
		if (area[parseInt(idcard.substr(0, 2))] == null)
			return Errors[4];
		switch (idcard.length) {
		case 15:
			if ((parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0
					|| ((parseInt(idcard.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(idcard
							.substr(6, 2)) + 1900) % 4 == 0)) {
				ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;// 测试出生日期的合法性
			} else {
				ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;// 测试出生日期的合法性
			}
			if (ereg.test(idcard))
				return Errors[0];
			else
				return Errors[2];
			break;
		case 18:
			if (parseInt(idcard.substr(6, 4)) % 4 == 0
					|| (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard
							.substr(6, 4)) % 4 == 0)) {
				ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;// 闰年出生日期的合法性正则表达式
				eregNow = /^[1-9][0-9]{5}20[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;// 闰年出生日期的合法性正则表达式
			} else {
				ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;// 平年出生日期的合法性正则表达式
				eregNow = /^[1-9][0-9]{5}20[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;// 平年出生日期的合法性正则表达式
			}
			if (ereg.test(idcard) || eregNow.test(idcard)) {
				S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10]))
						* 7
						+ (parseInt(idcard_array[1]) + parseInt(idcard_array[11]))
						* 9
						+ (parseInt(idcard_array[2]) + parseInt(idcard_array[12]))
						* 10
						+ (parseInt(idcard_array[3]) + parseInt(idcard_array[13]))
						* 5
						+ (parseInt(idcard_array[4]) + parseInt(idcard_array[14]))
						* 8
						+ (parseInt(idcard_array[5]) + parseInt(idcard_array[15]))
						* 4
						+ (parseInt(idcard_array[6]) + parseInt(idcard_array[16]))
						* 2 + parseInt(idcard_array[7]) * 1
						+ parseInt(idcard_array[8]) * 6
						+ parseInt(idcard_array[9]) * 3;
				Y = S % 11;
				M = "F";
				JYM = "10X98765432";
				M = JYM.substr(Y, 1);
				if (M == idcard_array[17])
					return Errors[0];
				else
					return Errors[3];
			} else
				return Errors[2];
			break;
		default:
			return Errors[1];
			break;
		}

	}

	// 验证密码
	function checkPwd(value) {
		if (value == '') {
			return false;
		}
		var _reg = "^[\\w@\\-\\.]{6,16}$";
		var re = new RegExp(_reg);
		if (!re.test(value)) {
			return false;
		}
		return true;
	}
})(xigou,Zepto);
