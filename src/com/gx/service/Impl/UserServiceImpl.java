package com.gx.service.Impl;

import com.gx.dao.Impl.*;
import com.gx.po.SysDepartment;
import com.gx.po.SysPosition;
import com.gx.po.SysRole;
import com.gx.po.SysUser;
import com.gx.vo.*;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements IUserService{
    //dao
    private  final ISysDepartmentDao departmentDao=new SysDepartmentDaoImpl();
    private final ISysUserDao userDao=new SysUserDaoImpl();
    private final ISysPositionDao positionDao=new SysPositionDaoImpl();
    private final ISysRoleDao roleDao=new SysRoleDaoImpl();

    /**
     * 查询部门下拉树
     * @return
     */
    @Override
    public List<LayuiTreeVo> selectDepartmentForTree() {
        List<SysDepartment> departmentList=this.departmentDao.selectAll();
        return dealLayuiTreeVo(departmentList,0);
    }

    /**
     * 查找分页数据
     * @param page 页数
     * @param limit 每页数据条数
     * @param departmentId 部门id
     * @param userName 用户名
     * @param realName 姓名
     * @param mobile 手机号
     * @param userStatus 用户状态
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    @Override
    public LayuiTableData<UserVo> selectPageList(int page, int limit, Integer departmentId, String userName
    , String realName, String mobile, Integer userStatus, String startDate, String endDate) {
        //获取所选部门的子节点的id
        Integer[] departmentIds=new Integer[]{};
        if(departmentId!=null){
            List<SysDepartment> departmentList=this.departmentDao.selectAll();
            //查找选中部门id 下的子节点id
            List<Integer> departmentIdList=getChildrenIds(departmentList,departmentId);
            departmentIdList.add(departmentId);
            //List 转 数组
            departmentIds=new Integer[departmentIdList.size()];
            departmentIdList.toArray(departmentIds);
        }
        List<UserVo> data=this.userDao.selectPageList(page,limit,departmentIds,userName,realName,mobile,userStatus,startDate,endDate);
        int count=this.userDao.countAll(departmentIds,userName,realName,mobile,userStatus,startDate,endDate);
        return new LayuiTableData<>(count,data);
    }

    /**
     * 导出数据
     */
    @Override
    public List<UserVo> selectForExport(Integer departmentId, String userName, String realName, String mobile, Integer userStatus, String startDate, String endDate) {
        //获取所选部门的子节点的id
        Integer[] departmentIds=new Integer[]{};
        if(departmentId!=null){
            List<SysDepartment> departmentList=this.departmentDao.selectAll();
            //查找选中部门id 下的子节点id
            List<Integer> departmentIdList=getChildrenIds(departmentList,departmentId);
            departmentIdList.add(departmentId);
            //List 转 数组
            departmentIds=new Integer[departmentIdList.size()];
            departmentIdList.toArray(departmentIds);
        }
        return this.userDao.selectForExport(departmentIds,userName,realName,mobile,userStatus,startDate,endDate);
    }

    @Override
    public SysUser selectById(int id) {
        return this.userDao.selectById(id);
    }

    @Override
    public List<TreeSelectVo> selectForTreeSelect() {
        //获取所有的部门数据
        List<SysDepartment> departmentList = this.departmentDao.selectAll();
        return this.dealTreeSelect(departmentList,0);
    }

    @Override
    public List<H5SelectVo> selectPositionForH5Select() {
        List<SysPosition>  positionList=this.positionDao.selectAll();
        List<H5SelectVo> rList=new ArrayList<>();
        for (SysPosition position:positionList) {
            rList.add(new H5SelectVo(String.valueOf(position.getId()),position.getPositionName()));
        }
        return rList;
    }

    @Override
    public List<H5SelectVo> selectRoleForH5Select() {
        List<SysRole> roleList=this.roleDao.selectAll();
        List<H5SelectVo> rList=new ArrayList<>();
        for (SysRole role:roleList) {
            rList.add(new H5SelectVo(String.valueOf(role.getId()),role.getRoleName()));
        }
        return rList;
    }

    @Override
    public boolean insert(SysUser user) {
        return this.userDao.insert(user);
    }

    @Override
    public boolean update(SysUser user) {
        return this.userDao.update(user);
    }

    @Override
    public boolean deleteById(int id) {
        return this.userDao.deleteById(id);
    }

    /**
     * List<SysDepartment> 转 List<LayuiTreeVo>
     * @param listSoure
     * @param pid
     * @return
     */
    private List<LayuiTreeVo> dealLayuiTreeVo(List<SysDepartment> listSoure,int pid){
        List<LayuiTreeVo> rList=new ArrayList<>();
        LayuiTreeVo layuiTreeVo=null;
        for (SysDepartment department:listSoure) {
            if(department.getParentId()==pid){
                layuiTreeVo=new LayuiTreeVo();
                layuiTreeVo.setId(department.getId());
                layuiTreeVo.setTitle(department.getDepartmentName());
                layuiTreeVo.setSpread(true);//展开
                //获取子节点
                List<LayuiTreeVo> listChildren=dealLayuiTreeVo(listSoure,department.getId());
                if(listChildren.size()>0){
                    layuiTreeVo.setChildren(listChildren);
                }
                rList.add(layuiTreeVo);
            }
        }
        return rList;
    }
    private List<Integer> getChildrenIds(List<SysDepartment> listSource,int pid){
        List<Integer> rList=new ArrayList<>();
        for (SysDepartment department:listSource) {
            if(department.getParentId()==pid){
                rList.add(department.getId());
                //把子节点List 添加到父节点List
                rList.addAll(getChildrenIds(listSource,department.getId()));
            }
        }
        return rList;
    }

    /**
     * 递归查找部门下拉树的子节点
     */
    private List<TreeSelectVo> dealTreeSelect(List<SysDepartment> listSource, int pid) {
        List<TreeSelectVo> rList = new ArrayList<>();
        TreeSelectVo treeSelectVo=null;
        for (SysDepartment department : listSource) {
            if (department.getParentId()==pid){
                treeSelectVo=new TreeSelectVo();
                treeSelectVo.setId(department.getId());//id
                treeSelectVo.setName(department.getDepartmentName());//显示的值
                treeSelectVo.setChecked(false);
                treeSelectVo.setOpen(true);//默认展开

                List<TreeSelectVo> children=this.dealTreeSelect(listSource,department.getId());
                if (children.size()>0){
                    //有子节点
                    treeSelectVo.setChildren(children);
                }else {
                    //没有子节点
                    treeSelectVo.setChildren(null);
                }
                rList.add(treeSelectVo);
            }
        }

        return rList;
    }
}
