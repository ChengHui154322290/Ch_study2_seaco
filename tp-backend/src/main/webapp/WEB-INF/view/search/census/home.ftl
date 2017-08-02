<#include "/common/common.ftl"/>
<@backend title=" "
js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/supplier/component/date/WdatePicker.js']
css=[
'/statics/supplier/component/date/skin/WdatePicker.css'
] >
<form class="jqtransform"  method="post" id="queryAttForm" action="${domain}/search/census/home.htm">
    <div>
        <div>
            <table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
                <tbody>
                <tr>
                    <td class="td_right" width="50" align="left">开始时间：</td>
                    <td class="" width="50" align="left">
                        <input type="text" name="startTime" class="input-text lh30 Wdate"
                               value='<#if query.startTime??>${query.startTime?string("yyyy-MM-dd")}</#if>'
                               onClick="WdatePicker({isShowClear:true,readOnly:true})">
                    </td>
                    <td class="td_right" width="50" align="left">结束时间：</td>
                    <td class="" width="50" align="left">
                        <input type="text" name="endTime" class="input-text lh30 Wdate"
                               value='<#if query.endTime??>${query.endTime?string("yyyy-MM-dd")}</#if>'
                               onClick="WdatePicker({isShowClear:true,readOnly:true})">
                    </td>



                    <td class="td_right" width="50" align="right">统计类型</td>
                    <td align="left">
                        <label><input type="radio" value="1" name="searchType" <#if query.searchType==1> checked</#if>>搜索关键字
                        </label>&nbsp;&nbsp;
                        <label><input type="radio" value="2" name="searchType" <#if query.searchType==2> checked</#if>>分类导航
                        </label>&nbsp;&nbsp;
                        <label><input type="radio" value="3" name="searchType" <#if query.searchType==3> checked</#if>>品牌导航
                        </label>&nbsp;&nbsp;
                    </td>
                    </tr>


            </table>
        </div>
        <div style="text-align: center">
           <input class="btn btn82 btn_search" type="submit" id="searthAtt" value="查询">

        </div>


    </div>

    <div id="table" class="mt10">
        <div class="box span10 oh">
            <table width="100%" border="0" cellpadding="0" style="width:60%" cellspacing="0" class="list_table">
                <tr align="center">
                    <th width="50%">key</th>
                    <th>count</th>

                </tr>
                <#list page.getRows() as cdo>
                    <tr class="tr">
                        <td>
                        ${cdo.searchKey}
                        </td>
                        <td>
                        ${cdo.searchCount}
                        </td>
                    </tr>
                </#list>
            </table>
        </div>
    </div>

    <div style="text-align: left;width: 600px"> <@pager  pagination=page  formId="queryAttForm"  /></div>

</form>

<script>
    $(function(){


        $("input[name='searchType']").on("click",function(){
            $("#queryAttForm").submit();
        });

    });


</script>
</@backend>
