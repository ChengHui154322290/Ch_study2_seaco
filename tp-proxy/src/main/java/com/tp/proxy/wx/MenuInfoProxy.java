package com.tp.proxy.wx;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.wx.MenuConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.wx.KVDto;
import com.tp.dto.wx.MenuButtonDto;
import com.tp.dto.wx.MenuInfoDto;
import com.tp.dto.wx.message.widget.Button;
import com.tp.dto.wx.message.widget.ClickButton;
import com.tp.dto.wx.message.widget.ComplexButton;
import com.tp.dto.wx.message.widget.Menu;
import com.tp.dto.wx.message.widget.ViewButton;
import com.tp.model.wx.MenuInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.wx.IMenuInfoService;
import com.tp.util.StringUtil;

/**
 * 菜单管理
 * @author zhuss
 */
@Service
public class MenuInfoProxy extends BaseProxy<MenuInfo>{
	
	private static final Logger log = LoggerFactory.getLogger(MenuInfoProxy.class);

	@Autowired
	private IMenuInfoService menuInfoService;
	
	@Override
	public IBaseService<MenuInfo> getService() {
		return menuInfoService;
	}
	
	/**
	 * 封装菜单树
	 * @return
	 */
	public List<MenuInfoDto> convTree(){
		MenuInfo qm = new MenuInfo();
		qm.setIsDel(0);
		List<MenuInfo> result = menuInfoService.queryByObject(qm);
		List<MenuInfoDto> mlist = new ArrayList<>();
		if(!CollectionUtils.isEmpty(result)){
			for(MenuInfo m : result){//拼装一级菜单
				if(m.getPid() == 0){
					MenuInfoDto mi = new MenuInfoDto();
					mi.toDto(m);
					List<MenuInfoDto> children = new ArrayList<>();
					for(MenuInfo mc : result){
						if(mc.getPid() == m.getId()){
							MenuInfoDto mic = new MenuInfoDto();
							mic.toDto(mc);
							children.add(mic);
							continue;
						}
					}
					mi.setChildren(children);
					mlist.add(mi);
				}
			}
		}
		return mlist;
	}
	
	/**
	 * 保存菜单
	 * @param menuInfo
	 * @return
	 */
	public ResultInfo<?> saveMenu(MenuInfo menuInfo){
		if(menuInfo.getId()==null){
			if(null == menuInfo.getPid()) return new ResultInfo<>(new FailInfo("菜单PID不能为空"));
			if(StringUtil.isBlank(menuInfo.getType())) return new ResultInfo<>(new FailInfo("菜单类型不能为空"));
			if(menuInfo.getType().equals(MenuConstant.TYPE.CLICK.getCode())){
				if(StringUtil.isBlank(menuInfo.getcKey())) return new ResultInfo<>(new FailInfo("菜单KEY不能为空"));
				MenuInfo m = new MenuInfo();
				m.setcKey(menuInfo.getcKey());
				m = menuInfoService.queryUniqueByObject(m);
				if(null != m) return new ResultInfo<>(new FailInfo("菜单KEY不能重复"));
			}else if(menuInfo.getType().equals(MenuConstant.TYPE.VIEW.getCode())){
				if(StringUtil.isBlank(menuInfo.getvUrl())) return new ResultInfo<>(new FailInfo("菜单URL不能为空"));
			}else return new ResultInfo<>(new FailInfo("菜单类型不在范围内"));
			//验证菜单数量  一级菜单最多3个  二级菜单最多5个
			if(menuInfo.getPid() == 0){
				Integer parentCount = menuInfoService.queryCountByPid(0);
				if(parentCount>2)return new ResultInfo<>(new FailInfo("一级菜单最多3个"));
			}else{
				Integer childCount = menuInfoService.queryCountByPid(menuInfo.getPid());
				if(childCount>4)return new ResultInfo<>(new FailInfo("二级菜单最多5个"));
			}
			menuInfo.setIsDel(0);
			menuInfo.setCreateTime(new Date());
			menuInfo.setModifyTime(new Date());
			MenuInfo m = menuInfoService.insert(menuInfo);
			//如果是子菜单  且 其父菜单有type 则修改父菜单
			if(m.getPid() != 0){
				MenuInfo pm = menuInfoService.queryById(m.getPid());
				if(StringUtil.isNotBlank(pm.getType())){
					pm.setcKey(StringUtil.EMPTY);
					pm.setType(StringUtil.EMPTY);
					pm.setvUrl(StringUtil.EMPTY);
					menuInfoService.updateById(pm);
				}
			}
			return new ResultInfo<>(m);
		}else{
			menuInfo.setModifyTime(new Date());
			if(menuInfo.getType().equalsIgnoreCase(MenuConstant.TYPE.CLICK.getCode()))menuInfo.setvUrl(StringUtil.EMPTY);
			if(menuInfo.getType().equalsIgnoreCase(MenuConstant.TYPE.VIEW.getCode()))menuInfo.setcKey(StringUtil.EMPTY);
			return new ResultInfo<>(menuInfoService.updateNotNullById(menuInfo));
		}
	}
	
	/**
	 * 删除菜单
	 * @param params
	 * @return
	 */
	public ResultInfo<Integer> delMenu(Map<String,Object> params){
		return new ResultInfo<>(menuInfoService.delMenu(params));
	}
	
	/**
	 * 发布到微信公众号
	 * @return
	 */
	public ResultInfo<Boolean> pushMenu(){
		//获取菜单列表
		List<MenuInfoDto> treeData = convTree();
		if(!CollectionUtils.isEmpty(treeData)){
			Menu menu = new Menu();
			menu.setButton(convertBtn(treeData).toArray(new Button[treeData.size()]));
			//删除已有菜单
			menuInfoService.deleteMenu();
			//创建菜单
			return new ResultInfo<>(menuInfoService.createMenu(menu));
		}
		return new ResultInfo<>(new FailInfo("菜单为空，发布失败"));
	}
	
	/**
	 * 封装菜单按钮
	 * @param treeData
	 * @return
	 */
	public List<Button> convertBtn(List<MenuInfoDto> treeData){
		List<Button> buttons = new ArrayList<Button>();
		for(MenuInfoDto tree : treeData){
			if(StringUtil.equalsIgnoreCase(tree.getType(), MenuConstant.TYPE.CLICK.getCode())){
				ClickButton cb = new ClickButton();
				cb.setName(tree.getName());
				cb.setKey(tree.getcKey());
				cb.setType(tree.getType());
				buttons.add(cb);
			}else if(StringUtil.equalsIgnoreCase(tree.getType(), MenuConstant.TYPE.VIEW.getCode())){
				ViewButton vb = new ViewButton();
				vb.setName(tree.getName());
				vb.setType(tree.getType());
				vb.setUrl(tree.getvUrl());	
				buttons.add(vb);
			}else{
				if(!CollectionUtils.isEmpty(tree.getChildren())){
					ComplexButton clb = new ComplexButton();
					clb.setName(tree.getName());
					clb.setSub_button(convertBtn(tree.getChildren()).toArray(new Button[tree.getChildren().size()]));
					buttons.add(clb);
				}
			}
		}
		return buttons;
	}
	
	/**
	 * 封装类型为click的菜单 下拉框列表
	 * @return
	 */
	public List<KVDto> getKVMenu(){
		try{
			List<MenuButtonDto> button = menuInfoService.getMenu().getButton();
			if(!CollectionUtils.isEmpty(button)){
				return convertKVMenu(button);
			}
		}catch(Exception e){
			log.error("[微信 - 封装KV菜单 Exception] = {}",e);
		}
		return null;
	}
	
	public List<KVDto> convertKVMenu(List<MenuButtonDto> button){
		List<KVDto>  list = new ArrayList<>();
		for(MenuButtonDto mb : button){
			if(StringUtil.isNotBlank(mb.getKey()))list.add(new KVDto(mb.getKey(),mb.getName()));
			if(!CollectionUtils.isEmpty(mb.getSub_button())){
				list.addAll(convertKVMenu(mb.getSub_button()));
			}
		}
		return list;
	}
}
