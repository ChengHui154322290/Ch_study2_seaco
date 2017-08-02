<#-------------------------------------------->
<table border="0" cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
    <tbody>
        <tr>
            <td class="td_left" style="width:100px;">审核记录
            </td>
            <td align="center"">
                <table border="1" cellspacing="0" cellpadding="0" width="70%" class="list_table CRZ">
                    <tr align="center">
                        <td>姓名</td>
                        <td>操作</td>
                        <td>备注</td>
                        <td>时间</td>
                    </tr>
                    <#list auditRecords as audit>
                    <tr class="tr" align="center">
                        <td>${audit.userName}</td>
                        <td>
                            ${audit.operate}
                        </td>
                        <td>${audit.content}</td>
                        <td><#if audit.createTime?exists>
                                 ${audit.createTime?string("yyyy-MM-dd HH:mm:ss")}
                            </#if>
                        </td>
                    </tr>
                    </#list>
                </table>
            </td>
        </tr>
    </tbody>
</table>