package com.tp.service.mmp.groupbuy;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.groupbuy.Groupbuy;
import com.tp.dto.mmp.groupbuy.GroupbuyDetail;
import com.tp.dto.mmp.groupbuy.GroupbuyGroupDTO;
import com.tp.model.mmp.GroupbuyGroup;
import com.tp.model.usr.UserInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by ldr on 2016/3/11.
 */
public interface IGroupbuyService {

    /**
     * 保存/更新团购信息
     *
     * @param groupbuy
     * @param user
     * @throws Exception
     */
    void save(Groupbuy groupbuy, UserInfo user) throws Exception;

    /**
     * 分页获取团购信息
     *
     * @param groupbuy
     * @return
     */
    PageInfo<Groupbuy> queryPageByObj(Groupbuy groupbuy);

    /**
     * 根据Id获取团购信息
     *
     * @param id
     * @return
     */
    Groupbuy getGroupbuyInfo(Long id);

    /**
     * 更新团购状态
     *
     * @param groupbuy
     * @param user
     */
    void updateGroupbuyStatus(Groupbuy groupbuy, UserInfo user);

    /**
     * 获取团购详情 for app
     *
     * @param groupbuyId
     * @param groupId
     * @param memberId
     * @return
     */
    GroupbuyDetail getGroupbuyDetail(Long groupbuyId, Long groupId, Long memberId);

    /**
     * 发起团购
     *
     * @param groupbuyId
     * @param memberId
     * @return
     */
    GroupbuyDetail launch(Long groupbuyId, Long memberId);

    /**
     * 参团
     *
     * @param groupbuyId
     * @param groupId
     * @param memberId
     * @return
     */
    GroupbuyDetail join(Long groupbuyId, Long groupId, Long memberId);

    /**
     * 我发起的团
     *
     * @param memberId
     * @return
     */
    List<GroupbuyGroupDTO> myLaunch(Long memberId);

    /**
     * 我参加的团
     *
     * @param memberId
     * @return
     */
    List<GroupbuyGroupDTO> myJoin(Long memberId);

    /**
     * 获取团购库存信息
     *
     * @param topicId
     * @param sku
     * @param wareHouseId
     * @return
     */
    Map<String, Object> getInventoryInfo(Long topicId, String sku, Long wareHouseId);

    /**
     * 添加库存
     *
     * @param topicId
     * @param sku
     * @param wareHouseId
     * @param supplierId
     * @param inventory
     * @param userId
     */
    void addInventory(Long topicId, String sku, Long wareHouseId, Long supplierId, Integer inventory, Long userId);


}
