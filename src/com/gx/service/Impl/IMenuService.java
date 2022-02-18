package com.gx.service.Impl;

import com.gx.exception.MyException;
import com.gx.po.SysMenu;
import com.gx.vo.LayuiTableData;
import com.gx.vo.MenuTableTreeVo;
import com.gx.vo.TreeSelectVo;

import java.util.List;

public interface IMenuService {
    /**
     * 根据角色ID查菜单
     * @param roleId//角色ID
     * @return//菜单List
     */
    List<MenuTableTreeVo> selectMenuByRoleId(Integer roleId);
    /**
     * 查询菜单数据 for tableTree
     * @return 菜单数据
     */
    LayuiTableData<MenuTableTreeVo> selectForTable();

    /**
     * 查询菜单数据 for 树形下拉框
     * @return
     */
    List<TreeSelectVo> SelectForTreeSelect();

    /**
     * 查询菜单数据 by id
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
    boolean deleteById(int id) throws MyException;
}
