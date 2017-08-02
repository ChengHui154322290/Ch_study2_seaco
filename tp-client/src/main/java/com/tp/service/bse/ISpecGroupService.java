package com.tp.service.bse;

import java.util.List;

import com.tp.model.bse.SpecGroup;
import com.tp.result.bse.SpecGroupResult;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 规格组表接口
  */
public interface ISpecGroupService extends IBaseService<SpecGroup>{
	/**
     * 
     * <pre>
     *  根据尺码组ids返回规格组信息,根据status的值进行过滤,0获取状态为无效的,1获取所有有效的,2获取全部数据
     * </pre>
     *
     * @param ids: 尺码组id主键的list数组
     * @param status: 0-无效，1-有效，2-全部
     * @return
     */
    List<SpecGroup> selectListSpecGroup(List<Long> ids,Integer status);
    
    /**
     * 
     * @param group
     * @param ids
     */
	void addSpecAndSpecGroupLinkMethod(SpecGroup group, Long[] ids);

	void updateSpecAndSpecGroupLinkMethod(SpecGroup group, Long[] ids);

	List<SpecGroupResult> selectListSpecGroupResult(List<Long> groupIds,Integer itemGroupStatusAll);

	SpecGroupResult selectSpecGroupResultById(Long id, Integer status);

	List<SpecGroupResult> getSpecGroupResultByCategoryId(Long categoryId);

	List<SpecGroupResult> getAllSpecGroupResult();

}
