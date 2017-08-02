package com.tp.m.vo.home;

import com.alibaba.fastjson.JSONObject;
import com.tp.m.base.BaseVO;

/**
 * Created by ldr on 2016/12/9.
 */
public class DCVO implements BaseVO {

    private static final long serialVersionUID = 7810591576452482323L;

    private JSONObject content;

    public JSONObject getContent() {
        return content;
    }

    public void setContent(JSONObject content) {
        this.content = content;
    }
}
