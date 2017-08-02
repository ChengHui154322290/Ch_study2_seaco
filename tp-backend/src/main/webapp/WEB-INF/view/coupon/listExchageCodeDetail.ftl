<div>
    <br/>

    <table class="input tabContent">
        <tr>
            <td colspan="3"><b class="pl15">已生成兑换券相关信息</b></td>
        </tr>
        <tr>
            <td>优惠券Id</td>
            <td>兑换券个数</td>
            <td>生成时间</td>
        </tr>
    <#list details as detail>
        <tr>
            <td>
            ${detail.couponId}
            </td>
            <td>
            ${detail.count}
            </td>
            <td>
            ${detail.createTime}
            </td>
        </tr>
    </#list>
    </table>
</div>