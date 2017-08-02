package com.tp.model.mkt;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.mkt.ChannelPromoteConstant;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 渠道推广
  */
public class ChannelPromote extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1461572523912L;

	/** 数据类型int(11)*/
	@Id
	private Integer id;
	
	/**1个人2企业 数据类型tinyint(2)*/
	private Integer type;
	
	/**1微信 数据类型tinyint(2)*/
	private Integer source;
	
	/**渠道 数据类型varchar(50)*/
	private String channel;
	
	/**唯一标识 数据类型varchar(100)*/
	private String uniqueId;
	
	/**渠道二维码  数据类型varchar(500)*/
	private String qrcode;
	
	/**是否关注0否1是 数据类型tinyint(1)*/
	private Integer isFollow;
	
	/**是否删除 0否1是 数据类型tinyint(1)*/
	private Integer isDel;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	@Virtual
	private int unFollowCount;
	
	@Virtual
	private int followCount;
	
	public String getTypeDesc(){
		return ChannelPromoteConstant.TYPE.getDesc(type);
	}
	
	public String getSourceDesc(){
		return ChannelPromoteConstant.SOURCE.getDesc(source);
	}
	
	public Integer getId(){
		return id;
	}
	public Integer getType(){
		return type;
	}
	public String getChannel(){
		return channel;
	}
	public String getUniqueId(){
		return uniqueId;
	}
	public Integer getIsDel(){
		return isDel;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Integer id){
		this.id=id;
	}
	public void setType(Integer type){
		this.type=type;
	}
	public void setChannel(String channel){
		this.channel=channel;
	}
	public void setUniqueId(String uniqueId){
		this.uniqueId=uniqueId;
	}
	public void setIsDel(Integer isDel){
		this.isDel=isDel;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}
	public Integer getIsFollow() {
		return isFollow;
	}

	public void setIsFollow(Integer isFollow) {
		this.isFollow = isFollow;
	}

	public int getUnFollowCount() {
		return unFollowCount;
	}

	public void setUnFollowCount(int unFollowCount) {
		this.unFollowCount = unFollowCount;
	}

	public int getFollowCount() {
		return followCount;
	}

	public void setFollowCount(int followCount) {
		this.followCount = followCount;
	}
	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public void remove(Map<String, Object> param) {
		Field[] fields = ChannelPromote.class.getDeclaredFields();
		for (Field f : fields) {
			Virtual v = f.getAnnotation(Virtual.class);
			if(null != v)
				param.remove(f.getName());
		}
	}
}
