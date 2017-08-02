<#list optionsSel as optionSel>
   <#if defaultVal?exists && defaultVal == optionSel.id>
   <option value="${optionSel.id}" selected="selected">${optionSel.name}</option>
   <#else>
   <option value="${optionSel.id}">${optionSel.name}</option>
   </#if>
</#list>