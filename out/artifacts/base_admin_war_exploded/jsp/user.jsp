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
    <title>用户管理</title>
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

    <style>
        /*去除xadmin 对layui table最小行高*/
        .layui-table td, .layui-table th {
            min-width: 0 !important;
        }
    </style>
</head>
<body>
<div>
    <%--导航条--%>
    <div class="x-nav">
            <span class="layui-breadcrumb">
                <a href="">首页</a>
                <a href="">单位组织</a>
                <a>
                    <cite>用户管理</cite>
                </a>
            </span>
        <a class="layui-btn layui-btn-small" style="line-height:1.6em;margin-top:3px;float:right"
           onclick="location.reload()" title="刷新">
            <i class="layui-icon layui-icon-refresh" style="line-height:30px"></i>
        </a>
    </div>
    <%--主容器布局--%>
    <div class="layui-fluid">
        <div class="layui-row layui-col-space15">
            <div class="layui-col-md2">
                <div class="layui-card">
                    <div class="layui-card-header">
                        组织机构
                        <button class="layui-btn layui-btn-xs layui-btn-primary" style="float:right;margin-top: 6px">
                            <i class="layui-icon layui-icon-refresh"></i>
                        </button>
                    </div>
                    <div class="layui-card-body">
                        <%--layuiTree占位--%>
                        <div id="treeDepartment"></div>
                    </div>
                </div>
            </div>
            <div class="layui-col-md10">
                <div class="layui-card">
                    <%--搜索栏--%>
                    <div class="layui-card-body ">
                        <%--departmentId,userName,realName ,mobile ,userStatus ,startEndDate --%>
                        <form class="layui-form layui-col-space5" id="formSearch" lay-filter="formSearch">
                            <%--存放部门id--%>
                            <input type="hidden" name="departmentId">
                            <div class="layui-inline layui-show-xs-block">
                                <label class="layui-form-label">员工名称:</label>
                                <div class="layui-input-inline layui-show-xs-block">
                                    <input type="text" name="realName" class="layui-input">
                                </div>
                            </div>
                            <div class="layui-inline layui-show-xs-block">
                                <label class="layui-form-label">登录名称:</label>
                                <div class="layui-input-inline layui-show-xs-block">
                                    <input type="text" name="userName" class="layui-input">
                                </div>
                            </div>
                            <div class="layui-inline layui-show-xs-block">
                                <label class="layui-form-label">手机号码:</label>
                                <div class="layui-input-inline layui-show-xs-block">
                                    <input type="text" name="mobile" class="layui-input">
                                </div>
                            </div>
                            <div class="layui-inline layui-show-xs-block">
                                <label class="layui-form-label">员工状态:</label>
                                <div class="layui-input-inline layui-show-xs-block">
                                    <select name="userStatus">
                                        <option value="">全部</option>
                                        <option value="1">启用</option>
                                        <option value="0">禁用</option>
                                    </select>
                                </div>
                            </div>
                            <div class="layui-inline layui-show-xs-block">
                                <label class="layui-form-label">创建时间:</label>
                                <div class="layui-input-inline layui-show-xs-block">
                                    <input type="text" id="searchCreateTime" name="startEndDate" class="layui-input">
                                </div>
                            </div>
                            <div class="layui-input-inline layui-show-xs-block">
                                <button class="layui-btn" type="button" id="btnSearch">
                                    <i class="layui-icon layui-icon-search"></i>
                                </button>
                            </div>
                        </form>
                    </div>
                    <%--操作栏--%>
                    <div class="layui-card-header">
                        <button class="layui-btn" id="btnAdd">
                            <i class="layui-icon layui-icon-addition"></i>添加
                        </button>
                        <button class="layui-btn layui-btn-normal" id="btnEdit">
                            <i class="layui-icon layui-icon-edit"></i>修改
                        </button>
                        <button class="layui-btn layui-btn-danger" id="btnDelete">
                            <i class="layui-icon layui-icon-delete"></i>删除
                        </button>
                        <button class="layui-btn layui-btn-primary" id="btnExportXlsx">
                            <i class="layui-icon layui-icon-export"></i>导出Xlsx
                        </button>
                        <button class="layui-btn layui-btn-primary" id="btnExportXls">
                            <i class="layui-icon layui-icon-export"></i>导出Xls
                        </button>
                    </div>
                    <%--表格--%>
                    <div class="layui-card-body ">
                        <table class="layui-hide" id="tabUser" lay-filter="tabUser"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div>
    <div style="display: none" id="upPortraitDiv">
        <div>
            <input type="file" hidden name="portrait" id="upPortrait"><%--头像上传文本框--%>
        </div>
    </div>

    <form class="layui-form" id="formUser" lay-filter="formUser" style="display: none;margin: 20px;"
          autocomplete="off">
        <%--标识是新增1还是修改0--%>
        <input type="hidden" name="isInsert" value="1">
        <%--修改时存放主键id--%>
        <input type="hidden" name="id">
        <div class="layui-container">
            <div class="layui-row">
                <div class="layui-col-md3">
                    <img src="${ctx}/static/images/uploadImg.png" width="221" id="userPicture" alt="头像"
                         style="max-height: 340px;">
                </div>
                <div class="layui-col-md9">
                    <div class="layui-form-item">
                        <div class="layui-inline">
                            <label class="layui-form-label">登录名</label>
                            <div class="layui-input-block">
                                <input type="text" name="userName" lay-verify="loginName" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-inline" id="divUserPassword">
                            <label class="layui-form-label">登录密码</label>
                            <div class="layui-input-block">
                                <input type="password" name="userPassword" id="userPassword" lay-verify="loginPassword"
                                       class="layui-input">
                            </div>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <div class="layui-inline">
                            <label class="layui-form-label">姓名</label>
                            <div class="layui-input-block">
                                <input type="text" name="realName" lay-verify="required" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-inline">
                            <label class="layui-form-label">性别</label>
                            <div class="layui-input-block">
                                <input type="radio" name="gender" value="0" lay-verify="requiredRadioCheckbox"
                                       title="未知">
                                <input type="radio" name="gender" value="1" lay-verify="requiredRadioCheckbox"
                                       title="男">
                                <input type="radio" name="gender" value="2" lay-verify="requiredRadioCheckbox"
                                       title="女">
                            </div>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <div class="layui-inline">
                            <label class="layui-form-label">生日</label>
                            <div class="layui-input-block">
                                <input type="text" name="birthday" id="formBirthday" lay-verify="birthday"
                                       class="layui-input">
                            </div>
                        </div>
                        <div class="layui-inline">
                            <label class="layui-form-label">部门</label>
                            <div class="layui-input-block">
                                <input type="text" id="departmentId" name="departmentId" class="layui-input"
                                       lay-verify="required" lay-filter="departmentIdTreeSelect">
                            </div>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <div class="layui-inline">
                            <label class="layui-form-label">手机</label>
                            <div class="layui-input-block">
                                <input type="text" name="mobile" lay-verify="phone" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-inline">
                            <label class="layui-form-label">职位</label>
                            <div class="layui-input-block">
                                <select name="positionId" id="selectPositionId" lay-verify="required"></select>
                            </div>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <div class="layui-inline">
                            <label class="layui-form-label">邮箱</label>
                            <div class="layui-input-block">
                                <input type="text" name="email" lay-verify="emailNr" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-inline">
                            <label class="layui-form-label">角色</label>
                            <div class="layui-input-block">
                                <select name="roleId" id="selectRoleId" lay-verify="required"></select>
                            </div>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <div class="layui-inline">
                            <label class="layui-form-label">QQ</label>
                            <div class="layui-input-block">
                                <input type="text" name="qq" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-inline">
                            <label class="layui-form-label">微信</label>
                            <div class="layui-input-block">
                                <input type="text" name="wechat" class="layui-input">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">员工状态</label>
            <div class="layui-input-block">
                <input type="radio" name="userStatus" value="1" title="启用" checked>
                <input type="radio" name="userStatus" value="0" title="禁用">
            </div>
        </div>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">备注</label>
            <div class="layui-input-block">
                <textarea name="remark" placeholder="请输入备注（可留空）" class="layui-textarea"></textarea>
            </div>
        </div>
        <div class="layui-btn-container" style="text-align: end">
            <button type="submit" class="layui-btn" lay-submit lay-filter="formUserSubmit">提交</button>
            <button type="reset" class="layui-btn layui-btn-warm">重置</button>
        </div>
    </form>
</div>

<script src="${ctx}/static/lib/layui/layui.js" charset="utf-8"></script>
<script src="${ctx}/static/js/myUtils.js" charset="utf-8"></script>
<script src="${ctx}/static/js/layuiVerifyExt.js" charset="utf-8"></script>
<script>
    var layerFormIndex,$,layer;
    layui.extend({
        treeSelect: 'static/module/js/treeSelect', //下拉树
    }).use(['treeSelect'],function () {
        $ = layui.$;
        layer = layui.layer;
        var table = layui.table,

            form = layui.form,
            tree = layui.tree,
            laydate = layui.laydate,
            treeSelect=layui.treeSelect;
        //layui form 注册自定义校验规则
        form.verify(formVerifyExt);
        // 菜单树形table初始化
        var tableRole = table.render({
            elem: '#tabUser',//table元素
            id: 'tabUser',
            url: '${ctx}/user?method=selectPageList',//数据url
            page: true,//分页
            cols: [[
                {type: 'radio'},
                {field: 'id', title: 'id', event: 'id', width: 120, hide: true},
                {field: 'userName', title: '登录名', width: 100, align: 'center'},
                {field: 'realName', title: '姓名', width: 100, align: 'center'},
                {field: 'departmentName', title: '部门', width: 100, align: 'center'},
                {field: 'positionName', title: '职位', width: 100, align: 'center'},
                {field: 'roleName', title: '角色', width: 100, align: 'center'},
                {
                    field: 'gender', title: '性别', width: 80, align: 'center', templet: function (rowData) {
                        if (rowData.gender === 0) {//未知
                            return '未知';
                        } else if (rowData.gender === 1) {//男
                            return '男';
                        } else {
                            return '女';
                        }
                    }
                },
                {field: 'mobile', title: '手机号', width: 110, align: 'center'},
                {
                    field: 'userStatus',
                    title: '状态',
                    event: 'userStatus',
                    width: 70,
                    templet: function (rowData) {
                        if (rowData.userStatus === 1) {//目录
                            return '<span class="layui-badge layui-bg-green">启用</span>';
                        } else {
                            return '<span class="layui-badge layui-bg-orange">禁用</span>';
                        }
                    }
                },
                {
                    field: 'gmtCreate', title: '创建时间', event: 'gmtCreate', width: 160, templet: function (rowData) {
                        var dt = new Date(rowData.gmtCreate);
                        return dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + " " +
                            dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();
                    }
                },
                {
                    field: 'portrait', title: '头像', event: 'portrait', width: 130, templet: function (rowData) {
                        return '<img src="${ctx}/user?method=getPortraitImage&imgName=' + rowData.portrait + '" class="portraitImg">';
                    }
                },
                {field: 'remark', title: '备注', event: 'remark'},
                {
                    title: '重置密码', width: 90, align: 'center', templet: function (rowData) {
                        return '<button class="layui-btn layui-btn-xs layui-btn-warm" onclick="resetPassword('
                            +rowData.id+')"><i class="layui-icon layui-icon-refresh"></i></button>';
                    }
                }
            ]],
            limit: 10,//每页页数
            limits: [5, 10, 15, 20, 25, 30, 35, 40, 45, 50]//定义每页页数选择下拉框
        });

        //日期段时间控件
        laydate.render({
            elem: '#searchCreateTime', //指定元素
            type: 'date',
            range: true,//日期段的选择
        });
        //表单 生日日期控件
        laydate.render({
            elem: '#formBirthday', //指定元素
            type: 'date'
        });
        //部门下拉树
        $.post("${ctx}/user?method=selectDepartmentForTree", function (jsonData) {
            tree.render({
                elem: '#treeDepartment',  //绑定元素
                id: 'treeDepartment',
                onlyIconControl: true,
                data: jsonData,//数据
                click: function (obj) {
                    // console.log(obj.data); //得到当前点击的节点数据
                    // console.log(obj.state); //得到当前节点的展开状态：open、close、normal
                    // console.log(obj.elem); //得到当前节点元素
                    //
                    // console.log(obj.data.children); //当前节点下是否有子节点

                    var id = obj.data.id;//部门id
                    $('#treeDepartment .layui-tree-entry .layui-tree-main').css("background", "#ffffff");
                    $('#treeDepartment [data-id="' + id + '"] .layui-tree-entry .layui-tree-main').css("background", "#dddddd");
                    form.val('formSearch',{departmentId:id});
                    //触发搜索按钮
                    $("#btnSearch").click();
                }
            });
        });

        //搜索框
        $("#btnSearch").click(function () {
            //获取搜索表单的数据
            var searchFormData=form.val('formSearch');
            console.log(searchFormData);
            //表格重载
            table.reload('tabUser', {
                where: searchFormData
            });
        });
        //打开新增弹窗
        $("#btnAdd").click(function (){
            //表单 生日日期控件
            laydate.render({
                elem: '#formBirthday', //指定元素
                type: 'date'
            });
            $('#formUser [type="reset"]').click();//重置表单
            //清空文件选择器
            document.getElementById("upPortrait").outerHTML=document.getElementById("upPortrait").outerHTML;
            $('#formUser [name="isInsert"]').val(1);//设置为新增
            $("#divUserPassword").show();//显示密码框
            loadDepartmentTreeSelect();//加载部门下拉树
            $("#userPicture").prop("src",'${ctx}/static/images/uploadImg.png');
            createH5Select("selectPositionId",'${ctx}/user?method=selectPositionForH5Select',{},null,function () {
                form.render('select'); //刷新select选择框渲染
            });
            createH5Select("selectRoleId",'${ctx}/user?method=selectRoleForH5Select',{},null,function () {
                form.render('select'); //刷新select选择框渲染
            });
            layerFormIndex = layer.open({
                type: 1,//0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
                skin:'layui-layer-molv',
                area: ['1000px', '710px'],
                title:'新增用户',
                content:$("#formUser"),
                cancel:function (){
                    layer.closeAll();
                }
            });
        });
        //打开修改弹窗
        $("#btnEdit").click(function (){
            //清空文件选择框
            document.getElementById("upPortrait").outerHTML=document.getElementById("upPortrait").outerHTML;
            $('#formUser [type="reset"]').click();//重置表单
            $('#formUser [name="isInsert"]').val(0);//设置为修改
            $("#divUserPassword").hide();//隐藏密码框
            //获取table中勾选的数据
            var selectData=table.checkStatus('tabUser');
            if(selectData.data.length>0){
                var selectId = selectData.data[0].id;
                //加载被修改数据
                $.post("${ctx}/user?method=selectById",{id:selectId},function (jsonMsg){
                    if(jsonMsg.state){//正常
                        //回填表单数据
                        form.val("formUser",jsonMsg.data);
                        form.val("formUser",{userPassword:"Abc12345"});//这个密码不会保存的数据库的，只是修改是避免layui 表单校验无法通过
                        //显示图片(回填图片)
                        if(jsonMsg.data.portrait!=undefined && jsonMsg.data.portrait!=null && jsonMsg.data.portrait!=""){
                            $("#userPicture").prop("src",'${ctx}/user?method=getPortraitImage&imgName='+jsonMsg.data.portrait);
                        }
                        var dt = new Date(jsonMsg.data.birthday);
                        var strDate=dt.getFullYear() + "-" + ((dt.getMonth() + 1)<10?"0":"")+(dt.getMonth() + 1) + "-" + (dt.getDate()<10?"0":"")+dt.getDate();
                        //表单 生日日期控件
                        laydate.render({
                            elem: '#formBirthday', //指定元素
                            type: 'date',
                            value:strDate
                        });
                        loadDepartmentTreeSelect(jsonMsg.data.departmentId);//加载部门下拉树
                        createH5Select("selectPositionId",'${ctx}/user?method=selectPositionForH5Select',{},jsonMsg.data.positionId,function (){
                            form.render('select'); //刷新select选择框渲染
                        });
                        createH5Select("selectRoleId",'${ctx}/user?method=selectRoleForH5Select',{},jsonMsg.data.roleId,function (){
                            form.render('select'); //刷新select选择框渲染
                        });
                        layerFormIndex = layer.open({
                            type: 1,//0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
                            skin:'layui-layer-molv',
                            area: ['1000px', '710px'],
                            title:'修改用户',
                            content:$("#formUser"),
                            cancel:function (){
                                layer.closeAll();
                            }
                        });

                    }else {
                        layer.msg(jsonMsg.msg,{icon:5});
                    }
                });


            }else {
                layer.msg("请选择需要修改的数据",{icon:5});
            }
        });
        //删除
        $("#btnDelete").click(function (){
            //获取table中勾选的数据
            var selectData= table.checkStatus('tabUser');
            if (selectData.data.length>0){
                var selectId=selectData.data[0].id;
                layer.confirm('您确定要删除该员工吗?', {icon: 3, title:'提示'}, function(index){
                    layer.close(index);//关闭询问框
                    //发送请求
                    var layerIndex=layer.load();//打开加载层
                    $.post('${ctx}/user?method=deleteById',{id:selectId},function (jsonMsg){
                        layer.close(layerIndex);//关闭加载层
                        if (jsonMsg.state){//正常
                            layer.msg(jsonMsg.msg,{icon:6});
                            table.reload('tabUser',{});//表格的重载
                        }else {
                            layer.msg(jsonMsg.msg,{icon:5});
                        }
                    });
                });
            }else {
                layer.msg("请选择需要删除的数据");
            }
        });
        //新增、修改表单提交事件  submit(提交按钮的lay-filter属性的值)
        form.on('submit(formUserSubmit)', function(fromData){
            // console.log(data.elem) //被执行事件的元素DOM对象，一般为button对象
            // console.log(data.form) //被执行提交的form对象，一般在存在form标签时才会返回
            console.log(fromData.field) //当前容器的全部表单字段，名值对形式：{name: value}
            //判断是新增 还是修改
            var url="";
            if (fromData.field.isInsert==="1"){//新增
                url='${ctx}/user?method=insert';
            }else {//修改
                url='${ctx}/user?method=update';
            }

            var upFormData=new FormData();
            //把layui form返回json格式数据转为 FormData
            /*
             fromData.field
             {
                a:"",
                b:"",
                c:"",
             }
             */
            // x是指被循环对象的属性名称
            //js的数组是特殊对象，x就是 索引 0,1,2,3.....
            for (var x in fromData.field) {
                //append(名称，值)
                upFormData.append(x,fromData.field[x])
            }
            //把文件添加到upFormData
            var file=$("#upPortrait").get(0).files[0];
            upFormData.append("portrait",file);

            // 提交表单
            var layerIndex=layer.load();
            $.ajax({
                type:"POST",
                url:url,
                data:upFormData,
                cache:false,
                processData:false,//禁止jq对上传的数据进行处理
                contentType:false,
                dataType:'json',
                success:function (jsonMsg){
                    layer.close(layerIndex);
                    if(jsonMsg.state){
                        layer.msg(jsonMsg.msg,{icon:6});
                        layer.close(layerFormIndex);//关闭弹窗
                        table.reload('tabUser',{});//表格重载
                    }else {
                        layer.msg(jsonMsg.msg,{icon:5});
                    }
                }
            });
            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });


        // 载入/重载（销毁再初始化） 部门下拉树
        function loadDepartmentTreeSelect(selectDepartmentId) {
            try {
                treeSelect.destroy('departmentIdTreeSelect');
            } catch {

            }
            //初始化
            treeSelect.render({
                elem: '#departmentId', // 选择器
                data: '${ctx}/user?method=selectForTreeSelect',// 数据
                type: 'post',// 异步加载方式：get/post，默认get
                placeholder: '请选择',// 占位符
                search: true,// 是否开启搜索功能：true/false，默认false
                // 一些可定制的样式
                style: {
                    folder: {enable: false},// 父节点图标
                    line: {enable: true} // 层级线是否显示
                },
                // 点击回调
                click: function (d) {
                    console.log(d);
                },
                // 加载完成后的回调函数
                success: function (d) {
                    // console.log(d);
                    // treeSelect.checkNode('parentIdTreeSelect', 3);//选中节点，根据id筛选
                    // var treeObj = treeSelect.zTree('tree');// 获取zTree对象，可以调用zTree方法
                    // console.log(treeObj);//
                    if (selectDepartmentId!=undefined && selectDepartmentId!=null){
                        treeSelect.checkNode('departmentIdTreeSelect', selectDepartmentId);
                    }
                    treeSelect.refresh('departmentIdTreeSelect');// 刷新树结构(parentIdTreeSelect 和  lay-filter值对应)
                }
            });
        }
        //图片上传部分
        //双击图片弹出文件选择框
        $("#userPicture").dblclick(function (){
            $("#upPortrait").click();
        });
        //图片文件 正则表达式过滤image/jpeg,image/png,image/jpg,image/gif,image/bmp
        var regexImageFilter = /^(?:image\/bmp|image\/gif|image\/jpg|image\/jpeg|image\/png)$/i;
        var imgReader = new FileReader();

        //文件读取器读取到文件后的回调事件
        imgReader.onload=function (event){
            ////显示图片 base64编码的图片
            $("#userPicture").attr("src",event.target.result);
        }
        $("#upPortraitDiv").on('change','input[type="file"]',function (){
            //获取出文件选择器中的第一个文件
            var file=$("#upPortrait").get(0).files[0];
            //判断文件选择类型
            if(regexImageFilter.test(file.type)){
                //读取文件转换成URL把图片转为Base64编码
                imgReader.readAsDataURL(file);
            }else {
                layer.alert("请选择图片");
            }
        });
        //导出===
        $("#btnExportXlsx").click(function () {
            //获取搜索表单的数据
            var searchFormData=form.val('formSearch');
            var parm='';
            //&xxx=xxxxx
            for (var x in searchFormData ) {
                parm=parm+"&"+x+"="+searchFormData[x];
            }
            console.log(parm)
            window.open('${ctx}/user?method=exportXlsx'+parm);
        });
        $("#btnExportXls").click(function () {
            //获取搜索表单的数据
            var searchFormData=form.val('formSearch');
            var parm='';
            //&xxx=xxxxx
            for (var x in searchFormData ) {
                parm=parm+"&"+x+"="+searchFormData[x];
            }
            console.log(parm)
            window.open('${ctx}/user?method=exportXls'+parm);
        });


    });
    //重置密码
    function resetPassword(id){
        //例子2
        layer.prompt({
            formType: 1,//输入框类型，支持0（文本）默认1（密码）2（多行文本）
            value: '',
            title: '请输新的密码',
        }, function(value, index, elem){
            // alert(value); //得到value
            // 发送请求
            var layerIndex=layer.load();//打开加载层
            $.post('${ctx}/user?method=resetPassword',{id:id,password:value},function (jsonMsg){
                layer.close(layerIndex);//关闭加载层
                if (jsonMsg.state){//正常
                    layer.msg(jsonMsg.msg,{icon:6});
                    layer.close(index);//输入层
                }else {
                    layer.msg(jsonMsg.msg,{icon:5});
                }
            });
        });

    }

</script>
</body>
</html>