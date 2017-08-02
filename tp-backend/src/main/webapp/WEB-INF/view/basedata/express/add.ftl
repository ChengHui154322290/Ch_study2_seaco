<#include "/common/common.ftl"/>
<@backend title="快递公司管理" 
js=[
'/statics/backend/js/formValidator/formValidator-4.0.1.min.js',
'/statics/backend/js/formValidator/formValidatorRegex.js',
'/statics/backend/js/formValidator/DateTimeMask.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/basedata/express.js',
'/statics/backend/js/form.js'
] 
css=['/statics/backend/js/formValidator/style/validator.css']>

<div id="forms" class="mt10">
	<div class="box">
		<div class="box_border"> 
			<form  acton="${domain}/basedata/express/save.htm" class="jqtransform" id="expressSaveForm" method="post">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="td_right">编号:</td>
						<td>由系统自动生成</td></tr>
					<tr>
						<td class="td_right">快递公司名称:</td>
						<td><input class="input-text lh25" type="text" id="name" name="name" size="20"/></td>
						<td><div id="nameTip" style="width:230px"></div></td>
					</tr>
					<tr>
						<td class="td_right">code:</td>
						<td><input class="input-text lh25" type="text" id="code" name="code" size="20"/></td>
						<td><div id="codeTip" style="width:230px"></div></td>
					</tr>
					<tr>
						<td class="td_right">排序值(sort_no)</td>
						<td><input class="input-text lh25" type="text" id="sortno" name="sortNo" size="20"/></td>
						<td><div id="sortnoTip" style="width:230px"></div></td>
					</tr>
					<tr><td colspan="2"><div style="color:red">(提示：快递公司名称和code均不能存在重复的值)</div></td></tr>
					<tr>
					   <td class="td_left">
							<div class="box_bottom pb5 pt5 pr10" style="border-top:0px solid #dadada;">
			                 <div class="search_bar_btn" style="text-align:right;">
			                   <input class="btn btn82 btn_save2" type="button" id='datasubmit' value="保存" param="saveexpress"/>
			                 </div>
			                </div>
	                  </td> 
                  <td class="td_rigth">
                    <div class="box_bottom pb5 pt5 pr10" style="border-top:0px solid #dadada;">
		                 <div class="search_bar_btn" style="text-align:right;">
		             	   <input class="btn btn82 btn_nochecked closebtn " type="button" value="取消" id="buttoncancel" / >
		                 </div>
		            </div>
                  </td>
               </tr>
				</table>
			</form>
		</div>
	</div>
</div>

</@backend>