package com.tp.proxy.mmp.groupbuy;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.groupbuy.Groupbuy;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.mmp.callBack.AbstractProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.service.mmp.groupbuy.IGroupbuyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by ldr on 2016/3/11.
 */
@Service
public class GroupbuyProxy extends AbstractProxy {

    @Autowired
    private IGroupbuyService groupbuyService;

    public ResultInfo save(final Groupbuy groupbuy, final UserInfo user) {
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                groupbuyService.save(groupbuy, user);
            }
        });
        return result;
    }

    public ResultInfo<PageInfo<Groupbuy>> queryPageByObj(final Groupbuy groupbuy) {
        final ResultInfo<PageInfo<Groupbuy>> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                PageInfo pageInfo = groupbuyService.queryPageByObj(groupbuy);
                result.setData(pageInfo);
            }
        });
        return result;
    }

    public ResultInfo<Groupbuy> getGroupbuyInfo(final Long id) {
        final ResultInfo<Groupbuy> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                Groupbuy groupbuy = groupbuyService.getGroupbuyInfo(id);
                result.setData(groupbuy);
            }
        });
        return result;
    }

    public ResultInfo updateGroupbuyStatus(final Groupbuy groupbuy, final UserInfo userInfo) {
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                groupbuyService.updateGroupbuyStatus(groupbuy, userInfo);
            }
        });
        return result;
    }

    public ResultInfo<Map<String,Object>> getInventoryInfo(Long topicId, String sku, Long wareHouseId){
        final ResultInfo<Map<String,Object>> result = new ResultInfo<>();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                Map<String,Object> info= groupbuyService.getInventoryInfo(topicId,sku,wareHouseId);
                result.setData(info);
            }
        });
        return result;
    }

    public ResultInfo addInventory(Long topicId,String sku,Long wareHouseId,Long supplierId,Integer inventory,Long userId){
        final ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                groupbuyService.addInventory(topicId,sku,wareHouseId,supplierId,inventory,userId);
            }
        });
        return result;
    }
}
