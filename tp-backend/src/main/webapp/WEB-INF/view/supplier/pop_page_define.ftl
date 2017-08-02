<div class="container" id="" style="displayNone">
    <div class="box_top">
        <b class="pl15">添加</b>  
    </div>
    <div class="box_center">
        <form method="post" action="#" class="jqtransform" id="supplier_list_form">
            <table width="100%" cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15">
                <tbody>
                    <tr>
                        <td width="50" align="right" class="td_right">供应商编号：</td>
                        <td width="50" align="left" class=""><input type="text" size="17" class="input-text lh30" name="supplierId"></td>
                        <td width="50" align="left" class="td_right">名称：</td>
                        <td width="50" align="left" class=""><input type="text" size="17" class="input-text lh30" name="supplierName"></td>
                    </tr>
                    <tr>
                        <td align="left" colspan="4">
                            <input type="button" value="查询" class="btn btn82 btn_search" id="supplierListQuery" name="button">
                        </td>
                    </tr>
                </tbody>
            </table>
            <input type="hidden" name="index" value="1" id="pageIndexId">
        </form>
        <table width="100%" cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15">
            <tbody>
                <#--虚线-->
                <tr>
                    <td colspan="8">
                        <hr style="border: 1px dashed #247DFF;">
                    </td>
                </tr>
                <tr>
                    <td align="left" colspan="7">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="button" value="新增" class="btn btn82 btn_add" id="supplier_list_add" name="button">
                    </td>
                    <td align="center"><input type="button" class="ext_btn ext_btn_submit" value="？"></td>
                </tr>
                <#--虚线-->
                <tr>
                    <td colspan="8">
                        <hr style="border: 1px dashed #247DFF;">
                    </td>
                </tr>
                <tr>
                    <td colspan="8">
                        <table width="100%" cellspacing="0" cellpadding="0" border="0" id="CRZ0" class="list_table CRZ">
                            <tbody>
                                <tr align="center">
                                    <th>供应商编号</th>
                                    <th>供应商名称</th>
                                    <th>类型</th>
                                    <th>备注</th>
                                    <th>状态</th>
                                    <th>操作</th>
                                </tr>
                                <tr align="center" style="background-color: rgb(255, 255, 255);" class="tr">
                                    <td>51</td>
                                    <td>供应商名称：</td>
                                    <td>全部</td>
                                    <td></td>
                                    <td>1</td>
                                    <td>
                                        <a href="/supplier/supplierEdit.htm?spId=51">编辑</a>
                                        &nbsp;&nbsp;
                                        <a href="">终止</a>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</div>