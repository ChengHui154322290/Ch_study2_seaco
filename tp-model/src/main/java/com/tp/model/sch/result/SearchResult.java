package com.tp.model.sch.result;

import java.util.List;

import com.tp.model.BaseDO;

/**
 * Created by ldr on 2016/2/17.
 */
public class SearchResult extends BaseDO {

    private static final long serialVersionUID = -33165365003671020L;

    private String status;

    private SearchResultDetail result;

    private List<SearchError> errors;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SearchResultDetail getResult() {
        return result;
    }

    public void setResult(SearchResultDetail result) {
        this.result = result;
    }

    public List<SearchError> getErrors() {
        return errors;
    }

    public void setErrors(List<SearchError> errors) {
        this.errors = errors;
    }
}
