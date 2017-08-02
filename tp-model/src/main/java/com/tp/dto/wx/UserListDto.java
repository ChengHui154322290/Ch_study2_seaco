package com.tp.dto.wx;

import java.io.Serializable;

/**
 * 公众号关注用户列表
 * @author zhuss
 * @2016年4月27日 下午3:43:55
 */
public class UserListDto implements Serializable{

	private static final long serialVersionUID = -8217767471304998520L;
	
	private int total; //关注该公众账号的总用户数
	private int count; //拉取的OPENID个数，最大值为10000
	private UserListDataDto data;
	private String next_openid; //拉取列表的最后一个用户的OPENID
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public UserListDataDto getData() {
		return data;
	}
	public void setData(UserListDataDto data) {
		this.data = data;
	}
	public String getNext_openid() {
		return next_openid;
	}
	public void setNext_openid(String next_openid) {
		this.next_openid = next_openid;
	}
}
