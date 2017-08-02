package com.tp.proxy.bse;

import com.tp.dto.common.ResultInfo;
import com.tp.dto.sch.BrandRange;
import com.tp.dto.sch.enums.NavigationType;
import com.tp.model.bse.Brand;
import com.tp.model.bse.Category;
import com.tp.model.bse.NavigationCategoryRange;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.service.IBaseService;
import com.tp.service.bse.IBrandService;
import com.tp.service.bse.ICategoryService;
import com.tp.service.bse.INavigationCategoryRangeService;
import com.tp.service.bse.INavigationCategoryService;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 分类导航类目范围表代理层
 *
 * @author szy
 */
@Service
public class NavigationCategoryRangeProxy extends BaseProxy<NavigationCategoryRange> {

    @Autowired
    private INavigationCategoryRangeService navigationCategoryRangeService;

    @Autowired
    private INavigationCategoryService navigationCategoryService;

    @Autowired
    private IBrandService brandService;

    @Autowired
    private ICategoryService categoryService;

    @Override
    public IBaseService<NavigationCategoryRange> getService() {
        return navigationCategoryRangeService;
    }

    public ResultInfo<List<NavigationCategoryRange>> queryRangesByParam(final Map<String, Object> param) {
        final ResultInfo<List<NavigationCategoryRange>> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                List<NavigationCategoryRange> rangeList = navigationCategoryRangeService.queryByParam(param);
                List<Long> brandIds = new ArrayList<>();
                List<Long> categoryIds = new ArrayList<>();

                for (NavigationCategoryRange r : rangeList) {
                    if (r.getType() == NavigationType.BRAND.getCode()) {
                        brandIds.add(Long.parseLong(r.getContent()));
                    } else if (r.getType() == NavigationType.CATEGORY.getCode()) {
                        String[] cates = r.getContent().split(",");
                        for (String temp : cates) {
                            if (NumberUtils.isNumber(temp)) {
                                categoryIds.add(Long.parseLong(temp));
                            }
                        }
                    }
                }

                List<Brand> brandList;
                if (brandIds.isEmpty()) {
                    brandList = Collections.EMPTY_LIST;
                } else {
                    brandList = brandService.selectListBrand(brandIds, 1);
                }
                List<Category> categoryList;
                if (categoryIds.isEmpty()) {
                    categoryList = Collections.EMPTY_LIST;
                } else {
                    categoryList = categoryService.selectByIdsAndStatus(categoryIds, 1);
                }
                for (NavigationCategoryRange range : rangeList) {
                    int type = range.getType();
                    StringBuilder content = new StringBuilder();
                    if (type == NavigationType.BRAND.getCode()) {
                        content.append("品牌:");
                        content.append(getBrandName(brandList, range));
                    } else if (type == NavigationType.CATEGORY.getCode()) {
                        content.append("分类:");
                        String[] cates = range.getContent().split(",");
                        for (String c : cates) {
                            content.append(getCategoryName(categoryList, c));
                        }
                    }
                    String cc = content.toString();
                    if (cc.endsWith("->")) {
                        cc = cc.substring(0, cc.lastIndexOf("->"));
                    }
                    range.setContent(cc);
                }
                result.setData(rangeList);
            }
        });
        return result;
    }

    public ResultInfo add(final List<String> brands, final List<String> categories, final Long categoryId, final UserInfo user){
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                navigationCategoryRangeService.add(brands,categories,categoryId,user);
            }
        });
        return result;
    }
    public ResultInfo add(final List<BrandRange> brandRangeList, final Long categoryId, final UserInfo user ){
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                navigationCategoryRangeService.add(brandRangeList,categoryId,user);
                navigationCategoryService.clearNav();
            }
        });
        return result;
    }

    String getCategoryName(List<Category> categoryList, String c) {
        if (!NumberUtils.isNumber(c)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Category category : categoryList) {
            if (category.getId().equals(Long.parseLong(c))) {
                sb.append(category.getName());
                sb.append("->");
                return sb.toString();
            }
        }
        return sb.toString();
    }

    String getBrandName(List<Brand> brandList, NavigationCategoryRange range) {
        if (!NumberUtils.isNumber(range.getContent()))
            return "";
        for (Brand brand : brandList) {
            if (brand.getId().equals(Long.parseLong(range.getContent()))) {
                return brand.getName();
            }
        }
        return "";
    }


}
