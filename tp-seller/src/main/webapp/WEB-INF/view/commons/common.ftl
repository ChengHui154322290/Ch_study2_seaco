<#assign domain="${rc.contextPath}" />
<#--这个是从配置问价加载出来的外部静态文件的地址 现在还没有使用 -->
<#assign staticDomain="${staticDomain}" />
<#--本地静态文件的引用地址 -->
<#assign staticPath="${domain}" />
<#--本地静态文件的引用时 加一个版本可以防止页面静态缓存 -->
<#assign version="201412241636" />
<#assign webuploadbase=domain>
<#assign webuploadstatic = webuploadbase + "/static">
<#assign webuploadjs = webuploadstatic + "/workorder">
<script>
var domain = "${domain}";
var staticPath = "${staticPath}";
var webUploader = domain+'/static/workorder/webUploader';
var workorder = '${webuploadbase}';
</script>