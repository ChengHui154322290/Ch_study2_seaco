package com.tp.backend.controller.bse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.ForbiddenWords;
import com.tp.proxy.bse.ForbiddenWordsProxy;

@Controller
@RequestMapping("/basedata/forbiddenWords")
public class ForbiddenWordsController extends AbstractBaseController {

	 private static final Logger LOG = LoggerFactory.getLogger(ForbiddenWordsController.class);


	@Autowired
	private ForbiddenWordsProxy forbiddenWordsProxy;

	/**
	 * 违禁词列表查询
	 * @param model
	 * @param forbiddenWords
	 */
	@RequestMapping(value = "/list")
	public void list(Model model, ForbiddenWords forbiddenWords,Integer page,Integer size) {
		if (null==forbiddenWords) {
			forbiddenWords = new ForbiddenWords();
		}
		PageInfo<ForbiddenWords> queryAllForbiddenWordsByPage = forbiddenWordsProxy.queryAllLikeForbiddenWordsByPageInfo(forbiddenWords, page, size);
		//封装好的分页对象(包含数据)
		model.addAttribute("queryAllForbiddenWordsByPage", queryAllForbiddenWordsByPage);
		//回显
		model.addAttribute("forbiddenWords", forbiddenWords);
	}

	/**
	 * 违禁词编辑
	 * @param model
	 * @param forbiddenWords
	 */
	@RequestMapping(value = "/edit")
	public void edit(@RequestParam(value = "id") Long id, Model model) throws Exception{
		if (null==id) {
			throw new Exception("id为空,异常");
		}
		ForbiddenWords forbiddenWords = forbiddenWordsProxy.getBrandById(id);
		model.addAttribute("forbiddenWords", forbiddenWords);
	}

	/**
	 *  转跳到 forbiddenWords增加页面
	 */
	@RequestMapping(value = "/add")
	public void addBrand() {

	}

	/**
	 * 违禁词增加
	 * @param
	 */
	@RequestMapping(value = "/addForbiddenWordsSubmit")
	@ResponseBody
	public ResultInfo<?> addForbiddenWordsSubmit(ForbiddenWords forbiddenWords) throws Exception{
		if (null==forbiddenWords) {
			LOG.info("数据不能为空");
			throw new Exception("数据异常");
		}
		forbiddenWordsProxy.addForbiddenWords(forbiddenWords);
		return new ResultInfo<>(Boolean.TRUE) ;
	}

	/**
	 * 更新违禁词
	 * @param
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> updateBrand(ForbiddenWords forbiddenWords)  throws Exception{
		if (null==forbiddenWords ) {
			LOG.info("数据不能为空");
			throw new Exception("数据异常");
		}
		forbiddenWordsProxy.updateForbiddenWords(forbiddenWords, false);
		return new ResultInfo<>(Boolean.TRUE) ;
	}

}
