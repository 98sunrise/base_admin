package com.gx.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

public class LayuiTreeVo implements Serializable {
    private static final long serialVersionUID = -5335800146404209396L;
    private int id;
    private String title;
    private String field;
    private String href;
    private Boolean spread;
    private Boolean checked;
    private Boolean disabled;
    @JsonInclude(JsonInclude.Include.NON_NULL)//注解 属性为null 不序列化
    private List<LayuiTreeVo> children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Boolean getSpread() {
        return spread;
    }

    public void setSpread(Boolean spread) {
        this.spread = spread;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public List<LayuiTreeVo> getChildren() {
        return children;
    }

    public void setChildren(List<LayuiTreeVo> children) {
        this.children = children;
    }
}
