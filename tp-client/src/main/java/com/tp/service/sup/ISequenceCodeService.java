package com.tp.service.sup;

import java.util.HashMap;
import java.util.Map;

import com.tp.model.sup.SequenceCode;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 采购管理-编号表接口
  */
public interface ISequenceCodeService extends IBaseService<SequenceCode>{
	public final static Map<String,String> CONTRACT_TEMPLATE_MAP = new HashMap<String,String>();
	//品类代发
	public final static String CONTRACT_TEMPLATE_PLDF = "contract_template_pldf";
	//品类入仓
	public final static String CONTRACT_TEMPLATE_PLRC = "contract_template_plrc";
	//海淘直邮
	public final static String CONTRACT_TEMPLATE_HTZY = "contract_template_htzy";
	//海淘保税
	public final static String CONTRACT_TEMPLATE_HTBS = "contract_template_htbs";
	
	/**
	 * 获取对应模板的code
	 * 
	 * @param templateType
	 * @return
	 */
	public Long currentCode(String templateType);
	
	/**
	 * 下一个code
	 * 
	 * @param templateType
	 * @return
	 */
	public Long nextCode(String templateType);
}
