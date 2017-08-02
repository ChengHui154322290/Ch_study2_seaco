package com.tp.dao.mmp;

import com.tp.common.dao.BaseDao;
import com.tp.model.mmp.CouponUser;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CouponUserDao extends BaseDao<CouponUser> {

    int updateStatusById(Map<String,Object> param);

    List<CouponUser> queryCouponWithoutMemberIdByMobile(String mobile);

    int updateForCorrelation(@Param("ids") List<Long> ids,@Param("toUserId")Long toUserId);

}
