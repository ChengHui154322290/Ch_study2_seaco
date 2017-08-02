<#include "/common/common.ftl"/>
<@backend title="" js=[
'/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/wx/menu.js',
'/statics/backend/js/jqueryui/js/jquery-ui-1.9.2.custom.min.js',
'/statics/backend/js/jqueryui/i18n/jquery.ui.datepicker-zh-CN.js',
'/statics/backend/js/ztree/js/jquery.ztree.core.js',
'/statics/backend/js/ztree/js/jquery.ztree.excheck.js',
'/statics/backend/js/ztree/js/jquery.ztree.exedit.js'
]
css=[
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
'/statics/backend/js/formValidator/style/validator.css',
'/statics/backend/css/style.css' ,
'/statics/backend/css/ztree.css' ,
'/statics/backend/js/ztree/css/zTreeStyle/zTreeStyle.css'
] >
<br>
<input type="button" value="发布到微信公众号" id= "pushBtn" class="ext_btn ext_btn_submit">
（右键添加菜单）<br>
<div class="content_wrap">
	<div class="zTreeDemoBackground left">
		<ul id="menuTree" class="ztree"></ul>
	</div>
	<div class="right" id="editMenuDiv">
		<#if resultInfo.msg??>${resultInfo.msg.message}<#else>
		<#assign menu = resultInfo.data>
		<form method="post" action="${domain}/wx/menu/save.htm" id="saveMenuForm">
		<input type="hidden" id="id" name="id" value="${menu.id}"/>
		<input type="hidden" id="pid" name="pid" value="${menu.pid}"/>
		<div id="search_bar" class="mt10">
			<div class="box">
				<div class="box_border">
					<div class="box_center pt10 pb10">
						<div class="box_top">
							<b class="pl15">自定义菜单</b>
						</div>
						<div class="box_center pt10 pb10">
							<table class="form_table" border="0" cellpadding="0" cellspacing="0">
								<tr><td style="width:120px;"></td><td></td><tr>
								<tr id = "tr_parentName" style="display:none">
									<td><span style="color: red;">*</span>父菜单：</td>
									<td>
											<div style="float:left">
												<input type="text"  id="parentName" class="input-text lh25" size="45" maxlength = "25" disabled= "disabled">
											</div>
									</td>
								</tr>
								<tr>
									<td><span style="color: red;">*</span>菜单名称：</td>
									<td>
											<div style="float:left">
												<input type="text" name="name" id="name" class="input-text lh25" size="45" maxlength = "25" value="${menu.name}" placeholder="" requeried>
											</div>
											<div id="titleTip" style="width:250px;float:left"></div>
										</td>
								</tr>
								<tr id = "tr_type" style="display:none">
									<td><span style="color: red;">*</span>菜单类型</td>
									<td>
										<span class="fl">
					                      <div class="select_border"> 
					                        <div class="select_containers "> 
					                        <select name="type" id="type" class="select" > 
					                        	<option value=""> 请选择(有子菜单)</option>
						                       	 <#list typeList as type>
						                       		<option value="${type.code}"> ${type.desc} </option>
						                       	 </#list>
					                        </select> 
					                        </div> 
					                      </div> 
					                    </span>
		                    		</td>
								</tr>
								<tr id = "tr_url" <#if menu.code == 'viem'><#else> style="display:none"</#if>>
									<td><span style="color: red;">*</span>URL：</td>
									<td>
											<div style="float:left">
												<input type="text" name="vUrl" id="vUrl" class="input-text lh25" size="100" maxlength = "100" value="${menu.vUrl}" placeholder="" requeried>
											</div>
											<div id="titleTip" style="width:250px;float:left"></div>
										</td>
								</tr>
								<tr id = "tr_key" <#if menu.code == 'click'><#else> style="display:none"</#if>>
									<td><span style="color: red;">*</span>KEY：</td>
									<td>
											<div style="float:left">
												<input type="text" name="cKey" id="cKey" class="input-text lh25" size="45" maxlength = "25" value="${menu.cKey}" placeholder="" requeried>
											</div>
											<div id="titleTip" style="width:250px;float:left"></div>
										</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</div>
			<div style="margin-top:30px;text-align:center;margin-top:10px;">
				<input type="button" value="保存" class="btn btn82 btn_add" id="saveMenuBtn" name="saveMenuBtn">
			</div>
		</div>
		</form>
		</#if>
	</div>
</div>
<div id="rMenu">
	<ul>
		<li id="m_add">增加节点</li>
		<li id="m_del">删除节点</li>
	</ul>
</div>
</@backend>