<#--
	分页标签：用于显示数据分页链接。
	pagination：分页对象。
	url：链接地址
	showPageLinkCount:显示的页数链接数
	isShowMoreLI:是否显示“<li>...</li>”提示更多还有更多页数
	isNeedPageTo：是否显示转到指定页数的表单
-->
<#macro pager pagination url="" formId="" showPageLinkCount=5 isShowMoreLI=true isNeedPageTo=true  >
	<#if (pagination.total)??>
		<#if (pagination.total>=1)>
		    <#assign firstPageUrl=url+"?page=1&size="+(pagination.size)>  
            <#assign prePageUrl=url+"?page="+(pagination.page-1)+"&size="+(pagination.size)>  
            <#assign nextPageUrl=url+"?page="+(pagination.page+1)+"&size="+(pagination.size)>  
            <#assign lastPageUrl=url+"?page="+(pagination.total)+"&size="+(pagination.size)> 
            <#assign ss="${formId}">
            <div id="pagediv"></div> 
			<ul class="pager">
				<li class="firstPage">
					<#if (pagination.page>1)>
						<a class="vvssso" href="javascript:void(0);" param="1" >首页</a>
					<#else>
						<span>首页</span>
					</#if>
				</li>
				<li class="prePage">
					<#if (pagination.page>1)>
						<a class="vvssso" href="javascript:void(0);" param="${pagination.page-1}"  >上一页</a>
					<#else>
						<span>上一页</span>
					</#if>
				</li>
				<@outPutPageNo pagination=pagination url=url  formId=formId showPageLinkCount=showPageLinkCount isShowMoreLI=isShowMoreLI  />
		    
				<li class="nextPage">
					<#if (pagination.page<pagination.total)>
						<a class="vvssso" href="javascript:void(0);" param="${pagination.page+1}" >下一页</a>
					<#else>
						<span>下一页</span>
					</#if>
				</li>
			
				<li class="lastPage">
					<#if (pagination.page<pagination.total)>
						<a class="vvssso" href="javascript:void(0);" param="${pagination.total}" >尾页</a>
					<#else>
						<span>尾页</span>
					</#if>
				</li>
				
				<li class="pageInfo">
					共 ${pagination.total} 页
				</li>
				<li class="">每页条数
					<select id="sizeaaa" name="size" onchange="sizeChange()">
					<option <#if pagination.size=='10'>selected='selected'</#if> value='10'>10</option>
					<option <#if pagination.size=='20'>selected='selected'</#if> value='20'>20</option>
					<option <#if pagination.size=='30'>selected='selected'</#if> value='30'>30</option>
					<option <#if pagination.size=='50'>selected='selected'</#if> value='50'>50</option>
					</select>
				</li>		
				<#if isNeedPageTo>
					<li class="pageTo">
							转到第&nbsp;<input type="text" id="pageToNum"  value="" class="pageToNum">&nbsp;页
							<input type="button" id="submitButton4Page" class="formButton" value="确定" onclick="pageSelect();" hidefocus=""> 
						<script type="text/javascript">
							 jQuery(document).ready(function(){   
							  $('.vvssso').on('click',function(){
							  var page= $(this).attr('param');			
						      $("input[name='page']").remove();		
							  var hidden="<input type='hidden'  name='startPage' value='"+page+"'/><input type='hidden'  name='page' value='"+page+"'/>"; 
							  $("#pagediv input[type='hidden']").remove();
							  $("#pagediv").append(hidden);
							    var submithref="${formId}";
							    document.getElementById(submithref).submit();	
							 });							    
							});
							function _jumpTo(page) {
								var count=${pagination.total};	
								if (/^\d+$/.test(page)) {
									if(page>count ){
										return count;
									}else if(page<=0){
										return 1;
									} else{
									return page;
									}
								}else{
									return 1;
								}	
						    }; 					  
						
							function pageSelect()	{
								  var els=$("#sizeaaa").val();
								  var page=$("#pageToNum").val();
								  var pagem= _jumpTo(page);		
						          $("input[name='page']").remove();		
							      var hidden="<input type='hidden'  name='page' value='"+pagem+"'/>"; 
							      $("#pagediv").append(hidden);
							      var submithref="${formId}";
							      document.getElementById(submithref).submit();
						    };
							function sizeChange(){
								  var els=$("#sizeaaa").val();
						          $("input[name='page']").remove();		
							      var hidden="<input type='hidden'  name='page' value='1'/>"; 
							      $("#pagediv").append(hidden);
							      var submithref="${formId}";
							      document.getElementById(submithref).submit();
						   };
						</script>
					</li>
				</#if>
			</ul>
		</#if>
	</#if>
</#macro>

<#--
	输出分页链接。如果当前页超过 显示的页数链接数 的一半，则当前页居中显示。例如：当前第10页，总共20页，那么显示第6~第15页分页链接，且第10页居中。
	pagination：分页对象。
	url：链接地址
	showPageLinkCount:显示的页数链接数
	isShowMoreLI:是否显示“<li>...</li>”提示更多还有更多页数0
-->
<#macro outPutPageNo pagination url formId showPageLinkCount isShowMoreLI >
	<#if (showPageLinkCount%2==1)>
		<#assign spaceOFStartIndexToPageNo=((showPageLinkCount+1)/2)-1>
	<#else>
		<#assign spaceOFStartIndexToPageNo=(showPageLinkCount/2)-1>
	</#if>
	
	<#assign spaceOFStartIndexToPageLinkCount=(showPageLinkCount-1)>
	
	<#if ((pagination.page-spaceOFStartIndexToPageNo) <= 1) || (pagination.total<=showPageLinkCount)>
		<#assign startIndex=1>
		<#assign isNeedStartMore=false>
	<#else>
		<#assign startIndex=(pagination.page-spaceOFStartIndexToPageNo)>
		<#assign isNeedStartMore=true>
	</#if>
	
	<#if ((startIndex+spaceOFStartIndexToPageLinkCount) < pagination.total)>
		<#assign endIndex=startIndex+spaceOFStartIndexToPageLinkCount>
		<#assign isNeedEndMore=true>
	<#else>
		<#assign endIndex=(pagination.total)>
		<#assign isNeedEndMore=false>
	</#if>

	<#if isNeedStartMore&&isShowMoreLI><li>...</li></#if>
	<#list startIndex..endIndex as i>
		<#if pagination.page != i>
			<li>
				<a class="vvssso" href="javascript:void(0);" param="${i}">${i}</a>
			</li>
		<#else>
			<li class="currentPage">
				<span>${i}</span>
			</li>
		</#if>
	</#list>
	<#if isNeedEndMore&&isShowMoreLI><li>...</li></#if>
	
	<style type="text/css" mce_bogus="1">
	.pager{float: right; clear: both; margin-top: 5px;}
	.pager li{line-height: 18px; display: block; float: left; padding: 0px 5px; margin: 0px 3px; font-size: 12px; border: 1px solid #cccccc;height:20px;}
	.pager li:hover{color: blue; border: 1px solid blue;}
	.pager li:hover a{color: blue;}
	.pager li a{color: #464646;}
	.pager li span{color: #cfcfcf;}
	.pager li.currentPage{border: 1px solid blue; background-color: #d3dbde;}
	.pager li.currentPage span{font-weight: bold; color: #000000;}
	.pager li.pageInfo{color: #464646; border: none; background: none;}
	.pager li.pageTo{height:20px; color: #464646; border: none; background: none;}
	.pager li.pageTo input{line-height: 20px;}
	.pager li.pageTo input.pageToNum{width: 20px; height: 18px; margin-top: 0px; border: 1px solid #cccccc; display:table-cell; vertical-align:top;}
	.pager li.pageTo input.formButton {width: 40px; background: #F3F3F3;  height: 20px;}
	.pager li.pageTo input.formButton:hover {background: #FFFFFF; color: blue;border: 1px solid blue;}
	</style>
</#macro>