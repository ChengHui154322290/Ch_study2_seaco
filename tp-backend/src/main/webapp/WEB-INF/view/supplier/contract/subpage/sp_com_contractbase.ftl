<table cellspacing="0" cellpadding="0" border="0" width="65%" class="form_table pt15 pb15" >
    <tbody>
        <tr>
            <td class="td_right" align="right" nowrap><font color="red">*</font>供应商/商家（乙方）： </td>
            <td align="left" nowrap>
                <input type="text" name="supplierId"  id="all_page_add_supplier_id" value="${(contractVO.supplierId)!}" class="input-text lh30" size="5">
                <input type="text" name="supplierName" id="all_page_add_supplier_name" value="${(contractVO.supplierName)!}" class="input-text lh30" size="10">
                <input type="button" value="确定" id="all_page_add_supplier_confirm" class="ext_btn ext_btn_submit">
                <input type="button" class="ext_btn" id="all_page_add_supplier_query"  value="查询">
                <input type="hidden" name="supplierId" class="_req" value="${(contractVO.supplierId)!}" id="all_page_add_supplier_id_hidden" />
            </td>
            <td class="td_right" align="left">合同主体（甲方）：</td>
            <td class="" width="100" align="left">
                <select class="select" name="contractPartyA" style="width: 240px;" id="contractPartyA">
                    <#list contractPartyA?keys as key>
                        <#if key == (contractVO.contractXg)!"">
                        <option value="${key}" selected="selected">${contractPartyA[key]}</option>
                        <#else>
                        <option value="${key}">${contractPartyA[key]}</option>
                        </#if>
                    </#list>
                </select>
            </td>

            <td class="td_right" align="left" nowrap>状态：</td>
            <td align="left" nowrap>
                <span class="fl">${(aduitStatusMap[contractVO.auditStatus?c])!"新建"}</span>
            </td>
        </tr>
        
        <tr>
            <td class="td_right" align="right"><font color="red">*</font>合同模板： </td>
            <td align="left" id="contractTemplateShow">${(contractVO.templateName)!"根据供应商类型自动选择"}
            </td>
            <td class="td_right" align="left">签约日期：</td>
            <td class="" width="100" align="left" colspan="3">
                <input type="text" name="signingDate" value="${(contractVO.signingDate?string("yyyy-MM-dd"))!}" id="c_signingDate" class="_dateField input-text lh30" size="20">
            </td>
        </tr>

        <tr>
            <td class="td_right" align="left" nowrap>合同编号：</td>
            <td class="" nowrap>
                <span id="contractCodeShowTd">
                <#if (contractVO.id)?exists>
                    ${(contractVO.contractCode)!}
                    <input type="hidden" id="contractId" class="input-text lh30" readOnly="readOnly" size="33" value="${(contractVO.id)!}" />
                <#else>系统自动生成
                </#if>
                </span>
            </td>
            
            <td class="td_right" align="left" nowrap><font color="red">*</font>有效期：</td>
            <td class="" align="left" nowrap>
                <input type="text" name="startDate" id="c_starDate_0" value="${(contractVO.startDate?string("yyyy-MM-dd"))!}" class="_req  _dateField input-text lh30" size="20">&nbsp;到&nbsp;
                <input type="text" name="endDate" id="c_endDate_0" value="${(contractVO.endDate?string("yyyy-MM-dd"))!}" class="_req  _dateField input-text lh30" size="20">
            </td>
            <td class="td_right" colspan="2" nowrap></td>
        </tr>
        <tr>
            <td class="td_right" nowrap>备注：</td>
            <td class="" nowrap>
                <input type="text" name="contractDesc"  class="input-text lh30" size="33" value="${(contractVO.contractDesc)!}">
            </td>
            <td class="td_right" nowrap><font color="red">*</font>合同名称：</td>
            <td nowrap colspan="3">
                <input type="text" class="_req input-text lh30" size="60" name="contractName" id="contractNameText" value="${(contractVO.contractName)!}">
            </td>
        </tr>
        <tr align="left" >
            <td class="td_right" nowrap><font color="red">*</font>销售渠道：</td>
            <td class="" colspan="5">
                <#list salesWayMap?keys as salesWay>
                    <span  style="margin-right:20px;">
                        <#if !(contractVO?exists) || contractVO.salesChannelsMap[salesWay]>
	                    <input type="checkbox" name="salesWay" checked="checked" value="${salesWay}">${salesWayMap[salesWay]}
	                    <#else>
	                    <input type="checkbox" name="salesWay" value="${salesWay}">${salesWayMap[salesWay]}
	                    </#if>
	                </span>
                </#list>
            </td>
        </tr>
    </tbody>
</table>
<table class="form_table pt15 pb15" cellspacing="0" cellpadding="0" border="0" width="100%">
    <tbody>
        <tr>
            <td colspan="6">
                <hr style="border: 1px dashed #247DFF;" />
            </td>
        </tr><br/>
        <#--商品范围-->
        <tr>
            <td colspan="6" align="left"><font color="red">*</font>商品范围&nbsp;&nbsp; <a class="ext_btn"
                href="javascript:void(0)"   onclick="addproduct();"><span
                    class="add"></span>添加</a> &nbsp;&nbsp;
            </td>
        </tr>
        <tr>
            <td colspan="6" align="center">
                <table width="100%" cellspacing="0" cellpadding="0" class="list_table CRZ" >
	                <thead>
	                    <tr align="center">
	                        <th>NO.</th>
	                        <th>品牌</th>
	                        <th>大类</th>
	                        <th>中类</th>
	                        <th>小类</th>
	                        <th>佣金(%)</th>
	                        <th>资质材料</th>
	                        <th>状态</th>
	                        <th>操作</th>
	                    </tr>
	                </thead>
	                
	                <tbody id="productAddInfo">
	                    <#if (contractVO.contractProductMap)?exists>
	                    <#assign countIndex=1000> 
	                    <#assign countIndexShow=1>
						<#list (contractVO.contractProductMap)?keys as tagKey>
						<#assign rowSpanSize=contractVO.contractProductMap[tagKey]?size> 
						<#assign mapValIndex = 1>
						<#list contractVO.contractProductMap[tagKey] as product>
						<#assign endTag=product.brandId+"">
						<#assign bigCTag = "_"+product.brandId>
						<#if product.bigId != null>
						    <#assign endTag=endTag+"_b"+product.bigId>
						    <#assign bigCTag=bigCTag+"_"+product.bigId>
						</#if>
						<#if product.midId != null>
						    <#assign endTag=endTag+"_m"+product.midId>
						</#if>
						<#if product.smallId != null>
						    <#assign endTag=endTag+"_s"+product.smallId>
						</#if>
	                    <tr id="contractProduct_${countIndex}" align="center"
	                        name="contractProducts"">
	                        <td><input type="hidden" value="${product.contractId}" name="contractId" id="contractId"/>${countIndexShow}</td>
	                        <td>${product.brandName}</td>
	                        <#if rowSpanSize gt 1 && mapValIndex == 1>
	                            <td id="bigCname${bigCTag}" rowSpan="${rowSpanSize}">${product.bigName}</td>
	                        <#elseif rowSpanSize == 1>
	                            <td>${(product.bigName)!"——"}</td>
	                        <#else>
	                           <#-- <td>${product.bigName}</td> -->
	                        </#if>
	                        <td>${(product.midName)!"——"}</td>
	                        <td>${(product.smallName)!"——"}</td>
	                        <td>${product.commission}</td>
	                        <#if rowSpanSize gt 1 && mapValIndex == 1>
	                            <td id="qualiTdId${bigCTag}" rowSpan="${rowSpanSize}">
		                            <a href="javascript:void(0)" onclick="addZzzmInfo(this,'${endTag}');">上传/查看</a>
		                        </td>
		                    <#elseif rowSpanSize == 1>
		                        <#if product.bigId != null>
		                        <td>
		                            <a href="javascript:void(0)" onclick="addZzzmInfo(this,'${endTag}');">上传/查看</a>
		                        </td>
		                        <#else>
		                        <td>
		                            ——
		                        </td>
		                        </#if>
	                        <#else>
	                            <#--
	                            <td>
		                            <a href="javascript:void(0)" onclick="addZzzmInfoShow(this,${endTag});">上传/查看</a>
		                        </td>
		                        -->
	                        </#if>
	                        <td name="status">
	                            <#if product.status == 'true'>启用
	                            <#elseif product.status == 'false'>停用
	                            </#if>
	                        </td>
	                        <td>
	                            <input type="button" value="删除" onclick="deleteContractProduct(${countIndex},'${endTag}')" class="ext_btn">
	                        </td>
	                        <input type="hidden" name="brandId" value="${product.brandId}"  id="brandId${endTag}"/>
	                        <input type="hidden" name="brandName" value="${product.brandName}" id="brandName${endTag}"/>
	                        <input type="hidden" name="bigId" value="${product.bigId}"/>
	                        <input type="hidden" name="midId" value="${product.midId}"/>
	                        <input type="hidden" name="smallId" value="${product.smallId}"  id="smallId${endTag}"/>
	                        <input type="hidden" name="bigName" value="${product.bigName}" />
	                        <input type="hidden" name="midName" value="${product.midName}" />
	                        <input type="hidden" name="smallName" value="${product.smallName}" id="smallName${endTag}"/>
	                        <input type="hidden" name="commission" value="${product.commission}">
	                    </tr>
	                    <#assign mapValIndex = mapValIndex + 1>
	                    <#assign countIndexShow=countIndexShow+1>
	                    <#assign countIndex=countIndex+1> 
						</#list>
						</#list>
						</#if>
	                </tbody>
                </table>
            </td>
        </tr>
    </tbody>
</table>    

<#--资质证明-->
<div id="zzInfoTr">
<#if (contractVO.contractQualificationsMap)?exists>
<#list (contractVO.contractQualificationsMap)?keys as tagKey>
<table width="100%" cellspacing="0" cellpadding="0" style="width:495px;display:none;" class="form_table pt15 pb15" name="zzInfoTable" id="zzzm_${(tagKey)!}">
    <tbody>
        <#assign countIndex=1> 
        <#list contractVO.contractQualificationsMap[tagKey] as quaItem>
        <#if countIndex == 1>
            <tr>
	            <td colspan="4">资质证明&nbsp;&nbsp;&nbsp;${(quaItem.brandName)!}&nbsp;&nbsp;${(quaItem.smallName)!}
	            </td>
	        </tr>
	        <tr>
	            <td align="center"  width="20%">
	                <#if quaItem.hasChecked=='true'>
	                <input type="checkbox" name="quaitionAttach_${(tagKey)!}" checked="checked" value="${(quaItem.papersId)!}">
	                <input type="hidden" id="quaitionAttach_sel_${(tagKey)!}" value="${(quaItem.papersId)!}">
	                <#else>
	                <input type="checkbox" name="quaitionAttach_${(tagKey)!}" value="${(quaItem.papersId)!}">
	                </#if>
	            </td>
	            <td  width="40%">${(quaItem.papersName)!}<input type="hidden" name="quaName${(tagKey)!}_${(quaItem.papersId)!}" value="${(quaItem.papersName)!}">
	            </td>
	            <td width="120" align="right" class="">
	                <input type="file" size="15" class="_imgPre input-text lh30" id="qualityFile${(tagKey)!}_${(quaItem.papersId)!}" name="qualityFile${(tagKey)!}_${(quaItem.papersId)!}">
	            </td>
	            <td align="left">
	                <#if (quaItem.url)?exists && quaItem.url != ''>
	                <input type="button" value="预览" onclick="showImage('qualityFile${(tagKey)!}_${quaItem.papersId}','${(dfsDomainUtil.getFileFullUrl(quaItem.url))!}');" class="ext_btn">
	                <input type="hidden" value="${(quaItem.url)!}" name="beforeUrl${(tagKey)!}_${(quaItem.papersId)!}" />
	                <#else>
	                <input type="button" value="预览" onclick="showImage('qualityFile${(tagKey)!}_${quaItem.papersId}');" class="ext_btn">
	                </#if>
	            </td>
	        </tr>
        <#else>
	        <tr>
	            <td align="center">
	                <#if quaItem.hasChecked=='true'>
	                <input type="checkbox" name="quaitionAttach_${(tagKey)!}" checked="checked" value="${(quaItem.papersId)!}">
	                <input type="hidden" id="quaitionAttach_sel_${(tagKey)!}" value="${(quaItem.papersId)!}">
	                <#else>
	                <input type="checkbox" name="quaitionAttach_${(tagKey)!}" value="${(quaItem.papersId)!}">
	                </#if>
	            </td>
	            <td>${(quaItem.papersName)!}<input type="hidden" name="quaName${(tagKey)!}_${(quaItem.papersId)!}" value="${(quaItem.papersName)!}">
	            </td>
	            <td width="120" align="right" class="">
	                <input type="file" size="15" class="_imgPre input-text lh30" id="qualityFile${(tagKey)!}_${(quaItem.papersId)!}" name="qualityFile${(tagKey)!}_${(quaItem.papersId)!}">
	            </td>
	            <td align="left">
	                <#if (quaItem.url)?exists && quaItem.url != ''>
	                <input type="button" value="预览" onclick="showImage('qualityFile${(tagKey)!}_${quaItem.papersId}','${(dfsDomainUtil.getFileFullUrl(quaItem.url))!}');" class="ext_btn">
	                <input type="hidden" value="${(quaItem.url)!}" name="beforeUrl${(tagKey)!}_${(quaItem.papersId)!}" />
	                <#else>
	                <input type="button" value="预览" onclick="showImage('qualityFile${(tagKey)!}_${quaItem.papersId}');" class="ext_btn">
	                </#if>
	            </td>
	        </tr>
        </#if>
        <#assign countIndex=countIndex+1> 
        </#list>
    </tbody>
</table>
</#list>
</#if>
</div>
<#-- contractQualificationsVOList-->
<#-- 
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr id="zzInfoTr">
        <td>
            <table width="100%" cellspacing="0" cellpadding="0" id="zzzm_1000" name="zzInfoTable">
            </table>
            <input type="hidden" name="yxzpjyxkz" id="papersName1" value="${papersName1}"/>
            <input type="hidden" name="papersUrl1" id="yxzpjyxkz_file" value="${yxzpjyxkz_file}" />
            <input type="hidden" name="wsxkz" id="papersName2" value="${papersName2}" />
            <input type="hidden" name="papersUrl2" id="wsxkz_file" value="${wsxkz_file}" />
            <input type="hidden" name="productId" id="productId" value="${productId}" />
        <td>
    </tr>
    <tr>
        <td>
        </td>
    </tr>
</table>
-->
<hr style="border: 1px dashed #247DFF;" />
<#--结算规则-->
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="form_table pt15 pb15">
    <tr>
        <td align="left">
            <div class="select_border">
                <div class="select_containers">
                    <span class="fl"><font color="red">*</font>结算规则
                        <select class="select" name="ruleType" style="width: 140px;" id="ruleType">
                           <#if settlementruleTypeMap?exists>
                           <#list settlementruleTypeMap?keys as key>
                               <#if contractSettlementRuleVO?? && contractSettlementRuleVO.ruleType == key>
                                 <option value="${key}">${settlementruleTypeMap[key]}</option>
                               <#else>
                                 <option value="${key}">${settlementruleTypeMap[key]}</option>
                               </#if>
                           </#list>
                           </#if>
                        </select>
                    </span>
                </div>
            </div>
        </td>
    </tr>
     <tr>
        <td align="left">
            <div class="select_border" style="margin-left:72px;">
                <span class="fl">长期活动每月
                    <select class="select" name="frequence" style="width:50px;">
                       <#if settlementRuleCountMap?exists>
                       <#list settlementRuleCountMap?keys as key>
                           <#if contractSettlementRuleVO?? && contractSettlementRuleVO.frequence == key>
                             <option value="${key}">${settlementRuleCountMap[key]}</option>
                           <#else>
                             <option value="${key}">${settlementRuleCountMap[key]}</option>
                           </#if>
                       </#list>
                       </#if>
                    </select>次结算
                </span>
            </div>
        </td>
    </tr>
    
    <#if (contractVO.contractSettlementRuleVOList)?exists> 
    <#assign countIndex=1000> 
    <#assign countIndexShow=1>
    <#list contractVO.contractSettlementRuleVOList as settleRule>
    <tr align="left" >
        <td>
            <table width="100%" border="0" cellspacing="0" cellpadding="0" style="width:1285px;">
                <tbody id="settlementruleInfo" name="settlementrule">
                    <tr id="contractsettlementrule_${countIndex}">
                        <td  class="td_right" style="width:150px;">活动结束后 ：</td>
                        <td style="width: 90px;">
                            <input type="text" name="day" id="day_${countIndex}" value="${(settleRule.day)!}" class="_intnum _req input-text lh30" size="10" onchange="ruleDay();"/>
                        </td>
                        <td align="left" style="width: 80px;">
                            <select class="select" name="dayType" style="width: 80px;" id="dayType" onchange="selectday();">
                                <#if settlementruleDayTypeMap?exists>
                                <#list settlementruleDayTypeMap?keys as key>
                                     <#if settleRule?? && settleRule.dayType == key>
                                         <option value="${key}" selected="true">${settlementruleDayTypeMap[key]}</option>
                                     <#else>
                                         <option value="${key}">${settlementruleDayTypeMap[key]}</option>
                                     </#if>
                                </#list>
                                </#if>
                            </select>
                        </td>
                        <td align="left" style="width: 140px;">工作日结算当期 (%)：</td>
                        <td  style="width: 90px;">
                            <input type="text" name="percent" id="percent_${countIndex}" value="${(settleRule.percent)!}" class="_intnum _req input-text lh30" size="10" onchange="rulePercent();">
                        </td>
                        <td align="left">
                            <a class="ext_btn" href="javascript:void(0)" onclick="addContractSettlementRule();"><span class="add"></span>添加</a>
                            <#if countIndexShow &gt; 1 >
                            <a class="ext_btn" href="javascript:void(0)" onclick="deletesettlementRule(${countIndex});"><span class="sub"></span>删除</a>
                            </#if>
                        </td>
                        <td>
                        </td>
                    </tr>
                </tbody>
            </table>
        </td>
    </tr>
    <#assign countIndexShow=countIndexShow+1>
	<#assign countIndex=countIndex+1> 
	</#list> 
	<#else>
	<tr align="left" >
        <td>
            <table width="100%" border="0" cellspacing="0" cellpadding="0" style="width:1085px;">
                <tbody id="settlementruleInfo" name="settlementrule">
                    <tr id="contractsettlementrule_1000">
                        <td  class="td_right" style="width:150px;">活动结束后 ：</td>
                        <td style="width: 90px;">
                            <input type="text" name="day" id="day_1000"  class="_intnum _req input-text lh30" value="15" size="10" onchange="ruleDay();"/>
                        </td>
                        <td align="left" style="width: 80px;">
                                        <select class="select" name="dayType" style="width: 80px;" id="dayType" onchange="selectday();">
                                            <#if settlementruleDayTypeMap?exists>
                                            <#list settlementruleDayTypeMap?keys as key>
                                                <#if key == "workday">
                                                 <option value="${key}" selected="selected">${settlementruleDayTypeMap[key]}</option>
                                                 <#else>
                                                 <option value="${key}">${settlementruleDayTypeMap[key]}</option>
                                                 </#if>
                                            </#list>
                                            </#if>
                                        </select>
                        </td>
                        <td align="left" style="width: 140px;">&nbsp;&nbsp;工作日结算当期(%) ：</td>
                        <td  style="width: 90px;">
                            <input type="text" name="percent" id="percent_1000" class="_intnum _req input-text lh30" size="10" value="70" onchange="rulePercent();">
                        </td>
                        <td align="left">
                            <a class="ext_btn" href="javascript:void(0)" onclick="addContractSettlementRule();"><span class="add"></span>添加</a>
                            <#--<a class="ext_btn" href="javascript:void(0)" onclick="deletesettlementRule(1000);"><span class="sub"></span>删除</a> -->
                        </td>
                    </tr>
                    <tr id="contractsettlementrule_1001">
                        <td  class="td_right" style="width:150px;">活动结束后 ：</td>
                        <td style="width: 90px;">
                            <input type="text" name="day" id="day_1001"  class="_intnum _req input-text lh30" value="45" size="10" onchange="ruleDay();"/>
                        </td>
                        <td align="left" style="width: 80px;">
                            <select class="select" name="dayType" style="width: 80px;">
                                <#if settlementruleDayTypeMap?exists>
                                <#list settlementruleDayTypeMap?keys as key>
                                    <#if key == "naturnlday">
                                     <option value="${key}" selected="selected">${settlementruleDayTypeMap[key]}</option>
                                     <#else>
                                     <option value="${key}">${settlementruleDayTypeMap[key]}</option>
                                     </#if>
                                </#list>
                                </#if>
                            </select>
                        </td>
                        <td align="left" style="width: 140px;">&nbsp;&nbsp;工作日结算当期(%) ：</td>
                        <td  style="width: 90px;">
                            <input type="text" name="percent" id="percent_1001" class="_intnum _req input-text lh30" size="10" value="30" onchange="rulePercent();">
                        </td>
                        <td align="left">
                            <a class="ext_btn" href="javascript:void(0)" onclick="addContractSettlementRule();"><span class="add"></span>添加</a>
                            <a class="ext_btn" href="javascript:void(0)" onclick="deletesettlementRule(1001);"><span class="sub"></span>删除</a>
                        </td>
                    </tr>
                </tbody>
            </table>
        </td>
    </tr>
	</#if>
    <tr>
        <td colspan="7">
            <hr style="border: 1px dashed #247DFF;width:100%;"/>
        </td>
    </tr>
</table>
        
<#--费用明细-->
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="form_table pt15 pb15">
    <#assign countIndex=1000>
    <tr>
        <td colspan="6"><font color="red">*</font>费用明细
            <a class="ext_btn" href="javascript:void(0)" onclick="addContractCost();"><span class="add"></span>新增费用</a>
        </td>
    </tr>
    <tr align="left">
        <td>
	        <table width="100%" border="0" cellspacing="0" cellpadding="0" id="contractcostinfos" style="width:1085px;">
	            <#if (contractVO.contractCostList)?exists> 
			    <#assign countIndex=1000> 
			    <#assign countIndexShow=1>
			    <#list contractVO.contractCostList as contractCost>
	            <tr id="contractCostInfo_${countIndex}" name="costinfo">
	                <td  align="center" width="550">
                        <select class="select" name="costType" style="width: 120px;" id="costType">
                         <#if costTypesMap?exists>
                              <#list costTypesMap?keys as key>
                              <#if contractCost?? && contractCost.type == key>
                                    <option value="${key}" selected="true">${costTypesMap[key]}</option>
                              <#else>
                                    <option value="${key}">${costTypesMap[key]}</option>
                              </#if>
                              </#list>
                         </#if>
                        </select>    
                        <input type="text" name="value" id="basevalue" value="${(contractCost.value)!}" class="_price input-text lh30" size="20">
                  		      币种：
                        <select class="_req select" style="width: 100px;" name="currency" id="costCurrency" >
                            <#list currencyTypes?keys as key>
                            <#if key==contractCost.currency>
                                <option value="${key}" selected="true"> ${currencyTypes[key]}</option>
                            <#else>
                                <option value="${key}"> ${currencyTypes[key]}</option>
                            </#if>
                            </#list>
                        </select>
	                </td>
	                <td align="left">
	                     <input type="button" value="删除" onclick="deleteContractCost(${countIndex});" class="ext_btn">
	                </td>
	                <td>
	                    <input type="hidden" value="" id="changeValue" name="changeValue"/>
	                </td>
	            </tr>
	            <#assign countIndexShow=countIndexShow+1>
                <#assign countIndex=countIndex+1> 
                </#list> 
                </#if>
	        </table>
        </td>
    </tr>
</table>
<hr style="border: 1px dashed #247DFF;width: 100%;" />

<table cellspacing="0" cellpadding="0" border="0" width="25%" class="form_table pt15 pb15" >
    <tbody>
        <tr>
           <td>保证金：</td><td><input type="text" size="10" class="_price input-text lh30" value="${(contractVO.cash)!}" name="contractDeposit"></td>
           <td>币种：</td>
           <td>
               <select class="_req select" style="width: 100px;" name="currency" id="costCurrency" >
                    <#list currencyTypes?keys as key>
                    <#if contractVO?? && key==contractVO.currency>
                        <option value="${key}" selected="true"> ${currencyTypes[key]}</option>
                    <#else>
                        <option value="${key}"> ${currencyTypes[key]}</option>
                    </#if>
                    </#list>
                </select>
           </td>
        </tr>
    </tbody>
</table>
<hr style="border: 1px dashed #247DFF;width: 100%;" />

<#--采购联系方式 start-->
<table cellspacing="0" cellpadding="0" border="0" width="50%" class="form_table pt15 pb15"> 
	<tbody>
		<tr>
			<td width="26%">经办、采购及其联系方式（乙方）：</td>
			<td width="25%">
				<select style="width:140px;" name="suppLinkType" class="select" id="supplier_link_select">
				    <option value="">请选择</option>
				    <#list supplierLinkTypes?keys as key>
                    <#if key==(contractVO.contractProperties.spLinkType)!"">
                    <option value="${key}" selected="true"> ${supplierLinkTypes[key]}</option>
                    <#else>
                    <option value="${key}"> ${supplierLinkTypes[key]}</option>
                    </#if>
                    </#list>
				</select>
			</td>
			<td>
			    <select style="width:140px;" name="suppLinkName" class="select" id="supplier_link_name">
			        <#if supplierVo?exists && contractVO?? >
			        <#list supplierVo.supplierLinkList as supplierLinker>
			            <#if supplierLinker.linkType == (contractVO.contractProperties.spLinkType)!"" >
                            <option value="${(supplierLinker.linkName)!}" selected="true"> ${(supplierLinker.linkName)!}</option>
			            <#else>
			                <option value="${(supplierLinker.linkName)!}"> ${(supplierLinker.linkName)!}</option>
			            </#if>
			        </#list>
			        </#if>
				</select>
		    </td>
		</tr>
		<tr>
		    <td colspan="3">
		        <table id="supplier_link_table">
		            <tr>
						<td class="td_right">手机号码：</td>
						<td width="100"> <input type="text" size="40" class="_mobile input-text lh30" value="${(contractVO.contractProperties.spMobilePhone)!}" name="supplierLinkMobile" /></td>
						<td class="td_right">电子邮箱：</td>
						<td> <input type="text" size="40" class="_email input-text lh30" value="${(contractVO.contractProperties.spEmail)!}" name="supplierLinkEmail" /></td>
					</tr>
					<tr id="supplier_link_tr3_2">
						<td class="td_right">固定电话：</td>
						<td width="100"> <input type="text" size="40" class="_tel input-text lh30" value="${(contractVO.contractProperties.spTelephone)!}" name="supplierLinkTel" /></td>
						<td class="td_right">传真号码：</td>
						<td> <input type="text" size="40" class="input-text lh30" maxlength="40" value="${(contractVO.contractProperties.spFax)!}" name="supplierLinkFaq" /></td>
					</tr>
					<tr id="supplier_link_tr4_2">
						<td class="td_right">地址：</td>
						<td width="100"> <input type="text" size="40" class="input-text lh30" value="${(contractVO.contractProperties.spLinkAddress)!}" name="supplierLinkAddr" /></td>
						<td class="td_right">QQ：</td>
						<td> <input type="text" size="40" class="input-text lh30" maxlength="40" value="${(contractVO.contractProperties.spQq)!}" name="supplierLinkQQ" /></td>
					</tr>
		        </table>
		    </td>
		</tr>
		<tr>
			<td width="26%">经办、采购及其联系方式（甲方）：</td>
			<td width="25%">部门：
				<select style="width:140px;" name="xgLinkType" class="select" id="mt_linktype_select">
				    <option>请选择</option>
				    <#if contractVO?exists> 
				    <#list departmentList as department>
				        <#if department.id == (contractVO.contractProperties.xgDeptId)!"">
	                    <option value="${(department.id)!}" selected="selected">${(department.name)!}</option>
	                    <#else>
	                    <option value="${(department.id)!}">${(department.name)!}</option>
	                    </#if>
	                </#list>
	                <#else>
						<#list departmentList as department>
 							<option value="${(department.id)!}">${(department.name)!}</option>
						</#list>
	                </#if>
				</select>
				<input type="hidden" value="${(contractVO.contractProperties.xgDeptName)!}" name="xgLinktypeValue" id="mt_linktype_select_value" />
			</td>
			<td>姓名：
			    <select style="width:140px;" name="xgLinkName" class="select" id="mt_linkname_select">
			        <#if departmentUserMap?exists>
                    	<#list departmentUserMap[contractVO.contractProperties.xgDeptId] as spUser>
                        	<#if (spUser.id)?string == contractVO.contractProperties.xgUserId>
                            	<option value="${spUser.id}" selected="selected">${spUser.userName}</option>
                            <#else>
                                <option value="${spUser.id}">${spUser.userName}</option>
                    		</#if>
                    	</#list>
                    </#if>
				</select>
				<input type="hidden" value="${(contractVO.contractProperties.xgUserName)!}" name="xgLinkNameValue" id="mt_linkname_select_value" />
		    </td>
		</tr>
		<tr>
		    <td colspan="3">
		        <table id="mt_user_link_table">
		            <tr>
						<td class="td_right">手机号码：</td>
						<td width="100"> <input type="text" size="40" class="input-text lh30" value="${(contractVO.contractProperties.xgMobilePhone)!}" name="xgLinkMobile" /></td>
						<td class="td_right">电子邮箱：</td>
						<td> <input type="text" size="40" class="_email input-text lh30" value="${(contractVO.contractProperties.xgEmail)!}" name="xgLinkEmail" /></td>
					</tr>
					<tr>
						<td class="td_right">固定电话：</td>
						<td width="100"> <input type="text" size="40" class="input-text lh30" value="${(contractVO.contractProperties.xgTelephone)!}" name="xgLinkTel" /></td>
						<td class="td_right">传真号码：</td>
						<td> <input type="text" size="40" class="input-text lh30" value="${(contractVO.contractProperties.xgFax)!}" name="xgLinkFaq" /></td>
					</tr>
					<tr>
						<td class="td_right">地址：</td>
						<td width="100"><input type="text" size="40" class="input-text lh30" value="${(contractVO.contractProperties.xgLinkAddress)!}" name="xgLinkAddr" /></td>
						<td class="td_right">QQ：</td>
						<td> <input type="text" size="40" value="${(contractVO.contractProperties.xgQq)!}" class="input-text lh30" name="xgLinkQQ" /></td>
					</tr>
		        </table>
		    </td>
		</tr>
	</tbody>
</table>
<hr style="border: 1px dashed #247DFF;width: 100%;" />
<#--采购联系方式 end-->

<#--接收及汇出款项的银行信息 start-->
<table cellspacing="0" cellpadding="0" border="0" width="65%" class="form_table pt15 pb15"> 
	<tbody>
		<tr>
			<td width="17%"><font color="red">*</font>接收及汇出款项的银行信息：</td>
			<td>
			    <select style="width:140px;" id="contractBankAccountSel" class="select">
			        <option value="">请选择</option>
			        <#if supplierVo?exists>
			        <#list supplierVo.supplierBankaccountList as supplierBank>
			            <#if supplierBank.bankAccount == (contractVO.contractProperties.bankAccount)!"">
			            <option selected="selected" value="${supplierBank.bankAccount}">${supplierBank.bankAccount}</option>
			            <#else>
			            <option value="${supplierBank.bankAccount}">${supplierBank.bankAccount}</option>
			            </#if>
			        </#list>
			        </#if>
				</select>
		    </td>
		</tr>
		<tr>
		    <td colspan="2">
		        <table>
		            <tr id="supplierBankTrArea">
						<td class="td_right">银行名称：</td>
						<td width="100"> <input type="text" size="20" class="input-text lh30" value="${(contractVO.contractProperties.bankName)!}" name="contractBankName" /></td>
						<td class="td_right">银行账号：</td>
						<td> <input type="text" size="20" class="input-text lh30" value="${(contractVO.contractProperties.bankAccount)!}" name="contractBankAccount" /></td>
						<td class="td_right">开户名称：</td>
						<td width="100"> <input type="text" size="20" class="_req input-text lh30" value="${(contractVO.contractProperties.bankAccName)!}" name="contractAccountName" /></td>
						<td class="td_right">币种：</td>
						<td>
						    <select style="width:140px;" name="receBankCurrency" class="select">
	                            <#list currencyTypes?keys as key>
	                            <#if key==(contractVO.contractProperties.bankCurrency)!"">
	                                <option value="${key}" selected="true"> ${currencyTypes[key]}</option>
	                            <#else>
	                                <option value="${key}"> ${currencyTypes[key]}</option>
	                            </#if>
	                            </#list>
							</select>
						</td>
					</tr>
		        </table>
		    </td>
		</tr>
	</tbody>
</table>
<hr style="border: 1px dashed #247DFF;width: 100%;" />
<#--接收及汇出款项的银行信息 end-->

<#--开票信息 start-->
<table cellspacing="0" cellpadding="0" border="0" width="50%" class="form_table pt15 pb15"> 
	<tbody>
		<tr>
			<td width="10%">开票信息：</td>
			<td>开票名称
				<select style="width:140px;" id="invoiceNameSelect" class="select" name="contracInvoiceName">
			        <#if supplierVo?exists>
			            <#list supplierVo.supplierInvoiceList as supplierInvoice>
			                <#if supplierInvoice.name == (contractVO.contractProperties.spInvoiceName)!"">
			                <option value="${supplierInvoice.name}" selected="selected">${supplierInvoice.name}</option>
			                <#else>
			                <option value="${supplierInvoice.name}">${supplierInvoice.name}</option>
			                </#if>
			            </#list>
			        </#if>
				</select>
			</td>
		</tr>
		<tr>
		    <td colspan="2">
		        <table id="invoiceNameTrArea">
		            <tr>
						<td class="td_right">纳税人识别码：</td>
						<td width="100"> <input type="text" size="40" class="input-text lh30" value="${(contractVO.contractProperties.spTaxpayerCode)!}" name="taxpayerCode" /></td>
						<td class="td_right">开户银行：</td>
						<td> <input type="text" size="40" class="input-text lh30" value="${(contractVO.contractProperties.spBankName)!}" name="kpBank" /></td>
					</tr>
					<tr>
						<td class="td_right">开户名称：</td>
						<td width="100"> <input type="text" size="40" class="input-text lh30" value="${(contractVO.contractProperties.spBankAccName)!}" name="kpAccountName" /></td>
						<td class="td_right">开户银行账号：</td>
						<td> <input type="text" size="40" class="input-text lh30" value="${(contractVO.contractProperties.spBankAccount)!}" name="kpAccount" /></td>
					</tr>
					<tr>
						<td class="td_right">联系电话：</td>
						<td width="100"> <input type="text" size="40" value="${(contractVO.contractPropertiesVO.spLinkPhone)!}"  class="input-text lh30" name="kpTel" /></td>
						<td class="td_right">联系地址：</td>
						<td> <input type="text" size="40" class="input-text lh30" value="${(contractVO.contractPropertiesVO.spInvoiceLinkAddress)!}" name="kpAddress" /></td>
					</tr>
		        </table>
		    </td>
		</tr>
	</tbody>
</table>
<hr style="border: 1px dashed #247DFF;width: 100%;" />
<#--开票信息 end-->

<#--报价单-->
<table class="form_table pt15 pb15" cellspacing="0" cellpadding="0" border="0" width="70%">
    <tbody>
       <tr align="left">
            <td colspan="5" style="border: 0px;">报价单
                <a class="ext_btn" href="javascript:void(0)" onclick="addQuotation()"><span class="add"></span>新增</a>
            </td>
        </tr>
        <tr>
            <td colspan="6" align="center">
                <table width="100%" cellspacing="0" cellpadding="0" class="list_table CRZ" >
	                <thead>
				        <tr align="center">
				            <th>单号</th>
				            <th>名称</th>
				            <th>有效期</th>
				            <#--<th>状态</th>-->
				            <th>操作</th>
				        </tr>
				     </thead>
	                
	                <tbody>
	                    <#if (contractVO.quotationInfoList)?exists> 
					    <#assign countIndex=1000> 
					    <#assign countIndexShow=1>
					    <#list contractVO.quotationInfoList as quoVO>
				        <tr align="center">
				            <td>${quoVO.id}</td>
				            <td>${quoVO.quotationName}</td>
				            <td>${(quoVO.startDate?string("yyyy-MM-dd"))!}到${(quoVO.endDate?string("yyyy-MM-dd"))!}</td>
				            <#--<td>${quoVO.auditStatusName}</td>-->
				            <td><a href="javascript:void(0)" onclick="showQuotationDetailPage(${quoVO.id})">详情</a></td>
				        </tr>
				        <#assign countIndexShow=countIndexShow+1>
				        <#assign countIndex=countIndex+1> 
				        </#list> 
				        </#if>
	                </tbody>
                </table>
            </td>
        </tr>
    </tbody>
</table>    


<#--报价单-->
<#-- <table cellspacing="0" cellpadding="0" border="0" width="80%"  class="form_table pt15 pb15 " style="width:1085px;">
    <thead>
        <tr align="left">
            <td colspan="5" style="border: 0px;">报价单
                <a class="ext_btn" href="javascript:void(0)" onclick="addQuotation()"><span class="add"></span>新增</a>
            </td>
        </tr>
    </thead>
    <tbody>
	    <tr>
		    <td>
			    <table width="100%" cellspacing="0" cellpadding="0" class="list_table CRZ" >
				    <thead>
				        <tr align="center">
				            <td>单号</td>
				            <td>名称</td>
				            <td>有效期</td>
				            <td>状态</td>
				            <td>操作</td>
				        </tr>
				     </thead>
				     <tbody>
				        <#if (contractVO.quotationVOList)?exists> 
					    <#assign countIndex=1000> 
					    <#assign countIndexShow=1>
					    <#list contractVO.quotationVOList as quoVO>
				        <tr align="center">
				            <td>${quoVO.id}</td>
				            <td>${quoVO.quotationName}</td>
				            <td>${(quoVO.startDate?string("yyyy-MM-dd"))!}到${(quoVO.endDate?string("yyyy-MM-dd"))!}</td>
				            <td>${quoVO.auditStatusName}</td>
				            <td><a href="javascript:void(0)" onclick="showQuotationDetailPage(${quoVO.id})">详情</a></td>
				        </tr>
				        <#assign countIndexShow=countIndexShow+1>
				        <#assign countIndex=countIndex+1> 
				        </#list> 
				        </#if>
			            </tbody>
			        </table>
		        </td>
	        </tr>
    </tbody>
</table>-->
<hr style="border: 1px dashed #247DFF; width:100%;" />
<table cellspacing="0" cellpadding="0" border="0" width="100%"  class="form_table pt15 pb15" style="width:585px;">
    <tbody>
        <tr>
            <td width="30" nowrap><font color="red">*</font>签约人</td>
            <td width="30" nowrap></td>
        </tr>
        <tr>
            <td align="center" nowrap width="100">
                <div class="select_border">
                    <div class="select_containers">
                        <span class="fl" id="contractorDeptSpan">部门：
               					<#if contractVO?exists> 
                                    <select name="contractorDeptId" id="contractorDeptId" style="width:100px;" class="_req select">
                                        <option>请选择</option>
                                        <#list departmentList as department>
                                            <#if contractVO.contractorDeptId==department.id>
                                               <option value="${(department.id)!}" selected="selected">${(department.name)!}</option>
                                            <#else>
                                                <option value="${(department.id)!}">${(department.name)!}</option>
                                            </#if>
                                        </#list>
                                    </select>
                                    <input type="hidden" id="contractorDeptName" name="contractorDeptName" value="${contractVO.contractorDeptName}">
                           		</#if>
                        </span>
                    </div>
                </div>
            </td>
            <td align="left" width="100" nowrap>
            	<div class="select_border">
                	<div class="select_containers">
                    	<span class="fl">姓名：
	                        	<select name="contractorId" id="contractorId" style="width:100px;" class="_req select">
	                        		<option>请选择</option>
	                            	<#if departmentUserMap?exists>
	                                	<#list departmentUserMap[contractVO.contractorDeptId] as spUser>
	                                    	<#if (spUser.id)?string == contractVO.contractorId>
	                                        	<option value="${spUser.id}" selected="selected">${spUser.userName}</option>
	                                        <#else>
	                                            <option value="${spUser.id}">${spUser.userName}</option>
	                                		</#if>
	                                	</#list>
	                                </#if>
	                           	</select>
	                        	<input type="hidden" id="contractorName" name="contractorName" value="${contractVO.contractorName}">
                    	</span>
                 	</div>
             	</div>
             </td>
        </tr>
    </tbody>
</table>
<hr style="border: 1px dashed #247DFF; width:100%;" />
<input type="hidden" id="fileUploadIndex" value="0" />
<input type="hidden" value="0" id="fieldIdDefined" />
<input type="hidden" value="${(contractVO.contractCode)!}" name="contractCode" id="contractCodeHidden" />
<input type="hidden" id="all_page_add_contract_type" name="contractType" value="${(contractVO.contractType)!}" readonly="true">