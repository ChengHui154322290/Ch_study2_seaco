package com.tp.service.wx.manager;

import com.alibaba.fastjson.JSONObject;
import com.tp.common.util.RequestUtil;
import com.tp.common.vo.wx.RequestUrlConstant;
import com.tp.dto.wx.message.widget.Button;
import com.tp.dto.wx.message.widget.ClickButton;
import com.tp.dto.wx.message.widget.ComplexButton;
import com.tp.dto.wx.message.widget.Menu;
import com.tp.dto.wx.message.widget.ViewButton;
import com.tp.result.wx.MenuResult;
import com.tp.util.JsonUtil;
import com.tp.util.StringUtil;


/**
 * 菜单管理器
 * @author zhuss
 */
public class MenuManager extends BaseManager{
	
	/**
	 * 创建菜单
	 */
	public static boolean createMenu(String token, Menu menu) {
		if(null == menu || StringUtil.isBlank(token)) return false;
		boolean result = false;
		String url = RequestUrlConstant.MENU_CREATE_URL.replace("ACCESS_TOKEN",token).trim();
		String jsonMenu = JsonUtil.convertObjToStr(menu);
		JSONObject jsonObject = RequestUtil.httpsRequest(url.trim(), "POST", jsonMenu);
		handleError(jsonObject,"创建菜单");
		result = true;
		return result;
	}

	/**
	 * 查询菜单
	 * 
	 * @param accessToken 凭证
	 * @return
	 */
	public static MenuResult getMenu(String token) {
		String url = RequestUrlConstant.MENU_GET_URL.replace("ACCESS_TOKEN",token).trim();
		JSONObject jsonObject = RequestUtil.httpsRequest(url.trim(), "GET", null);
		handleError(jsonObject,"查询菜单");
		return JSONObject.parseObject(jsonObject.getString("menu"), MenuResult.class);
	}

	/**
	 * 删除菜单
	 * 
	 * @param accessToken 凭证
	 * @return true成功 false失败
	 */
	public static boolean deleteMenu(String token) {
		boolean result = false;
		String url = RequestUrlConstant.MENU_DELETE_URL.replace("ACCESS_TOKEN",token).trim();
		JSONObject jsonObject = RequestUtil.httpsRequest(url.trim(), "GET", null);
		handleError(jsonObject,"删除菜单");
		result = true;
		return result;
	}
	
	/**
	 * 定义菜单结构
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	private static Menu defineMenu() {
		ViewButton btn11 = new ViewButton();
		btn11.setName("开源社区");
		btn11.setType("view");
		btn11.setUrl("http://www.oschina.net");

		ViewButton btn12 = new ViewButton();
		btn12.setName("ITeye");
		btn12.setType("view");
		btn12.setUrl("http://www.iteye.com");

		ViewButton btn13 = new ViewButton();
		btn13.setName("CSND");
		btn13.setType("view");
		btn13.setUrl("http://www.csdn.net");

		ViewButton btn21 = new ViewButton();
		btn21.setName("西客妈妈");
		btn21.setType("view");
		btn21.setUrl("http://m.meitun.com");

		ViewButton btn22 = new ViewButton();
		btn22.setName("京东");
		btn22.setType("view");
		btn22.setUrl("http://m.jd.com");

		ViewButton btn23 = new ViewButton();
		btn23.setName("唯品会");
		btn23.setType("view");
		btn23.setUrl("http://m.vipshop.com");

		ViewButton btn24 = new ViewButton();
		btn24.setName("淘宝");
		btn24.setType("view");
		btn24.setUrl("http://m.taobao.com");

		ViewButton btn25 = new ViewButton();
		btn25.setName("苏宁易购");
		btn25.setType("view");
		btn25.setUrl("http://m.suning.com");

		ComplexButton mainBtn1 = new ComplexButton();
		mainBtn1.setName("技术交流");
		mainBtn1.setSub_button(new Button[] { btn11, btn12, btn13 });

		ComplexButton mainBtn2 = new ComplexButton();
		mainBtn2.setName("购物");
		mainBtn2.setSub_button(new Button[] { btn21, btn22, btn23, btn24, btn25 });
		
		
		ClickButton btn31 = new ClickButton();
		btn31.setName("最新消息");
		btn31.setType("click");
		btn31.setKey("ZXXX");

		ViewButton btn32 = new ViewButton();
		btn32.setName("小能客服");
		btn32.setType("view");
		btn32.setUrl("http://222.72.249.242:8080/xnkf.html");

		ComplexButton mainBtn3 = new ComplexButton();
		mainBtn3.setName("用户中心");
		mainBtn3.setSub_button(new Button[] { btn31,btn32 });
		
		ViewButton mainBtn4 = new ViewButton();
		mainBtn4.setName("在线商城");
		mainBtn4.setType("view");
		mainBtn4.setUrl("http://m.meitun.com");
		
		ClickButton mainBtn5 = new ClickButton();
		mainBtn5.setName("人工客服");
		mainBtn5.setType("click");
		mainBtn5.setKey("KF");

		Menu menu = new Menu();
		menu.setButton(new Button[] { mainBtn4, mainBtn5, mainBtn3 });

		return menu;
	}
}
