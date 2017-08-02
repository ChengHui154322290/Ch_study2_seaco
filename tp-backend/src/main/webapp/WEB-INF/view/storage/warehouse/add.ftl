<#include "/common/common.ftl"/>

<@backend title="仓库添加"
 js=[
'/statics/backend/js/formValidator/formValidator-4.0.1.min.js',
'/statics/backend/js/formValidator/formValidatorRegex.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/backend/js/jquery.form.2.2.7.js',
'/statics/backend/js/form.js',
'/statics/backend/js/formValidator/DateTimeMask.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/storage/js/area/1.js',
'/statics/backend/js/storage/js/area/2.js',
'/statics/backend/js/storage/js/area/3.js',
'/statics/backend/js/storage/js/area/4.js',
'/statics/backend/js/storage/js/china-area.js',
'/statics/backend/js/ztree/js/jquery.ztree.core-3.5.min.js',
'/statics/backend/js/ztree/js/jquery.ztree.excheck-3.5.min.js',
'/statics/select2/js/select2.js',
'/statics/select2/js/select2Util.js',
'/statics/select2/js/select2_locale_zh-CN.js',
'/statics/backend/js/item/item-select2.js',
'/statics/backend/js/storage/js/warehouse.js',
'/statics/backend/js/storage/js/bondedarea.js'
] 
css=[
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
'/statics/backend/js/formValidator/style/validator.css',
'/statics/backend/css/style.css' ,
'/statics/select2/css/select2.css',
'/statics/backend/js/ztree/css/zTreeStyle/zTreeStyle.css'
 ] >

<form action="${domain}/storage/warehouse/save.htm" class="jqtransform"  method="post"  id='warehouseSaveForm'  > 
<input type ="hidden" id="selectAreaIds" name ="deliverAddr" />
<div>
	<span id="pageAdd" style="display:none">addWarehouse</span>
	<table class="form_table" id="table1">
		<tr>
	       <td><h4>基本数据</h4></td>
	    </tr>
		<tr>
			<td class="td_right" >供应商名称:</td>
			<td><div style="display:inline"><input type="text"  readonly= "true" name="spName" id='spname' class="input-text lh25" size="20" value="${spName}" /></div>
			<div style="display:inline"><input id="selectSupplierbtn" class="ext_btn ext_btn_submit m10" type="button" value="选择供应商"></div>
			<div id="spnameTip" style="display:inline"></div>
			</td>
			<td><input type="hidden" id="spid" name="spId"/></td>
		</tr>
		<tr>
			<td class="td_right">机构：</td>
			<td>
                        <select id="districtId" name="districtId" class="select">
                        <!--<option value="">-- --</option>-->
                        <#list agencyAdd as dis>
                        	<option value="${dis.id}">${dis.name}</option>
                        </#list>
                        </select>
             </td>
            <td><div id="districtIdTip" style="width:200px"></div></td>
		</tr>
		<tr>
			<td class="td_right">仓库名称:</td>
			<td><div style="float:left;width:auto"><input type="text" name="name" id='storagename' class="input-text lh25" size="20" maxlength="20"/></div><div id="storagenameTip" style="display:inline;width:auto"></div> </td>
		</tr>
		<tr>
			<td class="td_right">仓库编号:</td>
			<td>
					<div style="display:inline;width:auto"><input type="text"  readonly="true" name="code" id="code"  value="" class="input-text lh25" size="20" /><font>(系统自动生成)</font></div>
					<div id="codeTip" style="display:inline;width:auto"></div>
			</td>
		</tr>
		<tr>
			<td class="td_right">仓库地址:</td>
			<td><input type="text" name="address" id='address' class="input-text lh25" size="50" maxlength="50"/> </td>
			<td><div id="addressTip" style="width:auto"></div></td> 
		</tr>
		<tr>
			<td class="td_right">邮编:</td>
			<td><div style="float:left"><input type="text" name="zipCode" id="zipcode" class="input-text lh25" size="20" maxlength="20"/></div><div style="display:inline;width:30%" id="zipcodeTip"></div></td>
		</tr>
		<tr>
			<td class="td_right">联系人:</td>
			<td><div style="float:left"><input type="text" name="linkman" id="linkman" class="input-text lh25" size="20" maxlength="20"/></div><div style="display:inline;width:30%" id="linkmanTip"></div></td>
		</tr>
		<tr>
			<td class="td_right">电话:</td>
			<td><div style="float:left"><input type="text" name="phone" id="phone" class="input-text lh25" size="20" maxlength="20"/></div><div style="display:inline;width:30%" id="phoneTip"></div></td>
		</tr>
		<tr>
			<td class="td_right">类型:</td>
			<td>
				 <select  name="type" class="select" id="storageType">
				   <#list storageTypes  as l> 
	                    	<option value="${l.getValue()}">${l.getName()}</option>
	               </#list>
	             </select>
			</td>
		</tr>
		<tr class="bondedAreaTr" >
			<td class="td_right">通关渠道:</td>
			<td> 
				 <select id="bondedArea" name="bondedArea" class="select" >
					
			     </select>
			</td>
		</tr>
		<tr>
			<td class="td_right">是否主仓库</td>
			<td>
				<select id="mainType" name="mainType" class="select">
					<option value="">请选择</option>
					<option value="0">否</option>
					<option value="1">是</option>
				</select>
			</td>
		</tr>
		<tr class="mainWarehouseTr">
			<td class="td_right">主仓库</td>
			<td>
				<select name="mainWarehouseId" class="select" id="mainWarehouseId" style="width:200px;">
					<option value="0">默认仓库</option>
					<#if warehouseList??>
						<#list warehouseList as wh>
							<option value="${wh.id}" whname="${wh.name}" spid="${wh.spId}" spname="${wh.spName}">${wh.spName}-${wh.name}</option>
						</#list>
					</#if>
				</select>
			<td>
			<td><input type ="hidden" id="mainWarehouseName" name ="mainWarehouseName" /></td>
			<td><input type ="hidden" id="mainSpId" name ="mainSpId" /></td>
			<td><input type ="hidden" id="mainSpName" name ="mainSpName" /></td>
		</tr>
		<tr>
			<td class="td_right">是否推送清关单</td>
			<td> 
				 <input type="checkbox" name="putCleanOrder" id="putCleanOrder" value="1" class="cleanorder">
			</td>
		</tr>
		<tr>
			<td class="td_right">是否推送运单</td>
			<td> 
				 <input type="checkbox" name="putWaybill" value="1" class="cleanorder waybill">
			</td>
		</tr>
		<tr>
			<td class="td_right">是否推送订单</td>
			<td> 
				 <input type="checkbox" name="putOrder" value="1" class="cleanorder waybill order">
			</td>
		</tr>
		<tr>
			<td class="td_right">是否推送支付单</td>
			<td> 
				 <input type="checkbox" name="putPayOrder" value="1" class="cleanorder waybill order">
			</td>
		</tr>
		<tr>
			<td class="td_right">是否推送仓库</td>
			<td> 
				 <input type="checkbox" name="putStorage" value="1">
			</td>
		</tr>
	</table>
	<table class="form_table" id="bondedAreaParamTable" style="display: none">
		<tr>
			<td>
				<h4>保税区仓库参数</h4>
			</td>
		</tr>
		<tr>
			<td class="td_right">进口类型</td>
			<td>
				<select  name="importType" class="select2" id="importType" style="width:200px;">
					<option value="-1">-----请选择-----</option>
	                <option value="0">直邮进口(集货模式)</option>
	                <option value="1">保税进口(备货模式)</option>
	            </select>
			</td>
		</tr>			
		<tr>
			<td class="td_right">WMS仓库名称</td>
			<td>
				<div style="float:left;width:auto"><input type="text" name="wmsName" id='wmsName' class="input-text lh25" size="20" maxlength="40"/></div>
			</td>
		</tr>
		<tr>
			<td class="td_right">WMS仓库编号</td>
			<td>
				<div style="float:left;width:auto"><input type="text" name="wmsCode" id='wmsCode' class="input-text lh25" size="20" maxlength="40"/></div>
			</td>
		</tr>
		<tr>
			<td class="td_right">进出口岸(关区)</td>
			<td>
				<select  name="ioSeaport" class="select2" id="ioSeaport" style="width:200px;">
					<option value="0">-----请选择-----</option>
				   <#list seaportList  as sp> 
	                    <option value="${sp.code}">${sp.name}</option>
	               </#list>
	            </select>
			</td>
		</tr>	
		<tr>
			<td class="td_right">申报口岸(关区)</td>
			<td>
				<select  name="declSeaport" class="select2" id="declSeaport" style="width:200px;">
					<option value="0">-----请选择-----</option>
				   <#list seaportList  as sp> 
	                    <option value="${sp.code}">${sp.name}</option>
	               </#list>
	            </select>
			</td>
		</tr>
		<tr>
			<td class="td_right">码头货场</td>
			<td>
				<select  name="customsField" class="select2" id="customsField" style="width:200px;">
					<option value="0">-----请选择-----</option>
				   <#list customsFieldList  as cf> 
	                    <option value="${cf.code}">${cf.desc}</option>
	               </#list>
	            </select>
			</td>
		</tr>
		<tr>
			<td class="td_right">快递企业</td>
			<td>
				<select  name="logistics" class="select2" id="logistics" style="width:200px;">
					<option value="0">-----请选择-----</option>
				   <#list expressList  as ep> 
	                    <option value="${ep.code}">${ep.name}</option>
	               </#list>
	            </select>
			</td>
		</tr>
		<tr>
			<td class="td_right">快递企业编码（报关参数）</td>
			<td>
				<div style="float:left;width:auto"><input type="text" name="logisticsCode" id='logisticsCode' class="input-text lh25" size="20" maxlength="40"/></div>
			</td>
		</tr>
		<tr>
			<td class="td_right">快递企业名称（报关参数）</td>
			<td>
				<div style="float:left;width:auto"><input type="text" name="logisticsName" id='logisticsName' class="input-text lh25" size="20" maxlength="40"/></div>
			</td>
		</tr>
		<tr>
			<td class="td_right">货主名(仓库参数)</td>
			<td>
				<div style="float:left;width:auto"><input type="text" name="goodsOwner" id='goodsOwner' class="input-text lh25" size="20" maxlength="40"/></div>
			</td>
		</tr>
		<tr>
			<td class="td_right">账册编号(仓库参数)</td>
			<td>
				<div style="float:left;width:auto"><input type="text" name="accountBookNo" id='accountBookNo' class="input-text lh25" size="20" maxlength="40"/></div>
			</td>
		</tr>
		<tr>
			<td class="td_right">仓储企业名称(报关)</td>
			<td>
				<div style="float:left;width:auto"><input type="text" name="storageName" id='storageName' class="input-text lh25" size="20" maxlength="40"/></div>
			</td>
		</tr>	
		<tr>
			<td class="td_right">仓储企业代码(报关)</td>
			<td>
				<div style="float:left;width:auto"><input type="text" name="storageCode" id='storageCode' class="input-text lh25" size="20" maxlength="40"/></div>
			</td>
		</tr>
		<tr>
			<td class="td_right">报关类型</td>
			<td>
				<select  name="declareType" class="select2" id="declareType" style="width:200px;">
					<option value="0">-----请选择-----</option>
				   <#list declareTypeList  as dt> 
	                    <option value="${dt.code}">${dt.desc}</option>
	               </#list>
	            </select>
			</td>
		</tr>	
		<tr>
			<td class="td_right">报关企业名称</td>
			<td>
				<div style="float:left;width:auto"><input type="text" name="declareCompanyName" id='declareCompanyName' class="input-text lh25" size="20" maxlength="40"/></div>
			</td>
		</tr>	
		<tr>
			<td class="td_right">报关企业编号</td>
			<td>
				<div style="float:left;width:auto"><input type="text" name="declareCompanyCode" id='declareCompanyCode' class="input-text lh25" size="20" maxlength="40"/></div>
			</td>
		</tr>	
		<tr>
			<td class="td_right">发货方式</td>
			<td>
				<select  name="postMode" class="select2" id="postMode" style="width:200px;">
					<option value="0">-----请选择-----</option>
				   <#list postModeList as dt> 
	                    <option value="${dt.code}">${dt.desc}</option>
	               </#list>
	            </select>
			</td>
		</tr>	
		<tr>
			<td class="td_right">发件人</td>
			<td>
				<div style="float:left;width:auto"><input type="text" name="senderName" id='senderName' class="input-text lh25" size="20" maxlength="20"/></div>
			</td>
		</tr>	
		<tr>
			<td class="td_right">发件人国别</td>
			<td>
				<select  name="senderCountryCode" class="select2" id="senderCountryCode" style="width:200px;">
					<option value="0">-----请选择-----</option>
				   <#list senderCountryList as sc> 
	                    <option value="${sc.code}">${sc.name}</option>
	               </#list>
	            </select>
			</td>
		</tr>	
		<tr>
			<td class="td_right">发件人城市</td>
			<td>
				<div style="float:left;width:auto"><input type="text" name="senderCity" id='senderCity' class="input-text lh25" size="20" maxlength="20"/></div>
			</td>
		</tr>
		<tr>
			<td class="td_right">仓库申请单编号</td>
			<td>
				<div style="float:left;width:auto"><input type="text" name="applicationFormNo" id='applicationFormNo' class="input-text lh25" size="20" maxlength="50"/></div>
			</td>
		</tr>
		<tr>
			<td class="td_right">贸易国（起运地）</td>
			<td>
				<select  name="tradeCountry" class="select2" id="tradeCountry" style="width:200px;">
					<option value="0">-----请选择-----</option>
					<#list tradeCountryList as tc> 
			            <option value="${tc.code}" <#if tc.code==wareHouseDO.tradeCountry>selected</#if>>${tc.name}</option>
			        </#list>
			    </select>
			</td>
			</tr>			
			<tr>
				<td class="td_right">运输方式</td>
				<td>
					<select  name="trafMode" class="select2" id="trafMode" style="width:200px;">
						<option value="0">-----请选择-----</option>
						<#list trafModeList as tm> 
			            	<option value="${tm.code}" <#if tm.code==wareHouseDO.trafMode>selected</#if>>${tm.desc}</option>
			            </#list>
			        </select>
				</td>
			</tr>						
	</table>
	<table class="form_table">	
			<tr>
				 <td class="td_left">
						<div class="box_bottom pb5 pt5 pr10" style="border-top:0px solid #dadada;">
			                 <div class="search_bar_btn" style="text-align:right;">
			                  <input class="btn btn82 btn_save2"  type="button" id='warehouseSaveBtn' value="增加"  />
			                 </div>
		                </div>
                 </td> 
                 <td class="td_rigth">
                    <div class="box_bottom pb5 pt5 pr10" style="border-top:0px solid #dadada;">
		                 <div class="search_bar_btn" style="text-align:right;">
		             	   <input class="btn btn82 btn_nochecked closebtn " type="button" value="取消" id="buttoncancel"  onclick="location.href='javascript:history.go(-1)'"/ >
		                 </div>
		            </div>
                  </td>
            </tr>
	</table>		 
</div>
	<div><h4>配送区域</h4></div>
	<div style="overflow:hidden;border:1px solid gray;width:320px;">
		<div class="zTreeDemoBackground left" style="padding-bottom:50px;">
			<ul id="areaTree" class="ztree"></ul>
		</div>
	</div>	
</form>

</@backend>