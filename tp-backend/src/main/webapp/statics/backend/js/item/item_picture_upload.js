var fileCountlimit = 10;
var hasUploadCount = 0;
$(function(){
	var bucketname= $(":hidden[name=bucketname]").val();
	var bucketURL= $(":hidden[name=bucketURL]").val();
    var imagenameattribute=$('[imagenameattribute]').attr('imagenameattribute');
	QiniuUpload(true,imagenameattribute,bucketname,bucketURL,"pickfiles","container","imguplod",10,
		function(){
			//$("#imguplod").html('');
		},
		function(name,path,key){
			var allItems = $('#imguplod');
			var img = $("<img />");
			setTimeout(function(){img.attr('src',path);}, 1000);
			setTimeout(function(){img.attr('onclick','showImg(this)');}, 1000);
			
			var container = $("<div />");
			container.addClass("item-picture");
			container.append(img);
			container.hover(function(){
				container.find('.remove-btn').fadeIn();
				container.find('.pre-btn').fadeIn();
				container.find('.next-btn').fadeIn();
			},function(){
				container.find('.remove-btn').fadeOut();
				container.find('.pre-btn').fadeOut();
				container.find('.next-btn').fadeOut();
			});
			
			var input = $("<input />");
			input.val(key);
			input.attr('name',name);
			container.append(input);
			
			var deleteBtn = $("<span />");
			deleteBtn.html("删除");
			deleteBtn.attr('title','删除');
			deleteBtn.addClass("remove-btn");
			container.append(deleteBtn);
			deleteBtn.click(function(){
				container.remove();
				fileCountlimit++;
				swfu.setFileUploadLimit(fileCountlimit);
			});
			
			var pre = $("<span />");
			pre.html("前移");
			pre.attr('title','前移');
			pre.addClass('pre-btn');
			container.append(pre);
			pre.click(function(){
				$(container).insertBefore($(container).prev());
			});
			
			var next = $("<span />");
			next.html("后移");
			next.attr('title','后移');
			next.addClass('next-btn');
			container.append(next);
			next.click(function(){
				$(container).insertAfter($(container).next());
			});
			allItems.append(container);
	});
});	