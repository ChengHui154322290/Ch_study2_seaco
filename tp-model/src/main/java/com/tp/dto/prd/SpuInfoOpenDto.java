package com.tp.dto.prd;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 
 * <pre>
 * 开放平台sup简略信息 SpuInfoOpenDto
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class SpuInfoOpenDto implements Serializable {

	private static final long serialVersionUID = 7651046609477055112L;
    //spu下的prdid 信息
	List<SimpleDetailOpenDto> listOfSimpleDetailOpenDto;
	//规格组信息
	Map<String, String> specGroupInfo;

	public List<SimpleDetailOpenDto> getListOfSimpleDetailOpenDto() {
		return listOfSimpleDetailOpenDto;
	}

	public void setListOfSimpleDetailOpenDto(
			List<SimpleDetailOpenDto> listOfSimpleDetailOpenDto) {
		this.listOfSimpleDetailOpenDto = listOfSimpleDetailOpenDto;
	}

	public Map<String, String> getSpecGroupInfo() {
		return specGroupInfo;
	}

	public void setSpecGroupInfo(Map<String, String> specGroupInfo) {
		this.specGroupInfo = specGroupInfo;
	}

}
