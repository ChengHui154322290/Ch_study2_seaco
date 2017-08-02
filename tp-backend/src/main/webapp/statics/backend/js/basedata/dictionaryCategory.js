var pageii;
$(function(){
	
	$('.dictionaryCategoryeditbtn').live('click',function(){
		
		var id= $(this).attr('param');						
		  pageii=$.layer({
            type : 2,
            title: '类型->编辑',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['500px', 300],                     
            iframe: {
                src : domain+'/basedata/dictionary/category/edit.htm?id='+id
            } 
        });
	}); 
//取消按钮
	$('.closebtn').on('click',function(){
		parent.layer.close(parent.pageii);
	});
	
$('.dictionaryCategoryaddbtn').live('click',function(){				
		  pageii=$.layer({
            type : 2,
            title: '类型->增加',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['500px', 300],                     
            iframe: {
                src : domain+'/basedata/dictionary/category/add.htm'
            } 
        });
	});

if($("#datasubmit").is("input")){
	var butt=$("#datasubmit");
	$.formValidator.initConfig({submitButtonID:"datasubmit",debug:false,onSuccess:function(){dictionaryCategorySubmit(butt);},onError:function(){alert("具体错误，请看网页上的提示")}});
	$("#code").formValidator({onShow:"请输入类型code码",onFocus:"至少1个字符,最多20个字符",onCorrect:"正确"}).inputValidator({min:1,max:20,onError:"你输入的不合法,请确认"});
	$("#name").formValidator({onShow:"请输入类型名称",onFocus:"至少1个字符,最多20个字符",onCorrect:"正确"}).inputValidator({min:1,max:20,onError:"你输入类型名称名称不合法,请确认"});
	}

});

function dictionaryCategorySubmit(button){
	//var address= button.getAttribute('param');
	var address= $(button).attr('param');
	$.ajax({
        cache: true,
        type: "POST",
        url:domain+address,
        data:$('#dictionaryCategoryupdate').serialize(),
        async: false,
        error: function(request) {
            alert("Connection error");
        },
        success: function(data) {
        	if(data.resultInfo.type=="1"){
                layer.alert(data.resultInfo.message, 1, function(){
            	parent.window.location.reload();
            	parent.layer.close(parent.pageii);
                })
            	}else{
            		layer.alert(data.resultInfo.message, 8);		
            	}
        }
    });
}