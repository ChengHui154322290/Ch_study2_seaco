var pageii;
var chk_value =[];
$(function(){
	
	$('.specGroupeditbtn').live('click',function(){	
		var id= $(this).attr('param');						
		  pageii=$.layer({
            type : 2,
            title: '规格组->编辑',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['900px', 600],                     
            iframe: {
                src : domain+'/basedata/specGroup/group/edit.htm?id='+id
            } 
        });
	}); 
	
	$('.specGroupViewbtn').live('click',function(){	
		var id= $(this).attr('param');						
		  pageii=$.layer({
            type : 2,
            title: '尺码组->查看',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['900px', 600],                     
            iframe: {
                src : domain+'/basedata/specGroup/group/view.htm?id='+id
            } 
        });
	}); 
	
//取消按钮
	$('.closebtn').on('click',function(){
		parent.layer.close(parent.pageii);
	});
	
$('.specGroupaddbtn').live('click',function(){				
		  pageii=$.layer({
            type : 2,
            title: '规格组->增加',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['900px', 600],                     
            iframe: {
                src : domain+'/basedata/specGroup/group/add.htm'
            } 
        });
	}); 


$("#searchSp").on('input',function(e){  
	searchSpec();
 });  

$('input[name="ids"]:checked').each(function(){ 
chk_value.push($(this).val()); 
}); 


if($("#datasubmit").is("input")){
	var butt=$("#datasubmit");
	$.formValidator.initConfig({submitButtonID:"datasubmit",debug:false,onSuccess:function(){specGroupSubmit(butt);},onError:function(){alert("具体错误，请看网页上的提示")}});
	$("#name").formValidator({onShow:"请输入规格组名称",onFocus:"至少1个字符,最多20个字符",onCorrect:"正确"}).inputValidator({min:1,max:20,onError:"你输入的品规格组名称不合法,请确认"});
	$("#remark").formValidator({onShow:"请输入备注",onFocus:"可以为空,最多100个字符",onCorrect:"正确"}).inputValidator({min:0,max:100,onError:"你输入的备注不合法,请确认"});	
	}

});
function specGroupSubmit(button){
  //var address= button.getAttribute('param');
	var address= $(button).attr('param');
	$.ajax({
        cache: true,
        type: "POST",
        url:domain+address,
        data:$('#specGroupUpdate').serialize(),
        async: false,
        error: function(request) {
            alert("Connection error");
        },
        success: function(result) {
        	if(result.success){
                layer.alert("保存成功", 1, function(){
            	parent.window.location.reload();
            	parent.layer.close(parent.pageii);
                })
            }else{
            	layer.alert(result.msg.message, 8);		
            }
        }
    });
}

   function addSpec(attra){
		var address= $(attra).attr('param');						
		  pageii=$.layer({
            type : 2,
            title: '规格->增加',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['850px', 400],                     
            iframe: {
                src : domain+address
            } 
     
	}); 
}

	function searchSpec() {
		var txt = $("#searchSp").val().trim();
		
		$("#editcheckboxs").find("span").show();
		$("#editcheckboxs").find("input[type='checkbox']").show();
		
		var treespans = $("#editcheckboxs").find("span");
		var checkboxs = $("#editcheckboxs").find("input[type='checkbox']");
		
		 $("#editcheckboxs").find("span").css("color","black");	
		 
		if(txt==null ||txt==""){
			return;
		}
		
		for (var i=0; i < treespans.length; i++) {
			var textval=$(treespans[i]).text();
			if (textval.indexOf(txt) >= 0) {
				$(treespans[i]).css("color","red");
			} else{
				$(checkboxs[i]).hide();
				$(treespans[i]).hide();
			}
		}
	}
	
	
   function deleteSpec(attra){
   	var address= $(attra).attr('param');
   	layer.confirm('您确认删除么?', function(){
   		$.ajax({
   	        cache: true,
   	        type: "get",
   	        url:domain+address,
   	        async: false,
   	        error: function(request) {
   	            alert("Connection error");
   	        },
   	        success: function(result) {
	        	if(result.success){
	                layer.alert("保存成功", 1, function(){
	                	parent.window.location.reload();
	                	parent.layer.close(parent.pageii);
	                })
	            }else{
	            	layer.alert(result.msg.message, 8);		
	            }
	        }
   	    });
   		
   		
   	 },function(){
   	 	
   	});
   	
   }

   
   
   function dataReset(button){
		clearForm('queryForm');
	}

	function clearForm(id) {
		var formObj = document.getElementById(id);
		if (formObj == undefined) {
			return;
		}
		for (var i = 0; i < formObj.elements.length; i++) {
			if (formObj.elements[i].type == "text") {
				formObj.elements[i].value = "";
			} else if (formObj.elements[i].type == "password") {
				formObj.elements[i].value = "";
			} else if (formObj.elements[i].type == "radio") {
				formObj.elements[i].checked = false;
			} else if (formObj.elements[i].type == "checkbox") {
				formObj.elements[i].checked = false;
			} else if (formObj.elements[i].type == "select-one") {
				formObj.elements[i].options[0].selected = true;
			} else if (formObj.elements[i].type == "select-multiple") {
				for (var j = 0; j < formObj.elements[i].options.length; j++) {
					formObj.elements[i].options[j].selected = false;
				}
			} else if (formObj.elements[i].type == "file") {
				var file = formObj.elements[i];
				if (file.outerHTML) {
					file.outerHTML = file.outerHTML;
				} else {
					file.value = ""; // FF(包括3.5)
				}
			} else if (formObj.elements[i].type == "textarea") {
				formObj.elements[i].value = "";
			}
		}
	}
	
