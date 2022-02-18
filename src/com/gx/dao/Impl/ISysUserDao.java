package com.gx.dao.Impl;

import com.gx.po.SysUser;
import com.gx.vo.UserVo;

import java.util.List;

public interface ISysUserDao {
    /**
     * 根据用户名查找用户数据（login）
     * @param userName//用户名
     * @return//用户数据
     */
    UserVo selectUserByName(String userName);

    /**
     * 查询分页数据
     * @param page 页数
     * @param limit 每页数据条数
     * @param departmentIds 部门id
     * @param userName 用户名
     * @param realName 姓名
     * @param mobile 手机号
     * @param userStatus 用户状态
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 分页数据
     */
    List<UserVo> selectPageList(int page,int limit,Integer[] departmentIds,String userName,String realName,String mobile,Integer userStatus,String startDate,String endDate);
    /**
     * 查询数据 for 导出
     *
     * @param departmentIds 部门id
     * @param userName      用户名
     * @param realName      姓名
     * @param mobile        手机号
     * @param userStatus    用户状态
     * @param startDate     开始时间
     * @param endDate       结束时间
     * @return 分页数据
     */
    List<UserVo> selectForExport(Integer[] departmentIds, String userName, String realName, String mobile, Integer userStatus, String startDate, String endDate);

    /**
     * 根据id查询员工数据
     * @param id
     * @return
     */
    SysUser selectById(int id);
    /**
     * 统计员工数据总条数
     * @param departmentIds 部门id
     * @param userName 用户名
     * @param realName 姓名
     * @param mobile 手机号
     * @param userStatus 用户状态
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 职位员工总条数
     */
   int countAll(Integer[] departmentIds,String userName,String realName,String mobile,Integer userStatus,String startDate,String endDate);

    /**
     * 统计在使用 指定职位的用户数
     * @param positionId 职位id
     * @return用户数
     */
    int countUserByPositionId(int positionId);
    /**
     * 统计在使用 指定角色的用户数
     * @param roleId 职位id
     * @return用户数
     */
    int countUserByRoleId(int roleId);
    /**
     * 统计在使用 指定部门的用户数
     * @param departmentId 部门id
     * @return用户数
     */
    int countUserByDepartmentId(int departmentId);

    /**
     * 新增用户
     * @param user 用户数据
     * @return 成功否
     */
    boolean insert(SysUser user);
    /**
     * 修改用户
     * @param user 用户数据
     * @return 成功否
     */
    boolean update(SysUser user);
    /**
     * 删除用户
     * @param id 用户id
     * @return 成功否
     */
    boolean deleteById(int id);

}
