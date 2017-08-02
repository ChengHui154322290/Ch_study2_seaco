<#include "/common/common.ftl"/>
<@backend title="" js=[
	'/statics/backend/js/jquery.tools.js',
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/editor/kindeditor-all-min.js',
	'/statics/backend/js/editorUtil.js',
	'/statics/backend/js/tab.js',
	'/statics/backend/js/item/item.js'
	] 
	css=['/statics/backend/css/common.css',
	     '/statics/backend/css/style.css'
	] >
	 <div class="box_border">
       <div class="box_top"><b class="pl15">修改商品信息</b></div>
       <div class="box_center">
		<form id="inputForm" action="save.htm" method="post" enctype="multipart/form-data">
		<!-- 基础信息  -->
		<table class="">
		    	<tr>
				   <td class="td_left">
						<div class="box_bottom pb5 pt5 pr10" style="border-top:0px solid #dadada;">
		                 <div class="search_bar_btn" style="text-align:right;">
		                 <input type="submit" value="保存" class="ext_btn ext_btn_submit">
		                 </div>
		                </div>
                  </td> 
                  <td class="td_rigth">
                    <div class="box_bottom pb5 pt5 pr10" style="border-top:0px solid #dadada;">
		                 <div class="search_bar_btn" style="text-align:right;">
		             	   <input type="button" value="返回" onclick="location.href='javascript:history.go(-1)'" class="ext_btn">
		                 </div>
		            </div>
                  </td>
              </tr>
		</table>
		<table class="commContent"  >
				<tr>
                  <td class="td_right">SPU编号：</td>
                  <td class=""> 
                    <input type="text" name="spu" class="input-text lh30" size="40" value="10000001">
                  </td>
                  <td class="td_right">仓库收货名称：</td>
                  <td class=""> 
                    <input type="text" name="receiveName" class="input-text lh30" size="40" value="上海一号仓库">
                  </td>
                </tr>
                 <tr>
                  <td class="td_right">商品中文名称：</td>
                  <td class=""> 
                    <input type="text" name="enName" class="input-text lh30" size="40" value="婴儿爱喝的奶粉哈">
                  </td>
                  <td class="td_right">商品英文名称：</td><td><input type="text" name="cnName" class="input-text lh30" size="40" value="naifen"></td>
                  </td>
                </tr>
                <tr>
                  <td class="td_right">网站显示名称：</td>
                  <td class=""> 
                    <input type="text" name="mainTitle" class="input-text lh30" size="40" value="婴儿爱喝的奶粉哈">
                  </td>
                  <td class="td_right">副标题：</td><td><input type="text" name="subTitle" class="input-text lh30" size="40" value="婴儿爱喝的奶粉哈"></td>
                  </td>
                </tr>
		</table>
		<ul id="tab" class="tab">
			<li>
				<input type="button" value="基本信息" />
			</li>
			<li>
				<input type="button" value="SKU信息" />
			</li>
			<li>
				<input type="button" value="商品介绍" />
			</li>
			<li>
				<input type="button" value="图片" />
			</li>
			<li>
				<input type="button" value="商品属性" />
			</li>
			<li>
				<input type="button" value="销售信息" />
			</li>
			<li>
				<input type="button" value="仓储采购信息" />
			</li>
			<li>
				<input type="button" value="供应商信息" />
			</li>
			
		</ul>
		<table class="input tabContent"  >
			  <!--
				<tr>
                  <td class="td_right">SPU编号：</td>
                  <td class=""> 
                    <input type="text" name="spu" class="input-text lh30" size="40">
                  </td>
                  <td class="td_right">仓库收货名称：</td>
                  <td class=""> 
                    <input type="text" name="receiveName" class="input-text lh30" size="40">
                  </td>
                </tr>
                 <tr>
                  <td class="td_right">商品中文名称：</td>
                  <td class=""> 
                    <input type="text" name="enName" class="input-text lh30" size="40">
                  </td>
                  <td class="td_right">商品英文名称：</td><td><input type="text" name="cnName" class="input-text lh30" size="40"></td>
                  </td>
                </tr>
                <tr>
                  <td class="td_right">网站显示名称：</td>
                  <td class=""> 
                    <input type="text" name="mainTitle" class="input-text lh30" size="40">
                  </td>
                  <td class="td_right">副标题：</td><td><input type="text" name="subTitle" class="input-text lh30" size="40"></td>
                  </td>
                </tr>-->
                <tr >
                  <td class="td_right">商品类别：</td>
                  <td class="">
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="largeId" class="select"> 
                        <option>大类１</option> 
                        <option>大类２</option> 
                        <option>大类３</option> 
                        </select> 
                        </div> 
                      </div> 
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="mediumId" class="select"> 
                        <option>中类１</option> 
                        <option>中类２</option> 
                        <option>中类３</option> 
                        </select> 
                        </div> 
                      </div> 
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="smallId" class="select"> 
                        <option>小类１</option> 
                        <option>小类11111111111２11111</option> 
                        <option>小类22222222３</option> 
                        </select> 
                        </div> 
                      </div> 
                    </span>
                  </td>
                  <td class="td_right">商品品牌：</td>
                  <td class="">
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="brandId" class="select"> 
                        <option>西客商城11111111111111</option>
                        <option>帮宝适</option> 
                        <option>好孩子</option> 
                        </select> 
                        </div> 
                      </div> 
                    </span>
                  </td>
                 </tr>
                 <tr >
                  <td class="td_right">生产厂家：</td><td><input type="text" name="manufacturer" class="input-text lh30" size="40"></td>
                  </td>
                  <td class="td_right">商品类型：</td>
                  <td class="">
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="itemType" class="select"> 
                        <option>正常商品</option> 
                        <option>服务商品</option> 
                        <option>二手商品</option>
                        <option>报废商品</option> 
                        </select> 
                        </div> 
                      </div> 
                    </span>
                  </td>
                 </tr>
		</table>
		<table class="input tabContent">
				<tr>
               	   <th width="15">序号</th>
                   <th width="100">SKU编号</th>
                   <th width="200">颜色</th>
                   <th width="200">尺寸</th>
                   <th width="60">限购数量</th>
                   <th width="60">起购数量</th>
                   <th width="60">市场价</th>
                   <th width="60">销售价</th>
                   <th width="60">排序</th>
                   <th width="100">操作</th>
                </tr>
                
                <tr>
                   <td width="15">1</td>
                   <td width="100">0001</td>
                   <td width="200">红色</td>
                   <td width="200">M</td>
                   <td width="60">20</td>
                   <td width="60">1</td>
                   <td width="60">100</td>
                   <td width="60">100</td>
                   <td width="60">是</td>
                   <td width="100"><a href="#" class="itemSkubtn">[修改]</a></td>
                </tr>
		</table>
		<table id="" class="input tabContent">
				<tr>
					<th>
					</th>
					<td>
					<textarea id="editor" name="description" class="editor" style="width: 1100px;"></textarea>
					</td>
				</tr>
		</table>
		<table id="" class="input tabContent">
				<tr>
				</tr>
		</table>
		<table id="" class="input tabContent">
				<tr>
               	   <th width="15">序号</th>
                   <th width="100">属性名称</th>
                   <th width="200">属性值</th>
                </tr>
                
                <tr>
                   <td width="15">1</td>
                   <td width="100">型号</td>
                   <td width="200">A－001</td>
                </tr>
		</table>
		<table id="" class="input tabContent">
				 <tr >
                  <td class="td_right">销售模式：</td>
                  <td class="">
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="" class="select"> 
                        <option>买断</option> 
                        <option>分销</option> 
                        <option>平台</option> 
                        </select> 
                        </div> 
                      </div> 
                    </span>
                  </td>
                  <td class="td_right">经销税率：</td>
                  <td class="">
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="taxRate" class="select"> 
                        <option>17%</option> 
                        <option>3%</option> 
                        </select> 
                        </div> 
                      </div> 
                    </span>
                  </td>
                 </tr>
                 <tr>
                    <td class="td_right">是否用券：</td>
                    <td class=""> 
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="vouchersSign" class="select"> 
                        <option>是</option> 
                        <option>否</option>
                        </select> 
                        </div> 
                      </div> 
                    </span>
                   </td>
                   <td class="td_right">是否无理由退货：</td>
                   <td class=""> 
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="noreasonReturn" class="select"> 
                        <option>是</option> 
                        <option>否</option>
                        </select> 
                        </div> 
                      </div> 
                    </span>
                   </td>
                 </tr> 
                  <tr>
                   <td class="td_right">是否开票：</td>
                   <td class=""> 
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="invoiceSign" class="select"> 
                        <option>是</option> 
                        <option>否</option>
                        </select> 
                        </div> 
                      </div> 
                    </span>
                   </td>
                    <td class="td_right">是否开增票：</td>
                    <td class=""> 
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="invoiceSign" class="select"> 
                        <option>是</option> 
                        <option>否</option>
                        </select> 
                        </div> 
                      </div> 
                    </span>
                   </td>
                   </td>
                 </tr> 
		</table>
		<table class="input tabContent">
			    <tr>
                  <td class="td_right">长：</td>
                  <td class=""> 
                    <input type="text" name="length" class="input-text lh30" size="10">
                  </td>
                  <td class="td_right">宽：</td><td><input type="text" name="width" class="input-text lh30" size="10"></td>
                  <td class="td_right">高：</td><td><input type="text" name="height" class="input-text lh30" size="10"></td>
                  </td>
                </tr>
                <tr>
                  <td class="td_right">毛重：</td>
                  <td class=""> 
                    <input type="text" name="grossWeight" class="input-text lh30" size="10">
                  </td>
                  <td class="td_right">净重：</td><td><input type="text" name="netWeight" class="input-text lh30" size="10"></td>
                  <td class="td_right">箱入数：</td><td><input type="text" name="intboxNums" class="input-text lh30" size="10"></td>
                </tr>
                 <tr>
                  <td class="td_right">是否液体：</td>
                  <td class=""> 
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="fluidSign" class="select"> 
                        <option value="1">是</option>
                        <option value="2">否</option> 
                        </select> 
                        </div> 
                      </div> 
                    </span>
                  </td>
                  <td class="td_right">是否易碎：</td><td>
                   <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="fragileSign" class="select"> 
                        <option value="1">是</option>
                        <option value="2">否</option> 
                        </select> 
                        </div> 
                      </div> 
                    </span>
                  </td>
                  <td class="td_right">是否粉末：</td><td>
                   <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="powderSign" class="select"> 
                        <option value="1">是</option>
                        <option value="2">否</option> 
                        </select> 
                        </div> 
                      </div> 
                    </span>
                  </td>
                </tr>
                <tr>
                  <td class="td_right">包装清单：</td>
                  <td class=""> 
                    <input type="text" name="packList" class="input-text lh30" size="10">
                  </td>
                  <td class="td_right">入库仓库：</td><td>
				   <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="inStorageId" class="select"> 
                        <option>仓库一</option> 
                        <option>仓库二</option>
                        <option>仓库三</option> 
                        </select> 
                        </div> 
                      </div> 
                    </span>
				  </td>
                  <td class="td_right">发货仓库：</td><td>
                  <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="outStorageId" class="select"> 
                        <option>仓库一</option> 
                        <option>仓库二</option>
                        <option>仓库三</option> 
                        </select> 
                        </div> 
                      </div> 
                    </span>
                  </td>
                </tr>
                <tr>
                  <td class="td_right">是否保质期管理：</td>
                  <td class=""> 
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="expSign" class="select"> 
                        <option value="1">是</option>
                        <option value="2">否</option> 
                        </select> 
                        </div> 
                      </div> 
                    </span>
                  </td>
                  <td class="td_right">是否进口商品：</td><td>
                   <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="importedSign" class="select"> 
                        <option value="1">是</option>
                        <option value="2">否</option> 
                        </select> 
                        </div> 
                      </div> 
                    </span>
                  </td>
                  <td class="td_right">是否批次管理</td><td>
                   <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="batchSign" class="select"> 
                        <option value="1">是</option>
                        <option value="2">否</option> 
                        </select> 
                        </div> 
                      </div> 
                    </span>
                  </td>
                </tr>
                <tr>
                  <td class="td_right">销售税率：</td>
                  <td class=""> 
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="invoicingRate" class="select"> 
                        <option value="1">17%</option>
                        <option value="2">3%</option> 
                        </select> 
                        </div> 
                      </div> 
                    </span>
                  </td>
                  <td class="td_right">最晚入库时间：</td><td>
                   <input type="text" name="lastInDate" class="input-text lh30" size="20">
                  </td>
                  <td class="td_right">保质期</td><td>
                    <input type="text" name="expDate" class="input-text lh30" size="20">
                  </td>
                </tr>
		</table>
		<table class="input tabContent">
				<tr>
               	   <th width="15">序号</th>
                   <th width="100">供应商编号</th>
                   <th width="200">供应商类别</th>
                   <th width="200">供应商名称</th>
                   <th width="100">操作</th>
                </tr>
                
                <tr>
                   <td width="15">1</td>
                   <td width="100">0001</td>
                   <td width="200">A</td>
                   <td width="200">帮宝适供应商</td>
                   <td width="100"><a href="#">[修改]</a></td>
                </tr>
		</table>
		<table class="input tabContent">
			<tr><th>SKU444</th><td></td></tr>
		</table>
		<!---->
	</form>
		</div>
	</div>
	</@backend>