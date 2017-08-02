<#if workorderInfo.logList??>
<div class="box_center">
	<table width="100%" cellspacing="0" cellpadding="0" border="0" class="list_table">
		<tbody>
			<#list workorderInfo.logList as log>
			<#if log_index!=0>
			<tr class="tr">
			<#if log.userType==1>
				<td style="background-color:#d3d3d3">西客商城客服[${log.cnUserType}]回复</td>
				<td style="background-color:#d3d3d3">${log.createDate?string('yyyy-MM-dd HH:mm:ss')}</td>
			</#if>
			<#if log.userType==3>
				<td style="background-color:#b0c4de">${log.userName}[${log.cnUserType}]回复</td>
				<td style="background-color:#b0c4de">${log.createDate?string('yyyy-MM-dd HH:mm:ss')}</td>
			</#if>
			</tr>
			<tr class="tr">
				<td colspan="2"><#if log.context??>${log.context}</#if><br>
				<#if log.imgsUrl??>	
					<#list log.imgsUrl?split(",") as imgUrl>
			                  <img src="<@itemImageDownload code='${imgUrl!}' width="80" />" data-pic="<@itemImageDownload code='${imgUrl!}' width="640" />">
					</#list>
				</#if></td>
			</tr>
			</#if>
			</#list>
		</tbody>
	</table>
</div>
</#if>
<script type="text/javascript" >
	
	$(function(){
	 $('[data-pic]').click(function(){
		 var src = $(this).attr('data-pic');
		 var pic = $('<img>').attr('src', src);
		 var div = $('<div>').css({
			 'position' : 'fixed',
			 'left' : 0,
			 'top' : 0,
			 'width' : '100%',
			 'height' : '100%',
			 'line-height' : $(top).height() + 'px',
			 'text-align' : 'center',
			 'background' : 'rgba(0,0,0,.5)'
		 }).click(function(){
			 $(this).remove();
		 }).append(pic);
		 $(top.document.body).append(div);
	 })
});

</script>