package com.tp.backend.controller.mmp.groupbuy;

import com.alibaba.fastjson.JSON;
import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.util.ImageUtil;
import com.tp.common.vo.Constant;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.TopicStatus;
import com.tp.dto.mmp.groupbuy.Groupbuy;
import com.tp.model.stg.Inventory;
import com.tp.model.stg.InventoryDistribute;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.mmp.groupbuy.GroupbuyProxy;
import com.tp.proxy.stg.InventoryDistributeProxy;
import com.tp.proxy.stg.InventoryProxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by ldr on 2016/3/10.
 */
@Controller
@RequestMapping("/groupbuy")
public class GroupbuyController extends AbstractBaseController {

    @Autowired
    private GroupbuyProxy groupbuyProxy;

    @Autowired
    private InventoryDistributeProxy inventoryDistributeProxy;
    @RequestMapping("/groupbuyInfo")
    public String groupbuyInfo(Model model, Long topicId, Integer mode) {

        if (topicId != null) {
            ResultInfo<Groupbuy> result = groupbuyProxy.getGroupbuyInfo(topicId);
            if(result.getData() !=null){
                String sku = result.getData().getSku();
                Long topicIdForInv = result.getData().getTopicId();

                InventoryDistribute distributeForQuery = new InventoryDistribute();
                distributeForQuery.setSku(sku);
                distributeForQuery.setBizId(String.valueOf(topicIdForInv));
                ResultInfo<List<InventoryDistribute>> distributes = inventoryDistributeProxy.queryByObject(distributeForQuery);
                if(result.isSuccess() && distributes.getData() !=null && distributes.getData().size()>0){
                    InventoryDistribute inventoryDistribute = distributes.getData().get(0);
                    model.addAttribute("inventoryInfo","剩余库存:"+inventoryDistribute.getInventory() +" .已用库存:"+inventoryDistribute.getOccupy());
                }



            }

            model.addAttribute("groupbuy", result.getData());
            model.addAttribute("pic", ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.item,result.getData().getItemPic()));
        }
        if (mode != null && mode.equals(1)) {
            model.addAttribute("mode", 1);
        } else {
            model.addAttribute("mode", 0);
        }
        model.addAttribute("bucketURL", Constant.IMAGE_URL_TYPE.item.url);
        model.addAttribute("bucketname", Constant.IMAGE_URL_TYPE.item.name());

        return "/groupbuy/groupbuyInfo";
    }


    @ResponseBody
    @RequestMapping("/saveGroupbuyInfo")
    public ResultInfo saveGroupbuyInfo(String params) {
        Groupbuy groupbuy = JSON.parseObject(params, Groupbuy.class);
        UserInfo user = getUserInfo();
        groupbuy.setStatus(TopicStatus.EDITING.ordinal());
        ResultInfo result = groupbuyProxy.save(groupbuy, user);
        return result;
    }

    @ResponseBody
    @RequestMapping("/subGroupbuyInfo")
    public ResultInfo subGroupbuyInfo(String params) {
        Groupbuy groupbuy = JSON.parseObject(params, Groupbuy.class);
        UserInfo user = getUserInfo();
        groupbuy.setStatus(TopicStatus.AUDITING.ordinal());
        ResultInfo result = groupbuyProxy.save(groupbuy, user);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/updateGroupbuyStatus", method = RequestMethod.POST)
    public ResultInfo updateGroupbuyStatus(Groupbuy groupbuy) {
        ResultInfo result = groupbuyProxy.updateGroupbuyStatus(groupbuy, getUserInfo());
        return result;
    }

    @RequestMapping("/groupbuyList")
    public String groupbuyList(HttpServletRequest request, Model model, Groupbuy groupbuy, Integer page, Integer size) {
        groupbuy.setStartPage(page);
        groupbuy.setPageSize(size);
        ResultInfo result = groupbuyProxy.queryPageByObj(groupbuy);
        model.addAttribute("result", result);
        model.addAttribute("query", groupbuy);

        return "/groupbuy/groupbuyList";
    }

    @RequestMapping("/groupbuyInventory")
    public String groupbuyInventory(Model model, Long topicId, String sku, Long wareHouseId, Long supplierId) {

        ResultInfo<Map<String, Object>> result = groupbuyProxy.getInventoryInfo(topicId, sku, wareHouseId);
        model.addAttribute("info", result.getData());
        model.addAttribute("topicId", topicId);
        model.addAttribute("sku", sku);
        model.addAttribute("wareHouseId", wareHouseId);
        model.addAttribute("supplierId", supplierId);
        return "/groupbuy/groupbuyInventory";
    }

    @ResponseBody
    @RequestMapping("/groupbuyInventoryAdd")
    public ResultInfo groupbuyInventoryAdd(Model model, Long topicId, String sku, Long wareHouseId, Long supplierId, Integer inventory) {

        ResultInfo result = groupbuyProxy.addInventory(topicId, sku, wareHouseId, supplierId, inventory, getUserInfo().getId());

        return result;
    }

}
