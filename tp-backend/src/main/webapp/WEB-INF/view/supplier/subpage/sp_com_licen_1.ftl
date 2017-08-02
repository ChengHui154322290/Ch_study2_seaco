<table cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15">
    <tbody>
        <tr>
            <td>
        		<div id="container_businessLicense" class="ext_btn ext_btn_submit">
                    <a class="ext_btn ext_btn_submit" id="pickfiles_businessLicense"   href="#"  imagenameattribute="businessLicense">上传营业执照</a>
                </div>
            </td>
            <td>
        		<div id="container_taxregist" class="ext_btn ext_btn_submit">
                    <a class="ext_btn ext_btn_submit" id="pickfiles_taxregist"   href="#"  imagenameattribute="taxregist">上传税务登记证</a>
                </div>
            </td>
            <td>
        		<div id="container_organize" class="ext_btn ext_btn_submit">
                    <a class="ext_btn ext_btn_submit" id="pickfiles_organize"   href="#"  imagenameattribute="organize">上传组织机构代码</a>
                </div>
            </td>
         </tr>
         <tr>
            <td style="text-align: center; ">
               <div class="main_left m10 span6">
				  	<div class="sel_box" id="imguplod_businessLicense"  style="width: 200px;height: 200px"><#if supplierAttacheVO.businessLicense??><img class="img_big add_more_goods" style="width: 200px;height: 150px" src="${bucketURL}${supplierAttacheVO.businessLicense}" /><input type="hidden" name="businessLicense" value="${(supplierAttacheVO.businessLicense)!}"/></#if></div>
				  	
	           </div>
            </td>
            <td style="text-align: center; ">
               <div class="main_left m10 span6"> 
				  	<div class="sel_box" id="imguplod_taxregist"  style="width: 200px;height: 200px"><#if supplierAttacheVO.taxregist??><img class="img_big add_more_goods" style="width: 200px;height: 150px" src="${bucketURL}${supplierAttacheVO.taxregist}" /><input type="hidden" name="taxregist" value="${(supplierAttacheVO.taxregist)!}"/></#if></div>
	           </div>
            </td>
            <td style="text-align: center; ">
                <div class="main_left m10 span6">
				 <div class="sel_box" id="imguplod_organize"  style="width: 200px;height: 200px"><#if supplierAttacheVO.organize??><img class="img_big add_more_goods" style="width: 200px;height: 150px" src="${bucketURL}${supplierAttacheVO.organize}" /><input type="hidden" name="organize" value="${(supplierAttacheVO.organize)!}"/></#if></div>
	            </div>
	        </td>
        </tr>
        
        <tr>
            <td>
        		<div id="container_brandRetist" class="ext_btn ext_btn_submit">
                    <a class="ext_btn ext_btn_submit" id="pickfiles_brandRetist"   href="#"  imagenameattribute="brandRetist">上传商标注册证</a>
                </div>
            </td>
            <td>
        		<div id="container_taxpayer" class="ext_btn ext_btn_submit">
                    <a class="ext_btn ext_btn_submit" id="pickfiles_taxpayer"   href="#"  imagenameattribute="taxpayer">上传一般纳税人资格证</a>
                </div>
            </td>
            <td>
        		<div id="container_depositBank" class="ext_btn ext_btn_submit">
                    <a class="ext_btn ext_btn_submit" id="pickfiles_depositBank"   href="#"  imagenameattribute="depositBank">上传银行开户许可证</a>
                </div>
            </td>
        </tr>
        <tr>
        	<td style="text-align: center; ">
	            <div class="main_left m10 span6">
					<div class="sel_box" id="imguplod_brandRetist"  style="width: 200px;height: 200px"><#if supplierAttacheVO.brandRetist??><img class="img_big add_more_goods" style="width: 200px;height: 150px" src="${bucketURL}${supplierAttacheVO.brandRetist}" /><input type="hidden" name="brandRetist" value="${(supplierAttacheVO.brandRetist)!}"/></#if></div>
		        </div>
		    </td>
            <td style="text-align: center; ">
               <div class="main_left m10 span6">
				  	<div class="sel_box" id="imguplod_taxpayer"  style="width: 200px;height: 200px"><#if supplierAttacheVO.taxpayer??><img class="img_big add_more_goods" style="width: 200px;height: 150px" src="${bucketURL}${supplierAttacheVO.taxpayer}" /><input type="hidden" name="taxpayer" value="${(supplierAttacheVO.taxpayer)!}"/></#if></div>
	           </div>
            </td>
            <td style="text-align: center; ">
               <div class="main_left m10 span6">
				  	<div class="sel_box" id="imguplod_depositBank"  style="width: 200px;height: 200px"><#if supplierAttacheVO.depositBank??><img class="img_big add_more_goods" style="width: 200px;height: 150px" src="${bucketURL}${supplierAttacheVO.depositBank}" /><input type="hidden" name="depositBank" value="${(supplierAttacheVO.depositBank)!}"/></#if></div>
	           </div>
            </td
        </tr>
        <tr>
            <td>
        		<div id="container_agentLiscenceCredit" class="ext_btn ext_btn_submit">
                    <a class="ext_btn ext_btn_submit" id="pickfiles_agentLiscenceCredit"   href="#"  imagenameattribute="agentLiscenceCredit">上传法人/授权人身份证明</a>
                </div>
            </td>
            <td>
        		<div id="container_agentLiscence" class="ext_btn ext_btn_submit">
                    <a class="ext_btn ext_btn_submit" id="pickfiles_agentLiscence"   href="#"  imagenameattribute="agentLiscence">上传法定代表人授权委托书</a>
                </div>
            </td>
            <td>
            </td>
        </tr>
        <tr>
            <td style="text-align: center; ">
               <div class="main_left m10 span6">
				  	<div class="sel_box" id="imguplod_agentLiscenceCredit"  style="width: 200px;height: 200px"><#if supplierAttacheVO.agentLiscenceCredit??><img class="img_big add_more_goods" style="width: 200px;height: 150px" src="${bucketURL}${supplierAttacheVO.agentLiscenceCredit}" /><input type="hidden" name="agentLiscenceCredit" value="${(supplierAttacheVO.agentLiscenceCredit)!}"/></#if></div>
	           </div>
            </td>
            <td style="text-align: center; ">
               <div class="main_left m10 span6">
				  	<div class="sel_box" id="imguplod_agentLiscence"  style="width: 200px;height: 200px"><#if supplierAttacheVO.agentLiscence??><img class="img_big add_more_goods" style="width: 200px;height: 150px" src="${bucketURL}${supplierAttacheVO.agentLiscence}" /><input type="hidden" name="agentLiscence" value="${(supplierAttacheVO.agentLiscence)!}"/></#if></div>
	           </div>
            </td>
            <td>
            </td>
        </tr>
    </tbody>
</table>
<span id="hiddenFieldId">
<#if (supplierAttacheVO.supplierImageList)?exists>
<#assign countIndex=1000>
<#list supplierAttacheVO.supplierImageList as imgVO>
<#if imgVO.imageType == 'brandLiscence'>
<input type="hidden" id="text_field_${countIndex}" value="${(imgVO.brandName)!}" name="brandName" />
<input type="hidden" id="desc_field_${countIndex}" value="${(imgVO.description)!}" name="imageDesc" />
<input type="hidden" id="hidden_field_${countIndex}" value="${(imgVO.imageUrl)!}" name="brandLiscence" />
</#if>
<#assign countIndex=countIndex+1>
</#list>
</#if>

<#if (supplierAttacheVO.supplierImageList)?exists>
<#assign countIndex=2000>
<#list supplierAttacheVO.supplierImageList as imgVO>
<#if imgVO.imageType == 'specialPapers'>
<input type="hidden" name="specialPapers" value="${(imgVO.imageUrl)!}" id="hidden_field_${countIndex}" />
<input type="hidden" name="specialPapersDesc" value="${(imgVO.description)!}" id="desc_field_${countIndex}" />
</#if>
<#assign countIndex=countIndex+1>
</#list>
</#if>
</span>
<#if (supplierAttacheVO.qualityLiscence)?exists>
<input type="hidden" id="qualityLiscenceId" name="qualityLiscence" value="${(supplierAttacheVO.qualityLiscence)!}"/>
<#else>
<input type="hidden" id="qualityLiscenceId" name="qualityLiscence" />
</#if>