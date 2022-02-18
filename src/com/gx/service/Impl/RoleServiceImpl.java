package com.gx.service.Impl;

import com.gx.dao.Impl.ISysRoleDao;
import com.gx.dao.Impl.ISysUserDao;
import com.gx.dao.Impl.SysRoleDaoImpl;
import com.gx.dao.Impl.SysUserDaoImpl;
import com.gx.exception.MyException;
import com.gx.po.SysRole;
import com.gx.vo.JsonMsg;
import com.gx.vo.LayuiTableData;

import java.util.List;

public class RoleServiceImpl implements IRoleService{
    //dao
    private final ISysRoleDao roleDao=new SysRoleDaoImpl();
    private final ISysUserDao userDao=new SysUserDaoImpl();
    @Override
    public LayuiTableData<SysRole> selectForPageList(int page, int limit, String searchName, Integer status) {
        List<SysRole> data=this.roleDao.selectForPageList(page,limit,searchName,status);
        int count=this.roleDao.countAll(searchName,status);
        return new LayuiTableData<>(count,data);
    }

    @Override
    public int countAll() {
        return this.roleDao.countAll(null,null);
    }

    @Override
    public SysRole selectById(int id) {
        return this.roleDao.selectById(id);
    }

    @Override
    public boolean insert(SysRole role) {
        return this.roleDao.insert(role);
    }

    @Override
    public boolean update(SysRole role) {
        return this.roleDao.update(role);
    }

    @Override
    public boolean deleteById(int id) throws MyException {
        boolean bolR=false;
        //查询准备删除的职位是否在使用
        int useCount=this.userDao.countUserByRoleId(id);
        if(useCount==0){
            //未使用，直接删除
            boolean isOk=this.roleDao.deleteById(id);
            if(isOk){
                bolR=true;
            }else {
                //删除失败
                throw new MyException("删除失败！");
            }
        }else {
            //使用中，删除失败
            throw new MyException("该角色使用中，不能删除！");
        }

        return bolR;
    }
}
