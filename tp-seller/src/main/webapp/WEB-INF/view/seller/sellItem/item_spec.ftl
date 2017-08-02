<#include "/layout/inner_layout.ftl" />
<script type="text/javascript" charset="utf-8" src="/static/scripts/common/jquery-1.9.1.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/scripts/common/jquery.form.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/component/jqueryui/js/jquery-ui-1.9.2.custom.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/scripts/common/bootstrap.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/scripts/web/main.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/component/date/WdatePicker.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/scripts/web/common/common.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/scripts/web/common/validator.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/scripts/layer/layer.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/scripts/layer/extend/layer.ext.js"></script>

<script type="text/javascript" charset="utf-8" src="/static/assets/js/bootstrap.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/typeahead-bs2.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/jquery-ui-1.10.3.custom.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/jquery.ui.touch-punch.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/jquery.slimscroll.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/jquery.easy-pie-chart.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/jquery.sparkline.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/flot/jquery.flot.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/flot/jquery.flot.pie.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/flot/jquery.flot.resize.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/ace-elements.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/ace.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/assets/js/ace-extra.min.js"></script>

<@sellContent title="" js=[
	'/static/select2/js/select2.js',
	'/static/select2/js/select2Util.js',
	'/static/select2/js/select2_locale_zh-CN.js',
	'/static/seller/js/item/item-select2.js',
	'/static/seller/js/item/item-prd.js'
	] 
	css=['/static/seller/css/common.css','/static/select2/css/select2.css',
	     '/static/seller/css/style.css'
	] >
	<div class="box_border">
       <div class="box_center">
		 <#if specGroupList?default([])?size !=0>
			 <div class="tc">
			            条形码： <input type="text" class="input-text lh30" size="20" value="" id="barcode" maxlength=20  />
				<input type="button" id="chooseSpecBtn"  value="确定" class="ext_btn ext_btn_submit m10" />
			</div>
		   <table class="input">
            <#list specGroupList as g>
			   <tr class="">
				<th>
					<label>
						${g.specGroup.name}
					</label>
				</th>
				<td>
					<span class="">
						<#if g.specDoList?default([])?size !=0>
						    <select
								name="supplierId"  class="select2 specGroupSelect" style="width:250px; margin-left: 1px" >
									<option value=""  >--请选择规格--</option>
								<#list g.specDoList as s>
									<option specGroupId = "${g.specGroup.id}"  value="${s.id}" >${s.spec}</option>
								</#list>
							</select>
	        			</#if>
					</span>
				</td>
			  </tr>
		   </#list>
		   </table>
         <#else>
         	均码只能有一个prd
         </#if>
		</div>
		</div>
	
</@sellContent>		
		