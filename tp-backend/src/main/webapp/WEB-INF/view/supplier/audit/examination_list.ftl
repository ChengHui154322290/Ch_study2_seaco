<#include "/common/common.ftl"/> 
<#include "/supplier/common/page.ftl" />
<@backend title="审批流设置" 
    js=['/statics/supplier/js/common.js',
        '/statics/supplier/js/web/examination_list.js'
       ] 
    css=[] >
<div class="mt10" id="forms">
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">采购管理->供应商->审批流设置</b> 
            </div>
            <div class="box_center">
                <form id="examination_list_form" class="jqtransform" action="" method="post">
                    <table cellspacing="0" cellpadding="0" border="0" width="100%"
                        class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td class="td_right" width="50" align="left">单据类型：</td>
                                <td align="left">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> 
                                            <select class="select" name="billType"
                                                style="width: 130px;">
                                                    <option value="">全部</option>
                                                    <#list billTypeMap?keys as key>
                                                    <option value="${key}">${billTypeMap[key]}</option>
                                                    </#list>
                                            </select>
                                            </span>
                                        </div>
                                    </div>
                                </td>
                            
                                <td class="td_right" width="50" align="right">版本：</td>
                                <td class="" width="50" align="left">
                                    <input type="text" name="billVersion" class="input-text lh30" size="17">
                                </td>

                                <td class="td_right" width="50" align="left">用户：</td>
                                <td class="" width="50" align="left">
                                    <input type="text" name="exUserName" class="input-text lh30" size="17">
                                </td>
                               
                                <td class="td_right" width="50" align="left">状态：</td>
                                <td align="left">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> 
                                            <select class="select" name="status"
                                                style="width: 130px;">
                                                    <option value="">全部</option>
                                                    <option value="1">有效</option>
                                                    <option value="0">无效</option>
                                            </select>
                                            </span>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="8" align="center">
                                    <input type="reset" class="btn btn82 btn_res" value="重置">
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                                    <input type="button" id="examinationListQuery" class="btn btn82 btn_search" value="查询">
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <input id="pageIndexId" type="hidden" value="1" name="index" />
                </form>
                <table cellspacing="0" cellpadding="0" border="0" width="100%"
                    class="form_table pt15 pb15">
                    <tbody>
                        <#--虚线-->
                        <tr>
                            <td colspan="8">
                                <hr style="border: 1px dashed #247DFF;" />
                            </td>
                        </tr>

                        <tr>
                            <td colspan="7" align="left">
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <input type="button" name="button" id="examination_list_add" class="btn btn82 btn_add" value="新增">&nbsp;&nbsp;&nbsp;
                                 <input type="button" name="button" id="" class="btn btn82 btn_add" value="复制">
                            </td>
                            <td align="center"><input type="button" value="？"
                                class="ext_btn ext_btn_submit"></td>
                        </tr>

                        <#--虚线-->
                        <tr>
                            <td colspan="8">
                                <hr style="border: 1px dashed #247DFF;" />
                            </td>
                        </tr>

                        <tr>
                            <td colspan="8">
                                <table width="100%" cellspacing="0" cellpadding="0" border="0"
                                    class="list_table CRZ" id="CRZ0">
                                    <tbody>
                                        <tr align="center">
                                            <th>编号</th>
                                            <th>单据</th>
                                            <th>版本</th>
                                            <th>审批流程</th>
                                            <th>状态</th>
                                            <th>操作</th>
                                        </tr>
                                    <#if page.list?default([])?size !=0>       
                                    <#list page.list as sl>
                                        <tr align="center" class="tr"
                                            style="background-color: rgb(255, 255, 255);">
                                            <td>${sl.id}</td>
                                            <td>${billTypeMap[sl.billType]}</td>
                                            <td>${sl.auditVersion}</td>
                                            <td>${sl.keyWords}</td>
                                            <td>
                                                <#if sl.status=='true'>启用
                                                <#elseif sl.status == 'false'>停用
                                                <#else>
                                                </#if>
                                            </td>
                                            <td><a href="javascript:void(0)" onclick="toEditPage(1)">编辑</a></td>
                                        </tr>
                                    </#list>
                                    </#if>
                                    </tbody>
                                </table>
                                <@p page=page.pageNo totalpage=page.getTotalPageCount() />
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</@backend>
