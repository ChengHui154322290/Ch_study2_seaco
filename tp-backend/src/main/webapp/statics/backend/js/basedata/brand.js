var pageii;
$(function(){
	$('.brandeditbtn').live('click',function(){
		
		var id= $(this).attr('param');						
		  pageii=$.layer({
            type : 2,
            title: '品牌->编辑',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['700px', 500],                     
            iframe: {
                src : domain+'/basedata/brand/edit.htm?id='+id
            } 
        });
	}); 
	
 
//取消按钮
	$('.closebtn').on('click',function(){
		parent.layer.close(parent.pageii);
	});
	
$('.brandaddbtn').live('click',function(){				
		  pageii=$.layer({
            type : 2,
            title: '品牌->增加',
            shadeClose: true,
            maxmin: true,
            fix : false,  
            area: ['700px', 500],                     
            iframe: {
                src : domain+'/basedata/brand/add.htm'
            } 
        });
	}); 

//日志窗口
$('.journalReview').live('click',function(){
	var type="BrandDO";
	var id= $(this).attr('param');						
	pageii=$.layer({
		type : 2,
		title: '日志查询',
		shadeClose: true,
		maxmin: true,
		fix : false,  
		area: ['950px', 480],                     
		iframe: {
			src : domain+'/basedata/logs/view.htm?id='+id+"&type="+type
		} 
	});
}); 
if($("#datasubmit").is("input")){
var butt=$("#datasubmit");
$.formValidator.initConfig({submitButtonID:"datasubmit",debug:false,onSuccess:function(){brandSubmit(butt);},onError:function(){alert("具体错误，请看网页上的提示")}});
$("#name").formValidator({onShow:"请输入品牌中文名",onFocus:"至少1个字符,最多20个字符",onCorrect:"正确"}).inputValidator({min:1,max:40,onError:"你输入的品牌名称不合法,请确认"});
$("#nameEn").formValidator({onShow:"请输入品牌英文名",onFocus:"可以为空",onCorrect:"正确"}).inputValidator({min:0,max:50,onError:"你输入的品牌名称不合法,请确认"});
$("#remark").formValidator({onShow:"请输入备注",onFocus:"可以为空,最多50个字符",onCorrect:"正确"}).inputValidator({min:0,max:100,onError:"你输入的备注不合法,请确认"});	
}
});

function brandSubmit(button){
	var address= $(button).attr('param');
	$.ajax({
        cache: true,
        type: "POST",
        url:domain+address,
        data:$('#brandupdate').serialize(),
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
        		layer.alert("保存失败", 8);
        	}
        }
    });
}