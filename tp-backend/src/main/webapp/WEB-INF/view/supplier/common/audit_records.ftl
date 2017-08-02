<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
    <tbody>
        <tr>
            <td class="td_right">审核记录：
            </td>
            <td>
                <table cellspacing="0" cellpadding="0" border="0" width="100%" class="list_table CRZ">
                    <tr align="center">
                        <th  width="18%">姓名</th>
                        <th width="18%">操作</th>
                        <th width="18%">时间</th>
                        <th >备注</th>
                    </tr>
                    <#list auditRecords as audit>
                    <tr class="tr" align="center">
                        <td>${audit.userName}</td>
                        <td>
                            ${audit.operate}
                        </td>
                        <td><#if audit.createTime?exists>
                                 ${audit.createTime?string("yyyy-MM-dd HH:mm:ss")}
                            </#if>
                        </td>
                        <td>${audit.content}</td>
                    </tr>
                    </#list>
                </table>
            </td>
        </tr>
    </tbody>
</table>