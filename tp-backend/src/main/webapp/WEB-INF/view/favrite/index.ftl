 <#include "/common/common.ftl"/>
 <!doctype html>
 <html lang="zh-CN">
 <head>
   <meta charset="UTF-8">
   <link rel="stylesheet" href="${domain}/statics/backend/css/common.css">
   <link rel="stylesheet" href="${domain}/statics/backend/css/main.css">
   <script type="text/javascript" src="${domain}/statics/backend/js/jquery.min.js"></script>
   <script type="text/javascript" src="${domain}/statics/backend/js/common.js"></script>	
   <script type="text/javascript" src="${domain}/statics/js/favrite/favrite.js"></script>
   <script>var domain = "${domain}";</script>
   <title>常用功能列表</title>
 </head>
 <body>
  <div class="container">
     <div class="main_top">
          <div class="main_left fl span9">
              <div class="box pr5">
                <div class="box_border">
                  <div class="box_top">
                    <div class="box_top_l fl"><b class="pl15">我的常用功能</b></div>
                  </div>
                  <div class="box_center">
                  		<div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                   <th width="30">序号</th>
                   <th width="600">标题</th>
                   <th>操作</th>
                    </tr>
                    <#if menuDOs??&&menuDOs?size gt 0>
                <#list menuDOs as menu>
                <tr class="tr">
                   <td class="td_center">${menu_index+1}</td>
                   <td><a href="javascript:void(0)" onclick="openNewOperTab('${menu.id}','${menu.name}','${menu.url}')" class="favriteMenu"> ${menu.name} </a></td>
                   <td><a href="javascript:void(0)" onclick="Fav.removeFavrite('${menu.id}',function(){window.location.reload();})">删除</a></td>
                 </tr>
                </#list>
                <#else>
                <tr>
                   <td colspan="3" class="tc">暂无</td>
                </tr>
                </#if>
              </table>
	        </div>
    	 </div>
                  </div>
                </div>
              </div>
          </div>
          <div class="clear"></div>
     </div>
   </div> 
 </body>
 </html>
  