package com.gx.dao.Impl;

import com.gx.po.SysMenu;
import com.gx.util.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SysMenuDaoImpl implements ISysMenuDao{
    private static final String SelectMenuByRoleId="SELECT sys_menu.id,sys_menu.parent_id,sys_menu.menu_name,sys_menu.menu_icon,sys_menu.menu_url\n" + "\t,sys_menu.menu_sort,sys_menu.menu_type,sys_menu.menu_status,sys_menu.authorize,sys_menu.remark\n" + "\tFROM sys_menu\n" + "\tINNER JOIN sys_menu_authorize ON sys_menu.id=sys_menu_authorize.menu_id\n" + "\t#1# ORDER BY sys_menu.menu_sort ASC";//WHERE AND sys_menu.menu_status=1 AND sys_menu_authorize.role_id=1
    private static final String SelectAll="SELECT id,gmt_create,gmt_modified,parent_id,menu_name,menu_icon,menu_url,menu_sort,menu_type,menu_status,authorize,remark FROM sys_menu ORDER BY menu_sort\n";
    private static final String SelectById ="SELECT id,gmt_create,gmt_modified,parent_id,menu_name,menu_icon,menu_url,menu_sort,menu_type,menu_status,authorize,remark FROM sys_menu WHERE id=?";
    private static final String CountAllByPid="SELECT COUNT(id) FROM sys_menu WHERE parent_id=?";
    private static final String Insert="INSERT INTO sys_menu(parent_id,menu_name,menu_icon,menu_url,menu_sort,menu_type,menu_status,authorize,remark,gmt_create) VALUE (?,?,?,?,?,?,?,?,?,NOW())";
    private static final String Update="UPDATE sys_menu SET parent_id=?,menu_name=?,menu_icon=?,menu_url=?,menu_sort=?,menu_type=?,menu_status=?,authorize=?,remark=?,gmt_modified=NOW() WHERE id=?";
    private static final String DeleteById="DELETE FROM sys_menu WHERE id=?";
    private static final String UpdateSortPlus1="UPDATE sys_menu SET menu_sort=menu_sort+1 WHERE parent_id=? #1# #2#";//AND menu_sort>=minSort AND menu_sort<=maxSort
    private static final String UpdateSortMinus1="UPDATE sys_menu SET menu_sort=menu_sort-1 WHERE parent_id=? #1# #2#";//AND menu_sort>= minSort AND menu_sort<= maxSort
    /**
     * ????????????id??????????????????
     * @param roleId
     * @return
     */
    @Override
    public List<SysMenu> selectMenuByRoleId(Integer roleId)  {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        List<SysMenu> rList=new ArrayList<>();
        try {
            String sql=SelectMenuByRoleId;
            if(roleId==null){//????????????
                sql=sql.replace("#1#","");
            }else {//??????????????????int???????????????????????????sql?????????????????????????????????????????????????????????????????????sql??????
                sql=sql.replace("#1#","WHERE sys_menu.menu_status=1 AND sys_menu_authorize.role_id="+roleId);
            }
            conn= JdbcUtils.getConnection();
            ps=conn.prepareStatement(sql);
            rs=ps.executeQuery();
            SysMenu menu=null;
            //id,gmt_create,gmt_modified,parent_id,menu_name,menu_icon,menu_url,menu_sort,menu_type,menu_status,authorize,remark
            while (rs.next()){
                menu=new SysMenu();
                menu.setId(rs.getInt("id"));
                menu.setParent_Id(rs.getInt("parent_id"));
                menu.setMenuName(rs.getString("menu_name"));
                menu.setMenuIcon(rs.getString("menu_icon"));
                menu.setMenuUrl(rs.getString("menu_url"));
                menu.setMenuSort(rs.getInt("menu_sort"));
                menu.setMenuType(rs.getByte("menu_type"));
                menu.setMenuStatus(rs.getByte("menu_status"));
                menu.setAuthroize(rs.getString("authorize"));
                menu.setRemark(rs.getString("remark"));
                rList.add(menu);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }


        return rList;
    }

    /**
     * ??????????????????
     * @return
     */
    @Override
    public List<SysMenu> selectAll() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<SysMenu> rList = new ArrayList<>();
        try {
            conn = JdbcUtils.getConnection();

            ps = conn.prepareStatement(SelectAll);
            //????????????
            SysMenu sysMenu=null;
            rs = ps.executeQuery();
            while (rs.next()) {
                //id,gmt_create,gmt_modified,parent_id,menu_name,menu_icon,menu_url,menu_sort,menu_type,menu_status,authorize,remark
                sysMenu=new SysMenu();
                sysMenu.setId(rs.getInt("id"));
                sysMenu.setParent_Id(rs.getInt("parent_id"));
                sysMenu.setMenuName(rs.getString("menu_name"));
                sysMenu.setMenuIcon(rs.getString("menu_icon"));
                sysMenu.setMenuUrl(rs.getString("menu_url"));
                sysMenu.setMenuSort(rs.getInt("menu_sort"));
                sysMenu.setMenuType(rs.getByte("menu_type"));
                sysMenu.setMenuStatus(rs.getByte("menu_status"));
                sysMenu.setAuthroize(rs.getString("authorize"));
                sysMenu.setRemark(rs.getString("remark"));

                rList.add(sysMenu);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JdbcUtils.close(conn, ps, rs);
        }
        return rList;
    }

    /**
     * ????????????id??????????????????
     * @param id ??????
     * @return
     */
    @Override
    public SysMenu selectById(int id) {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        SysMenu sysMenu=null;
        try {
            conn=JdbcUtils.getConnection();
            ps=conn.prepareStatement(SelectById);
            ps.setInt(1,id);
            //????????????
            rs = ps.executeQuery();
            while (rs.next()){
                //id,gmt_create,gmt_modified,parent_id,menu_name,menu_icon,menu_url,menu_sort,menu_type,menu_status,authorize,remark
                sysMenu=new SysMenu();
                sysMenu.setId(rs.getInt("id"));
                sysMenu.setParent_Id(rs.getInt("parent_id"));
                sysMenu.setMenuName(rs.getString("menu_name"));
                sysMenu.setMenuIcon(rs.getString("menu_icon"));
                sysMenu.setMenuUrl(rs.getString("menu_url"));
                sysMenu.setMenuSort(rs.getInt("menu_sort"));
                sysMenu.setMenuType(rs.getByte("menu_type"));
                sysMenu.setMenuStatus(rs.getByte("menu_status"));
                sysMenu.setAuthroize(rs.getString("authorize"));
                sysMenu.setRemark(rs.getString("remark"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn, ps, rs);
        }
        return sysMenu;
    }

    @Override
    public int countAllByPid(int pid) {
        int intR=0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn=JdbcUtils.getConnection();
            ps=conn.prepareStatement(CountAllByPid);
            ps.setInt(1,pid);
            //????????????
            rs = ps.executeQuery();
            while (rs.next()) {
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
     * ??????????????????
     * @param menu ????????????
     * @return
     */
    @Override
    public boolean insert(SysMenu menu) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        boolean boolR=false;
        try {
            conn=JdbcUtils.getConnection();
            ps=conn.prepareStatement(Insert);
            //????????????  parent_id,menu_name,menu_icon,menu_url,menu_sort
            // ,menu_type,menu_status,authorize,remark,gmt_create
            ps.setInt(1,menu.getParent_Id());
            ps.setString(2,menu.getMenuName());
            ps.setString(3,menu.getMenuIcon());
            ps.setString(4,menu.getMenuUrl());
            ps.setInt(5,menu.getMenuSort());
            ps.setByte(6,menu.getMenuType());
            ps.setByte(7,menu.getMenuStatus());
            ps.setString(8,menu.getAuthroize());
            ps.setString(9,menu.getRemark());
            //??????
            int rowNum=ps.executeUpdate();
            boolR=rowNum==1;//?????? 1??????????????????
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }
        return boolR;
    }

    /**
     * ??????????????????
     * @param menu ????????????
     * @return
     */
    @Override
    public boolean update(SysMenu menu) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        boolean boolR=false;
        try {
            conn=JdbcUtils.getConnection();
            ps=conn.prepareStatement(Update);
            //????????????  parent_id=?,menu_name=?,menu_icon=?,menu_url=?,menu_sort=?
            // ,menu_type=?,menu_status=?,authorize=?,remark=?,gmt_modified=NOW()
            ps.setInt(1,menu.getParent_Id());
            ps.setString(2,menu.getMenuName());
            ps.setString(3,menu.getMenuIcon());
            ps.setString(4,menu.getMenuUrl());
            ps.setInt(5,menu.getMenuSort());
            ps.setByte(6,menu.getMenuType());
            ps.setByte(7,menu.getMenuStatus());
            ps.setString(8,menu.getAuthroize());
            ps.setString(9,menu.getRemark());
            ps.setInt(10,menu.getId());
            //??????
            int rowNum= ps.executeUpdate();
            boolR=rowNum==1;//?????? 1??????????????????
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }

        return boolR;
    }

    /**
     * ??????????????????
     * @param id ??????id
     * @return
     */
    @Override
    public boolean deleteById(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        boolean boolR=false;
        try {
            conn = JdbcUtils.getConnection();

            ps = conn.prepareStatement(DeleteById);
            //???????????? WHERE id=?
            ps.setInt(1,id);
            //??????
            int rowNum= ps.executeUpdate();
            boolR=rowNum==1;//?????? 1??????????????????
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JdbcUtils.close(conn,ps,rs);
        }
        return boolR;
    }

    /**
     * ???????????? +1 by pid
     * @param pid ???id
     * @param minSort ????????????????????????????????????????????? ???????????????null
     * @param maxValue ????????????????????????????????????????????? ???????????????null
     * @return
     */
    @Override
    public boolean updateSortPlus1(int pid, Integer minSort, Integer maxValue) {
        if(minSort==null && maxValue==null)return false;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        boolean boolR=false;
        try {
            conn=JdbcUtils.getConnection();
            String strSql=UpdateSortPlus1;
            if(minSort!=null){
                strSql=strSql.replace("#1#","AND department_sort>= "+minSort);
            }else {
                strSql=strSql.replace("#1#","");
            }
            if(maxValue!=null){
                strSql=strSql.replace("#2#","AND department_sort<= "+maxValue);
            }else {
                strSql=strSql.replace("#2#","");
            }
            ps=conn.prepareStatement(strSql);
            ps.setInt(1,pid);
            //??????
            ps.executeUpdate();
            boolR=true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }
        return boolR;
    }

    /**
     * ???????????? -1 by pid
     * @param pid ???id
     * @param minSort ????????????????????????????????????????????? ???????????????null
     * @param maxValue ????????????????????????????????????????????? ???????????????null
     * @return
     */
    @Override
    public boolean updateSortMinus1(int pid, Integer minSort, Integer maxValue) {
        if(minSort==null && maxValue==null)return false;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        boolean boolR=false;
        try {
            conn = JdbcUtils.getConnection();

            String strSql=UpdateSortMinus1;
            if (minSort!=null){
                strSql=strSql.replace("#1#","AND department_sort>= "+minSort);
            }else {
                strSql=strSql.replace("#1#","");
            }

            if (maxValue!=null){
                strSql=strSql.replace("#2#","AND department_sort<= "+maxValue);
            }else {
                strSql=strSql.replace("#2#","");
            }

            ps = conn.prepareStatement(strSql);
            ps.setInt(1,pid);
            //??????
            ps.executeUpdate();
            boolR=true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JdbcUtils.close(conn,ps,rs);
        }
        return boolR;
    }
}
