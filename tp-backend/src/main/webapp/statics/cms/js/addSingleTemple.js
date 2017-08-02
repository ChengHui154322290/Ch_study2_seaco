var PFUNC ={};
var PCACHE  ={};
var removeIds = [];
/**
 * 页面事件注册
 */
$(function(){
	
	PFUNC.inputButtonEvent = function() {
		/**
		 * 点击增加操作
		 */
		$('.singletemple_list_add').live('click',function(){
			$(this).parent().attr("style","display:none");
			$(this).parent().parent().find("td").eq(1).removeAttr("style");
			$("table.kptabContent").removeAttr("style");
			
			var tempTable = $("table.kptabContent").clone(true);
			$("table.kptabContent").addClass("cp_table_bak");
			
			$(this).parent().parent().parent().parent().parent().append(tempTable); 
			
			$("table.kptabContent").removeClass("kptabContent");
			$("table.cp_table_bak").addClass("kptabContent");
			$("table.cp_table_bak").removeClass("cp_table_bak");
			$("table.kptabContent").attr("style","display:none");
		});
		
		/**
		 * 点击移除操作
		 */
		$('.singletemple_list_del').live('click',function(){
			//移除的时候，需要把有id值的表格的id值存放到removeIds数组中，以便后面保存可以去操作
			/*removeIds.push({
				templeNodeId:$(this).find("td input.templeNodeId").val()
        	});*/
			//判断如果只有一个表单的时候，是不能移除
			if($('div.sel_singletemp_table table').length == 1){
				alert("模板节点信息不能全部移除");
				return;
			}
			
			if($(this).parentsUntil("table").find("td input.templeNodeId").val() != null && 
					$(this).parentsUntil("table").find("td input.templeNodeId").val() != ""){
				removeIds.push($(this).parentsUntil("table").find("td input.templeNodeId").val());
			}
			$(this).parentsUntil("table").parent().remove();
			
			//删除后判断有没有同级别的表单，如果有则需要把上面的同级别的增加按钮放开，否则就没有增加按钮了
			$('div.sel_singletemp_table table:last').find(".singletemple_list_add").parent().removeAttr("style");
		});
		
		/**
		 * 保存按钮
		 */
		$('#inputFormSaveBtn').live('click',function(){
			var params = {};
			var subject = [];
			var temple = {};
			temple.templeName= $("input.templeName").val();//模板名称
			temple.templePath = $("input.templePath").val();//模板路径
			temple.status = $("select.status").val();//模板状态
			temple.filename = $("#ftltemple").val();//模板文件名
			temple.ftltemplename = $("#ftltemplename").val();//模板需要上传的实际文件名
			temple.singleTempleId = $("input.tempId").val();//模板id值
			
			//暂时去除文件上传功能
			/*if(temple.templePath == "" && !check()){
				return;
			}*/
			
			if(Putil.isEmpty($("input.templeName").val())){
				alert("请填写模板名称");
				return;
			}
			
			var flag = false;
			$("div.sel_singletemp_table").find("table").each(function(){
					if(Putil.isEmpty($(this).find("td input.positionName").val())){
						flag = true;
					}
					if(Putil.isEmpty($(this).find("td input.positionSort").val())){
						flag = true;
					}
					if(Putil.isEmpty($(this).find("td input.positionSize").val())){
						flag = true;
					}
					if(Putil.isEmpty($(this).find("td input.buriedCode").val())){
						flag = true;
					}
					subject.push({
						positionName:$(this).find("td input.positionName").val(),
						positionSize:$(this).find("td input.positionSize").val(),
						positionSort:$(this).find("td input.positionSort").val(),
						buriedCode:$(this).find("td input.buriedCode").val(),
						id:$(this).find("td input.templeNodeId").val()
	            	});
				
	        });  
			if(flag){
				alert("请完善模板信息");
				return;
			}
			params.info = subject; 
			params.removeIds = removeIds;
			//表单添加url
			$("#inputForm").attr("action",domain +"/cmsSingleTemple/saveSingleTemp/"+$.toJSON(params)+".htm"); 
			
			$('#inputForm').ajaxSubmit(function(data){
	            if(data != "0"){
	            	alert("提交成功");
	            	
	            	//这个是把查询条件带过去
	    			var singleTempInfo={}; 
	    			singleTempInfo.templeNameBak = $('#templeNameBak').val();
	    			singleTempInfo.positionNameBak = $('#positionNameBak').val();
	    			singleTempInfo.statusBak = $('#statusBak').val();
	    			singleTempInfo.platformTypeBak = $('#platformTypeBak').val();
	    			singleTempInfo.typeBak = $('#typeBak').val();
	    			
	            	//goPageGet.call(this,domain +"/cmsSingleTemple/listSingletemple.htm?singleTempInfo="+$.toJSON(singleTempInfo));   
	            	window.location.href=domain +"/cmsSingleTemple/listSingletemple.htm?singleTempInfo="+$.toJSON(singleTempInfo);
	            }else{
	            	alert("提交失败");
	            }
	        });
			
		});
		
		function check(){
			var fileName = $("#ftltemple").val();
			if($.trim(fileName)==''){
				alert("请选择模板");
			    return false;
			}
			if(fileName.lastIndexOf(".")!=-1){
				var fileType = (fileName.substring(fileName.lastIndexOf(".")+1,fileName.length)).toLowerCase();
			    var suppotFile = new Array();
			    suppotFile[0] = "ftl";
			    suppotFile[0] = "txt";
			    for(var i =0;i<suppotFile.length;i++){
			       if(suppotFile[i]==fileType){
			    	   return true;
			       }else{
			    	   continue;
			       }
			    }
			    alert("不支持文件类型"+fileType);
				return false;
			 }
		}
		
	};
	
	
	/**
	 * 屏蔽输入框只能输入数字
	 */
	PFUNC.inputFormValidate = function() {
			$("input.positionSort").keyup(function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).bind("paste",function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).css("ime-mode", "disabled");    
			
			$("input.positionSize").keyup(function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).bind("paste",function(){      
		        var tmptxt=$(this).val();      
		        $(this).val(tmptxt.replace(/\D|^0/g,''));      
		   }).css("ime-mode", "disabled");    
			
	};
	
	PFUNC.retpar = function() {
		$('#returnPage').unbind().bind('click',function(){
			//history.go(-1);
			
			//这个是把查询条件带过去
			var singleTempInfo={}; 
			singleTempInfo.templeNameBak = $('#templeNameBak').val();
			singleTempInfo.positionNameBak = $('#positionNameBak').val();
			singleTempInfo.statusBak = $('#statusBak').val();
			singleTempInfo.platformTypeBak = $('#platformTypeBak').val();
			singleTempInfo.typeBak = $('#typeBak').val();
   	        window.location.href=domain +"/cmsSingleTemple/listSingletemple.htm?singleTempInfo="+$.toJSON(singleTempInfo);  
   	        
		});
	};
	
});

//页面初始化
$(document).ready(function()
{
	//PCACHE.temp_table_singletemp=$("table.tabContent").clone(true);
	
	// 返回事件
	PFUNC.retpar.call(this);
	
	// 按钮事件注册
	PFUNC.inputButtonEvent.call(this);
	// 控制界面的输入框只能输入数字问题
	PFUNC.inputFormValidate.call(this);
	
	
});	