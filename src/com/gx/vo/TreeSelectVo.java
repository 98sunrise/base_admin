package com.gx.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

public class TreeSelectVo implements Serializable {
    private static final long serialVersionUID = 2363251427553911364L;
    /**
     * id
     */
    private int id;
    /**
     *显示文本值
     */
    private String name;
    /**
     *节点是否打开
     */
    private boolean open;
    /**
     *是否选中
     */
    private boolean checked;
    /**
     *子节点
     */
    @JsonInclude (JsonInclude.Include.NON_NULL)//注解 属性为null 不序列化
    private List<TreeSelectVo> children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public List<TreeSelectVo> getChildren() {
        return children;
    }

    public void setChildren(List<TreeSelectVo> children) {
        this.children = children;
    }
}
