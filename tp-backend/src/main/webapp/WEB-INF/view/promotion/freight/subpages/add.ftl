<#include "/common/common.ftl"/>
<@backend title="" js=['/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/promotion/freight.js'] 
                	css=[] >
<div class="mt10">
	<div class="box">
		<div class="box_border">
            <div class="box_center pt10 pb10">
            	<form class="jqtransform" method="post" id="freightTemplateAdd">
            		<table class="form_table" border="0" cellpadding="0" cellspacing="0">
              			<tr>
							<td valign="middle" width="80">类型:</td>
							<td valign="middle" align="left">
								<input id="at5" type="radio" name="freightType" checked="checked" value="0"/>
								<label for="at5">国内</label>&nbsp&nbsp&nbsp
								<input id="at6" type="radio" name="freightType" value="1"/>
								<label for="at6" >海淘</label>
							</td>
						</tr>
						<tr>
							<td>模板名称:</td>
							<td>
								<input type="text" name="templateName" class="input-text lh25" size="20">
							</td>
						</tr>
						<tr id="freightType2">
							<td valign="middle">计算方式:</td>
							<td valign="middle" align="left">
								<input id="at5" type="radio" name="calculateMode" checked="checked" value="0"/>
								<label for="at5" >满包邮</label>&nbsp&nbsp&nbsp
								<input id="at6" type="radio" name="calculateMode" value="1"/>
								<label for="at6" >统一邮资</label>
							</td>
						</tr>
						<tr id="freightType1">
							<td valign="middle">计算方式:</td>
							<td valign="middle" align="left">
								<input id="at5" type="radio" name="calculateMode" checked="checked" value="0"/>
								<label for="at5" >满包邮</label>&nbsp&nbsp&nbsp
							</td>
						</tr>
						<tr id="calculateMode2">
							<td>设置明细:</td>
							<td>
								邮费<input style="margin-left:20px;margin-right:10px;" width="5" type="text" name="fee" value="" class="input-text lh25" size="5">元
							</td>
						</tr>
						<tr id="calculateMode1">
							<td>设置明细:</td>
							<td>
								满<input style="margin-left:10px;margin-right:10px;" width="5" type="text" name="fullBy" value="" class="input-text lh25" size="5">元包邮
								&nbsp&nbsp&nbsp邮费<input style="margin-left:10px;" width="5" type="text" name="feeFullBy" value="" class="input-text lh25" size="5">
							</td>
						</tr>
					</table>
            		<div id="div1_submit" style="text-align:center;">
						<input class="btn btn82 btn_save2" type="button" value="保存" param='/freight/add.htm' onclick="saveAdd(this)" />
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
		$("#freightType1").hide();
		$("input[name=freightType]").click(function(){
			var ind = parseInt($(this).val())+1;
			if(ind == 2) {
				$("#freightType2").hide();
				$("#freightType1").show();
			} else {
				$("#freightType1").hide();
				$("#freightType2").show();
			}
		});
		
		
		$("#calculateMode2").hide();
		$("input[name=calculateMode]").click(function(){
			$("#calculateMode1").hide();
			$("#calculateMode2").hide();
			
			var ind = parseInt($(this).val())+1;
			$("#calculateMode"+ind).show();
		});
	});
</script>