package com.gx.servlet.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gx.common.BaseServlet;
import com.gx.service.Impl.ILoginService;
import com.gx.service.Impl.LoginServiceImpl;
import com.gx.util.MD5Util;
import com.gx.util.ProjectParameter;
import com.gx.util.Tools;
import com.gx.util.ValidateImage.MathPngCaptcha;
import com.gx.vo.JsonMsg;
import com.gx.vo.UserVo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

public class LoginServlet extends BaseServlet {

    //service服务层
    private final ILoginService loginService=new LoginServiceImpl();

//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        doPost(request, response);
//    }
//      这里的if和else利用了Java的反射机制实现动态调用
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//            request.setCharacterEncoding("UTF-8");
//            response.setContentType("text/html;charset=UTF-8");
//            String method=request.getParameter("method");
//            if("index".equals(method)){
//                index(request,response);
//            }else if("identity".equals(method)){
//                identity(request,response);
//            }else if("doLogin".equals(method)){
//                doLogin(request,response);
//            }else {
//                index(request,response);
//            }
//
//    }

    /**
     *登录页面
     */
    public void index(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
            request.getRequestDispatcher("/jsp/login.jsp").forward(request,response);
    }

    /**
     *生成验证码图片
     */
    public void identity(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        //响应图片的格式
        response.setContentType("image/png");//image/png image/jpeg image/gif
        //创建验证码工具类的实例
        MathPngCaptcha captcha=new MathPngCaptcha(135,50);
        OutputStream out=response.getOutputStream();//获取输出字节流
        String identityKey=captcha.out(out);
        //把验证码保存到session中
        HttpSession session=request.getSession();
        session.setAttribute(ProjectParameter.SESSION_LOGIN_IDENTITY,identityKey);
        System.out.println("验证码"+identityKey);
        out.flush();
        out.close();

    }
    /**
     * 登录的方法
     */
    public void doLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        JsonMsg jsonMsg=new JsonMsg();

        //获取session中的验证码
        HttpSession session=request.getSession();
        String sessionIdentityKey= (String) session.getAttribute(ProjectParameter.SESSION_LOGIN_IDENTITY);
        //获取参数
        String userName=request.getParameter("userName");
        String password=request.getParameter("password");
        String identityKey=request.getParameter("identityKey");
        //数据验证
        if(Tools.isNotNull(sessionIdentityKey)){
            //判断用户输入的验证码是否正确
            if(sessionIdentityKey.equalsIgnoreCase(identityKey)){
                if(Tools.isNotNull(userName)){
                    if(Tools.isNotNull(password)){
                        //调用service层
                        UserVo dbUser=this.loginService.selectUserByName(userName);
                        //判断是否查询出用户的数据
                        if(dbUser!=null){
                            //验证密码 MD5（用户输入的密码+盐）
                            String md5pass= MD5Util.getMD5(password+dbUser.getSalt());
                            if(dbUser.getUserPassword().equals(md5pass)){
                                //登陆成功
                                //dbUser保存到sesion中
                                session.setAttribute(ProjectParameter.SESSION_USER,dbUser);
                                //移除session中的验证码
                                session.removeAttribute(ProjectParameter.SESSION_LOGIN_IDENTITY);
                                jsonMsg.setState(true);//成功！
                                jsonMsg.setMsg("登录成功！");


                            }else {
                                jsonMsg.setMsg("登录失败！");
                            }


                        }else {
                            jsonMsg.setMsg("用户不存在！");
                        }
                    }else {
                        jsonMsg.setMsg("请输入密码！");
                    }
                }else {
                    jsonMsg.setMsg("请输入用户名");
                }
            }else {
                jsonMsg.setMsg("验证码输入错误！");
            }
        }else {
            jsonMsg.setMsg("非法访问！");
        }
        returnJson(response,jsonMsg);

    }

    /**
     * 注销登录
     * @param request
     * @param response
     */
    public void loginOut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session=request.getSession();
//        //从session中移除登录的用户记录
        session.removeAttribute(ProjectParameter.SESSION_USER);
        returnJson(response,true);
    }

}
