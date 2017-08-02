<#include "/common/common.ftl"/> 
<@backend title="分销佣金比列管理" 
 js=[ '/statics/backend/js/layer/layer.min.js',
 	  '/statics/backend/js/layer/extend/layer.ext.js',
	  '/statics/backend/js/promotion/utils.js',
	  '/statics/backend/js/jqgrid/js/jquery.jqGrid.min.js',
	  '/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js',
	  '/statics/backend/js/promotion/topicPrice.js'] 
css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/backend/js/jqgrid/css/ui.jqgrid.css'] >
<form method="post" action="${domain}/promotion/topicprice/list.htm" id="skupriceform">
<div id="search_bar" class="mt10" style="width: 1100px;">
	<div class="box">
		<div class="box_border">
			<div class="box_top">
				<b class="pl15">商品列表</b>
			</div>
			<div class="box_center pt10 pb10">
				<table class="form_table" border="0" cellpadding="0" cellspacing="0" width="1100">
					<tr>
						<td>SPU名称:</td>
                  		<td><input type="text" name="spuName" value="${itemSku.spuName}"  class="input-text lh25" size="15"></td>
                  		<td>spu:</td>
                  		<td><input type="text" name="spu" value="${itemSku.spu}"  class="input-text lh25" size="15"></td>
                  		<td>商品名称:</td>
                  		<td>
                    		<input type="text" name="detailName" value="${itemSku.detailName}" class="input-text lh25" size="15">
                  		</td>
                  		<td>sku:</td>
                  		<td>
                   			<input type="text" name="sku" value="${itemSku.sku}" class="input-text lh25" size="15">
                 		</td>
						<td>发布状态</td>
						<td>
							<span class="fl">
		                      <div class="select_border"> 
		                        <div class="select_containers "> 
		                        <select name="status" class="select"> 
			                        <option value="">请选择</option> 
			                       	<#list statusList as status>
			                       		<option value="${status.value}" <#if status.value==itemSku.status>selected</#if> >${status.key}</option>
			                       	</#list>
		                        </select> 
		                        </div> 
		                      </div> 
		                    </span>
						</td>
					</tr>
				</table>
			</div>
			<div class="box_bottom pb5 pt5 pr10" style="border-top: 1px solid #dadada;">
				<div class="search_bar_btn" style="text-align: right;">
					<input type="button" value="查询" class="btn btn82 btn_search querypagebtn" name="button">
					<input type="button" value="设置价格" class="btn btn82 btn_save2 batchupdatepricebtn" name="button">
				</div>
			</div>
		</div>
	</div>
</div>
</form>
<table id="skulist" class="mt10 skulist scroll"></table>
<div id="gridpager" class="scroll"></div> 
</@backend>
