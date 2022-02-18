package com.gx.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MenuTableTreeVo implements Serializable {
    private static final long serialVersionUID = 9077980436552990810L;
    private Integer id;
    /**
     * 父菜单Id
     */
    private Integer parentId;
    /**
     *菜单名称
     */
    private String menuName;
    /**
     *菜单图标
     */
    private String menuIcon;
    /**
     *菜单路径
     */
    private String menuUrl;
    /**
     *菜单排序
     */
    private Integer menuSort;
    /**
     *菜单类型
     */
    private Byte menuType;
    /**
     *菜单状态
     */
    private Byte menuStatus;
    /**
     *权限标识
     */
    private String authorize;
    /**
     *菜单备注
     */
    private String remark;
    /**
     *子菜单集合
     */
    private List<MenuTableTreeVo> TreeList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public Integer getMenuSort() {
        return menuSort;
    }

    public void setMenuSort(Integer menuSort) {
        this.menuSort = menuSort;
    }

    public Byte getMenuType() {
        return menuType;
    }

    public void setMenuType(Byte menuType) {
        this.menuType = menuType;
    }

    public Byte getMenuStatus() {
        return menuStatus;
    }

    public void setMenuStatus(Byte menuStatus) {
        this.menuStatus = menuStatus;
    }

    public String getAuthorize() {
        return authorize;
    }

    public void setAuthorize(String authorize) {
        this.authorize = authorize;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<MenuTableTreeVo> getTreeList() {
        return TreeList;
    }

    public void setTreeList(List<MenuTableTreeVo> treeList) {
        TreeList = treeList;
    }
}
