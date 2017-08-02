<#include "/common/common.ftl"/>
<@backend title="" js=[] 
    css=['/statics/backend/css/common.css',
    	 '/statics/backend/css/style.css',
    	 '/statics/backend/css/user.css'] >  
  <body>
    <div class="mt10" id="forms">
    <div class="box">
        <div class="box_border">
            <div class="box_center" style="margin-bottom: 30px;">
                <div class="box_top"><b class="pl15">会员管理</b></div>
                <#include "/user/subpage/user_form.ftl" />
                <#include "/user/subpage/user_basic.ftl" />
                <!--虚线-->
                <tr>
                   <td colspan="2">
                       <hr style="border: 1px dashed #247DFF;" />
                   </td>
                </tr>           
                <#include "/user/subpage/user_detail.ftl" />
                <#include "/user/subpage/user_address.ftl" />
                <!--虚线-->
                <tr>
                   <td colspan="2">
                       <hr style="border: 1px dashed #247DFF;" />
                   </td>
                </tr>   
                <#include "/user/subpage/user_promotion.ftl" /> 
            </div>
        </div>
    </div>
	</div>
  </body> 
</@backend>