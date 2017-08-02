package com.tp.dto.cms;

import java.io.Serializable;
import java.util.Date;


public class CmsSingleTempleParmDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
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

	

}
