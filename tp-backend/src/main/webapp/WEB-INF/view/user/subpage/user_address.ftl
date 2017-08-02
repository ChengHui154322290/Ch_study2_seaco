<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pb15">
    <tbody>
    <#assign countIndex=0>
    <#list backendObj.address as address>
        <tr>
            <#if countIndex == 0 >
            	<td class="td_right" width="150">收货地址： </td>
            </#if>
        	<#if countIndex != 0 >
            	<td class="td_right" width="150">       </td>
            </#if>
            <td class="">
              ${address}
            </td>
        </tr>
    <#assign countIndex=countIndex+1>
    </#list>
    </tbody>
</table>