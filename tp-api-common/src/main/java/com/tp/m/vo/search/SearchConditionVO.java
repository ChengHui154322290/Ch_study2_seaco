package com.tp.m.vo.search;

import java.util.List;

import com.tp.m.base.BaseVO;

/**
 * 搜索结果 - 筛选条件
 * @author zhuss
 * @2016年3月2日 下午3:28:15
 */
public class SearchConditionVO implements BaseVO{

	private static final long serialVersionUID = 2678211490082901362L;

	private String id;
	private String code;
	private String name;
	private List<SearchConditionVO> child;
	
	public SearchConditionVO() {
		super();
	}
	public SearchConditionVO(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<SearchConditionVO> getChild() {
		return child;
	}
	public void setChild(List<SearchConditionVO> child) {
		this.child = child;
	}
}
