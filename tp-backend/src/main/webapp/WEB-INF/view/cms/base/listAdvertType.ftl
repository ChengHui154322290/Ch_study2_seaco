<#include "/common/common.ftl"/> 
<#include "/cms/common/page.ftl" />
<@backend title="图片类型管理" 
    js=['/statics/cms/js/common.js',
    	'/statics/backend/js/layer/layer.min.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/layerly/layer.js',
		
		'/statics/cms/js/jquery/jquery.json-2.4.min.js',
		'/statics/cms/js/base/listAdvertType.js'
       ] 
    css=['/statics/backend/css/style.css'] >
<div class="mt10" id="forms">
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">CMS管理->首页管理->图片类型管理</b> 
            </div>
            <div class="box_center">
                <form id="contract_list_form" class="jqtransform" action="">
                    <table cellspacing="0" cellpadding="0" border="0" width="100%"
                        class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td colspan="1" class="td_right" width="50" align="right">类型名称：</td>
                                <td class="" width="50" align="left"><input type="text"
                                    name="name" class="input-text lh30 name" size="20"></td>

								<td colspan="1" class="td_right" width="50" align="right">接口标识：</td>
                                 <td class="" width="50" align="left"><input type="text"
                                    name="ident" class="input-text lh30 ident" size="20"></td>
                                    
								<td colspan="1" class="td_right" width="50" align="left">是否启用：</td>
                            	 <td align="left">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> <select class="select status" name="" 
                                                style="width: 130px;">
                                                    <option value="">全部</option>
                                                    <option value="0">启用</option>
                                                    <option value="1">禁用</option>
                                            </select>
                                            </span>
                                         </div>
                                     </div>
                               	  </td>
                               	 
                         	 </tr>
                         	 
                            <tr>
                                <td colspan="8" align="center">
                                	<input type="reset" name="button" class="btn btn82 btn_res" value="重置">
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                                    <input type="button" name="button" id="advertiseListQuery" class="btn btn82 btn_search" value="查询">
                                </td>
                            </tr>

                            <!--虚线-->
                            <tr>
                                <td colspan="8">
                                    <hr style="border: 0.1px dashed #247DFF;" />
                                </td>
                            </tr>

                            <tr>
                                <td>
                                    <input type="button" name="advertise_list_add" id="advertise_list_add" class="btn btn82 btn_add" value="创建">
                                </td>
                            </tr>

                            <!--虚线-->
                            <tr>
                                <td colspan="8">
                                    <hr style="border: 0.1px dashed #247DFF;" />
                                </td>
                            </tr>

                            <tr>
                                <td colspan="8">
                                    <table width="100%" cellspacing="0" cellpadding="0" border="0"
                                        class="list_table CRZ customertable" id="CRZ0">
                                        
                                        <thead>
											<tr>
												<th width="2%"><input type="checkbox" id="cust_check"></th>
												<th>类型名称</th>
                                                <th>接口标识</th>
                                                <th>是否启用</th>
                                                <th>操作</th>
											</tr>
										</thead>
										<tbody>
											<tr class="temp_tr_advertise">
												<td><input class="dev_ck" type="checkbox" name="pop_check" /></td>
												<td class="td_center"><span class="pop_name"></span></td>
												<td class="td_center"><span class="pop_ident"></span></td>
												<td class="td_center"><span class="pop_status"></span></td>
                   								<td class="td_center"><a href="javascript:;" class="editAtt" param='${att.id}'>编辑</a></td>
												<td style="display:none;"><span type="hidden" class="pop_Id" /></td>
												</tr>
										</tbody>
										
                                    </table>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    
                </form>
            </div>
        </div>
    </div>
</div>

</@backend>
