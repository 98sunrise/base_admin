<%--
  Created by IntelliJ IDEA.
  User: CHH
  Date: 2021/4/13
  Time: 8:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page"/>
<html class="x-admin-sm">
<head>
    <title>后台登录</title>
    <meta http-equiv="X-UA-Compatible" content="IE,chrome=1">
    <meta name="viewport"
          content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <link rel="stylesheet" href="${ctx}/static/css/font.css">
    <link rel="stylesheet" href="${ctx}/static/css/login.css">
    <link rel="stylesheet" href="${ctx}/static/css/xadmin.css">
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
     <script>
        if (window.top.location.href !== window.location.href) {
            window.top.location.href = window.location.href;
        }
    </script>
</head>
<body class="login-bg">
    <div class="login layui-anim layui-anim-up">
        <div class="message">后台管理登录</div>
        <div id="darkbannerwrap"></div>
        <form method="post" class="layui-form">
            <input name="username" id="username" placeholder="用户名" type="text" class="layui-input"/>
            <hr class="hr15">
            <input name="password" id="password" placeholder="密码" type="password" class="layui-input"/>
            <hr class="hr15">
            <input name="password" id="identityKey" placeholder="验证码" type="text" class="layui-input"
                   style="width:200px;display:inline-block" autocomplete="off"/>
            <img src="${ctx}/login?method=identity" id="identityKeyImg" onclick="reloadIdentityKeyImg()">
            <hr class="hr15">
            <input value="登录" style="width:100%;" type="button" id="btnSubmit"/>
            <hr class="hr20">

        </form>
    </div>
    <script src="${ctx}/static/js/jquery.min.js" charset="utf-8"></script>
    <script src="${ctx}/static/lib/layui/layui.js" charset="utf-8"></script>
    <script>
        layui.use(function (){
           var layer=layui.layer;
           $("#btnSubmit").click(function (){
              var username=$("#username").val();
              var password=$("#password").val();
              var identityKey=$("#identityKey").val();
              //数据校验
               if(username===undefined||username===null||username===""){
                   layer.msg("请输入用户名！",{icon:5});
                   return;
               }
               if(password===undefined||password===null||password===""){
                   layer.msg("请输入密码！",{icon:5});
                   return;
               }
               if(identityKey===undefined||identityKey===null||identityKey===""){
                   layer.msg("请输验证码！",{icon:5});
                   return;
               }
               console.log(username);
                //表单提交
               var layerIndex=layer.load();//打开加载层
               $.post('${ctx}/login?method=doLogin',{
                    userName:username,
                    password:password,
                    identityKey:identityKey

               },function (jsonMsg){
                    layer.close(layerIndex);
                    if(jsonMsg.state){
                        //登录成功 页面跳转

                        window.location.replace('${ctx}/home')
                    }else {
                        layer.msg(jsonMsg.msg,{icon:5});
                    }
               },'json');

           });
        });


        function reloadIdentityKeyImg(){
            document.getElementById("identityKeyImg")
                .setAttribute("src","${ctx}/login?method=identity&t="+new Date().getTime());
        }
    </script>
</body>
</html>
