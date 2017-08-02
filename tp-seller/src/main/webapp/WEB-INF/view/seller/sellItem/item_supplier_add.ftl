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
    '/static/seller/js/layer/layer.min.js',
    '/static/seller/js/form.js',
    '/static/seller/js/item/item-supplier-add.js'] 
	css=[] >

    <div >
	     <form action="selectSupplier.htm"  method="post" class="jqtransform"   id='selectSupplierForm'  > 
	     <input type="hidden" id="supplierIds" value="${supplierIds}" />
	     <input type="hidden" name="addNewSupplierFlag" value="${addNewSupplierFlag}" />
	     <input type="hidden" id="skuSupplierList" name="skuSupplierList" />
	     <input type="hidden" id="skuId" name="skuId" value="${skuId}" />
	     <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td>编号</td>
                  <td><input type="text" name="supplierIdQuery" id="supplierIdQuery"  class="input-text lh25" size="10"></td>
                  <td>名称</td>
                  <td><input type="text" name="supplierNameQuery" class="input-text lh25" size="20"></td>
                  <td/><td/>
                  <td>供应商</td>
                  <td>
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="supplierTypeQuery" id="supplierTypeQuery" class="select"> 
                          	  <option value="">--全部--</option> 
                        	  <option value="Purchase">自营</option> 
			                  <option value="sell">代销</option> 
                        </select>
                        </div> 
                      </div> 
                    </span>
                  </td>
                  <td class="td_right"> <a href="#"><input class="btn btn82 btn_search" type="button" id="querySupplierBtn" value="查询" name="button" /></a></td>
                  <td class="td_right"> <a href="#"><input class="btn btn82 btn_search" type="button" id="saveSupplierBtn"   value="保存" name="button" /></a></td>
                </tr>
              </table>
            <div id="table" class="mt10">
       		 <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
               	   <th width="15"><input type="checkbox" id="chkall" /> </th>
                   <th>供应商编号</th>
                   <th>供应商名称</th>
                   <th>类型</th>
                   <!-- <th>备注</th>
                   <th>状态</th> -->
                </tr>
               <#if page.rows?default([])?size !=0>       
                <#list page.rows as sl>
                 <tr class="supplierListTr">
                   <td width="15">
                     <input type="checkbox" name="spId" value ="${sl.id}"/>
                     <input type="hidden" name="supplierType" value='${sl.supplierType}' />
                     <input type="hidden" name="supplierName" value='${sl.name}' />
                     <input type="hidden" name="supplierTypeName" value='${supplierTypes[sl.supplierType]}' />
                    </td>
                    <td >${sl.id}</td>
                    <td >${sl.name}</td>
                    <td >${supplierTypes[sl.supplierType]}</td>
                    <!--<td></td>
                    <td>${sl.status}</td> -->
                </tr>
                </#list>
                </#if>
                
              </table>
              <@pager  pagination=page  formId="selectSupplierForm"  /> 
       	 </div>
	     </form>            
	   </div>	
    </div>
</@sellContent>