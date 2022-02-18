package com.gx.dao.Impl;

import com.gx.po.SysPosition;

import java.util.List;

/**
 * 职位接口
 */
public interface ISysPositionDao {
    /**
     * 分页查询职位
     * @param page//当前页数
     * @param limit//每页数据条数
     * @param searchName//查询名称
     * @param status//分页数据
     * @return
     */
    List<SysPosition> selectForPageList(int page, int limit, String searchName, Integer status);

    /**
     * 返回数据总条数
     * @param searchName 查询名称
     * @param status 状态
     * @return 数据总条数
     */
    int countAll(String searchName,Integer status);

    /**
     * 根据id查询职位信息
     * @param id
     * @return 返回单条职位信息
     */
    SysPosition selectById(int id);
    /**
     * 查询全部职位
     * @return
     */
    List<SysPosition> selectAll();
    /**
     * 新增职位
     * @param position 职位数据
     * @return 是否成功
     */
    boolean insert(SysPosition position);

    /**
     * 修改职位
     * @param position
     * @return 是否成功
     */
    boolean update(SysPosition position);

    /**
     * 根据id删除职位信息
     * @param //id主键
     * @return 是否成功
     */
    boolean deleteById(int id);

    /**
     * 更新排序 + 1
     * @param minSort 需要更新的最小排序（包含最小） 只是小于传null
     * @param maxValue 需要更新的最大排序（包含最大） 只是大于传null
     * @return 更新结果
     */
    boolean updateSortPlus1(Integer minSort,Integer maxValue);
    /**
     * 更新排序 - 1
     * @param minSort 需要更新的最小排序（包含最小） 只是小于传null
     * @param maxValue 需要更新的最大排序（包含最大） 只是大于传null
     * @return 更新结果
     */
    boolean updateSortMinus1(Integer minSort,Integer maxValue);

}
