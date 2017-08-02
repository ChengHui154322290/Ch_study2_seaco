package com.tp.backend.controller.ord;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.CartLineSimpleDTO;
import com.tp.dto.ord.ShoppingCartDto;
import com.tp.dto.ord.remote.SubOrder4BackendDTO;
import com.tp.model.mem.MemberInfo;
import com.tp.model.ord.CartItem;
import com.tp.model.ord.SubOrder;
import com.tp.proxy.mem.MemberInfoProxy;
import com.tp.proxy.ord.CartInfoProxy;
import com.tp.proxy.ord.CartItemInfoProxy;

@Controller
@RequestMapping("/salesorder/cart/")
public class CartRedisManageController extends AbstractBaseController {
	
	@Autowired
	private CartInfoProxy cartInfoProxy;
	@Autowired
	private MemberInfoProxy memberInfoProxy;
	@Autowired
	private CartItemInfoProxy cartItemInfoProxy;
	
	@RequestMapping("list")
	public void list(Model model,Long memberId,String memberName){
		if(memberId!=null){
			model.addAttribute("result", cartInfoProxy.selectCartLineDTOListByMemberIdFromDB(memberId));
		}
		model.addAttribute("memberId", memberId);
		model.addAttribute("memberName", memberName);
/*		if(memberId!=null){
			PageInfo<ShoppingCartDto> orderPageInfoResultInfo = cartInfoProxy.selectCartLineDTOListByMemberId(memberId);
			model.addAttribute("result", orderPageInfoResultInfo);
		}
		model.addAttribute("memberId", memberId);
		model.addAttribute("memberName", memberName);*/
	}
	
	@RequestMapping("deletecartitem")
	@ResponseBody
	public ResultInfo<Boolean> delete(Model model,CartLineSimpleDTO cartLineSimpleDTO){
		return cartInfoProxy.deleteCartLineSimpleDTOFromDB(cartLineSimpleDTO.getSkuCode(), cartLineSimpleDTO.getTopicId(), cartLineSimpleDTO.getMemberId());
	}
	
	@RequestMapping("transfarCartRedis")
	@ResponseBody
	public ResultInfo<Boolean> insert(){
		List<MemberInfo> memberInfoList = memberInfoProxy.queryByParamNotEmpty(new HashMap<String,Object>()).getData();
		if(CollectionUtils.isNotEmpty(memberInfoList)){
			for(MemberInfo memberInfo:memberInfoList){
				List<CartLineSimpleDTO> cartItemList = cartInfoProxy.selectCartLineDTOListByMemberId(memberInfo.getId()).getData();
				if(CollectionUtils.isNotEmpty(cartItemList)){
					for(CartLineSimpleDTO cartItem:cartItemList){
						CartItem cartInfo = new CartItem();
						cartInfo.setMemberId(memberInfo.getId());
						cartInfo.setQuantity(cartItem.getQuantity());
						cartInfo.setSkuCode(cartItem.getSkuCode());
						cartInfo.setTopicId(cartItem.getTopicId());
						cartItemInfoProxy.insert(cartInfo);
					}
				}
			}
		}
		return new ResultInfo<>(Boolean.TRUE);
	}
}
