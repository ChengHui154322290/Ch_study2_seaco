<table cellspacing="0" cellpadding="0" border="0" width="100%" class="list_table">
    <thead>

    <tr align="center">

        <th style="width:250px;">专场名称(ID)  </th>
        <th style="width:250px;">商品名称</th>
        <th style="width:150px;">SKU</th>
        <th style="width:150px;">排序</th>
        <th style="width:150px;">状态</th>
         <th style="width:150px;">创建时间</th>
        <th  style="width:150px;">创建用户</th>
        <th  style="width:150px;">操作</th>
    </tr>
    </thead>
    <tbody id="quotation_item_add_body">
    <#if (list)?exists>

        <#list list as bl>
        <tr align="center" <#if (bl.value) ==(target)> style="background-color: aquamarine;" </#if>>
            <#--<td><input type="hidden" value="${bl.id}" name="id" id="id" />${(bl.value)!}</td>-->

            <td>${bl.topicName} (${bl.topicId}) <input type="hidden" value="${bl.id}" name="id" id="id" /></td>
            <td>${bl.itemName}  </td>
            <td>${bl.sku}</td>
            <td>${bl.sort}  </td>
            <td><#if bl.status ==1>有效<#else>失效</#if></td>
            <td>${bl.createTime?string("yyyy-MM-dd hh:mm:ss")}</td>
            <td>${bl.createUser}</td>
            <td><input type="button" class="ext_btn" name="deleteBlacklist" onclick="deleteConfig(${bl.id})"  value="删除" data="${bl.id}"></td>

        </#list>
    </#if>
    </tbody>
</table>

