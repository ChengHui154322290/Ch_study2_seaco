<#include "/common/common.ftl"/>

<@backend title="仓库编辑" js=[
'/statics/backend/js/form.js',
'/statics/backend/js/formValidator/formValidator-4.0.1.min.js',
'/statics/backend/js/formValidator/formValidatorRegex.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/backend/js/jquery.form.2.2.7.js',
'/statics/backend/js/formValidator/DateTimeMask.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/storage/js/warehouse.js',
'/statics/backend/js/storage/js/area/1.js',
'/statics/backend/js/storage/js/area/2.js',
'/statics/backend/js/storage/js/area/3.js',
'/statics/backend/js/storage/js/area/4.js',
'/statics/backend/js/storage/js/china-area.js',
'/statics/select2/js/select2.js',
'/statics/select2/js/select2Util.js',
'/statics/select2/js/select2_locale_zh-CN.js',
'/statics/backend/js/item/item-select2.js',
'/statics/backend/js/ztree/js/jquery.ztree.core-3.5.min.js',
'/statics/backend/js/ztree/js/jquery.ztree.excheck-3.5.min.js'
] 
css=[
'/statics/backend/js/formValidator/style/validator.css',
'/statics/select2/css/select2.css',
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
'/statics/backend/js/ztree/css/zTreeStyle/zTreeStyle.css'
] >
<span id="pageEdit" style="display:none">editWarehouse</span>
 <form action="${domain}/storage/warehouse/update.htm" class="jqtransform"  method="post"  id='warehouseditForm' >
	  <input type ="hidden" id="selectAreaIds" value='${wareHouseDO.deliverAddr}' name ="deliverAddr" />
	  <input type= "hidden" id="hiddenTypeId" value="${wareHouseDO.type}" />
	   <div>	  
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">
	       		   <tr>
	       		   	<td><h4>基本数据</h4></td>
	       		   </tr>
	       		   <tr>
	       		   <td class="td_right">仓库ID:</td>
	       		   	<td><input type="text" readonly="true" name="id" class="input-text lh25" size="20" value="${wareHouseDO.id}" />(不可更改)</td>
	       		   	</tr>
			       <tr>
					<td class="td_right">供应商名称:</td>
					<td><div style="display:inline"><input type="text" readonly= "true" name="spName" id='spname' class="input-text lh25" size="20" value='${wareHouseDO.spName}'/>(不可更改)</div>
					</td>
					<!-- <td>
							<input type="hidden" name="id" id='id' class="input-text lh25" size="20" value='${wareHouseDO.id}'>
					</td> -->
				  </tr>
				<tr>
					<td class="td_right">供应商编号:</td>
					<td><div style="float:left;"><input type="text"  readonly= "true" name="spId" id='spid' class="input-text lh25" size="20" value='${wareHouseDO.spId}'/>(不可更改)</div>
                    </td>
				</tr>
			    <tr>
					<td class="td_right">机构：</td>
					 <td>
		                        <select id="districtid" name="districtId" class="select">
		                        <!--<option value="">-- --</option>-->
		                        <#list agencyEdit as dis>
		                        	<option value="${dis.id}" <#if wareHouseDO.districtId==dis.id>selected='selected'</#if>>${dis.name}</option>
		                        </#list>
		                        </select>
		              </td>
		              <td><div id="districtidTip" style="width:200px"></div></td>
			   </tr>
			   <tr>
					 <td class="td_right">仓库名称:</td>
					 <td><div style="float:left;width:auto"><input type="text" name="name" id='storagename' class="input-text lh25" size="20" value='${wareHouseDO.name}'/></div><div id="storagenameTip" style="display:inline;width:auto"></div> 
					 </td>
				</tr>
				<tr>
					<td class="td_right">仓库编号:</td>
					<td><div style="display:inline;"><input type="text" readonly="true"  name="code" id='code' class="input-text lh25" size="20" value='${wareHouseDO.code}'/><font>(不可更改)</font>
                    </td>
				</tr>
				<tr>
					<td class="td_right">仓库地址:</td>
					<td><input type="text" name="address" id='address' class="input-text lh25" size="50" value="${wareHouseDO.address}"/> </td>
					<td><div id="addressTip" style="width:auto"></div></td> 
				</tr>
				<!--
				<tr>
					<td class="td_right">仓库地址:</td>
					<td><div style="float:left;"><input type="text" name="address" id='address' class="input-text lh25" size="50" value='${wareHouseDO.address}'/></div><div style="display:inline;width:auto" id="addressTip"></div>
                    </td>
				</tr>
				-->
				<tr>
					<td class="td_right">邮编:</td>
					<td><div style="float:left"><input type="text" name="zipCode" id="zipcode" class="input-text lh25" size="20" value='${wareHouseDO.zipCode}'/></div><div style="display:inline;width:30%" id="zipcodeTip"></div></td>
				</tr>
				<tr>
					<td class="td_right">联系人:</td>
					<td><div style="float:left"><input type="text" name="linkman" id="linkman" class="input-text lh25" size="20"  value='${wareHouseDO.linkman}'/></div><div style="display:inline;width:30%" id="linkmanTip"></div></td>
				</tr>
				<tr>
					<td class="td_right">电话:</td>
					<td><div style="float:left"><input type="text" name="phone" id="phone" class="input-text lh25" size="20" value='${wareHouseDO.phone}'/></div><div style="display:inline;width:30%" id="phoneTip"></div></td>
				</tr>
				<tr>
					<td class="td_right">类型:</td>
					<td>
						 <select id="storageType" name="type" class="select">
						 <#list storageTypes  as l> 
	                    	<option value="${l.getValue()}" <#if l.getValue() == wareHouseDO.type > selected</#if> > ${l.getName()}</option>
	              		 </#list>
			             </select>
			             <!-- <input type="hidden" name="type"  value="${wareHouseDO.type}" /> -->
					</td>
				</tr>
				<tr class="bondedAreaTr" >
					<td class="td_right">通关渠道(不可随意改动):</td>
					<td> 
						 <select id="bondedArea" name="bondedArea" class="select" >
							 <option value="0">请选择</option>
							 <#list bondedareaTypes as l> 
				                    	<option value="${l.id}"  <#if l.id == wareHouseDO.bondedArea> selected</#if> >${l.name}</option>
				             </#list>
					     </select>
					     <!-- <input type="hidden" name="bondedArea"  value="${wareHouseDO.bondedArea}" /> -->
					</td>
				</tr>
				<tr>
					<td class="td_right">是否主仓库</td>
					<td>
						<select id="mainType" name="mainType" class="select">
							<option value="">请选择</option>
							<option value="0" <#if wareHouseDO.mainType == 0>selected="selected"</#if>>否</option>
							<option value="1" <#if wareHouseDO.mainType==1>selected="selected"</#if>>是</option>							
						</select>
					</td>
				</tr>
				
					<tr class="mainWarehouseTr" <#if wareHouseDO.mainType != 0>style="display:none;"</#if>>
						<td class="td_right">主仓库</td>
						<td>
							<select name="mainWarehouseId" class="select" id="mainWarehouseId" style="width:200px;">
								<option value="0">默认仓库</option>
								<#if warehouseList??>
									<#list warehouseList as wh>
										<option value="${wh.id}" whname="${wh.name}" spid="${wh.spId}" spname="${wh.spName}" <#if wh.id==wareHouseDO.mainWarehouseId>selected="selected"</#if>>
											${wh.spName}-${wh.name}
										</option>
									</#list>
								</#if>
							</select>
						<td>
						<td><input type ="hidden" id="mainWarehouseName" name ="mainWarehouseName" value="${wareHouseDO.mainWarehouseName}"/></td>
						<td><input type ="hidden" id="mainSpId" name ="mainSpId" value="${wareHouseDO.mainSpId}"/></td>
						<td><input type ="hidden" id="mainSpName" name ="mainSpName" value="${wareHouseDO.mainSpName}"/></td>
					</tr>				
				<tr>
					<td class="td_right">是否推送清关单</td>
					<td> 
						 <input type="checkbox" name="putCleanOrder" id="putCleanOrder" value="1" class="cleanorder" <#if wareHouseDO.putCleanOrder==1>checked</#if>>
					</td>
				</tr>
				<tr>
					<td class="td_right">是否推送运单</td>
					<td> 
						 <input type="checkbox" name="putWaybill" value="1" class="cleanorder waybill" <#if wareHouseDO.putWaybill==1>checked</#if>>
					</td>
				</tr>
				<tr>
					<td class="td_right">是否推送订单</td>
					<td> 
						 <input type="checkbox" name="putOrder" value="1" class="cleanorder waybill order" <#if wareHouseDO.putOrder==1>checked</#if>>
					</td>
				</tr>
				<tr>
					<td class="td_right">是否推送支付单</td>
					<td> 
						 <input type="checkbox" name="putPayOrder" value="1" class="cleanorder waybill order" <#if wareHouseDO.putPayOrder==1>checked</#if>>
					</td>
				</tr>
				<tr>
					<td class="td_right">是否推送仓库</td>
					<td> 
						 <input type="checkbox" name="putStorage" value="1" <#if wareHouseDO.putStorage==1>checked</#if>>
					</td>
				</tr>
			</table>
			<table class="form_table" id="bondedAreaParamTable" <#if wareHouseDO.putCleanOrder != 1>style="display: none"</#if>>
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
							<option value="0" <#if wareHouseDO.importType == 0>selected="selected"</#if>>直邮进口(集货模式)</option>
							<option value="1" <#if wareHouseDO.importType == 1>selected="selected"</#if>>保税进口(备货模式)</option>
			            </select>
					</td>
				</tr>					
				<tr>
					<td class="td_right">WMS仓库名称</td>
					<td>
						<div style="float:left;width:auto"><input type="text" name="wmsName" id='wmsName' value="${wareHouseDO.wmsName}" class="input-text lh25" size="20" maxlength="20"/></div>
					</td>
				</tr>
				<tr>
					<td class="td_right">WMS仓库编号</td>
					<td>
						<div style="float:left;width:auto"><input type="text" name="wmsCode" id='wmsCode' value="${wareHouseDO.wmsCode}" class="input-text lh25" size="20" maxlength="20"/></div>
					</td>
				</tr>
				<tr>
					<td class="td_right">进出口岸(关区)</td>
					<td>
						<select  name="ioSeaport" class="select2" id="ioSeaport" style="width:200px;">
							<option value="0">-----请选择-----</option>
						   <#list seaportList  as sp> 
			                    <option value="${sp.code}" <#if sp.code==wareHouseDO.ioSeaport>selected="selected"</#if>>${sp.name}</option>
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
			                    <option value="${sp.code}" <#if sp.code==wareHouseDO.declSeaport>selected="selected"</#if>>${sp.name}</option>
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
			                    <option value="${cf.code}" <#if cf.code==wareHouseDO.customsField>selected="selected"</#if>>${cf.desc}</option>
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
			                    <option value="${ep.code}" <#if ep.code==wareHouseDO.logistics>selected="selected"</#if>>${ep.name}</option>
			               </#list>
			            </select>
					</td>
				</tr>
				<tr>
					<td class="td_right">快递企业编码（报关参数）</td>
					<td>
						<div style="float:left;width:auto"><input type="text" name="logisticsCode" id='logisticsCode' value="${wareHouseDO.logisticsCode}" class="input-text lh25" size="20" maxlength="20"/></div>
					</td>
				</tr>
				<tr>
					<td class="td_right">快递企业名称（报关参数）</td>
					<td>
						<div style="float:left;width:auto"><input type="text" name="logisticsName" id='logisticsName' value="${wareHouseDO.logisticsName}" class="input-text lh25" size="20" maxlength="20"/></div>
					</td>
				</tr>
				<tr>
					<td class="td_right">货主名(仓库参数)</td>
					<td>
						<div style="float:left;width:auto"><input type="text" name="goodsOwner" id='goodsOwner' value="${wareHouseDO.goodsOwner}" class="input-text lh25" size="20" maxlength="20"/></div>
					</td>
				</tr>
				<tr>
					<td class="td_right">账册编号(仓库参数)</td>
					<td>
						<div style="float:left;width:auto"><input type="text" name="accountBookNo" id='accountBookNo' value="${wareHouseDO.accountBookNo}" class="input-text lh25" size="20" maxlength="20"/></div>
					</td>
				</tr>
				<tr>
					<td class="td_right">仓储企业名称(报关)</td>
					<td>
						<div style="float:left;width:auto"><input type="text" name="storageName" id='storageName' value="${wareHouseDO.storageName}" class="input-text lh25" size="20" maxlength="20"/></div>
					</td>
				</tr>	
				<tr>
					<td class="td_right">仓储企业代码(报关)</td>
					<td>
						<div style="float:left;width:auto"><input type="text" name="storageCode" id='storageCode' value="${wareHouseDO.storageCode}" class="input-text lh25" size="20" maxlength="20"/></div>
					</td>
				</tr>
				<tr>
					<td class="td_right">报关类型</td>
					<td>
						<select  name="declareType" class="select2" id="declareType" style="width:200px;">
							<option value="0">-----请选择-----</option>
						   <#list declareTypeList as dt> 
			                    <option value="${dt.code}" <#if dt.code==wareHouseDO.declareType>selected</#if>>${dt.desc}</option>
			               </#list>
			            </select>
					</td>
				</tr>	
				<tr>
					<td class="td_right">报关企业名称</td>
					<td>
						<div style="float:left;width:auto"><input type="text" name="declareCompanyName" id='declareCompanyName' value="${wareHouseDO.declareCompanyName}" class="input-text lh25" size="20" maxlength="20"/></div>
					</td>
				</tr>	
				<tr>
					<td class="td_right">报关企业编号</td>
					<td>
						<div style="float:left;width:auto"><input type="text" name="declareCompanyCode" id='declareCompanyCode' value="${wareHouseDO.declareCompanyCode}" class="input-text lh25" size="20" maxlength="20"/></div>
					</td>
				</tr>	
				<tr>
					<td class="td_right">发货方式</td>
					<td>
						<select  name="postMode" class="select2" id="postMode" style="width:200px;">
							<option value="0">-----请选择-----</option>
						   <#list postModeList as dt> 
			                    <option value="${dt.code}" <#if dt.code==wareHouseDO.postMode>selected</#if>>${dt.desc}</option>
			               </#list>
			            </select>
					</td>
				</tr>	
				<tr>
					<td class="td_right">发件人</td>
					<td>
						<div style="float:left;width:auto"><input type="text" name="senderName" id='senderName' value="${wareHouseDO.senderName}" class="input-text lh25" size="20" maxlength="20"/></div>
					</td>
				</tr>	
				<tr>
					<td class="td_right">发件人国别</td>
					<td>
						<select  name="senderCountryCode" class="select2" id="senderCountryCode" style="width:200px;">
							<option value="0">-----请选择-----</option>
						   <#list senderCountryList as sc> 
			                    <option value="${sc.code}" <#if sc.code==wareHouseDO.senderCountryCode>selected</#if>>${sc.name}</option>
			               </#list>
			            </select>
					</td>
				</tr>
				<tr>
					<td class="td_right">发件人城市</td>
					<td>
						<div style="float:left;width:auto"><input type="text" name="senderCity" id='senderCity' value="${wareHouseDO.senderCity}" class="input-text lh25" size="20" maxlength="20"/></div>
					</td>
				</tr>
				<tr>
					<td class="td_right">仓库申请单编号</td>
					<td>
						<div style="float:left;width:auto"><input type="text" name="applicationFormNo" id='applicationFormNo' value="${wareHouseDO.applicationFormNo}" class="input-text lh25" size="20" maxlength="50"/></div>
					</td>
				</tr>
				<tr>
					<td class="td_right">贸易国（起运地）</td>
					<td>
						<select  name="tradeCountry" class="select2" id="tradeCountry" style="width:200px;">
							<option value="">-----请选择-----</option>
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
							<option value="">-----请选择-----</option>
						   <#list trafModeList as mode>
			                    <option value="${mode.code}" <#if mode.code==wareHouseDO.trafMode>selected</#if>>${mode.desc}</option>
			               </#list>
			            </select>
					</td>
				</tr>
			</table>				
			<div class="box_bottom pb5 pt5 pr10" style="border-top:0px solid #dadada;">
			      <input class="btn btn82 btn_save2"  type="button" id='warehouseditBtn' value="修改"  />
			       <input class="btn btn82 btn_nochecked closebtn " type="button" value="取消" id="buttoncancel" onclick="location.href='javascript:history.go(-1)'"/>
			</div>  	
   		</div>
        <div style="overflow:hidden;border:1px solid gray;width:320px;">
				<div class="zTreeDemoBackground left" style="padding-bottom:50px;">
					<div>
						<span style="color:red">提示：</span>
						<span style="color:red"><img src="${domain}/statics/backend/images/notAllSelect.png" />灰色选中代表部分选中。
						<br/>
					</div>
					<ul id="areaTree" class="ztree"></ul>
				</div>
		</div>     
     </form>      
</@backend>