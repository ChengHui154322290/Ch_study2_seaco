package com.tp.service.bse;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.util.ImageUtil;
import com.tp.common.util.mmp.AssertUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.PageInfo;
import com.tp.dao.bse.NavigationCategoryDao;
import com.tp.dao.bse.NavigationCategoryRangeDao;
import com.tp.dto.sch.Nav;
import com.tp.dto.sch.NavBrandDTO;
import com.tp.dto.sch.NavBrandSimple;
import com.tp.dto.sch.NavCategoryDTO;
import com.tp.dto.sch.enums.NavigationType;
import com.tp.exception.ServiceException;
import com.tp.model.bse.Brand;
import com.tp.model.bse.NavigationCategory;
import com.tp.model.bse.NavigationCategoryRange;
import com.tp.model.usr.UserInfo;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.BaseService;
import com.tp.service.bse.IBrandService;
import com.tp.service.bse.INavigationCategoryService;
import com.tp.util.BeanUtil;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.List;

@Service
public class NavigationCategoryService extends BaseService<NavigationCategory> implements INavigationCategoryService {

    private static final String NAV_CACHE_KEY = "NAV_CACHE_KEY_";

    @Autowired
    private NavigationCategoryDao navigationCategoryDao;

    @Autowired
    private NavigationCategoryRangeDao navigationCategoryRangeDao;

    @Autowired
    private IBrandService brandService;

    @Autowired
    private JedisCacheUtil jedisCacheUtil;

    Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public BaseDao<NavigationCategory> getDao() {
        return navigationCategoryDao;
    }

    public void add(NavigationCategory category, UserInfo user) {
        AssertUtil.notNull(category, "参数错误");
        AssertUtil.notNull(user, "用户信息为空");
        AssertUtil.notEmpty(category.getName(), "分类名称为空");
        category.setCode("");
        Date cur = new Date();
        category.setCreateUser(user.getUserName());
        category.setUpdateUser(user.getUserName());
        category.setCreateTime(cur);
        category.setUpdateTime(cur);
        this.insert(category);
    }

    public Integer getMaxSort(Long parentId, Integer type) {
        if (parentId == null) return 0;
        Integer sort = navigationCategoryDao.getMaxSort(parentId, type);
        return sort == null ? 0 : sort > Integer.MAX_VALUE ? Integer.MAX_VALUE : sort;

    }

    public PageInfo<NavigationCategory> queryByPage(NavigationCategory query) {
        Map<String, Object> param = BeanUtil.beanMap(query);
        param.put(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " create_time asc");

        PageInfo<NavigationCategory> page = this.queryPageByParam(param, new PageInfo<>(query.getStartPage(), query.getPageSize()));
        return page;
    }

    public int update(NavigationCategory category, UserInfo user) {
        AssertUtil.notNull(category, "参数错误");
        AssertUtil.notNull(user, "用户信息为空");
        AssertUtil.notEmpty(category.getName(), "分类名称为空");
        category.setCode("");
        Date cur = new Date();
        category.setUpdateUser(user.getUserName());
        category.setUpdateTime(cur);
        int c = this.updateNotNullById(category);
        return c;

    }

    @Override
    public Nav getNav() {
        try {
            Object o = jedisCacheUtil.getCache(NAV_CACHE_KEY);
            if (o != null) {
                return (Nav) o;
            }
        }catch (Exception e){
            LOGGER.error("NAV_LOAD_FROM_CACHE_ERROR:",e);
        }
        LOGGER.info("NAV_LOAD_FROM_DB...");
        List<NavCategoryDTO> navCateDTOs = getNavigationCategory();
        List<NavBrandDTO> navBrandDTOs = getNavigationBrand();
        Nav nav = new Nav(navCateDTOs, navBrandDTOs);

        jedisCacheUtil.setCache(NAV_CACHE_KEY, nav);

        return nav;
    }

    @Override
    public void freshNav() {
        try {
            List<NavCategoryDTO> navCateDTOs = getNavigationCategory();
            List<NavBrandDTO> navBrandDTOs = getNavigationBrand();
            Nav nav = new Nav(navCateDTOs, navBrandDTOs);
            jedisCacheUtil.setCache(NAV_CACHE_KEY, nav);
            LOGGER.info("NAV_CACHE_REFRESHED...");
        } catch (Exception e) {
            LOGGER.error("NAV_CACHE_FRESH_ERROR:", e);
        }
    }

    @Override
    public void clearNav() {
        try {
            jedisCacheUtil.deleteCacheKey(NAV_CACHE_KEY);
            LOGGER.info("NAV_CACHE_CLEARED...");
        } catch (Exception e) {
            LOGGER.error("NAV_CACHE_NAV_CACHE_CLEAR_ERROR:", e);
        }
    }

    @Override
    public List<NavCategoryDTO> getNavigationCategory() {
        NavigationCategory query = new NavigationCategory();
        query.setLevel(1);
        query.setStatus(1);
        query.setType(NavigationType.CATEGORY.getCode());
        List<NavigationCategory> firCateList = this.queryByParam(BeanUtil.beanMap(query));

        query.setLevel(2);
        List<NavigationCategory> secCateList = this.queryByParam(BeanUtil.beanMap(query));

        List<NavCategoryDTO> navCateDTOs = new ArrayList<>();
        for (NavigationCategory category : firCateList) {

            NavCategoryDTO navCateDTO = convertToNavCategoryDTO(category);

            List<NavCategoryDTO> children = getCateChild(secCateList, category);

            navCateDTO.setChild(children);

            navCateDTOs.add(navCateDTO);
        }
        sort(navCateDTOs);
        return navCateDTOs;
    }

    @Override
    public List<NavBrandDTO> getNavigationBrand() {

        List<NavigationCategory> brandCategoryList = getNavigationCategories();

        if (CollectionUtils.isEmpty(brandCategoryList)) {
            return Collections.emptyList();
        }
        List<NavigationCategoryRange> rangeList = getNavigationCategoryRanges(brandCategoryList);

        List<Brand> brandList = getBrands(rangeList);

        List<NavBrandDTO> navBrandDTOList = new ArrayList<>();
        for (NavigationCategory category : brandCategoryList) {
            NavBrandDTO navBrandDTO = convertToNavBrandDTO(category);

            List<NavBrandSimple> brandSimpleList = getBrands(rangeList, brandList, category);

            navBrandDTO.setBrands(brandSimpleList);

            navBrandDTOList.add(navBrandDTO);
        }
        sortNavBrand(navBrandDTOList);

        return navBrandDTOList;
    }

    @Transactional
    public void del(Long id) {
        NavigationCategory category = queryById(id);
        if (category == null) {
            throw new ServiceException("分类不存在或已被删除");
        }
        if (category.getType() == NavigationType.CATEGORY.getCode() && category.getLevel() == 1) {
            List<Long> ids = navigationCategoryDao.getIdsByParentId(id);
            if (!CollectionUtils.isEmpty(ids)) {
                navigationCategoryRangeDao.delByCategoryIds(ids);
                navigationCategoryDao.delByParentId(id);
            }
        }
        navigationCategoryRangeDao.delByCategoryIds(Arrays.asList(id));
        navigationCategoryDao.deleteById(id);
    }

    private List<NavigationCategory> getNavigationCategories() {
        NavigationCategory query = new NavigationCategory();
        query.setStatus(1);
        query.setType(NavigationType.BRAND.getCode());
        return this.queryByParam(BeanUtil.beanMap(query));
    }

    private List<NavigationCategoryRange> getNavigationCategoryRanges(List<NavigationCategory> brandCategoryList) {
        if (CollectionUtils.isEmpty(brandCategoryList)) {
            return Collections.emptyList();
        }

        List<Long> categoryIds = new ArrayList<>();
        for (NavigationCategory category : brandCategoryList) {
            categoryIds.add(category.getId());
        }

        return navigationCategoryRangeDao.queryByCategoryIds(categoryIds);
    }

    private List<Brand> getBrands(List<NavigationCategoryRange> rangeList) {
        List<Long> brandIds = new ArrayList<>();
        for (NavigationCategoryRange range : rangeList) {
            if (NumberUtils.isNumber(range.getContent())) {
                brandIds.add(Long.parseLong(range.getContent()));
            }
        }
        if (brandIds.isEmpty()) {
            return Collections.emptyList();
        }

        return brandService.selectListBrand(brandIds, 1);
    }

    private List<NavBrandSimple> getBrands(List<NavigationCategoryRange> rangeList, List<Brand> brandList, NavigationCategory category) {
        List<NavBrandSimple> brandSimpleList = new ArrayList<>();
        for (NavigationCategoryRange range : rangeList) {
            if (range.getCategoryId().equals(category.getId())) {
                if (!NumberUtils.isNumber(range.getContent())) {
                    continue;
                }
                NavBrandSimple bs = new NavBrandSimple();
                bs.setBrandId(Long.parseLong(range.getContent()));
                getBrandInfo(brandList, range,bs);
                bs.setSort(range.getSort());
                brandSimpleList.add(bs);
            }
        }
        sortNavBrandSimple(brandSimpleList);
        return brandSimpleList;
    }

    private void getBrandInfo(List<Brand> brandList, NavigationCategoryRange range, NavBrandSimple bs) {
        for (Brand brand : brandList) {
            if (brand.getId().equals(Long.parseLong(range.getContent()))) {
                bs.setPic( ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.basedata, brand.getLogo()));
                bs.setName(brand.getName());
            }
        }
    }

    private List<NavCategoryDTO> getCateChild(List<NavigationCategory> secCateList, NavigationCategory category) {
        List<NavCategoryDTO> children = new ArrayList<>();
        for (NavigationCategory secCate : secCateList) {
            if (secCate.getParentId().equals(category.getId())) {
                NavCategoryDTO child = convertToNavCategoryDTO(secCate);
                children.add(child);
            }
        }
        sort(children);
        return children;
    }

    private void sort(List<NavCategoryDTO> children) {
        Collections.sort(children, new Comparator<NavCategoryDTO>() {
            @Override
            public int compare(NavCategoryDTO o1, NavCategoryDTO o2) {
                return o1.getSort().compareTo(o2.getSort());
            }
        });
    }

    private void sortNavBrand(List<NavBrandDTO> children) {
        Collections.sort(children, new Comparator<NavBrandDTO>() {
            @Override
            public int compare(NavBrandDTO o1, NavBrandDTO o2) {
                return o1.getSort().compareTo(o2.getSort());
            }
        });
    }

    private void sortNavBrandSimple(List<NavBrandSimple> children) {
        Collections.sort(children, new Comparator<NavBrandSimple>() {
            @Override
            public int compare(NavBrandSimple o1, NavBrandSimple o2) {
                int res = o1.getSort().compareTo(o2.getSort());
                return res;
            }
        });
    }

    private NavCategoryDTO convertToNavCategoryDTO(NavigationCategory category) {
        NavCategoryDTO dto = new NavCategoryDTO();
        dto.setLevel(category.getLevel());
        dto.setStatus(category.getStatus());
        dto.setParentId(category.getParentId());
        dto.setId(category.getId());
        dto.setIsHighlight(category.getIsHighlight());
        dto.setCode(category.getCode());
        dto.setSort(category.getSort());
        dto.setPic(ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.basedata, category.getPic()));
        dto.setName(category.getName());
        dto.setIsPublish(category.getIsPublish());


        return dto;
    }

    private NavBrandDTO convertToNavBrandDTO(NavigationCategory category) {
        NavBrandDTO dto = new NavBrandDTO();
        dto.setLevel(category.getLevel());
        dto.setStatus(category.getStatus());
        dto.setParentId(category.getParentId());
        dto.setId(category.getId());
        dto.setIsHighlight(category.getIsHighlight());
        dto.setCode(category.getCode());
        dto.setSort(category.getSort());
        dto.setPic(ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.basedata, category.getPic()));
        dto.setName(category.getName());
        dto.setIsPublish(category.getIsPublish());

        return dto;
    }

}
