<#include "/common/common.ftl"/> 
<#include "/cms/common/page.ftl" />
<@backend title="缓存规则管理" 
    js=['/statics/cms/js/common.js',
    	'/statics/backend/js/layer/layer.min.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/layerly/layer.js',
        
        '/statics/backend/js/dateTime/jquery.datetimepicker.js',
		
		'/statics/cms/js/jquery/jquery.json-2.4.min.js',
		'/statics/cms/js/listRuleRedis.js'
       ] 
    css=['/statics/backend/js/dateTime/jquery.datetimepicker.css',
			 '/statics/backend/css/style.css'] >
<div class="mt10" id="forms">
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">CMS管理->首页管理->缓存规则管理</b> 
            </div>
            <div class="box_center">
                <form id="contract_list_form" class="jqtransform" action="">
                    <table cellspacing="0" cellpadding="0" border="0" width="100%"
                        class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td colspan="1" class="td_right" width="50" align="right">功能名称：</td>
                                <td class="" width="50" align="left"><input type="text"
                                    name="functionName" value="${functionName}" class="input-text lh30 functionName" size="20"></td>

								<td colspan="1" class="td_right" width="50" align="right">地区名称：</td>
                                 <td class="" width="50" align="left"><input type="text"
                                    name="area" value="${area}" class="input-text lh30 area" size="20"></td>
                                    
                         	 </tr>
                         	 
                         	 <tr>
                         	 	<td colspan="1" class="td_right" width="50" align="left">平台类型：</td>
                               	 <td class="" width="50" align="left">
                               	 	<div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> <select class="select platformType" name="" 
                                                style="width: 130px;">
                                                    <option value="">全部</option>
                                                    <option value="PC" <#if "${platformType}"=="PC"> selected</#if>>PC</option>
                                                    <option value="APP" <#if "${platformType}"=="APP"> selected</#if>>APP</option>
                                            </select>
                                            </span>
                                         </div>
                                     </div>
                               	 </td>
                               	 
                               	 <td colspan="1" class="td_right" width="50" align="left">是否启用：</td>
                            	 <td class="" width="50" align="left">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> <select class="select status" name="" 
                                                style="width: 130px;">
                                                    <option value="">全部</option>
                                                    <option value="0" <#if "${status}"=="0"> selected</#if>>启用</option>
                                                    <option value="1" <#if "${status}"=="1"> selected</#if>>禁用</option>
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
                                <td>
                                    <input type="button" name="advertise_open" id="advertise_open" class="btn btn82 btn_add" value="启用">
                                     <input type="button" name="advertise_no_open" id="advertise_no_open" class="btn btn82 btn_add" value="禁用">
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
												<th>功能名称</th>
                                                <th>功能代码</th>
                                                <th>平台类型</th>
                                                <th>地区</th>
                                                <th>地区代码</th>
                                                <th>key(功能代码-平台类型)</th>
                                                <th width="20%">key(功能代码-平台类型-地区代码)</th>
                                                <th>页数</th>
                                                <th>状态</th>
                                                <th>操作</th>
											</tr>
										</thead>
										<tbody>
											<tr class="temp_tr_advertise">
												<td><input class="dev_ck" type="checkbox" name="pop_announce_check" /></td>
												<td class="td_center"><span class="functionName"></span></td>
												<td class="td_center"><span class="functionCode"></span></td>
												<td class="td_center"><span class="platformType"></span></td>
												<td class="td_center"><span class="area"></span></td>
												<td class="td_center"><span class="areaCode"></span></td>
												<td class="td_center"><span class="firstKey"></span></td>
												<td class="td_center"><span class="secondKey"></span></td>
												<td class="td_center"><span class="page"></span></td>
                   								<td class="td_center"><span class="status"></span></td>
                   								<td class="td_center"><a href="javascript:;" class="editAtt" param='${att.id}'>编辑</a></td>
												<td style="display:none;"><span type="hidden" class="pop_Id" /></td>
												</tr>
										</tbody>
                                            
                                    </table>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    
                    
                    <div style="margin-top:10px;width:100%">
					<div style="float:right;padding-right:10px;">
						<a href="javascript:void(0);" id="nextPage">下一页</a>
					</div>
					<div style="float:right;padding-right:10px;">
						<a href="javascript:void(0);" id="prePage">上一页</a>
					</div>
					<div style="float:right;padding-right:10px;">
						总：<span  id="topicCount">${topicCount!}</span>
						每页：
						<#if ("50" == "${perCount!}")>
							<select id="perCount">
								<option value="20">20</option>
								<option value="50" selected>50</option>
								<option value="100">100</option>
								<option value="200">200</option>
							</select>
						<#elseif ("100" == "${perCount!}")>
							<select id="perCount">
								<option value="20">20</option>
								<option value="50">50</option>
								<option value="100" selected>100</option>
								<option value="200">200</option>
							</select>
						<#elseif ("200" == "${perCount!}")>
							<select id="perCount">
								<option value="20">20</option>
								<option value="50">50</option>
								<option value="100">100</option>
								<option value="200" selected>200</option>
							</select>
						<#else>
							<select id="perCount">
								<option value="20" selected>20</option>
								<option value="50">50</option>
								<option value="100">100</option>
								<option value="200">200</option>
							</select>
						</#if>
						<span id="currPage">${currPage!}</span>/<span id="totalPage">${totalPage!}</span>页
					</div>
				</div>
                    
                    <#include "/cms/index/queryAdvertTemp.ftl">
                    <!--@pagination value=page /-->
                </form>
            </div>
        </div>
    </div>
</div>

</@backend>
