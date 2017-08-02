package com.tp.proxy.bse;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.exception.ServiceException;
import com.tp.model.bse.NavigationCategory;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.service.IBaseService;
import com.tp.service.bse.INavigationCategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 分类导航类目表代理层
 *
 * @author szy
 */
@Service
public class NavigationCategoryProxy extends BaseProxy<NavigationCategory> {

    @Autowired
    private INavigationCategoryService navigationCategoryService;

    @Override
    public IBaseService<NavigationCategory> getService() {
        return navigationCategoryService;
    }

    public ResultInfo add(final NavigationCategory navigationCategory, final UserInfo user) {
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                navigationCategoryService.add(navigationCategory, user);
                navigationCategoryService.clearNav();
            }
        });
        return result;
    }

    public ResultInfo<Integer> getMaxSort(final Long parentId, final Integer type) {
        final ResultInfo<Integer> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                Integer sort = navigationCategoryService.getMaxSort(parentId, type);
                result.setData(sort);
            }
        });
        return result;
    }

    public ResultInfo<PageInfo<NavigationCategory>> queryByPage(final NavigationCategory query) {
        final ResultInfo<PageInfo<NavigationCategory>> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                PageInfo<NavigationCategory> page = navigationCategoryService.queryByPage(query);
                result.setData(page);
            }
        });
        return result;
    }

    public ResultInfo<NavigationCategory> queryById(final Long id) {
        final ResultInfo<NavigationCategory> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                NavigationCategory category = navigationCategoryService.queryById(id);
                result.setData(category);
            }
        });
        return result;
    }

    public ResultInfo update(final NavigationCategory category, final UserInfo user) {
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                int count = navigationCategoryService.update(category, user);
                if (count == 0)
                    throw new ServiceException("记录不存在");
                navigationCategoryService.clearNav();
            }
        });
        return result;
    }

    public ResultInfo updatePublishStatus(final Long id, final Integer isPublish, final UserInfo user) {
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                NavigationCategory category = new NavigationCategory();
                category.setId(id);
                category.setIsPublish(isPublish);
                category.setUpdateTime(new Date());
                category.setUpdateUser(user.getUserName());
                navigationCategoryService.updateNotNullById(category);
            }
        });
        return result;
    }

    public ResultInfo del(final Long id) {
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                navigationCategoryService.del(id);
                navigationCategoryService.clearNav();
            }
        });
        return result;
    }
}
