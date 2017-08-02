package com.tp.model.cms;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 模板管理表
  */
public class SingleproTemple extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451446451848L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**模板名称 数据类型varchar(50)*/
	private String templeName;
	
	/**路径 数据类型varchar(128)*/
	private String path;
	
	/**模板状态 数据类型varchar(10)*/
	private String status;
	
	/** 数据类型varchar(10)*/
	private String type;
	
	/**平台标识,0表示PC,1表示app 数据类型int(4)*/
	private Integer platformType;
	
	/**模板上传日志表主键 数据类型bigint(11)*/
	private Long uploadTempleId;
	
	
	public Long getId(){
		return id;
	}
	public String getTempleName(){
		return templeName;
	}
	public String getPath(){
		return path;
	}
	public String getStatus(){
		return status;
	}
	public String getType(){
		return type;
	}
	public Integer getPlatformType(){
		return platformType;
	}
	public Long getUploadTempleId(){
		return uploadTempleId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setTempleName(String templeName){
		this.templeName=templeName;
	}
	public void setPath(String path){
		this.path=path;
	}
	public void setStatus(String status){
		this.status=status;
	}
	public void setType(String type){
		this.type=type;
	}
	public void setPlatformType(Integer platformType){
		this.platformType=platformType;
	}
	public void setUploadTempleId(Long uploadTempleId){
		this.uploadTempleId=uploadTempleId;
	}
}
