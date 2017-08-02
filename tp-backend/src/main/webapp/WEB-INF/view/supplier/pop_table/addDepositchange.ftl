<#include "/supplier/pop_table/context.ftl"/>
<@backend title="押金变更" js=[] 
    css=[] >
    <div class="container" id="supplierPopTable" style="z-index: 19891099;width: 821px; background: none repeat scroll 0% 0% rgb(255, 255, 255);">
	    <div class="box_top">
	        <b class="pl15">押金变更</b>  
	        <a class="box_top_r fr pr15" id="popClosebtn" href="javascript:void(0);">关闭</a>  
	    </div>
    
    <div class="box_center">
	    <form id="" action="" method="post" enctype="multipart/form-data">
		    <table width="400" cellpadding="0" cellspacing="0">
		    	<tr>
		          <td class="td_right" align="center" style="font-size:14px;">押金:</td>
		          <td align="center">
		            <input type="text" style="border:0px;" name="value" id="value" value="${basevalue}"/>
		          </td>
		          <td></td>
		        </tr><br/>
		        
		        <tr>
		          <td class="td_right" align="right" style="font-size:14px;">增/减:</td>
		          <td align="center">
		             <input type="text" name="changeValue" id="changeValue" class="input-text lh30" size="20" onblur="sumValue();">
		          </td>
		          <td align="left">负数表示减少</td>
		        </tr><br/>
		        
		        <tr>
		          <td class="td_right" align="right" style="font-size:14px;">押金:</td>
		          <td align="center">
		             <input type="text" name="newvalue" id="newvalue" class="input-text lh30" size="20" >
		          </td>
		          <td></td>
		        </tr>
		        
		        <tr>
		           <td colspan="3" align="center">
		             <input type="button" value="确定" class="ext_btn ext_btn_submit" id="costconfirm">&nbsp;&nbsp;
		             <input type="button" class="ext_btn"  value="取消" id="costCancel">
		           </td>
		           <td>
				    	<input type="hidden"  name="changeValue" id="changeValue">
				    	<input type="hidden"  name="newvalue" id="newvalue">
		           </td>
		        </tr>
		    </table>
		</form>
    </div>
    
    </div>
</@backend>