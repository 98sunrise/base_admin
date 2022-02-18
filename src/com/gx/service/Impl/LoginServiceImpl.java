package com.gx.service.Impl;

import com.gx.dao.Impl.ISysUserDao;
import com.gx.dao.Impl.SysUserDaoImpl;
import com.gx.vo.UserVo;

public class LoginServiceImpl implements ILoginService{
    //dao
    private final ISysUserDao userDao=new SysUserDaoImpl();
    @Override
    public UserVo selectUserByName(String userName) {
        //直接调用dao方法
        return this.userDao.selectUserByName(userName);
    }


}
