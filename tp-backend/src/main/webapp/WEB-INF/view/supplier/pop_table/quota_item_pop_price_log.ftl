<#include "/supplier/pop_table/context.ftl"/>
<@backend title="添加商品" js=[] 
    css=[] >
<div class="container" id="brandPopTable" style="z-index: 19891099;width: 821px; background: none repeat scroll 0% 0% rgb(255, 255, 255);">
    <#-- 
    <div class="box_top">
        <b class="pl15">新增报价单</b>  
        <a class="box_top_r fr pr15" id="popClosebtn" href="javascript:void(0);">关闭</a>  
    </div>
    -->
    <div class="box_center" style="overflow: auto;height: 430px;">
        <form method="post" action="" class="jqtransform" id="product_pop_form">

            <table width="100%" cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15">
                <tr><td colspan="3" class="td_center"><b>商品裸价历史</b>(最近5条记录)</td></tr>
                <tr>
                <td class="td_center" style="width: 200px">时间</td>
                <td  class="td_center" style="width: 100px">价格</td>
                <td  class="td_center"  style="width: 100px">用户</td>
                </tr>
                <tbody>
                <#if basePrices??>
                <#list basePrices as base>
                <tr>
                <td class="td_center"> ${base.startDate?string("yyyy年MM月dd日 HH:mm")} -  ${base.endDate?string("yyyy年MM月dd日 HH:mm")}</td>
                <td class="td_center">${base.price}</td>
                <td class="td_center">${base.createUser}</td>
                </tr>
                </#list>
                </#if>

                </tbody>
            </table>
            </br>
            <table width="100%" cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15">
                <tr><td colspan="3" class="td_center"><b>包邮包税代发价历史</b>(最近5条记录)	</td></tr>
                <tr>
                    <td  class="td_center" style="width: 200px"">时间</td>
                    <td  class="td_center">价格</td>
                    <td  class="td_center">用户</td>
                </tr>
                <tbody>
                    <#if basePrices??>
                        <#list sumPrices as base>
                        <tr>
                            <td class="td_center"> ${base.startDate?string("yyyy年MM月dd日 HH:mm")} -  ${base.endDate?string("yyyy年MM月dd日 HH:mm")}</td>
                            <td class="td_center">${base.price}</td>
                            <td class="td_center">${base.createUser}</td>
                        </tr>
                        </#list>
                    </#if>

                </tbody>
            </table>

        </form>
    </div>
</div>
</@backend>