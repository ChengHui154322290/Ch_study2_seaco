package com.tp.service.sup;

import java.util.List;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.sup.AuditRecords;
import com.tp.model.sup.QuotationInfo;
import com.tp.model.sup.QuotationProduct;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 报价单主表接口
  */
public interface IQuotationInfoService extends IBaseService<QuotationInfo>{

    /**
     * <pre>
     * 报价单审核
     * </pre>
     *
     * @param quotationDO
     * @param setStatus
     * @param record
     * @return
     * @throws Exception
     */
	public ResultInfo<Boolean> auditQuotation(QuotationInfo quotationInfo, Integer auditStatus, AuditRecords record);

    /**
     * <pre>
     * 保存报价单
     * </pre>
     *
     * @param quotationDTO
     * @return @throws DAOException
     */
	ResultInfo<QuotationInfo> saveQuotationInfo(QuotationInfo quotationInfo);

	
    /**
     * <pre>
     * 往同一报价单里新增products
     * </pre>
     *
     * @param quotationDTO
     * @return @throws DAOException
     */
	
	ResultInfo<QuotationInfo> addQuotationProducts(QuotationInfo quotationInfo,  List<QuotationProduct> newprdlist);

    /**
     * <pre>
     *   更新报价单的信息
     *   当auditStatus存在的时候  要校验报价单之前的状态
     * </pre>
     *
     * @param quotationDTO
     * @param auditStatus
     * @return @throws DAOException
     */
	ResultInfo<Boolean> updateQuotationInfo(QuotationInfo quotationInfo, Integer auditStatus);
}
