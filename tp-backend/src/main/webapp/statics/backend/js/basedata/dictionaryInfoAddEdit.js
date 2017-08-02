var pageii;
$(function(){
	
	$(".select2").select2();
	$(".select2").css("margin-left","1px");
	
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


	$("#datasubmit").on('click', function(){
		var name = $("#name").val();
		var nameLen = name.length;
		if($.trim(name) =="" || nameLen < 1 || nameLen > 25){
			alert("类型名称非法,请重新输入：至少1个字符,最多25个字符");
			return;
		}
		var code = $("#code").val();
		if(code == "c1001"){
			var customsId = $("#customsUnitId").val();
			if(customsId == 0){
				alert("请选择海关计量单位");
				return;
			}
		}
		var butt=$("#datasubmit");
		dictionaryInfoSubmit(butt);
	});

	$("#code").change(function(){
		var code = $(this).val();
		if(code != "c1001"){
			$(".customsunit").hide();
		}else{
			$(".customsunit").show();
			queryCustomsUnitData();
		}
	});

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

function queryCustomsUnitData(){
	$.ajax({
		url : 'customsUnit',
		type : "get",
		cache : false,
		success : function(data) {
			if (data.success) {// 成功
				var list = data.data;
				var html = [];
				html.push('<option value=0 selected=selected>请选择</option>');
				var len = data.data.length;
				for(var i = 0; i< len; i++){
					var option = "<option " + " value=" + data.data[i].id + ">" + data.data[i].name + "</option>";
					html.push(option);
				}
				$("#customsUnitId").html(html);
			} else {// 失败
				layer.alert(data.msg.message, 8);
			}
		}
	});
}
