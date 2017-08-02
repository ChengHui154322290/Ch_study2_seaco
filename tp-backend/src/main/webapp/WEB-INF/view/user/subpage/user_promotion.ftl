<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
    <tbody>
        <tr>
            <td class="td_right">优惠券： </td>
            <td class=""  width="150">
                ${(backendObj.couponNum)!0}张
            </td>
            <td class="td_right">现金券：</td>
            <td>
                ${(backendObj.voucherNum)!0}个
            </td>
        </tr>
        <tr>
            <td class="td_right">最近登录记录： </td>
            <td class=""  width="150">
                ${(backendObj.lastLoginTime?string("yyyy-MM-dd HH:mm:ss"))!}
            </td>
        </tr>
    </tbody>
</table>