package com.tp.dto.cms;

import java.io.Serializable;
import java.util.Date;

import com.tp.model.BaseDO;


public class CmsSingleTempleDTO extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	/** 模板名称 */
	private String templeName;
	
	/** 模板表主键 */
	private Long singleTempleId;
	
	/** 模板节点表主键 */
	private Long singleTempleNodeId;

	/** 位置名称 */
	private String positionName;

	/** 位置尺寸 */
	private String positionSize;
	
	/** 位置顺序 */
	private Integer positionSort;
	
	/** 删除标志 */
	private Integer dr;
	
	/** 埋点编码 */
	private String buriedCode;
	
	/** 模板状态 */
	private String status;

	/** 创建人 */
	private Integer creater;

	/** 创建时间 */
	private Date create_time;

	/** 修改人 */
	private Integer modifier;

	/** 修改时间 */
	private Date modify_time;
	
	/** 模板路径 */
	private String path;
	
	/** 文件名称 */
	private String filename;
	
	/** 文件路径 */
	private String srcpath;
	
	/** 模板类型（如单品团为10，今日特卖为4，明日预告是11，今日必海淘是12） */
	private String type;
	
	/** 活动id */
	private Long activityId;
	
	/** 总页数 */
	private Integer totalCount;
	
	/** 总条数 */
	private Integer totalCountNum;
	
	/** 平台标识 */
	private String platformType;
	
	/** 模板上传表主键 */
	private Long uploadTempleId;
	
	/** 开始日期字符串 */
	private String startDateStr;
	
	/** 结束日期字符串 */
	private String endDateStr;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTempleName() {
		return templeName;
	}

	public void setTempleName(String templeName) {
		this.templeName = templeName;
	}

	public Long getSingleTempleId() {
		return singleTempleId;
	}

	public void setSingleTempleId(Long singleTempleId) {
		this.singleTempleId = singleTempleId;
	}

	public Long getSingleTempleNodeId() {
		return singleTempleNodeId;
	}

	public void setSingleTempleNodeId(Long singleTempleNodeId) {
		this.singleTempleNodeId = singleTempleNodeId;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getPositionSize() {
		return positionSize;
	}

	public void setPositionSize(String positionSize) {
		this.positionSize = positionSize;
	}

	public Integer getPositionSort() {
		return positionSort;
	}

	public void setPositionSort(Integer positionSort) {
		this.positionSort = positionSort;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getBuriedCode() {
		return buriedCode;
	}

	public void setBuriedCode(String buriedCode) {
		this.buriedCode = buriedCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getCreater() {
		return creater;
	}

	public void setCreater(Integer creater) {
		this.creater = creater;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Integer getModifier() {
		return modifier;
	}

	public void setModifier(Integer modifier) {
		this.modifier = modifier;
	}

	public Date getModify_time() {
		return modify_time;
	}

	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getSrcpath() {
		return srcpath;
	}

	public void setSrcpath(String srcpath) {
		this.srcpath = srcpath;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getTotalCountNum() {
		return totalCountNum;
	}

	public void setTotalCountNum(Integer totalCountNum) {
		this.totalCountNum = totalCountNum;
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}

	public Long getUploadTempleId() {
		return uploadTempleId;
	}

	public void setUploadTempleId(Long uploadTempleId) {
		this.uploadTempleId = uploadTempleId;
	}

	public String getStartDateStr() {
		return startDateStr;
	}

	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}

	public String getEndDateStr() {
		return endDateStr;
	}

	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	

}
