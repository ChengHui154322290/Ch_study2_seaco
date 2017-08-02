<#include "/common/common.ftl"/> 
<#include "/cms/common/page.ftl" />
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
        '/statics/backend/js/jqgrid/js/jquery.jqGrid.min.js',
		'/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js',
		'/statics/cms/js/jquery/jquery.ui.core.js',
		'/statics/cms/js/jquery/jquery.form.js',
		
		'/statics/cms/js/manage/cmsIndexOpt.js'
			]
		css=['/statics/backend/css/style.css',
    	'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.css']>
    	
    	<div class="box">
        <div class="box_border">
            <div class="box_top">页面管理</b>
            </div>
            <div class="box_center" style="margin-bottom: 30px;">
            
			<table cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">
			<tbody>
				<tr>
					<td class="td_right">页面缓存刷新： </td>
					<td>
						<input type="button" class="btn btn82 btn_add rushxigouindex" id="rushxgindex" value="刷新页面" style="margin-top: 10px;" />
					</td>
				</tr>
				
				<!--tr>
					<td colspan="2" align="center">
						<div id="imgMap">
							<img src="${domain + '/statics/cms/js/manage/womanExercise150.jpg'}" name="test" width="417" height="264" border="0" usemap="#Map" ref='imageMaps' />
							<map name="Map">
							  <area shape="rect" coords="203,134,383,187" href="http://yiye.name" />
							</map>
						</div>
					</td>
				 </tr>
				 
				 <tr>
					<td>
						<input type="button" class="btn btn82 btn_del subview" id="subview" value="提交" style="margin-top: 10px;" />
					</td>
				 </tr-->
				</tbody>
			</table>
			
		</div>
        </div>
    </div>
</div>
</@backend>