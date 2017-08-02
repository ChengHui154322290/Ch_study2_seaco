package com.tp.model.dss;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.DssConstant;
import com.tp.common.vo.MemberConstant;
import com.tp.common.vo.PaymentConstant;
import com.tp.model.BaseDO;
import com.tp.util.BigDecimalUtil;
import com.tp.util.DateUtil;
/**
  * @author zhs
  * 分销员主题团上下架
  */
public class PromoterTopic extends BaseDO implements Serializable {

	private static final long serialVersionUID = 8070979074235417371L;

	/**编号 数据类型bigint(12)*/
	@Id
	private Long id;
	
	/**主题ID 数据类型bigint(20)*/
	private Long topicId;

	/**SKU 数据类型varchar(20)*/
	private String sku;

	/**分销员ID 数据类型bigint(20)*/
	private Long promoterId;
	
	/**状态(0-未上架 1-上架') 数据类型tinyint(4)*/
	private Integer status;
	
	/**状态(0-专场 1-专卖商品') 数据类型tinyint(4)*/
	private Integer type;
	
	/** topic list*/
	@Virtual
	private List<Long> listTopic = new ArrayList<Long>();

	/**专场类型*/
	@Virtual
	private Integer topicType;
	
	@Virtual
	private String channelCode;
	
	public Integer getTopicType() {
		return topicType;
	}

	public void setTopicType(Integer topicType) {
		this.topicType = topicType;
	}

	public List<Long> getListTopic() {
		return listTopic;
	}

	public void setListTopic(List<Long> listTopic) {
		this.listTopic = listTopic;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}
	
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Long getPromoterId() {
		return promoterId;
	}

	public void setPromoterId(Long promoterId) {
		this.promoterId = promoterId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}


}
