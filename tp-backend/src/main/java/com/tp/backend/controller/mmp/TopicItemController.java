/**
 *
 */
package com.tp.backend.controller.mmp;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.util.ImageUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.mmp.TopicConstant;
import com.tp.dfsutils.util.DfsDomainUtil;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.TopicItemDTO;
import com.tp.dto.mmp.TopicItemInfoDTO;
import com.tp.dto.mmp.enums.DeletionStatus;
import com.tp.dto.stg.InventoryDto;
import com.tp.model.mmp.TopicItem;
import com.tp.model.prd.ItemSku;
import com.tp.model.stg.Warehouse;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.mmp.TopicItemBatchProxy;
import com.tp.proxy.mmp.TopicItemProxy;

import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 */
@Controller
@RequestMapping(value = "/topicItem")
public class TopicItemController extends AbstractBaseController {

    Logger log = LoggerFactory.getLogger(TopicItemController.class);

    @Autowired
    private TopicItemProxy topicItemProxy;

    @Autowired
    private TopicItemBatchProxy topicItemBatchProxy;



    @RequestMapping(value = "/{topicId}/addItem/{spu}/{supplierId}/{sortIndex}", method = RequestMethod.GET)
    public String addTopicItem(@PathVariable Long topicId,
                               @PathVariable String spu, @PathVariable Long supplierId,
                               @PathVariable Integer sortIndex, ModelMap model,
                               HttpServletRequest request) {

        if (null == sortIndex || 0 == sortIndex) {
            sortIndex = 10;
        } else {
            sortIndex += 10;
        }
        model.addAttribute("topicId", topicId);
        if (null != spu && "0" != spu) {
            model.addAttribute("spu", spu.trim());
        } else {
            model.addAttribute("spu", "");
        }
        if (null != supplierId && -1 != supplierId) {
            model.addAttribute("supplierId", supplierId);
        } else {
            model.addAttribute("supplierId", "");
        }
        model.addAttribute("sortIndex", sortIndex);
        return "promotion/topicItemAdd";
    }

    /**
     * 跳转至增加当前库存界面
     *
     * @param topicId
     * @param topicItemId
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/{topicId}/editItem/{topicItemId}", method = RequestMethod.GET)
    public String editTopicItemLoad(@PathVariable Long topicId,
                                    @PathVariable Long topicItemId, ModelMap model,
                                    HttpServletRequest request) {
        TopicItem itemDO = topicItemProxy.getTopicItemByItemId(topicItemId);
        ResultInfo<TopicItem> resultInfo = topicItemProxy.getTopicItemCurrentStock(itemDO);
        model.addAttribute("topicItemInfo", resultInfo.getData());
        model.addAttribute("remainStock", topicItemProxy.getTopicItemRemainStock(itemDO));
        return "promotion/topicItemEdit";
    }

    /**
     * 增加单个锁定库存商品的库存
     *
     * @param topicItemId
     * @param amount
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/editItem", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo editTopicItem(
            @RequestParam("topicItemId") Long topicItemId,
            @RequestParam("amount") int amount, ModelMap model,
            HttpServletRequest request) {

        if (null == topicItemId || 0 == topicItemId) {
            log.error("topic item info error.... id:" + topicItemId);
            return new ResultInfo(new FailInfo("选定专场活动商品无效!"));
        }
        UserInfo user = getUserInfo();
        if (amount > 0) {
            return topicItemProxy.requestAddStock(topicItemId, amount,
                    user.getId(), user.getUserName());
        } else if (amount < 0) {
            return topicItemProxy.requestBackStock(topicItemId, amount,
                    user.getId(), user.getUserName());
        }
        return new ResultInfo();
    }

    /**
     * 跳转至按Barcode检索界面
     *
     * @param itemBarCode
     * @param brandId
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/itemBarCode/search/{barCode}/{brandId}", method = RequestMethod.GET)
    public String searchItemByBarCode(
            @PathVariable("barCode") String itemBarCode,
            @PathVariable("brandId") Long brandId, ModelMap model,
            HttpServletRequest request) {
        if (!"0".equalsIgnoreCase(itemBarCode)) {
            model.addAttribute("itemBarCode", itemBarCode);
        }
        model.addAttribute("parentBrandId", brandId);
        return "promotion/topicItemSearch";
    }

    /**
     * 跳转至按sku检索界面
     *
     * @param sku
     * @param brandId
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/itemSKU/search/{sku}/{brandId}", method = RequestMethod.GET)
    public String searchItemBySKU(@PathVariable("sku") String sku,
                                  @PathVariable("brandId") Long brandId, ModelMap model,
                                  HttpServletRequest request) {
        if (null != sku && !"0".equalsIgnoreCase(sku)) {
            model.addAttribute("sku", sku.trim());
        } else {
            model.addAttribute("sku", "");
        }
        model.addAttribute("parentBrandId", brandId);
        return "promotion/topicItemSearchSKU";
    }

    /**
     * 根据BarCode或者Sku检索商品信息,如果有spu或者brandId信息，也做为检索条件一并检索
     * 按Barcode检索或者按sku检索界面一并使用的查询方法
     *
     * @param barCode
     * @param sku
     * @param spu
     * @param brandId
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/SKU/search", method = RequestMethod.GET)
    public String searchSKU(@RequestParam("barCode") String barCode,
                            @RequestParam("sku") String sku, @RequestParam("spu") String spu,
                            @RequestParam("brandId") Long brandId,
                            @RequestParam("supplierId") Long supplierId, ModelMap model,
                            HttpServletRequest request) {
        List<ItemSku> skuDOs = topicItemProxy.getSKUInfosBySkuOrBarcodeWithBidSpu(sku, barCode, brandId, spu, supplierId);
        model.addAttribute("skus", skuDOs);
        return "promotion/subpages/topicItemSearchList";
    }

    @RequestMapping(value = "/SKUInfo/search", method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo searchSKU(@RequestParam("skuId") Long skuId,
                                ModelMap model, HttpServletRequest request) {

        ResultInfo<TopicItemInfoDTO> result = topicItemProxy.getTopicItemInfoBySkuId(skuId);
        if (null != result.getData()) {
            String file = result.getData().getTopicImage();
            if (!StringUtils.isBlank(file)) {
                result.getData().setImageFullPath(ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.item,file));
            }
        }

        return result;

    }

    @RequestMapping(value = "/item/selectionSupplier/{sku}/{spu}/{supplierId}/{barcode}/{brandId}", method = RequestMethod.GET)
    public String selectionSupplier(@PathVariable("sku") String sku,
                                    @PathVariable("barcode") String barcode,
                                    @PathVariable("supplierId") Long supplierId,
                                    @PathVariable("spu") String spu,
                                    @PathVariable("brandId") Long brandId, ModelMap model,
                                    HttpServletRequest request) {
        List<ItemSku> skuDOs = null;
        if (null != sku && !StringUtils.isBlank(sku) && "0".equals(barcode)) {
            skuDOs = topicItemProxy.getSKUInfosBySkuOrBarcodeWithBidSpu(
                    sku, null, brandId, spu, supplierId);
        } else {
            skuDOs = topicItemProxy.getSKUInfosBySkuOrBarcodeWithBidSpu(
                    null, barcode, brandId, spu, supplierId);
        }
        model.addAttribute("skus", skuDOs);
        return "promotion/topicItemSupplierSelection";
    }

    @RequestMapping(value = "/wh", method = RequestMethod.GET)
    public String searchItemWarehouse(
            @RequestParam("supplierId") Long supplierId, ModelMap model,
            HttpServletRequest request) {

        try {
            List<Warehouse> iDtos = this.topicItemProxy.getWarehouseList(supplierId);
            model.addAttribute("warehouses", iDtos);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "promotion/subpages/topicItemWarehouse";
    }

    @RequestMapping(value = "/wh/inventory", method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo searchSkuInventory(@RequestParam("whId") Long whId,
                                         @RequestParam("sku") String sku, ModelMap model,
                                         HttpServletRequest request) {
        ResultInfo<InventoryDto> resultInfo = this.topicItemProxy.getInventory(whId, sku);
        return resultInfo;
    }

    @RequestMapping(value = "/itemBarCode/confirmSearch", method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo<TopicItemInfoDTO> confirmItemByBarCode(
            @RequestParam("itemBarCode") String itemBarCode,
            @RequestParam("brandId") Long brandId,
            @RequestParam("spu") String spu,
            @RequestParam("supplierId") Long supplierId, ModelMap model,
            HttpServletRequest request) {

        List<ItemSku> skuDOs = topicItemProxy.getSKUInfosBySkuOrBarcodeWithBidSpu("", itemBarCode, brandId, spu, supplierId);
        if (skuDOs.size() == 0) {
            return new ResultInfo<>(new FailInfo());
        }
        if (skuDOs.size() > 1) {
            return new ResultInfo<>();
        } else {
            Long skuId = skuDOs.get(0).getId();
            ResultInfo<TopicItemInfoDTO> result = topicItemProxy.getTopicItemInfoBySkuId(skuId);
            if (null != result.getData()) {
                String file = result.getData().getTopicImage();
                if (!StringUtils.isBlank(file)) {
                    result.getData().setImageFullPath(ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.item,file));
                }
            }
            return result;
        }

    }

    @RequestMapping(value = "/itemSKU/confirmSearch", method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo<TopicItemInfoDTO> confirmItemBySKU(@RequestParam("sku") String sku,
                                                         @RequestParam("spu") String spu,
                                                         @RequestParam("brandId") Long brandId,
                                                         @RequestParam("supplierId") Long supplierId, ModelMap model,
                                                         HttpServletRequest request) {

        List<ItemSku> skuDOs = topicItemProxy.getSKUInfosBySkuOrBarcodeWithBidSpu(sku, null, brandId, spu, supplierId);
        if (skuDOs.size() == 0) {
            return new ResultInfo<>(new FailInfo());
        }
        if (skuDOs.size() > 1) {
            return new ResultInfo<>();
        } else {
            Long skuId = skuDOs.get(0).getId();
           ResultInfo<TopicItemInfoDTO> result= topicItemProxy.getTopicItemInfoBySkuId(skuId);
            if (null != result.getData()) {
                String file = result.getData().getTopicImage();
                if (!StringUtils.isBlank(file)) {
                    result.getData().setImageFullPath(ImageUtil.getImgFullUrl(Constant.IMAGE_URL_TYPE.item,file));
                }
            }
            return result;
        }
    }

    @RequestMapping(value = "/batchAdd", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo batchAddItem(String batchInput, Integer sortIndex,Integer type, ModelMap model, HttpServletRequest request) {
        if (null == batchInput || StringUtils.isBlank(batchInput)) {
            return new ResultInfo(new FailInfo("录入格式不正确!"));
        }
        if (null == sortIndex) {
            sortIndex = 0;
        }
        if (null == type) {
            type = 0;
        }
        return topicItemBatchProxy.addBatchItems(batchInput, sortIndex, type);
    }

    @RequestMapping(value = "/{topicId}/pasteItems/{sortIndex}", method = RequestMethod.GET)
    public String itemsBatchAdd(@PathVariable("topicId") Long topicId,
                                @PathVariable("sortIndex") Integer sortIndex, ModelMap model,
                                HttpServletRequest request) {

        model.addAttribute("topicId", topicId);
        model.addAttribute("sortIndex", sortIndex);
        return "promotion/topicItemBatchAdd";
    }

    @RequestMapping(value = "/{topicId}/pasteItem/{sortIndex}", method = RequestMethod.GET)
    public String itemBatchAdd(@PathVariable("topicId") Long topicId,
                               @PathVariable("sortIndex") Integer sortIndex, ModelMap model,
                               HttpServletRequest request) {

        model.addAttribute("topicId", topicId);
        model.addAttribute("sortIndex", sortIndex);
        model.addAttribute("type", TopicConstant.TOPIC_TYPE_SINGLE);
        return "promotion/topicItemBatchAdd";
    }

    /**
     * 上传图片
     *
     * @param request
     * @param file
     * @return
     */
    @RequestMapping(value = "/{topicItemIndex}/upload/Image", method = RequestMethod.GET)
    public String uploadImage(@PathVariable int topicItemIndex, ModelMap model,
                              HttpServletRequest request) {

        model.addAttribute("itemIndex", topicItemIndex);
        model.addAttribute("bucketURL", Constant.IMAGE_URL_TYPE.item.url);
        model.addAttribute("bucketname", Constant.IMAGE_URL_TYPE.item.name());
        return "promotion/topicItemUploadPic";
    }

    /**
     * 锁定活动库存
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/lockItem", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo lockTopicStock(@RequestParam("itemId") Long id, ModelMap model) {
        UserInfo user = getUserInfo();
        return topicItemProxy.lockItem(id,user);
    }

    /**
     * 锁定活动库存
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/releaseItem", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo releaseTopicStock(@RequestParam("itemId") Long id, ModelMap model) {
        UserInfo user = getUserInfo();
        return topicItemProxy.releaseItem(id,user);
    }

    @RequestMapping(value = "/batchLock", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo batchAddItem(@RequestParam Long topicId, ModelMap model,
                                   HttpServletRequest request) {

        if (topicId == null || topicId == 0) {
            return new ResultInfo(new FailInfo("参数错误"));
        }
        UserInfo user = getUserInfo();
        log.info("batch lock,topicID:{},user:{}", topicId, user.getUserName());
        ResultInfo result = topicItemBatchProxy.batchLockItem(topicId, user.getId(), user.getUserName());
        return result;
    }
    @RequestMapping(value = "/querySameSkuPrice", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo<List<TopicItem>> querySameSkuPrice(@RequestParam String sku) {
    	if(StringUtils.isEmpty(sku)) return new ResultInfo<>();
        ResultInfo<List<TopicItem>> list = topicItemProxy.getValidTopicItemBySku(sku);
        return list;
    } 
    
}
