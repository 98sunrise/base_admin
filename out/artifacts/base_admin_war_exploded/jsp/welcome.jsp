<%--
  Created by IntelliJ IDEA.
  User: en
  Date: 2021/3/4
  Time: 20:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page"/>
<!DOCTYPE html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>欢迎页面-X-admin2.2</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <link rel="stylesheet" href="${ctx}/static/css/font.css">
    <link rel="stylesheet" href="${ctx}/static/css/xadmin.css">
    <!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-body ">
                    <blockquote class="layui-elem-quote">欢迎${sessionScope.login_user.roleName}：
                        <span class="x-red">${sessionScope.login_user.userName}</span>！
                        当前时间:<span id="spanTime"></span>
                    </blockquote>
                </div>
            </div>
        </div>


        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-header">开发团队</div>
                <div class="layui-card-body ">
                    <table class="layui-table">
                        <tbody>
                        <tr>
                            <th>开发者</th>
                            <td>施显军(1396715343@qq.com)</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <style id="welcome_style"></style>
    </div>
</div>

<script src="${ctx}/static/lib/layui/layui.js" charset="utf-8"></script>
<script type="text/javascript" src="${ctx}/static/js/xadmin.js"></script>
<script>
    var spanTime=document.getElementById("spanTime");
    setspanTime();
    var timer=setInterval(setspanTime,1000);
    function setspanTime(){
        var time=new Date();
        spanTime.innerText=time.getFullYear() + "-" + (time.getMonth()+1) + "-" + time.getDate() + " " +
            time.getHours() + ":" + time.getMinutes() + ":" + time.getSeconds();
    }
</script>
</body>
</html>