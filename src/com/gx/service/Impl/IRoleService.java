package com.gx.service.Impl;


import com.gx.exception.MyException;
import com.gx.po.SysRole;
import com.gx.vo.JsonMsg;
import com.gx.vo.LayuiTableData;

public interface IRoleService {
    /**
     * 分页查询角色
     * @param page 当前页数
     * @param limit 每页数据条数
     * @param searchName 查询名称
     * @param status 查询状态
     * @return 分页数据
     */
    LayuiTableData<SysRole> selectForPageList(int page, int limit, String searchName, Integer status);

    /**
     * 查询所有数据条数
     * @return 所有数据条数
     */
    int countAll();

    /**
     * 根据id查询角色信息
     * @param id
     * @return
     */
    SysRole selectById(int id);

    /**
     * 新增角色
     * @param role 角色数据
     * @return 是否成功
     */
    boolean insert(SysRole role);

    /**
     * 修改角色
     * @param role
     * @return 是否成功
     */
    boolean update(SysRole role);

    /**
     * 根据id删除角色信息
     * @param //id主键
     * @return 是否成功
     */
    boolean deleteById(int id) throws MyException;
}
