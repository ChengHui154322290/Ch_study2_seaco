package com.tp.m.ao.promotion;

import com.alibaba.fastjson.JSON;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.groupbuy.*;
import com.tp.m.base.MResultVO;
import com.tp.m.base.Page;
import com.tp.m.convert.ProductConvert;
import com.tp.m.enums.ImgEnum;
import com.tp.m.enums.MResultInfo;
import com.tp.m.helper.ImgHelper;
import com.tp.m.helper.PropertiesHelper;
import com.tp.m.query.promotion.QueryGroupbuy;
import com.tp.m.util.NumberUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.groupbuy.GroupbuyDetailVO;
import com.tp.m.vo.groupbuy.GroupbuyGroupVO;
import com.tp.m.vo.groupbuy.GroupbuyVO;
import com.tp.m.vo.groupbuy.MyGroupVO;
import com.tp.proxy.mmp.groupbuy.GroupbuyFacadeProxy;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ldr on 2016/3/21.
 */
@Service
public class GroupbuyAO {

    @Autowired
    private GroupbuyFacadeProxy groupbuyFacadeProxy;

    @Autowired
    PropertiesHelper propertiesHelper;


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public MResultVO<GroupbuyDetailVO> detail(QueryGroupbuy query) {
        try {
            Long groupId = StringUtils.isBlank(query.getGid()) ? null : Long.parseLong(query.getGid());
            Long groupbuyId = Long.parseLong(query.getGbid());
            ResultInfo<GroupbuyDetail> result = groupbuyFacadeProxy.getGroupbuyInfo(groupbuyId, groupId, query.getUserid());
            if (!result.isSuccess()) {
                logger.error("M_GET_GROUPBUY_DETAIL_ERROR,RESULT=" + JSON.toJSONString(result));
                return new MResultVO<>(MResultInfo.FAILED);
            }
            GroupbuyDetailVO groupbuyDetailVO = getGroupbuyDetailVO(result);
            return new MResultVO<>(MResultInfo.SUCCESS, groupbuyDetailVO);
        } catch (Exception e) {
            logger.error("M_GET_GROUPBUY_DETAIL_ERROR", e);
            return new MResultVO<>(MResultInfo.FAILED);
        }

    }

    public MResultVO<GroupbuyDetailVO> launch(QueryGroupbuy query) {
        try {
            ResultInfo<GroupbuyDetail> result = groupbuyFacadeProxy.launth(Long.parseLong(query.getGbid()), query.getUserid());
            if (!result.isSuccess()) {
                return new MResultVO<>(result.getMsg().getMessage());
            }
            GroupbuyDetailVO groupbuyDetailVO = getGroupbuyDetailVO(result);
            return new MResultVO<>(MResultInfo.SUCCESS, groupbuyDetailVO);
        } catch (Exception e) {
            logger.error("M_GET_GROUPBUY_LAUNCH_ERROR", e);
            return new MResultVO<>(MResultInfo.FAILED);
        }
    }

    public MResultVO<GroupbuyDetailVO> join(QueryGroupbuy query) {
        try {
            ResultInfo<GroupbuyDetail> result = groupbuyFacadeProxy.join(Long.parseLong(query.getGbid()), Long.parseLong(query.getGid()), query.getUserid());
            if (!result.isSuccess()) {
                return new MResultVO<>(result.getMsg().getMessage());
            }
            GroupbuyDetailVO groupbuyDetailVO = getGroupbuyDetailVO(result);
            return new MResultVO<>(MResultInfo.SUCCESS, groupbuyDetailVO);
        } catch (Exception e) {
            logger.error("M_GET_GROUPBUY_JOIN_ERROR", e);
            return new MResultVO<>(MResultInfo.FAILED);
        }
    }

    public MResultVO<MyGroupVO> my(QueryGroupbuy query) {
        try {
            ResultInfo<MyGroup> result = groupbuyFacadeProxy.myGroup(query.getUserid());
            if (!result.isSuccess()) {
                return new MResultVO<>(result.getMsg().getMessage());
            }

            List<GroupbuyGroupVO> myLaunch = new ArrayList<>();
            List<GroupbuyGroupVO> myJoin = new ArrayList<>();
            for (GroupbuyGroupDTO group : result.getData().getMyLaunch()) {
                GroupbuyGroupVO groupVO = getGroupbuyGroupVO(group);
                myLaunch.add(groupVO);
            }
            for (GroupbuyGroupDTO group : result.getData().getMyJoin()) {
                GroupbuyGroupVO groupVO = getGroupbuyGroupVO(group);
                myJoin.add(groupVO);
            }


            return new MResultVO<>(MResultInfo.SUCCESS, new MyGroupVO(myLaunch, myJoin));
        } catch (Exception e) {
            logger.error("M_GET_GROUPBUY_MY_GROUP_ERROR", e);
            return new MResultVO<>(MResultInfo.FAILED);
        }
    }

    public MResultVO<Page<GroupbuyVO>> list(QueryGroupbuy query) {
        try {
            GroupbuyListDTO groupbuyListDTO = new GroupbuyListDTO();
            groupbuyListDTO.setStartPage(query.getCurpage() == null ? 1 : Integer.parseInt(query.getCurpage()) < 1 ? 1 : Integer.parseInt(query.getCurpage()));
            groupbuyListDTO.setPageSize(10);
            ResultInfo<PageInfo<GroupbuyListDTO>> result = groupbuyFacadeProxy.list(groupbuyListDTO);
            if (!result.isSuccess()) {
                return new MResultVO<>(result.getMsg().getMessage());
            }
            PageInfo<GroupbuyListDTO> pageInfo = result.getData();
            if(CollectionUtils.isEmpty(pageInfo.getRows())) return new MResultVO<>(MResultInfo.SUCCESS,new Page<GroupbuyVO>());
            List<GroupbuyVO> groupbuyVOList = new ArrayList<>();
            convert(result, groupbuyVOList);
            Page<GroupbuyVO> page = new Page<>();
            page.setCurpage(pageInfo.getPage());
            page.setPagesize(pageInfo.getSize());
            page.setList(groupbuyVOList);
            page.setTotalcount(pageInfo.getRecords());
            page.setTotalpagecount(pageInfo.getTotal());

        return new MResultVO<>(MResultInfo.SUCCESS,page);

        } catch (Exception e) {
            logger.error("GET_GROUPBUY_LIST_ERROR", e);
            return new MResultVO<>(MResultInfo.FAILED);
        }


    }

    public MResultVO<Page<GroupbuyVO>> listForIndex(QueryGroupbuy query) {
        try {
            GroupbuyListDTO groupbuyListDTO = new GroupbuyListDTO();
            groupbuyListDTO.setStartPage(query.getCurpage() == null ? 1 : Integer.parseInt(query.getCurpage()) < 1 ? 1 : Integer.parseInt(query.getCurpage()));
            groupbuyListDTO.setPageSize(10);
            groupbuyListDTO.setForIndex(true);
            ResultInfo<PageInfo<GroupbuyListDTO>> result = groupbuyFacadeProxy.list(groupbuyListDTO);
            if (!result.isSuccess()) {
                return new MResultVO<>(result.getMsg().getMessage());
            }
            PageInfo<GroupbuyListDTO> pageInfo = result.getData();
            if(CollectionUtils.isEmpty(pageInfo.getRows())) return new MResultVO<>(MResultInfo.SUCCESS,new Page<GroupbuyVO>());
            List<GroupbuyVO> groupbuyVOList = new ArrayList<>();
            convert(result, groupbuyVOList);
            Page<GroupbuyVO> page = new Page<>();
            page.setCurpage(pageInfo.getPage());
            page.setPagesize(pageInfo.getSize());
            page.setList(groupbuyVOList);
            page.setTotalcount(pageInfo.getRecords());
            page.setTotalpagecount(pageInfo.getTotal());

            return new MResultVO<>(MResultInfo.SUCCESS,page);

        } catch (Exception e) {
            logger.error("GET_GROUPBUY_LIST_ERROR", e);
            return new MResultVO<>(MResultInfo.FAILED);
        }


    }

    private void convert(ResultInfo<PageInfo<GroupbuyListDTO>> result, List<GroupbuyVO> groupbuyVOList) {
        for (GroupbuyListDTO g : result.getData().getRows()) {
            GroupbuyVO groupbuyVO = new GroupbuyVO();
            groupbuyVO.setGbid(StringUtil.getStrByObj(g.getGroupbuyId()));
            groupbuyVO.setTid(StringUtil.getStrByObj(g.getTopicId()));
            groupbuyVO.setSku(g.getSku());
            groupbuyVO.setChannel(g.getChannel());
            groupbuyVO.setCountryname(g.getCountryName());
            groupbuyVO.setCountryimg(ImgHelper.getImgUrl(g.getCountryImg(), ImgEnum.Width.WIDTH_30));
            groupbuyVO.setName(g.getTopicName());
            groupbuyVO.setFeature(g.getFeature());
            groupbuyVO.setGroupprice(NumberUtil.sfwr(g.getGroupPrice()));
            groupbuyVO.setSaleprice(NumberUtil.sfwr(g.getSalePrice()));
            groupbuyVO.setGrouptype(StringUtil.getStrByObj(g.getGroupType()));
            groupbuyVO.setStarttime(g.getStartTime() == null ? "" : String.valueOf(g.getStartTime().getTime()));
            groupbuyVO.setEndtime(g.getEndTime() == null ? "" : String.valueOf(g.getEndTime().getTime()));
            groupbuyVO.setPa(StringUtil.getStrByObj(g.getPlanAmount()));
            groupbuyVO.setImgurl(ImgHelper.getImgUrl(g.getItemImg(), ImgEnum.Width.WIDTH_750));
            groupbuyVOList.add(groupbuyVO);
        }
    }

    private GroupbuyGroupVO getGroupbuyGroupVO(GroupbuyGroupDTO group) {
        GroupbuyGroupVO groupVO = new GroupbuyGroupVO();
        groupVO.setGid(StringUtil.getStrByObj(group.getId()));
        groupVO.setGbid(StringUtil.getStrByObj(group.getGroupbuyId()));
        groupVO.setPa(StringUtil.getStrByObj(group.getPlanAmount()));
        groupVO.setFa(StringUtil.getStrByObj(group.getFactAmount()));
        groupVO.setCode(StringUtil.getStrByObj(group.getCode()));
        // groupVO.setMemberId(StringUtil.getStrByObj(group.getMemberId()));
        groupVO.setName(StringUtil.getStrByObj(group.getTopicName()));
        groupVO.setMemberName(StringUtil.mobile(group.getMemberName()));
        groupVO.setStatus(StringUtil.getStrByObj(group.getStatus()));
        groupVO.setTid(StringUtil.getStrByObj(group.getTopicId()));
        groupVO.setPic(ImgHelper.getImgUrl(group.getPic(), ImgEnum.Width.WIDTH_180));
        groupVO.setSprice(NumberUtil.sfwr(group.getSalePrice()));
        groupVO.setGprice(NumberUtil.sfwr(group.getGroupPrice()));
        return groupVO;
    }


    private GroupbuyDetailVO getGroupbuyDetailVO(ResultInfo<GroupbuyDetail> result) {
        GroupbuyDetail detail = result.getData();
        GroupbuyDetailVO groupbuyDetailVO = new GroupbuyDetailVO();

        String shareHost = propertiesHelper.readValue("share_host");
        if(StringUtils.isBlank(shareHost)){
            shareHost="http://m.51seaco.com";
        }
        StringBuilder builder = new StringBuilder().append(shareHost).append("/tuan.html?gbid=").append(detail.getGroupbuyId());
        if(detail.getGroupId()!=null){
            builder.append("&gid=").append(detail.getGroupId());
        }

        groupbuyDetailVO.setProduct(ProductConvert.convertProductDetail(detail.getItem(), builder.toString(), result.getData().getTopicId().toString()));
        groupbuyDetailVO.setGbid(StringUtil.getStrByObj(detail.getGroupbuyId()));
        groupbuyDetailVO.setTid(StringUtil.getStrByObj(detail.getTopicId()));
        groupbuyDetailVO.setDetail(detail.getDetail());
        groupbuyDetailVO.setFa(StringUtil.getStrByObj(detail.getFactAmount()));
        groupbuyDetailVO.setGstatus(StringUtil.getStrByObj(detail.getGroupStatus()));
        groupbuyDetailVO.setJstatus(StringUtil.getStrByObj(detail.getJoinStatus()));
        groupbuyDetailVO.setGid(StringUtil.getStrByObj(detail.getGroupId()));
        groupbuyDetailVO.setLstatus(StringUtil.getStrByObj(detail.getLaunchStatus()));
        groupbuyDetailVO.setLsecond(StringUtil.getStrByObj(detail.getLeftSecond()));
        groupbuyDetailVO.setPstatus(StringUtil.getStrByObj(detail.getPayStatus()));
        groupbuyDetailVO.setPa(StringUtil.getStrByObj(detail.getPlanAmount()));
        groupbuyDetailVO.setTstatus(StringUtil.getStrByObj(detail.getTopicStats()));
        groupbuyDetailVO.setGtype(StringUtil.getStrByObj(detail.getGroupType()));
        return groupbuyDetailVO;
    }


}
