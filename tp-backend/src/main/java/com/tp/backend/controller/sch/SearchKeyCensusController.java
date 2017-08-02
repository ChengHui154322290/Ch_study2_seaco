package com.tp.backend.controller.sch;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.sch.SearchKeyCensusQuery;
import com.tp.model.sch.SearchKeyCensus;
import com.tp.proxy.sch.SearchKeyCensusProxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by ldr on 2016/3/4.
 */
@Controller
@RequestMapping("/search/census")
public class SearchKeyCensusController extends AbstractBaseController {

    @Autowired
    private SearchKeyCensusProxy searchKeyCensusProxy;

    @RequestMapping("/home")
    public String home(Model model, SearchKeyCensusQuery query,Integer page,Integer size){

        if(page == null) page =1;
        if(size==null) size=10;

        if(query.getSearchType() == null){
            query.setSearchType(1);
        }
        query.setStartPage(page);
        query.setPageSize(size);

        ResultInfo<PageInfo<SearchKeyCensus>> result = searchKeyCensusProxy.multiQuery(query);

        model.addAttribute("page",result.getData());
        model.addAttribute("query",query);

        return "search/census/home";

    }
}
