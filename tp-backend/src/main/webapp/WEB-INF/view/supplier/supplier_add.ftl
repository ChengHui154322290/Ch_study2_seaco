<#include "/common/common.ftl"/>
<@backend title="添加供应商" js=[
        '/statics/supplier/js/common.js',
        '/statics/supplier/js/validator.js',
        '/statics/supplier/js/web/base_option.js',
        '/statics/supplier/js/web/supplier_add.js'] 
    css=[] >
<script>
    var isSupplierEdit = false;
</script>
<style type="text/css">
.pb15 {
    padding-bottom: 0px;
}
</style>
<div class="mt10" id="forms">
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">基本信息</b>
            </div>
            <div class="box_center" style="margin-bottom: 30px;">
                <form id="supp_add_form" class="jqtransform" method="post" action="/supplier/supplierBaseSave.htm">
                    <#include "/supplier/subpage/sp_com_base.ftl" />
                    <table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td class="td_right"></td>
                                <td>
                                    <input type="button" id="supplier_add_save" value="保存" class="btn btn82 btn_save2" name="button">
                                    <#--<input type="button" id="supplier_add_cancel" value="取消" class="btn btn82 btn_save2" name="button">-->
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </form>
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
	<select id="customsChannelListid">
		<option value="">请选择 </option>
		<#if customsChannelList??>
		<#list customsChannelList as clearanceChannels>
			<option value="${clearanceChannels.id}">${clearanceChannels.name}</option>
		</#list>
		</#if>
	</select>
	<select id="supplierBankTypes">
		<option value="">请选择 </option>
		<#if bankTypeList??>
		<#list bankTypeList as supplierBankType>
			<option value="${supplierBankType.getValue()}">${supplierBankType.getName()}</option>
		</#list>
		</#if>
	</select>
	<select id="departmentList">
		<#if departmentList??>
		<#list departmentList as department>
			<option value="${department.id}">${department.name}</option>
		</#list>
		</#if>
	</select>
</div>
</@backend>