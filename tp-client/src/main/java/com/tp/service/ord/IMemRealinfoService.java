package com.tp.service.ord;

import java.util.List;

import com.tp.model.ord.MemRealinfo;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 接口
  */
public interface IMemRealinfoService extends IBaseService<MemRealinfo>{
	 MemRealinfo selectOneByOrderCode(Long orderCode);

	List<MemRealinfo> selectListByOrderCodeList(List<Long> orderCodeList);
}
