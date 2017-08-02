<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
    <tbody>
        <tr>
            <td class="td_right"><font color="red">*</font>品牌授权证明： </td>
            <td class=""  width="220" colspan="3">
                <div id="imgSpan_brandLiscence">
                    <#if (supplierAttacheVO.supplierImageList)?exists>
                    <#assign countIndex=1000>
                    <#list supplierAttacheVO.supplierImageList as imgVO>
                    <#if imgVO.imageType == 'brandLiscence'>
                    <div id="image_span_${countIndex}" style="padding:5px;width: 800px;">品牌<font color="red">*</font>：
                        <select class="select" id="pre_text_field_${countIndex}" style="width: 120px;">
                           <#if (supplierDTO.uniSupplierCategoryList)?exists>
                            <#list supplierDTO.uniSupplierCategoryList as brandVO>
                                <#if brandVO.brandName == imgVO.brandName>  
                                      <option selected="selected" value="${brandVO.brandName}">${brandVO.brandName}</option>
                                </#if>   
                            </#list>
                        </#if>
                        </select>
                        &nbsp;&nbsp;&nbsp;&nbsp;销售商品类别描述（如：食品，服装）：
                        <input type="text" class="input-text lh30" id="pre_desc_field_${countIndex}" value="${imgVO.description}">
                        &nbsp;&nbsp;
                        <input type="button" class="ext_btn" onclick="showImage('brandImgView','${bucketURL}${imgVO.imageUrl}')" value="预览">
                    </div>
                    </#if>
                    <#assign countIndex=countIndex+1>
                    </#list>
                    </#if>
                </div>
            </td>
        </tr>
        <tr>
            <td class="td_right"><font color="red">*</font>特殊资质证件证明： </td>
            <td class=""  width="220" colspan="3">
                <div id="imgSpan_specialPapers">
                    <#if (supplierAttacheVO.supplierImageList)?exists>
                    <#assign countIndex=1000>
                    <#list supplierAttacheVO.supplierImageList as imgVO>
                    <#if imgVO.imageType == 'specialPapers'>
                    <div id="image_span_${countIndex}" style="padding:5px;width: 800px;">说明：
                        <input type="text" class="input-text lh30" id="pre_desc_field_${countIndex}" value="${imgVO.description}">
                        &nbsp;&nbsp;
                        <input type="button" class="ext_btn" onclick="showImage('brandImgView','${(dfsDomainUtil.getFileFullUrl(imgVO.imageUrl))!}')" value="预览">
                        <br>
                    </div>
                    </#if>
                    <#assign countIndex=countIndex+1>
                    </#list>
                    </#if>
                </div>
            </td>
        </tr>
        <tr>
            <td class="td_right"><font color="red">*</font>产品质检报告： </td>
            <td class=""  width="120" colspan="3">
                <#if supplierAttacheVO.qualityLiscence?exists >
                <a href="javascript:void(0)" onclick="return downloadFile('${supplierAttacheVO.qualityLiscence}')">下载</a>
                </#if>
            </td>
        </tr>
    </tbody>
</table>
<input type="hidden" id="fileUploadIndex" value="0" />