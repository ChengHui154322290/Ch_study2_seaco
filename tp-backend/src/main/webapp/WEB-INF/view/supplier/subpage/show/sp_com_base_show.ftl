<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
    <tbody>
        <tr>
            <td class="td_right"><font color="red">*</font>供应商名称： </td>
            <td class=""  width="100">
                <input type="text" id="supp_add_name_id" readOnly="readonly" name="name" value="${(supplierVO.name)!}" class="_req input-text lh30" maxlength="60" size="30">
            </td>
            <td class="td_right"><font color="red">*</font>供应商简称：</td>
            <td>
                <input type="text" id="supp_add_simple_name_id" name="simpleName"  readOnly="readonly" value="${(supplierVO.alias)!}" class="_req input-text lh30" maxlength="60" size="30">
            </td>
        </tr>
        <tr>
            <td class="td_right"><font color="red">*</font>公司法人： </td>
            <td class=""  width="100">
                <input type="text" name="legalPerson" readOnly="readonly" value="${(supplierVO.legalPerson)!}" maxlength="60" class="_req input-text lh30" size="30">
            </td>
            <td class="td_right">状态：</td>
            <td>
                ${statusShow!"新建"}
            </td>
        </tr>
        <tr>
            <td class="td_right"><font color="red">*</font>联系地址： </td>
            <td class=""  width="100">
                <input type="text" name="address" readOnly="readonly" value="${(supplierVO.address)!}" maxlength="200" class="_req input-text lh30" size="30">
            </td>
            <td class="td_right"><font color="red">*</font>供应商类型：</td>
            <td>
            <#--<div class="select_border">
                <div class="select_containers">
                    <span class="fl">
                        <select class="_req select" name="supplierType" style="width:140px;">-->
                            <#if supplierTypes?exists>
                                <#list supplierTypes?keys as key>
                                   <#if supplierVO?? && supplierVO.supplierType == key>
                                   <#--<option value="${key}" selected="true">-->${supplierTypes[key]}<#--</option>-->
                                   <#else>
                                   </#if>
                                </#list>
                            </#if>
                        <#--</select>
                     </span>
                 </div>
            </div>-->
            </td>
        </tr>
        <tr>
            <td class="td_right"><font color="red">*</font>联系人： </td>
            <td class=""  width="100">
                <input type="text" name="linkName" readOnly="readonly"  value="${(supplierVO.linkName)!}" maxlength="60" class="_req input-text lh30" size="30">
            </td>
            <td class="td_right">主供应商：</td>
            <td>
               <input type="text" class="input-text lh30" readOnly="readonly" value="${(supplierVO.parentSupplierName)!}" id="supplier_query_text" size="20">
               <input type="hidden" readOnly="readonly" value="${(supplierVO.parentId)!}" id="supplier_add_parent_id" name="parentId" />
            </td>
        </tr>
        <tr>
            <td class="td_right"><font color="red">*</font>联系电话： </td>
            <td class=""  width="100">
                <input type="text" name="phone" readOnly="readonly" value="${(supplierVO.phone)!}"  maxlength="30" class="_req input-text lh30" size="30">
            </td>
            <td class="td_right">海淘供应商：</td>
            <td>
          		<#if supplierVO?? && supplierVO.isSea == 1>
	            	<input type="radio" value="1" checked="checked"  disabled="disabled"/>是&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" value="0"  disabled="disabled"/>否
	            	<input type="hidden" value="1" name="isHtSupplier"/>
	            <#else>
	            	<input type="radio" value="1"  disabled="disabled"/>是&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" checked="checked" value="0"  disabled="disabled"/>否
	            	<input type="hidden" value="0" name="isHtSupplier"/>
	            </#if> 
	            
            </td>
        </tr>
        <tr>
            <td class="td_right"><font color="red">*</font>电子邮箱： </td>
            <td class=""  width="100">
                <input type="text" name="email" readOnly="readonly" value="${(supplierVO.email)!}" maxlength="30" class="_req _email input-text lh30" size="30">
            </td>
            <td class="td_right">运费模板：</td>
            <td>
               <input type="text" readOnly="readonly" value="${(supplierVO.freightTemplateId)!}" class="input-text lh30" size="8" id="expressTemplateId"/>
               <input type="text" readOnly="readonly" value="${(supplierVO.freightTemplateName)!}" class="input-text lh30" size="22" id="expressTemplateName" />
            </td>
        </tr>
        <tr>
            <td class="td_right">传真： </td>
            <td class=""  width="100">
                <input type="text" readOnly="readonly" name="fax" value="${(supplierVO.fax)!}" maxlength="60" class="input-text lh30" size="30">
            </td>
            <td class="td_right">进项税率：</td> 
            <td>
                <#--<div class="select_border">
                <div class="select_containers">
                    <span class="fl">
                        <select class="_req select" name="incomeTaxRate" style="width:140px;">
                           <option value="${(supplierVO.incomeTaxRate)!}">-->${(supplierVO.incomeTaxRate)!}%<#--</option>
                        </select>
                     </span>
                 </div>
            </div>-->
            </td>
        </tr>
        <tr>
           <td class="td_right">备注：</td> 
            <td colspan="3">
                <input type="text" readOnly="readonly" value="${(supplierVO.supplierDesc)!}" name="expressTemplateRemark" value="" maxlength="100" class="input-text lh30" size="30">
            </td>
        </tr>
    </tbody>
</table>
<#--海关备案号start-->
<#if supplierVO?? && supplierVO.isSea == 1>
	<div id="recordationsDiv" >
<#else>
	<div id="recordationsDiv" style="display:none">
</#if>
<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
    <thead>
        <tr>
            <td class="td_right"><font color="red">*</font><font color="red">海关备案号</font></td>
            <td>
            </td>
            <td colpan="2">
            </td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td class="td_right"></td>
            <td colspan="3">
                <table id="recordationChooseTable">
                    <#if (supplierDTO.supplierCustomsRecordationList)?exists>
                    <#assign countIndex=1000>
                    <#list supplierDTO.supplierCustomsRecordationList as recordationVO>
                    <tr id="recordation_${countIndex}">
                        <td>
                            <div class="select_border">
                                <div class="select_containers">
                                    <span class="fl">
                                        <select name="customsChannel" style="width:100px;" class="_req select" >
                                            <#list supplierRecordations as supplierRecordation>
                                            <#if supplierRecordation.id==recordationVO.customsChannelId>
                                               <option value="${(supplierRecordation.id)!}" selected="true"> ${(supplierRecordation.name)!}</option>
                                            <#else>
                                            </#if>
                                            </#list>
                                        </select>
                                    </span>
                                </div>
                            </div>
                        </td>
                        <td>备案名称：
                            <input type="text" size="15" value="${recordationVO.recordationName}" class="_req input-text lh30" maxlength="60"
                            name="recordationName">
                        </td>
                        <td>备案编号：
                            <input type="text" size="15" value="${recordationVO.recordationNum}" class="_req input-text lh30" maxlength="60"
                            name="recordationNum">
                        </td>
                        <td>
                        </td>
                    </tr>
                    <#assign countIndex=countIndex+1>
                    </#list>
                    </#if>
                </table>
            </td>
        </tr>
    </tbody>
</table>
</div>
<#--海关备案号end-->
<#--经营品牌 start-->
<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
    <thead>
        <tr>
            <td class="td_right"><font color="red">*经营范围</font></td>
            <td class="" colpan="3">
            </td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td class="td_right"></td>
            <td colspan="3">
                <table id="brandSpanTableId">
                    <#if (supplierDTO.supplierCategoryList)?exists>
                    <#list supplierDTO.supplierCategoryList as categoryVO>
                    <tr>
                        <td>
                            <input type="hidden" value="${categoryVO.brandId}" name="supplierBrand" id="sBNameSel_${categoryVO.brandId}">
                            <input type="hidden" dataname="${(categoryVO.brandName)!}" dstatus="1" value="${categoryVO.brandId}" name="supplierABrandSel"
                            id="sBName_${categoryVO.brandId}">
                            ${categoryVO.brandId}
                        </td>
                        <td> ${(categoryVO.brandName)!}
                        </td>
                        <td>
                            <div class="select_border">
                                <div class="select_containers">
                                    <span class="fl">
                                        <select name="categoryDalei" id="supplierCategoryBig_${categoryVO.brandId}" style="width:140px;"
                                            class="select">
                                            <#if daleiCateList?exists>
                                            <#list daleiCateList as daleiCate>
                                            <#if categoryVO.categoryBigId == daleiCate.id>
                                            <option value="${daleiCate.id}" selected="selected">${daleiCate.name}</option>
                                            <#else>
                                            </#if>
                                            </#list>
                                            </#if>
                                        </select>
                                    </span>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="select_border">
                                <div class="select_containers">
                                    <span class="fl">
                                        <select name="supplierCategoryMid" id="supplierCategoryMid_${categoryVO.brandId}" style="width:140px;"  class="select">
                                            <#if categorySiblingsMap?exists && categoryVO.categoryBigId != null>
                                            <#list categorySiblingsMap[(categoryVO.categoryBigId)?string] as category>
                                            <#if categoryVO.categoryMidId == category.id>
                                            <option value="${category.id!}" selected="selected">${category.name!}</option>
                                            <#else>
                                            </#if>
                                            </#list>
                                            </#if>
                                        </select>
                                    </span>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="select_border">
                                <div class="select_containers">
                                    <span class="fl">
                                        <select name="supplierCategorySmall" id="supplierCategorySmall_${categoryVO.brandId}" style="width:140px;"
                                        class="select">
                                            <#if categorySiblingsMap?exists && categoryVO.categoryMidId != null>
                                            <#list categorySiblingsMap[(categoryVO.categoryMidId)?string] as category>
                                            <#if categoryVO.categorySmallId == category.id>
                                            <option value="${category.id!}" selected="true">${category.name!}</option>
                                            <#else>
                                            </#if>
                                            </#list>
                                            </#if>
                                        </select>
                                    </span>
                                </div>
                            </div>
                        </td>
                    </tr>
                    </#list>
                    </#if>
                </table>
            </td>
        </tr>
    </tbody>
</table>
<#--经营品牌 end-->

<#--银行账号start-->
<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
    <thead>
        <tr>
            <td class="td_right"><font color="red">*</font><font color="red">银行账号</font></td>
            <td>
                <#--<a class="ext_btn" href="javascript:void(0)" onclick="addBank();"><span class="add"></span>添加</a>-->
            </td>
            <td colpan="2">
            </td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td class="td_right"></td>
            <td colspan="3">
                <table id="bankChooseTable">
                    <#if (supplierDTO.supplierBankAccountList)?exists>
                    <#assign countIndex=1000>
                    <#list supplierDTO.supplierBankAccountList as bankVO>
                    <tr id="bankTr_${countIndex}">
                        <td>
                            <div class="select_border">
                                <div class="select_containers">
                                    <span class="fl">
                                        <select name="bankType" style="width:100px;" class="_req select">
                                            <#list supplierBankTypes?keys as key>
                                            <#if key==bankVO.accountType>
                                               <option value="${key}" selected="true"> ${supplierBankTypes[key]}</option>
                                            </#if>
                                            </#list>
                                        </select>
                                    </span>
                                </div>
                            </div>
                        </td>
                        <td>银行名称：
                            <input type="text" size="15" readOnly="readonly" value="${bankVO.bankName}" class="_req input-text lh30" maxlength="60"
                            name="bankName">
                        </td>
                        <td>银行账号：
                            <input type="text" size="15" readOnly="readonly" value="${bankVO.bankAccount}" class="_req input-text lh30" maxlength="60"
                            name="bankAccount">
                        </td>
                        <td>开户名称：
                            <input type="text" size="15" readOnly="readonly" value="${bankVO.bankAccName}" class="_req input-text lh30" maxlength="60"
                            name="bankAccName">
                        </td>
                        <td>
                            <div class="select_border">
                                <div class="select_containers">
                                    <span class="fl"> 币种：
                                        <select name="bankCurrency" style="width:100px;" class="_req select" id="bankcurrency_${countIndex}">
                                            <#list supplierCurrencyTypes?keys as key>
                                            <#if key==bankVO.bankCurrency>
                                            <option value="${key}" selected="true"> ${supplierCurrencyTypes[key]}</option>
                                            </#if>
                                            </#list>
                                        </select>
                                    </span>
                                </div>
                            </div>
                        </td>
                        <td>
                        </td>
                    </tr>
                    <#assign countIndex=countIndex+1>
                    </#list>
                    </#if>
                </table>
            </td>
        </tr>
    </tbody>
</table>
<#--银行账号end-->

<#--开票信息start-->
<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
    <thead>
        <tr>
            <td class="td_right"><font color="red">*开票信息</font></td>
            <td colpan="3">
            </td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td></td>
            <td>
                <table id="kaipiao_table">
		        <#if (supplierDTO.supplierInvoiceList)?exists>
		        <#assign countIndex=1000>
		        <#list supplierDTO.supplierInvoiceList as kaipiaoVO>
		        <tr id="kaipiaoTr1_${countIndex}">
		            <td class="td_right">
		                <font color="red">*</font>开票名称：
		            </td>
		            <td width="100" class="">
		                <input type="text"  size="40" readOnly="readonly" value="${kaipiaoVO.name}" class="_req input-text lh30" name="kpName">
		            </td>
		            <td class="td_right">
		                <font color="red">*</font>纳税人识别码：
		            </td>
		            <td>
		                <input type="text" size="40" readOnly="readonly" class="_req input-text lh30" value="${kaipiaoVO.taxpayerCode}" name="taxpayerCode">
		            </td>
		        </tr>
		        <tr id="kaipiaoTr2_${countIndex}">
		            <td class="td_right">
		                <font color="red">*</font>开户银行：
		            </td>
		            <td width="100" class="">
		                <input type="text" size="40" readOnly="readonly" value="${kaipiaoVO.bankName}" class="_req input-text lh30" name="kpBank">
		            </td>
		            <td class="td_right">
		                <font color="red">*</font> 开户名称：
		            </td>
		            <td>
		                <input type="text" size="40" readOnly="readonly" value="${kaipiaoVO.bankAccName}" class="_req input-text lh30" name="kpAccountName">
		            </td>
		        </tr>
		        <tr id="kaipiaoTr3_${countIndex}">
		            <td class="td_right">
		                <font color="red">*</font>开户银行账号：
		            </td>
		            <td width="100" class="">
		                <input type="text" size="40" readOnly="readonly" value="${kaipiaoVO.bankAccount}" class="_req input-text lh30" name="kpAccount">
		            </td>
		            <td class="td_right">
		                <font color="red">*</font>联系电话：
		            </td>
		            <td>
		                <input type="text" size="40" readOnly="readonly" value="${kaipiaoVO.linkPhone}" class="_req input-text lh30" name="kpTel">
		            </td>
		        </tr>
		        <tr id="kaipiaoTr4_${countIndex}">
		            <td class="td_right">
		                <font color="red">*</font>联系地址：
		            </td>
		            <td width="100" class="">
		                <input type="text" size="40" readOnly="readonly" class="_req input-text lh30" value="${kaipiaoVO.linkAddr}" name="kpAddress">
		            </td>
		            <td class="td_right">
		            </td>
		            <td>
		            </td>
		        </tr>
		        <#assign countIndex=countIndex+1>
		        </#list>
		        </#if>
		        </table>
            </td>
        </tr>
    </tbody>
</table>
<#--开票信息end-->

<#--供应商联系人 start-->
<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
    <thead>
        <tr>
            <td class="td_right"><font color="red">*供应商联系人</font></td>
            <td colpan="3">
            </td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td></td>
            <td>
                <table id="supplier_link_table">
		        <#if (supplierDTO.supplierLinkList)?exists>
		        <#assign countIndex=1000>
		        <#list supplierDTO.supplierLinkList as linkerVO>
		        <tr id="supplier_link_tr1_${countIndex}">
		            <td class="td_right">联系人类型：
		            </td>
		            <td width="100">
		                <div class="select_border">    
		                    <div class="select_containers">        
		                        <span class="fl">            
		                            <select style="width:140px;" name="suppLinkType" class="_req select" disable="disable"> 
		                               <#list supplierLinkTypes?keys as key>
		                                <#if key==linkerVO.linkType>
		                                <option value="${key}" selected="true"> ${supplierLinkTypes[key]}</option>
		                                </#if>
		                                </#list>
		                            </select>        
		                        </span>    
		                    </div>
		                </div>
		            </td>
		            <td class="td_right"><font color="red">*</font>姓名：
		            </td>
		            <td>
		                <input type="text" size="40"  readOnly="readonly" value="${linkerVO.linkName}" class="_req input-text lh30" name="supplierLinkName">
		            </td>
		        </tr>
		        <tr id="supplier_link_tr2_${countIndex}">
		            <td class="td_right"><font color="red">*</font>手机号码：
		            </td>
		            <td width="100">
		                <input type="text" size="40"  readOnly="readonly" value="${linkerVO.mobilePhone}" class="_req input-text lh30" name="supplierLinkMobile">
		            </td>
		            <td class="td_right"><font color="red">*</font>电子邮箱：
		            </td>
		            <td>
		                <input type="text" size="40"  readOnly="readonly" value="${linkerVO.email}" class="_req_email input-text lh30" name="supplierLinkEmail">
		            </td>
		        </tr>
		        <tr id="supplier_link_tr3_${countIndex}">
		            <td class="td_right"><font color="red">*</font>固定电话：
		            </td>
		            <td width="100">
		                <input type="text" size="40"  readOnly="readonly" value="${linkerVO.telephone}" class="_req input-text lh30" name="supplierLinkTel">
		            </td>
		            <td class="td_right">传真号码：
		            </td>
		            <td><input type="text" size="40"  readOnly="readonly" value="${(linkerVO.fax)!}" class="input-text lh30" name="supplierLinkFaq" >
		            </td>
		        </tr>
		        <tr id="supplier_link_tr4_${countIndex}">
		            <td class="td_right"><font color="red">*</font>地址：
		            </td>
		            <td width="100">
		                <input type="text" size="40"  readOnly="readonly" value="${linkerVO.linkAddress}" class="input-text lh30" name="supplierLinkAddr">
		            </td>
		            <td class="td_right">QQ：
		            </td>
		            <td><input type="text" size="40"  readOnly="readonly" value="${linkerVO.qq}" class="input-text lh30" name="supplierLinkQQ">
		            </td>
		        </tr>
		        <#assign countIndex=countIndex+1>
		        </#list>
		        </#if>
		        </table>
            </td>
        </tr>
    </tbody>
</table>
<#--供应商联系人 end-->

<#--仓库及配送start-->
<#-- 
<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
    <thead>
        <tr>
            <td class="td_right"><font color="red">仓库及配送</font></td>
            <td class="" colpan="3">
            </td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td class="td_right"></td>
            <td colspan="3">
                <table id="storageSpanTableId">
                    <#if (supplierVO.supplierWarehouseList)?exists>
                    <#assign countIndex=1000>
                    <#list supplierVO.supplierWarehouseList as storeVO>
                    <tr>
                        <td>
                            <input type="hidden" value="${storeVO.warehouseId}" name="supplierstorage" id="storageNameSel_${storeVO.warehouseId}">
                            <input type="hidden" dataname="${storeVO.warehouseName}" dstatus="1" value="${storeVO.warehouseId}" name="supplierAstorageSel"
                            id="storageName_${storeVO.warehouseId}">
                            ${storeVO.warehouseId}
                        </td>
                        <td>${storeVO.warehouseName}</td>
                        <td id="storagespp_add_status_${storeVO.warehouseId}">有效</td>
                        <td id="sp_storage_btn_1">
                            <input type="button" class="ext_btn ext_btn_error" value="失效" onclick="changeStatus_${storeVO.warehouseId}(${storeVO.warehouseId},'storage')">
                        </td>
                    </tr>
                    <#assign countIndex=countIndex+1>
                    </#list>
                    </#if>
                </table>
            </td>
        </tr>
    </tbody>
</table>
-->
<#--仓库及配送end-->

<#--西客商城对接人start-->
<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
    <thead>
        <tr>
            <td class="td_right"><font color="red">*</font><font color="red">西客对接人</font></td>
            <td colpan="3">
            </td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td class="td_right"></td>
            <td colspan="3">
                <table id="mt_link_table">
                    <#if (supplierDTO.supplierXgLinkList)?exists>
                    <#assign countIndex=1000>
                    <#list supplierDTO.supplierXgLinkList as mtLinkVO>
                    <tr id="mt_linker_tr_${countIndex}">
                        <td>
                            <div class="select_border">
                                <div class="select_containers">
                                    <span class="fl">类型：
                                        <select name="mtLinkType" style="width:100px;" class="_req select" readonly="true">
                                            <#list supplierLinkTypes?keys as key>
                                            <#if key==mtLinkVO.linkType>
                                            <option value="${key}" selected="true"> ${supplierLinkTypes[key]}</option>
                                            </#if>
                                            </#list>
                                        </select>
                                    </span>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="select_border">
                                <div class="select_containers">
                                    <span class="fl"> 部门：
                                        <select name="xgLinkerDeptId" id="mt_link_bm_${countIndex}" style="width:100px;" class="_req select">
                                       		<option value="${mtLinkVO.deptId}">${mtLinkVO.deptName}</option>
                                        </select>
                                         <input type="hidden" id="mt_link_bms_${countIndex}" name="xgLinkerDeptName" value="${mtLinkVO.deptName}">
                                    </span>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="select_border">
                                <div class="select_containers">
                                    <span class="fl">姓名：
                                        <select name="xgLinkerUserId" id="mt_link_name_${countIndex}" style="width:100px;" class="_req select">
                                            <option value="${mtLinkVO.userId}">${mtLinkVO.userName}</option>
                                        </select>
                                        <input type="hidden" id="mt_link_names_${countIndex}" name="xgLinkerUserName" value="${mtLinkVO.userName}">
                                    </span>
                                </div>
                            </div>
                        </td>
                        <#--<td>
                            <input type="button" value="删除" onclick="deleteMtLink(${countIndex})" disabled="disabled" class="ext_btn">
                        </td>-->
                    </tr>
                    <#assign countIndex=countIndex+1>
                    </#list>
                    </#if>
                </table>
            </td>
        </tr>
    </tbody>
</table>
<#--商家平台账号start-->
<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
    <thead>
        <tr>
            <td class="td_right"><font color="red">*商家平台账号</font></td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td class="td_right"></td>
            <td colspan="3">
                <table id="mt_link_table">
              
                    <tr id="mt_Supplier_tr_User">
                        <td>
                            <div class="select_border">
                                <div class="select_containers">
                                    <span class="fl">账号：
                                    	<input type="text" id="sp_user_Id" name="sp_user_Id" value="${(supplierUserVO.loginName)!'系统自动生成'}" disabled="disabled" class="input-text lh30" size="15"> 
                                    </span>
                                </div>
                            </div>
                        </td> 
                        <td>
                            <div class="select_border">
                                <div class="select_containers">
                                    <span class="fl">密码：
                                        <input type="text"  id="sp_user_password" name="sp_user_password" class="input-text lh30" value="商家平台密码" maxlength="60" size="15"  onfocus="this.type='password'" autocomplete="off" />
                                    </span>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="select_border">
                                <div class="select_containers">
                                    <span class="fl" id="supp_user_status_show">
                                    <#if (supplierUserVO?exists) && supplierUserVO.status == 1> 有效
                                    <#elseif (supplierUserVO?exists) && supplierUserVO.status == 0>无效
                                    </#if>
                                    </span>
                                </div>
                            </div>
                        </td>
                    </tr>
                </table>
               
            </td>
        </tr>
    </tbody>
</table>
<input type="hidden" value="0" id="fieldIdDefined" />
