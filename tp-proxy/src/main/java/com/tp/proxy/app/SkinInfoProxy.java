package com.tp.proxy.app;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.PageInfo;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.remote.OrderList4UserDTO;
import com.tp.exception.OrderServiceException;
import com.tp.model.app.SkinInfo;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.query.ord.SubOrderQO;
import com.tp.service.IBaseService;
import com.tp.service.app.ISkinInfoService;
import com.tp.util.DateUtil;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class SkinInfoProxy extends BaseProxy<SkinInfo>{

	@Autowired
	private ISkinInfoService skinInfoService;

	@Override
	public IBaseService<SkinInfo> getService() {
		return skinInfoService;
	}
    /**
     * 皮肤详情
     * 

     * @return
     */
    public ResultInfo<SkinInfo> searchSkinNew(Long skinid){
		ResultInfo<SkinInfo> result = new ResultInfo<>();
    	this.execute(result, () -> {
            Date nowTime = new Date();
            SkinInfo skinInfo = skinInfoService.searchSkin(skinid,nowTime);
            result.setData(skinInfo);
        });
		return  result;
    }
}
