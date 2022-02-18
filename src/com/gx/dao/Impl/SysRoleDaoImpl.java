package com.gx.dao.Impl;

import com.gx.po.SysPosition;
import com.gx.po.SysRole;
import com.gx.util.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SysRoleDaoImpl implements ISysRoleDao{
    private static final String SelectPageList="SELECT id,gmt_create,role_name,role_sort,role_status,remark\n" + "FROM sys_role\n" + "WHERE role_name LIKE ? ### \n" + "ORDER BY role_sort \n" + "LIMIT ?,?";
    private static final String CountAll="SELECT COUNT(id)\n" + "FROM sys_role\n" + "WHERE role_name\n" + "LIKE ? ###";//AND role_status=1
    private static final String SelectAll="SELECT id,gmt_create,role_name,role_sort,role_status,remark FROM sys_role";
    private static final String SelectById="SELECT id,gmt_create,role_name,role_sort,role_status,remark\n" + "FROM sys_role\n" + "WHERE id=?";
    private static final String Insert="INSERT INTO sys_role(gmt_create,role_name,role_sort,role_status,remark)\n" + "VALUE (NOW(),?,?,?,?)";
    private static final String Update="UPDATE sys_role\n" + "SET role_name=?,role_sort=?,role_status=?,remark=?,gmt_modified=NOW()\n" + "WHERE id=?";
    private static final String DeleteById="DELETE FROM sys_role WHERE id=?";
    @Override
    public List<SysRole> selectForPageList(int page, int limit, String searchName, Integer status) {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        List<SysRole> rList=new ArrayList<>();
        try {
            String strQuerySql=SelectPageList;
            //判断状态
            if(status!=null){//AND position_status=1
                strQuerySql=strQuerySql.replace("###","AND role_status="+status);
            }else {
                strQuerySql=strQuerySql.replace("###","");
            }
            conn= JdbcUtils.getConnection();
            ps=conn.prepareStatement(strQuerySql);
            //处理searchName为null的情况
            if(searchName==null){
                searchName="";
            }
            //设置参数
            ps.setString(1,"%"+searchName+"%");//索引从1开始
            ps.setInt(2,(page-1)*limit);//数据开始索引(page-1)*limit
            ps.setInt(3,limit);
            //执行查询
            SysRole role;
            rs=ps.executeQuery();
            while (rs.next()){
                role=new SysRole();
                //取值id,gmt_create,role_name,role_sort,role_status,remark
                role.setId(rs.getInt("id"));//主键
                role.setGmtCreate(new java.util.Date(rs.getTimestamp("gmt_create").getTime()));
                role.setRoleName(rs.getString("role_name"));
                role.setRoleSort(rs.getInt("role_sort"));
                role.setRoleStatus(rs.getByte("role_status"));
                role.setRemark(rs.getString("remark"));
                rList.add(role);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }
        return rList;
    }

    @Override
    public int countAll(String searchName, Integer status) {
        int intR=0;
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            String strQuerySql=CountAll;
            //判断状态
            if(status!=null){//AND role_status=1
                strQuerySql=strQuerySql.replace("###","AND role_status="+status);
            }else {
                strQuerySql=strQuerySql.replace("###","");
            }
            conn= JdbcUtils.getConnection();
            ps=conn.prepareStatement(strQuerySql);
            //处理searchName为null的情况
            if(searchName==null){
                searchName="";
            }
            //设置参数
            ps.setString(1,"%"+searchName+"%");//索引从1开始
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
    public SysRole selectById(int id) {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        SysRole role=null;
        try {
            conn= JdbcUtils.getConnection();
            ps=conn.prepareStatement(SelectById);
            //设置参数
            ps.setInt(1,id);
            //执行查询
            rs=ps.executeQuery();
            while (rs.next()){
                role=new SysRole();
                //取值id,gmt_create,role_name,role_sort,role_status,remark
                role.setId(rs.getInt("id"));//主键
                role.setGmtCreate(new java.util.Date(rs.getTimestamp("gmt_create").getTime()));
                role.setRoleName(rs.getString("role_name"));
                role.setRoleSort(rs.getInt("role_sort"));
                role.setRoleStatus(rs.getByte("role_status"));
                role.setRemark(rs.getString("remark"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }
        return role;
    }

    @Override
    public List<SysRole> selectAll() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<SysRole> rList = new ArrayList<>();
        try {
            conn = JdbcUtils.getConnection();
            ps = conn.prepareStatement(SelectAll);
            //执行查询
            SysRole role;
            rs = ps.executeQuery();
            while (rs.next()) {
                role=new SysRole();
                //取值 id,gmt_create,role_name,role_sort,role_status,remark
                role.setId(rs.getInt("id"));//主键
                role.setGmtCreate(new java.util.Date(rs.getTimestamp("gmt_create").getTime()));//创建时间
                role.setRoleName(rs.getString("role_name"));//角色名称
                role.setRoleSort(rs.getInt("role_sort"));//角色显示顺序
                role.setRoleStatus(rs.getByte("role_status"));//角色状态
                role.setRemark(rs.getString("remark"));//备注
                //添加到list
                rList.add(role);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JdbcUtils.close(conn,ps,rs);
        }
        return rList;
    }

    @Override
    public boolean insert(SysRole role) {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        boolean boolR=false;
        try {
            conn= JdbcUtils.getConnection();
            ps=conn.prepareStatement(Insert);
            //设置参数 gmt_create,role_name,role_sort,role_status,remark
            ps.setString(1,role.getRoleName());
            ps.setInt(2,role.getRoleSort());
            ps.setByte(3,role.getRoleStatus());
            ps.setString(4,role.getRemark());
            //执行
            int rowNum=ps.executeUpdate();
            boolR=rowNum==1;//新增1行数据受影响
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }

        return boolR;
    }

    @Override
    public boolean update(SysRole role) {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        boolean boolR=false;
        try {
            conn= JdbcUtils.getConnection();
            ps=conn.prepareStatement(Update);
            //设置参数 role_name=?,role_sort=?,role_status=?,remark=?,gmt_modified=NOW()
            ps.setString(1,role.getRoleName());
            ps.setInt(2,role.getRoleSort());
            ps.setByte(3,role.getRoleStatus());
            ps.setString(4,role.getRemark());
            ps.setInt(5,role.getId());//WHERE id=?
            //执行
            int rowNum=ps.executeUpdate();
            boolR=rowNum==1;//新增1行数据受影响
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }

        return boolR;
    }

    @Override
    public boolean deleteById(int id) {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        boolean boolR=false;
        try {
            conn= JdbcUtils.getConnection();
            ps=conn.prepareStatement(DeleteById);
            //设置参数 //WHERE id=?
            ps.setInt(1,id);
            //执行
            int rowNum=ps.executeUpdate();
            boolR=rowNum==1;//新增1行数据受影响
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }

        return boolR;
    }
}
