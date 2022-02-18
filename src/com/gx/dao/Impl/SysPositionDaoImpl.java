package com.gx.dao.Impl;

import com.gx.po.SysPosition;
import com.gx.util.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SysPositionDaoImpl implements ISysPositionDao {
    private static final String SelectPageList="SELECT id,gmt_create,position_name,position_sort,position_status,remark\n" + "FROM sys_position\n" + "WHERE position_name LIKE ? ### \n" + "ORDER BY position_sort \n" + "LIMIT ?,?";
    private static final String CountAll="SELECT COUNT(id)\n" + "FROM sys_position\n" + "WHERE position_name\n" + "LIKE ? ###";
    private static final String SelectAll="SELECT id,gmt_create,position_name,position_sort,position_status,remark FROM sys_position";
    private static final String SelectById="SELECT id,gmt_create,position_name,position_sort,position_status,remark\n" + "FROM sys_position\n" + "WHERE id=?";
    private static final String Insert="INSERT INTO sys_position(gmt_create,position_name,position_sort,position_status,remark)\n" + "VALUE (NOW(),?,?,?,?)";
    private static final String Update="UPDATE sys_position\n" + "SET position_name=?,position_sort=?,position_status=?,remark=?,gmt_modified=NOW()\n" + "WHERE id=?";
    private static final String DeleteById="DELETE FROM sys_position WHERE id=?";
    private static final String UpdateSortPlus1="UPDATE sys_position SET position_sort=position_sort+1 WHERE 1=1 #1# #2#";//AND position_sort>= minSort AND position_sort<=maxSort
    private static final String UpdateSortMinus1="UPDATE sys_position SET position_sort=position_sort-1 WHERE 1=1 #1# #2#";//AND position_sort>=minSort AND position_sort<=maxSort
    /**
     * 按照分页查询职位表
     * @param page//当前页数
     * @param limit//每页数据条数
     * @param searchName//查询名称
     * @param status//分页数据
     * @return
     */
    @Override
    public List<SysPosition> selectForPageList(int page, int limit, String searchName, Integer status) {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        List<SysPosition> rList=new ArrayList<>();
        try {
            String strQuerySql=SelectPageList;
            //判断状态
            if(status!=null){//AND position_status=1
                strQuerySql=strQuerySql.replace("###","AND position_status="+status);
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
            SysPosition position;
            rs=ps.executeQuery();
            while (rs.next()){
                position=new SysPosition();
                //取值
                position.setId(rs.getInt("id"));//主键
                position.setGmtCreate(new java.util.Date(rs.getTimestamp("gmt_create").getTime()));
                position.setPositionName(rs.getString("position_name"));
                position.setPositionSort(rs.getInt("position_sort"));
                position.setPositionStatus(rs.getByte("position_status"));
                position.setRemark(rs.getString("remark"));
                rList.add(position);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }
        return rList;
    }

    /**
     * 返回查询的总条数
     * @param searchName 查询名称
     * @param status 状态
     * @return
     */
    @Override
    public int countAll(String searchName, Integer status) {
        int intR=0;
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            String strQuerySql=CountAll;
            //判断状态
            if(status!=null){//AND position_status=1
                strQuerySql=strQuerySql.replace("###","AND position_status="+status);
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

    /**
     * 根据id查询职位信息
     * @param id
     * @return
     */
    @Override
    public SysPosition selectById(int id) {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        SysPosition position=null;
        try {
            conn= JdbcUtils.getConnection();
            ps=conn.prepareStatement(SelectById);
            //设置参数
           ps.setInt(1,id);
            //执行查询
            rs=ps.executeQuery();
            while (rs.next()){
                position=new SysPosition();
                //取值
                position.setId(rs.getInt("id"));//主键
                position.setGmtCreate(new java.util.Date(rs.getTimestamp("gmt_create").getTime()));
                position.setPositionName(rs.getString("position_name"));
                position.setPositionSort(rs.getInt("position_sort"));
                position.setPositionStatus(rs.getByte("position_status"));
                position.setRemark(rs.getString("remark"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }
        return position;
    }

    @Override
    public List<SysPosition> selectAll() {

        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        List<SysPosition> rList = new ArrayList<>();
        try {

            conn= JdbcUtils.getConnection();
            ps=conn.prepareStatement(SelectAll);

            //执行查询
            SysPosition position;
            rs=ps.executeQuery();
            while (rs.next()){
                position=new SysPosition();
                //取值
                position.setId(rs.getInt("id"));//主键
                position.setGmtCreate(new java.util.Date(rs.getTimestamp("gmt_create").getTime()));//创建时间
                position.setPositionName(rs.getString("position_name"));//职位名称
                position.setPositionSort(rs.getInt("position_sort"));//职位显示顺序
                position.setPositionStatus(rs.getByte("position_status"));//职位状态
                position.setRemark(rs.getString("remark"));//备注
                //添加到list
                rList.add(position);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }

        return rList;
    }

    /**
     * 新增职位
     * @param position 职位数据
     * @return
     */
    @Override
    public boolean insert(SysPosition position) {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        boolean boolR=false;
        try {
            conn= JdbcUtils.getConnection();
            ps=conn.prepareStatement(Insert);
            //设置参数 position_name   position_sort  position_status  remark
            ps.setString(1,position.getPositionName());
            ps.setInt(2,position.getPositionSort());
            ps.setByte(3,position.getPositionStatus());
            ps.setString(4,position.getRemark());
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

    /**
     * 修改职位
     * @param position
     * @return
     */
    @Override
    public boolean update(SysPosition position) {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        boolean boolR=false;
        try {
            conn= JdbcUtils.getConnection();
            ps=conn.prepareStatement(Update);
            //设置参数 sys_position position_name=?,position_sort=?,position_status=?,remark=?,gmt_modified=NOW() WHERE id=?
            ps.setString(1,position.getPositionName());
            ps.setInt(2,position.getPositionSort());
            ps.setByte(3,position.getPositionStatus());
            ps.setString(4,position.getRemark());
            ps.setInt(5,position.getId());//WHERE id=?
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

    /**
     * 根据id删除职位信息
     * @param id
     * @return
     */
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

    @Override
    public boolean updateSortPlus1(Integer minSort, Integer maxValue) {
        if(minSort==null && maxValue==null)return false;
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        boolean boolR=false;
        try {
            conn= JdbcUtils.getConnection();
            String strSql=UpdateSortPlus1;
            if(minSort!=null){
                strSql=strSql.replace("#1#","AND position_sort>= "+minSort);
            }else {
                strSql=strSql.replace("#1#","");
            }

            if(maxValue!=null){
                strSql=strSql.replace("#2#","AND position_sort<= "+maxValue);
            }else {
                strSql=strSql.replace("#2#","");
            }
            ps=conn.prepareStatement(strSql);
            //执行
            ps.executeUpdate();
            boolR=true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }

        return boolR;
    }

    @Override
    public boolean updateSortMinus1(Integer minSort, Integer maxValue) {
        if(minSort==null && maxValue==null)return false;
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        boolean boolR=false;
        try {
            conn= JdbcUtils.getConnection();
            String strSql=UpdateSortMinus1;
            if(minSort!=null){
                strSql=strSql.replace("#1#","AND position_sort>= "+minSort);
            }else {
                strSql=strSql.replace("#1#","");
            }
            if(maxValue!=null){
                strSql=strSql.replace("#2#","AND position_sort<= "+maxValue);
            }else {
                strSql=strSql.replace("#2#","");
            }
            ps=conn.prepareStatement(strSql);
            //执行
            ps.executeUpdate();
            boolR=true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }

        return boolR;
    }
}
