package com.tp.service.sup;

import com.tp.model.sup.SupplierUser;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 商家平台用户主表接口
  */
public interface ISupplierUserService extends IBaseService<SupplierUser>{

    /**
     * <pre>
     * 根据供应商id获取商家平台的信息
     * </pre>
     *
     * @param spId
     * @return
     */
    SupplierUser getSupplierUserBySupplierId(Long spId);
    /**
     * 
     * getSupplierUserByUserId:(根据用户ID  获取供应商平台用户信息). <br/>  
     *  
     * @author zhouguofeng  
     * @param userId
     * @return  
     * @sinceJDK 1.8
     */
    public SupplierUser getSupplierUserByUserId(final Long userId);
    /**
     * 
     * getSupplierUserByUserName:(根据登录名获取平台用户信息). <br/>  
     * TODO(这里描述这个方法适用条件 – 可选).<br/>   
     *  
     * @author zhouguofeng  
     * @param name
     * @return  
     * @sinceJDK 1.8
     */
    public SupplierUser getSupplierUserByUserName(final String name);
}
