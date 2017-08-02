<@backend title="会员信息" js=['/statics/backend/js/user/toShow.js']  >
<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
    <tbody>
        <tr>
            <td class="td_right">会员账号： </td>
            <td>
             <form id="searchUser" autocomplete="off">
                <input type="text" name="username" class="input-text" size="20">
                <input type="submit" class="btnuser btn82 btn_search" value="查   询">	
                       <#if backendObj.status==1>
	                      <input type="button" style="margin-left:100px" id="btnChangeStatus" class="btnuser btn82 btn_recycle" status="${backendObj.status}"  value="冻结" />
	                   <#elseif backendObj.status==0>
	                      <input type="button" style="margin-left:100px"  id="btnChangeStatus"  class="btnuser btn82 btn_recycle" status="${backendObj.status}"  value="开启" />
	                   </#if>
             </form>
            </td>
        </tr>
    </tbody>
</table>
</@backend>