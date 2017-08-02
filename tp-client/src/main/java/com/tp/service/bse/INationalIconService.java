package com.tp.service.bse;

import java.util.List;

import com.tp.model.bse.DistrictInfo;
import com.tp.model.bse.NationalIcon;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 国家图标表接口
  */
public interface INationalIconService extends IBaseService<NationalIcon>{
	
	List<NationalIcon> selectListByCountryIds( List<Long> ids );
}
