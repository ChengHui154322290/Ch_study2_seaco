<div>
<table cellspacing="0" cellpadding="0" border="0" width="600px" class="list_table">
    <#--<thead>-->

    <#--<tr align="center">-->

        <#--<th style="width:150px;">SKU</th>-->
        <#--<th>名称</th>-->
        <#--<th>创建时间</th>-->
        <#--<th>创建用户</th>-->
        <#--<th>二维码</th>-->
    <#--</tr>-->
    <#--</thead>-->
    <tbody id="quotation_item_add_body">
    <#if (items)?exists>

        <#list items as i>
        <tr  >
            <td colspan="3" style="color: #6638b8 ;width:500px;word-break:break-all"><div style="color: #6638b8 ;width:500px;word-break:break-all;text-align: left">名称:  ${i.name}</div></td>
            <td  rowspan="2" style="width: 200px;"><img src="${bd}/qr/${s}/${v}.htm?url=${md}/item.htm?tid=${i.topicId}%26sku=${i.sku}%26sc=ol"/></td>
        </tr>
        <tr>
            <td><input type="hidden" value="${i.id}" name="id" id="id" />SKU: ${(i.sku)!}</td>
            <td>创建时间: ${i.createTime?string("yyyy-MM-dd hh:mm:ss")}</td>
            <td>创建人: ${i.createUser}</td>
        </tr>
        </#list>

    </#if>
    </tbody>
</table>
</div>
