package com.tp.dao.ord;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.ord.OrderDelivery;

public interface OrderDeliveryDao extends BaseDao<OrderDelivery> {

	Integer batchInsert(List<OrderDelivery> orderDeliveryDOList);

	List<OrderDelivery> selectNotSuccessPostKuaidi100List(OrderDelivery orderDelivery);

	Integer batchUpdatePostKuaidi100(List<OrderDelivery> orderDeliveryDOList);

	Integer updatePostKuaidi100(OrderDelivery orderDelivery);

	List<OrderDelivery> selectListBySubCodeAndPackageNo(@Param("orderCode") Long subOrderCode,@Param("packageNo")String packageNo);

}
