package com.tp.proxy.mmp.groupbuy;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.groupbuy.GroupbuyGroupStatus;
import com.tp.dto.mmp.groupbuy.GroupbuyDetail;
import com.tp.dto.mmp.groupbuy.GroupbuyListDTO;
import com.tp.dto.mmp.groupbuy.MyGroup;
import com.tp.model.mmp.GroupbuyInfo;
import com.tp.proxy.mmp.callBack.AbstractProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.service.mmp.groupbuy.IGroupbuyInfoService;
import com.tp.service.mmp.groupbuy.IGroupbuySendMsgService;
import com.tp.service.mmp.groupbuy.IGroupbuyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ldr on 2016/3/11.
 */
@Service
public class GroupbuyFacadeProxy extends AbstractProxy {

    @Autowired
    private IGroupbuyService groupbuyService;

    @Autowired
    private IGroupbuySendMsgService groupbuySendMsgService;

    @Autowired
    private IGroupbuyInfoService groupbuyInfoService;

    /**
     * 获取团购信息
     *
     * @param groupbuyId 团购活动序号
     * @param groupId    用户组号
     * @param memberId   用户Id
     * @return 团购详情
     */
    public ResultInfo<GroupbuyDetail> getGroupbuyInfo(final Long groupbuyId, final Long groupId, final Long memberId) {
        final ResultInfo<GroupbuyDetail> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                GroupbuyDetail groupbuyDTO = groupbuyService.getGroupbuyDetail(groupbuyId, groupId, memberId);
                result.setData(groupbuyDTO);
            }
        });
        return result;
    }

    /**
     * 发起团
     *
     * @param groupbuyId 团购活动号
     * @param memberId   用户Id
     * @return 团购详情
     */
    public ResultInfo<GroupbuyDetail> launth(final Long groupbuyId, final Long memberId) {
        final ResultInfo<GroupbuyDetail> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                GroupbuyDetail groupbuyDetail = groupbuyService.launch(groupbuyId, memberId);
                result.setData(groupbuyDetail);
            }
        });
        return result;
    }

    /**
     * 参团
     *
     * @param groupbuyId 团购活动序号
     * @param groupId    用户团号
     * @param memberId   用户Id
     * @return 团购详情
     */
    public ResultInfo<GroupbuyDetail> join(final Long groupbuyId, final Long groupId, final Long memberId) {
        final ResultInfo<GroupbuyDetail> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                GroupbuyDetail groupbuyDetail = groupbuyService.join(groupbuyId, groupId, memberId);
                if (groupbuyDetail != null && groupbuyDetail.getGroupStatus().equals(GroupbuyGroupStatus.SUCCESS.getCode())) {
                    groupbuySendMsgService.sendMsg(groupbuyDetail.getGroupId());
                }
                result.setData(groupbuyDetail);
            }
        });
        return result;
    }

    /**
     * 我的团
     *
     * @param memberId 用户Id
     * @return 我的团
     */
    public ResultInfo<MyGroup> myGroup(final Long memberId) {
        final ResultInfo<MyGroup> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                MyGroup myGroup = new MyGroup();
                myGroup.setMyJoin(groupbuyService.myJoin(memberId));
                myGroup.setMyLaunch(groupbuyService.myLaunch(memberId));
                result.setData(myGroup);
            }
        });
        return result;
    }

    public ResultInfo<PageInfo<GroupbuyListDTO>> list(GroupbuyListDTO query) {
        final ResultInfo<PageInfo<GroupbuyListDTO>> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                PageInfo<GroupbuyListDTO> pageInfo = groupbuyInfoService.list(query);
                result.setData(pageInfo);
            }
        });
        return result;
    }

}
