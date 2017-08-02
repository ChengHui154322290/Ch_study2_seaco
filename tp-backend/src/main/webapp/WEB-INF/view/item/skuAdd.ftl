<#include "/common/common.ftl"/>
<@backend title="" js=[	
    '/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/item/item.js'] 
	css=[] >
    <div >
	   <div>	  
	     <form action="${domain}/basedata/item/saveSku" class="jqtransform"  method="post"  id='colorupdate'  > 
	       <table class="form_table" border="0" cellpadding="0" cellspacing="0">
		        <tr>
					<td class="td_right">Sku编号:</td>
					<td><input type="text" name="sku" class="input-text lh25" size="20" >                    
                     </td>
				</tr>
   		
				<tr>
				  <td class="td_right">颜色:</td>
				  <td><input type="text" name="color" class="input-text lh25" size="20" ></td>
				<tr>   
				   <td class="td_right">尺寸:</td>
				   <td><input type="text" name="size" class="input-text lh25" size="20" ></td>
				</tr>	
				<tr>
					<td class="td_right">市场价:</td>
					<td><input type="text" name="basic_price" class="input-text lh25" size="20" >                    
                     </td>
				</tr>
				 <tr>
					<td class="td_right">销售价:</td>
					<td><input type="text" name="xg_price" class="input-text lh25" size="20" >                    
                     </td>
				</tr>		
				 <tr>
					<td class="td_right">库存:</td>
					<td><input type="text" name="quantity" class="input-text lh25" size="20" >                    
                     </td>
				</tr>		
				<tr>
				   <td class="td_left">
						<div class="box_bottom pb5 pt5 pr10" style="border-top:0px solid #dadada;">
		                 <div class="search_bar_btn" style="text-align:right;">
		                   <input class="btn btn82 btn_save2" type="button"  value="保存" param='/basedata/color/addColorSubmit.htm' onclick="colorSubmit(this)"} />
		                 </div>
		                </div>
                  </td> 
                  <td class="td_rigth">
                    <div class="box_bottom pb5 pt5 pr10" style="border-top:0px solid #dadada;">
		                 <div class="search_bar_btn" style="text-align:right;">
		             	   <input class="btn btn82 btn_nochecked closebtn " type="button" value="取消" id="buttoncancel"/ >
		                 </div>
		            </div>
                  </td>
               </tr>
              </table>    
              </form>            
	   </div>	
    </div>
</@backend>