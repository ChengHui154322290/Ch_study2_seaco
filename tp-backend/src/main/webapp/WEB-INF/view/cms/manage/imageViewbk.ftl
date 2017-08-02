<#include "/common/common.ftl"/> 
<@backend title="cms后台管理" 
		js=[
		'/statics/cms/js/common.js',
		'/statics/backend/js/json2.js',
    	'/statics/backend/js/layer/layer.min.js',
        '/statics/cms/js/common/hi-base.js',
        '/statics/cms/js/common/hi-util.js',
        '/statics/cms/js/layerly/layer.js',
		'/statics/cms/js/common/time/js/jquery-ui-1.9.2.custom.js',
        '/statics/cms/js/jquery/jquery.json-2.4.min.js',
		'/statics/cms/js/jquery/jquery.ui.core.js',
		'/statics/cms/js/jquery/jquery.form.js',
		
		'/statics/cms/js/manage/jquery-1.4.2.min.js',
		'/statics/cms/js/manage/jquery.image-maps.js',
		
		'/statics/cms/js/manage/cmsIndexOpt.js'
			]
		css=['/statics/backend/css/style.css']>
		<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
			<tr>
				<td colspan="2" align="center">
				
					<img src="${domain + '/statics/cms/js/manage/womanExercise150.jpg'}" width="417" height="264" border="0" usemap="#Map" />
					<map name="Map" id="Map">
					
					<#if pageList?exists>
						<#list pageList as page>
							<area shape="rect" coords=${page.coords} href=${page.href} />
						</#list>
					</#if>
												
					    <!--area shape="rect" coords="12,37,181,88" href="http://www.qq.com" />
					    <area shape="rect" coords="12,97,182,143" href="http://www.yahoo.com" />
					    <area shape="rect" coords="18,155,179,200" href="http://www.sina.com" />
					    <area shape="rect" coords="18,211,182,260" href="http://www.baidu.com" /-->
					    
					</map>
					
				</td>
			 </tr>
			 
		</table>
</@backend>