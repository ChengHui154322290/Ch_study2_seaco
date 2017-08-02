package com.tp.model.sys;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.sys.CommonLogConstant.RequestMethod;
import com.tp.common.vo.sys.CommonLogConstant.RestLogType;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 对外REST请求日志表
  */
public class RestLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1474348814075L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**请求类型 数据类型varchar(30)*/
	private String type;
	
	/**请求方法 数据类型varchar(20)*/
	private String method;
	
	/**请求地址 数据类型varchar(60)*/
	private String url;
	
	/**请求数据 数据类型text*/
	private String content;
	
	/**返回结果 数据类型text*/
	private String result;
	
	/**请求时间 数据类型datetime*/
	private Date requestTime;
	
	/**保留字段 数据类型varchar(200)*/
	private String remain1;
	
	/**保留字段 数据类型varchar(200)*/
	private String remain2;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	@Virtual
	private Date startTime;
	
	@Virtual
	private Date endTime;
	
	public RestLog() {
	}
	public RestLog(String url, String content, String result) {
		this(url, "", content, result);
	}
	public RestLog(String url, String type, String content, String result) {
		this(url, type, "", content, result);
	}
	public RestLog(String url, String type, String method, String content, String result) {
		this(url, type, method, content, result, new Date());
	}
	public RestLog(String url, String type, String method, String content, String result, Date requestTime) {
		this.url = url;
		this.type = type;
		this.method = method;
		this.content = content;
		this.result = result;
		this.requestTime = requestTime;
		this.createTime = new Date();
	}
	
	public String getMethodStr(){
		return RequestMethod.getRequestTypeDescByCode(method);
	}
	
	public String getTypeStr(){
		return RestLogType.getLogTypeDescByCode(type);
	}
	
	public Long getId(){
		return id;
	}
	public String getType(){
		return type;
	}
	public String getMethod(){
		return method;
	}
	public String getUrl(){
		return url;
	}
	public String getContent(){
		return content;
	}
	public String getResult(){
		return result;
	}
	public Date getRequestTime(){
		return requestTime;
	}
	public String getRemain1(){
		return remain1;
	}
	public String getRemain2(){
		return remain2;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setType(String type){
		this.type=type;
	}
	public void setMethod(String method){
		this.method=method;
	}
	public void setUrl(String url){
		this.url=url;
	}
	public void setContent(String content){
		this.content=content;
	}
	public void setResult(String result){
		this.result=result;
	}
	public void setRequestTime(Date requestTime){
		this.requestTime=requestTime;
	}
	public void setRemain1(String remain1){
		this.remain1=remain1;
	}
	public void setRemain2(String remain2){
		this.remain2=remain2;
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
