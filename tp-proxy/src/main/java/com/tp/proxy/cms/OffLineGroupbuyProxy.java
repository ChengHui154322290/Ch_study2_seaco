package com.tp.proxy.cms;

import com.tp.common.util.ImageUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.dto.cms.app.AppIndexAdvertReturnData;
import com.tp.dto.cms.app.AppSingleAllInfoDTO;
import com.tp.dto.common.ResultInfo;
import com.tp.model.mmp.Topic;
import com.tp.model.sup.SupplierShop;
import com.tp.proxy.mmp.callBack.AbstractProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.proxy.sup.SupplierShopProxy;
import com.tp.service.cms.app.IOffLineGroupbuyService;
import com.tp.service.mmp.ITopicService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ldr on 2016/10/12.
 */
@Service
public class OffLineGroupbuyProxy extends AbstractProxy {

    @Autowired
    private IOffLineGroupbuyService offLineGroupbuyService;

    @Autowired
    private SupplierShopProxy supplierShopProxy;

    @Autowired
    private ITopicService topicService;

    public ResultInfo<AppIndexAdvertReturnData> getAdvert() {
        final ResultInfo<AppIndexAdvertReturnData> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                AppIndexAdvertReturnData data = offLineGroupbuyService.getAdvert();
                result.setData(data);
            }
        });
        return result;
    }

    public ResultInfo<PageInfo<AppSingleAllInfoDTO>> shopList(int startPage, int pageSize) {
        final ResultInfo<PageInfo<AppSingleAllInfoDTO>> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                PageInfo<AppSingleAllInfoDTO> page = offLineGroupbuyService.shopList(startPage, pageSize);
                setShopName(page);

                result.setData(page);
            }
        });
        return result;
    }

    private void setShopName(PageInfo<AppSingleAllInfoDTO> page) {
        if (page == null || CollectionUtils.isEmpty(page.getRows())) return;
        List<Long> supplierIds = new ArrayList<>();
        List<Long> topicIds = new ArrayList<Long>();
        for (AppSingleAllInfoDTO appSingleAllInfoDTO : page.getRows()) {
            if (appSingleAllInfoDTO.getSpecialid() == null) continue;
            topicIds.add(appSingleAllInfoDTO.getSpecialid());
        }

        if (topicIds.isEmpty()) return;

        List<Topic> topics = topicService.queryTopicInList(topicIds);
        if (CollectionUtils.isEmpty(topicIds)) return;

        for(AppSingleAllInfoDTO dto : page.getRows()){
            for(Topic topic: topics){
                if(dto.getSpecialid().equals(topic.getId())){
                    dto.setSupplier(topic.getSupplierId());

                    break;
                }
            }
        }

        for (Topic topic : topics) {
            if (topic.getSupplierId() == null) continue;
            supplierIds.add(topic.getSupplierId());
        }


        ResultInfo<List<SupplierShop>> resultInfo = supplierShopProxy.queryBySupplierIds(supplierIds);
        if (resultInfo.isSuccess() && !CollectionUtils.isEmpty(resultInfo.getData())) {
            for (AppSingleAllInfoDTO appSingleAllInfoDTO : page.getRows()) {
                if (appSingleAllInfoDTO.getSupplier() == null) continue;
                for (SupplierShop supplierShop : resultInfo.getData()) {
                    if (appSingleAllInfoDTO.getSupplier().equals(supplierShop.getSupplierId())) {
                        appSingleAllInfoDTO.setShopName(supplierShop.getShopName());
                        appSingleAllInfoDTO.setShopLogo(ImageUtil.getImgFullUrl( Constant.IMAGE_URL_TYPE.item,supplierShop.getLogoPath()));
                        break;
                    }
                }
            }
        }

    }

}
