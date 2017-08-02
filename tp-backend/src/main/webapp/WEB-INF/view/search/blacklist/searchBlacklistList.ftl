<table cellspacing="0" cellpadding="0" border="0" width="100%" class="list_table">
    <thead>

    <tr align="center">

        <th style="width:150px;">专场ID | SKU</th>
        <th style="width:150px;">类型</th>
        <th>名称</th>
        <th>创建时间</th>
        <th>创建用户</th>

        <th>操作</th>
    </tr>
    </thead>
    <tbody id="quotation_item_add_body">
    <#if (list)?exists>

        <#list list as bl>
        <tr align="center" <#if (bl.value) ==(target)> style="background-color: aquamarine;" </#if>>
            <td><input type="hidden" value="${bl.id}" name="id" id="id" />${(bl.value)!}</td>
            <td><#list types as type>
                <#if type.getCode()==bl.type>${type.getDesc()}</#if>
            </#list></td>
            <td>${bl.name}</td>
            <td>${bl.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
            <td>${bl.createUser}</td>
            <td><input type="button" class="ext_btn" name="deleteBlacklist" onclick="deleteBlacklist(${bl.id})"  value="删除" data="${bl.id}"></td>

        </#list>
    </#if>
    </tbody>
</table>

