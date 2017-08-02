<#include "/common/common.ftl"/>
<@backend title="导出兑换码" js=['/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/promotion/promotionAddExchangeCode.js',
'/statics/cms/js/common/time/js/jquery-ui-1.9.2.custom.js',
'/statics/cms/js/common/time/js/jquery-ui-timepicker-addon.js',
'/statics/cms/js/common/time/js/jquery-ui-timepicker-zh-CN.js']
                	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.css',
					'/statics/cms/js/common/time/css/jquery-ui-timepicker-addon.css',
					'/statics/cms/js/common/time/css/jquery-ui-1.8.17.custom.css'] >

<div class="mt10">
        <div class="box">
          <div class="box_border">
            <div class="box_center pt10 pb10">
                <form id="inputForm" action="" method="post" enctype="multipart/form-data">
				<input type="hidden" name="actId" value="${actiId}"  class="actiId">
               <table class="form_table" border="0" cellpadding="0" cellspacing="0">
               <#-- <tr>
                 &lt;#&ndash; <input type='hidden' name='level' value='${level}'>
                  <input type='hidden' name='parentId' value='${parentId}'>  
                  <input type='hidden' name='parentCode' value='${parentCode}'> &ndash;&gt;
                     
					<td>类别:</td>
					<td>
					&lt;#&ndash;${levelName}&ndash;&gt;
					</td>
				</tr>
				<tr>
					<td>上级类别:</td>
					<td>
					&lt;#&ndash;	${ansNameStr}&ndash;&gt;
					</td>
				</tr>	-->
				<tr>
					<#--<td>* 类别名称</td>-->
					<#--<td>
						<input type="text" name="name" class="input-text lh25" size="20"  maxlength="50"/>
					</td>-->
                        <td class="td_left"><span class="requiredField">*</span>生成码开始日期:</td>
                        <td class="">
                            <input type="text" readonly="readonly" name="startTime" value=""
                                   class="input-text lh30 startdate" size="20">
                        </td>

                        <td class="td_right"><span class="requiredField">*</span>生成码结束日期:</td>
                        <td class="">
                            <input type="text" readonly="readonly" name="endTime" value=""
                                   class="input-text lh30 enddate" size="20">
                        </td>
				</tr>
				<#--<tr>
					<td>备注</td>
					<td>
						<input type="text" name="remark" class="input-text lh25" size="20"  maxlength="250"/>
					</td>
				</tr>-->
				<tr>
				<td valign="middle">
					状态:
				</td>
				<td valign="middle" align="left">
                    <input id="at6" type="radio" name="status" checked="checked" value="0"/>
                    <label for="at6" >未使用</label>
					<input id="at5" type="radio" name="status" value="1"/>
					<label for="at5" >已使用</label>
				</td>
				</tr>

			</table>
			
		<div id="div1_submit" style="text-align:center;">
	
			<input class="btn btn82 btn_save2 exportData2" type="button" value="导出" param='/topicCoupon/export.htm' onclick="uploadExcel()" />
    		<input class="btn btn82 btn_del closebtn" type="button" value="关闭" name="button1" />
		</div>
			</form>
		</div>
	</div>
</div>
</div>

</@backend>