<#assign domain="${backendDomain}" />
<#assign version="201412241635" />
<#include "/common/macros/context.ftl" />
<#include "/common/pageInfo.ftl" />
<#include "/common/domain.ftl"/> 
<#assign base=domain>

<#-- 静态资源访问路径 -->
<#assign statics = base + "/statics">

<#assign backend_folder=statics+"/backend" />
<#-- js服务器访问路径 -->
<#assign js = statics + "/js">


<#assign kaifa = statics + "/kaifa">

<#-- css服务器访问路径 -->
<#assign css = statics + "/css">
<#-- 表单验证框架服务器访问路径 -->
<#assign validation = js + "/validation">
<#assign messenger = js + "/messenger">

<#assign tree = js + "/tree">

<#assign contextMenu = js + "/contextMenu">


<#-- 图片服务器访问路径 -->
<#assign img = statics + "/img">

<#-- 版本号 -->
<#assign version= .now?string("yyyyMMddHHmm")>
<#assign logo="西客后台管理系统">

