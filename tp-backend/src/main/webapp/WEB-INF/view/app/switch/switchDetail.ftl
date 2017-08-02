<#include "/common/common.ftl"/>
<@backend title="新增专场活动"
js=['/statics/backend/js/json2.js',
'/statics/backend/js/jquery.form.2.2.7.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/promotion/utils.js',
'/statics/backend/js/app/switch/switch.js']
css=[]>
<form id="addTopic" method="POST">
    <table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15" id="dcDetail">
        <tbody>
        <tr>

            <td class="td_left" style="width: 20px" align="left"><strong style="color:red;">*</strong>名称</td>
            <td class="td_left" width="80" align="left">
               <input type="text" style="width: 400px" name="name" id="name" class="input-text lh25"  >
            </td>

        </tr>
        <tr>

            <td class="td_left" style="width: 20px" align="left"><strong style="color:red;">*</strong>CODE</td>
            <td class="td_left" width="80" align="left">
                <input type="text" style="width: 400px" name="code" id="code" class="input-text lh25" >
            </td>

        </tr>

        <tr>

            <td class="td_left" style="width: 20px" align="left"><strong style="color:red;">*</strong>状态</td>
            <td class="td_left" width="80" align="left">
                <select id="status" class="select" >
                    <option value="1" >启用</option>
                    <option value="0" >禁用</option>
                </select>
            </td>

        </tr>


        <tr>
            <td align="center">
                <div style="padding-right:10px">
                    <input type="button" class="btn btn82 btn_save2" id="save" value="保存" />
                </div>
            </td>
            <td align="left">
                <div style="padding-left:10px">
                    <input type="button" class="btn btn82 btn_del closebtn" id="cancel" value="取消" />
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</form>
</@backend>