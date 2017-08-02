package com.tp.service.wms;

import com.tp.dto.wms.excel.StockinImportDetailDto;
import com.tp.model.wms.StockasnFact;
import com.tp.result.wms.ResultMessage;
import com.tp.service.IBaseService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;
import java.util.List;

/**
 * @author szy
 *         接口
 */
public interface IStockasnFactService extends IBaseService<StockasnFact> {


    ResultMessage purchaseFactOrder(String serviceId, String content) throws Exception;

	void saveImportDetailAndImportLog(String newName, String userName,
			List<StockinImportDetailDto> excelList,Long stockasnId,String realFileName,String secretKey,String token);

}
