<#include "/common/common.ftl"/> 
<#include "/cms/common/page.ftl" />
<@backend title="广告管理" 
    js=['/statics/cms/js/common.js',
    	'/statics/backend/js/layer/layer.min.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/layerly/layer.js',
        
		'/statics/cms/js/common/time/js/jquery-ui-1.9.2.custom.js',
		'/statics/cms/js/common/time/js/jquery-ui-timepicker-addon.js',
		'/statics/cms/js/common/time/js/jquery-ui-timepicker-zh-CN.js',
		
        '/statics/cms/js/jquery/jquery.json-2.4.min.js',
        
		'/statics/backend/js/swfupload/js/fileprogress.js',
		'/statics/backend/js/swfupload/js/handlers.js',
		'/statics/backend/js/jqgrid/js/jquery.jqGrid.min.js',
		'/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js',
		'/statics/qiniu/js/plupload/plupload.full.min.js',
	    '/statics/qiniu/js/plupload/plupload.dev.js',
	    '/statics/qiniu/js/plupload/moxie.js',
		'/statics/qiniu/js/plupload/moxie.js',
		'/statics/qiniu/src/qiniu.js',
		'/statics/qiniu/js/highlight/highlight.js',
		'/statics/qiniu/js/ui.js',
		'/statics/backend/js/uplod.js',
		'/statics/backend/js/areaUplod.js',
		'/statics/backend/js/basedata/upload.js',
		'/statics/cms/js/addAdvertise.js',
		
		'/statics/cms/js/jquery/jquery.ui.core.js',
		'/statics/cms/js/jquery/jquery.form.js'
       ] 
    css=['/statics/backend/css/style.css',
    	'/statics/qiniu/bootstrap/css/bootstrap.css'
    	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.css',
    	'/statics/cms/js/common/time/css/jquery-ui-timepicker-addon.css',
    	'/statics/cms/js/common/time/css/jquery-ui-1.8.17.custom.css'] >
<style type="text/css">
	.container{margin-top:30px}
	.table{text-align:center}
	.table .form-control{box-shadow:none;border:0;cursor:pointer}
</style>
<input type="hidden" value="${sessionId}" id="sessionId" />
<form id="inputForm" action="saveAdvertiseTemp.htm" method="post" enctype="multipart/form-data">	

	<input type="hidden" id="nameBak" 	value="${nameBak}" >
	<input type="hidden" id="positionBak" value="${positionBak}" >
	<input type="hidden" id="startdateBak" value="${startdateBak}" >
	<input type="hidden" id="enddateBak" 	value="${enddateBak}" >
	<input type="hidden" id="typeBak" value="${typeBak}" >
	<input type="hidden" id="identBak" value="${identBak}" >
	<input type="hidden" id="statusBak" value="${statusBak}" >
	
	<input type="hidden" id="bucketname" name="bucketname" value="${bucketname}">
    <input type="hidden" id="bucketURL" name="bucketURL" value="${bucketURL}">

	<div class="mt10" id="forms">
	    <div class="box">
	        <div class="box_border">
	            <div class="box_top">
	                <b class="pl15">CMS管理->首页管理->广告管理</b> 
	            </div>
	            <div class="box_center">
	                
	                <div class="box_border">
						<div class="box_top">
							<b class="pl15">详情信息</b>
						</div>
						<#include "/cms/img/add_pictures.ftl">
						<table class="input tabContent">
							<tr>
								<td class="td_right" rowspan="1"><span class="requiredField">*</span>图片名称：</td>
								<td>
									<input type="text" name="advertname" maxlength="30" value="${detail.advertname}" rows="3" 
										size="20" class="input-text lh30 advertname">
									<input type="hidden" name="idd" value="${detail.id}" class="idd">
								</td>
								
								<td class="td_right">平台标识：</td>
								<td>
									<span class="fl"> 
										<select name="platformType" class="select platformType" onChange="oncl()">
											<option value="0" <#if "${detail.platformType}"=="0"> selected</#if>>PC</option>
											<option value="1" <#if "${detail.platformType}"=="1"> selected</#if>>APP</option>
										</select>
									</span>
								</td>
								<!--td class="td_right"><span class="requiredField">*</span>图片位置：</td>
								<td><input type="text" name="position" value="${detail.position}"
									class="input-text lh30 position" size="20"></td-->
									
								<td colspan="1" rowspan="5" class="" width="50" align="left">
									<a href="javascript:void(0);"><img id="imgPrivewsrc" src="<#if detail.path??>${bucketURL}${detail.path}</#if>" alt="加载图片失败" width="266" height="197" /></a>
                                </td>
                                
							</tr>
							<tr>
								<td class="td_right"><span class="requiredField">*</span>页面位置：</td>
								<td class=""><!--span class="fl"> <select
										name="type" class="select type">
											<option value="3" <#if "${detail.type}"=="3"> selected</#if>>首页-轮播图</option>
											<option value="5" <#if "${detail.type}"=="5"> selected</#if>>首页-最优惠图片</option>
											<option value="6" <#if "${detail.type}"=="6"> selected</#if>>最后疯抢-最优惠图片</option>
											<option value="7" <#if "${detail.type}"=="7"> selected</#if>>首页-弹出层</option>
											<option value="8" <#if "${detail.type}"=="8"> selected</#if>>用户登录-图片</option>
											
											<option value="101" <#if "${detail.type}"=="101"> selected</#if>>(APP)首页-广告位</option>
											<option value="102" <#if "${detail.type}"=="102"> selected</#if>>(APP)秒杀-广告位信息</option>
											<option value="103" <#if "${detail.type}"=="103"> selected</#if>>(APP)首页-功能标签信息</option>
											<option value="104" <#if "${detail.type}"=="104"> selected</#if>>(APP)海淘-广告位信息</option>
											<option value="105" <#if "${detail.type}"=="105"> selected</#if>>(APP)广告-启动页面</option>
											<option value="106" <#if "${detail.type}"=="106"> selected</#if>>(APP)广告-支付成功</option>
											<option value="107" <#if "${detail.type}"=="107"> selected</#if>>(APP)wap-首页弹框</option>
											<option value="108" <#if "${detail.type}"=="108"> selected</#if>>(APP)Wap-今日精选-首图</option>
									</select>
								</span-->
									
									<input type="text" readonly="readonly" name="type" id="type" value="${detail.type}" class="input-text lh30 type" size="20">
                               	 	<input type="hidden" name="ident" id="ident" value="${detail.ident}" class="input-text lh30 ident" size="20">
                               	 	<input id="advert_type_query" class="ext_btn" type="button" value="查询">
								</td>
								
								<td class="td_right"><span class="requiredField">*</span>图片排序：</td>
								<td class="">
									<input type="text" id="sort" name="sort" value="${detail.sort}" rows="3" 
										size="20" class="input-text lh30 sort">
								</td>
								
							</tr>
							<tr>
								<td class="td_right"><span class="requiredField">*</span>是否启用</td>
								<td class=""><span class="fl"> <select
										name="status" class="select status">
											<option value="0" <#if "${detail.status}"=="0"> selected</#if>>启用</option>
											<option value="1" <#if "${detail.status}"=="1"> selected</#if>>停用</option>
											<option value="2" <#if "${detail.status}"=="2"> selected</#if>>过期</option>
									</select>
								</span></td>
								
								<td class="td_right"><span class="requiredField">*</span>开始时间：</td>
								<td class="">
									<input type="text" id="startdate" name="startdate" value="${detail.startdateStr}" rows="3" 
										size="20" class="input-text lh30 startdateStr">
								</td>
								
							</tr>
							
							<tr>
								<td class="td_right">图片路径：</td>
								<td><input type="text" name="path" value ="${detail.path}"
									class="input-text lh30 path" size="30" readonly="readonly"></td>
									
								<td class="td_right"><span class="requiredField">*</span>结束时间：</td>
								<td class="">
									<input type="text" id="enddate" name="enddate" value="${detail.enddateStr}" rows="3" 
										size="20" class="input-text lh30 enddateStr">
								</td>
							</tr>
							<tr>
								<td class="td_right">图片链接：</td>
								<td><input type="text" name="link" value ="${detail.link}"
									class="input-text lh30 link" size="30" ></td>
								<td class="td_right">活动类型：</td>
								<td>
									<input type="text" name="actType" value ="${detail.actType}"
									class="input-text lh30 path" size="20" >
								</td>
							</tr>
							<tr>
								<td class="td_right">活动id：</td>
								<td><input type="text" name="activityid" value ="${detail.activityid}"
									class="input-text lh30 activityid" size="30" ></td>
									
								<!--td class="td_right">商品id：</td>
								<td>
									<input type="text" name="productid" value ="${detail.productid}"
									class="input-text lh30 productid" size="20" >
								</td-->
								
								<td class="td_right">sku：</td>
								<td>
									<input type="text" maxlength="100" name="sku" value ="${detail.sku}"
									class="input-text lh30 sku" size="20" >
								</td>
								
								<td>
									<a id="picSizeRemark" href="javascript:void(0);">图片尺寸说明</a>
								</td>
								
							</tr>
							<tr>
								<td class="td_right">启动时间(秒)：</td>
								<td><input type="text" name="time" value ="${detail.time}"
									class="input-text lh30 time" size="20" ></td>
								
								<td></td><td></td>
							</tr>
						</table>
					</div>
					
					<div class="tc">
							<input type="button" id="inputFormSaveBtn" value="保存" class="ext_btn ext_btn_submit m10">
							<!--input type="hidden" value="${detailId}" name="detail.id" />
							<input type="hidden" name="skuListJson"  id="skuListJson"/-->
							<input type="button" id="returnPage" value="返回" class="ext_btn m10">
					</div>
					
					<div class="appRemark">
							<span class="requiredField">app需要注意下面几点：活动类型必须要输入</span>
					</div>
	                
	                <#include "/cms/index/queryAdvertTemp.ftl">
	                
	           </div>
	        </div>
	    </div>
	</div>
</form>
</@backend>
