package com.tp.model.mmp;

import java.util.Date;

import com.tp.model.BaseDO;

public class PromotionInfo extends BaseDO {

    /**
     * <pre>
     * 
     * </pre>
     */
    private static final long serialVersionUID = 4241616792166526572L;
    
    private Long id;
    
    /**供应商名称*/
    private String name;
    /**状态*/
    private Integer status;
    /**修改时间*/
    private Date modifiedTime;
    /**创建时间*/
    private Date createTime;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Date getModifiedTime() {
        return modifiedTime;
    }
    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
    

}
