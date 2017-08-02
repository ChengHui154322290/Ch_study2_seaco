<#include "/common/common.ftl"/>
<@backend title="" js=[	
    '/statics/backend/js/layer/layer.min.js',
    '/statics/backend/js/item/item-detail.js',
    '/statics/backend/js/item/item-supplier.js'] 
    
	css=[] >

    <div >
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
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
               	   <th width="15"><input type="checkbox" id="chkall" /> </th>
                   <th>供应商编号</th>
                   <th>供应商名称</th>
                   <th>类型</th>
                   <!--<th>备注</th>
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
                    <!-- <td width="200"><a href="edit.htm">[编辑]</a><a href="#">[日志]</a><a href="edit.htm">[查看]</a></td> -->
                </tr>
                </#list>
                </#if>
                
              </table>
              <@pager  pagination=page  formId="selectSupplierForm"  /> 
       	 </div>
	     </form>            
	   </div>	
    </div>
</@backend>