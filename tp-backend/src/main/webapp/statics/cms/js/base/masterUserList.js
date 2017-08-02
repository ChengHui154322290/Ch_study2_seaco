var MASTER_USER_VIEW = domain + "/cms/masterUser/view/{id}";
var MASTER_USER_EXAMINE = domain + "/cms/masterUser/examine/{id}";
var MASTER_USER_TERMINATE = domain + "/cms/masterUser/terminate/{id}";

$(document).ready(
		function() {
			$("#cmmApplyStartTime").datetimepicker({
				showTime: false,
				showHour: false,
				showMinute: false,
				showButtonPanel: false
		    });
			$("#cmmApplyEndTime").datetimepicker({
				showTime: false,
				showHour: false,
				showMinute: false,
				showButtonPanel: false
		    });
			$("#search").on("click", function() {
				$("#pageNo").val(1);
				$("#contract_list_form").submit();
			});
			$("#perCount").on("change", function() {
				$("#pageNo").val(1);
				$("#contract_list_form").submit();
			});
			$("#nextPage").on(
					"click",
					function() {
						var currentPage = $("#currPage")[0].innerHTML;
						if (null == currentPage
								|| $.trim(currentPage).length == 0
								|| 0 > parseInt(currentPage)) {
							return;
						}
						var totalPage = $("#totalPage")[0].innerHTML;
						var nextPage = parseInt(currentPage) + 1;
						if (nextPage > parseInt(totalPage)) {
							layer.alert("已经是最后一页");
							return;
						}
						$("#pageNo").val(nextPage);
						$("#contract_list_form").submit();
					});
			$("#prePage").on(
					"click",
					function() {
						var currentPage = $("#currPage")[0].innerHTML;
						if (null == currentPage
								|| $.trim(currentPage).length == 0
								|| 0 > parseInt(currentPage)) {
							return;
						}
						var prePage = parseInt(currentPage) - 1;
						if (prePage < 1) {
							layer.alert("已经是第一页");
							return;
						}
						$("#pageNo").val(prePage);
						$("#contract_list_form").submit();
					});
			$("a[name='examine']").on("click", function() {
				var id = $(this).attr("param");
				if (null == id || $.trim(id).length == 0) {
					id = "0";
				}
				var url = MASTER_USER_EXAMINE.replaceAll("{id}", id);
				$.layer({
					type : 2,
					title : "",
					shadeClose : true,
					shadeClose : true,
					maxmin : false,
					area : [ '700px', 500 ],
					iframe : {
						src : url
					},
					end : function() {
						$("#contract_list_form").submit();
					}
				});
			});
			$("a[name='terminate']").on("click", function() {
				var id = $(this).attr("param");
				if (null == id || $.trim(id).length == 0) {
					id = "0";
				}
				var url = MASTER_USER_TERMINATE.replaceAll("{id}", id);
				$.ajax({ 
				     type:"POST", //表单提交类型 
				     url:url, //表单提交目标 
				     dataType:'html',
				     success:function(msg){
				    	 var data = eval("("+msg+")");
				    	 if(data.isSuccess){
				    		 $("#contract_list_form").submit();
				    	 } else {
				    		 layer.alert(data.data);
				    	 }
				     },
					error : function(data) {
						layer.alert("保存失败!");
					}
			    });
			});
			$("a[name='view']").on("click", function() {
				var id = $(this).attr("param");
				if (null == id || $.trim(id).length == 0) {
					id = "0";
				}
				var url = MASTER_USER_VIEW.replaceAll("{id}", id);
				$.layer({
					type : 2,
					title : "",
					shadeClose : true,
					maxmin : false,
					fix : true,
					area : [ '700px', 500 ],
					iframe : {
						src : url
					},
					end : function() {
						$("#contract_list_form").submit();
					}
				});
			});
		})

String.prototype.replaceAll = function(s1, s2) {
	return this.replace(new RegExp(s1, "gm"), s2);
}