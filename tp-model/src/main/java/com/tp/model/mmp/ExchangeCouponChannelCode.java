package com.tp.model.mmp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;

/**
 * @author szy
 *         活动兑换码明细表
 */
public class ExchangeCouponChannelCode extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1451440579107L;

    /**
     * 兑换码 数据类型varchar(24)
     */
    private String exchangeCode;
    
    /**批次内卡券序列*/
    private Long codeSeq;

    /**
     * 数据类型bigint(11)
     */
    @Id
    private Long id;

    /**
     * 活动id 数据类型bigint(11)
     */
    private Long actId;

    /**
     * 优惠券批次号 数据类型bigint(11)
     */
    private Long couponId;

    /**
     * 兑换券的版本号
     */
    private String versionCode;

    /**
     * 状态(0未使用，1已使用) 数据类型tinyint(1)
     */
    private Integer status;
    
    /**卡券推广员*/
    private Long promoterId;

    /**
     * 兑换的用户id
     */
    private Long memberId;

    /**
     * 兑换的用户名
     */
    private String memberName;

    /**
     * 创建时间 数据类型datetime
     */
    private Date createTime;

    /**
     * 创建人 数据类型varchar(32)
     */
    private String createUser;

    /**
     * 修改时间 数据类型datetime
     */
    private Date updateTime;

    /**绑定卡券时间*/
    private Date bindTime;
    /**绑定操作人员*/
    private String bindUser;
    /**
     * 修改人 数据类型varchar(32)
     */
    private String updateUser;
    
    /**
     * mmp_coupon_user id,兑换码对应优惠券详细
     */
    private Long couponUserId;
    /**
     * 取消原因  1: 转线下购物卡  2:业务作废  3:其他'
     */
    private String cancelReason;

    @Virtual
    private CouponUser couponUser;
    
    @Virtual
    private String promoterName;
    @Virtual
    private String couponName;
    @Virtual
    private Integer useStatus;
    
    @Virtual
    private Long beginCodeSeq;
    @Virtual
    private Long endCodeSeq;
    @Virtual
    private List<Long> codeSeqList;
    @Virtual
    private List<Long> couponCodeIdList;
    
    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getExchangeCode() {
        return exchangeCode;
    }

    public void setExchangeCode(String exchangeCode) {
        this.exchangeCode = exchangeCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

	public Long getCouponUserId() {
		return couponUserId;
	}

	public void setCouponUserId(Long couponUserId) {
		this.couponUserId = couponUserId;
	}

	public CouponUser getCouponUser() {
		return couponUser;
	}

	public void setCouponUser(CouponUser couponUser) {
		this.couponUser = couponUser;
	}

	public String getPromoterName() {
		return promoterName;
	}

	public void setPromoterName(String promoterName) {
		this.promoterName = promoterName;
	}

	public Long getPromoterId() {
		return promoterId;
	}

	public void setPromoterId(Long promoterId) {
		this.promoterId = promoterId;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public Integer getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(Integer useStatus) {
		this.useStatus = useStatus;
	}

	public Long getCodeSeq() {
		return codeSeq;
	}

	public void setCodeSeq(Long codeSeq) {
		this.codeSeq = codeSeq;
	}

	public Long getBeginCodeSeq() {
		return beginCodeSeq;
	}

	public void setBeginCodeSeq(Long beginCodeSeq) {
		this.beginCodeSeq = beginCodeSeq;
	}

	public Long getEndCodeSeq() {
		return endCodeSeq;
	}

	public void setEndCodeSeq(Long endCodeSeq) {
		this.endCodeSeq = endCodeSeq;
	}

	public List<Long> getCodeSeqList() {
		return codeSeqList;
	}

	public void setCodeSeqList(List<Long> codeSeqList) {
		this.codeSeqList = codeSeqList;
	}

	public List<Long> getCouponCodeIdList() {
		return couponCodeIdList;
	}

	public void setCouponCodeIdList(List<Long> couponCodeIdList) {
		this.couponCodeIdList = couponCodeIdList;
	}

	public Date getBindTime() {
		return bindTime;
	}

	public void setBindTime(Date bindTime) {
		this.bindTime = bindTime;
	}

	public String getBindUser() {
		return bindUser;
	}

	public void setBindUser(String bindUser) {
		this.bindUser = bindUser;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	
}
