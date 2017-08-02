package com.tp.backend.controller.bse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.util.ImageUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.ItemDto;
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
 * 分类导航 分类信息相关Controller
 * Created by ldr on 2016/2/22.
 */

@Controller
@RequestMapping("/basedata/navigation")
public class NavigationCategoryController extends AbstractBaseController {

    public static final String NAVIGATION_NAV_CATEGORY_HOME = "basedata/navigation/navCategoryHome";

    public static final String NAVIGATION_NAV_CATEGORY_INFO = "basedata/navigation/navCategoryInfo";

    public static final String NAVIGATION_NAV_SEC_LEVEL_LIST = "basedata/navigation/navSecLevelList";

    public static final String NAVIGATION_NAV_CATEGORY_RANGE = "basedata/navigation/navCategoryRange";

    @Autowired
    private NavigationCategoryProxy navigationCategoryProxy;

    @Autowired
    private NavigationCategoryRangeProxy navigationCategoryRangeProxy;

    @Autowired
    private ItemProxy itemProxy;

    /**
     * 分类信息展示页面
     * @param request
     * @param model
     * @param query
     * @return
     */
    @RequestMapping("/navHome")
    public String navHome(HttpServletRequest request, Model model, NavigationCategory query) {
        if (query == null) query = new NavigationCategory();
        if (query.getStartPage() == null || query.getStartPage() < 1) query.setStartPage(1);
        if (query.getPageSize() == null || query.getPageSize() < 1) query.setPageSize(10);
        query.setParentId(-1L);
        query.setType(NavigationType.CATEGORY.getCode());
        ResultInfo<PageInfo<NavigationCategory>> result = navigationCategoryProxy.queryByPage(query);
        model.addAttribute("result", result);

        return NAVIGATION_NAV_CATEGORY_HOME;
    }

    /**
     * 分类信息JSON
     * @param request
     * @param model
     * @param query
     * @param parentId
     * @return
     */
    @ResponseBody
    @RequestMapping("/navHomeJSON")
    public Map<String, Object> navHomeJSON(HttpServletRequest request, Model model, NavigationCategory query, Long parentId) {
        query.setStartPage(1);
        query.setPageSize(999);
        query.setParentId(parentId == null ? -1 : parentId);
        query.setType(NavigationType.CATEGORY.getCode());
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
     * 分类添加一级目录展示页
     * @param model
     * @return
     */
    @RequestMapping("/navAddFirstLevel")
    public String navAddFirstLevel(Model model) {
        ResultInfo<Integer> resultSort = navigationCategoryProxy.getMaxSort(-1L, NavigationType.CATEGORY.getCode());

        model.addAttribute("sort", resultSort.getData());
        model.addAttribute("level", 1);
        model.addAttribute("parentId", -1);
        model.addAttribute("type", NavigationType.CATEGORY.getCode());
        model.addAttribute("bucketURL", Constant.IMAGE_URL_TYPE.basedata.url);
        model.addAttribute("bucketname", Constant.IMAGE_URL_TYPE.basedata.name());
        return NAVIGATION_NAV_CATEGORY_INFO;
    }

    /**
     * 分类信息编辑一级分类
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/navEditFirstLevel")
    public String navEditFirstLevel(Model model, Long id) {
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
     * 添加分类信息Ajax
     * @param request
     * @param category
     * @return
     */
    @ResponseBody
    @RequestMapping("/doAddAjax")
    public ResultInfo doAddAjax(HttpServletRequest request, NavigationCategory category) {
        UserInfo user = getUserInfo();
        category = JSON.parseObject(request.getParameter("param"), NavigationCategory.class);
        if (category.getId() == null) {
            category.setType(NavigationType.CATEGORY.getCode());
            ResultInfo result = navigationCategoryProxy.add(category, user);
            return result;
        } else {
            ResultInfo result = navigationCategoryProxy.update(category, user);
            return result;
        }
    }

    /**
     * 更新发布状态
     * @param id
     * @param publish
     * @return
     */
    @Deprecated
    @ResponseBody
    @RequestMapping("/doUpdatePublishStatusAjax")
    public ResultInfo doUpdatePublishStatusAjax(Long id, Integer publish) {
        UserInfo user = getUserInfo();
        ResultInfo result = navigationCategoryProxy.updatePublishStatus(id, publish, user);
        return result;
    }

    /**
     * 二级分类列表
     * @param parentId
     * @param model
     * @return
     */
    @Deprecated
    @RequestMapping("/navSecLevelList")
    public String navSecLecelList(Long parentId, Model model) {
        NavigationCategory category = new NavigationCategory();
        category.setParentId(parentId);
        Map<String, Object> param = BeanUtil.beanMap(category);
        param.put(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " create_time desc");
        ResultInfo<List<NavigationCategory>> result = navigationCategoryProxy.queryByParam(param);
        model.addAttribute("parentId", parentId);
        model.addAttribute("result", result);
        model.addAttribute("list", result.getData());

        return NAVIGATION_NAV_SEC_LEVEL_LIST;
    }

    /**
     * 分类添加二级目录
     * @param model
     * @param parentId
     * @return
     */
    @RequestMapping("/navAddSecLevel")
    public String navAddSecLevel(Model model, Long parentId) {
        ResultInfo<Integer> resultSort = navigationCategoryProxy.getMaxSort(parentId, NavigationType.CATEGORY.getCode());

        model.addAttribute("sort", resultSort.getData());
        model.addAttribute("level", 2);
        model.addAttribute("parentId", parentId);
        model.addAttribute("type", NavigationType.CATEGORY.getCode());
        model.addAttribute("bucketURL", Constant.IMAGE_URL_TYPE.basedata.url);
        model.addAttribute("bucketname", Constant.IMAGE_URL_TYPE.basedata.name());
        return NAVIGATION_NAV_CATEGORY_INFO;
    }

    /**
     * 分类添加筛选信息
     * @param catId
     * @param model
     * @param subTableId
     * @return
     * @throws Exception
     */
    @RequestMapping("/navRangeAdd")
    public String navRangeAdd(Long catId, Model model, String subTableId) throws Exception {
        model.addAttribute("categoryId", catId);
        model.addAttribute("subTableId", subTableId);

        ResultInfo<NavigationCategory> categoryRes = navigationCategoryProxy.queryById(catId);
        if (categoryRes.isSuccess() && categoryRes.getData() != null) {
            model.addAttribute("type", categoryRes.getData().getType());
        }

        System.out.println(catId);
        ItemDto itemDto = itemProxy.initSpu(model, null);
        NavigationCategoryRange range = new NavigationCategoryRange();
        range.setCategoryId(catId);
        ResultInfo<List<NavigationCategoryRange>> rangeResult = navigationCategoryRangeProxy.queryByParam(BeanUtil.beanMap(range));
        List<NavigationCategoryRange> rangeList = rangeResult.getData();

        StringBuilder brandBuilder = new StringBuilder();
        StringBuilder categoryBuilder = new StringBuilder();
        for (NavigationCategoryRange r : rangeList) {
            if (r.getType() == NavigationType.BRAND.getCode()) {
                brandBuilder.append(r.getContent()).append(",");
            } else if (r.getType() == NavigationType.CATEGORY.getCode()) {
                categoryBuilder.append(r.getContent().replaceAll(",", "-")).append(",");
            }
        }

        model.addAttribute("brands", brandBuilder);
        model.addAttribute("categories", categoryBuilder);

        model.addAttribute("item", itemDto);
        return NAVIGATION_NAV_CATEGORY_RANGE;
    }

    /**
     * 分类添加筛选信息Ajax
     * @param request
     * @param categoryId
     * @return
     */
    @ResponseBody
    @RequestMapping("/doNavRangeAddAjax")
    public ResultInfo doNavRangeAddAjax(HttpServletRequest request, Long categoryId) {
        if (categoryId == null) {
            return new ResultInfo(new FailInfo("分类Id为空"));
        }
        String categoriesStr = request.getParameter("categories");
        String brandsStr = request.getParameter("brands");
        List<String> brands = JSONArray.parseArray(brandsStr, String.class);
        List<String> categories = JSONArray.parseArray(categoriesStr, String.class);
        if (CollectionUtils.isEmpty(brands) && CollectionUtils.isEmpty(categories)) {
            return new ResultInfo(new FailInfo("请至少填写一条分类或品牌信息"));
        }
        ResultInfo result = navigationCategoryRangeProxy.add(brands, categories, categoryId, getUserInfo());
        return result;
    }

    /**
     * 分类筛选信息JSON
     * @param cateId
     * @return
     */
    @ResponseBody
    @RequestMapping("/navRangeJSON")
    public Map<String, Object> navRangeJSON(Long cateId) {

        Map<String, Object> data = new HashMap<>();
        NavigationCategoryRange range = new NavigationCategoryRange();
        range.setCategoryId(cateId);
        ResultInfo<List<NavigationCategoryRange>> result = navigationCategoryRangeProxy.queryRangesByParam(BeanUtil.beanMap(range));
        List<NavigationCategoryRange> rangeList = result.getData();

        data.put("rows", rangeList);
        return data;
    }

    /**
     * 删除分类信息
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/delCategory")
    public ResultInfo delCategory(Long id) {
        ResultInfo result = navigationCategoryProxy.del(id);
        return result;
    }
}
