    <#include "/common/common.ftl"/>
    <!-- Page Head -->
    <h2>新增菜单</h2>
    <form id="addSysMenuForm" method="post">
        <p>
          <label>菜单名称</label>
          <input type="text" name="name" id="name" class="text-input small-input">
          <span class="input-notification information png_bg">请输入菜单名称,不可重复</span>
        </p>
         <p>
          <label>菜单URL</label>
          <input type="text" name="url" class="text-input small-input">
          <span class="input-notification information png_bg">请输入菜单的跳转url,如果是父菜单可以不填</span>
        </p>
        <p>
          <label>父菜单 <span class="input-notification information png_bg">如果不选,则是最顶级菜单</span></label>
          <span class="sysMenu-span">
          	<select style="margin-bottom: 10px;" class="small-input">
	           	<option value="">请选择</option>
	          	<#list parentList as menu>
	           	 <option data-category="${menu.category!}" value="${menu.id}">${menu.name}</option>
	            </#list>
	          </select>
          </span>
          <input type="hidden" id="parentId" name="parentId" value=""/>
          <input type="hidden" id="index" name="index" value=""/>
          <input type="hidden" id="category" name="category" value=""/>
        </p>
        <p>
          <input type="button" value="保    存" class="button addSysMenuBtn">
          <input type="reset" value="重    置" class="button addSysMenuReset">
        </p>
        <div class="clear"></div>
      </form>

   
 <script>
 	$(function(){
 		$(".sysMenu-span select").on("change",function(){
 			seChange($(this));
 		});
 		
 		$(".addSysMenuBtn").click(function(){
 			var name = $("#name").val();
 		
 			if(null == name || "" == name){
 				$("#name").next().removeClass("information").addClass("error").html("请输入用户名");
 				return;
 			}
 			
 			var index = 1;
 			var parentId = "";
 			var category = "";
 			
 			var $selectObj = $(".sysMenu-span select:last");
 			if((null == $selectObj.val() || "" == $selectObj.val())&&$selectObj.prev().length>0){
 				$selectObj = $selectObj.prev();
 			}
 			
 			
 			if($(".sysMenu-span select:last[value!='']").length>0){
 				parentId = $selectObj.val();
 				if(null!=$selectObj.val()&&""!=$selectObj.val()) index = $(".sysMenu-span").children().length+1;
 				else index = $(".sysMenu-span").children().length;
 				
 				if(parentId.length>0) category = $selectObj.find("option:selected").attr("data-category")+"/"+parentId;
 			}
 			$("#category").val(category);
 			$("#index").val(index);
 			$("#parentId").val(parentId);
 			 $.ajax({
                    url: "${base}/kaifa/addSysMenu",
                    data: $("#addSysMenuForm").serialize(),
                    type:'POST',
                    dataType:'json',
                    success : function(result) {
                    	if (result.success) {
                    		alert("添加成功");
							$("#link_menu").click();
						} else {
							alert(result.msg);
						}
					}
                });
 		
 		});
 	
 	});
 	
 	function seChange(obj){
 		var $obj = obj;
		var parentId = $obj.val();
		var $innerObj = null;
		var optionObjs = null;
		
		$obj.nextAll().remove();
		
		if(null == parentId || "" == parentId) return;
		
		$.post("${base}/kaifa/getSysmenuList",{sysMenuId:parentId},function(result){
			if(result.success){
				var menuList = result.obj.sysMenuList;
				if(null != menuList && menuList.length>0){
					optionObjs = "<option value=''>请选择</option>";
					$.each(menuList,function(i,n){
						optionObjs += "<option data-category='"+n.category+"' value='"+n.id+"'>"+n.name+"</option>";
					});
					$innerObj = $('<select class="small-input" style="display:block;margin-bottom: 10px;"></select>');
	 				$innerObj.append(optionObjs);
	 				$obj.parent().append($innerObj);
	 				
	 				$innerObj.on("change",function(){
			 			seChange($(this));
			 		});
	 				
				}
			}else{
				alert(result.msg);
			}
		},"json");
 	}
 </script>