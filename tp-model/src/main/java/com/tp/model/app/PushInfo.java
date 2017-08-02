package com.tp.model.app;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.app.PushConstant;
import com.tp.exception.AppServiceException;
import com.tp.model.BaseDO;
import com.tp.util.StringUtil;

public class PushInfo extends BaseDO implements Serializable{

	private static final long serialVersionUID = -2803504067117889392L;

	/** 编号 */
	@Id
	private Long id;
	
	/** 推送标题 */
	private String title;
	
	/** 页面显示标题 */
	private String pageTitle;
	
	/** 推送内容体 JSON格式 */
	private String content;
	
	/** 推送平台 Android;iOS */
	private String platform;
	
	/** 推送有效期 */
	private Integer activeTime;
	
	/** 推送消息状态  0:未发送;1:已发送 */
	private Integer pushStatus;
	
	/**推送目标 1单体推送 2群体推送*/
	private Integer pushTarget;

	/** 推送类型  1:超链;2:专场;3:商品;*/
	private Integer pushType;
	
	/** 推送类型  1:即时;2:定时;*/
	private Integer pushWay;
	
	/** 网络类型 0:不限制;1:仅WIFI;*/
	private Integer netType;
	
	/** 超链 */
	private String link;
	
	/** 专场编号 */
	private String tid;
	
	/** 商品SKU */
	private String sku;
	
	/** 个推 ClientId*/
	private String clientId;
	
	/** 推送时间 */
	private Date sendDate;
	
	/** 是否删除  0否1是*/
	private Integer isDel = 0;
	
	/** 创建时间*/
	private Date createDate;
	
	/** 创建人*/
	private String createUser;
	
	/** 修改时间*/
	private Date modifyDate;
	
	/** 修改人*/
	private String modifyUser;
	
	/**个推成功后的ID*/
	private String contentId;
	
	/**打开数*/
	private Integer openCount;
	@Virtual
	private Date startTime;
	@Virtual
	private Date endTime;
	
	public String getPushStatusDesc(){
		return PushConstant.PUSH_STATUS.getDesc(pushStatus);
	}
	
	public String getPushTypeDesc(){
		return PushConstant.PUSH_TYPE.getDesc(pushType);
	}
	
	public String getPushTargetDesc(){
		return PushConstant.PUSH_TARGET.getDesc(pushTarget);
	}
	
	public String getPushWayDesc(){
		return PushConstant.PUSH_WAY.getDesc(pushWay);
	}
	
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Integer getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(Integer activeTime) {
		this.activeTime = activeTime;
	}

	public Integer getPushStatus() {
		return pushStatus;
	}

	public void setPushStatus(Integer pushStatus) {
		this.pushStatus = pushStatus;
	}

	public Integer getPushType() {
		return pushType;
	}

	public void setPushType(Integer pushType) {
		this.pushType = pushType;
	}

	public Integer getPushTarget() {
		return pushTarget;
	}

	public void setPushTarget(Integer pushTarget) {
		this.pushTarget = pushTarget;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getPushWay() {
		return pushWay;
	}

	public void setPushWay(Integer pushWay) {
		this.pushWay = pushWay;
	}

	public Integer getNetType() {
		return netType;
	}

	public void setNetType(Integer netType) {
		this.netType = netType;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	
	public void remove(Map<String, Object> param) {
		Field[] fields = PushInfo.class.getDeclaredFields();
		for (Field f : fields) {
			Virtual v = f.getAnnotation(Virtual.class);
			if(null != v)
				param.remove(f.getName());
		}
	}
	
	public void validate(PushInfo pushInfo){
		if(StringUtil.isEmpty(pushInfo.getTitle())) throw new AppServiceException("推送标题不能为空");
		if(StringUtil.isNull(pushInfo.getPushTarget())) throw new AppServiceException("推送目标不能为空");
		if(StringUtil.isNull(pushInfo.getPlatform())) throw new AppServiceException("推送平台不能为空");
		if(StringUtil.isNull(pushInfo.getPushType())) throw new AppServiceException("推送类型不能为空");
		if(StringUtil.isNull(pushInfo.getPushWay())) throw new AppServiceException("推送方式不能为空");
		if(StringUtil.isNull(pushInfo.getActiveTime())) throw new AppServiceException("推送有效期不能为空");
		switch(pushInfo.getPushType()){
			case 0:
				if(StringUtil.isEmpty(pushInfo.getLink())) throw new AppServiceException("推送链接不能为空");
				break;
			case 1:
				if(StringUtil.isEmpty(pushInfo.getTid())) throw new AppServiceException("推送专题不能为空");
				break;
			case 2:
				if(StringUtil.isEmpty(pushInfo.getTid())) throw new AppServiceException("推送专题不能为空");
				if(StringUtil.isEmpty(pushInfo.getSku())) throw new AppServiceException("推送商品不能为空");
				break;
		}
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public Integer getOpenCount() {
		return openCount;
	}

	public void setOpenCount(Integer openCount) {
		this.openCount = openCount;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
}
