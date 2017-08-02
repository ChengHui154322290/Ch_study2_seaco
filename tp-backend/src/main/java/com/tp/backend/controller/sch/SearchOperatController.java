package com.tp.backend.controller.sch;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.dto.common.ResultInfo;
import com.tp.proxy.sch.SearchDataProxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ldr on 2016/3/1.
 */
@Controller
@RequestMapping("/search/operate")
public class SearchOperatController extends AbstractBaseController {

    @Autowired
    private SearchDataProxy searchDataProxy;

    @RequestMapping("/home")
    public String searchOperate(HttpServletRequest request, Model model){

        String operate = request.getParameter("operate");

        model.addAttribute("type" ,operate);


        return "search/operate";
    }

    @ResponseBody
    @RequestMapping("/updateSearchData")
    public ResultInfo updateSearchData(){
        return searchDataProxy.updateSearchData();
    }

    @ResponseBody
    @RequestMapping("/updateDoc")
    public ResultInfo updateDoc(){
        return searchDataProxy.updateDoc();
    }

    @ResponseBody
    @RequestMapping("/updateDataAndDoc")
    public ResultInfo updateDataAndDoc(){
        return searchDataProxy.updateDataAndDoc();
    }

    @ResponseBody
    @RequestMapping("/updateDataAndDocTotal")
    public ResultInfo updateDataAndDocTotal(){
        return searchDataProxy.updateDataAndDocTotal();
    }
    @ResponseBody
    @RequestMapping("/update")
    public ResultInfo update(){
        return searchDataProxy.update();
    }

   @ResponseBody
    @RequestMapping("/clearDoc")
    public ResultInfo clearDoc(){
        return searchDataProxy.clearDoc();
    }

    @ResponseBody
    @RequestMapping("/updateSHOPDoc")
    public ResultInfo updateSHOPDoc(){
        return searchDataProxy.updateSHOPDoc();
    }

    @ResponseBody
    @RequestMapping("/updateSHOPDocTOTAL")
    public ResultInfo updateSHOPDocTOTAL(){
        return searchDataProxy.updateSHOPDocTOTAL();
    }




}
