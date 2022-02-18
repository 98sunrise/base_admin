package com.gx.service.Impl;

import com.gx.po.SysUser;
import com.gx.vo.*;

import java.util.List;

public interface IUserService {
    /**
     * 查询部门数据 for table
     * @return
     */
    List<LayuiTreeVo> selectDepartmentForTree();

    /**
     * 查询分页数据
     * @param page 页数
     * @param limit 每页数据条数
     * @param departmentId 部门id
     * @param userName 用户名
     * @param realName 姓名
     * @param mobile 手机号
     * @param userStatus 用户状态
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 分页数据
     */
    LayuiTableData<UserVo> selectPageList(int page, int limit, Integer departmentId, String userName, String realName, String mobile, Integer userStatus, String startDate, String endDate);
    /**
     * 查询数据 for 导出
     *
     * @param userName   用户名
     * @param realName   姓名
     * @param mobile     手机号
     * @param userStatus 用户状态
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @return 分页数据
     */
    List<UserVo> selectForExport(Integer departmentId, String userName, String realName, String mobile, Integer userStatus, String startDate, String endDate);

    /**
     * 根据id查询员工数据
     * @param id 注解
     * @return 员工数据
     */
    SysUser selectById(int id);
    /**
     * 查询部门数据 for 树形下拉框 （表单）
     * @return
     */
    List<TreeSelectVo> selectForTreeSelect();
    /**
     * 查询职位 for h5的下拉框
     * @return
     */
    List<H5SelectVo> selectPositionForH5Select();

    /**
     * 查询角色 for h5的下拉框
     * @return
     */
    List<H5SelectVo> selectRoleForH5Select();

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
