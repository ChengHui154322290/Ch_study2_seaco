<#include "/common/common.ftl"/>
<@backend title="团购列表"
js=['/statics/backend/js/jquery.min.js',
'/statics/backend/js/jquery.tools.js',
'/statics/backend/js/jquery.form.2.2.7.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/json2.js',
'/statics/backend/js/promotion/utils.js',
'/statics/supplier/component/date/WdatePicker.js',
'/statics/backend/js/editor/kindeditor-all.js',
'/statics/backend/js/seagoorpay/payinfo.js',
'/statics/select2/js/select2.js',
'/statics/select2/js/select2Util.js',
'/statics/select2/js/select2_locale_zh-CN.js'
]
css=['/statics/backend/js/dateTime2/css/jquery-ui-1.8.17.custom.css',
'/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css',
'/statics/backend/css/style.css',
'/statics/supplier/component/date/skin/WdatePicker.css',

'/statics/select2/css/select2.css',
'/statics/select2/css/common.css'

]>
<title>支付信息列表</title>
<form class="jqtransform" method="post" id="queryAttForm" action="${domain}/seagoorpay/payinfo.htm">
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">支付信息列表</b>
            </div>
            <table id="topicSearch" cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
                <tbody>
                <tr>
                    <td class="td_right" width="50" align="right">用户Id:</td>
                    <td class="" width="150" align="left"><input type="text" id="memberId" name="memberId" class="input-text lh30 topicInteger" value="${query.memberId!}"/></td>
                    <td class="td_right" width="50" align="right">用户手机号:</td>
                    <td class="" width="150" align="left"><input type="text" id="memberMobile" name="memberMobile" class="input-text lh30" value="${query.memberMobile!}" /></td>

                    <td class="td_right" width="50" align="right">状态:</td>
                    <td class="" width="150" align="left">
                        <select name="status" class="select" id="status" style="width:150px">
                            <option value="" <#if ("${query.status}"??)>selected</#if>>全部</option>
                            <option value="1" <#if ("${query.status}" == "1")>selected</#if>>未支付</option>
                            <option value="2" <#if ("${query.status}" == "2")>selected</#if>>支付成功</option>
                            <option value="3" <#if ("${query.status}" == "3")>selected</#if>>支付中</option>
                            <option value="4" <#if ("${query.status}" == "4")>selected</#if>>订单已关闭</option>
                            <option value="5" <#if ("${query.status}" == "5")>selected</#if>>支付错误</option>
                        </select>
                    </td>
                </tr>
                <tr  >

                    <td class="td_right" width="50" align="right">商家:</td>
                    <td class="" width="150" align="left">
                        <select class="select2" style="width:200px" id="merchantId" name="merchantId" class="input-text lh30" value="${query.merchantId!}">
                        <option value="">全部</option>
                        <#list mlist as  t >
                            <option value= ${t.merchantId} <#if query.merchantId == t.merchantId>selected</#if> >  ${t.merchantName}(${t.merchantId})</option>
                        </#list>

                    </select></td>
                    <td class="td_right" width="50" align="right">支付单号:</td>
                    <td class="" width="150" align="left"><input type="text" id="paymentCode" name="paymentCode" class="input-text lh30" value="${query.paymentCode!}" /></td>
                    <td class="td_right" width="50" align="right">商户支付单号:</td>
                    <td class="" width="150" align="left"><input type="text" id="merPayCode" name="merPayCode" class="input-text lh30" value="${query.merPayCode!}" /></td>
                </tr>
                <tr>
                    <td class="td_right" width="50" align="right">时间:</td>

                    <td class="" width="150" align="left" colspan="4">
                        <input type="text" class="input-text dateInput" width="20px" name="startTime" readonly="true" id="startTime"   value="<#if query.startTime??>${query.startTime?string('yyyy-MM-dd HH:mm:ss')}</#if>"  onClick="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>至
                        <input type="text" class="input-text dateInput" width="20px" name="endTime" readonly="true"  id="endTime" value="<#if query.endTime??>${query.endTime?string('yyyy-MM-dd HH:mm:ss')}</#if>"  onClick="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="8" align="center">
                        <input type="button" id="reset" class="btn btn82 btn_res" value="重置" style="margin-right:50px;" />
                        <input type="button" id="search" class="btn btn82 btn_search" value="查询"/>
                    </td>
                </tr>
                </tbody>
            </table>

            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr align="center">
                    <th>支付单号</th>
                    <th>商家支付单号</th>
                    <th>商家信息</th>
                    <th>用户Id</th>
                    <th>支付西客币数量</th>
                    <th>支付状态</th>
                    <th>商品描述</th>
                    <th>ip</th>
                    <th>创建时间</th>

                </tr>
                <#if (result.data??)>
                    <#list result.data.rows as info>
                        <tr>
                            <td class="td_center" style="width:30px;text-align:center;">
                                <span>${info.paymentCode}</span>
                            </td>
                            <td class="td_center" style="width:30px;text-align:center;">
                                <span>${info.merTradeCode}</span>
                            </td>
                            <td class="td_center" style="width:30px;text-align:center;">
                                <#list mlist as mm>
                                <#if mm.merchantId == info.merchantId> <span>${mm.merchantName}</span></#if>
                                </#list>

                            </td>
                            <td class="td_center" style="text-align:center;">
                                <span>${info.memberId}</span>
                            </td>
                            <td style="width:80px;text-align:center;">
                            ${info.totalFee}
                            </td>

                            <td style="width:80px;text-align:center;">
                                <#list paysStatusValues as st>
                                    <#if st.code == info.status>${st.desc}</#if>
                                </#list>
                            </td>
                            <td class="td_center" style="text-align:center;">${info.itemDesc}</td>
                            <td class="td_center" style="text-align:center;">${info.ip}</td>
                            <td class="td_center" style="text-align:center;"><#if (info.createTime??)>${info.createTime?string("yyyy-MM-dd HH:mm:ss")!}</#if></td>

                        </tr>
                    </#list>
                </#if>
            </table>
        </div>
    </div>


    <div style="text-align: left;width: 900px"> <@pager  pagination=result.data  formId="queryAttForm"  /></div>
</form>
</@backend>