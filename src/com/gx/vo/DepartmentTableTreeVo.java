package com.gx.vo;

import com.gx.po.SysDepartment;

import java.io.Serializable;
import java.util.List;

public class DepartmentTableTreeVo extends SysDepartment implements Serializable {

    private static final long serialVersionUID = -6829215591111722333L;
    /**
     * 子部门集合（递归）
     */
    private List<DepartmentTableTreeVo> treeList;

    public List<DepartmentTableTreeVo> getTreeList() {
        return treeList;
    }

    public void setTreeList(List<DepartmentTableTreeVo> treeList) {
        this.treeList = treeList;
    }
}
