<#macro sellMain title="电商后台管理系统" 
js=[] 
css=[] 
>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>商家系统 </title>
    <#include "/commons/js_and_css.ftl" />
    <#list css as file>   
    <#if file?lower_case?starts_with('http://')>
    <link rel="stylesheet" href="${file}?v=${version}" />
    <#else>
    <link rel="stylesheet" href="${staticPath}${file}?v=${version}" />	
    </#if>		
    </#list>
    <#list js as file>
    <#if file?lower_case?starts_with('http://')>   		
    <script type="text/javascript" src="${file}?v=${version}"></script>
    <#else>
    <script type="text/javascript" src="${staticPath}${file}?v=${version}"></script>
    </#if>
    </#list>
</head>
<body>
    <div class="navbar navbar-default" id="navbar">
        <script type="text/javascript">
        try{ace.settings.check('navbar' , 'fixed')}catch(e){}
        </script>
        <div class="navbar-container" id="navbar-container">
            <#include "/commons/top.ftl" />
        </div>
    </div>

    <div class="main-container" id="main-container">
        <script type="text/javascript">
        	try{ace.settings.check('main-container' , 'fixed')}catch(e){}
        </script>

        <div class="main-container-inner">
            <a class="menu-toggler" id="menu-toggler" href="#">
                <span class="menu-text"></span>
            </a>

            <div class="sidebar" id="sidebar">
                <script type="text/javascript">
                	try{ace.settings.check('sidebar' , 'fixed')}catch(e){}
                </script>
                <#include "/commons/left_menu.ftl" />
                <div class="sidebar-collapse" id="sidebar-collapse">
                    <i class="icon-double-angle-left" data-icon1="icon-double-angle-left" data-icon2="icon-double-angle-right"></i>
                </div>
                <script type="text/javascript">
                	try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
                </script>
            </div>

            <div class="main-content">
                <div class="page-content">
                    <div class="row">
                        <div class="col-xs-12">
                            <!-- PAGE CONTENT BEGINS -->
                            <div >
                                <ul class="nav nav-tabs tab-color-blue background-blue" role="tablist">
                                    <li role="presentation" class="active">
                                        <a href="#index" role="tab" onclick="refreshIndex()" data-toggle="tab">首页</a>
                                    </li>
                                </ul>
                                <div id="content" class="tab-content"><#nested/></div>
                            </div>
                            <!-- PAGE CONTENT ENDS -->
                        </div><!-- /.col -->
                    </div><!-- /.row -->
                </div><!-- /.page-content -->
            </div><!-- /.main-content -->
        </div><!-- /.main-container-inner -->

        <a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
            <i class="icon-double-angle-up icon-only bigger-110"></i>
        </a>
    </div>
    <#include "/commons/pop_page.ftl" />
    <#include "/commons/message_info.ftl" />
    <input type="hidden" id="fileUploadIndex" value="0" />
</body>
</html>
</#macro>