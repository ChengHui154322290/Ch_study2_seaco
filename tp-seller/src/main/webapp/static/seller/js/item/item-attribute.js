$(function(){
	
	$('#smallIdSel').change(function(){
		var val = $(this).val();
		if(val==undefined||val==null||val.length==0){
			return;
		}
		categoryChange(val, $("#attributeId"));
	});
	
	function categoryChange(id,target){
		var url = domain+"/seller/item/getAttributeList.htm";
		$.ajax({
			type : "get",
			url : url,
			data : {catId:id},
			async : false,
			success : function(data){
				if(data){
					var jsonObj=eval("("+data+")");
					target.html('');
					$.each(jsonObj,function(i,n){
						var attribute = n.attribute;
						var allowMultiSelect = attribute.allowMultiSelect;
						var isRequired = attribute.isRequired;
						var attributeValues = n.attributeValues;
						var div=$("<div />");
						div.attr("class","attrItem");
						div.attr("style","margin-bottom:5px;");
						var hidden = $("<input />");
						hidden.attr("type","hidden");
						hidden.attr("name","attributeId");
						hidden.val(attribute.id);
						div.append(hidden);
						var span = $("<span />");
						span.html(attribute.name);
						if(isRequired){
							var span1 = $("<span />");
							span1.attr("class",'requiredField');
							span1.html("(必选): ");
							span.append(span1);
						}else{
							var span1 = $("<span />");
							span1.html(": ");
							span.append(span1);
						}
						div.append(span);
						if(allowMultiSelect){
							$.each(attributeValues,function(j,valueObject){
								var isSelect = valueObject.isSelect;
								var id = valueObject.id;
								var name = valueObject.name;
								var checkbox = $("<input />");
								checkbox.attr("class","attributeValueClass");
								checkbox.attr("type","checkbox");
								checkbox.attr("isRequired",isRequired);
								checkbox.attr("name","attributeValueId");
								checkbox.val(id);
								if(isSelect==1){
									checkbox.attr(" checked "," checked ");
								}
								div.append(checkbox).append(name+" ");
							});
						}else{
							var sel = $("<select />");
							sel.attr("name","attributeValueId");
							sel.attr("class","select");
							sel.attr("isRequired",isRequired);
							if(!isRequired){
								var opt = $("<option />");
								opt.html("请选择");
								opt.val("");
								sel.append(opt);
							}
							$.each(attributeValues,function(j,valueObject){
								var isSelect = valueObject.isSelect;
								var id = valueObject.id;
								var name = valueObject.name;
								var opt = $("<option />");
								opt.val(id);
								opt.html(name);
								if(isSelect==1){
									opt.attr("selected","selected");
								}
								sel.append(opt);
							
							});
							div.append(sel);
						}
						target.append(div);
					});
				}
			}
		});
	}
	
	
	
});