package com.gx.po;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class SysPosition implements Serializable {
    private static final long serialVersionUID = -8358215031162496720L;
    private Integer id;
    private Date gmtCreate;
    private Date gmtModified;
    //职位名称
    private String positionName;
    //职位排序
    private Integer positionSort;
    //职位状态（0禁用 1启用）
    private Byte positionStatus;
    //职位备注
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

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public Integer getPositionSort() {
        return positionSort;
    }

    public void setPositionSort(Integer positionSort) {
        this.positionSort = positionSort;
    }

    public Byte getPositionStatus() {
        return positionStatus;
    }

    public void setPositionStatus(Byte positionStatus) {
        this.positionStatus = positionStatus;
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
        SysPosition that = (SysPosition) o;
        return id.equals(that.id) && gmtCreate.equals(that.gmtCreate) && gmtModified.equals(that.gmtModified) && positionName.equals(that.positionName) && positionSort.equals(that.positionSort) && positionStatus.equals(that.positionStatus) && remark.equals(that.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gmtCreate, gmtModified, positionName, positionSort, positionStatus, remark);
    }

    @Override
    public String toString() {
        return "SysPosition{" +
                "id=" + id +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", positionName='" + positionName + '\'' +
                ", positionSort=" + positionSort +
                ", positionStatus=" + positionStatus +
                ", remark='" + remark + '\'' +
                '}';
    }
}
