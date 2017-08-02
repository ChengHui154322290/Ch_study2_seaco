package com.tp.shop.convert;

import java.util.Comparator;
import java.util.function.Consumer;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.mmp.PointConstant;
import com.tp.m.base.Page;
import com.tp.m.vo.point.PointDetailVO;
import com.tp.model.mmp.PointDetail;
import com.tp.util.DateUtil;

/**
 * 积分转换器
 * @author szy
 *
 */
public final class PointConvert {

	public static Page<PointDetailVO> convertPointDetailPage(PageInfo<PointDetail> data) {
		final Page<PointDetailVO> page = new Page<PointDetailVO>();
		page.setCurpage(data.getPage());
		page.setPagesize(data.getSize());
		page.setTotalpagecount(data.getTotal());
		page.setTotalcount(data.getRecords());
		data.getRows().sort(new Comparator<PointDetail>(){
			public int compare(PointDetail o1, PointDetail o2) {
				return o2.getCreateTime().compareTo(o1.getCreateTime());
			}
		});
		data.getRows().forEach(new Consumer<PointDetail>(){
			public void accept(PointDetail t) {
				PointDetailVO pointDetailVO = new PointDetailVO();
				pointDetailVO.setTitle(t.getTitle());
				pointDetailVO.setBizId(t.getBizId());
				pointDetailVO.setBizTypeName(PointConstant.BIZ_TYPE.getCnName(t.getBizType()));
				pointDetailVO.setPoint(Integer.toString(t.getPoint()));
				pointDetailVO.setPointTypeName(t.getPointTypeName());
				pointDetailVO.setCreateTime(DateUtil.format(t.getCreateTime(), DateUtil.NEW_FORMAT));
				page.getList().add(pointDetailVO);
			}
		});
		return page;
	}

}
