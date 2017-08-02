package com.tp.backend.controller.bse;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.Attribute;
import com.tp.model.bse.Category;
import com.tp.model.bse.CategoryAttributeLink;
import com.tp.proxy.bse.AttributeProxy;
import com.tp.proxy.bse.CategoryAttributeLinkProxy;
import com.tp.proxy.bse.CategoryProxy;
import com.tp.util.BeanUtil;

@Controller
@RequestMapping("/basedata/attribute")
public class AttributeController  extends AbstractBaseController{
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(AttributeController.class);

	@Autowired
	private AttributeProxy attributeProxy;
	
	@Autowired
	private CategoryProxy categoryProxy;
	@Autowired 
	private CategoryAttributeLinkProxy  categoryAttributeLinkProxy;
	
	  /**查询  
	   * @param attribute
	   * @throws Exception
	   */
	  @RequestMapping(value="/list")  
	  public void list(Attribute attribute, Model model,Integer page,Integer size) throws Exception { 
		  PageInfo<Attribute> pageInfo = new PageInfo<Attribute>(page,size);
		  Map<String,Object> params = BeanUtil.beanMap(attribute);
		  if(StringUtils.isNotBlank(attribute.getName())){
			  params.remove("name");			 
			  params.put(MYBATIS_SPECIAL_STRING.LIKE.name(), " name like concat ('%','"+attribute.getName()+"','%')");
		  }
		  ResultInfo<PageInfo<Attribute>> resultInfo = attributeProxy.queryPageByParam(params,pageInfo);
		  model.addAttribute("queryPageListByAttribute", resultInfo);		  
		  model.addAttribute("attribute", attribute);
	  } 
	
	
	/**
	 * 转到增加 属性页面
	 * @param Attribute
	 */
	@RequestMapping(value="/add")
	public void add(Model model) {
	}
	
	/**
	 * <pre>
	 * 转到edit页面.
	 * </pre>
	 * @param id
	 * @param model
	 */
	@RequestMapping(value="/edit")
	public void edit(Long id, Model model)throws Exception {
		if(null==id){
			model.addAttribute("errorMsg","属性ID为空");
			return;
		}
		ResultInfo<Attribute> attributeResultInfo = attributeProxy.queryById(id);
		CategoryAttributeLink  attributeLink=new CategoryAttributeLink();
		attributeLink.setAttributeId(id);
		ResultInfo<List<CategoryAttributeLink>> cateAttrLinkedResultInfo = categoryAttributeLinkProxy.queryByObject(attributeLink);
	    String checkValue="";
	    if(CollectionUtils.isNotEmpty(cateAttrLinkedResultInfo.getData())){
	    	for (CategoryAttributeLink CategoryAttributeLink : cateAttrLinkedResultInfo.getData()) {
				 checkValue=checkValue+(CategoryAttributeLink.getCategoryId())+",";
			}
	    }
		model.addAttribute("checkValue", checkValue);
		model.addAttribute("attribute", attributeResultInfo.getData());
	}
	/**
	 * <pre>
	 * 转到viewAttr页面.
	 * </pre>
	 * @param id
	 * @param model
	 */
	@RequestMapping(value="/viewAttr")
	public void viewAttr(Long id, Model model)throws Exception {
		if(null==id){
			model.addAttribute("errorMsg","属性ID为空");
			return;
		}
		ResultInfo<Attribute> attributeResultInfo = attributeProxy.queryById(id);
		CategoryAttributeLink  attributeLink=new CategoryAttributeLink();
		attributeLink.setAttributeId(id);
		ResultInfo<List<CategoryAttributeLink>> cateAttrLinkedResultInfo = categoryAttributeLinkProxy.queryByObject(attributeLink);
		if(CollectionUtils.isNotEmpty(cateAttrLinkedResultInfo.getData())){
    	   	List<Long> list=new ArrayList<Long>();
    		 for (CategoryAttributeLink CategoryAttributeLink : cateAttrLinkedResultInfo.getData()) {
    			 list.add(CategoryAttributeLink.getCategoryId());
    		}
    	   ResultInfo<List<Category>> listOfCategoryResultInfo=categoryProxy.queryCategoryByParams(list);
    	   model.addAttribute("listOfCategory", listOfCategoryResultInfo.getData());
    	}	
		model.addAttribute("attribute", attributeResultInfo.getData());
	}

	/**
	 * <pre>
	 * 更新attribute
	 * </pre>
	 * @param
	 * @throws ExceptionResultInfo 
	 */	
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo<?> edit(Attribute attribute, Long [] ids) throws Exception{
		 attributeProxy.updateAttribute(attribute,ids);
		 return new ResultInfo<>(Boolean.TRUE);
	}
	
	
	/**
	 * <pre>
	 * 增加Attribute
	 * </pre>
	 * @param Attribute
	 * @return
	 */
	@RequestMapping(value="/saveAdd", method = RequestMethod.POST)
	@ResponseBody
	public  ResultInfo<?>  saveAdd(Attribute Attribute, Long [] ids)  throws Exception{
		if(null==Attribute){
			throw new Exception("数据不能为空");
		}
		attributeProxy.addAttributeAndLinked(Attribute,ids);
		return new ResultInfo<>(Boolean.TRUE);
	}	

	/*
	 * 转到view页面. 
	 */
	@RequestMapping(value="/view")
	public void viewNotCheckAttribute( @RequestParam(value = "cateId") Long cateId ,Model model)  throws Exception{
		if(null==cateId){
			throw new Exception("id为空,异常");
		}
		List<CategoryAttributeLink> slectLinkInfo = attributeProxy.slectLinkInfo(cateId);
	
		Attribute attribute =new Attribute();
		attribute.setStatus(1);
		List<Attribute> list = attributeProxy.selectDyamicCategory(attribute);
		model.addAttribute("slectLinkInfo", slectLinkInfo);
		model.addAttribute("list", list);
		model.addAttribute("cateId", cateId);
		
	}
	
}
