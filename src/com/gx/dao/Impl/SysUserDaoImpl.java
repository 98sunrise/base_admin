package com.gx.dao.Impl;

import com.gx.po.SysUser;
import com.gx.util.JdbcUtils;
import com.gx.util.ProjectParameter;
import com.gx.util.Tools;
import com.gx.vo.UserVo;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SysUserDaoImpl implements ISysUserDao{

    private static final String SelectUserByName="SELECT sys_user.id,sys_user.user_name,sys_user.user_password,sys_user.salt,sys_user.department_id,sys_user.position_id,sys_user.role_id,sys_user.real_name,sys_user.gender,sys_user.birthday,sys_user.portrait,sys_user.email,sys_user.mobile,sys_user.qq,sys_user.wechat,sys_user.user_status,sys_user.remark,sys_department.department_name,sys_position.position_name,sys_role.role_name FROM sys_user\n" + "INNER JOIN sys_department ON sys_user.department_id=sys_department.id\n" + "INNER JOIN sys_position ON sys_user.position_id=sys_position.id\n" + "INNER JOIN sys_role ON sys_user.role_id=sys_role.id WHERE sys_user.is_deleted=0 AND sys_user.user_name=?";
    private static final String SelectPageList="SELECT sys_user.gmt_create,sys_user.id,sys_user.user_name,sys_user.user_password,sys_user.salt,sys_user.department_id,sys_user.position_id,sys_user.role_id,sys_user.real_name,sys_user.gender,sys_user.birthday,sys_user.portrait,sys_user.email,sys_user.mobile,sys_user.qq,sys_user.wechat,sys_user.user_status,sys_user.remark,sys_department.department_name,sys_position.position_name,sys_role.role_name \n" + "FROM sys_user\n" + "INNER JOIN sys_department ON sys_user.department_id=sys_department.id\n" + "INNER JOIN sys_position ON sys_user.position_id=sys_position.id\n" + "INNER JOIN sys_role ON sys_user.role_id=sys_role.id\n" + "WHERE sys_user.is_deleted=0 AND sys_user.user_name LIKE ? AND real_name LIKE ? AND mobile LIKE ? #1# #2# #3# #4# LIMIT ?,?";//AND department_id IN   AND user_status=? AND gmt_create>=''  AND gmt_create<=''
    private static final String SelectForExport="SELECT sys_user.gmt_create,sys_user.id,sys_user.user_name,sys_user.user_password,sys_user.salt,sys_user.department_id,sys_user.position_id,sys_user.role_id,sys_user.real_name,sys_user.gender,sys_user.birthday,sys_user.portrait,sys_user.email,sys_user.mobile,sys_user.qq,sys_user.wechat,sys_user.user_status,sys_user.remark ,sys_department.department_name,sys_position.position_name,sys_role.role_name FROM sys_user INNER JOIN sys_department ON sys_user.department_id = sys_department.id INNER JOIN sys_position ON sys_user.position_id = sys_position.id INNER JOIN sys_role ON sys_user.role_id = sys_role.id WHERE sys_user.is_deleted=0 AND sys_user.user_name LIKE ? AND real_name LIKE ? AND mobile LIKE ? #1# #2# #3# #4#";//AND department_id IN   AND user_status = ?  AND gmt_create>= '' AND gmt_create<= ''
    private static final String CountAll="SELECT COUNT(sys_user.id)  FROM sys_user INNER JOIN sys_department ON sys_user.department_id=sys_department.id INNER JOIN  sys_position ON sys_user.position_id=sys_position.id INNER JOIN sys_role ON sys_user.role_id=sys_role.id WHERE sys_user.is_deleted=0 AND sys_user.user_name LIKE ? AND real_name LIKE ? AND mobile LIKE ? #1# #2# #3# #4# ";
    private static final String SelectById="SELECT id,user_name,user_password,salt,department_id,position_id,role_id,real_name,gender,birthday,portrait,email,mobile,qq,wechat,user_status,remark FROM sys_user WHERE id=?";
    private static final String Insert="INSERT INTO sys_user(gmt_create,is_deleted,user_name,user_password,salt,department_id,position_id,role_id,real_name,gender,birthday,portrait,email,mobile,qq,wechat,user_status,remark) VALUE(NOW(),0,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String Update="UPDATE sys_user SET user_name=?,user_password=?,salt=?,department_id=?,position_id=?,role_id=?,real_name=?,gender=?,birthday=?,portrait=?,email=?,mobile=?,qq=?,wechat=?,user_status=?,remark=?,gmt_modified=NOW() WHERE id=?";
    private static final String DeleteById="UPDATE sys_user SET is_deleted=1 WHERE id=?";
    //统计正在用的用户数（为删除做准备）
    private static final String CountUserByPositionId="SELECT COUNT(id) FROM sys_user WHERE is_deleted=0 AND position_id=?";
    private static final String CountUserByRoleId="SELECT COUNT(id) FROM sys_user WHERE is_deleted=0 AND role_id=?";
    private static final String CountUserByDepartmentId="SELECT COUNT(id) FROM sys_user WHERE is_deleted=0 AND department_id=?";
    @Override
    public UserVo selectUserByName(String userName) {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        UserVo userVo=null;
        try {
            conn= JdbcUtils.getConnection();//获取数据库连接
            ps=conn.prepareStatement(SelectUserByName);
            ps.setString(1,userName);

            rs=ps.executeQuery();
            while (rs.next()){
                userVo=new UserVo();
                userVo.setId(rs.getInt("id"));
                userVo.setUserName(rs.getString("user_name"));
                userVo.setUserPassword(rs.getString("user_password"));
                userVo.setSalt(rs.getString("salt"));
                userVo.setDepartmentId(rs.getInt("department_id"));
                userVo.setPositionId(rs.getInt("position_id"));
                userVo.setRoleId(rs.getInt("role_id"));
                userVo.setRealName(rs.getString("real_name"));
                userVo.setGender(rs.getByte("gender"));
                userVo.setPortrait(rs.getString("portrait"));
                userVo.setBirthday(new java.util.Date(rs.getDate("birthday").getTime()));
                userVo.setEmail(rs.getString("email"));
                userVo.setMobile(rs.getString("mobile"));
                userVo.setQq(rs.getString("qq"));
                userVo.setWechat(rs.getString("wechat"));
                userVo.setUserStatus(rs.getByte("user_status"));
                userVo.setRemark(rs.getString("remark"));
                userVo.setDepartmentName(rs.getString("department_name"));
                userVo.setPositionName(rs.getString("position_name"));
                userVo.setRoleName(rs.getString("role_name"));


            }
            System.out.println("输出为"+userVo);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }


        return userVo;
    }

    /**
     * 查询分页数据
     * @param page 页数
     * @param limit 每页数据条数
     * @param departmentIds 部门id
     * @param userName 用户名
     * @param realName 姓名
     * @param mobile 手机号
     * @param userStatus 用户状态
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    @Override
    public List<UserVo> selectPageList(int page, int limit, Integer[] departmentIds, String userName, String realName, String mobile, Integer userStatus, String startDate, String endDate) {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;

        List<UserVo> rList=new ArrayList<>();
        UserVo userVo=null;
        try {
            String strSql=SelectPageList;
            //部门ids
            if(departmentIds!=null&&departmentIds.length>0){
                strSql=strSql.replace("#1#","AND sys_user.department_id IN "+ Arrays.toString(departmentIds).replace("[","(").replace("]",")"));
            }else {
                strSql=strSql.replace("#1#","");
            }
            //用户状态
            if(userStatus!=null){
                strSql=strSql.replace("#2#","AND sys_user.user_status = "+userStatus);
            }else {
                strSql=strSql.replace("#2#","");
            }
            //开始时间
            if(Tools.isNotNull(startDate)){
                strSql=strSql.replace("#3#","AND sys_user.gmt_create>= '"+startDate+"'");
            }else {
                strSql=strSql.replace("#3#","");
            }
            //结束时间
            if(Tools.isNotNull(endDate)){
                strSql=strSql.replace("#4#","AND sys_user.gmt_create<= '"+endDate+"'");
            }else {
                strSql=strSql.replace("#4#","");
            }

            conn=JdbcUtils.getConnection();
            ps=conn.prepareStatement(strSql);
            //sys_user.user_name LIKE ? AND real_name LIKE ? AND mobile LIKE ?
            if(userName==null) userName="";
            if(realName==null) realName="";
            if(mobile==null) mobile="";
            ps.setString(1,"%"+userName+"%");
            ps.setString(2,"%"+realName+"%");
            ps.setString(3,"%"+mobile+"%");
            ps.setInt(4,(page-1)*limit);
            ps.setInt(5,limit);
            rs= ps.executeQuery();
            while (rs.next()){
                //gmt_create,id,user_name,user_password,salt,department_id,position_id
                // ,role_id,real_name,gender,birthday,portrait,email,mobile,qq
                // ,wechat,user_status,remark,department_name,position_name,role_name
                userVo=new UserVo();
                userVo.setId(rs.getInt("id"));
                userVo.setUserName(rs.getString("user_name"));
                userVo.setUserPassword(rs.getString("user_password"));
                userVo.setSalt(rs.getString("salt"));
                userVo.setDepartmentId(rs.getInt("department_id"));
                userVo.setPositionId(rs.getInt("position_id"));
                userVo.setRoleId(rs.getInt("role_id"));
                userVo.setRealName(rs.getString("real_name"));
                userVo.setGender(rs.getByte("gender"));
                userVo.setPortrait(rs.getString("portrait"));
                userVo.setBirthday(new java.util.Date(rs.getDate("birthday").getTime()));
                userVo.setEmail(rs.getString("email"));
                userVo.setMobile(rs.getString("mobile"));
                userVo.setQq(rs.getString("qq"));
                userVo.setWechat(rs.getString("wechat"));
                userVo.setUserStatus(rs.getByte("user_status"));
                userVo.setRemark(rs.getString("remark"));
                userVo.setDepartmentName(rs.getString("department_name"));
                userVo.setPositionName(rs.getString("position_name"));
                userVo.setRoleName(rs.getString("role_name"));
                userVo.setGmtCreate(new Date(rs.getTimestamp("gmt_create").getTime()));

                rList.add(userVo);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }
        return rList;
    }

    /**
     * 导出数据
     */
    @Override
    public List<UserVo> selectForExport(Integer[] departmentIds, String userName, String realName, String mobile, Integer userStatus, String startDate, String endDate) {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;

        List<UserVo> rList=new ArrayList<>();
        UserVo userVo=null;
        try {
            String strSql=SelectForExport;
            //部门ids
            if (departmentIds!=null && departmentIds.length>0){
                strSql=strSql.replace("#1#","AND sys_user.department_id IN "+ Arrays.toString(departmentIds).replace("[","(").replace("]",")"));
            }else {
                strSql=strSql.replace("#1#","");
            }
            //用户状态 userStatus
            if (userStatus!=null){
                strSql=strSql.replace("#2#","AND sys_user.user_status = "+userStatus);
            }else {
                strSql=strSql.replace("#2#","");
            }
            //开始时间
            if (Tools.isNotNull(startDate)){
                strSql=strSql.replace("#3#","AND sys_user.gmt_create >= '"+ startDate+"'");
            }else {
                strSql=strSql.replace("#3#","");
            }
            //结束时间
            if (Tools.isNotNull(endDate)){
                strSql=strSql.replace("#4#","AND sys_user.gmt_create <= '"+ endDate+"'");
            }else {
                strSql=strSql.replace("#4#","");
            }

            conn= JdbcUtils.getConnection();
            ps=conn.prepareStatement(strSql);
            // sys_user.user_name LIKE ? AND real_name LIKE ? AND mobile LIKE ?
            if (userName==null) userName="";
            if (realName==null) realName="";
            if (mobile==null) mobile="";
            ps.setString(1,"%"+userName+"%");
            ps.setString(2,"%"+realName+"%");
            ps.setString(3,"%"+mobile+"%");

            rs=ps.executeQuery();
            while (rs.next()){
                userVo=new UserVo();
                userVo.setId(rs.getInt("id"));
                userVo.setUserName(rs.getString("user_name"));
                userVo.setUserPassword(rs.getString("user_password"));
                userVo.setSalt(rs.getString("salt"));
                userVo.setDepartmentId(rs.getInt("department_id"));
                userVo.setPositionId(rs.getInt("position_id"));
                userVo.setRoleId(rs.getInt("role_id"));
                userVo.setRealName(rs.getString("real_name"));
                userVo.setGender(rs.getByte("gender"));
                userVo.setPortrait(rs.getString("portrait"));
                userVo.setBirthday(new java.util.Date(rs.getDate("birthday").getTime()));
                userVo.setEmail(rs.getString("email"));
                userVo.setMobile(rs.getString("mobile"));
                userVo.setQq(rs.getString("qq"));
                userVo.setWechat(rs.getString("wechat"));
                userVo.setUserStatus(rs.getByte("user_status"));
                userVo.setRemark(rs.getString("remark"));
                userVo.setDepartmentName(rs.getString("department_name"));
                userVo.setPositionName(rs.getString("position_name"));
                userVo.setRoleName(rs.getString("role_name"));
                userVo.setGmtCreate(new Date(rs.getTimestamp("gmt_create").getTime()));

                rList.add(userVo);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }
        return rList;
    }

    /**
     * 根据id查询用户数据（为修改做准备）
     * @param id
     * @return
     */
    @Override
    public SysUser selectById(int id) {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        SysUser user=null;
        try {
            conn= JdbcUtils.getConnection();
            ps=conn.prepareStatement(SelectById);
            ps.setInt(1,id);
            rs=ps.executeQuery();
            while (rs.next()){
                user=new SysUser();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("user_name"));
                user.setUserPassword(rs.getString("user_password"));
                user.setSalt(rs.getString("salt"));
                user.setDepartmentId(rs.getInt("department_id"));
                user.setPositionId(rs.getInt("position_id"));
                user.setRoleId(rs.getInt("role_id"));
                user.setRealName(rs.getString("real_name"));
                user.setGender(rs.getByte("gender"));
                user.setPortrait(rs.getString("portrait"));
                user.setBirthday(new java.util.Date(rs.getDate("birthday").getTime()));
                user.setEmail(rs.getString("email"));
                user.setMobile(rs.getString("mobile"));
                user.setQq(rs.getString("qq"));
                user.setWechat(rs.getString("wechat"));
                user.setUserStatus(rs.getByte("user_status"));
                user.setRemark(rs.getString("remark"));

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }
        return user;
    }

    @Override
    public int countAll(Integer[] departmentIds, String userName, String realName, String mobile, Integer userStatus, String startDate, String endDate) {
        int intR=0;
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            String strSql=CountAll;
            //部门ids
            if(departmentIds!=null&&departmentIds.length>0){
                strSql=strSql.replace("#1#","AND sys_user.department_id IN "+ Arrays.toString(departmentIds).replace("[","(").replace("]",")"));
            }else {
                strSql=strSql.replace("#1#","");
            }
            //用户状态
            if(userStatus!=null){
                strSql=strSql.replace("#2#","AND sys_user.user_status = '"+userStatus+"'");
            }else {
                strSql=strSql.replace("#2#","");
            }
            //开始时间
            if(Tools.isNotNull(startDate)){
                strSql=strSql.replace("#3#","AND sys_user.gmt_create>= '"+startDate+"'");
            }else {
                strSql=strSql.replace("#3#","");
            }
            //结束时间
            if(Tools.isNotNull(endDate)){
                strSql=strSql.replace("#4#","AND sys_user.gmt_create<= "+endDate);
            }else {
                strSql=strSql.replace("#4#","");
            }

            conn=JdbcUtils.getConnection();
            ps=conn.prepareStatement(strSql);
            //sys_user.user_name LIKE ? AND real_name LIKE ? AND mobile LIKE ?
            if(userName==null) userName="";
            if(realName==null) realName="";
            if(mobile==null) mobile="";
            ps.setString(1,"%"+userName+"%");
            ps.setString(2,"%"+realName+"%");
            ps.setString(3,"%"+mobile+"%");
            //执行查询
            rs=ps.executeQuery();
            while (rs.next()){
                intR=rs.getInt(1);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }

        return intR;
    }

    /**
     * 统计在使用 指定职位的用户数（为删除做准备的）
     * @param positionId 职位id
     * @return
     */
    @Override
    public int countUserByPositionId(int positionId) {
        int intR=0;
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn= JdbcUtils.getConnection();
            ps=conn.prepareStatement(CountUserByPositionId);
           ps.setInt(1,positionId);
            //执行查询
            rs=ps.executeQuery();
            while (rs.next()){
                intR=rs.getInt(1);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }

        return intR;
    }

    @Override
    public int countUserByRoleId(int roleId) {
        int intR=0;
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn= JdbcUtils.getConnection();
            ps=conn.prepareStatement(CountUserByRoleId);
            ps.setInt(1,roleId);
            //执行查询
            rs=ps.executeQuery();
            while (rs.next()){
                intR=rs.getInt(1);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }

        return intR;
    }

    /**
     * 统计在使用 指定部门的用户数
     * @param departmentId 部门id
     * @return
     */
    @Override
    public int countUserByDepartmentId(int departmentId) {
        int intR=0;
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn= JdbcUtils.getConnection();
            ps=conn.prepareStatement(CountUserByDepartmentId);
            ps.setInt(1,departmentId);
            //执行查询
            rs=ps.executeQuery();
            while (rs.next()){
                intR=rs.getInt(1);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }

        return intR;
    }

    /**
     * 新增用户数据
     * @param user 用户数据
     * @return 是否成功
     */
    @Override
    public boolean insert(SysUser user) {
        boolean bolR=false;
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn=JdbcUtils.getConnection();
            ps=conn.prepareStatement(Insert);
            //gmt_create,is_deleted,user_name,user_password,salt,department_id
            // ,position_id,role_id,real_name,gender,birthday,portrait,email
            // ,mobile,qq,wechat,user_status,remark
            ps.setString(1,user.getUserName());
            ps.setString(2,user.getUserPassword());
            ps.setString(3,user.getSalt());
            ps.setInt(4,user.getDepartmentId());
            ps.setInt(5,user.getPositionId());
            ps.setInt(6,user.getRoleId());
            ps.setString(7,user.getRealName());
            ps.setByte(8,user.getGender());
            ps.setDate(9,new java.sql.Date(user.getBirthday().getTime()));
            ps.setString(10,user.getPortrait());
            ps.setString(11,user.getEmail());
            ps.setString(12,user.getMobile());
            ps.setString(13,user.getQq());
            ps.setString(14,user.getWechat());
            ps.setByte(15,user.getUserStatus());
            ps.setString(16, user.getRemark());
            //执行新增
            int row=ps.executeUpdate();
            bolR=row==1;//新增1行受影响

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }
        return bolR;
    }

    /**
     * 修改用户数据
     * @param user 用户数据
     * @return 是否成功
     */
    @Override
    public boolean update(SysUser user) {
        boolean bolR=false;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            ps = conn.prepareStatement(Update);
            //SET user_name=?,user_password=?,salt=?,department_id=?,position_id=?,role_id=?,real_name=?,gender=?,birthday=?,portrait=?,email=?,mobile=?,qq=?,wechat=?,user_status=?,remark=?,gmt_modified=NOW() WHERE id=?
            ps.setString(1,user.getUserName());
            ps.setString(2,user.getUserPassword());
            ps.setString(3,user.getSalt());
            ps.setInt(4,user.getDepartmentId());
            ps.setInt(5,user.getPositionId());
            ps.setInt(6,user.getRoleId());
            ps.setString(7,user.getRealName());
            ps.setByte(8,user.getGender());
            ps.setDate(9,new java.sql.Date(user.getBirthday().getTime()));
            ps.setString(10,user.getPortrait());
            ps.setString(11,user.getEmail());
            ps.setString(12,user.getMobile());
            ps.setString(13,user.getQq());
            ps.setString(14,user.getWechat());
            ps.setByte(15,user.getUserStatus());
            ps.setString(16, user.getRemark());
            ps.setInt(17,user.getId());

            //执行修改
            int row=ps.executeUpdate();
            bolR=row==1;//修改1行受影响
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JdbcUtils.close(conn,ps,rs);
        }
        return bolR;
    }

    /**
     * 删除用户数据
     * @param id 用户id
     * @return 是否成功
     */
    @Override
    public boolean deleteById(int id) {
        //逻辑删除 本质就是修改
        boolean bolR=false;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            ps = conn.prepareStatement(DeleteById);
            // WHERE id=?
            ps.setInt(1,id);

            //执行修改
            int row=ps.executeUpdate();
            bolR=row==1;//修改1行受影响
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JdbcUtils.close(conn,ps,rs);
        }
        return bolR;
    }
}
