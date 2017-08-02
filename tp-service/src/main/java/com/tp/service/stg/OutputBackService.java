package com.tp.service.stg;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.dao.stg.OutputBackDao;
import com.tp.dao.stg.OutputBackSkuDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.stg.OutputBackDetailDto;
import com.tp.dto.stg.OutputBackDto;
import com.tp.model.stg.OutputBack;
import com.tp.model.stg.OutputBackSku;
import com.tp.service.BaseService;
import com.tp.service.stg.IOutputBackService;

@Service
public class OutputBackService extends BaseService<OutputBack> implements IOutputBackService {
	private Logger logger = LoggerFactory.getLogger(OutputBackService.class);
	
	@Autowired
	private OutputBackDao outputBackDao;
	
	@Autowired
	private OutputBackSkuDao outputBackSkuDAO;
	
	@Override
	public String selectShipNoByOrderCode(String orderCode) {
		Map<String, Object> params = new HashMap<>();
		params.put("orderNo", orderCode);
		try {
			List<OutputBack> backDOs = outputBackDao.queryByParamNotEmpty(params);
			if(CollectionUtils.isNotEmpty(backDOs)){
				return backDOs.get(0).getShipNo();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public OutputBack selectOutputBackInfoByOrderCode(String orderCode) {
		Map<String, Object> params = new HashMap<>();
		params.put("orderNo", orderCode);
		try {
			List<OutputBack> backDOs = outputBackDao.queryByParamNotEmpty(params);
			if(CollectionUtils.isNotEmpty(backDOs)){
				return backDOs.get(0);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ResultInfo<String> addOutputBackInfo(OutputBackDto back) throws Exception{
		if(null==back){
			return new ResultInfo<String>(new FailInfo("backDO 为空"));
		}
		//验证参数
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<OutputBackDto>> violations = validator.validate(back); 
		if(CollectionUtils.isNotEmpty(violations)){
			for (ConstraintViolation<OutputBackDto> constraintViolation : violations) {
				return new ResultInfo<String>(new FailInfo(constraintViolation.getPropertyPath()+":"+constraintViolation.getMessage()));
			}
		}
		
		Date now = new Date();
		OutputBack outputBackObj = new OutputBack();
		outputBackObj.setBgNo(back.getBgNo());
		outputBackObj.setCarrierId(back.getCarrierId());
		outputBackObj.setCarrierName(back.getCarrierName());
		outputBackObj.setCreateTime(now);
		outputBackObj.setCustomerId(back.getCustomerId());
		outputBackObj.setOrderNo(back.getOrderNo());
		outputBackObj.setShipNo(back.getShipNo());
		outputBackObj.setShipTime(back.getShipTime());
		outputBackObj.setVarehouseId(back.getVarehouseId());
		outputBackObj.setWeight(back.getWeight());
		try {
			outputBackDao.insert(outputBackObj);
			List<OutputBackDetailDto> details = back.getDetails();
			OutputBackSku outputBackSkuObj = null;
			List<OutputBackSku> backSkuObjs = new ArrayList<OutputBackSku>();
			//发运明细
			for (OutputBackDetailDto detailDto : details) {
				outputBackSkuObj = new OutputBackSku();
				outputBackSkuObj.setCreateTime(now);
				outputBackSkuObj.setLotatt01(detailDto.getLotatt01());
				outputBackSkuObj.setLotatt02(detailDto.getLotatt02());
				outputBackSkuObj.setLotatt03(detailDto.getLotatt03());
				outputBackSkuObj.setNum(detailDto.getNum());
				outputBackSkuObj.setOutputBackId(outputBackObj.getId());
				outputBackSkuObj.setSku(detailDto.getSku());
				backSkuObjs.add(outputBackSkuObj);
			}
			outputBackSkuDAO.insertBatch(backSkuObjs);
			return new ResultInfo<String>("添加成功");
		} catch (Exception e) {
			logger.error(e.toString(), e);
			throw e;
		}		
	}
	
	@Override
	public BaseDao<OutputBack> getDao() {
		return outputBackDao;
	}
}
