package com.gx.service.Impl;
/**
 * 菜单服务实现类
 */

import com.gx.dao.Impl.ISysMenuDao;
import com.gx.dao.Impl.SysMenuDaoImpl;
import com.gx.exception.MyException;
import com.gx.po.SysMenu;
import com.gx.util.JdbcUtils;
import com.gx.vo.LayuiTableData;
import com.gx.vo.MenuTableTreeVo;
import com.gx.vo.TreeSelectVo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuServiceImpl implements IMenuService {
    //Dao
    private final ISysMenuDao menuDao=new SysMenuDaoImpl();

    @Override
    public List<MenuTableTreeVo> selectMenuByRoleId(Integer roleId) {
        //调用dao查询角色对应的菜单
        List<SysMenu> menuList=this.menuDao.selectMenuByRoleId(roleId);

        return dealMenuTableTreeVoList(menuList,0);//从父节点为0开始查找
    }

    /**
     * 查询表格数据
     * @return
     */
    @Override
    public LayuiTableData<MenuTableTreeVo> selectForTable() {
        List<SysMenu> menuList=this.menuDao.selectAll();
        List<MenuTableTreeVo> data=dealMenuTableTreeVoList(menuList,0);
        //组装layui table数据
        LayuiTableData<MenuTableTreeVo> layuiTableData=new LayuiTableData<>();
        layuiTableData.setCount(data.size());
        layuiTableData.setData(data);
        return layuiTableData;
    }

    /**
     * 加载下拉树
     * @return
     */
    @Override
    public List<TreeSelectVo> SelectForTreeSelect() {
        //获取所有的部门数据
        List<SysMenu> departmentList=this.menuDao.selectAll();
        List<TreeSelectVo> rList=new ArrayList<>();

        //根 不存在，添加是为了方便选择
        TreeSelectVo root=new TreeSelectVo();
        root.setId(0);
        root.setName("根");
        root.setChecked(false);
        root.setOpen(true);
        root.setChildren(this.dealTreeSelect(departmentList,0));
        rList.add(root);
        return rList;
    }

    /**
     * 根据id查询菜单数据
     * @param id 主键
     * @return
     */
    @Override
    public SysMenu selectById(int id) {
        return this.menuDao.selectById(id);
    }

    /**
     * 根据父id查询菜单数据
     * @param pid 父id
     * @return
     */
    @Override
    public int countAllByPid(int pid) {
        return this.menuDao.countAllByPid(pid);
    }

    /**
     * 新增菜单的数据
     * @param menu 菜单数据
     * @return
     */
    @Override
    public boolean insert(SysMenu menu) {
        boolean bolR=false;
        try {
            //开始事务
            JdbcUtils.beginTransaction();
            int nextSort=this.menuDao.countAllByPid(menu.getParent_Id())+1;
            //判断是否是 小于nextSort，是,调小的序号 需要处理其他序号
            if(menu.getMenuSort()<nextSort){
                boolean isOk=this.menuDao.updateSortPlus1(menu.getParent_Id(),menu.getMenuSort(),null);
                if(!isOk){
                    throw new SQLException("调整序号操作失败：pid="+menu.getParent_Id()+";minSort="+menu.getMenuSort());
                }
            }
            //新增
            boolean isOk=this.menuDao.insert(menu);
            if(isOk){
                bolR=true;
                //提交事务
                JdbcUtils.commitTransaction();
            }else {
                throw new SQLException("新增操作失败："+menu);
            }
        } catch (SQLException throwables) {
            //事务的回滚
            try {
                JdbcUtils.rollbackTransaction();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        }
        return bolR;
    }

    /**
     * 修改菜单数据
     * @param menu 菜单数据
     * @return
     */
    @Override
    public boolean update(SysMenu menu) {
        boolean bolR=false;
        try {
            JdbcUtils.beginTransaction();
            //查询出未修改的数据
            SysMenu dbMenu=this.menuDao.selectById(menu.getId());
            ////判断上级部门是否改变
            if(!dbMenu.getParent_Id().equals(menu.getParent_Id())){
                //上级部门 发生改变
                //对于旧上级部门 大于原来的sort -1
                boolean isOk=this.menuDao.updateSortMinus1(dbMenu.getParent_Id(),(dbMenu.getMenuSort()+1),null);
                if(!isOk){
                    throw  new SQLException("处理序号操作失败：updateSortMinus1");
                }
                //对于新的上级部门 大于等于新sort =1
                isOk=this.menuDao.updateSortPlus1(menu.getParent_Id(),menu.getMenuSort(),null);
                if(!isOk){
                    throw  new SQLException("调整序号操作失败：pid="+menu.getParent_Id()+";minSort="+dbMenu.getMenuSort());
                }

            }else {
                //上级部门没有发生改变

                //判断修改后的序号 和 修改前的序号
                if(menu.getMenuSort()<dbMenu.getMenuSort()){
                    //向前（小）移  大于等于新sort，小于旧的sort +1
                    boolean isOk=this.menuDao.updateSortPlus1(dbMenu.getParent_Id(),menu.getMenuSort(),(dbMenu.getMenuSort()-1));
                    if(!isOk){
                        throw new SQLException("处理序号操作失败：updateSortPlus1");
                    }
                }else {
                    //向后（大）移  大于旧sort，小于等于新的sort -1
                    boolean isOk=this.menuDao.updateSortMinus1(dbMenu.getParent_Id(),(dbMenu.getMenuSort()+1),menu.getMenuSort());
                    if(!isOk){
                        throw  new SQLException("处理序号操作失败：updateSortMinus1");
                    }
                }
            }
            //修改
            boolean isOk=this.menuDao.update(menu);
            if(!isOk){
                throw  new SQLException("修改操作失败：update");
            }else {
                bolR=true;
                //提交事务
                JdbcUtils.commitTransaction();
            }
        } catch (SQLException throwables) {
            //事务的回滚
            try {
                JdbcUtils.rollbackTransaction();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        }
        return bolR;
    }

    /**
     * 删除菜单数据
     * @param id 菜单id
     * @return
     * @throws MyException
     */
    @Override
    public boolean deleteById(int id) throws MyException {
        boolean bolR=false;
        //判断是否有子节点
        int countChildren=this.countAllByPid(id);
        if(countChildren==0){
            try {
                JdbcUtils.beginTransaction();
                //查询出未修改的数据
                SysMenu dbMenu=this.menuDao.selectById(id);
                //处理序号  大于sort -1
                boolean isOk=this.menuDao.updateSortMinus1(dbMenu.getParent_Id(),dbMenu.getMenuSort()+1,null);
                if(!isOk){
                    throw new SQLException("处理序号操作失败：updateSortMinus1");
                }
                //修改
                isOk=this.menuDao.deleteById(id);
                if(!isOk){
                    throw new SQLException("删除操作失败：deleteById");
                }else {
                    bolR=true;
                    JdbcUtils.commitTransaction();
                }
            } catch (SQLException throwables) {
                //事务的回滚
                try {
                    JdbcUtils.rollbackTransaction();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                throwables.printStackTrace();
            }
        }else {
            throw new MyException("该部门存在子部门，不能直接删除");
        }
        return bolR;
    }

    private List<MenuTableTreeVo> dealMenuTableTreeVoList(List<SysMenu> listSource,int pid){
        List<MenuTableTreeVo> rList=new ArrayList<>();
        MenuTableTreeVo menuTableTreeVo=null;
        for (SysMenu menu:listSource) {
            if(menu.getParent_Id()==pid){//如果SysMenu里的夫ID等于传过来的夫ID
                menuTableTreeVo=new MenuTableTreeVo();
                //复制SysMenu到MenuTableTreeVo
                menuTableTreeVo.setId(menu.getId());
                menuTableTreeVo.setParentId(menu.getParent_Id());
                menuTableTreeVo.setMenuName(menu.getMenuName());
                menuTableTreeVo.setMenuIcon(menu.getMenuIcon());
                menuTableTreeVo.setMenuUrl(menu.getMenuUrl());
                menuTableTreeVo.setMenuSort(menu.getMenuSort());
                menuTableTreeVo.setMenuType(menu.getMenuType());
                menuTableTreeVo.setMenuStatus(menu.getMenuStatus());
                menuTableTreeVo.setAuthorize(menu.getAuthroize());
                menuTableTreeVo.setRemark(menu.getRemark());
                //菜单类型（1目录 2页面 3按钮）
                if(menu.getMenuType()<3){
                    //1目录 2页面 查找子节点 listSource保证数据源不变，menu.getId()作为下一层子节点的父节点
                    List<MenuTableTreeVo> childList=dealMenuTableTreeVoList(listSource,menu.getId());
                    menuTableTreeVo.setTreeList(childList);
                }else {
                    //3按钮 无子节点
                    menuTableTreeVo.setTreeList(null);
                }
                //MenuTableTreeVo 添加到List
                rList.add(menuTableTreeVo);
            }
        }


        return rList;
    }
    private List<TreeSelectVo> dealTreeSelect(List<SysMenu> listSource,int pid){
        List<TreeSelectVo> rList = new ArrayList<>();
        TreeSelectVo treeSelectVo=null;
        for (SysMenu menu:listSource) {
            if(menu.getParent_Id()==pid){
                treeSelectVo=new TreeSelectVo();
                treeSelectVo.setId(menu.getId());
                treeSelectVo.setName(menu.getMenuName());//显示的值
                treeSelectVo.setChecked(false);
                treeSelectVo.setOpen(true);//默认展开
                //菜单类型(1目录 2页面 3按钮)
                if(menu.getMenuType()==1){
                    List<TreeSelectVo> children=this.dealTreeSelect(listSource,menu.getId());
                    if (children.size()>0){
                        //有子节点
                        treeSelectVo.setChildren(children);
                    }else {
                        //没有子节点
                        treeSelectVo.setChildren(null);
                    }
                }else {
                    treeSelectVo.setChildren(null);
                }

                rList.add(treeSelectVo);
            }
        }
        return rList;
    }
}
