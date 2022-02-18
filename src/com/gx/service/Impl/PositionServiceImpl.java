package com.gx.service.Impl;

import com.gx.dao.Impl.ISysPositionDao;
import com.gx.dao.Impl.ISysUserDao;
import com.gx.dao.Impl.SysPositionDaoImpl;
import com.gx.dao.Impl.SysUserDaoImpl;
import com.gx.po.SysPosition;
import com.gx.util.JdbcUtils;
import com.gx.vo.JsonMsg;
import com.gx.vo.LayuiTableData;

import java.sql.SQLException;
import java.util.List;

public class PositionServiceImpl implements IPositionService{
    //dao
    private final ISysPositionDao positionDao=new SysPositionDaoImpl();
    private final ISysUserDao userDao=new SysUserDaoImpl();

    @Override
    public LayuiTableData<SysPosition> selectForPageList(int page, int limit, String searchName, Integer status) {
        //调用Dao查询数据
        List<SysPosition> list=this.positionDao.selectForPageList(page,limit,searchName,status);
        int count=this.positionDao.countAll(searchName,status);
        //组装数据
        LayuiTableData<SysPosition> layuiTableData=new LayuiTableData<>(count,list);
        return layuiTableData;
    }

    @Override
    public int countAll() {
        return this.positionDao.countAll(null,null);
    }

    /**
     * 根据id查询职位信息
     */
    @Override
    public SysPosition selectById(int id) {
        return this.positionDao.selectById(id);
    }

    @Override
    public boolean insert(SysPosition position) {

//        return this.positionDao.insert(position);
        boolean bolR=false;
        try {
            //开启事务
            JdbcUtils.beginTransaction();
            //查询理论下一序号
            int nextSort=this.countAll()+1;
            //传入的序号 小于nextSort，用户调小的自动生成的序号
            if(nextSort>position.getPositionSort()){
                boolean isOk=this.positionDao.updateSortPlus1(position.getPositionSort(),null);
                if(!isOk){
                    throw new SQLException("序号调整失败：minSort"+position.getPositionSort());
                }
            }
            //新增
            boolean isOk=this.positionDao.insert(position);
            if(isOk){
                bolR=true;
                //提交事务
                JdbcUtils.commitTransaction();
            }else {
                throw new SQLException("新增操作失败:"+position);
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
    public boolean update(SysPosition position) {
//        return this.positionDao.update(position);
        boolean bolR=false;
        try {
            JdbcUtils.beginTransaction();
            //查询出未修改的数据
            SysPosition dbPosition=this.positionDao.selectById(position.getId());
            if(dbPosition.getPositionSort()>position.getPositionSort()){
               // 例：1 2 3 4 5 6 7 8 9  将 6 移动到 4 的位置
                //序号向前（小的方向）移动 大于等于新的序号，小于旧的序号 +1     这里的范围是 4 <= x< = 6
                boolean isOk=this.positionDao.updateSortPlus1(position.getPositionSort(),dbPosition.getPositionSort()-1);
                if(!isOk){
                    throw new SQLException("序号调整失败：minSort="+position.getPositionSort()+";maxSort="+(dbPosition.getPositionSort()-1));
                }
            }else {
                boolean isOk=this.positionDao.updateSortMinus1(dbPosition.getPositionSort()+1,position.getPositionSort());
                if(!isOk){
                    throw new SQLException("序号调整失败：minSort="+(position.getPositionSort()+1)+";maxSort="+dbPosition.getPositionSort());
                }
            }
            //修改
            boolean isOk=this.positionDao.update(position);
            if(isOk){
                bolR=true;
                //提交事务
                JdbcUtils.commitTransaction();
            }else {
                throw new SQLException("修改操作失败："+position);
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


        return  bolR;
    }

    @Override
    public JsonMsg deleteById(int id) {
        JsonMsg msg=new JsonMsg();
        //查询准备删除的职位是否在使用
        int useCount=this.userDao.countUserByPositionId(id);
        if(useCount==0){
            //未使用 直接删除
            try {
                JdbcUtils.beginTransaction();
                SysPosition dbPosition=this.positionDao.selectById(id);
                //更新序号
                boolean isOk=this.positionDao.updateSortMinus1(dbPosition.getPositionSort()+1,null);
                if(!isOk){
                    throw new SQLException("序号调整失败：minSort="+(dbPosition.getPositionSort()+1));
                }
                //执行删除
                isOk=this.positionDao.deleteById(id);

                if(isOk){
                    msg.setState(true);
                    msg.setMsg("删除成功！");
                    //提交事务
                    JdbcUtils.commitTransaction();
                }else {
                    throw new SQLException("删除操作失败："+id);
                }
            } catch (SQLException throwables) {
                //事务回滚
                try {
                    JdbcUtils.rollbackTransaction();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                throwables.printStackTrace();
                msg.setMsg("删除失败!");
            }

        }else {
            msg.setState(false);
            msg.setMsg("该职位正在使用中，不能删除！");
        }
        return msg;
    }
}
