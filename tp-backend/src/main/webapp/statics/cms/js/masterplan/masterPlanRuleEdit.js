var PFUNC ={};
var PCACHE  ={};
/**
 * 页面事件注册
 */
$(function(){
	
	PFUNC.retpar = function() {
		$('#cancelMasterRule').unbind().bind('click',function(){
			history.go(-1);
		});
	};
	
	PFUNC.inputFormBtn = function() {
		
		//添加
		$('.addReward').unbind().bind('click',function(){
			$("tr.temp_tr_reward").removeAttr("style");
			$("tr.temp_tr_reward").addClass("cp_tr_reward");
			var temptr = $("tr.temp_tr_reward").clone(true);
			$("tr.temp_tr_reward").addClass("cp_table_bak");
			
			$(this).parent().parent().parent().append(temptr); 
			
			$("tr.temp_tr_reward").removeClass("temp_tr_reward");
			$("tr.cp_table_bak").addClass("temp_tr_reward");
			$("tr.cp_table_bak").removeClass("cp_table_bak");
			$("tr.temp_tr_reward").removeClass("cp_tr_reward");
			$("tr.temp_tr_reward").attr("style","display:none");
		});
		
		//删除
		$('.delReward').unbind().bind('click',function(){
			$(this).parentsUntil("tr").parent().remove();
		});
		
		//提交
		$('#submMasterRule').unbind().bind('click',function(){
			var str = validata();
			if(!Putil.isEmpty(str)){
				alert(str);
				return;
			}
			var params = {};
			params.status = "submit";
			submitTmp(params);
		});
		
		//保存
		$('#saveMasterRule').unbind().bind('click',function(){
			var str = validata();
			if(!Putil.isEmpty(str)){
				alert(str);
				return;
			}
			var params = {};
			params.status = "add";
			submitTmp(params);
		});
		
		//保存和提交共用的submit
		function submitTmp(params){
			/** 主表记录保存 **/
			params.id = $("#masterId").val();
			params.cmaActivityStatus = $("#cmaActivityStatus").val();
			params.cmaActivityName = $("input.cmaActivityName").val();
			params.cmaPlanRecruit = $("input.cmaPlanRecruit").val();
			params.cmaRecruitStartdate = $("input.cmaRecruitStartdate").val();
			params.cmaRecruitEnddate = $("input.cmaRecruitEnddate").val();
			params.cmaCouponId = $("input.cmaCouponId").val();
			params.cmaCouponName = $("input.cmaCouponName").val();
			params.cmaActivityStartdate = $("input.cmaActivityStartdate").val();
			params.cmaActivityEnddate = $("input.cmaActivityEnddate").val();
			params.cmaRemarks = $("input.cmaRemarks").val();
			params.cmaSettleStartdate = $("input.cmaSettleStartdate").val();
			params.cmaSettleEnddate = $("input.cmaSettleEnddate").val();
			params.cmaIdent = $("select.cmaIdent").val();
			
			/** 子表奖励保存 **/
			var rewardSubject = [];
			var tempCmrType="";
			$("tr.tr_reward").each(function(){
				if(!Putil.isEmpty($(this).find("td select.cmrType").val())){
					tempCmrType = $(this).find("td select.cmrType").val();
				}
				rewardSubject.push({
					cmrType:tempCmrType,
					cmrThresholdStart:$(this).find("td input.cmrThresholdStart").val(),
					cmrThresholdEnd:$(this).find("td input.cmrThresholdEnd").val(),
					cmrAmount:$(this).find("td input.cmrAmount").val(),
					cmrWay:$(this).find("td select.cmrWay").val(),
					id:$(this).find("td input.rewardId").val()
            	});
	        });  
	        $("tr.cp_tr_reward").each(function(){
				rewardSubject.push({
					cmrType:$(this).find("td select.cmrType").val(),
					cmrThresholdStart:$(this).find("td input.cmrThresholdStart").val(),
					cmrThresholdEnd:$(this).find("td input.cmrThresholdEnd").val(),
					cmrAmount:$(this).find("td input.cmrAmount").val(),
					cmrWay:$(this).find("td select.cmrWay").val(),
					id:$(this).find("td input.rewardId").val()
            	});
	        });   
			params.reward = rewardSubject; 
			
			/** 子表消费优惠保存 **/
			var consuleSubject = [];
			$("tr.tr_consule").each(function(){
				consuleSubject.push({
					cmcType:$(this).find("td select.cmcType").val(),
					cmcAmount:$(this).find("td input.cmcAmount").val(),
					cmcNum:$(this).find("td input.cmcNum").val(),
					id:$(this).find("td input.consulerualId").val()
            	});
				
	        });  
			params.consule = consuleSubject; 
			
			console.log(params);
			
			/** 提交到服务端 **/
			$.post(domain +"/cms/addSubmit.htm", {params:$.toJSON(params)}, function(redata){
	           if(redata.isSuccess){
	        	   alert("提交成功");
	        	   goPageGet.call(this,domain +"/cms/listMasterPlan.htm");   
			   }else{
				   alert(redata.data);
			   }
	        }, "json");
	        
		}
		
		//提交和保存的验证
		function validata(){
			if(Putil.isEmpty($("input.cmaActivityName").val())){
				return "请输入活动名称";
			}
			if(Putil.isEmpty($("input.cmaPlanRecruit").val())){
				return "请输入招募人数";
			}
			if(Putil.isEmpty($("input.cmaCouponName").val())){
				return "请输入优惠批信息";
			}
			if(Putil.isEmpty($("input.cmaRecruitStartdate").val()) || 
				Putil.isEmpty($("input.cmaRecruitEnddate").val()) ){
				return "请输入招募时间";
			}
			if(Putil.isEmpty($("input.cmaActivityStartdate").val()) || 
				Putil.isEmpty($("input.cmaActivityEnddate").val()) ){
				return "请输入活动时间";
			}
			if(Putil.isEmpty($("input.cmaSettleStartdate").val()) || 
				Putil.isEmpty($("input.cmaSettleEnddate").val()) ){
				return "请输入结算时间";
			}
			
			var announceflag = 0;
			/*$("tr.tr_reward").find("input.cmrThresholdStart").each(function(){
	            var chk=$(this).val();
	           if(Putil.isEmpty(chk)){
	            	announceflag++;
	            }
	        });  
	        $("tr.tr_reward").find("input.cmrThresholdEnd").each(function(){
	            var chk=$(this).val();
	           if(Putil.isEmpty(chk)){
	            	announceflag++;
	            }
	        });  
	        $("tr.tr_reward").find("input.cmrAmount").each(function(){
	            var chk=$(this).val();
	           if(Putil.isEmpty(chk)){
	            	announceflag++;
	            }
	        });  
	        $("tr.cp_tr_reward").find("input.cmrThresholdStart").each(function(){
	            var chk=$(this).val();
	            console.log(chk);
	           if(Putil.isEmpty(chk)){
	            	announceflag++;
	            }
	        });  
	        $("tr.cp_tr_reward").find("input.cmrThresholdEnd").each(function(){
	            var chk=$(this).val();
	           if(Putil.isEmpty(chk)){
	            	announceflag++;
	            }
	        });  
	        $("tr.cp_tr_reward").find("input.cmrAmount").each(function(){
	            var chk=$(this).val();
	           if(Putil.isEmpty(chk)){
	            	announceflag++;
	            }
	        });  */
	        $("tr.tr_reward").each(function(){
				if(Putil.isEmpty($(this).find("td input.cmrThresholdStart").val())){
					announceflag++;
				}
				if(Putil.isEmpty($(this).find("td input.cmrThresholdEnd").val())){
					announceflag++;
				}
				if(Putil.isEmpty($(this).find("td input.cmrAmount").val())){
					announceflag++;
				}
	        });  
	        $("tr.cp_tr_reward").each(function(){
				if(Putil.isEmpty($(this).find("td input.cmrThresholdStart").val())){
					announceflag++;
				}
				if(Putil.isEmpty($(this).find("td input.cmrThresholdEnd").val())){
					announceflag++;
				}
				if(Putil.isEmpty($(this).find("td input.cmrAmount").val())){
					announceflag++;
				}
	        });  
			if(announceflag != 0){
				return "请完善达人奖励信息";
			}
			
			var anflag = 0;
			/*$("tr.tr_consule").find("input.cmcAmount").each(function(){
	            var chk=$(this).val();
	           if(Putil.isEmpty(chk)){
	            	anflag++;
	            }
	        });  
	        $("tr.tr_consule").find("input.cmcNum").each(function(){
	            var chk=$(this).val();
	           if(Putil.isEmpty(chk)){
	            	anflag++;
	            }
	        });  */
	        $("tr.tr_consule").each(function(){
				/*if(Putil.isEmpty($(this).find("td input.cmcAmount").val())){
					anflag++;
				}*/
				if(Putil.isEmpty($(this).find("td input.cmcNum").val())){
					anflag++;
				}
	        });  
			if(anflag != 0){
				return "请完善消费优惠信息";
			}
			
		};
		
		//优惠券批次号-确定
		var SEARCH_COUPON_NAME = domain + "/coupon/searchCouponName";
		$('#confirmBrand').unbind().bind('click',function(){
			var batchNo = $("input.tmpCmaCouponId").val();
			if(null == batchNo || $.trim(batchNo).length == 0){
				return;
			}
			$("input.cmaCouponId").val(batchNo);
			$.get(SEARCH_COUPON_NAME, {
				couponId : batchNo
			}, function(data) {
				if (data.success) {
					$("input.cmaCouponName").val(data.data);
				} else {
					layer.alert("查询优惠券批次信息失败");
				}
			});
		});
		//优惠券批次号-查询
		var SEARCH_COUPON = domain + "/cms/showCouponSearch";
		$('#searchBrand').unbind().bind('click',function(){
				$.layer({
					type : 2,
					title : '查询优惠券',
					shadeClose : true,
					maxmin : true,
					fix : false,
					area : [ '800px', 600 ],
					iframe : {
						src : SEARCH_COUPON + "?selectRow=" + 0
					},
					end : function() {
						searchNameByBatchNo(rowIndex);
					}
				});
		});
		/*//优惠券查询界面-取消
		$('#cancelRd').unbind().bind('click',function(){
			alert(1);
			var index = parent.layer.getFrameIndex(window.name);
			parent.layer.close(index);
			layer.close($("#applay_pop").data("pop"));
		});
		//优惠券查询界面-选择
		$('#selectedRd').unbind().bind('click',function(){
				
		});*/
		
	};
	
	/**
	 * 屏蔽输入框只能输入数字
	 */
	PFUNC.inputFormValidate = function() {
			$("input.cmrThresholdStart").keyup(function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).bind("paste",function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).css("ime-mode", "disabled");    
			
			$("input.cmrThresholdEnd").keyup(function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).bind("paste",function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).css("ime-mode", "disabled");    
			
			$("input.cmrAmount").keyup(function(){    
                $(this).val($(this).val().replace(/[^0-9.]/g,''));    
            }).bind("paste",function(){  //CTR+V事件处理    
                $(this).val($(this).val().replace(/[^0-9.]/g,''));     
            }).css("ime-mode", "disabled"); //CSS设置输入法不可用    

			$("input.cmcNum").keyup(function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).bind("paste",function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).css("ime-mode", "disabled");    
			
			$("input.cmcAmount").keyup(function(){    
                $(this).val($(this).val().replace(/[^0-9.]/g,''));    
            }).bind("paste",function(){  //CTR+V事件处理    
                $(this).val($(this).val().replace(/[^0-9.]/g,''));     
            }).css("ime-mode", "disabled"); //CSS设置输入法不可用    
			
			$("input.cmaPlanRecruit").keyup(function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).bind("paste",function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).css("ime-mode", "disabled");    
			
			$("input.cmaCouponId").keyup(function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).bind("paste",function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).css("ime-mode", "disabled");  
			
	};
	
});

//页面初始化
$(document).ready(function()
{
	//按钮事件
	PFUNC.inputFormBtn.call(this);
	// 返回事件
	PFUNC.retpar.call(this);
	// 控制界面的输入框只能输入数字问题
	PFUNC.inputFormValidate.call(this);
	
	PCACHE.temp_tr_reward=$("tr.temp_tr_reward").clone(true);
	
	$(".cmaRecruitStartdate").datetimepicker({
		showSecond: true,
        timeFormat: 'hh:mm:ss',
        stepHour: 1,
        stepMinute: 1,
        stepSecond: 1
	});

	$(".cmaRecruitEnddate").datetimepicker({
		showSecond: true,
        timeFormat: 'hh:mm:ss',
        stepHour: 1,
        stepMinute: 1,
        stepSecond: 1
	});
	
	$(".cmaActivityStartdate").datetimepicker({
		showSecond: true,
        timeFormat: 'hh:mm:ss',
        stepHour: 1,
        stepMinute: 1,
        stepSecond: 1
	});

	$(".cmaActivityEnddate").datetimepicker({
		showSecond: true,
        timeFormat: 'hh:mm:ss',
        stepHour: 1,
        stepMinute: 1,
        stepSecond: 1
	});
	
	$(".cmaSettleStartdate").datetimepicker({
		showSecond: true,
        timeFormat: 'hh:mm:ss',
        stepHour: 1,
        stepMinute: 1,
        stepSecond: 1
	});

	$(".cmaSettleEnddate").datetimepicker({
		showSecond: true,
        timeFormat: 'hh:mm:ss',
        stepHour: 1,
        stepMinute: 1,
        stepSecond: 1
	});
	
});	