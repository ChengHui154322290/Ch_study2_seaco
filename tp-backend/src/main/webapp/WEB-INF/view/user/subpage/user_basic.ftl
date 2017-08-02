<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pb15">
    <tbody>
    	<tr>
            <td class="td_right">会员账号： </td>
            <td class="" >
             <input type="hidden" id="username" value="${(username)!}"   />
                ${(username)!}
            </td>
            <td class="td_right">会员状态</td>
           
            <td>
                 <#if backendObj.status??>
	                  <#if backendObj.status==1>
	                      <span id="statusText">正常</span>
	                   <#else>
	                     <span  id="statusText" >冻结</span>
	                  </#if>
              </#if>
            </td>
        </tr>
        <tr>
            <td class="td_right">手机号码： </td>
            <td class=""  width="150">
            
                ${(backendObj.mobile)!}
            </td>
            <td class="td_right">邮箱：</td>
            <td>
                ${(backendObj.email)!}
            </td>
        </tr>
        <tr>
            <td class="td_right">性别： </td>
            <td class=""  width="150">
                ${(backendObj.sex)!}
            </td>
            <td class="td_right">出生年月：</td>
            <td>
                ${(backendObj.birthday)!}
            </td>
        </tr>
        <tr>
            <td class="td_right">注册时间： </td>
            <td class="">
               ${(backendObj.createTime?string("yyyy-MM-dd HH:mm:ss"))!'无数据 '}
            </td>
            <td class="td_right"></td>
            <td></td>
        </tr>
        <!--
        <tr>
            <td class="td_right">宝宝姓名： </td>
            <td class=""  width="150">
                ${(backendObj.babyName)!}
            </td>
            <td class="td_right">宝宝性别：</td>
            <td>
                ${(backendObj.babySex)!}
            </td>
        </tr>
        <tr>
            <td class="td_right">宝宝年龄： </td>
            <td class=""  width="150">
                ${(backendObj.babyAge)!}
            </td>
        </tr>
        -->
        
    </tbody>
</table>