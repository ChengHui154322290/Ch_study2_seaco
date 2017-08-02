<#include "/common/common.ftl"/>
<@backend title="" js=[
'/statics/backend/js/formValidator/formValidator-4.0.1.min.js',
'/statics/backend/js/formValidator/formValidatorRegex.js',
'/statics/backend/js/formValidator/DateTimeMask.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/offlinegb/offlinegb_add_config.js'
]
css=[
'/statics/backend/js/formValidator/style/validator.css'
] >
<div>
    <div>
        <form action="${domain}/basedata/navigation/add" class="jqtransform" method="post" id='navigation_add'>
            <input type="hidden" id="bucketname" name="bucketname" value="${bucketname}"/>
            <input type="hidden" id="bucketURL" name="bucketURL" value="${bucketURL}"/>
            <input type="hidden" id="level" value="#{level}"/>
            <input type="hidden" id="parentId" value="#{parentId}"/>
            <input type="hidden" id="id" name="id" value="${category.id}"/>
            <input type="hidden" id="type" name="type" value="${type}"/>
            <table class="form_table " border="0" cellpadding="0" cellspacing="0" style="padding-left: 39px;padding-top: 40px;" >

                <tr>
                    <td class="td_center" width="150px">专场Id:</td>
                    <td style="width: 330px"><input  type="text" name="topicId" id='topicId' class="input-text lh25" value="<#if config?? >${config.topicId}</#if>" size="20">(线下团购专场Id)</td>

                </tr>
                <tr>
                    <td class="td_center">SKU:</td>
                    <td>
                        <input  type="text" name="sku" id='sku' class="input-text lh25" value="<#if config?? >${config.sku}</#if>" size="20">(线下团购商品SKU)
                    </td>
                </tr>
                <tr>
                    <td class="td_center">排序:</td>
                    <td><input type="text" name="sort" id="sort" value="#{config.sort}" class="input-text lh25" size="20">(取1-6可精确控制商品位置)</td>

                </tr>


    <tr>
         <td colspan="2" class="td_center">
            <input class="btn btn82 btn_save2" type="button" id='datasubmit' value="保存"/>
         </td>
    </tr>
    </table>
    </form>
</div>
</div>
</@backend>