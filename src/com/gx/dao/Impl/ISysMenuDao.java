package com.gx.dao.Impl;

import com.gx.po.SysMenu;

import java.sql.SQLException;
import java.util.List;

/**
 * 菜单接口
 */
public interface ISysMenuDao {
    /**
     * 根据角色ID查菜单
     * @param roleId
     * @return
     */
    List<SysMenu> selectMenuByRoleId(Integer roleId);
    /**
     * 查询所有菜单信息
     * @return list
     */
    List<SysMenu> selectAll();

    /**
     * 查询菜单数据 by 主键
     * @param id 主键
     * @return 菜单数据
     */
    SysMenu selectById(int id);

    /**
     * 根据父id查询菜单个数
     * @param pid 父id
     * @return 菜单个数
     */
    int countAllByPid(int pid);

    /**
     * 新增菜单
     * @param menu 菜单数据
     * @return 是否成功
     */
    boolean insert(SysMenu menu);

    /**
     * 修改菜单
     * @param menu 菜单数据
     * @return 是否成功
     */
    boolean update(SysMenu menu);

    /**
     * 删除菜单
     * @param id 菜单id
     * @return 是否成功
     */
    boolean deleteById(int id);

    /**
     * 更新排序 +1 by pid
     * @param pid 父id
     * @param minSort 需要更新的最小排序（包含最小） 只是小于传null
     * @param maxValue 需要更新的最大排序（包含最大） 只是大于传null
     * @return 更新结果
     */
    boolean updateSortPlus1(int pid,Integer minSort,Integer maxValue);

    /**
     * 更新排序 -1 by pid
     * @param pid 父id
     * @param minSort 需要更新的最小排序（包含最小） 只是小于传null
     * @param maxValue 需要更新的最大排序（包含最大） 只是大于传null
     * @return 更新结果
     */
    boolean updateSortMinus1(int pid,Integer minSort, Integer maxValue);
}
