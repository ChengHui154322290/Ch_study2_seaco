<#include "/common/common.ftl"/> 
<#include "/cms/common/page.ftl" />
<@backend title="单品团模板管理" 
    js=['/statics/cms/js/common.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-common.js',
        '/statics/cms/js/jquery/jquery.json-2.4.min.js',
        '/statics/cms/js/layerly/layer.js',
		'/statics/cms/js/listSingletemple.js',
		'/statics/cms/js/common/hi-util.js'
       ] 
    css=[] >
<div class="mt10" id="forms">
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">CMS管理->首页管理->单品团模板管理</b> 
            </div>
            <div class="box_center">
                <form id="contract_list_form" class="jqtransform" action="">
                    <table cellspacing="0" cellpadding="0" border="0" width="100%"
                        class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td colspan="1" class="td_right" width="50" align="right">模板名称：</td>
                                <td class="" width="50" align="left"><input type="text"
                                    name="name" class="input-text lh30 templeName" value="${templeName}" size="17"></td>

								<td colspan="1" class="td_right" width="50" align="right">位置名称：</td>
                                 <td class="" width="50" align="left"><input type="text"
                                    name="position" class="input-text lh30 positionName" value="${positionName}" size="17"></td>
                                    
								<td colspan="1" class="td_right" width="50" align="left">是否启用：</td>
                            	<td align="left">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> <select class="select status" name="" 
                                                style="width: 130px;">
                                                    <option value="0" <#if "${status}"=="0"> selected</#if>>启用</option>
                                                    <option value="1" <#if "${status}"=="1"> selected</#if>>禁用</option>
                                            </select>
                                            </span>
                                         </div>
                                     </div>
                               	 </td>
                               	 
                               	 <td colspan="1" class="td_right" width="50" align="left">平台标识：</td>
                            	 <td align="left">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> <select class="select platformType" name="platformType" 
                                                style="width: 130px;">
                                                	<option value="">全部</option>
                                                    <option value="0" <#if "${platformType}"=="0"> selected</#if>>PC</option>
                                                    <option value="1" <#if "${platformType}"=="1"> selected</#if>>APP</option>
                                            </select>
                                            </span>
                                         </div>
                                     </div>
                               	  </td>
                         	 </tr>
                         	 
                         	 <tr>
                                <td class="td_right" rowspan="1">模板类型：</td>
								<td class=""><span class="fl"> <select
										name="type" class="select type">
											<option value="">全部</option>
                                            <option value="10"  <#if "${type}"=="10"> selected</#if>>单品团(pc)</option>
											<option value="15"  <#if "${type}"=="15"> selected</#if>>海淘首页(pc)</option>
											<option value="16"  <#if "${type}"=="16"> selected</#if>>西客商城首页-热销榜单(pc)</option>
											<option value="17"  <#if "${type}"=="17"> selected</#if>>西客商城首页-品牌精选(pc)</option>
											<option value="102" <#if "${type}"=="102"> selected</#if>>单品团(APP)</option>
											<option value="206" <#if "${type}"=="206"> selected</#if>>秒杀-单品团(APP)</option>
											<option value="207" <#if "${type}"=="207"> selected</#if>>WAP今日精选-单品(APP)</option>
											<option value="208" <#if "${type}"=="208"> selected</#if>>WAP今日精选-专场(APP)</option>
									</select>
								</span></td>
                            </tr>
                         	 
                            <tr>
                                <td colspan="8" align="center">
                                	<input type="reset" name="button" class="btn btn82 btn_res" value="重置">
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                                    <input type="button" name="button" id="singleTempleListQuery" class="btn btn82 btn_search" value="查询">
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
                                    <input type="button" name="button" id="singleTemple_list_add" class="btn btn82 btn_add" value="创建模板">
                                </td>
                                <td>
                                    <input type="button" name="button" id="singleTemple_list_upd" class="btn btn82 btn_add" value="修改模板">
                                </td>
                                <td>
                                    <input type="button" name="button" id="singleTemple_list_del" class="btn btn82 btn_add" value="删除模板">
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
												<th>平台标识</th>
                                                <th>模板名称</th>
                                                <th>模板类型</th>
                                                <th>位置名称</th>
                                                <th>位置大小</th>
                                                <th>位置顺序</th>
                                                <th>埋点编码</th>
                                                <th>操作</th>
                                                <th>模板状态</th>
											</tr>
										</thead>
										<tbody>
											<tr class="temp_tr_singletemp">
												<td><input class="dev_ck" type="checkbox" name="pop_announce_check" /></td>
												<td class="td_center"><span class="pop_platformType"></span></td>
												<td class="td_center"><span class="pop_templeName"></span></td>
												<td class="td_center"><span class="pop_type"></span></td>
												<td class="td_center"><span class="pop_positionName"></span></td>
												<td class="td_center"><span class="pop_positionSize"></span></td>
												<td class="td_center"><span class="pop_positionSort"></span></td>
												<td class="td_center"><span class="pop_buriedCode"></span></td>
												<td class="td_center"><a href="javascript:;" class="addActivity" param='${att.id}'>添加活动</a></td>
												<td class="td_center"><span class="pop_status"></span></td>
												<td style="display:none;"><span type="hidden" class="pop_singleTempleId" /></td>
												<td style="display:none;"><span type="hidden" class="pop_singleTempleNodeId" /></td>
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
