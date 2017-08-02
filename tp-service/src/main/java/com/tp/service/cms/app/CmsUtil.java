package com.tp.service.cms.app;

import com.tp.common.vo.Constant;
import com.tp.common.vo.cms.AdvertTypeAPPEnum;
import com.tp.dto.cms.app.AppAdvertiseInfoDTO;
import com.tp.dto.cms.app.AppSingleInfoDTO;
import com.tp.model.cms.PictureElement;

import java.util.List;

/**
 * Created by ldr on 2016/10/12.
 */
public class CmsUtil {

    public static void processData(List<PictureElement> lst, List<AppAdvertiseInfoDTO<Object>> appAdvertiseInfoDTO) {
        for(int i=0;i<lst.size();i++){
            PictureElement ssmode = lst.get(i);
            AppAdvertiseInfoDTO<Object> csmode = new AppAdvertiseInfoDTO<Object>();
            csmode.setImageurl(Constant.IMAGE_URL_TYPE.cmsimg.url+ssmode.getPicSrc());
            //链接
            csmode.setLinkurl(ssmode.getLink());
            //sku
            csmode.setSku(ssmode.getSku());
            //活动类型
            csmode.setType(ssmode.getActtype());

            if(AdvertTypeAPPEnum.ACT_ITEMSKU.getValue().equals(ssmode.getActtype())){
                AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
                appSingleInfoDTO.setSku(ssmode.getSku());
                appSingleInfoDTO.setSpecialid(ssmode.getActivityid());
                csmode.setInfo(appSingleInfoDTO);
            }else if(AdvertTypeAPPEnum.ACTIVITYID.getValue().equals(ssmode.getActtype())){
                if(ssmode.getActivityid() != null){
                    AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
                    appSingleInfoDTO.setSpecialid(ssmode.getActivityid());
                    csmode.setInfo(appSingleInfoDTO);
                }
            }else{
                AppSingleInfoDTO appSingleInfoDTO = new AppSingleInfoDTO();
                appSingleInfoDTO.setText(ssmode.getLink());
                csmode.setInfo(appSingleInfoDTO);
            }

            appAdvertiseInfoDTO.add(csmode);
        }
    }
}
