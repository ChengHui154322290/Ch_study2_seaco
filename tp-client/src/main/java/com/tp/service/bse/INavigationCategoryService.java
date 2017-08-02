package com.tp.service.bse;

import com.tp.common.vo.PageInfo;
import com.tp.dto.sch.Nav;
import com.tp.dto.sch.NavBrandDTO;
import com.tp.dto.sch.NavCategoryDTO;
import com.tp.model.bse.NavigationCategory;
import com.tp.model.usr.UserInfo;
import com.tp.service.IBaseService;

import java.util.List;

/**
 * @author szy
 *         分类导航类目表接口
 */
public interface INavigationCategoryService extends IBaseService<NavigationCategory> {

    /**
     * 添加分类
     * @param category
     * @param user
     */
    void add(NavigationCategory category, UserInfo user);

    /**
     * 获取排序
     * @param parentId
     * @param type
     * @return
     */
    Integer getMaxSort(Long parentId, Integer type);

    /**
     * 分页获取分类信息
     * @param query
     * @return
     */
    PageInfo<NavigationCategory> queryByPage(NavigationCategory query);

    /**
     * 更新分类信息
     * @param category
     * @param user
     * @return
     */
    int update(NavigationCategory category, UserInfo user);

    /**
     * 获取导航的分类信息
     * @return
     */
    List<NavCategoryDTO> getNavigationCategory();

    /**
     * 获取导航的品牌信息
     * @return
     */
    List<NavBrandDTO> getNavigationBrand();

    /**
     * 获取分类导航信息
     * @return
     */
    Nav getNav();

    /**
     * 刷新分类导航的缓存信息
     */
    void freshNav();

    /**
     * 清空分类导航的缓存信息
     */
    void clearNav();

    /**
     * 删除分类信息
     * @param id
     */
   void del(Long id);


}
