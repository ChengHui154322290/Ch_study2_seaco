package com.tp.proxy.prd;

import java.io.OutputStream;
import java.util.List;

/**
 * 
 * @author szy
 * @version 0.0.1
 */
public interface ExcelWriter<T> {

	void write(List<T> dataList, OutputStream os) throws Exception;
	
}
