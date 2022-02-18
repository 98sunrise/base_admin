package com.gx.service.Impl;
/**
 * 登录服务层接口
 */

import com.gx.vo.UserVo;

public interface ILoginService {
    /**
     * 根据用户名查找用户数据（login）
     * @param userName
     * @return
     */
    UserVo selectUserByName(String userName);
}
