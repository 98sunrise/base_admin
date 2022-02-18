package com.gx.servlet.Impl;

import com.gx.common.BaseServlet;
import com.gx.service.Impl.IMenuService;
import com.gx.service.Impl.MenuServiceImpl;
import com.gx.vo.LayuiTableData;
import com.gx.vo.MenuTableTreeVo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MenuServlet extends BaseServlet {
    //service层
    private final IMenuService menuService=new MenuServiceImpl();

    public void index(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/menu.jsp").forward(request,response);
    }

    /**
     *查询数据 for table tree
     */
    public void selectPageList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LayuiTableData<MenuTableTreeVo> layuiTableData=this.menuService.selectForTable();
        returnJson(response,layuiTableData);
    }
}
