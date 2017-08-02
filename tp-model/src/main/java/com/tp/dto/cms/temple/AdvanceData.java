package com.tp.dto.cms.temple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 明日预告数据
 * 2015-1-7
 */
public class AdvanceData  implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 9208311663651733225L;

	private String date;
	
	private String message;
	
	List<AdvancePicData> advancePic =  new ArrayList<AdvancePicData>();

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<AdvancePicData> getAdvancePic() {
		return advancePic;
	}

	public void setAdvancePic(List<AdvancePicData> advancePic) {
		this.advancePic = advancePic;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
