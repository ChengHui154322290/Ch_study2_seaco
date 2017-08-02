<#include "/common/common.ftl"/>
<@backend title="" js=['/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
	'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
	'/statics/backend/js/jqgrid/js/jquery.jqGrid.min.js',
	'/statics/backend/js/user/register-point.js',
	'/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js']
    css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/backend/js/jqgrid/css/ui.jqgrid.css']  >
<div class="mt10">
	<div class="box">
		<div class="box_border">
            <div class="box_center pt10 pb10">
            	<form class="jqtransform" method="post" id="pointEdit">
            		<table class="form_table" border="0" cellpadding="0" cellspacing="0">
            		 	<input type='hidden' name='id' value='${point.id}'>
              			<tr>
							<td>积分数:</td>
        					<td><input type="text" name="point" class="input-text lh25" value='${point.point}' size="5"/></td>
						</tr>
						<tr>
							<td width="69">平台:</div></td>
       						<td valign="middle" align="left">
								<input id="at5" type="radio" data-v="${platform}" name="platform" <#if point.platForm==0> checked="checked"</#if> value="0"/>
								<label for="at5" >pc</label>&nbsp&nbsp&nbsp
								<input id="at6" type="radio" data-v="${platform}" name="platform" <#if point.platForm==1> checked="checked"</#if> value="1"/>
								<label for="at6" >app</label>
								<input id="at6" type="radio" data-v="${platform}" name="platform" <#if point.platForm==2> checked="checked"</#if> value="2"/>
								<label for="at6" >wap</label>
							</td>
        				</tr>
						<tr>
							<td width="69">固定期限:</div></td>
       						<td valign="middle" align="left">
								<input id="at5" type="radio" name="isExpiry" <#if point.deadLineFlag==0> checked="checked"</#if> value="0"/>
								<label for="at5" >无</label>&nbsp&nbsp&nbsp
								<input id="at6" type="radio" name="isExpiry" <#if point.deadLineFlag==1> checked="checked"</#if> value="1"/>
								<label for="at6" >有</label>
							</td>
        				</tr>
        				<tr>
        					<td id="expiryTitle">生效时间:</td>
        					<td id="expiryTime" colspan="3">
         						<input type="text" name="createBeginTime" id="createBeginTime" data-v="${createBeginTime}" value="<#if point.beginTime??>${point.beginTime?string("yyyy-MM-dd")}</#if>" class="input-text lh25" size="20">
            					<span>～</span>
            					<input type="text" name="createEndTime" id="createEndTime" data-v="${createEndTime}" value="<#if point.endTime??>${point.endTime?string("yyyy-MM-dd")}</#if>" class="input-text lh25" size="20">
        					</td>
     					</tr>
     					<tr>
							<td width="69">是否有效:</div></td>
       						<td valign="middle" align="left">
								<input id="at5" type="radio" name="state" <#if point.state==true> checked="checked"</#if> value="1"/>
								<label for="at5" >有效</label>&nbsp&nbsp&nbsp
								<input id="at6" type="radio" name="state" <#if point.state==false> checked="checked"</#if> value="0"/>
								<label for="at6" >无效</label>
							</td>
        				</tr>
					</table>
            		<div id="div1_submit" style="text-align:center;">
						<input class="btn btn82 btn_save2" type="button" value="保存" param='/user/point/edit.htm' onclick="upd(this)" />
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
		
		if(0 == '${point.deadLineFlag}') {
			$("#expiryTime").hide();
			$("#expiryTitle").hide();
		} else {
			$("#expiryTime").show();
			$("#expiryTitle").show();
		}
		
		$("input[name=isExpiry]").click(function(){
			var ind = parseInt($(this).val())+1;
			if(ind == 2) {
				$("#expiryTime").show();
				$("#expiryTitle").show();
			} else {
				$("#expiryTime").hide();
				$("#expiryTitle").hide();
			}
		});
	});
</script>