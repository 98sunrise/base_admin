package com.gx.service.Impl;

import com.gx.exception.MyException;
import com.gx.po.SysDepartment;
import com.gx.vo.DepartmentTableTreeVo;
import com.gx.vo.LayuiTableData;
import com.gx.vo.TreeSelectVo;

import java.util.List;

public interface IDepartmentService {
    /**
     * 查询部门数据 for tableTree
     * @return 部门数据
     */
    LayuiTableData<DepartmentTableTreeVo> selectForTable();

    /**
     * 查询部门数据for 树形下拉框
     * @return
     */
    List<TreeSelectVo> SelectForTreeSelect();

    /**
     * 查询部门数据 by id
     * @param id
     * @return
     */
    SysDepartment selectById(int id);
    /**
     * 根据父id查询部门个数
     * @param pid 父id
     * @return 部门个数
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
    boolean deleteById(int id) throws MyException;
}
