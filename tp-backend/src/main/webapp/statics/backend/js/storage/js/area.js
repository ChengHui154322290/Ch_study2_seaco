$(function(){$('#table2').on('click', 'input', function(){
	var input = $(this);
	if($(this).is(":checked")){
		
		$.post("/ROOT/storage/warehouse/getarea.htm",{id:$(this).attr("inputid")}, function(data) {
			var o = data;
			var html = '<tr>';
			$.each(o, function(i){
				var td = '<td><input type="checkbox" inputid="'+ o[i].id +'" parentId="'+ this.parentId +'" value="'+ o[i].name +'" name="deliverAddr" class="aaa" id="deliveraddr" >'+ o[i].name +'</td>';
				html += td;
				/*
				$(td).on("click","input:checked",function(){
					$.post("/ROOT/storage/warehouse/getarea.htm",{id:$(this).val().substring(0,3)}, function(data2) {
						var d = JSON.parse(data2);
						var html = "<tr>";
						$.each(o, function(i){
							html += '<td><input type="checkbox" value="'+ d[i].name +'" name="deliverAddr" class="" id="deliveraddr" >'+ d[i].name +'</td>';
						});
						html += "</tr>";
						sh.after(html);
					});
					
				});
				*/
			});
			html += "</tr>";
			var tr = input.parents('tr');
			//$("#new").remove();
			tr.after( html );
			
		});
	} else {
		$("[inputid]").each(function(){
			if( $(this).attr('parentId') == input.attr('inputid') )
				$(this).parents('tr').remove();
		});
	}
});
	
});

function getCityArea(){
	$.post("/ROOT/storage/warehouse/getarea.htm",{id:$(this).val().substring(0,3)}, function(data2) {
		var d = JSON.parse(data2);
		var html = '<tr><td><input type="checkbox" value="'+ d.children +','+ d.name +'" name="deliverAddr" class="aaa" id="deliveraddr" onclick="">江西省</td></tr>';
		sh.after();
	});
}