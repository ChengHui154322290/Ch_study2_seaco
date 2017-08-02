package com.tp.m.vo.order;

import java.util.List;

import com.tp.m.base.BaseVO;
import com.tp.m.to.order.LogDetailTO;

/**
 * 物流对象
 * @author zhuss
 * @2016年1月7日 下午8:48:12
 */
public class LogisticVO implements BaseVO{

	private static final long serialVersionUID = -6450375804945774846L;

	private String company;//物流公司
	private String logcode;//物流单号
	private List<LogDetailTO> loglist;
	
	
	public LogisticVO() {
		super();
	}
	public LogisticVO(String company, String logcode, List<LogDetailTO> loglist) {
		super();
		this.company = company;
		this.logcode = logcode;
		this.loglist = loglist;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getLogcode() {
		return logcode;
	}
	public void setLogcode(String logcode) {
		this.logcode = logcode;
	}
	public List<LogDetailTO> getLoglist() {
		return loglist;
	}
	public void setLoglist(List<LogDetailTO> loglist) {
		this.loglist = loglist;
	}
}
