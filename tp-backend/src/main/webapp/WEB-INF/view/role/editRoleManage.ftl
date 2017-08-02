<#include "/common/common.ftl"/> 
<div class="mt10" id="forms">
        <div class="box">
        	<label style="display:none" class="error"></label>
          <div class="box_border">
            <div class="box_center">
             <form id="saveRoleForm" class="editRole" autocomplete="off">
               <table width="100%" cellspacing="0" cellpadding="0" border="0" class="form_table pt15 pb15">
                 <tbody><tr>
                  <td class="td_right">角色名：</td>
                  <td class=""> 
                   <input type="hidden" id="id" name="id" value="${role.id}"/>
                    <input type="text" size="40" class="input-text lh30" id="name" name="name" value="${role.name}">
                  </td>
                </tr>
                 <tr>
                  <td class="td_right">角色描述：</td>
                  <td class="">
                    <textarea class="textarea" rows="10" cols="30" id="roleDesc" name="roleDesc">${role.roleDesc}</textarea>
                  </td>
                 </tr>
               </tbody>
              </table>
              	<input type="hidden" id="sysMenuIds" name="sysMenuIds" value="${sysMenuIds!}"/>
				<input type="hidden" id="sysMenuLimitIds" name="sysMenuLimitIds" value="${sysMenuLimitIds!}"/>
				<input type="hidden" id="roleMenuLimit" name="roleMenuLimit" value="${role.roleMenuLimit!}"/>
              </form>
            </div>
          </div>
        </div>
     </div>

