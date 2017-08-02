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
    '/static/seller/js/item/item-detail.js',
    '/static/seller/js/item/item-supplier.js'] 
	css=[] >
	<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">供应商选择</h3>
    </div>
    <div class="panel-body">
	     <form action="selectSupplier.htm" method="post" class="jqtransform"   id='selectSupplierForm'  > 
	     <input type="hidden" name="hasXgSeller" value="${hasXgSeller}" />
	     <input type="hidden" id="supplierTypeQueryHidden" value="${supplierTypeQueryHidden}" />
	     <table class="form_table" border="0" cellpadding="0" cellspacing="0">
	     		<tr>
                  <td>类型</td>
                  <td>
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <!-- 选择商家(卖家) -->
                        <input type="hidden" id="hasXgSeller" value="${hasXgSeller}"/>
                        <select name="saleType" id= "selectSeller" class="select"> 
                        <#if "${hasXgSeller}"=="1">
                         <option value="1">商家</option> 
                        <#else>
                        	 <#if "${sellerType}"=="0">
                        	 	<option value="0" selected>西客商城</option>
                         		<option value="1">商家</option> 
                         	 <#elseif "${sellerType}"=="1">
                         		 <option value="0">西客商城</option>
                         		 <option value="1" selected >商家</option> 
                         	 <#else>
                         	    <option value="0">西客商城</option>
                         	    <option value="1">商家</option> 
                         	 </#if>	
                        </#if>
                        </select> 
                        </div> 
                      </div> 
                    </span>
                  </td>
                </tr>
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
                         <!--
                         <#if supplierTypes?exists>
			                <#list supplierTypes?keys as key> 
			                  <option value="${key}">${supplierTypes[key]}</option> 
			                </#list>
			            </#if>
                        </select>
                        --> 
                        </div> 
                      </div> 
                    </span>
                  </td>
                  <td class="td_right"> <a href="#"><input class="btn btn82 btn_search" type="button" id="querySupplierBtn" value="查询" name="button" /></a></td>
                  <td class="td_right"> <a href="#"><input class="btn btn82 btn_search" type="button"  id="confirmSuppliersBtn"   value="确定" name="button" /></a></td>
                </tr>
              </table>
            <div id="table" class="mt10">
       		 <div class="box span10 oh">
              
       	 </div>
	     </form>  
	         <div id="contentShow">
        
              </div          
	   </div>	
    </div>
</@sellContent>