<table class="input tabContent">
	<tr>
		<td>
		<div>
			<span id="spanButtonPlaceHolder"></span><span style="margin-left:20px;">可以上传 jpg;jpeg;png;bmp类型图片,图片长宽为800像素,最大1 MB 第一张默认为主图（点击查看图片）</span>
		</div>
		<div id="container" >
            <a class="ext_btn ext_btn_submit m10" id="pickfiles"   href="#" imagenameattribute="picList">
                <i class="glyphicon glyphicon-plus"></i>
                <span class="legend">添加图片</span>
            </a>
            <div class="main_left m10 span6" id="imguplod">
		  	   <#list picList as picture>
		  	   		<div class="sel_box item-picture" >
						<img src="${bucketURL}${picture}" onclick="showImg(this)"/>
						<input name="picList" value="${picture}" />
						<span class="remove-btn" title="删除">删除</span>
						<span class="pre-btn" title="前移">前移</span>
						<span class="next-btn" title="后移">后移</span>
					</div>
				</#list>
		  	   </div>
    	    </div>
        </div>
		</td>
	</tr>
</table>