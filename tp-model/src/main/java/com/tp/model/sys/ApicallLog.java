package com.tp.model.sys;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * API请求日志表
  */
public class ApicallLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1473413569287L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**地址 数据类型varchar(60)*/
	private String uri;
	
	/**ip地址 数据类型varchar(32)*/
	private String ip;
	
	/**内容类型 数据类型varchar(50)*/
	private String contentType;
	
	/**内容长度 数据类型bigint(20)*/
	private Long contentLen;
	
	/**接口名 数据类型varchar(60)*/
	private String methodName;
	
	/**请求时间 数据类型datetime*/
	private Date requestTime;
	
	/**返回时间 数据类型datetime*/
	private Date returnTime;
	
	/**请求参数:json 数据类型text*/
	private String param;
	
	/**请求头信息 数据类型text*/
	private String header;
	
	/**请求内容 数据类型text*/
	private String content;
	
	/**请求方法 数据类型varchar(10)*/
	private String method;
	
	/**返回参数 数据类型text*/
	private String result;
	
	/**耗时 数据类型bigint(20)*/
	private Long timelapse;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	@Virtual
	private Date startTime;
	
	@Virtual
	private Date endTime;
	
	public Long getId(){
		return id;
	}
	public String getUri(){
		return uri;
	}
	public String getIp(){
		return ip;
	}
	public String getContentType(){
		return contentType;
	}
	public Long getContentLen(){
		return contentLen;
	}
	public String getMethodName(){
		return methodName;
	}
	public Date getRequestTime(){
		return requestTime;
	}
	public Date getReturnTime(){
		return returnTime;
	}
	public String getParam(){
		return param;
	}
	public String getHeader(){
		return header;
	}
	public String getContent(){
		return content;
	}
	public String getMethod(){
		return method;
	}
	public String getResult(){
		return result;
	}
	public Long getTimelapse(){
		return timelapse;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setUri(String uri){
		this.uri=uri;
	}
	public void setIp(String ip){
		this.ip=ip;
	}
	public void setContentType(String contentType){
		this.contentType=contentType;
	}
	public void setContentLen(Long contentLen){
		this.contentLen=contentLen;
	}
	public void setMethodName(String methodName){
		this.methodName=methodName;
	}
	public void setRequestTime(Date requestTime){
		this.requestTime=requestTime;
	}
	public void setReturnTime(Date returnTime){
		this.returnTime=returnTime;
	}
	public void setParam(String param){
		this.param=param;
	}
	public void setHeader(String header){
		this.header=header;
	}
	public void setContent(String content){
		this.content=content;
	}
	public void setMethod(String method){
		this.method=method;
	}
	public void setResult(String result){
		this.result=result;
	}
	public void setTimelapse(Long timelapse){
		this.timelapse=timelapse;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
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
}
