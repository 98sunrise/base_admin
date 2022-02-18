package com.gx.dao.Impl;

import com.gx.po.SysDepartment;
import com.gx.po.SysDepartment;
import com.gx.util.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SysDepartmentDaoImpl implements ISysDepartmentDao{
    private  static final String SelectAll="SELECT id,gmt_create,gmt_modified,parent_id,department_name,telephone,fax,email,principal,department_sort,remark\n" + "FROM sys_department ORDER BY department_sort";
    private  static final String SelectById="SELECT id,gmt_create,gmt_modified,parent_id,department_name,telephone,fax,email,principal,department_sort,remark\n" + "FROM sys_department WHERE id=?";
    private  static final String CountAllByPid="SELECT COUNT(id) FROM sys_department WHERE parent_id=?";
    private  static final String Insert="INSERT INTO sys_department(gmt_create,parent_id,department_name,telephone,fax,email,principal,department_sort,remark) VALUE (NOW(),?,?,?,?,?,?,?,?)";
    private  static final String Update="UPDATE sys_department SET parent_id=?,department_name=?,telephone=?,fax=?,email=?,principal=?,department_sort=?,remark=?,gmt_modified=NOW() WHERE id=?";
    private  static final String DeleteById="DELETE FROM sys_department WHERE id=?";
    private  static final String UpdateSortPlus1="UPDATE sys_department SET department_sort=department_sort+1 WHERE parent_id=? #1# #2#";//AND department_sort>=minSort AND department_sort<=maxSort
    private  static final String UpdateSortMinus1="UPDATE sys_department SET department_sort=department_sort-1 WHERE parent_id=? #1# #2#";//AND department_sort>=minSort AND department_sort<=maxSort;

    /**
     * 查询出所有的部门数据
     * @return
     */
    @Override
    public List<SysDepartment> selectAll() {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        List<SysDepartment> rList=new ArrayList<>();

        try {
            conn= JdbcUtils.getConnection();
            ps=conn.prepareStatement(SelectAll);
            //执行查询
            SysDepartment sysDepartment=null;
            rs=ps.executeQuery();
            while (rs.next()){
                //id,gmt_create,gmt_modified,parent_id,department_name,telephone
                // ,fax,email,principal,department_sort,remark
                sysDepartment=new SysDepartment();
                sysDepartment.setId(rs.getInt("id"));
                sysDepartment.setParentId(rs.getInt("parent_id"));
                sysDepartment.setDepartmentName(rs.getString("department_name"));
                sysDepartment.setTelephone(rs.getString("telephone"));
                sysDepartment.setFax(rs.getString("fax"));
                sysDepartment.setEmail(rs.getString("email"));
                sysDepartment.setPrincipal(rs.getString("principal"));
                sysDepartment.setDepartmentSort(rs.getInt("department_sort"));
                sysDepartment.setRemark(rs.getString("remark"));
                rList.add(sysDepartment);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }

        return rList;
    }

    /**
     * 根据id查询部门数据
     * @param id
     * @return
     */
    @Override
    public SysDepartment selectById(int id) {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        SysDepartment department=null;
        try {
            conn= JdbcUtils.getConnection();
            ps=conn.prepareStatement(SelectById);
            //设置参数
            ps.setInt(1,id);
            //执行查询
            rs=ps.executeQuery();
            while (rs.next()){
                department=new SysDepartment();
                //取值id,gmt_create,gmt_modified,parent_id,department_name,telephone,fax,email,principal,department_sort,remark
                department.setId(rs.getInt("id"));//主键
                department.setParentId(rs.getInt("parent_id"));
                department.setDepartmentName(rs.getString("department_name"));
                department.setTelephone(rs.getString("telephone"));
                department.setFax(rs.getString("fax"));
                department.setEmail(rs.getString("email"));
                department.setPrincipal(rs.getString("principal"));
                department.setDepartmentSort(rs.getInt("department_sort"));
                department.setRemark(rs.getString("remark"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }
        return department;
    }

    /**
     * 根据部门id查询出部门数据
     * @param pid
     * @return
     */
    @Override
    public int countAllByPid(int pid) {
        int intR=0;
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn= JdbcUtils.getConnection();
            ps=conn.prepareStatement(CountAllByPid);
            ps.setInt(1,pid);
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
     * 新增部门数据
     * @param department 部门数据
     * @return
     */
    @Override
    public boolean insert(SysDepartment department) {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        boolean boolR=false;
        try {
            conn= JdbcUtils.getConnection();
            ps=conn.prepareStatement(Insert);
            //设置参数gmt_create,parent_id,department_name,telephone
            // ,fax,email,principal,department_sort,remark
            ps.setInt(1,department.getParentId());
            ps.setString(2,department.getDepartmentName());
            ps.setString(3,department.getTelephone());
            ps.setString(4,department.getFax());
            ps.setString(5,department.getEmail());
            ps.setString(6,department.getPrincipal());
            ps.setInt(7,department.getDepartmentSort());
            ps.setString(8,department.getRemark());

            //执行查询
            int rowNum=ps.executeUpdate();
            boolR=rowNum==1;//新增一行数据受影响
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JdbcUtils.close(conn,ps,rs);
        }

        return boolR;
    }

    /**
     * 修改部门数据
     * @param department 部门数据
     * @return
     */
    @Override
    public boolean update(SysDepartment department) {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        boolean boolR=false;
        try {
            conn= JdbcUtils.getConnection();
            ps=conn.prepareStatement(Update);
            //设置参数 sys_department SET parent_id=?,department_name=?,telephone=?、
            // ,fax=?,email=?,principal=?,department_sort=?,remark=?,gmt_modified=NOW()
            ps.setInt(1,department.getParentId());
            ps.setString(2,department.getDepartmentName());
            ps.setString(3,department.getTelephone());
            ps.setString(4,department.getFax());
            ps.setString(5,department.getEmail());
            ps.setString(6,department.getPrincipal());
            ps.setInt(7,department.getDepartmentSort());
            ps.setString(8,department.getRemark());
            ps.setInt(9,department.getId());//WHERE id=?
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

    /**
     * 排序加 +1
     * @param pid
     * @param minSort 需要更新的最小排序（包含最小） 只是小于传null
     * @param maxValue 需要更新的最大排序（包含最大） 只是大于传null
     * @return
     */
    @Override
    public boolean updateSortPlus1(int pid, Integer minSort, Integer maxValue) {
        if(minSort==null && maxValue==null)return false;
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        boolean boolR=false;
        try {
            conn= JdbcUtils.getConnection();
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

    /**
     * 排序 -1 的情况
     * @param pid
     * @param minSort 需要更新的最小排序（包含最小） 只是小于传null
     * @param maxValue 需要更新的最大排序（包含最大） 只是大于传null
     * @return
     */
    @Override
    public boolean updateSortMinus1(int pid, Integer minSort, Integer maxValue) {
        if(minSort==null && maxValue==null)return false;
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        boolean boolR=false;
        try {
            conn= JdbcUtils.getConnection();
            String strSql=UpdateSortMinus1;
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
