package com.tp.backend.controller.mmp.groupbuy;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.groupbuy.GroupbuyGroupJoinStatus;
import com.tp.model.mmp.GroupbuyGroup;
import com.tp.model.mmp.GroupbuyJoin;
import com.tp.proxy.mmp.groupbuy.GroupbuyGroupProxy;
import com.tp.proxy.mmp.groupbuy.GroupbuyJoinProxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by ldr on 2016/3/21.
 */
@Controller
@RequestMapping("/groupbuy")
public class GroupbuyGroupController extends AbstractBaseController {

    @Autowired
    private GroupbuyGroupProxy groupbuyGroupProxy;

    @Autowired
    private GroupbuyJoinProxy groupbuyJoinProxy;


    @RequestMapping("/groupbuyGroupList")
    public String list(GroupbuyGroup query, Integer page, Integer size, Model model) {

        model.addAttribute("query", query);

        if (page != null) query.setStartPage(page);
        if (size != null) query.setPageSize(size);

        ResultInfo<PageInfo<GroupbuyGroup>> result = groupbuyGroupProxy.query(query);

        model.addAttribute("result", result);

        return "/groupbuy/groupbuyGroupList";
    }

    @RequestMapping("/groupbuyJoinList")
    public String joinList(Long groupId,Model model) {

        GroupbuyJoin groupbuyJoin = new GroupbuyJoin();
        groupbuyJoin.setGroupId(groupId);
        ResultInfo<List<GroupbuyJoin>> result = groupbuyJoinProxy.queryByObject(groupbuyJoin);
        model.addAttribute("result",result);

        return "/groupbuy/groupbuyJoinList";
    }

}
