<div class="box_border" id="dummyItemAttrInfo-div" style="display: <#if "${detail.itemType}"=="2"> block <#else >none</#if>">
    <div class="box_top">
        <b class="pl15">服务商品属性</b>
    </div>
    <div class="box_center">
        <div class="tl" style=" border: 1px solid #d3dbde">
        <#--<div style="float:left; " >-->
        <#--<b class="pl15">属性组 ：</b>-->
        <#--</div>-->
            <table id="dummyAttrList-required" class="input tabContent">
                <tr>
                    <td class="td_left" width="60px">
                        <label type="text" class=" lh30"> <span class="requiredField">*</span>有效期</label>
                    </td>
                    <td class="td_left" colspan="2" width="260px">
                        <input type="text" name="effectTimeStart" class="input-text lh30 "
                               value='<#if effectTimeStart??>${effectTimeStart}</#if>'
                               onClick="WdatePicker({isShowClear:true,readOnly:true,dateFmt: 'yyyy-MM-dd' })">
                        到
                        <input type="text" name="effectTimeEnd" class="input-text lh30 "
                               value='<#if effectTimeEnd??>${effectTimeEnd }</#if>'
                               onClick="WdatePicker({isShowClear:true,readOnly:true,dateFmt: 'yyyy-MM-dd' })">

                    </td>
                </tr>
                <tr>
                    <td class="td_left" width="60px">
                        <label><input type="checkbox" name="includeFestival" <#if includeFestival ?? && includeFestival==0><#else >checked</#if> />节假日通用</label>
                    </td>
                </tr>

            </table>


        </div>
        <div style="clear:both;"></div>
    </div>

    <div>
        <input type="button" id="addDummyAttributeBtn" value="添加属性" class="ext_btn ext_btn_submit m10">
    </div>
    <table id="dummyAttrList-custom" class="input tabContent">
        <tr>
            <th width="60">自定义属性名</th>
            <th width="200">自定义属性值</th>
            <th width="60">操作</th>
        </tr>
    <#list dummyAttrList  as l>
        <tr class="dummyAttrList">
            <td class="td_left">
                <input type="text" class="input-text lh30" name="dummyAttrKey" value="${l.attrKey}" maxlength="20"/>
            </td>
            <td class="td_left">
                <input type="text" class="input-text lh30" name="dummyAttrValue" value="${l.attrValue}" maxlength="200"/>
            </td>
            <td class="td_left">
                <input type="button" id="" value="-" class="ext_btn ext_btn_submit m10 deleteAttrBtn">
            </td>
        </tr>
    </#list>
    </table>

</div>
