package com.tp.test.mmp;

import com.alibaba.fastjson.JSON;
import com.tp.dto.mmp.CartCouponDTO;
import com.tp.dto.ord.CartLineDTO;
import com.tp.dto.ord.SeaOrderItemDTO;
import com.tp.dto.ord.SeaOrderItemWithSupplierDTO;
import com.tp.dto.ord.SeaOrderItemWithWarehouseDTO;
import com.tp.service.mmp.IPriceService;
import com.tp.service.mmp.PriceService;
import com.tp.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ldr on 2016/1/12.
 */
public class PriceServiceTest extends BaseTest {

    @Autowired
    private IPriceService priceService;

    @Test
    public void testT() throws Exception {
        SeaOrderItemDTO orderDTO = new SeaOrderItemDTO();
        List<SeaOrderItemWithSupplierDTO> seaOrderItemWithSupplierList = new ArrayList<>();
        orderDTO.setSeaOrderItemWithSupplierList(seaOrderItemWithSupplierList);
        orderDTO.setMemberId(68647L);

        SeaOrderItemWithSupplierDTO seaOrderItemWithSupplierDTO = new SeaOrderItemWithSupplierDTO();
        seaOrderItemWithSupplierDTO.setSupplierId(91L);
        seaOrderItemWithSupplierDTO.setSupplierName("91");
        seaOrderItemWithSupplierDTO.setFreightTempleteId(49L);
        List<SeaOrderItemWithWarehouseDTO> seaOrderItemWithWarehouseDTOList = new ArrayList<>();
        seaOrderItemWithSupplierDTO.setSeaOrderItemWithWarehouseList(seaOrderItemWithWarehouseDTOList);

        SeaOrderItemWithWarehouseDTO seaOrderItemWithWarehouseDTO = new SeaOrderItemWithWarehouseDTO();
        seaOrderItemWithWarehouseDTO.setWarehouseId(1L);
        List<CartLineDTO> cartLineDTOs = new ArrayList<>();
        CartLineDTO cartLineDTO = new CartLineDTO();
        cartLineDTO.setWarehouseId(1L);
        cartLineDTO.setSupplierId(91L);
        cartLineDTO.setBrandId(104L);
        cartLineDTO.setFreightTemplateId(49L);
        cartLineDTO.setSelected(true);
        cartLineDTO.setSkuCode("26030100170201");
        cartLineDTOs.add(cartLineDTO);

        List<Long> couponUserIdList = new ArrayList<>();
        CartCouponDTO cartCouponDTO = priceService.hitaoOrderTotalPriceWithCoupon(orderDTO, couponUserIdList);
        System.out.println(JSON.toJSONString(cartCouponDTO));
    }

}
