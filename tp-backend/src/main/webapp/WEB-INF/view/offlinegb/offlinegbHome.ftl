<#include "/common/common.ftl"/>
<@backend title="线下团购-热门商品配置" js=[
'/statics/supplier/js/common.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/offlinegb/offlinegb.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js'
]
css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >
<style type="text/css">


</style>
<div class="box">
    <div class="box_border">
        <div class="box_top">
            <b class="pl15">线下团购-热门商品列表配置</b>
        </div>
        <div class="box_center" style="margin-bottom: 30px;">
            <form class="jqtransform" action="" id="quotation_add_form" method="post">
                <table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
                    <tbody>
                    <tr>

                        <td class="td_right"> <input type="button" id="addConfig" class="btn btn82 btn_add" value="添加"></input></td>
                        <td>

                    </tr>

                    </tbody>
                </table>

            </form>
        </div>

        <div class="box_center" id="hsConfigList"></div>
    </div>
</div>
</div>
</@backend>