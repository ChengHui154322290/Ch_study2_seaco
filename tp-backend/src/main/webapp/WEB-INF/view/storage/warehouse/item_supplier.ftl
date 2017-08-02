<#include "/common/common.ftl"/>
<@backend title="供应商查询" js=[	
'/statics/backend/js/layer/layer.min.js',

'/statics/backend/js/storage/js/item_supplier.js'
] 
css=[] >
    <div >
	   <div>	  
	     <form action="selectSupplier.htm" method="post" class="jqtransform"   id='selectSupplierForm'  > 
	     <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td>编号</td>
                  <td><input type="text" name="supplierIdQuery" id="supplierIdQuery" value="${supplierIdQuery}"  class="input-text lh25" size="10"></td>
                  <td>名称</td>
                 <td><input type="text" name="supplierNameQuery" id="supplierNameQuery" value="${supplierNameQuery}" class="input-text lh25" size="20"></td>
                  <td class="td_right"> <a href="#"><input class="btn btn82 btn_search" type="submit" id="querySupplierBtn" value="查询" name="button" /></a></td>
                  <td class="td_right"> <a href="#"><input class="btn btn82 btn_search" type="button"  id="confirmSuppliersBtn"   value="确定" name="button" /></a></td>
                </tr>
              </table>
            <div id="table" class="mt10">
       		 <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table" id="table_list_supplier">
                <tr>
               	   <th width="15"></th> 
                   <th>供应商编号</th>
                   <th>供应商名称</th>
                   <th>类型</th>
                   <th>备注</th>
                   <th>状态</th>
                </tr>
               <#if page.rows?default([])?size !=0>       
                <#list page.rows as sl>
                 <tr class="supplierListTr">
                   <td width="15">
                     <input type="radio" name="supplierId" value ="${sl.id}"/> 
                     <input type="hidden" name="supplierType" value='${sl.supplierType}' />
                     <input type="hidden" name="supplierName" value='${sl.name}' />
                     <input type="hidden" name="supplierTypeName" value='${supplierTypes[sl.supplierType]}' />
                    </td>
                    <td >${sl.id}</td>
                    <td  class="supplierName">${sl.name}</td>
                    <!--
                    <td >${supplierTypes[sl.supplierType]}</td>
                    -->
                    <td >${supplierTypes[sl.supplierType]}</td>
                    <td></td>
                    <td>${sl.status}</td>
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