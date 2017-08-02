<#include "/common/common.ftl"/>
<@backend title="新增专场活动"
js=['/statics/backend/js/json2.js',
'/statics/backend/js/jquery.form.2.2.7.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/promotion/utils.js',
'/statics/backend/js/app/skin/skin.js',
'/statics/backend/js/spectrum/spectrum.js',
'/statics/supplier/component/date/WdatePicker.js']
css=[
'/statics/supplier/component/date/skin/WdatePicker.css',
'/statics/backend/js/spectrum/spectrum.css'
]>

<style>
    .image-style {
        margin-left: 10px;
        width: 80px;
        height: 60px;
    }

    .image-style-A {
        margin-left: 10px;
        width: 420px;
        height: 60px;
    }

    .td-style {
        width: 42px;
    }

    .del-style {
        float: right;
        margin-right: 5px;
        margin-top: 65px;
    }


</style>
<form id="addTopic" method="POST">
    <table cellspacing="0" cellpadding="0" border="0" width="80%" class="form_table pt15 pb15" id="dcDetail">
        <input type="hidden" id="id" name="id" value="${skinInfo.id}"/>
        <tbody>
        <tr>

            <td class="td_left" style="width: 60px" align="left"><strong style="color:red;">*</strong>名称</td>
            <td class="td_left" colspan="3" width="80" align="left">
                <input type="text" style="width: 400px" name="name" id="name" class="input-text lh25" value="${skinInfo.name}">
            </td>

        </tr>

        <tr>

            <td class="td_left" style="width: 60px" align="left"><strong style="color:red;">*</strong>有效期</td>
            <td class="td_left" width="80" align="left" colspan="3">
                <input type="text" name="startTime" id="startTime" class="input-text lh30 Wdate"
                       value='<#if skinInfo.startTime??>${skinInfo.startTime?string("yyyy-MM-dd HH:mm:ss")}</#if>'
                       onClick="WdatePicker({isShowClear:true,readOnly:true ,dateFmt:'yyyy-MM-dd HH:mm:ss'})"> --
                <input type="text" name="endTime" id="endTime" class="input-text lh30 Wdate"
                       value='<#if skinInfo.endTime??>${skinInfo.endTime?string("yyyy-MM-dd HH:mm:ss")}</#if>'
                       onClick="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})">
            </td>

        </tr>

        <tr>

            <td class="td_left" style="width: 20px" align="left"><strong style="color:red;">*</strong>状态</td>
            <td class="td_left" width="80" align="left">
                <select id="status" class="select">
                    <option value="1" <#if skinInfo.status==1>selected</#if>>启用</option>
                    <option value="0" <#if skinInfo.status==0>selected</#if>>禁用</option>
                </select>
            </td>

        </tr>



        <tr>
            <td colspan="4">
                <div class="box_center">
                    <table cellspacing="0" cellpadding="0" border="0" class="list_table CRZ" id="topicImages"
                           style="width:469px;">
                        <tbody>
                        <tr align="center">
                            <th style="width: 42px">ICON_A</th>
                            <th style="width: 42px">ICON_B</th>
                            <th style="width: 42px">ICON_C</th>
                            <th style="width: 42px">ICON_D</th>
                        </tr>
                        <tr>
                            <td valign="top" style="height:60px;">
                                <input type="hidden" name="iconA" id="iconA" value="${skinInfo.iconA}"/>
                                <img id="icon_a" class="image-style" src="${icon_a}"/>

                                <div class="del-style">
                                    <a id="icon_a_del" href="javascript:void(0);">X</a>
                                </div>
                                <div style="width:100%;text-align:center;height:20px;margin-top:5px;">
                                    <span id="icon_a_span"> 72*72px</span>
                                </div>
                            </td>
                            <td valign="top" style="height:60px;">
                                <input type="hidden" name="iconB" id="iconB" value="${skinInfo.iconB}"/>
                                <img id="icon_b" class="image-style" src="${icon_b}"/>

                                <div class="del-style">
                                    <a id="icon_b_del" href="javascript:void(0);">X</a>
                                </div>

                                <div style="width:100%;text-align:center;height:20px;margin-top:5px;">
                                    <span id="icon_b_span">  72*72px</span>
                                </div>
                            </td>
                            <td valign="top" style="height:60px;">
                                <input type="hidden" name="iconC" id="iconC" value="${skinInfo.iconC}"/>
                                <img id="icon_c" class="image-style" src="${icon_c}"/>

                                <div class="del-style">
                                    <a id="icon_c_del" href="javascript:void(0);">X</a>
                                </div>

                                <div style="width:100%;text-align:center;height:20px;margin-top:5px;">
                                    <span id="icon_c_span"> 72*72px</span>
                                </div>
                            </td>
                            <td valign="top" style="height:60px;">
                                <input type="hidden" name="iconD" id="iconD" value="${skinInfo.iconD}"/>
                                <img id="icon_d" class="image-style" src="${icon_d}"/>

                                <div class="del-style">
                                    <a id="icon_d_del" href="javascript:void(0);">X</a>
                                </div>
                                <div style="width:100%;text-align:center;height:20px;margin-top:5px;">
                                    <span id="icon_d_span">72*72px</span>
                                </div>
                            </td>

                        </tr>
                        </tbody>
                </div>
            </td>
        </tr>
        <tr class="imageRow">
            <td colspan="4">
                <div class="box_center">
                    <table cellspacing="0" cellpadding="0" border="0" class="list_table CRZ imageTable"
                           id="topicImageList" style="width:469px">
                        <tbody>
                        <tr align="center">
                            <th style="width: 42px">ICON_A_SEL</th>
                            <th style="width: 42px">ICON_B_SEL</th>
                            <th style="width: 42px">ICON_C_SEL</th>
                            <th style="width: 42px">ICON_D_SEL</th>
                        </tr>
                        <tr>
                            <td valign="top" class="pcTag" style="height:60px;">
                                <input type="hidden" name="iconASelected" id="iconASelected" value="${skinInfo.iconASelected}"/>
                                <img id="icon_a_sel" class="image-style" src="${icon_a_sel}"/>

                                <div class="del-style">
                                    <a id="icon_a_sel_del" href="javascript:void(0);">X</a>
                                </div>
                                <div style="width:100%;text-align:center;height:20px;margin-top:5px;">
                                    <span id="icon_a_sel_span">72*72px</span>
                                </div>
                            </td>
                            <td valign="top" class="mobileTag" style="height:60px;">
                                <input type="hidden" name="iconBSelected" id="iconBSelected" value="${skinInfo.iconBSelected}"/>
                                <img id="icon_b_sel" class="image-style" src="${icon_b_sel}"/>

                                <div class="del-style">
                                    <a id="icon_b_sel_del" href="javascript:void(0);">X</a>
                                </div>
                                <div style="width:100%;text-align:center;height:20px;margin-top:5px;">
                                    <span id="icon_b_sel_span">72*72px</span>
                                </div>
                            </td>
                            <td valign="top" class="interestTag" style="height:60px;">
                                <input type="hidden" name="iconCSelected" id="iconCSelected" value="${skinInfo.iconCSelected}"/>
                                <img id="icon_c_sel" class="image-style" src="${icon_c_sel}"/>

                                <div class="del-style">
                                    <a id="icon_c_sel_del" href="javascript:void(0);">X</a>
                                </div>
                                <div style="width:100%;text-align:center;height:20px;margin-top:5px;">
                                    <span id="icon_c_sel_span">72*72px</span>
                                </div>
                            </td>
                            <td valign="top" class="htTag" style="height:60px;">
                                <input type="hidden" name="iconDSelected" id="iconDSelected" value="${skinInfo.iconDSelected}"/>
                                <img id="icon_d_sel" class="image-style" src="${icon_d_sel}"/>

                                <div class="del-style">
                                    <a id="icon_d_sel_del" href="javascript:void(0);">X</a>
                                </div>
                                <div style="width:100%;text-align:center;height:20px;margin-top:5px;">
                                    <span id="icon_d_sel_span">72*72px</span>
                                </div>
                            </td>

                        </tbody>
                    </table>
                </div>
            </td>
        </tr>

        <tr class="imageRow">
            <td colspan="4">
                <div class="box_center">
                    <table cellspacing="0" cellpadding="0" border="0" class="list_table CRZ imageTable"
                           id="topicImageList" style="width:454px">
                        <tbody>
                        <tr align="center">
                            <th colspan="4" style="width: 160px">TAP_BAR</th>

                        </tr>
                        <tr>
                            <td valign="top" class="pcTag" colspan="4" style="height:60px;">
                                <input type="hidden" name="tapBar" id="tapBar" value="${skinInfo.tapBar}"/>
                                <img id="tap_bar" class="image-style-A" src="${tap_bar}"/>

                                <div class="del-style">
                                    <a id="tap_bar_del" href="javascript:void(0);">X</a>
                                </div>
                                <div style="width:100%;text-align:center;height:20px;margin-top:5px;">
                                    <span id="tap_bar_span">1125*147px</span>
                                </div>
                            </td>


                        </tbody>
                    </table>
                </div>
            </td>
        </tr>

        <tr>
            <td class="td_left" style="width: 40px;text-align:center;"><strong style="color:red;">*</strong>未选中颜色</td>
            <td class="td_left" width="80" align="left" style="text-align:center;">
                <input id="un-selected-color" data-color="${skinInfo.unSelectedColor}"></input>
            </td>
            <td class="td_left" style="width: 20px;text-align:center;" style=""><strong style="color:red;">*</strong>选中颜色</td>
            <td class="td_left" width="80" style="text-align:center;">
                <input id="selected-color" data-color="${skinInfo.selectedColor}"></input>
            </td>

        </tr>




        <tr>
            <td align="center">
                <div style="text-align:center;">
                    <input type="button" class="btn btn82 btn_save2" id="save" value="保存"/>
                </div>
            </td>
            <td align="left">
                <div style="text-align:center;">
                    <input type="button" class="btn btn82 btn_del closebtn" id="cancel" value="取消"/>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</form>
</@backend>