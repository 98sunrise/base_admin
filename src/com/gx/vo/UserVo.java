package com.gx.vo;

import com.gx.po.SysUser;

import java.io.Serializable;

public class UserVo extends SysUser implements Serializable {
    private static final long serialVersionUID = -4392833984083998105L;
    //部门名称
    private String departmentName;
    //职位名称
    private String positionName;
    //角色名称
    private String roleName;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
