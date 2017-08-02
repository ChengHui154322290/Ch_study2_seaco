		<div class="box span10 oh">
          <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
            <tr>
           	   <th width="130">订单编号</th>
           	   <th width="130">补偿单号</th>
               <th width="130">补偿原因</th>
               <th width="80">补偿金额</th>
               <th width="100">申请时间</th>
               <th width="80">申请人</th>
               <th width="80">收款人</th>
               <th>收款银行</th>
               <th>银行账号</th>
               <th>承担方</th>
               <th>交易流水号</th>
               <th>支付银行</th>
               <th>支付账号</th>
               <th>审核状态</th>
               <th>操作</th>
            </tr>
            <#if offsetInfoPage.rows?default([])?size !=0>
            <#list offsetInfoPage.rows as offset>
             <tr>
               <td>${offset.orderCode}</td>
               <td>${offset.offsetCode}</td>
               <td>${offset.zhOffsetReason}</td>
               <td>${offset.offsetAmount?string('0.00')}</td>
               <td>${offset.createTime?string('yy-MM-dd HH:mm:ss')}</td>
               <td>${offset.createUser}</td>
               <td>${offset.payee}</td>
               <td>${offset.payeeBank}</td>
               <td>${offset.bankAccount}</td>
               <td>${offset.zhBear}</td>
               <td>${offset.serialNo}</td>
               <td>${offset.payBank}</td>
               <td>${offset.payBankAccount}</td>
			   <td>${offset.zhOffsetStatus}</td>
               <td>
               		<#if (auditType=='audit' && offset.offsetStatus==1) ||(auditType=='finalaudit' && offset.offsetStatus==2)>
               			<a href="javascript:void(0);" class="applyoffsetshowbtn" auditType="${auditType}" operate="audit" offsetid="${offset.offsetId}" offsetno="${offset.offsetCode}">[审核]</a> 
               		</#if>
               		<a href="javascript:void(0);" class="applyoffsetshowbtn" auditType="${auditType}" operate="show" offsetid="${offset.offsetId}" offsetno="${offset.offsetCode}">[查看]</a>
               </td>
              </tr>
            </#list>
            </#if>
          </table>
    	</div>
	   