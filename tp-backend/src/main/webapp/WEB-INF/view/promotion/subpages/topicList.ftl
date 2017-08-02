<#include "/common/common.ftl"/> 
<#include "/common/domain.ftl"/>
<style>

    .caret_down {
        display: inline-block;
        width: 0;
        height: 0;
        vertical-align: top;
        border-top: 6px solid #000000;
        border-right: 6px solid transparent;
        border-left: 6px solid transparent;
        content: "";
        margin-top: 15px;
    }
    .caret_mid {
        display: inline-block;
        width: 0;
        height: 0;
        vertical-align: top;
        border-bottom: 6px solid transparent;
        border-right: 6px solid transparent;
        border-left: 6px solid #000000;
        border-top: 6px solid transparent;
        content: "";
        margin-top: 12px;
    }
    .caret_up {
        display: inline-block;
        width: 0;
        height: 0;
        vertical-align: top;
        border-bottom: 6px solid #000000;
        border-right: 6px solid transparent;
        border-left: 6px solid transparent;
        content: "";
        margin-top: 15px;
    }


</style>
<div style="width:100%;height:30px;">
	<div style="float:right;padding-right:10px;">
		每页：
		<#if ("50" == "${perCount!}")>
			<select name="perCount" class="select">
				<option value="30">30</option>
				<option value="50" selected>50</option>
				<option value="100">100</option>
				<option value="200">200</option>
			</select>
		<#elseif ("100" == "${perCount!}")>
			<select name="perCount" class="select">
				<option value="30">30</option>
				<option value="50">50</option>
				<option value="100" selected>100</option>
				<option value="200">200</option>
			</select>
		<#elseif ("200" == "${perCount!}")>
			<select name="perCount" class="select">
				<option value="30">30</option>
				<option value="50">50</option>
				<option value="100">100</option>
				<option value="200" selected>200</option>
			</select>
		<#else>
			<select name="perCount" class="select">
				<option value="30" selected>30</option>
				<option value="50">50</option>
				<option value="100">100</option>
				<option value="200">200</option>
			</select>
		</#if>
		第<span id="currPage">${currPage!}</span>/<span id="totalPage">${totalPage!}</span>页
		记录总数：<span>${topicCount!}</span>
	</div>
	<div style="float:right;padding-right:10px;">
		<a href="javascript:void(0);" name="nextPage">下一页</a>
	</div>
	<div style="float:right;padding-right:10px;">
		<a href="javascript:void(0);" name="prePage">上一页</a>
	</div>
</div>
<table width="100%" cellspacing="0" cellpadding="0" border="0" class="list_table CRZ" id="topicList">
	<tbody>
	    <tr align="center">
			<th>专场序号</th>
			<th>专场类型</th>
			<th>销售类型</th>
			<th>专场编号</th>
			<th>专场名称</th>
			<th><label id="bt_sort" style="cursor:pointer"> 开始时间<div style="margin-left: 2px;" <#if sort ==1 > class ="caret_down" <#elseif sort ==2>class="caret_up" <#else>class="caret_mid"</#if> ></div></label></th>
			<th><label id="et_sort" style="cursor:pointer">结束时间<div style="margin-left: 2px;" <#if sort ==3 > class ="caret_down" <#elseif sort ==4>class="caret_up" <#else>class="caret_mid"</#if> ></div></label></th>
			<th>状态</th>
			<th>专场进度</th>
			<th>操作</th>
		</tr>
		<#if (topics)??>
			<#list topics as topic>
				 <tr align="center" class="tr" style="background-color: rgb(255, 255, 255);">
					<td>${topic.topic.id!}</td>
					<td>
						<#if (1 == topic.topic.type)>
							单品团
						<#elseif (2 == topic.topic.type)>
							品牌团
						<#elseif (3 == topic.topic.type)>
							主题团
						<#elseif (5 == topic.topic.type)>
							商家店铺
						</#if>
					</td>
					<td>
						<#if (0 == topic.topic.salesPartten)>
							全部
						<#elseif (1 == topic.topic.salesPartten)>
							不限
						<#elseif (2 == topic.topic.salesPartten)>
							旗舰店
						<#elseif (3 == topic.topic.salesPartten)>
							西客商城商城
						<#elseif (4 == topic.topic.salesPartten)>
							海淘
						<#elseif (5 == topic.topic.salesPartten)>
							闪购
						<#elseif (6 == topic.topic.salesPartten)>
							秒杀
						<#elseif (8 == topic.topic.salesPartten)>
							分销
						<#elseif  (9== topic.topic.salesPartten) >
							线下团购
						</#if>

					</td>
					<td>
						<#if topic.skipLink??>
							<#if (1 == topic.topic.type)>
								<a style="padding-right:5px;" href="javascript:window.open('${item_domain}/${topic.skipLink}');void(0);">${topic.topic.number!}</a>
							<#else>
								<a style="padding-right:5px;" href="javascript:window.open('${m_domain}/${topic.skipLink}');void(0);">${topic.topic.number!}</a>
							</#if>
						<#else>
							<a style="padding-right:5px;" href="${domain}/topic/${topic.topic.id}/detail">${topic.topic.number!}</a>
						</#if>
					</td>
					<td><a style="padding-right:5px;" name="viewTopic" topicId="${topic.topic.id}" href="javascript:void(0);">${topic.topic.name!}</a></td>
					<td><#if (topic.topic.startTime??)>${topic.topic.startTime?string("yyyy-MM-dd HH:mm:ss")!}</#if></td>
					<td><#if (topic.topic.endTime??)>${topic.topic.endTime?string("yyyy-MM-dd HH:mm:ss")!}</#if></td>
					<td>
						<#if (1 == topic.topic.status)>
							审核中
						<#elseif (2 == topic.topic.status)>
							已取消
						<#elseif (3 == topic.topic.status)>
							审核通过
						<#elseif (4 == topic.topic.status)>
							已驳回
						<#elseif (5 == topic.topic.status)>
							终止
						<#else>
							编辑中
						</#if>
					</td>
					<td>
						<#if (1 == topic.topic.progress)>
							进行中
						<#elseif (2 == topic.topic.progress)>
							已结束
						<#else>
							未开始
						</#if>
					</td>
					<td>
						<#if (topic.topic.status == 0 || topic.topic.status == 4)>
							<a style="padding-right:5px;" name="editTopic" topicId="${topic.topic.id}" href="javascript:void(0);">[编辑]</a>
						</#if>
						<#if (topic.topic.status == 0 || topic.topic.status == 4)>
							<a style="padding-right:5px;" name="cancelTopic" topicId="${topic.topic.id}" href="javascript:void(0);">[取消]</a>
						</#if>
						<#if (topic.topic.status == 1)>
							<a style="padding-right:5px;" name="approveTopic" topicId="${topic.topic.id}" href="javascript:void(0);">[批准]</a>
						</#if>
						<#if (topic.topic.status == 1)>
							<a style="padding-right:5px;" name="refuseTopic" topicId="${topic.topic.id}" href="javascript:void(0);">[驳回]</a>
						</#if>
						<#if (topic.topic.status == 3)>
							<a style="padding-right:5px;" name="terminateTopic" topicId="${topic.topic.id}" href="javascript:void(0);">[终止]</a>
						</#if>
						<a style="padding-right:5px;" name="viewTopic" topicId="${topic.topic.id}" href="javascript:void(0);">[详细]</a>
						<a style="padding-right:5px;" name="copyTopic" topicId="${topic.topic.id}" href="javascript:void(0);">[复制]</a>
						<a style="padding-right:1px;" name="showLog" topicId="${topic.topic.id}" href="javascript:void(0);">[lg]</a>
					</td>
				</tr>
			</#list>
		</#if>
	</tbody>
</table>

<div style="margin-top:10px;width:100%;height:30px;">
	<div style="float:right;padding-right:10px;">
		每页：
		<#if ("50" == "${perCount!}")>
			<select name="perCount" class="select">
				<option value="30">30</option>
				<option value="50" selected>50</option>
				<option value="100">100</option>
				<option value="200">200</option>
			</select>
		<#elseif ("100" == "${perCount!}")>
			<select name="perCount" class="select">
				<option value="30">30</option>
				<option value="50">50</option>
				<option value="100" selected>100</option>
				<option value="200">200</option>
			</select>
		<#elseif ("200" == "${perCount!}")>
			<select name="perCount" class="select">
				<option value="30">30</option>
				<option value="50">50</option>
				<option value="100">100</option>
				<option value="200" selected>200</option>
			</select>
		<#else>
			<select name="perCount" class="select">
				<option value="30" selected>30</option>
				<option value="50">50</option>
				<option value="100">100</option>
				<option value="200">200</option>
			</select>
		</#if>
		第<span id="currPage">${currPage!}</span>/<span id="totalPage">${totalPage!}</span>页
		记录总数：<span>${topicCount!}</span>
	</div>
	<div style="float:right;padding-right:10px;">
		<a href="javascript:void(0);" name="nextPage">下一页</a>
	</div>
	<div style="float:right;padding-right:10px;">
		<a href="javascript:void(0);" name="prePage">上一页</a>
	</div>
</div>
<script>
    $(document).ready(function() {


        $("#bt_sort").on("click", function() {
            var target = $("#bt_sort").find("div");
            change(0,target);
        });

        $("#et_sort").on("click", function() {
            var target = $("#et_sort").find("div");
            change(2,target);

        });


    });


    function change(name,target) {

        var cname = $(target).attr("class");
        var sort = 0;
        if(name==2){
            sort = 2;
        }
        if(cname=="caret_mid"){
            sort++;
            $(target).attr("class","caret_down");
        }else if(cname =="caret_down") {
            sort++;
            sort++;
            $(target).attr("class","caret_up");
        }else {
            sort = 0;
            $(target).attr("class","caret_mid");
        }
        $("#sort_val").val(sort);
        searchTopics(1);
    }

</script>