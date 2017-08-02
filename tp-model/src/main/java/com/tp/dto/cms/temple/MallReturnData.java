/**
 * 
 */
package com.tp.dto.cms.temple;

import java.io.Serializable;

/**
 * 西客商城返回数据
 * 
 * @author szy
 * @version 0.0.1
 * 
 */
public class MallReturnData implements Serializable {

	private static final long serialVersionUID = -1620952831373453144L;
	
	/**商品名称**/
	private String name;
	
	/**链接**/
	private String link;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}


	
}
