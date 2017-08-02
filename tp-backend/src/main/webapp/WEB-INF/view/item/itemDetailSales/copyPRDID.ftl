<#include "/common/common.ftl"/>
<@backend title="" 
js=['/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/item/item-detailsales.js']
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css']
	 >
<div class="container">

	<form class="jqtransform" method="post" id="saveCopyPRDID" >
		<div class="box">
				<table class="form_table pt15 pb15" width="100%" border="0" cellpadding="0" cellspacing="0" style=" overflow:outo"> 
					<tr  style="height:30px;">
						<td class="td_left" style="width:50px;">格式：PRDID <font color="red">空格</font>历史基数<font color="red">最多200条</font></td>
					</tr>
				</table>  
				
				<table class="form_table pt15 pb15" width="100%" border="0" cellpadding="0" cellspacing="0" style=" overflow:outo"> 
					<tr style="width:100%;"> 
						<td style="width:100%;">
							<textarea rows="15" cols="15" name="prdidList" style="width:100%;"></textarea>    
						</td>  
					</tr>
				</table>    
			</div>
	</div>
	
	</from>	
					<div class="tc">  
							<td>
								<input type="button" class="ext_btn ext_btn_submit m10 saveCopyPRDID"  value="确定">
								<input type="button" class="ext_btn ext_btn_submit m10 closelayerbtn"  value="关闭">
							</td>
						</div>
</@backend>