package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.supplier.entry.ContractTemplate;
import com.tp.dao.sup.SequenceCodeDao;
import com.tp.model.sup.SequenceCode;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.BaseService;
import com.tp.service.sup.ISequenceCodeService;

@Service
public class SequenceCodeService extends BaseService<SequenceCode> implements ISequenceCodeService {
	static {
        CONTRACT_TEMPLATE_MAP.put(ContractTemplate.PLDF.getKey(), CONTRACT_TEMPLATE_PLDF);
        CONTRACT_TEMPLATE_MAP.put(ContractTemplate.PLRC.getKey(), CONTRACT_TEMPLATE_PLRC);
        CONTRACT_TEMPLATE_MAP.put(ContractTemplate.HTZY.getKey(), CONTRACT_TEMPLATE_HTZY);
        CONTRACT_TEMPLATE_MAP.put(ContractTemplate.HTBS.getKey(), CONTRACT_TEMPLATE_HTBS);
    }
	@Autowired
	private SequenceCodeDao sequenceCodeDao;
	@Autowired
	private JedisCacheUtil JedisCacheUtil;
	@Override
	public BaseDao<SequenceCode> getDao() {
		return sequenceCodeDao;
	}
    @Override
    public Long currentCode(final String templateType) {
    	return sequenceCodeDao.getCurrentCode(templateType);
    }

    @Override
    public Long nextCode(final String templateType) {
    	 final int succ = sequenceCodeDao.codeAdd(templateType);
         if (succ > 0) {
             return sequenceCodeDao.getCurrentCode(templateType);
         } else {
             return null;
         }
    }
}
