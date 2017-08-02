<#include "/common/common.ftl"/> 
<#include "/cms/common/page.ftl" />
<@backend title="公告资讯管理" 
    js=['/statics/cms/js/common.js',
        '/statics/cms/js/listAnnouncement.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-common.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/jquery/jquery.json-2.4.min.js',
        '/statics/cms/js/layerly/layer.js'
       ] 
    css=[] >
<div class="mt10" id="forms">
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">CMS管理->首页管理->公告资讯管理</b> 
            </div>
            <div class="box_center">
                <form id="contract_list_form" class="jqtransform" action="">
                    <table cellspacing="0" cellpadding="0" border="0" width="100%"
                        class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td colspan="1" class="td_right" width="50" align="right">标题：</td>
                                <td class="" width="50" align="left"><input type="text"
                                    name="name" class="input-text lh30 titleName" size="17" value="${titleName}"></td>

								<td colspan="1" class="td_right" width="50" align="right">状态：</td>
                            	<td align="left" colspan="2">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> <select class="select status" name="" 
                                                style="width: 130px;">
                                                    <option value="">全部</option>
                                                    <option value="1" <#if "${status}"=="1"> selected</#if>>草稿</option>
                                                    <option value="0" <#if "${status}"=="0"> selected</#if>>启用</option>
                                            </select>
                                            </span>
                                         </div>
                                     </div>
                               	 </td>
                               	 
                         	 </tr>
                         	 
                         	 <tr>
                         	 	 <td colspan="1" class="td_right" width="50" align="right">页面位置：</td>
	                            	<td align="left" colspan="2">
	                                    <div class="select_border">
	                                        <div class="select_containers">
	                                            <span class="fl"> <select class="select type" name="type" 
	                                                style="width: 130px;">
	                                                    <option value="">全部</option>
	                                                    <option value="1" <#if "${type}"=="1"> selected</#if>>西客商城首页-资讯</option>
	                                                    <option value="5" <#if "${type}"=="5"> selected</#if>>海淘首页-自定义区</option>
	                                                    <option value="6" <#if "${type}"=="6"> selected</#if>>西客商城首页-自定义区</option>
	                                            </select>
	                                            </span>
	                                         </div>
	                                     </div>
	                               	 </td>
                         	 </tr>

                            <tr>
                                <td colspan="8" align="center"><input type="reset"
                                    name="button" class="btn btn82 btn_res" value="重置">
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="button"
                                    name="button" id="contractListQuery" class="btn btn82 btn_search" value="查询">
                                </td>
                            </tr>

                            <!--虚线-->
                            <tr>
                                <td colspan="8">
                                    <hr style="border: 0.1px dashed #247DFF;" />
                                </td>
                            </tr>

                            <tr>
                                <td >
                                    <input type="button" name="button" id="announcement_list_add" class="btn btn82 btn_add" value="创建">
                                </td>
                                <td >
                                    <input type="button" name="button" id="announcement_list_upd" class="btn btn82 btn_add" value="修改">
                                </td>
                                <td >
                                    <input type="button" name="button" id="announcement_list_del" class="btn btn82 btn_del" value="删除">
                                </td>
                                <!--td >
                                    <input type="button" name="button" id="announcement_list_sav" class="btn btn82 btn_add" value="禁用/启用">
                                </td-->
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
                                                <th>标题</th>
                                                <th>状态</th>
                                                <th>顺序</th>
                                                <th>页面位置</th>
                                                <th>链接</th>
                                                <th>内容</th>
											</tr>
										</thead>
										<tbody>
											<tr class="temp_tr_announce">
												<td><input class="dev_ck" type="checkbox" name="pop_announce_check" /></td>
												<td class="td_center"><span class="pop_title"></span></td>
												<td class="td_center"><span class="pop_status"></span></td>
												<td class="td_center"><span class="pop_sort"></span></td>
												<td class="td_center"><span class="pop_type"></span></td>
												<td class="td_center"><span class="pop_link"></span></td>
												<td class="td_center"><span class="pop_contacts"></span></td>
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
							总：<span id="topicCount">${topicCount!}</span>
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
				
				
                    <!--@pagination value=page /-->
                </form>
            </div>
        </div>
    </div>
</div>

</@backend>
