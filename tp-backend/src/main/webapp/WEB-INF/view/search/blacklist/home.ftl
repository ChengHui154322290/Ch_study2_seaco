<#include "/common/common.ftl"/>
<@backend title="搜索黑名单" js=[
'/statics/supplier/js/common.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/sch/search_blacklist.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js'
]
css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css'] >
<style type="text/css">
    .pb15 {
        padding-bottom: 0px;
    }

    .big {
        padding: 0 40px;
        padding-top: 10px;
        height: 50px;
        width: 150px;
        text-transform: uppercase;
        font: bold 20px/22px Arial, sans-serif;
    }

    .big span {
        display: block;
        text-transform: none;
        font: italic normal 12px/18px Georgia, sans-serif;
        text-shadow: 1px 1px 1px rgba(255, 255, 255, .12);
    }

    .green {
        color: #3e5706;

        background: #a5cd4e; /* Old browsers */
        background: -moz-linear-gradient(top, #a5cd4e 0%, #6b8f1a 100%); /* FF3.6+ */
        background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #a5cd4e), color-stop(100%, #6b8f1a)); /* Chrome,Safari4+ */
        background: -webkit-linear-gradient(top, #a5cd4e 0%, #6b8f1a 100%); /* Chrome10+,Safari5.1+ */
        background: -o-linear-gradient(top, #a5cd4e 0%, #6b8f1a 100%); /* Opera 11.10+ */
        background: -ms-linear-gradient(top, #a5cd4e 0%, #6b8f1a 100%); /* IE10+ */
        background: linear-gradient(top, #a5cd4e 0%, #6b8f1a 100%); /* W3C */
    }

    /* Blue Color */

    .blue {
        color: #19667d;

        background: #70c9e3; /* Old browsers */
        background: -moz-linear-gradient(top, #70c9e3 0%, #39a0be 100%); /* FF3.6+ */
        background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #70c9e3), color-stop(100%, #39a0be)); /* Chrome,Safari4+ */
        background: -webkit-linear-gradient(top, #70c9e3 0%, #39a0be 100%); /* Chrome10+,Safari5.1+ */
        background: -o-linear-gradient(top, #70c9e3 0%, #39a0be 100%); /* Opera 11.10+ */
        background: -ms-linear-gradient(top, #70c9e3 0%, #39a0be 100%); /* IE10+ */
        background: linear-gradient(top, #70c9e3 0%, #39a0be 100%); /* W3C */
    }

    /* Gray Color */

    .gray {
        color: #515151;

        background: #d3d3d3; /* Old browsers */
        background: -moz-linear-gradient(top, #d3d3d3 0%, #8a8a8a 100%); /* FF3.6+ */
        background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #d3d3d3), color-stop(100%, #8a8a8a)); /* Chrome,Safari4+ */
        background: -webkit-linear-gradient(top, #d3d3d3 0%, #8a8a8a 100%); /* Chrome10+,Safari5.1+ */
        background: -o-linear-gradient(top, #d3d3d3 0%, #8a8a8a 100%); /* Opera 11.10+ */
        background: -ms-linear-gradient(top, #d3d3d3 0%, #8a8a8a 100%); /* IE10+ */
        background: linear-gradient(top, #d3d3d3 0%, #8a8a8a 100%); /* W3C */
    }

    .mybutton {
        cursor: pointer;
        display: inline-block;
        position: relative;
        margin: 10px;
        padding: 0 20px;
        text-align: center;
        text-decoration: none;
        font: bold 12px/25px Arial, sans-serif;

        text-shadow: 1px 1px 1px rgba(255, 255, 255, .22);

        -webkit-border-radius: 30px;
        -moz-border-radius: 30px;
        border-radius: 30px;

        -webkit-box-shadow: 1px 1px 1px rgba(0, 0, 0, .29), inset 1px 1px 1px rgba(255, 255, 255, .44);
        -moz-box-shadow: 1px 1px 1px rgba(0, 0, 0, .29), inset 1px 1px 1px rgba(255, 255, 255, .44);
        box-shadow: 1px 1px 1px rgba(0, 0, 0, .29), inset 1px 1px 1px rgba(255, 255, 255, .44);

        -webkit-transition: all 0.15s ease;
        -moz-transition: all 0.15s ease;
        -o-transition: all 0.15s ease;
        -ms-transition: all 0.15s ease;
        transition: all 0.15s ease;
    }

</style>
<div class="box">
    <div class="box_border">
        <div class="box_top">
            <b class="pl15">搜索黑名单</b><label> &nbsp;&nbsp;(黑名单设置好后,点击[同步数据]会立即更新并同步搜索数据)</label>
        </div>
        <div class="box_center" style="margin-bottom: 30px;">
            <form class="jqtransform" action="" id="quotation_add_form" method="post">
                <table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
                    <tbody>
                    <tr>

                        <td class="td_right"> 专场Id</td>
                        <td>
                            <input type="text" id="topicId" class="input-text lh30" style="width: 120px"
                                   placeholder="专场Id" size="5">
                            <input type="text" name="topicName" id="topicName" style="width:250px" readonly
                                   class="input-text lh30" size="10">
                            <input type="button" value="查询" id="queryTopic" class="ext_btn">
                            <input type="button" class="ext_btn  ext_btn_submit" id="addTopic" value="添加">

                        </td>

                        <td rowspan="2"><input type="button" class="big mybutton blue" id="synData" value="同步数据"></td>

                    </tr>
                    <tr>
                        <td class="td_right"> SKU
                        </td>
                        <td>

                            <input type="text" id="itemSku" name="itemSku" class="input-text lh30" style="width: 120px"
                                   placeholder="SKU" size="5">
                            <input type="text" name="itemName" id="itemName" style="width:250px" class="input-text lh30"
                                   readonly size="10">
                            <input type="button" value="查询" id="queryItemSku" class="ext_btn ">
                            <input type="button" class="ext_btn ext_btn_submit" id="addItemSku" value="添加">


                        </td>
                    </tr>

                    </tbody>
                </table>

            </form>
        </div>

        <div class="box_center" id="searchBlacklistList"></div>
    </div>
</div>
</div>
</@backend>