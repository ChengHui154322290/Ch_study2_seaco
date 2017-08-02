<#include "/common/common.ftl"/>
<@backend title="" js=[
	'/statics/backend/js/layer/layer.min.js',
	'/statics/backend/js/basedata/color.js'] 
	css=[] >
    <div id="search_bar" class="mt10">
       <div class="box">
          <div class="box_border">
            <div class="box_top"><b class="pl15">搜索</b></div>
            <div class="box_center pt10 pb10">
              <table class="form_table" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td>颜色编号</td>
                  <td><input type="text" name="name" class="input-text lh25" size="20"></td>
                  <td>颜色</td>
                  <td><input type="text" name="name" class="input-text lh25" size="20"></td>
                  <td>状态</td>
                  <td>
                    <span class="fl">
                      <div class="select_border"> 
                        <div class="select_containers "> 
                        <select name="" class="select"> 
                        <option>有效</option> 
                        <option>无效</option> 
                        </select> 
                        </div> 
                      </div> 
                    </span>
                  </td>
                </tr>
              </table>
            </div>
            <div class="box_bottom pb5 pt5 pr10" style="border-top:1px solid #dadada;">
              <div class="search_bar_btn" style="text-align:right;">
                 <input class="btn btn82 btn_search" type="button" value="查询" name="button">
                 <input class="btn btn82 btn_add coloreditbtn" type="button" value="新增" name="button">
              </div>
            </div>
          </div>
        </div>
    </div>
       <div id="table" class="mt10">
        <div class="box span10 oh">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
                <tr>
                   <th width="100">颜色编号</th>
                   <th width="100">颜色</th>
                   <th width="300">备注</th>
                   <th width="80"></th>
                   <th width="200"></th>
                </tr>
                <tr class="tr">
                   <td>${categoryDO.id}</td>
                   <td>${categoryDO.name}</td>
                   <td>${categoryDO.code}</td>
                   <td>${categoryDO.status}</td>
                   <td>${categoryDO.level}</td>
                   <td>${categoryDO.parentId}</td>
                   <td>${categoryDO.path}</td>
                   <td>${categoryDO.remark}</td>
                   <td>${categoryDO.createTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                   
                 </tr>
              </table>
              <#--
              <div class="page mt10">
                <div class="pagination">
                  <ul>
                      <li class="first-child"><a href="#">首页</a></li>
                      <li class="disabled"><span>上一页</span></li>
                      <li class="active"><span>1</span></li>
                      <li><a href="#">2</a></li>
                      <li><a href="#">下一页</a></li>
                      <li class="last-child"><a href="#">末页</a></li>
                  </ul>
                </div>
              </div>-->
        </div>
     </div>
</@backend>