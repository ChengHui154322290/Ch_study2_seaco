<#include "/common/common.ftl"/>
<@backend title="" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/basedata/bse_navigation_home.js',
'/statics/backend/js/jqgrid5/js/i18n/grid.locale-cn.js',
'/statics/backend/js/jqgrid5/js/jquery.jqGrid.min.js'
]
css=[
'/statics/backend/js/jqgrid5/css/ui.jqgrid.css',
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'
] >
<style>
    .list_table th {
        /* height: 37px; */
        /* background: url('../images/box_top.png') 0px 0px repeat-x; */
        line-height: 16px;

</style>
<form class="jqtransform" method="post" id="queryForm" action="${domain}/basedata/navigation/navHome.htm">
    <div>
        <div>

        </div>

        <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
            <div class="search_bar_btn" style="text-align:left;">

                <input class="btn btn82 btn_add addcatabtn add_nav_cat" type="button" value="新增"/>
            </div>
        </div>
    </div>

    <div id="table" class="mt10">
        <div class="box span10 oh">
            <table width="100%" border="0" id="cat_table" cellpadding="0" cellspacing="0" class="list_table">
                <#--<tr>-->
                    <#--<th width="50">ID</th>-->
                    <#--<th width="100">类目名称</th>-->
                    <#--<th width="50">类目级别</th>-->
                    <#--<th width="50">状态</th>-->
                    <#--<th width="50">顺序</th>-->
                    <#--<th width="50">是否发布</th>-->
                    <#--<th width="100">图片</th>-->
                    <#--<th width="300">操作</th>-->
                <#--</tr>-->
                <#--<#list result.data.rows as row>-->
                    <#--<tr class="tr">-->
                        <#--<td class="td_center">${row.id}-->
                            <#--<input type="hidden" value=${row.id}/>-->
                        <#--</td>-->
                        <#--<td class="td_center">${row.name}</td>-->
                        <#--<td class="td_center">${row.level}</td>-->
                        <#--<td class="td_center"><#assign sta="${row.status}" /><#if sta=='1'>有效<#else>无效</#if></td>-->
                        <#--<td class="td_center">${row.sort}</td>-->
                        <#--<td class="td_center"> <#assign pub="${row.isPublish}" /><#if pub=='1'>-->
                            <#--已发布<#else>未发布</#if></td>-->
                        <#--<td class="td_center">${row.pic}</td>-->
                        <#--<td class="td_center">-->
                            <#--<#if row.isPublish = 0> <a href="javascript:void(0);" class="editcatabtn  for_pub"-->
                                                       <#--param='${row.id}'>[发布]</a>&nbsp;&nbsp; </#if>-->
                            <#--<#if row.isPublish = 1> <a href="javascript:void(0);" class="editcatabtn  for_un_pub"  param='${row.id}'>[撤销]</a>&nbsp;&nbsp; </#if>-->
                            <#--<a href="javascript:void(0);" class="editcatabtn  for_edit" param='${row.id}'>[编辑]</a>-->
                            <#--&nbsp;&nbsp;-->
                            <#--<a href="javascript:void(0);" class="editcatabtn  for_load_sec_level" param='${row.id}'>[查看二级目录]</a>-->
                            <#--&nbsp;&nbsp;-->
                            <#--<a href="javascript:void(0);" class="editcatabtn for_log" param='${row.id}'>[日志]</a></td>-->
                    <#--</tr>-->
                  <#--<tr style="display: none" id="tr_sec_level_${row.id}">-->
                      <#--<td colspan="8">-->
                          <#--<div id="sec_level_${row.id}" >-->

                          <#--</div>-->
                      <#--</td>-->
                  <#--</tr>-->
                <#--</#list>-->
            </table>
        </div>
    </div>
    <#--<@pager  pagination=result.data  formId="queryForm"  />-->
</form>
</@backend>