package com.gx.servlet.Impl;

import com.gx.common.BaseServlet;
import com.gx.service.Impl.IMenuService;
import com.gx.service.Impl.MenuServiceImpl;
import com.gx.util.ProjectParameter;
import com.gx.vo.MenuTableTreeVo;
import com.gx.vo.UserVo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class HomeServlet extends BaseServlet {
    //service
    private final IMenuService menuService=new MenuServiceImpl();
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        doPost(request, response);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//       request.setCharacterEncoding("UTF-8");
//       response.setContentType("text/html;charset=UTF-8");
//       response.setCharacterEncoding("UTF-8");
//       String method=request.getParameter("method");
//       if("index".equals(method)){
//           index(request,response);
//       }
//       else {
//           index(request,response);
//       }
//    }
    //转发
    public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session=request.getSession();//获取登录时session中的dbUser
        UserVo loginUser= (UserVo) session.getAttribute(ProjectParameter.SESSION_USER);
        if(loginUser!=null){
            //登录
            int roleId=loginUser.getRoleId();
            List<MenuTableTreeVo> listMenu=this.menuService.selectMenuByRoleId(roleId);
            //把菜单通过request传到页面
            request.setAttribute("listMenu",listMenu);
            //转发
            request.getRequestDispatcher("/jsp/home.jsp").forward(request,response);
        }else {
            //未登录
            //重定向 到项目的根路径 跳转到login页面
            response.sendRedirect(request.getContextPath());
        }

    }
    public void welcome(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher("/jsp/welcome.jsp").forward(request,response);
    }
}
