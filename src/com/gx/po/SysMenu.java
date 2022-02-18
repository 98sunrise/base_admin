package com.gx.po;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class SysMenu implements Serializable {

    private static final long serialVersionUID = -3235036499531558145L;
    private Integer id;
    private Date gmtCreate;
    private Date gmtModified;
    /**
     * 父菜单ID（0表示根菜单）
     */
    private Integer parent_Id;

    private String menuName;
    /**
     * 菜单图标
     */
    private String menuIcon;
    /**
     * 菜单url
     */
    private String menuUrl;
    /**
     * 菜单排序
     */
    private Integer menuSort;
    /**
     * 菜单类型（1目录 2页面 3按钮）
     */
    private Byte menuType;
    /**
     * 菜单状态（0表示禁用 1表示启用）
     */
    private Byte menuStatus;
    /**
     * 权限标识
     */
    private String authroize;
    /**
     * 备注
     */
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getParent_Id() {
        return parent_Id;
    }

    public void setParent_Id(Integer parent_Id) {
        this.parent_Id = parent_Id;
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

    public String getAuthroize() {
        return authroize;
    }

    public void setAuthroize(String authroize) {
        this.authroize = authroize;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysMenu sysMenu = (SysMenu) o;
        return id.equals(sysMenu.id) && gmtCreate.equals(sysMenu.gmtCreate) && gmtModified.equals(sysMenu.gmtModified) && parent_Id.equals(sysMenu.parent_Id) && menuName.equals(sysMenu.menuName) && menuIcon.equals(sysMenu.menuIcon) && menuUrl.equals(sysMenu.menuUrl) && menuSort.equals(sysMenu.menuSort) && menuType.equals(sysMenu.menuType) && menuStatus.equals(sysMenu.menuStatus) && authroize.equals(sysMenu.authroize) && remark.equals(sysMenu.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gmtCreate, gmtModified, parent_Id, menuName, menuIcon, menuUrl, menuSort, menuType, menuStatus, authroize, remark);
    }

    @Override
    public String toString() {
        return "SysMenu{" +
                "id=" + id +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", parent_Id=" + parent_Id +
                ", menuName='" + menuName + '\'' +
                ", menuIcon='" + menuIcon + '\'' +
                ", menuUrl='" + menuUrl + '\'' +
                ", menuSort=" + menuSort +
                ", menuType=" + menuType +
                ", menuStatus=" + menuStatus +
                ", authroize='" + authroize + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
