package com.tp.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tp.dto.GoodsCategory;

/**
 * Created by szy on 15-9-11.
 */
@Controller
@RequestMapping("/")
public class IndexController extends AbstractBaseController {

	@RequestMapping("/index")
	public String index(){
		return "index";
	}
	
	@RequestMapping("/main")
	public void indexContent(Model model){
		
	}
	@RequestMapping("/upload")
	public String uploadFile(Model model){
		return "webuploader";
	}
	
	@RequestMapping(value="/category",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public JSONObject category(Long nodeid,Integer n_level,Long parentid){
		List<GoodsCategory> goodsCategoryList = new ArrayList<GoodsCategory>();
		nodeid=(nodeid==null?0l:nodeid);
		n_level=(n_level==null?0:n_level);
		if(null!=parentid){
			n_level++;
		}
		randomData(goodsCategoryList, nodeid, n_level);
		// 根据类别和上级类别编号查询
		JSONArray rows = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("page", 1);
		jsonObj.put("total", 1);
		jsonObj.put("records", goodsCategoryList.size());
		// int i = 0 ;
		for (GoodsCategory goodsCategory : goodsCategoryList) {
			JSONObject list = new JSONObject();
			list.put("id", goodsCategory.getId());
			list.put("name", goodsCategory.getName());
			// 是否显示添加子类按钮
			list.put("showAdd", true);
			int curLevel = goodsCategory.getLevel();
			list.put("level", curLevel);
			list.put("codeNum", goodsCategory.getCodeNum());
			// list.put("sort", i);
			// i++;
			list.put("loaded", "true");
			list.put("expanded","true");
			list.put("parent", goodsCategory.getParent());
			rows.add(list);
		}
		jsonObj.put("rows", rows);
		return jsonObj;
	}
	
	private void randomData(List<GoodsCategory> categories,Long parent,int level){
		GoodsCategory cat = null;
		for(int i=1;i<10;i++){
			cat = new GoodsCategory();
			cat.setCodeNum("code-"+i);
			cat.setId(Long.valueOf(i)+i*parent*10);
			cat.setLevel(level);
			cat.setName("name-"+i+"-p-"+parent);
			cat.setParent(parent);
			categories.add(cat);
			if(level<1){
				randomData(categories, cat.getId(), level+1);
			}
		}
	}
}
