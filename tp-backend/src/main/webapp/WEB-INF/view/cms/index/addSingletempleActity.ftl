<#include "/common/common.ftl"/> 
<#include "/cms/common/page.ftl" />
<@backend title="广告管理" 
    js=['/statics/cms/js/common.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/layerly/layer.js',
		'/statics/cms/js/addSingleTempleActivity.js',
		
		'/statics/cms/js/common/time/js/jquery-1.7.1.min.js',
		'/statics/cms/js/common/time/js/jquery-ui-1.8.17.custom.min.js',
		'/statics/cms/js/common/time/js/jquery-ui-timepicker-addon.js',
		'/statics/cms/js/common/time/js/jquery-ui-timepicker-zh-CN.js',
		
		'/statics/cms/js/jquery/jquery.json-2.4.min.js'
       ] 
    css=['/statics/backend/css/style.css',
    	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.css',
    	'/statics/cms/js/common/time/css/jquery-ui-timepicker-addon.css',
    	'/statics/cms/js/common/time/css/jquery-ui-1.8.17.custom.css'] >
	<div class="mt10" id="forms">
	 	
	 	<input type="hidden" id="templeNameBak" 	value="${templeNameBak}" >
		<input type="hidden" id="positionNameBak" value="${positionNameBak}" >
		<input type="hidden" id="statusBak" value="${statusBak}" >
		<input type="hidden" id="platformTypeBak" 	value="${platformTypeBak}" >
		<input type="hidden" id="typeBak" value="${typeBak}" >
	
	    <div class="box">
	        <div class="box_border">
	            <div class="box_top">
	                <b class="pl15">CMS管理->首页管理->单品团模板管理</b> 
	            </div>
	            <div class="box_center">
	                
	                <div class="box_border">
						<div class="box_top">
							<b class="pl15">添加单品团活动</b>
						</div>
						<div>
							<table class="tabContent_lab">
								<tr>
									<td class="td_right" rowspan="1">模板名称：</td>
									<td>
										<input type="text" name="templeName" value="${templeName}" 
											size="20" class="input-text lh30 templeName" disabled="true">
										<input type="hidden" id="tempnodeid" value="${tempnodeid}"/>
									</td>
									<td class="td_right">位置名称：</td>
									<td>
										<input type="text" name="positionName" value="${positionName}"
										class="input-text lh30 positionName" size="20" disabled="true">
									</td>
									<td class="td_right">位置尺寸：</td>
									<td>
										<input type="text" name="positionSize" value ="${positionSize}"
										class="input-text lh30 positionSize" size="20" disabled="true">
									</td>
									<td class="td_right">位置顺序：</td>
									<td>
										<input type="text" name="positionSort" value ="${positionSort}"
										class="input-text lh30 positionSort" size="20" disabled="true">
									</td>
								</tr>
							</table>
						</div>
						<table width="100%" cellspacing="0" cellpadding="0" border="0"
                            class="list_table CRZ customertable">
                            <thead>
								<tr>
                                    <th>专题编号</th>
                                    <th>专题名称</th>
                                    <th>SKU编号</th>
                                    <th>商品名称</th>
                                    <th>商家</th>
                                    <th>规格参数</th>
                                    <th>限购总量</th>
                                    <th>限购数量</th>
                                    <th>活动价</th>
                                    <th>状态</th>
                                    <th>开始时间</th>
                                    <th>结束时间</th>
                                    <th>操作</th>
								</tr>
							</thead>
							<tbody>
							
								<#if detaillist?exists>
									<#list detaillist as detail>
									
										<tr class="temp_tr_singleActivitytemp">
											<td class="td_center"><span class="activityCode">${detail.activityCode}</span></td>
											<td class="td_center"><span class="activityName">${detail.activityName}</span></td>
											<td class="td_center"><span class="skuCode">${detail.skuCode}</span></td>
											<td class="td_center"><span class="goodsName">${detail.goodsName}</span></td>
											<td class="td_center"><span class="seller">${detail.seller}</span></td>
											<td class="td_center"><span class="standardParams">${detail.standardParams}</span></td>
											<td class="td_center"><span class="limitTotal">${detail.limitTotal}</span></td>
											<td class="td_center"><span class="limitNumber">${detail.limitNumber}</span></td>
											<td class="td_center"><span class="sellingPrice">${detail.sellingPrice}</span></td>
											<td class="td_center"><span class="flagStr">${detail.flagStr}</span></td>
											<td class="td_center"><span class="startdate">${detail.startdate?string("yyyy-MM-dd HH:mm:ss")}</span></td>
											<td class="td_center"><span class="enddate">${detail.enddate?string("yyyy-MM-dd HH:mm:ss")}</span></td>
											<td class="td_center"><a href="javascript:;" class="delLocalActivity" param='${detail.id}'>移除</a></td>
											<td style="display:none;"><span type="hidden" class="singleTempleActivityId">${detail.id}</span></td>
											<td style="display:none;"><span type="hidden" class="singleTepnodeId">${detail.singleTepnodeId}</span></td>
										</tr>
										
									</#list>
								</#if>
							
							</tbody>
                        </table>
					</div>
					<br><br>
					
					<div class="box_border">
						<div class="box_top">
							<b class="pl15">选择单品团活动</b>
						</div>
						<table cellspacing="0" cellpadding="0" border="0" width="100%"
	                        class="form_table pt15 pb15">
	                        <tbody>
	                            <tr>
	                                <td colspan="1" class="td_right" width="50" align="right">专题ID：</td>
	                                <td class="" width="50" align="left"><input type="text"
	                                    name="name" class="input-text lh30 activityInputId" size="17"></td>
	
									<td colspan="1" class="td_right" width="50" align="right">专题名称：</td>
	                                 <td class="" width="50" align="left"><input type="text"
	                                    name="position" class="input-text lh30 activityInputName size="17"></td>
	                                    
									<td colspan="1" class="td_right" width="50" align="right">商品编码：</td>
	                                 <td class="" width="50" align="left"><input type="text"
	                                    name="position" class="input-text lh30 activityInputCode" size="20"></td>
	                                    
	                                <td colspan="1" class="td_right" width="50" align="right">起止时间：</td>
	                                <td class="" width="50" align="left"><input type="text" id="activityInputSd"
	                                    name="name" class="input-text lh30 activityInputSd" readonly="readonly" size="20"></td>
									
									<td>到：</td>
	                                 <td><input type="text" id="activityInputed"
	                                    name="position" readonly="readonly" class="input-text lh30 activityInputed size="17"></td>
	                         	 </tr>
	                         	 
	                         	 <tr>
	                                <td colspan="1" class="td_right" width="50" align="left">专场类型：</td>
	                            	<td align="left">
	                                    <div class="select_border">
	                                        <div class="select_containers">
	                                            <span class="fl"> <select class="select type" name="" 
	                                                style="width: 130px;">
	                                                    <option value="0">全部</option>
	                                                    <option value="1">单品团</option>
	                                                    <option value="2">品牌团</option>
	                                                    <option value="3">主题团</option>
	                                                    <option value="4">海淘</option>
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
	                                                    <option value="0">全部</option>
	                                                    <option value="1">PC</option>
	                                                    <option value="2">APP</option>
	                                            </select>
	                                            </span>
	                                         </div>
	                                     </div>
	                               	  </td>
	                               	 
	                               	 <td class="td_right" width="50" align="right">销售类型:</td>
										<td class="" width="50" align="left">
											<div class="select_border">
						                        <div class="select_containers">
						                            <span class="fl"> 
														<select name="salesPartten" class="select salesPartten" id="salesPartten" style="width:150px">
															<option value="" selected>全部</option>
															<option value="1">不限</option>
															<option value="2">旗舰店</option>
															<option value="3">西客商城商城</option>
															<option value="4">洋淘派</option>
															<option value="5">闪购</option>
															<option value="6">秒杀</option>
														</select>
													</span>
						                        </div>
						                    </div>
										</td>
										
	                         	 </tr>
	                         	 
	                            <tr>
	                                <td colspan="8" align="center">
	                                	<input type="reset" name="button" class="btn btn82 btn_res resetButtonInput" value="重置">
	                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
	                                    <input type="button" name="button" id="singleTempleListQuery" class="btn btn82 btn_search" value="查询">
	                                </td>
	                            </tr>
	
	                            <tr>
	                                <td colspan="10">
	                                    <table width="100%" cellspacing="0" cellpadding="0" border="0"
	                                        class="list_table CRZ customertable_temp" id="CRZ0">
	                                        
	                                        <thead>
												<tr>
													<th>专题编号</th>
				                                    <th>专题名称</th>
				                                    <th>SKU编号</th>
				                                    <th>商品名称</th>
				                                    <th>商家</th>
				                                    <th>规格参数</th>
				                                    <th>限购总量</th>
				                                    <th>限购数量</th>
				                                    <th>活动价</th>
				                                    <th>开始时间</th>
				                                    <th>结束时间</th>
				                                    <th>操作</th>
												</tr>
											</thead>
											<tbody>
												<tr class="temp_tr_singletemp">
													<td class="td_center"><span class="activityCode"></span></td>
													<td class="td_center"><span class="activityName"></span></td>
													<td class="td_center"><span class="skuCode"></span></td>
													<td class="td_center"><span class="goodsName"></span></td>
													<td class="td_center"><span class="seller"></span></td>
													<td class="td_center"><span class="standardParams"></span></td>
													<td class="td_center"><span class="limitTotal"></span></td>
													<td class="td_center"><span class="limitNumber"></span></td>
													<td class="td_center"><span class="sellingPrice"></span></td>
													<td class="td_center"><span class="flagStr"></span></td>
													<td class="td_center"><span class="startdate"></span></td>
													<td class="td_center"><span class="enddate"></span></td>
													<td class="td_center"><a href="javascript:;" class="addActivity" param='${att.id}'>添加</a></td>
													<td style="display:none;"><span type="hidden" class="singleTempleActivityId"></span></td>
													<td style="display:none;"><span type="hidden" class="singleTepnodeId"></span></td>
												</tr>
											</tbody>
	                                    </table>
	                                </td>
	                            </tr>
	                        </tbody>
	                    </table>
					</div>
					
					<div class="tc">
							<input type="button" value="返回" id="returnPage" class="ext_btn m10">
					</div>
	                
	           </div>
	        </div>
	    </div>
	</div>

</@backend>
