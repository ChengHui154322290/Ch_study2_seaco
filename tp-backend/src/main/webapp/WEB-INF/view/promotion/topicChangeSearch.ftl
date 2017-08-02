<#include "/common/common.ftl"/> 
<@backend title="专场活动变更单查询" 
		js=['/statics/backend/js/promotion/promotion_change_search.js',
			'/statics/backend/js/json2.js',
	        '/statics/backend/js/layer/layer.min.js',
			'/statics/backend/js/promotion/utils.js']
		css=[]>
<div class="mt10" id="forms">
    <div class="box">
        <div class="box_border">
            <div class="box_top">
                <b class="pl15">促销管理->专场活动->专场活动变更单列表</b> 
            </div>
  			<table id="topicSearch" cellspacing="0" cellpadding="0" border="0" width="100%" class="form_table pt15 pb15">  
  				<tbody>
	  				<tr>     	 
						<td class="td_right" width="50" align="right">专场序号:</td>
						<td class="" width="150" align="left"><input type="text" id="changeId" name="changeId" class="input-text lh30 topicInteger" /></td>
						<td class="td_right" width="50" align="right">专场编号:</td>
						<td class="" width="150" align="left"><input type="text" id="number" name="number" class="input-text lh30" /></td>
						<td class="td_right" width="50" align="right">专场名称:</td>
						<td class="" width="150" align="left"><input type="text" id="name" name="name" class="input-text lh30" /></td>
					</tr>
					</tr>
						<td class="td_right" width="50" align="right">专场类型:</td>
						<td class="" width="50" align="left">
							<div class="select_border">
		                        <div class="select_containers">
		                            <span class="fl"> 
										<select name="type" class="select" id="type" style="width:150px">
											<option value="0" selected>全部</option>
											<option value="1">单品团</option>
											<option value="2">品牌团</option>
											<option value="3">主题团</option>
											<option value="4">海淘</option>
										</select>
									</span>
		                        </div>
		                    </div>
						</td>
						<td class="td_right" width="50" align="right">状态:</td>
						<td class="" width="150" align="left">
							<div class="select_border">
		                        <div class="select_containers">
		                            <span class="fl"> 
										<select class="select" name="status" id="status" style="width:150px">
											<option value="-1" selected>全部</option>
											<option value="0">编辑中</option>
											<option value="1">审核中</option>
											<option value="2">已取消</option>
											<option value="3">审核通过</option>
											<option value="4">已驳回</option>
											<option value="5">终止</option>
										</select>
									</span>
		                        </div>
		                    </div>
						</td>
						<td class="td_right" width="50" align="right">专场进度:</td>
						<td class="" width="150" align="left">
							<div class="select_border">
		                        <div class="select_containers">
		                            <span class="fl"> 
										<select class="select" name="process" id="process" style="width:150px">
											<option value="-1">全部</option>
											<option value="0">未开始</option>
											<option value="1">进行中</option>
											<option value="2">已过期</option>
										</select>
									</span>
		                        </div>
		                    </div>
						</td>
					</tr>
					<tr>
						<td class="td_right" width="50" align="right">销售类型:</td>
						<td class="" width="50" align="left">
							<div class="select_border">
		                        <div class="select_containers">
		                            <span class="fl"> 
										<select name="salesPartten" class="select" id="salesPartten" style="width:150px">
											<option value="0" selected>全部</option>
											<option value="1">不限</option>
											<option value="2">旗舰店</option>
											<option value="3">西客商城商城</option>
											<option value="4">洋淘派</option>
											<option value="5">闪购</option>
											<option value="6">秒杀</option>
										</select>
									</span>
		                        </div>
		                    </div>
						</td>
						<td class="td_right" colspan="4" align="right"></td>
					</tr>
					<tr>
						<td colspan="8" align="center">
							<input type="button" id="reset" class="btn btn82 btn_res" value="重置" style="margin-right:50px;" />
							<input type="button" id="search" class="btn btn82 btn_search" value="查询"/>
						</td>
					</tr>
				</tbody>
			</table>
			<div class="search_bar_btn  pt10 pb10 box_border" style="padding-left:5px;">
				<input type="button" id="add" class="btn btn82 btn_add" sytle="margin-left:20px;margin-right:20px;" value="新增" />
			</div>
			<div id="topic_list">
			</div>
		</div>
    </div>
</div>
</@backend>