 <#include "/common/common.ftl"/>
 <!doctype html>
 <html lang="zh-CN">
 <head>
   <meta charset="UTF-8">
   <link rel="stylesheet" href="${domain}/statics/backend/css/common.css">
   <link rel="stylesheet" href="${domain}/statics/backend/css/main.css">
   <link rel="stylesheet" href="${domain}/statics/backend/js/webuploader/webuploader.css">
   <script src="${domain}/statics/backend/js/jquery.min.js"></script>
   <script src="${domain}/statics/backend/js/webuploader/webuploader.min.js"></script>
   <script>var domain = "${domain}";</script>
 </head>
 <body>
 <div class="main_left m10 span6">
 	<div class="box pr5">
	    <div class="box_border">
	      <div class="box_top">
	        <div class="box_top_l fl"><b class="pl15">webuploader文件上传</b></div>
	      </div>
	      <div class="box_center">
	      	<div id="thelist" class="uploader-list"></div>
		    <div class="btns">
		        <div id="picker">选择文件</div>
		    </div>
	      </div>
	    </div>
	</div>
</div>
 	<script>
 	$(function(){
 		var uploader = WebUploader.create({
 			auto:true,
		    // swf文件路径
		    swf: domain + '/statics/backend/js/webuploader/Uploader.swf',
		
		    // 文件接收服务端。
		    server: domain+'/item/uploadItemFiles.htm',
		
		    // 选择文件的按钮。可选。
		    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
		    pick: {id:'#picker',multiple:false},
		
		    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
		    resize: false,
		    // 只允许选择图片文件。
		    accept: {
		        title: 'Images',
		        extensions: 'gif,jpg,jpeg,bmp,png',
		        mimeTypes: 'image/*'
		    }
		});
		
		// 当有文件添加进来的时候
		uploader.on( 'fileQueued', function( file ) {
		    var $li = $(
		            '<div id="' + file.id + '" class="file-item thumbnail">' +
		                '<img>' +
		            '</div>'
		            ),
		   $img = $li.find('img');
		
		
		    // $list为容器jQuery实例
		    var $list = $("#thelist");
		    $list.html( $li );
		
		    // 创建缩略图
		    // 如果为非图片文件，可以不用调用此方法。
		    // thumbnailWidth x thumbnailHeight 为 100 x 100
		    uploader.makeThumb( file, function( error, src ) {
		        if ( error ) {
		            $img.replaceWith('<span>不能预览</span>');
		            return;
		        }
		
		        $img.attr( 'src', src );
		    }, 200, 200 );
		});
		
		uploader.on( 'uploadSuccess', function( file,response ) {
			alert(response.path);
		    alert(response.type);
		});
		
		uploader.on( 'uploadError', function( file ) {
		
		});
	
				
 	});
 	
 	</script>
 </body>
 </html>