var pageii;
$(function(){
	
	$('.clearanceChanneleditbtn').live('click',function(){
		
		var id= $(this).attr('param');						
		  pageii=$.layer({
            type : 2,
            title: '通关渠道->编辑',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['600px', 400],                     
            iframe: {
                src : domain+'/basedata/clearanceChannels/edit.htm?id='+id
            } 
        });
	}); 
//取消按钮
	$('.closebtn').on('click',function(){
		parent.layer.close(parent.pageii);
	});
	
$('.clearanceChanneladdbtn').live('click',function(){				
		  pageii=$.layer({
            type : 2,
            title: '通关渠道->增加',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['600px', 400],                     
            iframe: {
                src : domain+'/basedata/clearanceChannels/add.htm'
            } 
        });
	}); 

if($("#datasubmit").is("input")){
	var butt=$("#datasubmit");
	$.formValidator.initConfig({submitButtonID:"datasubmit",debug:false,onSuccess:function(){clearanceChannelSubmit(butt);},onError:function(){alert("具体错误，请看网页上的提示")}});
	$("#code").formValidator({onShow:"请输入备注",onFocus:"最多100个字符",onCorrect:"正确"}).inputValidator({min:1,max:100,onError:"你输入的备注不合法,请确认"});	
	$("#name").formValidator({onShow:"请输入备注",onFocus:"最多100个字符",onCorrect:"正确"}).inputValidator({min:1,max:100,onError:"你输入的备注不合法,请确认"});	
	$("#remark").formValidator({onShow:"请输入备注",onFocus:"可以为空,最多100个字符",onCorrect:"正确"}).inputValidator({min:0,max:100,onError:"你输入的备注不合法,请确认"});	
	}

});

function clearanceChannelSubmit(button){
	//var address= button.getAttribute('param');
	var address= $(button).attr('param');
	$.ajax({
        cache: true,
        type: "POST",
        url:domain+address,
        data:$('#clearanceChannelupdate').serialize(),
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