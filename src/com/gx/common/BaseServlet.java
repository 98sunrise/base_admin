package com.gx.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gx.util.Tools;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class BaseServlet extends HttpServlet {

    private static final long serialVersionUID = -2662113087973773010L;
    //jackon 序列化对象使用
    private final ObjectMapper objectMapper=new ObjectMapper();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * 通过反射机制实现动态调用
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        String methodName=request.getParameter("method");
        if(!Tools.isNotNull(methodName)) methodName="index";
        try {
            //通过反射获取方法
            Method doMethod=this.getClass().getMethod(methodName,HttpServletRequest.class,
                    HttpServletResponse.class);
            //通过反射调用方法
            doMethod.invoke(this,request,response);
            System.out.println("调用"+getClass().getSimpleName()+"的方法【"+methodName+"】");

        } catch (NoSuchMethodException |InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            String errorMsg=getClass().getSimpleName()+"的方法【"+methodName+"】不存在，或无法调用";
            System.out.println(errorMsg);
        }
    }

    /**
     * 把数据转为json返回
     * @param response//response对象
     * @param obj//需要转换的json的数据
     * @throws IOException
     */
    protected void returnJson(HttpServletResponse response,Object obj) throws
            IOException {
        //返回json
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out=response.getWriter();
        //把对象转换成json字符串
        String strJson=objectMapper.writeValueAsString(obj);
        out.write(strJson);
        out.flush();
        out.close();
    }

    /**
     * 获取Layui table分页 第几页
     * @param request 对象
     * @return 第几页
     */
    protected int  getParamPage(HttpServletRequest request) {
        String strPage=request.getParameter("page");
        if(Tools.isInteger(strPage)){
            return Integer.parseInt(strPage);
        }
        return 0;
    }

    /**
     * 获取Layui table分页 每页数据条数
     * @param request request对象
     * @return 每页数据条数
     */
    protected int  getParamLimit(HttpServletRequest request) {
        String strLimit=request.getParameter("limit");
        if(Tools.isInteger(strLimit)){
            return Integer.parseInt(strLimit);
        }
        return 0;
    }
}
