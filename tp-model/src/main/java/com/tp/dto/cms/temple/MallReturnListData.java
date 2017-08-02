/**
 * 
 */
package com.tp.dto.cms.temple;

import java.io.Serializable;
import java.util.List;

/**
 * 西客商城返回数据
 * 
 * @author szy
 * @version 0.0.1
 * 
 */
public class MallReturnListData implements Serializable {

	private static final long serialVersionUID = -1620952831373453144L;
	
	/**商品名称**/
	private List<MallReturnData> MallReturnList;

	public List<MallReturnData> getMallReturnList() {
		return MallReturnList;
	}

	public void setMallReturnList(List<MallReturnData> mallReturnList) {
		MallReturnList = mallReturnList;
	}
	
	


	
}
