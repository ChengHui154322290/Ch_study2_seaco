package com.tp.m.convert;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.ord.RejectConstant;
import com.tp.m.base.Page;
import com.tp.m.enums.AfterSalesEnum;
import com.tp.m.enums.ImgEnum;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.helper.ImgHelper;
import com.tp.m.query.order.QueryAfterSales;
import com.tp.m.util.NumberUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.order.AfterSalesVO;
import com.tp.model.mem.MemberInfo;
import com.tp.model.ord.RejectInfo;
import com.tp.model.ord.RejectItem;
import com.tp.util.DateUtil;

/**
 * 售后封装类
 * @author zhuss
 * @2016年2月26日 下午4:27:03
 */
public class AfterSalesConvert {
	
	/**
	 * 封装申请售后入参
	 */
	public static void convertApple(MemberInfo user,RejectInfo rejectInfo,RejectItem rejectItem,QueryAfterSales afterSales){
		user.setNickName(afterSales.getLoginname());
		user.setId(afterSales.getUserid());
		
		rejectInfo.setRejectType(RejectConstant.REJECT_TYPE.SEND_BACK.code);
		rejectInfo.setOrderCode(StringUtil.getLongByStr(afterSales.getOrdercode()));
		rejectInfo.setRejectReason(afterSales.getReturnreason());// 退货原因
		rejectInfo.setBuyerRemarks(afterSales.getReturninfo());// 退货说明
		rejectInfo.setLinkMan(afterSales.getLinkname());
		rejectInfo.setLinkMobile(afterSales.getLinktel());
		
		rejectItem.setItemRefundQuantity(StringUtil.getIntegerByStr(afterSales.getReturncount()));
		rejectItem.setOrderItemId(StringUtil.getLongByStr(afterSales.getLineid()));// 订单商品行ID
		conventBuyerImgUrl(afterSales, rejectInfo);// 用户凭证图片JSON格式用逗号分隔
		
		
	}
	
	/**
	 * 封装列表详情
	 * @param p
	 * @return
	 */
	public static Page<AfterSalesVO> convertList2Detail(PageInfo<RejectInfo> p){
		Page<AfterSalesVO> pages = new Page<AfterSalesVO>();
		if(null != p){
			List<AfterSalesVO> list = new ArrayList<>();
			List<RejectInfo> rows = p.getRows();
			if(CollectionUtils.isNotEmpty(rows)){
				for(RejectInfo pejectInfo :rows){
					AfterSalesVO vo = new AfterSalesVO();
					AfterSalesEnum.AFTERSALES_STATUS status = getStatus(pejectInfo.getRejectStatus(), pejectInfo.getAuditStatus());
					vo.setAsid(StringUtil.getStrByObj(pejectInfo.getRejectId()));
					vo.setAscode(StringUtil.getStrByObj(pejectInfo.getRejectCode()));
					vo.setApplydate(DateUtil.formatDateTime(pejectInfo.getCreateTime()));
					vo.setReturnimg(ImgHelper.splitJsonImg2DH(pejectInfo.getBuyerImgUrl()));
					vo.setReturninfo(pejectInfo.getBuyerRemarks());
					vo.setReturnreason(pejectInfo.getRejectReason());
					vo.setReturnreasondesc(RejectConstant.REJECT_REASON.getCnName(pejectInfo.getRejectReason()));
					vo.setReturnprice(StringUtil.getStrByObj(pejectInfo.getRefundAmount()));
					vo.setReturnaddress(pejectInfo.getReturnAddress());
					vo.setHistorycount(StringUtil.getStrByObj(pejectInfo.getCustServCount()));
					vo.setLineprice(NumberUtil.sfwr(pejectInfo.getSubTotal()));
					vo.setStatus(status.code);
					vo.setStatusdesc(status.desc);
					vo.setSellerinfo(pejectInfo.getSellerRemarks());
					vo.setKfinfo(pejectInfo.getRemarks());
					vo.setOrdercode(StringUtil.getStrByObj(pejectInfo.getOrderCode()));
					vo.setLogisticcode(pejectInfo.getExpressNo());
					vo.setCompany(pejectInfo.getCompanyName());
					vo.setCompanycode(pejectInfo.getCompanyCode());
					vo.setLinkname(pejectInfo.getReturnContact());
					vo.setLinktel(pejectInfo.getReturnMobile());
					List<RejectItem> rejectItemList = pejectInfo.getRejectItemList();
					if(CollectionUtils.isEmpty(rejectItemList)) throw new MobileException(MResultInfo.RETURN_ERROR_NO_ITEM);
					RejectItem rejectItem = rejectItemList.get(0);
					vo.setReturncount(StringUtil.getStrByObj(rejectItem.getItemRefundQuantity()));
					vo.setLineid(StringUtil.getStrByObj(rejectItem.getOrderItemId()));
					vo.setItemimg(ImgHelper.getImgUrl(rejectItem.getItemImgUrl(), ImgEnum.Width.WIDTH_150));
					vo.setItemname(rejectItem.getItemName());
					vo.setItemprice(StringUtil.getStrByObj(rejectItem.getItemUnitPrice()));
					vo.setBuycount(StringUtil.getStrByObj(rejectItem.getItemQuantity()));
					list.add(vo);
				}
				pages.setFieldTCount(list, p.getPage(), p.getRecords());
			}
			pages.setCurpage(p.getPage());
		}
		return pages;
	}
	

	/**
	 * 封装用户凭证:JSON格式用逗号分隔
	 */
	private static void conventBuyerImgUrl(QueryAfterSales afterSales,RejectInfo rejectInfo){
		StringBuffer imageStr = new StringBuffer(1024);
		if(StringUtil.isNotBlank(afterSales.getImageone()))imageStr.append(afterSales.getImageone()).append(",");
		if(StringUtil.isNotBlank(afterSales.getImagetwo()) )imageStr.append(afterSales.getImagetwo()).append(",");
		if(StringUtil.isNotBlank(afterSales.getImagethree()))imageStr.append(afterSales.getImagethree()).append(",");
		if(StringUtil.isNotBlank(afterSales.getImagefour()) )imageStr.append(afterSales.getImagefour()).append(",");
		if(StringUtil.isNotBlank(afterSales.getImagefive()))imageStr.append(afterSales.getImagefive()).append(",");
		if(StringUtil.isNotBlank(imageStr.toString()))rejectInfo.setBuyerImgUrl(imageStr.toString().substring(0, imageStr.length() - 1));
	}
	
	/**
	 * 根据退货单状态和审核状态封装成状态
	 * @param rejectStatus:退货单状态：0申请退货1退货中2退款中3退货完成4取消退货5退货失败6待确认退货
	 * @param auditStatus:审核状态：1待客服审核2待商家审核3客服审核通过4客服审核不通过5商家审核通过6商家审核不通过7客服终审通过8客服终审不通过9强制审核通过
	 * @return 1客服审核中2审核不通过3审核通过4取消退货5退货中6退款中7退货完成8退货失败
	 */
	public static AfterSalesEnum.AFTERSALES_STATUS getStatus(Integer rejectStatus,Integer auditStatus ){
		if(null != rejectStatus){
			switch(rejectStatus){
				case 0: return AfterSalesEnum.AFTERSALES_STATUS.WAIT_AUDIT;
				case 1: return AfterSalesEnum.AFTERSALES_STATUS.RETURN;
				case 2: return AfterSalesEnum.AFTERSALES_STATUS.REFUND;
				case 3: return AfterSalesEnum.AFTERSALES_STATUS.REFUND_FINISH;
				case 4: return AfterSalesEnum.AFTERSALES_STATUS.CANCLE;
				case 5: return AfterSalesEnum.AFTERSALES_STATUS.REFUND_FAIL;
				case 6: return AfterSalesEnum.AFTERSALES_STATUS.AUDIT_PASS;//等待用户填写物流信息
			}
		}else if(null != auditStatus && auditStatus != 0){
			switch(auditStatus){
				case 1: return AfterSalesEnum.AFTERSALES_STATUS.WAIT_AUDIT;
				case 2: return AfterSalesEnum.AFTERSALES_STATUS.WAIT_AUDIT;
				case 3: return AfterSalesEnum.AFTERSALES_STATUS.AUDIT_PASS;
				case 4: return AfterSalesEnum.AFTERSALES_STATUS.AUDIT_FAIL;
				case 5: return AfterSalesEnum.AFTERSALES_STATUS.AUDIT_PASS;
				case 6: return AfterSalesEnum.AFTERSALES_STATUS.AUDIT_FAIL;
				case 7: return AfterSalesEnum.AFTERSALES_STATUS.REFUND;
				case 8: return AfterSalesEnum.AFTERSALES_STATUS.REFUND_FAIL;
				case 9: return AfterSalesEnum.AFTERSALES_STATUS.REFUND;
			}
		}
		return AfterSalesEnum.AFTERSALES_STATUS.ERROR;
	}
}
