<#include "/layout/inner_layout.ftl" />

<@sellContent title="首页" 
    js=[ '/static/scripts/item/listSellerItem.js',
       	'/static/scripts/item/editor/kindeditor-all.js',
    	'/static/scripts/item/editorUtil.js'
    ]  
    css=[]>
    <script type="text/javascript" charset="utf-8">
    </script>
    <style>
    </style>
    
	<div class="panel panel-default">
	    <div class="panel-heading">
	        <h3 class="panel-title">商品查看</h3>
	    </div>
    <div class="panel-body">
        <form class="form-inline" role="form" id="searchFormId">
	  	    <table width="100%" height="200px;" class="table table-bordered">   
	  	    
	  	  	  <b class="pl15">商品SPU信息</b>
	      		<tr>
					<td width="20%">SPU：</td>
					<td width="80%">
							<input type="text" />
					</td>
	      		</tr>
	      	<tr>
	      		<td width="20%">商品类别：</td>
	      		<td width="80%">
	      			大类：<input type="text" />
	      			中类：<input type="text" />
	      			小类：<input type="text" />
	      		</td>
	      	</tr>
	      	
	      	<tr>
	      		<td width="20%">商品品牌：</td>
	      		<td width="80%">
		      			<input type="text" />
		      		    单位:<input type="text" />
	      		</td>
	      	</tr>
	      	<tr>
	      		<td width="20%">SPU名称：</td>
	      		<td >
		      			<input type="text" />
	      		</td>
	      	</tr>
	      	<tr>
	      		<td width="20%">备注：</td>
	      		<td >
		      			 <input type="text" />
	      		</td>
	      	</tr>
	    </table>
	    
	    
	    
	     <table width="100%" height="200px;" class="table table-bordered">
	     	  <b class="pl15">商品信息</b>
			     <tbody><tr>
			     
			     			<td width="20%">prdid:</td>
							 <td >
							 </td>	
							  <td rowspan="5" align="center"  valign="middle"  >规格维度:</td>
							  <td rowspan="5" >
								 	<ul>
								 	  	<li>
								 	  	</li>
								 	</ul>
							  </td>
						</tr>
						
						<tr>
							 <td width="20%">商品名称:</td>
							 <td>
								 <input type="text" />
							 </td>
						</tr>
						<tr>
							 <td width="20%">网站显示名称:</td> 
							 <td>
								 <input type="text" />
							 </td>
						</tr>
						<tr>
							<td width="20%">仓库名称:</td>
							<td>
								<input type="text" />
							</td>
						</tr>
						<tr>
							 <td width="20%">备注:</td>
							 <td>
							 	<input type="text" />
							 </td>
						</tr>
						
				</tbody>
	     </table>
	     
	     <table width="100%" height="200px;" class="table table-bordered">
	        <b class="pl15">基本信息</b>
		        <tbody>
		        <tr>
					<td width="20%" rowspan="2">商品卖点(副标题):</td>
					<td rowspan="2">
						<textarea  cols="30" rows="3"  maxlength="100" "></textarea>
					</td>
					<td width="10%">市场价:</td>
					<td><input type="text" /></td>
					<td width="10%" >生产厂家:</td>
					<td><input type="text" /></td>
				</tr>
				
				
				<tr>
					<td width="20%">进项税率:</td>
					<td ></td>
				</tr>
				<tr>
					<td width="20%">商品类型:</td>
					<td class="">
					</td>
					<td width="20%">体积:</td>
					<td class="">
					<input type="text"  size="5" style="line-height:normal;text-align:center;"  placeholder="长">
					<input type="text"  size="5" style="line-height:normal;text-align:center;"  placeholder="宽">
					<input type="text"  size="5" style="line-height:normal;text-align:center;"  placeholder="高">&nbsp;cm
					</td>
					
					
					<td width="20%">销项税率:</td>
					<td class="">
					</td>
				</tr>
				<tr>
					<td width="20%">规格:</td>
					<td><input type="text"  size="25"    maxlength="60" ></td>
					<td width="20%">毛重:</td>
					<td><input type="text"  size="5"   maxlength="9" >&nbsp;g</td>
					<td width="20%">箱规:</td>
					<td><input type="text"  size="25"   maxlength="60"></td>	
				</tr>
				
				<tr>
					<td width="20%">无理由退货天数:</td>
					<td><input type="text"  size="5" maxlength="5"></td>
					<td width="20%">净重:</td>
					<td><input type="text" size="5"  maxlength="9" >&nbsp;g</td>
					<td width="20%">条形码:</td>
					<td style="width:15%"><input type="text"  size="20"  maxlength="20"></td>
				</tr>
			</tbody>
	    </table>
	    
	       <table width="100%" height="50px;" class="table table-bordered">
	       
	       	    <tr>
	       			<td width="20%">是否海淘商品:</td>
	       			<td></td>
	       			<td width="20%">是否有效期管理:</td>
	       			<td>
	       			</td>
	       			<td width="20%">适用年龄:</td>
	       			<td>
	       			</td>
	       		</tr>
	       		<tr>
	       				<td width="20%">行邮税税率:</td>
	       				<td>
	       				</td>
	       				<td width="20%">产地:</td>
	       				<td>
	       				</td>
	       		</tr>
	       </table>
	       
	         <table width="100%" height="50px;" class="table table-bordered">
	      		   <b class="pl15">图片与详情</b>
	      		   	<div >
						<ul class="nav nav-tabs nav-justified"  style="width:300px;">   
								<li>
									<a href="#"/>PC模板</a>
								</li>
								<li>
									<a href="#"/>手机模板</a>
								</li>
						</ul>
						<table class="input tabContent"">
							<tr>
								<td>
								<textarea id="editor"    style="width: 100%;">
									
								</textarea>
								</td>
							</tr>
						</table>
						<table  class="input tabContent">
							<tr>
								<td>
								<textarea id="mobileEditor"  style="width:100%;">
									
								</textarea>
								</td>
							</tr>
						</table>
					</div>
					
					
	         </table>
	       
       </form>      
    </div><#-- panel-body -->
</div>
</@sellContent>
