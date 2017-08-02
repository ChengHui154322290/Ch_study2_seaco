package com.tp.dto.common;

import java.io.Serializable;
import java.util.Map;

import com.tp.common.vo.Constant;
import com.tp.constant.common.ExceptionConstant;
import com.tp.enums.common.XGCodeEnum;
import com.tp.exception.AbstractXgException;
/**
 * 错误信息
 * @author szy
 *
 */
public class FailInfo implements Serializable {

	private static final long serialVersionUID = 4134682692521248805L;
	/**错误代码*/
	private Integer code=0;
	/**异常信息*/
	private transient Throwable exception;
	/**异常级别*/
	private Integer level;
	/**异常信息*/
	private String message;
	
	/**详细错误信息*/
	private transient String detailMessage;
	
	private String modelType=Constant.EXCEPTION_MODEL_TYPE.system.name();

	private Map<String,String> errorMap; 
	public transient Object[] params={};
	
	/**打印所属类名、方法名、行号*/
	public transient String clazzMethodNumber;

	public FailInfo(){
		
	}
	public FailInfo(Integer code, Throwable exception,Object[] params) {
		super();
		this.code = code;
		this.params = params;
		if(null!=exception){
			this.detailMessage = exception.getStackTrace()[0].toString()+"【"+exception.getMessage()+"】";
			if(null==message){
				this.message = exception.getMessage();
			}
		}
		this.exception=exception;
	}
	public FailInfo(Integer code, Throwable exception) {
		this(code,exception,null);
	}
	public FailInfo(Integer code, Object... params) {
		this(code,null,params);
	}
	public FailInfo(Integer code){
		this(code,null,null);
	}
	public FailInfo(Throwable exception) {
		this(null,exception,null);
		if(null!=exception){
			if(exception instanceof AbstractXgException){
				code = ((AbstractXgException)exception).getCode();
			}
		}
	}
	public FailInfo(Integer code,String modelType){
		super();
		this.code = code;
		this.modelType = modelType;
	}
	public FailInfo(String message,Integer code){
		super();
		this.code = code;
		this.detailMessage = this.message = message;
	}
	public FailInfo(String hintMessage){
		super();
		this.detailMessage = this.message = hintMessage;
	}
	public FailInfo(Map<String,String> errorMap){
		super();
		this.errorMap = errorMap;
		if(errorMap!=null && errorMap.values().iterator().hasNext())
		this.detailMessage = this.message =errorMap.values().iterator().next();
	}

	public FailInfo(XGCodeEnum errorEnum){
		super();
		this.code = errorEnum.getCode();
		this.detailMessage = this.message = errorEnum.getMessage();
	}
	
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getLevel() {
		if(exception instanceof AbstractXgException){
			level = AbstractXgException.LEVEL;
		}else if(exception instanceof Exception){
			level = ExceptionConstant.EXCEPTION_LEVEL.SYSTEM.code;
		}else if(exception instanceof Throwable){
			level = ExceptionConstant.EXCEPTION_LEVEL.ERROR.code;
		}
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getModelType() {
		if(exception instanceof AbstractXgException){
			modelType = ((AbstractXgException)exception).getModelType();
		}else if(exception instanceof Exception){
			modelType = Constant.EXCEPTION_MODEL_TYPE.system.name();
		}else if(exception instanceof Throwable){
			modelType = Constant.EXCEPTION_MODEL_TYPE.error.name();
		}
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	@Override
	public String toString() {
		return "FailInfo [code=" + code + ", exception=" + exception
				+ ", level=" + level + ", message=" + message + ", modelType="
				+ modelType + "]";
	}
	public Map<String, String> getErrorMap() {
		return errorMap;
	}
	public void setErrorMap(Map<String, String> errorMap) {
		this.errorMap = errorMap;
	}
	public String getDetailMessage() {
		return detailMessage;
	}
	public void setDetailMessage(String detailMessage) {
		this.detailMessage = detailMessage;
	}
}
