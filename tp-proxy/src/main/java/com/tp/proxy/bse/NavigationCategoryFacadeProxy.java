package com.tp.proxy.bse;

import com.tp.dto.common.ResultInfo;
import com.tp.dto.sch.Nav;
import com.tp.dto.sch.NavBrandDTO;
import com.tp.dto.sch.NavCategoryDTO;
import com.tp.proxy.mmp.callBack.AbstractProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.service.bse.INavigationCategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类导航类目表代理层
 *
 * @author szy
 */
@Service
public class NavigationCategoryFacadeProxy extends AbstractProxy {

    @Autowired
    private INavigationCategoryService navigationCategoryService;

    public ResultInfo<Nav> getNavigation() {
        final ResultInfo<Nav> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                Nav nav = navigationCategoryService.getNav();
                result.setData(nav);
            }
        });
        return result;
    }



}
