
<form class="jqtransform" method="post" id="queryForm" action="${domain}/basedata/navigation/navHome.htm">
    <div>

        <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
            <div class="search_bar_btn" style="text-align:center;">
                <input class="btn btn82  addcatabtn add_nav_cat_sec" style="width: 150px" type="button" parentId="${parentId}" value="新增二级分类"/>
            </div>
        </div>
    </div>

    <div id="table" class="mt10">
        <div class="box span10 oh">
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                    <th width="50">ID</th>
                    <th width="100">类目名称</th>
                    <th width="50">类目级别</th>
                    <th width="50">状态</th>
                    <th width="50">顺序</th>
                    <th width="50">是否发布</th>
                    <th width="300">操作</th>
                </tr>
                <#list result.data as row>
                    <tr class="tr">
                        <td class="td_center">${row.id}
                            <input type="hidden" value=${row.id}/>
                        </td>
                        <td class="td_center">${row.name}</td>
                        <td class="td_center">${row.level}</td>
                        <td class="td_center"><#assign sta="${row.status}" /><#if sta=='1'>有效<#else>无效</#if></td>
                        <td class="td_center">${row.sort}</td>
                        <td class="td_center"> <#assign pub="${row.isPublish}" /><#if pub=='1'>
                            已发布<#else>未发布</#if></td>
                        <td class="td_center">
                            <#if row.isPublish = 0> <a href="javascript:void(0);" class="editcatabtn  for_pub"
                                                       param='${row.id}'>[发布]</a>&nbsp;&nbsp; </#if>
                            <#if row.isPublish = 1> <a href="javascript:void(0);" class="editcatabtn  for_un_pub"  param='${row.id}'>[撤销]</a>&nbsp;&nbsp; </#if>
                            <a href="javascript:void(0);" class="editcatabtn  for_edit" param='${row.id}'>[编辑]</a>
                            &nbsp;&nbsp;
                            <a href="javascript:void(0);" class="editcatabtn  for_load_sec_level" param='${row.id}' showSec="0">[查看筛选条件]</a>

                    </tr>
                  <tr style="display: none" id="tr_sec_level_${row.id}">
                      <td colspan="8">
                          <div id="sec_level_${row.id}" style="display: none">

                          </div>
                      </td>
                  </tr>
                </#list>
            </table>
        </div>
    </div>

</form>
