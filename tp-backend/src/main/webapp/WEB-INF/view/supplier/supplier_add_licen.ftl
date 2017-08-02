<#include "/common/common.ftl"/>
<@backend title="添加供应商" js=[
    '/statics/supplier/js/jquery.form.js',
    '/statics/supplier/js/common.js',
    '/statics/supplier/js/validator.js',
    '/statics/supplier/js/image_upload.js',
    '/statics/supplier/js/web/supplier_add_licen.js',
    '/statics/qiniu/js/plupload/plupload.full.min.js',
    '/statics/qiniu/js/plupload/plupload.dev.js',
    '/statics/qiniu/js/plupload/moxie.js',
	'/statics/qiniu/js/plupload/moxie.js',
	'/statics/qiniu/src/qiniu.js',
	'/statics/qiniu/js/highlight/highlight.js',
	'/statics/qiniu/js/ui.js',
	'/statics/backend/js/uplod.js'
    ] 
    css=[] >
<style type="text/css">
.pb15 {
    padding-bottom: 0px;
}
.container {
    margin-left: unset;
}
</style>
<script>
	var bucketURL = '${bucketURL}';
	var bucketname = '${bucketname}';
</script>
<div class="mt10" id="forms">
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">上传证件</b>
            </div>
            <div class="box_center" style="margin-bottom: 30px;">
                <#include "/supplier/subpage/sp_com_supplier_num.ftl" />
                <#include "/supplier/subpage/sp_com_licen.ftl" />
                <table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
                    <tbody>
                        <tr>
                            <td class="td_right"></td>
                            <td>
                                <input type="button" id="sp_add_lien_submit" value="保存" class="btn btn82 btn_save2" name="button">
                                <input type="button" id="sp_add_lien_submitAudit" value="提交" class="btn btn82 btn_save2" name="button">
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div style="display:none;">
	<select id="brandoptionselectid">
		<option value="">请选择 </option>
		<#if brandOption??>
		<#list brandOption as supplierCategory>
			<option value="${supplierCategory.brandName}">${supplierCategory.brandName}</option>
		</#list>
		</#if>
	</select>
</div>
</@backend>