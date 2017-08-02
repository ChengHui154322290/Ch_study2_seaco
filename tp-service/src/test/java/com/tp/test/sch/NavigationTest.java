package com.tp.test.sch;

import com.tp.dto.sch.Nav;
import com.tp.dto.sch.NavBrandDTO;
import com.tp.dto.sch.NavBrandSimple;
import com.tp.dto.sch.NavCategoryDTO;
import com.tp.service.bse.INavigationCategoryService;
import com.tp.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by ldr on 2016/2/25.
 */
public class NavigationTest extends BaseTest {

    @Autowired
    private INavigationCategoryService navigationCategoryService;

    @Test
    public void testNavigation() {
        List<NavCategoryDTO> list = navigationCategoryService.getNavigationCategory();
        for (NavCategoryDTO navCateDTO : list) {
            System.out.println(navCateDTO.getName());
            for (NavCategoryDTO navCateDTO1 : navCateDTO.getChild()) {
                System.out.println("----" + navCateDTO1);
            }
        }


    }

    @Test
    public void testNavigationBrand() {
        List<NavBrandDTO> list = navigationCategoryService.getNavigationBrand();
        for (NavBrandDTO dto : list) {
            System.out.println(dto.getName());
            for (NavBrandSimple brandDTO : dto.getBrands()) {
                System.out.println("----" + brandDTO);
            }
        }


    }

    @Test
    public void testNav() throws InterruptedException {
        for (int i = 0; i < 100000; i++) {
            long b = System.currentTimeMillis();
            Thread.sleep(100);
            Nav nav =navigationCategoryService.getNav();

            System.out.println("time cost " + (System.currentTimeMillis() - b-100)+"   "+ nav);
        }
    }

    @Test
    public void testNav5() throws InterruptedException {
        navigationCategoryService.clearNav();
            Nav nav =navigationCategoryService.getNav();

            System.out.println( nav);

    }

}
