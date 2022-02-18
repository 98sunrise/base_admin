package com.gx.service.Impl;

import com.gx.dao.Impl.ISysDepartmentDao;
import com.gx.dao.Impl.ISysUserDao;
import com.gx.dao.Impl.SysDepartmentDaoImpl;
import com.gx.dao.Impl.SysUserDaoImpl;
import com.gx.exception.MyException;
import com.gx.po.SysDepartment;
import com.gx.util.JdbcUtils;
import com.gx.vo.DepartmentTableTreeVo;
import com.gx.vo.LayuiTableData;
import com.gx.vo.TreeSelectVo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentServiceImpl implements IDepartmentService{
    //dao层
    private final ISysDepartmentDao departmentDao=new SysDepartmentDaoImpl();
    private final ISysUserDao userDao=new SysUserDaoImpl();
    /**
     * 根据表格查询出表格需要的数据
     * @return
     */
    @Override
    public LayuiTableData<DepartmentTableTreeVo> selectForTable() {
        List<SysDepartment> departmentList=this.departmentDao.selectAll();
        List<DepartmentTableTreeVo> data=dealDepartmentTableTreeList(departmentList,0);

        //组装layui table数据
        LayuiTableData<DepartmentTableTreeVo> layuiTableData=new LayuiTableData<>();
        layuiTableData.setCount(data.size());
        layuiTableData.setData(data);
        return layuiTableData;

    }

    /**
     * 查询下拉树 （）
     * @return
     */
    @Override
    public List<TreeSelectVo> SelectForTreeSelect() {
        //获取所有的部门数据
        List<SysDepartment> departmentList=this.departmentDao.selectAll();
        List<TreeSelectVo> rList=new ArrayList<>();
        //根不存在，添加是为了方便选择
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
     * 查询部门数据 by id
     * @param id
     * @return
     */
    @Override
    public SysDepartment selectById(int id) {
        return this.departmentDao.selectById(id);
    }

    /**
     * 根据父id查询所有数据
     * @param pid 父id
     * @return
     */
    @Override
    public int countAllByPid(int pid) {
        return this.departmentDao.countAllByPid(pid);
    }

    /**
     * 新增
     * @param department 部门数据
     * @return
     */
    @Override
    public boolean insert(SysDepartment department) {
        boolean bolR=false;
        try {
            JdbcUtils.beginTransaction();
            int nextSort=this.countAllByPid(department.getParentId())+1;
            //判断是否 小于nextSort，是，调小的序号 需要处理其他序号
            if(department.getDepartmentSort()<nextSort){
                boolean isOk=this.departmentDao
                        .updateSortPlus1(department.getParentId(),department.getDepartmentSort(),null);
                if(!isOk){
                    throw new SQLException("调整序号操作失败：pid="+department.getParentId()+";minSort"+department.getDepartmentSort());
                }
            }
            //新增
            boolean isOk=this.departmentDao.insert(department);
            if(isOk){
                bolR=true;
                //提交事务
                JdbcUtils.commitTransaction();

            }else {
                throw new SQLException("新增操作失败："+department);
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

    @Override
    public boolean update(SysDepartment department) {
        boolean bolR=false;
        try {
            //开启事务
            JdbcUtils.beginTransaction();
            //查出未修改的数据
            SysDepartment dbdepartment = this.departmentDao.selectById(department.getId());
            //a部门 1 2 3  5 6 7 8 把4调整到b部门去，上面的大于4的 - 1
            //b部门 1 4 2 3        下面大于插入的位置的数都 + 1
            //判断上级部门是否改变
            if (!dbdepartment.getParentId().equals(department.getParentId())) {
                //上级部门 发生改变
                //对于旧上级部门 大于原来的sort - 1
                boolean isOk = this.departmentDao.updateSortMinus1(dbdepartment.getParentId()
                        , (dbdepartment.getDepartmentSort() + 1), null);
                if (!isOk) {
                    throw new SQLException("处理序号操作失败：updateSortMinus1");
                }
                //对于新的上级部门 大于等于新sort =1
                isOk = this.departmentDao.updateSortPlus1(department.getParentId()
                        , department.getDepartmentSort(), null);
                if (!isOk) {
                    throw new SQLException("调整序号操作失败：pid=" + department.getParentId() + ";minSort=" + department.getDepartmentSort());
                }

            } else {
                //上级部门没有发生改变

                //判断修改后的序号 和 修改前的序号
                if (department.getDepartmentSort() < dbdepartment.getDepartmentSort()) {
                    //向前（小的方向）移动   大于等于新sort，小于旧的sort +1
                    boolean isOk = this.departmentDao.updateSortPlus1(dbdepartment.getParentId()
                            , department.getDepartmentSort(), (dbdepartment.getDepartmentSort() - 1));
                    if (!isOk) {
                        throw new SQLException("处理序号操作失败：updateSortPlus1");
                    }
                } else {
                    //向后（大的方向）移动  大于旧的sort，小于等于新的sort -1
                    boolean isOk = this.departmentDao.updateSortMinus1(dbdepartment.getParentId()
                            , (dbdepartment.getDepartmentSort() + 1), department.getDepartmentSort());
                    if (!isOk) {
                        throw new SQLException("处理序号操作失败：updateSortMinus1");
                    }
                }
            }
            //修改
            boolean isOk=this.departmentDao.update(department);
            if(!isOk){
                throw new SQLException("修改操作失败：update");
            }else {
                bolR=true;
                //提交事务
                JdbcUtils.commitTransaction();
            }


        } catch (SQLException throwables) {
            //事务回滚
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
     * 根据id删除部门数据
     * @param id 部门id
     * @return
     * @throws MyException
     */
    @Override
    public boolean deleteById(int id) throws MyException {
        boolean bolR=false;
        //判断是否有使用的用户
        int countUsed=this.userDao.countUserByDepartmentId(id);
        if(countUsed==0){
            //判断是否有子节点
            int countChildren=this.countAllByPid(id);
            if(countChildren==0){
                try {
                    JdbcUtils.beginTransaction();
                    //查出未修改的数据
                    SysDepartment dbDepartment=this.departmentDao.selectById(id);
                    //处理序号 大于sort - 1
                    boolean isOk=this.departmentDao.updateSortMinus1(dbDepartment.getParentId(),dbDepartment.getDepartmentSort()+1,null);
                    if(!isOk){
                        throw new SQLException("处理序号操作失败：updateSortPlus1");
                    }
                    //修改
                    isOk=this.departmentDao.deleteById(id);
                    if(!isOk){
                        throw new SQLException("删除操作失败：deleteById");
                    }else {
                        bolR=true;
                        JdbcUtils.commitTransaction();
                    }

                } catch (SQLException throwables) {
                    //事务回滚
                    try {
                        JdbcUtils.rollbackTransaction();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    throwables.printStackTrace();
                }
            }else {
                throw new MyException("该部门存在子部门，不能直接删除！");
            }
        }else {
            throw new MyException("该部门正在使用中，不能删除！");
        }


        return bolR;
    }

    /**
     * 处理tabletree数据
     * List<SysDepartment>转换成List<DepartmentTableTreeVo>
     * @param listSourse 源数据
     * @param pid 父id
     * @return 处理好的数据
     */
    private List<DepartmentTableTreeVo> dealDepartmentTableTreeList(List<SysDepartment> listSourse,int pid){
        List<DepartmentTableTreeVo> rList=new ArrayList<>();
        DepartmentTableTreeVo departmentTableTreeVo=null;
        for (SysDepartment department:listSourse) {
            if(department.getParentId()==pid){
                departmentTableTreeVo=new DepartmentTableTreeVo();
                departmentTableTreeVo.setId(department.getId());
                departmentTableTreeVo.setGmtCreate(department.getGmtCreate());
                departmentTableTreeVo.setParentId(department.getParentId());
                departmentTableTreeVo.setDepartmentName(department.getDepartmentName());
                departmentTableTreeVo.setTelephone(department.getTelephone());
                departmentTableTreeVo.setFax(department.getFax());
                departmentTableTreeVo.setEmail(department.getEmail());
                departmentTableTreeVo.setPrincipal(department.getPrincipal());
                departmentTableTreeVo.setDepartmentSort(department.getDepartmentSort());
                departmentTableTreeVo.setRemark(department.getRemark());
                //子节点
                departmentTableTreeVo.setTreeList(dealDepartmentTableTreeList(listSourse,department.getId()));
                rList.add(departmentTableTreeVo);
            }
        }

        return rList;
    }

    /**
     * 处理下拉树的数据（利用父id递归寻找子数据）
     * @param listSource
     * @param pid
     * @return
     */
    private List<TreeSelectVo> dealTreeSelect(List<SysDepartment> listSource,int pid){
        List<TreeSelectVo> rList=new ArrayList<>();
        TreeSelectVo treeSelectVo=null;
        for (SysDepartment department:listSource) {
           if(department.getParentId()==pid){
               treeSelectVo=new TreeSelectVo();
               treeSelectVo.setId(department.getId());
               treeSelectVo.setName(department.getDepartmentName());
               treeSelectVo.setChecked(false);
               treeSelectVo.setOpen(true);
               List<TreeSelectVo> children=this.dealTreeSelect(listSource,department.getId());
               if(children.size()>0){
                   //有子节点
                   treeSelectVo.setChildren(children);
               }else {
                   //无子节点
                   treeSelectVo.setChildren(null);
               }
               rList.add(treeSelectVo);
           }
        }
        return rList;
    }
}
