package com.tp.model.sch.result;

import java.util.List;

import com.tp.model.BaseDO;
import com.tp.model.sch.Element;

/**
 * Created by ldr on 2016/2/18.
 */
public class Aggregate extends BaseDO {

    private static final long serialVersionUID = 199498332053691288L;

    private String code;

    private String name;

    private List<Element> elements;

    public Aggregate() {
    }

    public Aggregate(String code, String name, List<Element> elements) {
        this.code = code;
        this.name = name;
        this.elements = elements;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }
}
