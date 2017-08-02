<#include "/common/common.ftl"/>
<@backend title="商家查询"
js=['/statics/backend/js/promotion/promotionSupplier_search.js',
'/statics/backend/js/promotion/promotionShop_search.js',
'/statics/backend/js/json2.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/promotion/utils.js',
'/statics/select2/js/select2.js',
'/statics/select2/js/select2Util.js',
'/statics/select2/js/select2_locale_zh-CN.js']
css=[
'/statics/backend/css/style.css',
'/statics/select2/css/select2.css',
'/statics/select2/css/common.css']>
<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
    </br>
    <tr>
        <span style="margin-left:20px;margin-top:50px">商家：</span>
        <select class="select2" style="width: 480px" name="supplier">
            <option value>--请选择--</option>
            <#list result.data as supp>
                <option value="${supp.id}" suppName="${supp.name}">${supp.id}-${supp.name}</option>
            </#list>
        </select>
    </tr>
    <tr>
        <td colspan="2" align="right">
            <div style="padding-right:10px">
                <input type="button" class="btn btn82 btn_save2" id="save" value="保存"/>
            </div>
        </td>
        <td align="left">
            <div style="padding-left:10px">
                <input type="button" class="btn btn82 btn_del closebtn" id="cancel" value="取消"/>
            </div>
        </td>
    </tr>
</table>
</@backend>