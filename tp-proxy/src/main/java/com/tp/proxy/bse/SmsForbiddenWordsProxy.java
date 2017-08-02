package com.tp.proxy.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.bse.SmsForbiddenWords;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.ISmsForbiddenWordsService;
/**
 * 短信违禁词代理层
 * @author szy
 *
 */
@Service
public class SmsForbiddenWordsProxy extends BaseProxy<SmsForbiddenWords>{

	@Autowired
	private ISmsForbiddenWordsService smsForbiddenWordsService;

	@Override
	public IBaseService<SmsForbiddenWords> getService() {
		return smsForbiddenWordsService;
	}
}
