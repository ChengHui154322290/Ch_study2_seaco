<#include "/common/common.ftl"/>
<@backend title="添加供应商" js=[
    '/statics/supplier/js/common.js',
    '/statics/backend/js/user/upload-file.js'
    ] 
    css=[] >
    ${message}!
</@backend>
<div class="tc">
	<input type="button" value="确定" id= "confirmImportBtn" class="ext_btn ext_btn_submit m10"  logId="${logId}"  />
</div>