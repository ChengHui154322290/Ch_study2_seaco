/**
 * 
 * @param 喻文杰
 */
var uploader;
$(function() {
	 var  $list = $(".filelist"),
	   
		  ratio = window.devicePixelRatio || 1,

	      // 缩略图大小
	      thumbnailWidth = 110 * ratio,
	     
	      thumbnailHeight = 110 * ratio;
     
     
	 uploader = WebUploader.create({
	
	    // 选完文件后，是否自动上传。
	    auto: true,
	    // swf文件路径
	    swf: webUploader+'/js/Uploader.swf',
	
	    // 文件接收服务端。
	    server: workorder+'/customerservice/workorder/uploadFile',
	
	    // 选择文件的按钮。可选。
	    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
	    pick: '#filePicker',
	
	    chunked: true,
	    
	    resize: false,
	    
	    chunkRetry:2,//失败重试次数
	    
	    prepareNextFile :true,//默认值：false 是否允许在文件传输时提前把下一个文件准备好
	    
	    // 只允许选择图片文件。
	    accept: {
	        title: 'Images',
	        extensions: 'gif,jpg,jpeg,bmp,png',
	        mimeTypes: 'image/*'
	    },
	    fileNumLimit: 5, //文件个数限制
	    fileSizeLimit: 5*2 * 1024 * 1024,    //总共文件限制 10 M
	    fileSingleSizeLimit: 2 * 1024 * 1024    //单个文件最大限制 2 M
	});
	
	 //再文件加入队列之前执行方法
	 uploader.on( 'beforeFileQueued', function( file ) {
		 
		 var queued = uploader.getFiles("queued").length;//在队列中等待上传的文件
		 var complete = uploader.getFiles("complete").length;//已经上传成功的文件
		 
	 	if(complete+queued > 4){
	 		showError(".imageError","超出的文件无法上传(最多上传5个文件)");
	 		return false;
	 	} 
	 });
 
	// 当有文件添加进来的时候
	uploader.on( 'fileQueued', function( file ) {
		    var $li = $(
		            '<li id="' + file.id + '" class="file-item thumbnail">' +
		                '<img>' +
		                '<div class="info">'+file.name+'</div>' +
		            '</li>'
		            ),
		        $img = $li.find('img');
		        
	     
	     
		 $btns = $('<div id="remove_'+file.id+'" class="file-panel">' +
	             '<span class="cancel">删除</span></div>').appendTo( $li ),
		
		 $li.on( 'mouseenter', function() {
	     $("#remove_"+file.id).stop().animate({height: 30});
	 });

	 $li.on( 'mouseleave', function() {
	     $("#remove_"+file.id).stop().animate({height: 0});
	 });
     
     
 	//删除文件
	$btns.on( 'click', 'span', function() {
		$("#"+file.id).remove();
		removeUploadFile(file.id);
		uploader.removeFile(file,true);	
	});
 
 
	    // $list为容器jQuery实例
	   $($list).append( $li );
	    // 创建缩略图
	    // 如果为非图片文件，可以不用调用此方法。
	    // thumbnailWidth x thumbnailHeight 为 100 x 100
	    uploader.makeThumb( file, function( error, src ) {
	        if ( error ) {
	            $img.replaceWith('<span>不能预览</span>');
	            return;
	        }
	
	        $img.attr( 'src', src );
	    }, thumbnailWidth, thumbnailHeight );
	});
	// 文件上传过程中创建进度条实时显示。
	uploader.on( 'uploadProgress', function( file, percentage ) {
	    var $li = $( '#'+file.id ),
	        $progress = $li.find('.progress');
	        $percent = $li.find('.progress span');
	
	    // 避免重复创建
	    if ( !$percent.length ) {
	        $percent = $('<p class="progress"><span></span></p>')
	                .appendTo( $li )
	                .find('span');
	    }
		$percent.css("display","inline-block");
	    $percent.css( 'width', percentage * 100 + '%' );
	});
	
	// 文件上传成功，给item添加成功class, 用样式标记上传成功。
	uploader.on( 'uploadSuccess', function( file ) {
	     $( '#'+file.id ).append( '<span class="success"></span>' );
	});
	
	// 文件上传失败，显示上传出错。
	uploader.on( 'uploadError', function( file ) {
	    var $li = $( '#'+file.id ),
	        $error = $li.find('div.error');
	
	    // 避免重复创建
	    if ( !$error.length ) {
	        $error = $('<div class="error"></div>').appendTo( $li );
	    }
	
	    $error.text('上传失败');
	});
	
	// 完成上传完了，成功或者失败，先删除进度条。
	uploader.on( 'uploadComplete', function( file ) {
	    $( '#'+file.id ).find('.progress').remove();
	});

});

