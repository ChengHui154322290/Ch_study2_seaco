package com.tp.dto.mmp.groupbuy;

import java.util.List;

import com.tp.model.BaseDO;
import com.tp.model.mmp.GroupbuyGroup;

/**
 * Created by ldr on 2016/3/17.
 */
public class MyGroup extends BaseDO {

    private static final long serialVersionUID = -5406920975645899864L;

    private List<GroupbuyGroupDTO> myLaunch;

    private List<GroupbuyGroupDTO> myJoin;

    public List<GroupbuyGroupDTO> getMyLaunch() {
        return myLaunch;
    }

    public void setMyLaunch(List<GroupbuyGroupDTO> myLaunch) {
        this.myLaunch = myLaunch;
    }

    public List<GroupbuyGroupDTO> getMyJoin() {
        return myJoin;
    }

    public void setMyJoin(List<GroupbuyGroupDTO> myJoin) {
        this.myJoin = myJoin;
    }
}
