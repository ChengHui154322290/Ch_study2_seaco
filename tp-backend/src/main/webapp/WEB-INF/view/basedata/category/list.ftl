<#include "/common/common.ftl"/>
<@backend title="" js=['/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/basedata/category.js',
	'/statics/backend/js/jqgrid/js/jquery.jqGrid.min.js',
	'/statics/backend/js/jqgrid/js/i18n/grid.locale-cn.js']
	css=['/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
	'/statics/backend/js/jqgrid/css/ui.jqgrid.css'] >
	
	<div id="search_bar" class="mt10">
       <div class="box">
       <form class="jqtransform" method="post" id="queryForm" action="${domain}/basedata/attribute/list.htm">
          <div class="box_border">
            <div class="box_top"><b class="pl15">搜索</b></div>
            <div class="box_center pt10 pb10">
              <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>  
                 <td>类别:</td>
                <td>
                <span class="fl">
                  <div class="select_border">
                    <div class="select_containers ">
                    <select name="level" id='level' class="select">  
                    <option value="0">大类</option>
                    <option value="1">中类</option>
                    <option value="2">小类</option>
                    </select>
                    </div>
                  </div> 
                </span>
                 </td>
                  <td>类别名称:</td>
                  <td><input type="text" name="name"  id='name' class="input-text lh25" size="30" /></td>     
                  <td>类别编号:</td>
                  <td><input type="text" name="code"  id='code' class="input-text lh25" size="30" /></td>
                </tr>
              </table>
            </div>
           
           <span id="nameInfo" class="error"></span>
           
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:left;">
                <input class="btn btn82 btn_search" id="searthAtt" type="button" value="查询" name="button"/>
                <input class="btn btn82 btn_res" type="button" value="重置" name="button" onclick="dataReset(this)" />
                <input class="btn btn82 btn_add categoryAdd" type="button" value="新增" name="addCatBtn"/>
              </div>
            </div>
            </div>
          </form>
          
        </div>
    </div>
    
         <table id="tree"></table>
          
</@backend>