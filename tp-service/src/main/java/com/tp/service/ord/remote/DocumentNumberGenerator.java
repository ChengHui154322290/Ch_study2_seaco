package com.tp.service.ord.remote;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant.DOCUMENT_TYPE;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.redis.util.JedisDBUtil;
import com.tp.service.IDocumentNumberGenerator;

@Service
public class DocumentNumberGenerator implements IDocumentNumberGenerator{

	private final static String DATE_FORMATE="yyMMdd";
	
	private final static int INDEX_NO_LENGTH = 8 ;
	
	private final static int ZERO = 0;
	@Autowired
	private JedisDBUtil jedisDBUtil;

	public Long generate(DOCUMENT_TYPE type) {
		String dateStr = dateString();
		String indexStr = indexString(type);
		return Long.valueOf(new StringBuffer().append(type.code).append(dateStr).append(indexStr).toString());
	}
	
	/**
	 * 日期字符串
	 * 
	 * @return
	 */
	private String dateString() {
		return new SimpleDateFormat(DATE_FORMATE).format(new Date());
	}

	/**
	 * 自增码
	 * 
	 * @return
	 */
	private String indexString(DOCUMENT_TYPE type) {
		Long index = jedisDBUtil.incr("documentnumber:generator:seqtype"+type.code);
		if(null==index){
			index = System.currentTimeMillis();
		}
		String idxStr = index.toString();
		int len = idxStr.length();
		StringBuilder sb = new StringBuilder(idxStr);
		if (len < INDEX_NO_LENGTH) {
			return index+String.format("%07d", index);
		} else {
			return index+sb.delete(0,sb.length()-INDEX_NO_LENGTH).toString();
		}
	}
}
