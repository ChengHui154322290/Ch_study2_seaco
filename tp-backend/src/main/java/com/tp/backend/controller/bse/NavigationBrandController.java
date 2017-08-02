package com.tp.backend.controller.bse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.util.ImageUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.ItemDto;
import com.tp.dto.sch.BrandRange;
import com.tp.dto.sch.enums.NavigationType;
import com.tp.model.bse.NavigationCategory;
import com.tp.model.bse.NavigationCategoryRange;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.bse.NavigationCategoryProxy;
import com.tp.proxy.bse.NavigationCategoryRangeProxy;
import com.tp.proxy.prd.ItemProxy;
import com.tp.util.BeanUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 分类导航 品牌分类相关Controller
 * Created by ldr on 2016/2/26.
 */
@Controller
@RequestMapping("/basedata/navigation")
public class NavigationBrandController extends AbstractBaseController {

    public static final String NAVIGATION_NAV_CATEGORY_INFO = "basedata/navigation/navCategoryInfo";

    public static final String NAVIGATION_NAV_BRAND_RANGE = "basedata/navigation/navBrandRange";

    public static final String NAVIGATION_NAV_BRAND_HOME = "basedata/navigation/navBrandHome";
    @Autowired
    private NavigationCategoryProxy navigationCategoryProxy;

    @Autowired
    private NavigationCategoryRangeProxy navigationCategoryRangeProxy;

    @Autowired
    private ItemProxy itemProxy;


    /**
     * 品牌分类展示页
     *
     * @param request
     * @return
     */
    @RequestMapping("/navBrandHome.htm")
    public String navBrandHome(HttpServletRequest request) {
        return NAVIGATION_NAV_BRAND_HOME;
    }

    /**
     * 品牌分类信息JSON
     *
     * @param request
     * @param model
     * @param query
     * @param parentId
     * @return
     */
    @ResponseBody
    @RequestMapping("/navBrandHomeJSON")
    public Map<String, Object> navBrandHomeJSON(HttpServletRequest request, Model model, NavigationCategory query, Long parentId) {
        query.setStartPage(1);
        query.setPageSize(999);
        query.setType(NavigationType.BRAND.getCode());
        ResultInfo<PageInfo<NavigationCategory>> result = navigationCategoryProxy.queryByPage(query);
        model.addAttribute("result", result);
        Map<String, Object> res = new HashMap<>();
        res.put("rows", result.getData().getRows());
        res.put("page", result.getData().getPage());
        res.put("recodes", result.getData().getRecords());
        res.put("total", result.getData().getTotal());
        return res;
    }

    /**
     * 添加品牌分类
     *
     * @param model
     * @return
     */
    @RequestMapping("/navAddBrand")
    public String navAddFirstLevel(Model model) {
        ResultInfo<Integer> resultSort = navigationCategoryProxy.getMaxSort(-1L, NavigationType.BRAND.getCode());

        model.addAttribute("sort", resultSort.getData());
        model.addAttribute("level", 1);
        model.addAttribute("parentId", -1);
        model.addAttribute("type", NavigationType.BRAND.getCode());
        model.addAttribute("bucketURL", Constant.IMAGE_URL_TYPE.basedata.url);
        model.addAttribute("bucketname", Constant.IMAGE_URL_TYPE.basedata.name());
        return NAVIGATION_NAV_CATEGORY_INFO;
    }

    /**
     * 修改品牌分类
     *
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/navEditBrand")
    public String navEditBrand(Model model, Long id) {
        ResultInfo<NavigationCategory> result = navigationCategoryProxy.queryById(id);

        model.addAttribute("result", result);
        model.addAttribute("bucketURL", Constant.IMAGE_URL_TYPE.basedata.url);
        model.addAttribute("bucketname", Constant.IMAGE_URL_TYPE.basedata.name());
        if (result.isSuccess() && result.getData() != null) {
            model.addAttribute("category", result.getData());
            model.addAttribute("sort", result.getData().getSort());
            model.addAttribute("level", result.getData().getLevel());
            model.addAttribute("parentId", result.getData().getParentId());
            model.addAttribute("type", result.getData().getType());
            model.addAttribute("pic", ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.basedata, result.getData().getPic()));
        }

        return NAVIGATION_NAV_CATEGORY_INFO;
    }

    /**
     * 添加品牌分类Ajax
     *
     * @param request
     * @param category
     * @return
     */
    @ResponseBody
    @RequestMapping("/doAddBrandAjax")
    public ResultInfo doAddAjax(HttpServletRequest request, NavigationCategory category) {
        UserInfo user = getUserInfo();
        category = JSON.parseObject(request.getParameter("param"), NavigationCategory.class);
        if (category.getId() == null) {
            category.setType(NavigationType.BRAND.getCode());
            ResultInfo result = navigationCategoryProxy.add(category, user);
            return result;
        } else {
            ResultInfo result = navigationCategoryProxy.update(category, user);
            return result;
        }
    }

    /**
     * 品牌分类添加筛选条件
     *
     * @param catId
     * @param model
     * @param subTableId
     * @return
     * @throws Exception
     */
    @RequestMapping("/navBrandRangeAdd")
    public String navRangeAdd(Long catId, Model model, String subTableId) throws Exception {
        model.addAttribute("categoryId", catId);
        model.addAttribute("subTableId", subTableId);

        ResultInfo<NavigationCategory> categoryRes = navigationCategoryProxy.queryById(catId);
        if (categoryRes.isSuccess() && categoryRes.getData() != null) {
            model.addAttribute("type", categoryRes.getData().getType());
        }

        ItemDto itemDto = itemProxy.initSpu(model, null);
        NavigationCategoryRange range = new NavigationCategoryRange();
        range.setCategoryId(catId);
        ResultInfo<List<NavigationCategoryRange>> rangeResult = navigationCategoryRangeProxy.queryByParam(BeanUtil.beanMap(range));
        List<NavigationCategoryRange> rangeList = rangeResult.getData();

        ResultInfo<Integer> sortResult = navigationCategoryProxy.getMaxSort(catId, NavigationType.BRAND.getCode());
        model.addAttribute("baseSort", sortResult.getData());

        StringBuilder brandBuilder = new StringBuilder();
        StringBuilder categoryBuilder = new StringBuilder();
        for (NavigationCategoryRange r : rangeList) {
            if (r.getType() == NavigationType.BRAND.getCode()) {
                brandBuilder.append(r.getContent()).append(",");
            } else if (r.getType() == NavigationType.CATEGORY.getCode()) {
                categoryBuilder.append(r.getContent().replaceAll(",", "-")).append(",");
            }
        }
        model.addAttribute("rangeList", JSONArray.toJSONString(rangeList).replaceAll("\"", "\'"));

        model.addAttribute("brands", brandBuilder);
        model.addAttribute("categories", categoryBuilder);

        model.addAttribute("item", itemDto);
        return NAVIGATION_NAV_BRAND_RANGE;
    }


    /**
     * 添加筛选条件Ajax
     *
     * @param request
     * @param categoryId
     * @return
     */
    @ResponseBody
    @RequestMapping("/doNavBrandRangeAddAjax")
    public ResultInfo doNavBrandRangeAddAjax(HttpServletRequest request, Long categoryId) {
        if (categoryId == null) {
            return new ResultInfo(new FailInfo("分类Id为空"));
        }
        String brandsStr = request.getParameter("brands");
        List<BrandRange> brands = JSONArray.parseArray(brandsStr, BrandRange.class);
        if (CollectionUtils.isEmpty(brands)) {
            return new ResultInfo(new FailInfo("请至少填写一条品牌信息"));
        }

        ResultInfo result = navigationCategoryRangeProxy.add(brands, categoryId, getUserInfo());
        return result;
    }


}
