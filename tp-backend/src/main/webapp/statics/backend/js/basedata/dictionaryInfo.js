var pageii;
$(function(){
	
	$('.dictionaryInfoeditbtn').live('click',function(){
		
		var id= $(this).attr('param');						
		  pageii=$.layer({
            type : 2,
            title: '类型->编辑',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['500px', 300],                     
            iframe: {
                src : domain+'/basedata/dictionary/info/edit.htm?id='+id
            } 
        });
	}); 
//取消按钮
	$('.closebtn').on('click',function(){
		parent.layer.close(parent.pageii);
	});
	
$('.dictionaryInfoaddbtn').live('click',function(){				
		  pageii=$.layer({
            type : 2,
            title: '类型->增加',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['500px', 300],                     
            iframe: {
                src : domain+'/basedata/dictionary/info/add.htm'
            } 
        });
	}); 


if($("#datasubmit").is("input")){
	var butt=$("#datasubmit");
	$.formValidator.initConfig({submitButtonID:"datasubmit",debug:false,onSuccess:function(){dictionaryInfoSubmit(butt);},onError:function(){alert("具体错误，请看网页上的提示")}});
	$("#name").formValidator({onShow:"请输入类型名称",onFocus:"至少1个字符,最多25个字符",onCorrect:"正确"}).inputValidator({min:1,max:25,onError:"你输入类型名称名称不合法,请确认"});
	}

});

function dictionaryInfoSubmit(button){
	var address= $(button).attr('param');
	$.ajax({
        cache: true,
        type: "POST",
        url:domain+address,
        data:$('#dictionaryInfoupdate').serialize(),
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
