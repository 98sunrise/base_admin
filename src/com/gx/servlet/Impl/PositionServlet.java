package com.gx.servlet.Impl;

import com.gx.common.BaseServlet;
import com.gx.po.SysPosition;
import com.gx.service.Impl.IPositionService;
import com.gx.service.Impl.PositionServiceImpl;
import com.gx.util.Tools;
import com.gx.vo.JsonMsg;
import com.gx.vo.LayuiTableData;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PositionServlet extends BaseServlet {
    //service层
    private final IPositionService positionService=new PositionServiceImpl();
    /**
     * 页面
     */
    public void index(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/position.jsp").forward(request,response);
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
        LayuiTableData<SysPosition> layuiTableData=this.positionService.selectForPageList(page,limit,searchName,status);
        //返回Json
        returnJson(response,layuiTableData);
    }

    /**
     * 新增时获取下一排序的序号
     */
    public void selectNextSort(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        //调用service获取当前的总行数
        int count=this.positionService.countAll();
        JsonMsg jsonMsg=new JsonMsg();
        jsonMsg.setState(true);
        jsonMsg.setData(count+1);//下一序号 = 当前数据条数 + 1
        returnJson(response,jsonMsg);
    }

    /**
     * 新增
     */
    public void insert(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        JsonMsg jsonMsg=new JsonMsg();
        //获取参数
        String positionName=request.getParameter("positionName");
        String strPositionSort=request.getParameter("positionSort");
        String strPositionStatus=request.getParameter("positionStatus");
        String remark=request.getParameter("remark");
        //数据验证
        if(Tools.isNotNull(positionName)){
            if(Tools.isNotNull(strPositionSort)){
                if(Tools.isNotNull(strPositionStatus)){
                    //数据类型转换
                    int positionSort=Integer.parseInt(strPositionSort);
                    byte positionStatus=Byte.parseByte(strPositionStatus);
                    //组装数据
                    SysPosition position=new SysPosition();
                    position.setPositionName(positionName);
                    position.setPositionSort(positionSort);
                    position.setPositionStatus(positionStatus);
                    position.setRemark(remark);
                    //调用service层
                    boolean ioOk=this.positionService.insert(position);
                    if(ioOk){
                        jsonMsg.setState(true);
                        jsonMsg.setMsg("新增成功！");
                    }else {
                        jsonMsg.setMsg("新增失败！");
                    }
                }else {
                    jsonMsg.setMsg("请选择职位状态！");
                }
            }else {
                jsonMsg.setMsg("请输入正确的职位排序（整数）！");
            }
        }else {
            jsonMsg.setMsg("请输入职位名称！");
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
                SysPosition position=this.positionService.selectById(id);
                msg.setState(true);
                msg.setData(position);//通过jsonMsg data把数据返回
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
        String positionName=request.getParameter("positionName");
        String strPositionSort=request.getParameter("positionSort");
        String strPositionStatus=request.getParameter("positionStatus");
        String remark=request.getParameter("remark");
        //数据验证
        if(Tools.isInteger(strId)){
            if(Tools.isNotNull(positionName)){
                if(Tools.isNotNull(strPositionSort)){
                    if(Tools.isNotNull(strPositionStatus)){
                        //数据类型转换
                        int id=Integer.parseInt(strId);
                        int positionSort=Integer.parseInt(strPositionSort);
                        byte positionStatus=Byte.parseByte(strPositionStatus);
                        //组装数据
                        SysPosition position=new SysPosition();
                        position.setId(id);
                        position.setPositionName(positionName);
                        position.setPositionSort(positionSort);
                        position.setPositionStatus(positionStatus);
                        position.setRemark(remark);
                        //调用service层
                        boolean ioOk=this.positionService.update(position);
                        if(ioOk){
                            jsonMsg.setState(true);
                            jsonMsg.setMsg("修改成功！");
                        }else {
                            jsonMsg.setMsg("修改失败！");
                        }
                    }else {
                        jsonMsg.setMsg("请选择职位状态！");
                    }
                }else {
                    jsonMsg.setMsg("请输入正确的职位排序（整数）！");
                }
            }else {
                jsonMsg.setMsg("请输入职位名称！");
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
        JsonMsg msg=new JsonMsg();
        String strId=request.getParameter("id");
        if(Tools.isInteger(strId)){
            int id=Integer.parseInt(strId);
            msg=this.positionService.deleteById(id);
        }else {
            msg.setMsg("参数异常！");
        }
        returnJson(response,msg);
    }
}
