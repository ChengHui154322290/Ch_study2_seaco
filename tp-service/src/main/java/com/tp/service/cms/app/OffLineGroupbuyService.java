package com.tp.service.cms.app;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.cms.AppTaiHaoTempletConstant;
import com.tp.common.vo.cms.OffLineGroupbuyTemplateConstant;
import com.tp.dto.cms.app.AppAdvertiseInfoDTO;
import com.tp.dto.cms.app.AppIndexAdvertReturnData;
import com.tp.dto.cms.app.AppSingleAllInfoDTO;
import com.tp.model.cms.ActivityElement;
import com.tp.model.cms.PictureElement;
import com.tp.service.cms.app.IHaitaoAppService;
import com.tp.service.cms.app.IOffLineGroupbuyService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ldr on 2016/10/12.
 */
@Service
public class OffLineGroupbuyService implements IOffLineGroupbuyService {

    @Autowired
   private IHaitaoAppService haitaoAppService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public AppIndexAdvertReturnData getAdvert() throws Exception {
        AppIndexAdvertReturnData returnDate = new AppIndexAdvertReturnData();

        List<PictureElement> lst = new ArrayList<PictureElement>();
        try {
            lst = (List<PictureElement>)  haitaoAppService.queryPageTempletElementInfo(OffLineGroupbuyTemplateConstant.OFF_LINE_GROUP_BUY,OffLineGroupbuyTemplateConstant.OFF_LINE_GROUP_BUY_ADVERT);
        } catch (Exception e) {
            logger.error("[OFF_LINE_GROUP_BUY_QUERY_ADVERT_ERROR]", e);
            e.printStackTrace();
        }
        if(lst==null || lst.size()==0){
            return null;
        }

        //封装数据
        List<AppAdvertiseInfoDTO<Object>> appAdvertiseInfoDTO = new ArrayList<>();
        CmsUtil.processData(lst, appAdvertiseInfoDTO);

        returnDate.setCount(lst.size());
        returnDate.setUrls(appAdvertiseInfoDTO);
        return returnDate ;

    }

    @Override
    public PageInfo<AppSingleAllInfoDTO> shopList( int pagestart, int pagesize) {


        List<ActivityElement> lst = new ArrayList<ActivityElement>();
        try {
            List<String> templeCodes = new ArrayList<>();

                templeCodes.add(OffLineGroupbuyTemplateConstant.OFF_LINE_GROUP_BUY_LIST);
                lst = (List<ActivityElement>) haitaoAppService.queryActivityElementsByTempletCodes(null, OffLineGroupbuyTemplateConstant.OFF_LINE_GROUP_BUY,templeCodes, pagestart,pagesize);

        } catch (Exception e) {
            logger.error("[OFF_LINE_GROUP_BUY_LIST_ERROR]", e);
        }
        if(lst==null || lst.size()==0){
            return null;
        }

        List<AppSingleAllInfoDTO> retLst = haitaoAppService.getAppSingleAllInfoDTOs(null, lst,new String[0]);

        PageInfo<AppSingleAllInfoDTO>  page = new PageInfo<AppSingleAllInfoDTO>();
        page.setRecords(retLst.size());//此处暂时不需要总页数,只返回此次的总条数
        page.setRows(retLst);
        page.setPage(pagestart);
        page.setSize(pagesize);
        return page;
    }


}
