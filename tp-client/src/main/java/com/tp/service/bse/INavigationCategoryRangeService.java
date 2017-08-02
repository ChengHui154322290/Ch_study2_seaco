package com.tp.service.bse;

import com.tp.dto.sch.BrandRange;
import com.tp.model.bse.NavigationCategoryRange;
import com.tp.model.usr.UserInfo;
import com.tp.service.IBaseService;

import java.util.List;

/**
  * @author szy 
  * 分类导航类目范围表接口
  */
public interface INavigationCategoryRangeService extends IBaseService<NavigationCategoryRange>{

    /**
     * 添加类目范围
     * @param brands
     * @param categories
     * @param categoryId
     * @param user
     */
    void add(List<String> brands, List<String> categories, Long categoryId, UserInfo user);

    /**
     * 添加类目范围
     * @param brandRangeList
     * @param categoryId
     * @param user
     */
    void add(List<BrandRange> brandRangeList, Long categoryId, UserInfo user );

}
