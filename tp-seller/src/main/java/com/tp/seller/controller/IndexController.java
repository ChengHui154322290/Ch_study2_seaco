package com.tp.seller.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


/**
 * 首页controller
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public ModelAndView defaultIndex(){
    	ModelAndView mav = new ModelAndView();
    	mav.setViewName("redirect:/seller/index");
    	return mav;
    }

    /**
     * 首页
     * 
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index() {
        final ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/seller/index");
        return mav;
    }

    @RequestMapping("/seller/index")
    public ModelAndView sellerIndex() {
        final ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }

    @RequestMapping("/seller/index_content")
    public ModelAndView sellerIndexContent() {
        final ModelAndView mav = new ModelAndView();
        mav.setViewName("index_content");
        return mav;
    }

}
