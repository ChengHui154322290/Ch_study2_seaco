package com.tp.backend.controller.bse;

import java.util.Date;
import java.util.List;

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
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.AdvertPlatform;
import com.tp.proxy.bse.AdvertPlatformProxy;

@Controller
@RequestMapping("/basedata/advertPlat")
public class AdvertPlatController extends AbstractBaseController {

	 private static final Logger LOG = LoggerFactory.getLogger(AdvertPlatController.class);


	@Autowired
	private AdvertPlatformProxy advertPlatformProxy;

	/**
	 *广告商平台列表查询
	 * @param model
	 * @param forbiddenWords
	 */
	@RequestMapping(value = "/list")
	public void list(Model model, AdvertPlatform advertPlatform,Integer page,Integer size) {
		if (null==advertPlatform) {
			advertPlatform = new AdvertPlatform();
		}
		PageInfo<AdvertPlatform> info=new PageInfo<AdvertPlatform>();
		ResultInfo<PageInfo<AdvertPlatform>> queryAllAdvertPlatformByPage = advertPlatformProxy.queryPageByObject(advertPlatform, info);
		info.setRecords(queryAllAdvertPlatformByPage.getData().getRecords());
		info.setRows(queryAllAdvertPlatformByPage.getData().getRows());
		info.setTotal(queryAllAdvertPlatformByPage.getData().getPage());
		//封装好的分页对象(包含数据)
		model.addAttribute("queryAlladvertPlatByPage", info);
		//回显
		model.addAttribute("advertPlatform", advertPlatform);
	}

	/**
	 * 广告平台
	 * @param model
	 * @param model
	 */
	@RequestMapping(value = "/edit")
	public void edit(@RequestParam(value = "id") Long id, Model model) throws Exception{
		if (null==id) {
			throw new Exception("id为空,异常");
		}
		ResultInfo<AdvertPlatform> advertPlatform = advertPlatformProxy.queryById(id);
		model.addAttribute("advertPlatform", advertPlatform.getData());
	}

	/**
	 *  转跳到 forbiddenWords增加页面
	 */
	@RequestMapping(value = "/add")
	public void addPlatform() {

	}

	/**
	 * 广告平台保存
	 * 	 * @param
	 */
	@RequestMapping(value = "/advertPlatSubmit")
	@ResponseBody
	public ResultInfo<?> andAdvertPlatSubmit(AdvertPlatform advertPlatform) throws Exception{
		AdvertPlatform advertPlatformForSearch=new AdvertPlatform();
		advertPlatformForSearch.setAdvertPlatCode(advertPlatform.getAdvertPlatCode());
		ResultInfo<List<AdvertPlatform>> searchResaultadvertPlatform = advertPlatformProxy.queryByObject(advertPlatformForSearch);
		if(searchResaultadvertPlatform.getData().size()>0){
			LOG.info("该平台编号已存在！");
			FailInfo failInfo=new FailInfo("该平台编号已存在！");
			return new ResultInfo<>(failInfo);
		}
		if (null==advertPlatform) {
			LOG.info("数据不能为空");
			throw new Exception("数据异常");
		}
		if(advertPlatform.getClickNum()==null){
			advertPlatform.setClickNum(0);
		}
		if(advertPlatform.getRegisterNum()==null){
			advertPlatform.setRegisterNum(0);
		}
		if(advertPlatform.getCreateTime()==null){
			advertPlatform.setCreateTime(new Date());
		}
		if(advertPlatform.getUpdateTime()==null){
			advertPlatform.setUpdateTime(new Date());
		}
		if(advertPlatform.getUpdateUser()==null){
			advertPlatform.setUpdateUser(getUserInfo().getLoginName());
		}
		advertPlatform.setCreateUser(getUserInfo().getLoginName());
		
		advertPlatformProxy.insert(advertPlatform);
		return new ResultInfo<>(Boolean.TRUE) ;
	}

	/**
	 * 更新广告平台
	 * @param
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> updateBrand(AdvertPlatform advertPlatform)  throws Exception{
		if (null==advertPlatform ) {
			LOG.info("数据不能为空");
			throw new Exception("数据异常");
		}
		AdvertPlatform advertPlatformForSearch=new AdvertPlatform();
		advertPlatformForSearch.setAdvertPlatCode(advertPlatform.getAdvertPlatCode());
		ResultInfo<List<AdvertPlatform>> searchResaultadvertPlatform = advertPlatformProxy.queryByObject(advertPlatformForSearch);
		if(searchResaultadvertPlatform.getData().size()>0){
			List<AdvertPlatform> advertPlatforms=searchResaultadvertPlatform.getData();
			for(int i=0,len=advertPlatforms.size();i<len;i++){
				AdvertPlatform advertPlatformResult=advertPlatforms.get(i);
				if(advertPlatformResult.getId()!=advertPlatform.getId()){
					LOG.info("该平台编号已存在！");
					FailInfo failInfo=new FailInfo("该平台编号已存在！");
					return new ResultInfo<>(failInfo);
				}
			}
			
		}
		AdvertPlatform advertPlatformForSave = advertPlatformProxy.queryById(advertPlatform.getId()).getData();
		advertPlatformForSave.setAdvertPlatName(advertPlatform.getAdvertPlatName());
		advertPlatformForSave.setAdvertPlatCode(advertPlatform.getAdvertPlatCode());
		advertPlatformProxy.updateById(advertPlatformForSave);
		return new ResultInfo<>(Boolean.TRUE) ;
	}

}
