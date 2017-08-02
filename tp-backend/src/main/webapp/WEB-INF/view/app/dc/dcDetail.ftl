<#include "/common/common.ftl"/>
<@backend title="新增专场活动"
js=['/statics/backend/js/json2.js',
'/statics/backend/js/jquery.form.2.2.7.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/promotion/utils.js',
'/statics/backend/js/app/dc.js']
css=[]>
<form id="addTopic" method="POST">
    <table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15" id="dcDetail">
        <tbody>
        <tr>
			<input type="hidden" id="id" value="${detail.id}">
            <td class="td_left" style="width: 20px" align="left"><strong style="color:red;">*</strong>名称</td>
            <td class="td_left" width="80" align="left">
               <input type="text" style="width: 400px" name="name" id="name" class="input-text lh25" value="${detail.name}">
            </td>

        </tr>
        <tr>

            <td class="td_left" style="width: 20px" align="left"><strong style="color:red;">*</strong>版本区间</td>
            <td class="td_left" width="80" align="left">
                <input type="text" style="width: 190px" name="versionFrom" maxlength="8" placeholder="起始版本(包含)" id="versionFrom" class="input-text lh25" value="${detail.versionFrom}">--
                <input type="text" style="width: 190px" name="versionTo" maxlength="8" placeholder="结束版本(包含)" id="versionTo" class="input-text lh25" value="${detail.versionTo}">
            </td>

        </tr>
        <tr>

            <td class="td_left" style="width: 20px" align="left"></td>
            <td class="td_left" width="80" align="left">
               1.5.0配置为1500,1.5.1.1配置为1511,两个点需要加0,三个点不用加0
            </td>

        </tr>
        <tr>
            <td class="td_left" style="width: 20px" align="left"><strong style="color:red;">*</strong>内容</td>
            <td align="left"  ><textarea style="width: 400px;height: 300px"  id = "content">${detail.content!}</textarea></td>
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