<table cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15">
    <tbody>
        <tr>
            <td class="td_right"><font color="red">*</font>品牌授权证明： </td>
            <td class=""  width="220" colspan="3">
                <form id="imageForm_brandLiscence" action="/uploadFile.htm?bucketname=supplier" method="post" enctype="multipart/form-data">
                    <span class="a-upload">
		                    <input type="file" name="file" id="ppsqzm_file" showTag="brandLiscence" showType="2" 
		                            class="_mulupload input-text lh30" size="10">
                            <span>点击添加品牌授权证明</span>
                    </span>
                    <input type="hidden" value="brandLiscence" name="fileName" />
                </form>
                <div id="imgSpan_brandLiscence">
                    <#if (supplierAttacheVO.supplierImageList)?exists>
                    <#assign countIndex=1000>
                    <#list supplierAttacheVO.supplierImageList as imgVO>
                    <#if imgVO.imageType == 'brandLiscence'>
                    <div id="image_span_${countIndex}" style="padding:5px;width: 800px;">
                                                                         品牌<font color="red">*</font>：
	                  <select class="select" id="pre_text_field_${countIndex}" style="width: 120px;">
	                   <#if (supplierDTO.uniSupplierCategoryList)?exists>
	                    <#list supplierDTO.uniSupplierCategoryList as brandVO>
	                        <#if brandVO.brandName == imgVO.brandName>  
	                              <option selected="selected" value="${brandVO.brandName}">${brandVO.brandName}</option>
	                        <#else>  
	                              <option value="${brandVO.brandName}">${brandVO.brandName}</option>
	                        </#if>   
	                    </#list>
	                    </#if>
						</select>
	               &nbsp;&nbsp;&nbsp;&nbsp;销售商品类别描述（如：食品，服装）：<input type="text" class="input-text lh30" id="pre_desc_field_${countIndex}" value="${imgVO.description}">&nbsp;&nbsp;<input type="button" class="ext_btn" onclick="showImage('brandImgView','${bucketURL}${imgVO.imageUrl}')" value="预览">&nbsp;&nbsp;<a href="javascript:deleteImg(${countIndex})">删除</a>
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
                <form id="imageForm_specialPapers" action="/uploadFile.htm?bucketname=supplier" method="post" enctype="multipart/form-data">
                    <span class="a-upload">
	                    <input type="file" id="specialPapers_file" name="file" showTag="specialPapers" showType="1" 
	                            class="_mulupload input-text lh30" size="10">
	                    <span>点击添加资质证明</span>
                    </span>
                    <input type="hidden" value="specialPapers" name="fileName" />
                </form>
                <div id="imgSpan_specialPapers">
                    <#if (supplierAttacheVO.supplierImageList)?exists>
                    <#assign countIndex=2000>
                    <#list supplierAttacheVO.supplierImageList as imgVO>
                    <#if imgVO.imageType == 'specialPapers'>
                    <div id="image_span_${countIndex}"  style="padding:5px;width: 800px;">说明：<input type="text" class="input-text lh30" id="pre_desc_field_${countIndex}" value="${imgVO.description}">&nbsp;&nbsp;<input type="button" class="ext_btn" onclick="showImage('brandImgView','${bucketURL}${imgVO.imageUrl}')" value="预览">&nbsp;&nbsp;<a href="javascript:deleteImg(${countIndex})">删除</a><br>
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
                <form id="fileForm_qualityLiscence" action="/uploadFile.htm?bucketname=supplier" method="post" enctype="multipart/form-data">
					<div>
					<input type="file" name="file" id="qualityLiscence_file" showTag="qualityLiscence"
                            class="_req _fileupload input-text lh30" size="10">
                    <input type="hidden" value="qualityLiscence" name="fileName" />
					<!-- by zhs 0128 -->
					<#if supplierAttacheVO.qualityLiscence?? && supplierAttacheVO.qualityLiscence != '' >
					<input disabled="true" value="已存在质检报告" />
					</#if>
					</div>
                </form>
            </td>
        </tr>
    </tbody>
</table>
<input type="hidden" id="fileUploadIndex" value="0" />