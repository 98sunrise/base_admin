package com.gx.dao.Impl;

import com.gx.po.SysDepartment;

import java.util.List;

/**
 * 部门查询接口
 */
public interface ISysDepartmentDao {
    /**
     * 查询部门所有信息
     * @return
     */
    List<SysDepartment> selectAll();

    /**
     * 查询部门数据 By 主键
     * @param id
     * @return
     */
    SysDepartment selectById(int id);
    /**
     * 根据父id查询部门个数
     * @param pid
     * @return
     */
    int countAllByPid(int pid);

    /**
     * 新增部门
     * @param department 部门数据
     * @return 是否成功
     */
    boolean insert(SysDepartment department);

    /**
     * 修改部门
     * @param department 部门数据
     * @return 是否成功
     */
    boolean update(SysDepartment department);

    /**
     * 删除部门
     * @param id 部门id
     * @return 是否成功
     */
    boolean deleteById(int id);
    /**
     * 更新排序 + 1 (根据父id进行排序)
     * @param minSort 需要更新的最小排序（包含最小） 只是小于传null
     * @param maxValue 需要更新的最大排序（包含最大） 只是大于传null
     * @return 更新结果
     */
    boolean updateSortPlus1(int pid,Integer minSort,Integer maxValue);
    /**
     * 更新排序 - 1(根据父id进行排序)
     * @param minSort 需要更新的最小排序（包含最小） 只是小于传null
     * @param maxValue 需要更新的最大排序（包含最大） 只是大于传null
     * @return 更新结果
     */
    boolean updateSortMinus1(int pid,Integer minSort,Integer maxValue);
}
