package com.tp.service.prd.openplantform;

import com.tp.dto.prd.ItemOpenSaveDto;
import com.tp.exception.ItemServiceException;

/***
 * 
 * @author caihui
 *
 */
public interface IItemPlatService {
	/***
	 * 保存platform平台商品
	 * @param saveDto
	 * @return
	 * @throws ItemServiceException
	 * @throws DAOException
	 */
	String savePushItemInfoFromPlatSpuLevel(ItemOpenSaveDto saveDto)throws ItemServiceException;
	
	/***
	 *  PRD level 保存
	 * @param saveDto
	 * @return
	 * @throws ItemServiceException
	 * @throws DAOException
	 */
	String savePushItemInfoFromPlatPRDLevel(ItemOpenSaveDto saveDto)throws ItemServiceException;
	
}
