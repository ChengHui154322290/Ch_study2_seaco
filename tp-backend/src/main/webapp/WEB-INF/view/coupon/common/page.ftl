<#macro p page totalpage params='' maxsteps=6>  
<div class="page mt10">
  <div class="pagination" style="text-align:right; padding-top:2px;border:1px,solid,red">
    <ul>
    <#assign ipage=page?number>  
    <#if maxsteps <= 0>  
        <#assign maxsteps=5>  
    </#if>  
    <#assign offset = ((ipage - 1) / maxsteps)?int>  
    <#assign offsetLast = ((totalpage - 1) / maxsteps)?int>  
       
    <#-- 首页 -->  
    <#if ipage gt 1>  
        <li class="first-child"><a href="javascript:void(0)" onclick="_gotoPage(1)">首页 </a></li>
    <#else>  
        <li class="disabled"><span>首页 </span></li>
    </#if>  
    <#-- 前组-->  
    <#if offset gt 0>  
        <li class="active"><a href="javascript:void(0)" onclick="_gotoPage(${offset * maxsteps})">…</a></li>
    <#else>  
        <!--
        <span class="disabled">…</span>  
        -->
    </#if>  
    <#-- 当前组中的页号-->  
    <#if (offset + 1) * maxsteps < totalpage>  
        <#assign pagelist = (offset + 1) * maxsteps>  
    <#else>  
        <#assign pagelist = totalpage>  
    </#if>  
    <#if ipage gt 0 && ipage lte totalpage>  
        <#list (offset * maxsteps + 1)..pagelist as num>  
            <#if ipage != num>  
                <li class="active"><a href="javascript:void(0)" onclick="_gotoPage(${num})">${num}</a></li>
            <#else>  
                <li class="active"><span>${num}</span></li>
            </#if>  
        </#list>  
    </#if>  
    <#-- 下组 -->  
    <#if offset lt offsetLast>  
        <li class="active"><a href="javascript:void(0)" onclick="_gotoPage(${(offset + 1) * maxsteps + 1})">…</a></li>
    <#else>  
        <!--
        <span class="disabled">…</span>  
        -->
    </#if>  
    <#-- 尾页 -->  
    <#if ipage lt totalpage>  
        <li class="last-child"><a href="javascript:void(0)" onclick="_gotoPage(${totalpage})">末页</a></li> 
    <#else>  
       <!--
        <span class="disabled">&gt;&gt;</span>  
        -->
    </#if>  
    <#-- 前一页 -->  
    <#if ipage gt 1>  
        <li><a href="javascript:void(0)" onclick="_gotoPage(${ipage - 1})">前一页</a></li>  
    <#else>  
        <!--
        <span class="disabled">前一页</span>  
        -->
    </#if>  
    <#-- 后一页 -->  
    <#if ipage lt totalpage>  
        <li><a href="javascript:void(0)" onclick="_gotoPage(${ipage + 1})">后一页</a> </li>
    <#else>  
        <!--
        <span class="disabled">后一页</span>  
        -->
    </#if> 
    </ul>
  </div>
</div> 
</#macro>  