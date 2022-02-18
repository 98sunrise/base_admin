package com.gx.servlet.Impl;

import com.gx.common.BaseServlet;
import com.gx.exception.MyException;
import com.gx.po.SysPosition;
import com.gx.po.SysRole;
import com.gx.service.Impl.IPositionService;
import com.gx.service.Impl.IRoleService;
import com.gx.service.Impl.PositionServiceImpl;
import com.gx.service.Impl.RoleServiceImpl;
import com.gx.util.Tools;
import com.gx.vo.JsonMsg;
import com.gx.vo.LayuiTableData;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RoleServlet extends BaseServlet {
    //service层
    private final IRoleService roleService=new RoleServiceImpl();
    /**
     * 页面
     */
    public void index(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/role.jsp").forward(request,response);
    }

    /**
     * 查询分页数据 for layuiTable
     */
    public void selectPageList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        //获取分页数据
        int page=this.getParamPage(request);
        int limit=this.getParamLimit(request);
        //参数
        String searchName=request.getParameter("searchName");
        String strStatus=request.getParameter("searchStatus");
        Integer status=null;
        if(Tools.isInteger(strStatus)){
            status=Integer.parseInt(strStatus);
        }
        LayuiTableData<SysRole> layuiTableData=this.roleService.selectForPageList(page,limit,searchName,status);
        //返回Json
        returnJson(response,layuiTableData);
    }
    /**
     * 新增
     */
    public void insert(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        JsonMsg jsonMsg=new JsonMsg();
        //获取参数
        String roleName=request.getParameter("roleName");
        String strRoleSort=request.getParameter("roleSort");
        String strRoleStatus=request.getParameter("roleStatus");
        String remark=request.getParameter("remark");
        //数据验证
        if(Tools.isNotNull(roleName)){
            if(Tools.isNotNull(strRoleSort)){
                if(Tools.isNotNull(strRoleStatus)){
                    //数据类型转换
                    int roleSort=Integer.parseInt(strRoleSort);
                    byte roleStatus=Byte.parseByte(strRoleStatus);
                    //组装数据
                    SysRole role=new SysRole();
                    role.setRoleName(roleName);
                    role.setRoleSort(roleSort);
                    role.setRoleStatus(roleStatus);
                    role.setRemark(remark);
                    //调用service层
                    boolean ioOk=this.roleService.insert(role);
                    if(ioOk){
                        jsonMsg.setState(true);
                        jsonMsg.setMsg("新增成功！");
                    }else {
                        jsonMsg.setMsg("新增失败！");
                    }
                }else {
                    jsonMsg.setMsg("请选择角色状态！");
                }
            }else {
                jsonMsg.setMsg("请输入正确的角色排序（整数）！");
            }
        }else {
            jsonMsg.setMsg("请输入角色名称！");
        }
        returnJson(response,jsonMsg);
    }

    /**
     * 根据id查询职位数据
     */
    public void selectById(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
            JsonMsg msg=new JsonMsg();
            String strId=request.getParameter("id");
            if(Tools.isInteger(strId)){
                int id=Integer.parseInt(strId);
                SysRole role=this.roleService.selectById(id);
                msg.setState(true);
                msg.setData(role);//通过jsonMsg data把数据返回
            }else {
                msg.setMsg("参数异常！");
            }
            returnJson(response,msg);
    }

    /**
     * 修改
     * @param request
     */
    public void update(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        JsonMsg jsonMsg=new JsonMsg();
        //获取参数
        String strId=request.getParameter("id");
        String roleName=request.getParameter("roleName");
        String strRoleSort=request.getParameter("roleSort");
        String strRoleStatus=request.getParameter("roleStatus");
        String remark=request.getParameter("remark");
        //数据验证
        if(Tools.isInteger(strId)){
            if(Tools.isNotNull(roleName)){
                if(Tools.isNotNull(strRoleSort)){
                    if(Tools.isNotNull(strRoleStatus)){
                        //数据类型转换
                        int id=Integer.parseInt(strId);
                        int roleSort=Integer.parseInt(strRoleSort);
                        byte roleStatus=Byte.parseByte(strRoleStatus);
                        //组装数据
                        SysRole role=new SysRole();
                        role.setId(id);
                        role.setRoleName(roleName);
                        role.setRoleSort(roleSort);
                        role.setRoleStatus(roleStatus);
                        role.setRemark(remark);
                        //调用service层
                        boolean ioOk=this.roleService.update(role);
                        if(ioOk){
                            jsonMsg.setState(true);
                            jsonMsg.setMsg("修改成功！");
                        }else {
                            jsonMsg.setMsg("修改失败！");
                        }
                    }else {
                        jsonMsg.setMsg("请选择角色状态！");
                    }
                }else {
                    jsonMsg.setMsg("请输入正确的角色排序（整数）！");
                }
            }else {
                jsonMsg.setMsg("请输入角色名称！");
            }
        }else {
            jsonMsg.setMsg("参数异常！");
        }

        returnJson(response,jsonMsg);
    }

    /**
     * 根据id删除职位数据
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void deleteById(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        JsonMsg jsonMsg=new JsonMsg();
        String strId=request.getParameter("id");
        if(Tools.isInteger(strId)){
            int id=Integer.parseInt(strId);
            try {
                boolean isOk=this.roleService.deleteById(id);
                jsonMsg.setState(true);
                jsonMsg.setMsg("删除成功！");
            } catch (MyException e) {
                e.printStackTrace();
            }
        }else {
            jsonMsg.setMsg("参数异常！");
        }
        returnJson(response,jsonMsg);
    }
}
