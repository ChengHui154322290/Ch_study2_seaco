<table class="input tabContent">
	<tr>
		<td>
		<div>
			<span id="spanButtonPlaceHolder"></span><span style="margin-left:20px;"></span>
		</div>
		<div id="item-pictures">
		<#list picList as picture>
			<div class="item-picture">
				<img src="<@itemImageDownload code="${picture}" width="200" />" />
				<input name="picList" value="${picture}" />
				<span class="remove-btn" title="删除">删除</span>
				<span class="pre-btn" title="前移">前移</span>
				<span class="next-btn" title="后移">后移</span>
			</div>
		</#list>
 		</div>
		<div class="fieldset flash" style="display:none" id="fsUploadProgress">
			<span class="legend"></span>
		</div>
		</td>
	</tr>
</table>