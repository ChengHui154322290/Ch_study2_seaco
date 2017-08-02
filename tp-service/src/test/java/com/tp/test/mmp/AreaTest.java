package com.tp.test.mmp;

import com.tp.dao.mmp.AreaDao;
import com.tp.model.mmp.Area;
import com.tp.service.mmp.IAreaService;
import com.tp.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ldr on 2015/12/30.
 */
public class AreaTest  extends BaseTest{

    @Autowired
    private AreaDao areaDao;

    @Autowired
    private IAreaService areaService;

    @Test
    public void testAddArea(){
        Area area = new Area();
        area.setName("阿斯顿；发垃圾速度发来");
        //areaDao.insert(area);

        System.out.println( area.getId());

        areaService.insert(area);
        System.out.println(area.getId());
    }
}
