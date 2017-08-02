package com.tp.service;

import com.tp.common.vo.Constant.DOCUMENT_TYPE;

public interface IDocumentNumberGenerator {
	
	/**
	 * 生成编码 
	 * @param type
	 * @return
	 */
	public Long generate(DOCUMENT_TYPE type);
}
