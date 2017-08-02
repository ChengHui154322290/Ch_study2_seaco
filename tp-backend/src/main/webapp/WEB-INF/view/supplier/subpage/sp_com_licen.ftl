<form id="sp_licen_info_form" action="/supplier/supplierLicenSave.htm" method="post" enctype="multipart/form-data">
<#include "/supplier/subpage/sp_com_licen_1.ftl" />
<input type="hidden" name="spId" value="${supplierId}" />
<input type="hidden" name="needAudit" id="needAuditId" value="0" />
</form>
<#include "/supplier/subpage/sp_com_licen_2.ftl" />

