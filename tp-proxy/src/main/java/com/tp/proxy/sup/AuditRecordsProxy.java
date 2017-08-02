package com.tp.proxy.sup;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.supplier.SupplierConstant;
import com.tp.common.vo.supplier.entry.BillType;
import com.tp.model.sup.AuditRecords;
import com.tp.model.sup.QuotationInfo;
import com.tp.proxy.BaseProxy;
import com.tp.result.sup.ContractDTO;
import com.tp.result.sup.SupplierDTO;
import com.tp.service.IBaseService;
import com.tp.service.sup.IAuditRecordsService;
/**
 * 审核记录表(采购管理模块中所有单据的审核记录信息)代理层
 * @author szy
 *
 */
@Service
public class AuditRecordsProxy extends BaseProxy<AuditRecords>{

	@Autowired
	private IAuditRecordsService auditRecordsService;

	@Override
	public IBaseService<AuditRecords> getService() {
		return auditRecordsService;
	}
	/**
     * 保存审核记录
     *
     * @param contractDTO
     * @param setStatus
     * @param string
     * @param request
     * @param billtype
     */
	public void saveAuditRecords(ContractDTO contractDTO, Integer auditStatus,String auditContent, BillType billtype,String UserName,Long userId) {
        final AuditRecords record = new AuditRecords();
        record.setAuditId(contractDTO.getId());
        record.setContent(auditContent);
        record.setAuditStatus(auditStatus);
        record.setUserId(userId);
        record.setCreateUser(UserName);
        record.setUpdateUser(UserName);
        record.setUserName(UserName);
        record.setOperate(SupplierConstant.AUDIT_RESULT.get(auditStatus));
        record.setRoleId(0L);
        record.setRoleName("");
        record.setBillType(billtype.getValue());
        record.setStatus(Constant.ENABLED.YES);
        record.setCreateTime(new Date());
        auditRecordsService.insert(record);
	}
	public void saveAuditRecords(QuotationInfo quotationInfo,Integer auditStatus, String auditContent, BillType billtype,Long userId, String userName) {
        final AuditRecords record = new AuditRecords();
        record.setAuditId(quotationInfo.getId());
        record.setContent(auditContent);
        record.setAuditStatus(auditStatus);
        record.setUserId(userId);
        record.setTitle("提交报价单审核");
        if (billtype.getValue().equals(BillType.SELL.getValue())
            || billtype.getValue().equals(BillType.PURCHARSE.getValue())) {
            record.setOperate(SupplierConstant.O_AUDIT_RESULT.get(auditStatus));
        } else if (billtype.getValue().equals(BillType.PURCHARSE_RETURN.getValue())
            || billtype.getValue().equals(BillType.SELL_RETURN.getValue())) {
            record.setOperate(SupplierConstant.REFUND_O_AUDIT_RESULT.get(auditStatus));
        } else if (billtype.getValue().equals(BillType.PRICE.getValue())) {
            record.setOperate(SupplierConstant.O_AUDIT_RESULT.get(auditStatus));
        }
        record.setBillType(billtype.getValue());
        record.setStatus(Constant.ENABLED.YES);
        record.setCreateTime(new Date());
        record.setCreateUser(quotationInfo.getUpdateUser());
        record.setUpdateUser(quotationInfo.getUpdateUser());
        record.setUserName(userName);
        record.setRoleId(0L);
        record.setRoleName("系统");
        auditRecordsService.insert(record);
    }
	public void saveAuditRecords(SupplierDTO supplierDTO, Integer auditStatus,
			String auditContent, BillType billtype, Long userId, String userName) {
        final AuditRecords record = new AuditRecords();
        record.setAuditId(supplierDTO.getId());
        record.setContent(auditContent);
        record.setAuditStatus(auditStatus);
        record.setUserId(userId);
        record.setUserName(userName);
        record.setOperate(SupplierConstant.AUDIT_RESULT.get(auditStatus));
        record.setBillType(billtype.getValue());
        record.setStatus(Constant.ENABLED.YES);
        record.setCreateTime(new Date());
        record.setCreateUser(userName);
        record.setUpdateUser(userName);
        record.setOperate("更新供应商信息");
        record.setTitle("保存供应商信息");
        auditRecordsService.insert(record);
    }
}
