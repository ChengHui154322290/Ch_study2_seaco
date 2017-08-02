var pageii;
$(function(){
	
	$('.taxRateeditbtn').live('click',function(){
		
		var id= $(this).attr('param');						
		  pageii=$.layer({
            type : 2,
            title: '税率->编辑',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['600px', 400],                     
            iframe: {
                src : domain+'/basedata/taxRate/edit.htm?id='+id
            } 
        });
	}); 
//取消按钮
	$('.closebtn').on('click',function(){
		parent.layer.close(parent.pageii);
	});
	
$('.taxRateaddbtn').live('click',function(){				
		  pageii=$.layer({
            type : 2,
            title: '税率->增加',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['600px', 400],                     
            iframe: {
                src : domain+'/basedata/taxRate/add.htm'
            } 
        });
	}); 

if($("#datasubmit").is("input")){
	var butt=$("#datasubmit");
	$.formValidator.initConfig({submitButtonID:"datasubmit",debug:false,onSuccess:function(){taxRateSubmit(butt);},onError:function(){alert("具体错误，请看网页上的提示")}});
	$("#rate").formValidator({autoModify:false,onShow:"请输入的税率（0-100）",onFocus:"只能输入0-100之间的数字",onCorrect:"恭喜你,你输对了"}).inputValidator({min:0,max:100,type:"value",onErrorMin:"你输入的值必须大于等于0",onError:"税率必须在0-100之间，请确认"});
	$("#dutiableValue").formValidator({onShow:"请输入完税金额,可以为空",onFocus:"输入在(0-2147483647)的整数",onCorrect:"正确"}).inputValidator({min:0,max:2147483647,type:"value",onErrorMin:"你输入的值必须在提示的区间内",onError:"你输入的值必须在提示的区间内，请确认"}).regexValidator({regExp:"^\\+?\\d*$",dataType:"string",onError:"整数格式不正确"});
	$("#code").formValidator({onShow:"请输入完税税号,可以为空",onFocus:"输入在字母,数字,或者为空",onCorrect:"正确"}).inputValidator({min:0,max:11,onError:"你输入的值长度必须在11位以内，请确认"}).regexValidator({regExp:"^[0-9a-zA-Z\s?]*$",dataType:"string",onError:"格式不正确"});
	$("#remark").formValidator({onShow:"请输入备注",onFocus:"可以为空,最多100个字符",onCorrect:"正确"}).inputValidator({min:0,max:100,onError:"你输入的备注不合法,请确认"});	
	}

});

function taxRateSubmit(button){
	var address= $(button).attr('param');
	$.ajax({
        cache: true,
        type: "POST",
        url:domain+address,
        data:$('#taxRateupdate').serialize(),
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