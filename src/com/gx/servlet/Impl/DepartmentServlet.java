package com.gx.servlet.Impl;

import com.gx.common.BaseServlet;
import com.gx.exception.MyException;
import com.gx.po.SysDepartment;
import com.gx.service.Impl.DepartmentServiceImpl;
import com.gx.service.Impl.IDepartmentService;
import com.gx.util.Tools;
import com.gx.vo.DepartmentTableTreeVo;
import com.gx.vo.JsonMsg;
import com.gx.vo.LayuiTableData;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DepartmentServlet extends BaseServlet {
    //service
    private final IDepartmentService departmentService=new DepartmentServiceImpl();
    public void index(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/department.jsp").forward(request,response);

    }

    /**
     * 查询数据 for table tree
     */
    public void selectPageList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        LayuiTableData<DepartmentTableTreeVo> layuiTableData=this.departmentService.selectForTable();
        returnJson(response,layuiTableData);
    }

    /**
     * 根据父id查询下一序号
     */
    public void selectNextSort(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        String strPid=request.getParameter("pid");
        int pid=0;
        if(Tools.isInteger(strPid)){
            pid=Integer.parseInt(strPid);
        }
        int nextSort=this.departmentService.countAllByPid(pid)+1;
        //返回
        JsonMsg msg=new JsonMsg();
        msg.setState(true);
        msg.setData(nextSort);
        returnJson(response,msg);
    }

    /**
     * 查询部门数据for树形下拉框
     */
    public void SelectForTreeSelect(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        returnJson(response,this.departmentService.SelectForTreeSelect());
    }

    /**
     * 新增部门
     */
    public void insert(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        JsonMsg jsonMsg=new JsonMsg();
        //获取参数、
        String strParentId=request.getParameter("parentId");
        String departmentName=request.getParameter("departmentName");
        String telephone=request.getParameter("telephone");
        String fax=request.getParameter("fax");
        String email=request.getParameter("email");
        String principal=request.getParameter("principal");
        String strDepartmentSort=request.getParameter("departmentSort");
        String remark=request.getParameter("remark");
        //数据验证
        if(Tools.isInteger(strParentId)){
            if(Tools.isNotNull(departmentName)){
                if(Tools.isInteger(strDepartmentSort)){
                    SysDepartment department=new SysDepartment();
                    department.setParentId(Integer.parseInt(strParentId));
                    department.setDepartmentName(departmentName);
                    department.setTelephone(telephone);
                    department.setFax(fax);
                    department.setEmail(email);
                    department.setPrincipal(principal);
                    department.setDepartmentSort(Integer.parseInt(strDepartmentSort));
                    department.setRemark(remark);
                    //新增
                    boolean isOk=this.departmentService.insert(department);
                    if(isOk){
                        jsonMsg.setState(true);
                        jsonMsg.setMsg("新增成功");
                    }else {
                        jsonMsg.setMsg("新增失败");
                    }
                }else {
                    jsonMsg.setMsg("请输入显示排序");
                }
            }else {
                jsonMsg.setMsg("部门名称不能为空！");
            }
        }else {
            jsonMsg.setMsg("请选择上级部门！");
        }
        returnJson(response,jsonMsg);
    }

    /**
     * 根据id查询部门数据
     */
    public void selectById(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JsonMsg jsonMsg = new JsonMsg();
        String strId=request.getParameter("id");
        if(Tools.isInteger(strId)){
            jsonMsg.setState(true);
            jsonMsg.setData(this.departmentService.selectById(Integer.parseInt(strId)));
        }else {
            jsonMsg.setMsg("参数异常");
        }
        returnJson(response,jsonMsg);
    }

    /**
     * 修改部门数据
     */
    public void update(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        JsonMsg jsonMsg = new JsonMsg();
        //获取参数
        String strId=request.getParameter("id");
        String departmentName=request.getParameter("departmentName");
        String strParentId=request.getParameter("parentId");
        String telephone=request.getParameter("telephone");
        String fax=request.getParameter("fax");
        String email=request.getParameter("email");
        String principal=request.getParameter("principal");
        String strDepartmentSort=request.getParameter("departmentSort");
        String remark=request.getParameter("remark");
        //数据验证
        if(Tools.isInteger(strId)){
            if(Tools.isInteger(strParentId)){
                if(Tools.isNotNull(departmentName)){
                    if(Tools.isInteger(strDepartmentSort)){
                        SysDepartment department=new SysDepartment();
                        department.setId(Integer.parseInt(strId));
                        department.setParentId(Integer.parseInt(strParentId));
                        department.setDepartmentName(departmentName);
                        department.setTelephone(telephone);
                        department.setFax(fax);
                        department.setEmail(email);
                        department.setPrincipal(principal);
                        department.setDepartmentSort(Integer.parseInt(strDepartmentSort));
                        department.setRemark(remark);
                        //新增
                        boolean isOk=this.departmentService.update(department);
                        if(isOk){
                            jsonMsg.setState(true);
                            jsonMsg.setMsg("修改成功！");
                        }else {
                            jsonMsg.setMsg("修改失败！");
                        }
                    }else {
                        jsonMsg.setMsg("请输入显示排序");
                    }
                }else {
                    jsonMsg.setMsg("请输入部门名称");
                }
            }else {
                jsonMsg.setMsg("请选择上级部门");
            }
        }else {
            jsonMsg.setMsg("参数异常");
        }
        returnJson(response,jsonMsg);
    }

    /**
     * 删除部门
     */
    public void deleteById(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        JsonMsg jsonMsg = new JsonMsg();
        String strId=request.getParameter("id");

        if(Tools.isInteger(strId)){
            //删除
            try {
                this.departmentService.deleteById(Integer.parseInt(strId));
                jsonMsg.setState(true);
                jsonMsg.setMsg("删除成功！");
            }catch (MyException e){
                jsonMsg.setMsg(e.getMessage());
            }

        }else {
            jsonMsg.setMsg("参数异常");
        }
        returnJson(response,jsonMsg);
    }
}
