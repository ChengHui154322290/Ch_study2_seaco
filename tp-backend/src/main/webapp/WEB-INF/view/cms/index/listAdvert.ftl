<#include "/common/common.ftl"/> 
<#include "/cms/common/page.ftl" />
<@backend title="广告管理" 
    js=['/statics/cms/js/common.js',
    	'/statics/backend/js/layer/layer.min.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/layerly/layer.js',
        
        '/statics/backend/js/dateTime/jquery.datetimepicker.js',
		
		'/statics/cms/js/jquery/jquery.json-2.4.min.js',
		'/statics/cms/js/listAdvertise.js'
       ] 
    css=['/statics/backend/js/dateTime/jquery.datetimepicker.css',
			 '/statics/backend/css/style.css'] >
<div class="mt10" id="forms">
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">CMS管理->首页管理->广告管理</b> 
            </div>
            <div class="box_center">
                <form id="contract_list_form" class="jqtransform" action="">
                    <table cellspacing="0" cellpadding="0" border="0" width="100%"
                        class="form_table pt15 pb15">
                        <tbody>
                            <tr>
                                <td colspan="1" class="td_right" width="50" align="right">图片名称：</td>
                                <td class="" width="50" align="left"><input type="text"
                                    name="name" value="${name}" class="input-text lh30 titleName" size="20"></td>

								<td colspan="1" class="td_right" width="50" align="right">图片位置：</td>
                                 <td class="" width="50" align="left"><input type="text"
                                    name="position" value="${position}" class="input-text lh30 position" size="20"></td>
                                    
								<td colspan="1" rowspan="4" class="td_right" width="50" align="left">
									<a href="javascript:void(0);"><img id="imgPrivew" src="" width="266" height="197" /></a>
                                </td>
                               	 
                         	 </tr>
                         	 
                         	 <tr>
                                <td colspan="1" class="td_right" width="50" align="right">开始时间：</td>
                                <td class="" width="50" align="left"><input type="text"
                                    name="startdate" value="${startdate}" class="_dateField input-text lh30 startdate"  readonly="readonly" size="20"></td>

								<td colspan="1" class="td_right" width="50" align="right">结束时间：</td>
                                 <td class="" width="50" align="left"><input type="text"
                                    name="enddate" value="${enddate}" class="_dateField input-text lh30 enddate"  readonly="readonly" size="20"></td>
                                 
                         	 </tr>
                         	 
                         	 <tr>
                         	 	<td colspan="1" class="td_right" width="50" align="left">图片类型：</td>
                            	<!--td align="left">
                                    <div class="select_border">
                                        <div class="select_containers">
                                            <span class="fl"> <select class="select type" name="" 
                                                style="width: 150px;">
                                                    <option value="">全部</option>
                                                    <option value="3">首页-轮播图</option>
                                                    <option value="5">首页-最优惠图片</option>
                                                    <option value="6">最后疯抢-最优惠图片</option>
                                                    <option value="7">首页-弹出层</option>
                                                    <option value="8">用户登录-图片</option>
                                                    
                                                    <option value="101">(APP)首页-广告位</option>
													<option value="102">(APP)秒杀-广告位信息</option>
													<option value="103">(APP)首页-功能标签信息</option>
													<option value="104">(APP)海淘-广告位信息</option>
													<option value="105">(APP)广告-启动页面</option>
													<option value="106">(APP)广告-支付成功</option>
													<option value="107">(APP)wap-首页弹框</option>
													<option value="108">(APP)Wap-今日精选-首图</option>
                                            </select>
                                            </span>
                                         </div>
                                     </div>
                               	 </td-->
                               	 <td class="" width="50" align="left">
                               	 	<input type="text"  name="type" value="${type}" id="type_query" class="input-text lh30 type" size="20">
                               	 	<input type="hidden" name="ident" value="${ident}" id="ident_query" class="input-text lh30 ident" size="20">
                               	 	<input id="advert_type_query" class="ext_btn" type="button" value="查询">
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
                                                    <option value="2" <#if "${status}"=="2"> selected</#if>>过期</option>
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
                            
                             <tr>
                             	 <td colspan="4" align="left">
                                 </td>
                                <!-- 图片尺寸说明 -->    
                                 <td colspan="4" rowspan="1" align="center">
									<a id="picSizeRemark" href="javascript:void(0);">图片尺寸说明</a>
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
												<th>平台标识</th>
                                                <th>图片名称</th>
                                                <th>图片类型</th>
                                                <!--th>图片位置</th-->
                                                <th>图片排序</th>
                                                <th>图片链接</th>
                                                <th>开始时间</th>
                                                <th>结束时间</th>
                                                <th>是否启用</th>
                                                <th>活动ID</th>
                                                <!--th>商品ID</th-->
                                                <th>sku</th>
                                                <th>活动类型</th>
                                                <th>启动时间</th>
                                                <th>操作</th>
											</tr>
										</thead>
										<tbody>
											<tr class="temp_tr_advertise">
												<td><input class="dev_ck" type="checkbox" name="pop_announce_check" /></td>
												<td class="td_center"><span class="pop_platformType"></span></td>
												<td class="td_center"><span class="pop_advertname"></span></td>
												<td class="td_center"><span class="pop_type"></span></td>
												<!--td class="td_center"><span class="pop_position"></span></td-->
												<td class="td_center"><span class="pop_sort"></span></td>
												<td class="td_center"><span class="pop_link"></span></td>
												<td class="td_center"><span class="pop_startdate"></span></td>
												<td class="td_center"><span class="pop_enddate"></span></td>
												<td class="td_center"><span class="pop_status"></span></td>
                   								<td class="td_center"><span class="pop_activityid"></span></td>
                   								<!--td class="td_center"><span class="pop_productid"></span></td-->
                   								<td class="td_center"><span class="pop_sku"></span></td>
                   								<td class="td_center"><span class="pop_actType"></span></td>
                   								<td class="td_center"><span class="pop_time"></span></td>
                   								<td class="td_center"><a href="javascript:;" class="editAtt" param='${att.id}'>编辑</a></td>
												<td style="display:none;"><span type="hidden" class="pop_Id" /></td>
												<td style="display:none;"><span type="hidden" class="pop_path" /></td>
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
