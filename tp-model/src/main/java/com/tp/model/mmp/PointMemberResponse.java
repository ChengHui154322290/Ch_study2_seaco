package com.tp.model.mmp;

import java.io.Serializable;
import java.util.List;

import com.tp.model.mem.MemberSignPoint;

public class PointMemberResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<MemberSignPoint> signResult ;
	
	private List<PointMember> pointResult ;

	public List<MemberSignPoint> getSignResult() {
		return signResult;
	}

	public void setSignResult(List<MemberSignPoint> signResult) {
		this.signResult = signResult;
	}

	public List<PointMember> getPointResult() {
		return pointResult;
	}

	public void setPointResult(List<PointMember> pointResult) {
		this.pointResult = pointResult;
	}
	
	
    
}
