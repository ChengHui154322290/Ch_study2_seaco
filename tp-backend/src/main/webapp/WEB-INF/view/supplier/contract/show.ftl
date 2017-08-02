<#include "/common/common.ftl"/>
<@backend title="合同详情" js=[
        '/statics/supplier/js/jquery.form.js',
        '/statics/supplier/js/common.js',
        '/statics/supplier/js/pop_common.js',
        '/statics/supplier/js/image_upload.js',
        '/statics/supplier/js/validator.js',
        '/statics/supplier/js/web/contract_add.js',
        '/statics/supplier/js/web/productadd_layer.js',
        '/statics/supplier/js/pop_common.js', 
        '/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
        '/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js'] 
    css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >
 <script>
    var actionType = 'detail';
</script>
<style type="text/css">
.pb15 {
    padding-bottom: 0px;
}
</style>
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">合同详情</b>
            </div>
            <div class="box_center" style="margin-bottom: 30px;">
                <form id="contract_edit_form" action="${domain}/supplier/contract/editSave.htm" method="post">
                    <#include "/supplier/contract/subpage/sp_com_contractbase_show.ftl" />
                    <input type="hidden" value="0" id="indexNumHiddenId" />
                    <input type="hidden" name="cid" value="${contractVO.id}"/>
                    <#assign attachUrl="">
                    <#if (contractVO.contractAttachVOList)??>
                    <#list contractVO.contractAttachVOList as attachDO>
                    <#if (attachDO.url)??>
                        <#assign attachUrl=attachDO.url>
                    </#if>
                    </#list>
                    </#if>
                    <input type="hidden" value="${attachUrl}" name="url" id="urlId" />
                    <input type="hidden" value="" name="status" id="statusSet" />
                </form>
                <form id="fileForm_agreementContract" action="${domain}/supplier/upload/fileUpload.htm" method="post" enctype="multipart/form-data">
                <table width="100%" border="0" cellspacing="0" cellpadding="0" style="width:1085px;" class="form_table pt15 pb15">
                     <tr align="left">
			            <td align="right" width="12%">
			                <#if (contractVO.isAgreementContract)?exists && contractVO.isAgreementContract==1>协议合同：
			                <#else>非协议合同
			                </#if>
			            </td>
			            <td>
			                <#if (contractVO.isAgreementContract)?exists && contractVO.isAgreementContract==1>
			                <span style="margin-left:10px; id="uploadAggermentContractSpan" >
	                            <a href="javascript:void(0)" onclick="return downloadFile('${(contractVO.agreementContractUrl)!}')">下载</a>
                            </span>
                            <#else>
			                <span style="margin-left:10px;display:none;" id="uploadAggermentContractSpan" >
                            </span>
			                </#if>
                        </td>
			        </tr>
                </table>
                </form>
                <hr style="border: 1px dashed #247DFF; width:100%;" />
                <form id="fileForm_url" action="${domain}/supplier/upload/fileUpload.htm" method="post" enctype="multipart/form-data">
                <table width="100%" border="0" cellspacing="0" cellpadding="0" style="width:1085px;" class="form_table pt15 pb15">
                     <tr align="left">
			            <td colspan="2" style="border: 0px;">合同附件
			            </td>
			        </tr>
                    <tr>
                        <td align="center" class="td_right" style="width:270px;">上传合同附件（最大上传20M文件）</td>
                        <td>
                            <#-- <input type="file" name="url" id="url_file" showTag="url"
                            class="_fileupload input-text lh30" size="10">  -->
                            <input type="hidden" value="url" name="fileName" />
                            <#if (contractVO.contractAttachVOList)??>
                            <#list contractVO.contractAttachVOList as attachDO>
                            <#if (attachDO.url)??>
                                <a href="javascript:void(0)" onclick="return downloadFile('${attachDO.url}')">下载</a>
                            </#if>
                            </#list>
                            </#if>
                        </td>
                    </tr>
                </table>
                </form>
                <table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
                    <tbody>
                        <tr>
                            <td class="td_right"></td>
                            <td class="td_right"></td>
                            <td align="center">
                                <#--   
                                <input class="btn btn82 btn_save2" type="button" id="contract_edit_save" value="保存" name="button">
                                <input class="btn btn82 btn_save2" type="button" id="contract_edit_submit" value="提交" name="button">
                                
                                <input class="btn btn82 btn_save2" type="button" id="contractShowAudit" value="审核" name="button">
                                <input class="btn btn82 btn_nochecked" type="button" value="取消" name="button">
                                -->
                                
                                <#if contractVO.auditStatus == 4>
                                <input type="button" value="终止" id="contractShowStop" class="btn btn82 btn_nochecked" name="button">
								<!-- by zhs 0130 原contractVO.isAgreementContract != 'true'-->
                                <#if contractVO.isAgreementContract == 0>
                                <#--  <input class="btn btn82 btn_search" type="button" id="contract_pre_view" value="预览" name="button">-->
                                <input class="btn btn82 btn_export" type="button" id="contract_download" value="下载" name="button">
                                </#if>
                                <#--   <input class="btn btn82 btn_nochecked" id="contract_cancel" type="button" value="取消" name="button">-->
                                <#elseif contractVO.auditStatus == 2>
                                <input type="button" value="审核" id="contractShowAudit" class="btn btn82 btn_add" name="button">
                                <#if contractVO.isAgreementContract == 0>
                                <input class="btn btn82 btn_search" type="button" id="contract_pre_view" value="预览" name="button">
                                </#if>
                                <#-- <input class="btn btn82 btn_nochecked" id="contract_cancel" type="button" value="取消" name="button">-->
                                <#else>
                                </#if>
                            </td>
                            <td class="td_right"></td>
                            <td class="td_right"></td>
                            <td class="td_right"></td>
                        </tr>
                    </tbody>
                </table>
                <p><hr style="border:1px dashed #247DFF;" /></p>
                <#include "/supplier/common/audit_records.ftl" />
            </div>
        </div>
    </div>
</div>
</@backend>