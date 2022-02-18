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
    <title>角色管理</title>
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
                    <cite>菜单管理</cite>
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
            <div class="layui-col-md12">
                <div class="layui-card">
                    <%--搜索栏--%>
                    <div class="layui-card-body ">
                        <form class="layui-form layui-col-space5">
                            <div class="layui-inline layui-show-xs-block">
                                <label class="layui-form-label">角色名称</label>
                                <div class="layui-input-inline layui-show-xs-block">
                                    <input type="text" id="searchName" placeholder="角色名称" class="layui-input">
                                </div>
                            </div>
                            <div class="layui-inline layui-show-xs-block">
                                <label class="layui-form-label">角色状态</label>
                                <div class="layui-input-inline layui-show-xs-block">
                                    <select id="searchStatus">
                                        <option value="">全部</option>
                                        <option value="1">启用</option>
                                        <option value="0">禁用</option>
                                    </select>
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
                    </div>
                    <%--表格--%>
                    <div class="layui-card-body ">
                        <table class="layui-hide" id="tableRole" lay-filter="tableRoleEvent"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%--表单弹窗--%>
<div>
    <form class="layui-form" id="formRole" lay-filter="formRole" style="display: none;margin: 20px;"
          autocomplete="off">
        <%--标识是新增1还是修改0--%>
        <input type="hidden" name="isInsert" value="1">
        <%--修改时存放主键id--%>
        <input type="hidden" name="id">
        <div class="layui-form-item">
            <label class="layui-form-label">角色名称</label>
            <div class="layui-input-block">
                <input type="text" name="roleName" lay-verify="required" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">显示排序</label>
            <div class="layui-input-block">
                <input type="number" name="roleSort" required lay-verify="required" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">角色状态</label>
            <div class="layui-input-block">
                <input type="radio" name="roleStatus" value="1" title="启用" checked>
                <input type="radio" name="roleStatus" value="0" title="禁用">
            </div>
        </div>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">备注</label>
            <div class="layui-input-block">
                <textarea name="remark" placeholder="请输入备注（可留空）" class="layui-textarea"></textarea>
            </div>
        </div>
        <div class="layui-btn-container" style="text-align: end">
            <button type="button" class="layui-btn" id="btnSubmit">提交</button>
            <button type="reset" class="layui-btn layui-btn-warm">重置</button>
        </div>
    </form>
</div>

<script src="${ctx}/static/lib/layui/layui.js" charset="utf-8"></script>
<script>
    var layerFormIndex;
    layui.use(function(){
        var table = layui.table,
            $ = layui.$,
            layer = layui.layer,
            form = layui.form;
        // 菜单树形table初始化
        var tableRole = table.render({
            elem: '#tableRole',//table元素
            id: 'tableRole',
            url: '${ctx}/role?method=selectPageList',//数据url
            page: true,//分页
            cols: [[
                {type:'radio'},//单选框
                {field:'roleName',title:'角色名称',width:400},
                {field:'id',title:'id',event:'id',width:120,hide:true},
                {field:'roleSort',title:'显示顺序',event:'sort',width:110,align:'center'},
                {
                    field: 'roleStatus',
                    title: '状态',
                    event: 'roleStatus',
                    width: 80,
                    templet:function (rowData){
                        if(rowData.roleStatus===1){//目录
                            return '<span class="layui-badge layui-bg-green">启用</span>';
                        }else {
                            return '<span class="layui-badge layui-bg-orange">禁用</span>';
                        }
                    }
                },
                {
                    field: 'gmtCreate',title: '创建时间',event: 'gmtCreate',width: 180,templet:function (rowData){
                        var dt=new Date(rowData.gmtCreate);
                        return dt.getFullYear()+"-"+dt.getMonth()+"-"+dt.getDate()+" "+dt.getHours()
                            +":"+dt.getMinutes()+":"+dt.getSeconds();
                    }

                },
                {
                    field: 'remark',title: '备注',event: 'remark'
                }
            ]],
            limit: 10,//每页页数
            limits: [5, 10, 15, 20, 25, 30, 35, 40, 45, 50]//定义每页页数选择下拉框
        });


        //查询按钮
        $("#btnSearch").click(function (){
            var searchName=$("#searchName").val();
            var searchStatus=$("#searchStatus").val();
            //表格重载
            table.reload('tableRole', {
                where: {
                    searchName:searchName,
                    searchStatus:searchStatus
                } //设定异步数据接口的额外参数
            });
        });

        //打开新增弹窗
        $("#btnAdd").click(function (){
            $('#formRole [type="reset"]').click();//重置表单
            $('#formRole [name="isInsert"]').val(1);//设置为新增

            layerFormIndex = layer.open({
                type: 1,//0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
                skin:'layui-layer-molv',
                area:['500px','420px'],
                title:'新增角色',
                content:$("#formRole"),
                cancel:function (){
                    layer.closeAll();
                }
            });
        });

        //打开修改弹窗（表单回填）
        $("#btnEdit").click(function (){
            $('#formRole [type="reset"]').click();//重置表单
            $('#formRole [name="isInsert"]').val(0);//设置为修改
            //获取table中勾选的数据
            var selectData= table.checkStatus('tableRole');
            if (selectData.data.length>0){
                var selectId=selectData.data[0].id;
                //加载被修改数据
                $.post('${ctx}/role?method=selectById',{id:selectId},function (jsonMsg){
                    if (jsonMsg.state){//正常
                        //数据回填
                        $('#formRole [name="id"]').val(jsonMsg.data.id);
                        $('#formRole [name="roleName"]').val(jsonMsg.data.roleName);
                        $('#formRole [name="roleSort"]').val(jsonMsg.data.roleSort);
                        //移除所有单选 选中
                        $('#formRole [name="roleStatus"]').removeAttr("checked");
                        //通过选择器 选中需要回填的radio ，设置checked
                        $('#formRole [name="roleStatus"][value="'+jsonMsg.data.roleStatus+'"]').prop("checked","checked");
                        $('#formRole [name="remark"]').val(jsonMsg.data.remark);
                        //由于使用的layui form 需要在修改值后更新表单
                        form.render(); //更新全部

                        //打开修改弹窗
                        layerFormIndex = layer.open({
                            type: 1,//0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
                            skin:'layui-layer-molv',
                            area:['500px','420px'],
                            title:'修改角色',
                            content:$("#formRole"),
                            cancel:function (){
                                layer.closeAll();
                            }
                        });
                    }else {
                        layer.msg(jsonMsg.msg,{icon:5});
                    }
                });
            }else {
                layer.msg("请选择需要修改的数据");
            }
        });



        //提交表单
        $("#btnSubmit").click(function (){
            //判断是新增还是修改 指定不同url
            var isInsert=$('#formRole [name="isInsert"]').val();
            var url="";
            if (isInsert=="1"){
                url='${ctx}/role?method=insert';
            }else {
                url='${ctx}/role?method=update';
            }

            //获取表单值
            var id=$('#formRole [name="id"]').val();
            var roleName=$('#formRole [name="roleName"]').val();
            var roleSort=$('#formRole [name="roleSort"]').val();
            var roleStatus=$('#formRole [name="roleStatus"]:checked').val();
            var remark=$('#formRole [name="remark"]').val();
            if (roleName===undefined || roleName===null || roleName===""){
                layer.msg("请输入角色名称",{icon:5});
                return;
            }
            if (roleSort===undefined || roleSort===null || isNaN(roleSort)){
                layer.msg("请输入正确的角色排序(整数)",{icon:5});
                return;
            }
            if (roleStatus===undefined || roleStatus===null || isNaN(roleStatus)){
                layer.msg("请选择角色状态",{icon:5});
                return;
            }
            //表单提交
            layerIndex=layer.load();//打开加载层
            $.post(url,{
                id:id,
                roleName:roleName,
                roleSort:roleSort,
                roleStatus:roleStatus,
                remark:remark
            },function (jsonMsg){
                layer.close(layerIndex);//关闭加载层
                if (jsonMsg.state){
                    layer.msg(jsonMsg.msg,{icon:6});
                    layer.close(layerFormIndex);//关闭弹窗
                    table.reload('tableRole',{});//表格的重载
                }else{
                    layer.msg(jsonMsg.msg,{icon:5});
                }
            });

        });


        //删除
        $("#btnDelete").click(function (){
            //获取table中勾选的数据
            var selectData= table.checkStatus('tableRole');
            if (selectData.data.length>0){
                var selectId=selectData.data[0].id;
                layer.confirm('您确定要删除该角色吗?', {icon: 3, title:'提示'}, function(index){
                    layer.close(index);//关闭询问框
                    //发送请求
                    var layerIndex=layer.load();//打开加载层
                    $.post('${ctx}/role?method=deleteById',{id:selectId},function (jsonMsg){
                        layer.close(layerIndex);//关闭加载层
                        if (jsonMsg.state){//正常
                            layer.msg(jsonMsg.msg,{icon:6});
                            table.reload('tableRole',{});//表格的重载
                        }else {
                            layer.msg(jsonMsg.msg,{icon:5});
                        }
                    });
                });
            }else {
                layer.msg("请选择需要删除的数据");
            }
        });

    });


</script>
</body>
</html>