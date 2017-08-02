package com.tp.dto.mmp;

import java.io.Serializable;

import com.tp.model.mmp.Topic;

/**
 * Created by ldr on 2016/12/7.
 */
public class TopicQueryDTO extends Topic implements Serializable {

    private static final long serialVersionUID = -6441916804768627661L;
    private Integer sortField;

    public Integer getSortField() {
        return sortField;
    }

    public void setSortField(Integer sortField) {
        this.sortField = sortField;
    }
}
