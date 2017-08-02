<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <style>
        body{
            font-size:22px;
            font-style:italic;
            font-weight:bold;
            color: #ff2327;
            font-family:SimSun;

        }
    </style>
</head>
<body>
<div style="width: 600px">
<table cellspacing="0" cellpadding="0" border="0" width="700px" class="list_table">
    <#--<thead>-->

    <#--<tr align="center">-->

        <#--<th style="width:100px;">SKU</th>-->
        <#--<th style="width: 200px"> 名称</th>-->
        <#--<th style="100px">创建时间</th>-->
        <#--<th style="100px">创建用户</th>-->
        <#--<th style="200px">二维码</th>-->
    <#--</tr>-->
    <#--</thead>-->
    <tbody id="quotation_item_add_body" style="border: darkcyan;font-size: small;">
    <#if (items)?exists>

        <#list items as i>
        <tr align="center" >
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
</body>
</html>