<#include "/common/common.ftl"/>
<@backend title="" js=['/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/promotion/freight.js'] 
                	css=[] >
<div class="mt10">
	<div class="box">
		<div class="box_border">
            <div class="box_center pt10 pb10">
            	<form class="jqtransform" method="post" id="freightTemplateEdit">
            		<table class="form_table" border="0" cellpadding="0" cellspacing="0">
            		 	<input type='hidden' name='id' value='${freightTemplate.id}'>
              			<tr>
							<td valign="middle" width="80">类型:</td>
							<td valign="middle" align="left">
								<input id="internalRadio" type="radio" name="freightType" <#if freightTemplate.freightType=0 >checked</#if>  value="0"/>
								<label for="MBYRadio">国内</label>&nbsp&nbsp&nbsp
								<input id="externalRadio" type="radio" name="freightType" <#if freightTemplate.freightType=1 >checked</#if>  value="1"/>
								<label for="TYYZRadio" >海淘</label>
							</td>
						</tr>
						<tr>
							<td>模板名称:</td>
							<td>
								<input type="text" name="templateName" value="${freightTemplate.name}" class="input-text lh25" size="20">
							</td>
						</tr>
						<tr id="calModeTYYZ">
							<td valign="middle">计算方式:</td>
							<td valign="middle" align="left">
								<input id="MBYRadio" type="radio" name="calculateMode"  <#if freightTemplate.calculateMode=0 >checked</#if>  value="0"/>
								<label for="MBYRadio" >满包邮</label>&nbsp&nbsp&nbsp
								<input id="TYYZRadio" type="radio" name="calculateMode" <#if freightTemplate.calculateMode=1 >checked</#if>  value="1"/>
								<label for="TYYZRadio" >统一邮资</label>
							</td>
						</tr>
						<tr id="calModeMBY">
							<td valign="middle">计算方式:</td>
							<td valign="middle" align="left">
								<input id="MBYRadio" type="radio" name="calculateMode" checked="checked" value="0"/>
								<label for="MBYRadio" >满包邮</label>&nbsp&nbsp&nbsp
							</td>
						</tr>
						<tr id="calculateMode2">
							<td>设置明细:</td>
							<td>
								邮费<input style="margin-left:20px;margin-right:10px;" width="5" type="text" name="fee" value="${freightTemplate.postage}" class="input-text lh25" size="5">元
							</td>
						</tr>
						<tr id="calculateMode1">
							<td>设置明细:</td>
							<td>
								满<input style="margin-left:10px;margin-right:10px;" width="5" type="text" value="${freightTemplate.freePostage}" name="fullBy" value="" class="input-text lh25" size="5">元包邮
								&nbsp&nbsp&nbsp邮费<input style="margin-left:10px;" width="5" type="text" name="feeFullBy" value="${freightTemplate.postage}" class="input-text lh25" size="5">
							</td>
						</tr>
					</table>
            		<div id="div1_submit" style="text-align:center;">
						<input class="btn btn82 btn_save2" type="button" value="保存" param='/freight/edit.htm' onclick="upd(this)" />
    					<input class="btn btn82 btn_del closebtn" type="button" value="取消" name="button1" />
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
</@backend>

<script>
	$(function(){
		$("#calModeMBY").hide();
		$("input[name=freightType]").click(function(){
			var ind = parseInt($(this).val())+1;
			if(ind == 2) {
				$("#calModeTYYZ").hide();
				$("#calModeMBY").show();
			} else {
				$("#calModeMBY").hide();
				$("#calModeTYYZ").show();
			}
		});
	
		$("#calculateMode2").hide();
		$("input[name=calculateMode]").click(function(){
			$("#calculateMode1").hide();
			$("#calculateMode2").hide();
			
			var ind = parseInt($(this).val())+1;
			$("#calculateMode"+ind).show();
		});
		
		// 国内，满包邮
		if(0 == '${freightTemplate.calculateMode}' && 0 == '${freightTemplate.freightType}') {
			$("#MBYRadio").attr("checked", "true");
		} 
		// 国内，统一邮资
		if(1 == '${freightTemplate.calculateMode}' && 0 == '${freightTemplate.freightType}') {
			$("#TYYZRadio").attr("checked", "true");
			$("#calculateMode1").hide();
			$("#calculateMode2").show();
		}
		  
		if($("#internalRadio").val() == '${freightTemplate.freightType}')
		  $("#internalRadio").click();
		else
		  $("#externalRadio").click();
		
		
	});
</script>