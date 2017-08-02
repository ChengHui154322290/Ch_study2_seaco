var pageii;
$(function(){
	$('.nationalIconeditbtn').live('click',function(){
		
		var id= $(this).attr('param');						
		  pageii=$.layer({
            type : 2,
            title: '产地信息->编辑',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['700px', 500],                     
            iframe: {
                src : domain+'/basedata/nationalIcon/edit.htm?id='+id
            } 
        });
	}); 
	
 
//取消按钮
	$('.closebtn').on('click',function(){
		parent.layer.close(parent.pageii);
	});
	
$('.nationalIconaddbtn').live('click',function(){				
		  pageii=$.layer({
            type : 2,
            title: '产地信息->增加',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['700px', 500],                     
            iframe: {
                src : domain+'/basedata/nationalIcon/add.htm'
            } 
        });
	}); 

if($( "#birds" ).is("input")){
$( "#birds" ).autocomplete({
	source: domain+'/basedata/brand/queryCountry.htm',
	minLength: 1,
	select: function( event, ui ) {
		  $("input[name='countryId']").remove();
		//  $("#conutrydiv").append(hidden);
		var hidden="<input type='hidden'  name='countryId' value='"+ui.item.id+"'/>"; 
       $("#conutrydiv").append(hidden);
	}
	});
    }

if($("#datasubmit").is("input")){
var butt=$("#datasubmit");
$.formValidator.initConfig({submitButtonID:"datasubmit",debug:false,onSuccess:function(){nationalIconSubmit(butt);},onError:function(){alert("具体错误，请看网页上的提示")}});
$("#remark").formValidator({onShow:"请输入备注",onFocus:"可以为空,最多50个字符",onCorrect:"正确"}).inputValidator({min:0,max:100,onError:"你输入的备注不合法,请确认"});	
$("#nameEn").formValidator({onShow:"请输入英文名称",onFocus:"可以为空,最多50个字符",onCorrect:"正确"}).inputValidator({min:0,max:100,onError:"你输入的备注不合法,请确认"});	
}
});

function nationalIconSubmit(button){
	var address= $(button).attr('param');
	$.ajax({
        cache: true,
        type: "POST",
        url:domain+address,
        data:$('#nationalIconupdate').serialize(),
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
