<#include "/supplier/pop_table/context.ftl"/>
<@backend title="添加商品" js=[] 
    css=[] >
    <div class="container" id="supplierPopTable" style="z-index: 19891099;width: 821px; background: none repeat scroll 0% 0% rgb(255, 255, 255);">
    <div class="box_center">
    	<form id="product_add_form" action="" method="post" enctype="multipart/form-data">
    		<table width="700" cellpadding="0" cellspacing="0" border="0" class="form_table pt15 pb15"> 
		    	<tr algin="center">
		          <td class="td_right">品牌</td>
		          <td>
			       	<select class="select" name="brandName" id="brandName" style="width: 140px;">
			       	    <option value="">请选择</option>
			            <#if categoryDOList?exists>
                             <#list categoryDOList as sBrand>
	                           <option value="${sBrand.brandId}">${sBrand.brandName}</option>
	                     	 </#list>
                  		</#if>
	                    </select>
	                     
		          </td>
		        </tr>
		        
		        <tr algin="center">
		          <td class="td_right">大类</td>
		          <td>
                     <select class="select" name="categoryDalei" id="supplierCategoryBig_1" style="width: 140px;">
                      	<option value="">请选择</option>  
                    </select>
                    
		          </td>
		        </tr>
		        
		        <tr algin="center">
		          <td class="td_right">中类</td>
		          <td>
		            <select class="select" name="supplierCategoryMid" id="supplierCategoryMid_1" style="width: 140px;">
		            <option value="">请选择</option>  
                    </select>
                    
		          </td>
		          
		        </tr>
		        
		        <tr algin="center">
		          <td class="td_right">小类</td>
		          <td>
		             <select class="select" name="supplierCategorySmall" id="supplierCategorySmall_1" style="width: 140px;">
		             <option value="">请选择</option>  
                    </select>
                   
		          </td>
		        </tr>
		        
		        <tr algin="center">
		          <td class="td_right">佣金</td>
		          <td>
		             <input type="text" name="commission" id="commission" class="_intnum input-text lh30" size="20">&nbsp;%
		          </td>
		        </tr>
		        
		        <tr algin="center">
		           <td colspan="2" align="center">
		             <input type="button" value="确定" class="ext_btn ext_btn_submit" id="contract_productpop_Confirm">&nbsp;&nbsp;
		             <input type="reset" class="ext_btn" id="contract_productpop_Cancel"  value="取消" >
		           </td>
		        </tr>
    	</table>
    	
    	<input type="hidden" id="supplierId" value="${supplierId!}" />
    	<input type="hidden"  name="brandId" id="brandId">
    	<input type="hidden"  name="minId" id="minId">
    	<input type="hidden"  name="smallId" id="smallId">
    	<input type="hidden"  name="bigId" id="bigId">
    </div>
</div>
</@backend>