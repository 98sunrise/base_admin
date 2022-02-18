package com.gx.po;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class SysUser implements Serializable {
    private static final long serialVersionUID = 3547218362003523952L;

    private Integer id;
    private Date gmtCreate;
    private Date gmtModified;
    private Byte isDeleted;
    /**
     *用户名
     */
    private String userName;
    /**
     *用户密码
     */
    private String userPassword;
    /**
     *密码盐值
     */
    private String salt;
    /**
     *所属部门ID
     */
    private Integer departmentId;
    /**
     *职位ID
     */
    private Integer positionId;
    /**
     *角色ID
     */
    private Integer roleId;
    /**
     *姓名
     */
    private String realName;
    /**
     *性别（0未知，1男，2女）
     */
    private Byte gender;
    /**
     *生日
     */
    private Date birthday;
    /**
     *头像
     */
    private String portrait;
    /**
     *邮箱
     */
    private String email;
    /**
     *手机号
     */
    private String mobile;
    /**
     *QQ
     */
    private String qq;
    /**
     *微信
     */
    private String wechat;
    /**
     *用户状态（0禁用1启用）
     */
    private Byte userStatus;
    /**
     *登录次数
     */
    private Integer loginCount;
    /**
     *备注
     */
    private String remark;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public Byte getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Byte userStatus) {
        this.userStatus = userStatus;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysUser sysUser = (SysUser) o;
        return id.equals(sysUser.id) && gmtCreate.equals(sysUser.gmtCreate) && gmtModified.equals(sysUser.gmtModified) && isDeleted.equals(sysUser.isDeleted) && userName.equals(sysUser.userName) && userPassword.equals(sysUser.userPassword) && salt.equals(sysUser.salt) && departmentId.equals(sysUser.departmentId) && positionId.equals(sysUser.positionId) && roleId.equals(sysUser.roleId) && realName.equals(sysUser.realName) && gender.equals(sysUser.gender) && birthday.equals(sysUser.birthday) && email.equals(sysUser.email) && mobile.equals(sysUser.mobile) && qq.equals(sysUser.qq) && wechat.equals(sysUser.wechat) && userStatus.equals(sysUser.userStatus) && loginCount.equals(sysUser.loginCount) && remark.equals(sysUser.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gmtCreate, gmtModified, isDeleted, userName, userPassword, salt, departmentId, positionId, roleId, realName, gender, birthday, email, mobile, qq, wechat, userStatus, loginCount, remark);
    }

    @Override
    public String toString() {
        return "SysUser{" +
                "id=" + id +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", isDeleted=" + isDeleted +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", salt='" + salt + '\'' +
                ", departmentId=" + departmentId +
                ", positionId=" + positionId +
                ", roleId=" + roleId +
                ", realName='" + realName + '\'' +
                ", gender=" + gender +
                ", birthday=" + birthday +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", qq='" + qq + '\'' +
                ", wechat='" + wechat + '\'' +
                ", userStatus=" + userStatus +
                ", loginCount=" + loginCount +
                ", remark='" + remark + '\'' +
                '}';
    }
}
