package com.tp.m.vo.groupbuy;

import java.util.List;

/**
 * Created by ldr on 2016/3/22.
 */
public class MyGroupVO {

    private List<GroupbuyGroupVO> launch;

    private List<GroupbuyGroupVO> join;

    public MyGroupVO() {
    }

    public MyGroupVO(List<GroupbuyGroupVO> launch, List<GroupbuyGroupVO> join) {
        this.launch = launch;
        this.join = join;
    }

    public List<GroupbuyGroupVO> getJoin() {
        return join;
    }

    public void setJoin(List<GroupbuyGroupVO> join) {
        this.join = join;
    }

    public List<GroupbuyGroupVO> getLaunch() {
        return launch;
    }

    public void setLaunch(List<GroupbuyGroupVO> launch) {
        this.launch = launch;
    }
}
