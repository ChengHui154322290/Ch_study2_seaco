<#include "/common/common.ftl"/>
<@backend title="商品二维码" js=[
'/statics/supplier/js/common.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/qr/qr_home.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/select2/js/select2.js',
'/statics/select2/js/select2Util.js',
'/statics/select2/js/select2_locale_zh-CN.js'
]
css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
'/statics/select2/css/select2.css',
'/statics/select2/css/common.css'] >

<div class="box">
    <div class="box_border">
        <div class="box_top">
            <b class="pl15">二维码</b><label> </label>
        </div>
        <div class="box_center" style="margin-bottom: 30px;">
            <form class="jqtransform" action="" id="quotation_add_form" method="post">
                <table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
                    <tbody>
                    <tr>
                        <td class="td_right"> 专场信息</td>
                        <td>
                            <select id="ti" class="select2" style="width: 400px">
                                <option value="">请选择专场信息</option>
                            <#list topics as  t >
                                <option value= ${t.id}>${t.name}</option>
                            </#list>
                            </select>

                            <input type="button" class="ext_btn  " id="genQR" value="生成二维码">
                            <input type="button" class="ext_btn  " id="toPDF" value="导出PDF">
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <input type="button" class="ext_btn  " id="uAdv" value="高级">
                                <label id="adv" style="display:none">
                                版本<input type="text" class="input-text lh30" placeholder="默认置空" style="width:100px;" id="version" >
                                尺寸<input type="text" class="input-text lh30" placeholder="默认置空" style="width:100px;" id="size"  >
                            </label>
                        </td>

                    </tr>


                    </tbody>
                </table>

            </form>
        </div>

        <div class="box_center" id="qrList"></div>
    </div>
</div>
</div>
</@backend>