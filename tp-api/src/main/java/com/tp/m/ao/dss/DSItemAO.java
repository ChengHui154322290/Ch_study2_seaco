package com.tp.m.ao.dss;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.distribution.DistributionItem;
import com.tp.dto.mmp.distribution.DistributionItemQuery;
import com.tp.m.base.MResultVO;
import com.tp.m.base.Page;
import com.tp.m.enums.MResultInfo;
import com.tp.m.util.NumberUtil;
import com.tp.m.vo.product.ProductVO;
import com.tp.proxy.dss.DistributionItemProxy;
import com.tp.proxy.dss.GlobalCommisionProxy;
import com.tp.proxy.dss.PromoterInfoProxy;

/**
 * Created by ldr on 2016/4/19.
 */
@Service
public class DSItemAO {

    @Autowired
    private DistributionItemProxy distributionItemProxy;

    private  final Logger logger = LoggerFactory.getLogger(this.getClass());

    public MResultVO<Page<ProductVO>> getDItems(DistributionItemQuery query) {
        try {
            ResultInfo<PageInfo<DistributionItem>> resultInfo = distributionItemProxy.getDistributionItems(query);
            if (!resultInfo.isSuccess())
                return new MResultVO<>(resultInfo.getMsg() == null ? MResultInfo.FAILED.message : resultInfo.getMsg().getMessage());
            PageInfo<DistributionItem> pageInfo = resultInfo.getData();
            if (CollectionUtils.isEmpty(pageInfo.getRows()))
                return new MResultVO<>(MResultInfo.SUCCESS.message, new Page<ProductVO>());
            List<ProductVO> dItemVOList = new ArrayList<>();
            for (DistributionItem i : pageInfo.getRows()) {
                ProductVO vo = new ProductVO();
                vo.setDisamount(NumberUtil.sfwr(i.getDisAmount()));
                vo.setTid(i.getTopicId().toString());
                vo.setSku(i.getSku());
                vo.setImgurl(i.getPic());
                vo.setName(i.getName());
                vo.setPrice(NumberUtil.sfwr(i.getTopicPirce()));
                vo.setOldprice(NumberUtil.sfwr(i.getSalesPrice()));
                dItemVOList.add(vo);
            }
            Page<ProductVO> page = new Page<>();
            page.setList(dItemVOList);
            page.setCurpage(pageInfo.getPage());
            page.setTotalcount(pageInfo.getRecords());
            page.setTotalpagecount(pageInfo.getTotal());

            return new MResultVO<>(MResultInfo.SUCCESS, page);
        } catch (Exception e) {
            logger.error("[DISTRIBUTION_GET_ITEM_LIST_ERROR]", e);
            return new MResultVO<>(MResultInfo.FAILED);
        }
    }
}
