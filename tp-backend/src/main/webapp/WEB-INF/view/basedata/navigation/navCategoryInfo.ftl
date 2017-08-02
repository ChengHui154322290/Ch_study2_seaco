<#include "/common/common.ftl"/>
<@backend title="" js=[
'/statics/backend/js/formValidator/formValidator-4.0.1.min.js',
'/statics/backend/js/formValidator/formValidatorRegex.js',
'/statics/backend/js/formValidator/DateTimeMask.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/qiniu/js/plupload/plupload.full.min.js',
'/statics/qiniu/js/plupload/plupload.dev.js',
'/statics/qiniu/js/plupload/moxie.js',
'/statics/qiniu/js/plupload/moxie.js',
'/statics/qiniu/src/qiniu.js',
'/statics/qiniu/js/highlight/highlight.js',
'/statics/qiniu/js/ui.js',
'/statics/qiniu/xgUplod.js',
'/statics/backend/js/basedata/bse_navigation_upload.js',
'/statics/backend/js/basedata/bse_navigation_info.js'
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
            <table class="form_table" border="0" cellpadding="0" cellspacing="0">

                <tr>
                    <td class="td_right" width="150px">名称:</td>
                    <td style="width: 240px"><input  type="text" name="name" id='name' class="input-text lh25" value="<#if category?? >${category.name}</#if>" size="20"></td>

                </tr>
                <tr>
                    <td class="td_right">状态:</td>
                    <td>
                        <input type="radio" name="status" <#if category??> <#if category.status==1>checked="checked" </#if> <#else > checked="checked" </#if>  value="1"> 有效
                        <input type="radio" name="status" <#if category?? && category.status==0> checked="checked" </#if>  value="0"> 无效
                    </td>
                </tr>
                <tr>
                    <td class="td_right">高亮:</td>
                    <td>
                        <input type="radio" name="isHighlight" value="1"   <#if category?? && category.isHighlight==1> checked="checked" </#if> > 是
                        <input type="radio" name="isHighlight" value="0"    <#if category??> <#if category.isHighlight==0>checked="checked" </#if> <#else > checked="checked" </#if>  > 否
                    </td>
                </tr>
                <tr>
                    <td class="td_right">排序:</td>
                    <td><input type="text" name="sort" id="sort" value="#{sort}" class="input-text lh25" size="20"></td>

                </tr>
                <tr >
                    <td class="td_right">
                        图片:
                    </td>
                    <td>
                        <div id="container"">
                        <a class="btn btn-default btn-lg " id="pickfiles" href="#" imagenameattribute="logo">
                            <i class="glyphicon glyphicon-plus"></i>
                            <button>选择文件</button>
                        </a>
                        <#if type=1 && level = 1>(尺寸:750*328px) <#elseif  type==1 && level==2>(尺寸:120*120px) <#elseif type==2 >(尺寸:750*88px)<#else></#if>

                        </div>
                        <input type="hidden" id="pic" name="pic" value="${category.pic}">
                         <img src="${pic}" id="img" style=" <#if pic??>display: block;<#else > display: none;</#if>  height:160px;">
                     </td>
                </tr>

    <tr>
        <td class="td_left" colspan="1">
            <input class="btn btn82 btn_save2" type="button" id='datasubmit' value="保存"/>
        </td>
        <td class="td_left" colspan="1">
            <input class="btn btn82 btn_nochecked closebtn" type="button" value="取消" id="buttoncancel"/ >
        </td>
    </tr>
    </table>
    </form>
</div>
</div>
</@backend>