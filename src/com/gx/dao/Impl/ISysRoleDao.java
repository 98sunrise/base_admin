package com.gx.dao.Impl;


import com.gx.po.SysRole;

import java.util.List;

/**
 * 角色接口
 */
public interface ISysRoleDao {
    /**
     * 分页查询角色
     * @param page//当前页数
     * @param limit//每页数据条数
     * @param searchName//查询名称
     * @param status//分页数据
     * @return
     */
    List<SysRole> selectForPageList(int page, int limit, String searchName, Integer status);

    /**
     * 返回数据总条数
     * @param searchName 查询名称
     * @param status 状态
     * @return 数据总条数
     */
    int countAll(String searchName,Integer status);

    /**
     * 根据id查询角色信息
     * @param id
     * @return 返回单条角色信息
     */
    SysRole selectById(int id);
    /**
     * 查询全部角色
     * @return
     */
    List<SysRole> selectAll();
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
    boolean deleteById(int id);
}
