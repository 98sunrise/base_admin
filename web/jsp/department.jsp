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
    <title>部门管理</title>
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
                                <label class="layui-form-label">部门名称</label>
                                <div class="layui-input-inline layui-show-xs-block">
                                    <input type="text" id="searchName" placeholder="部门名称" class="layui-input">
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
                        <table class="layui-hide" id="tableDepartment" lay-filter="tableDepartmentEvent"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%--表单弹窗--%>
<div>
    <form class="layui-form" id="formDepartment" lay-filter="formDepartment" style="display: none;margin: 20px;" autocomplete="off">
        <%--标识是新增1还是修改0--%>
        <input type="hidden" name="isInsert" value="1">
        <%--修改时存放主键id--%>
        <input type="hidden" name="id">
        <div class="layui-form-item">
            <label class="layui-form-label">上级部门</label>
            <div class="layui-input-block">
                <input type="text" id="parentId" name="parentId" class="layui-input" lay-verify="required"
                       lay-filter="parentIdTreeSelect">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">部门名称</label>
            <div class="layui-input-block">
                <input type="text" name="departmentName" lay-verify="required" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">部门电话</label>
            <div class="layui-input-block">
                <input type="text" name="telephone" class="layui-input" placeholder="部门电话" lay-verify="phoneNr">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">部门传真</label>
            <div class="layui-input-block">
                <input type="text" name="fax" class="layui-input"  placeholder="部门传真">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">email</label>
            <div class="layui-input-block">
                <input type="text" name="email" class="layui-input" placeholder="email" lay-verify="emailNr">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">部门负责人</label>
            <div class="layui-input-block">
                <input type="text" name="principal" class="layui-input" placeholder="部门负责人">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">显示排序</label>
            <div class="layui-input-block">
                <input type="number" name="departmentSort" required lay-verify="required" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">备注</label>
            <div class="layui-input-block">
                <textarea name="remark" placeholder="请输入备注（可留空）" class="layui-textarea"></textarea>
            </div>
        </div>
        <div class="layui-btn-container" style="text-align: end">
            <button type="submit" class="layui-btn" lay-submit lay-filter="formSubmit">提交</button>
            <button type="reset" class="layui-btn layui-btn-warm">重置</button>
        </div>
    </form>
</div>

<script src="${ctx}/static/lib/layui/layui.js" charset="utf-8"></script>
<script src="${ctx}/static/js/layuiVerifyExt.js" charset="utf-8"></script>
<script>
    var layerFormIndex;
    layui.extend({//layui 加载扩展组件
        tableEdit: 'static/module/js/tableEdit',   //表格树依赖另外tableEdit模块，本项目就有。
        tableTree: 'static/module/js/tableTree',   //树形表格
        treeSelect: 'static/module/js/treeSelect', //下拉树
    }).use(['tableEdit','tableTree','treeSelect'],function(){
        var table = layui.table,
            $ = layui.$,
            layer = layui.layer,
            form = layui.form,
            tableTree=layui.tableTree,
            treeSelect=layui.treeSelect;
        //注册自定义表单校验规则
        form.verify(formVerifyExt);

        // 菜单树形table初始化
        var tableDepartment = tableTree.render({
            elem: '#tableDepartment',//table元素
            id: 'tableDepartment',
            url: '${ctx}/department?method=selectPageList',//数据url
            page: false,//分页
            treeConfig: { //表格树所需配置
                showField: 'departmentName',  //表格树显示的字段
                treeid: 'id',  //treeid所对应字段的值在表格数据中必须是唯一的，且不能为空。
                treepid: 'parentId', //父级id字段名称
                iconClass: 'layui-icon-right',  //小图标class样式 窗口图标 layui-icon-layer
                showToolbar: true, //展示工具栏 false不展示 true展示
                rowToolIcon: false //['add','update','remove','sort']选择性开启功能图标，true：全部开启，false：全部关闭
            },
            cols: [[
                {type: 'radio'},
                {field: 'departmentName', title: '部门名称', width: 400},
                {field: 'id', title: 'id', event: 'id', width: 120, hide: true},
                {field: 'departmentSort', title: '显示顺序', event: 'sort', width: 110, align: 'center'},
                {field: 'telephone', title: '电话', event: 'url'},
                {field: 'fax', title: '传真', event: 'url'},
                {field: 'email', title: 'Email', event: 'url'},
                {field: 'principal', title: '负责人', event: 'url'},
                {field: 'remark', title: '备注', event: 'remark'}
            ]],
            done:function (){//加载完数据的回调
                tableDepartment.openAllTreeNodes();//打开所有的节点
                // tableDepartment.closeAllTreeNodes();//关闭所有的节点
            }
        });

        //搜索按钮
        $("#btnSearch").click(function (){
            var searchName=$("#searchName").val();
            tableDepartment.keywordSearch(searchName);
        });


        //打开新增弹窗
        $("#btnAdd").click(function (){
            loadDepartmentTreeSelect();//加载下拉树
            $('#formDepartment [type="reset"]').click();//重置表单
            form.val('formDepartment', {isInsert: 1});//设置为新增表单
            layerFormIndex = layer.open({
                type: 1,//0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
                skin:'layui-layer-molv',
                area:['500px','620px'],
                title:'新增部门',
                content:$("#formDepartment"),
                cancel:function (){
                    layer.closeAll();
                }
            });
        });
        //修改 打开弹窗
        $("#btnEdit").click(function (){
            //获取用户勾选的数据
            var checkedData = tableDepartment.getCheckedTreeNodeData();
            console.log(checkedData) //获取选中行的数据
            // console.log(checkStatus.data.length) //获取选中行数量，可作为是否有选中行的条件
            // console.log(checkStatus.isAll ) //表格是否全选
            if (checkedData.length>0){
                //获取选中行的id
                var selectId=checkedData[0].id;

                loadDepartmentTreeSelect();//加载下拉树
                $('#formDepartment [type="reset"]').click();//重置表单
                form.val('formDepartment', {isInsert: 0});//设置为修改表单
                //回填被修改的数据
                $.post('${ctx}/department?method=selectById',{id:selectId},function (jsonMsg){
                    if (jsonMsg.state){
                        //回填数据
                        form.val("formDepartment",jsonMsg.data);
                        //下拉树回填
                        treeSelect.checkNode('parentIdTreeSelect', jsonMsg.data.parentId);//选中节点，根据id筛选
                        layerFormIndex = layer.open({
                            type: 1,//0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
                            skin:'layui-layer-molv',
                            area:['500px','620px'],
                            title:'修改部门',
                            content:$("#formDepartment"),
                            cancel:function (){
                                layer.closeAll();
                            }
                        });
                    }else {
                        layer.msg(jsonMsg.msg,{icon:5});
                    }
                })
            }else{
                layer.msg("请选择需要修改的部门");
            }
        });
        //新增、修改表单提交事件  submit(提交按钮的lay-filter属性的值)
        form.on('submit(formSubmit)', function(fromData){
            // console.log(data.elem) //被执行事件的元素DOM对象，一般为button对象
            // console.log(data.form) //被执行提交的form对象，一般在存在form标签时才会返回
            console.log(fromData.field) //当前容器的全部表单字段，名值对形式：{name: value}
            //判断是新增 还是修改
            var url="";
            if (fromData.field.isInsert==="1"){//新增
                url='${ctx}/department?method=insert';
            }else {//修改
                url='${ctx}/department?method=update';
            }

            //提交表单
            var layerIndex=layer.load();
            $.post(url,fromData.field,function (jsonMsg){
                layer.close(layerIndex);
                if (jsonMsg.state){
                    layer.msg(jsonMsg.msg,{icon:6});
                    layer.close(layerFormIndex);//关闭弹窗
                    table.reload('tableDepartment',{});//表格的重载
                }else{
                    layer.msg(jsonMsg.msg,{icon:5});
                }
            })

            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });
        //删除 打开弹窗
        $("#btnDelete").click(function (){
            //获取用户勾选的数据
            var checkedData = tableDepartment.getCheckedTreeNodeData();
            console.log(checkedData) //获取选中行的数据
            // console.log(checkStatus.data.length) //获取选中行数量，可作为是否有选中行的条件
            // console.log(checkStatus.isAll ) //表格是否全选
            if (checkedData.length>0){
                //获取选中行的id
                var selectId=checkedData[0].id;
                layer.confirm('是否删除该部门?', {icon: 3, title:'提示'}, function(index){
                    layer.close(index);
                    //发送请求
                    var layerIndex=layer.load();
                    $.post('${ctx}/department?method=deleteById',{id:selectId},function (jsonMsg){
                        layer.close(layerIndex);
                        if (jsonMsg.state){
                            layer.msg(jsonMsg.msg,{icon:6});
                            table.reload('tableDepartment',{});//表格的重载
                        }else{
                            layer.msg(jsonMsg.msg,{icon:5});
                        }
                    })
                });


            }else{
                layer.msg("请选择需要修改的部门");
            }
        });

        // 载入/重载（销毁再初始化） 上级部门下拉树
        function loadDepartmentTreeSelect() {
            try {
                treeSelect.destroy('parentIdTreeSelect');
            } catch {

            }
            //初始化
            treeSelect.render({
                elem: '#parentId', // 选择器
                data: '${ctx}/department?method=SelectForTreeSelect',// 数据
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
                    //请求服务端 获取当前部门下 下一子部门排序序号
                    $.post('${ctx}/department?method=selectNextSort', {pid: d.current.id}, function (jsonMsg) {
                        if (jsonMsg.state) {//有返回
                            form.val('formDepartment', {
                                departmentSort: jsonMsg.data
                            });
                        } else {
                            form.val('formDepartment', {
                                departmentSort: 0
                            });
                        }
                    }, 'json');
                },
                // 加载完成后的回调函数
                success: function (d) {
                    // console.log(d);
                    // treeSelect.checkNode('parentIdTreeSelect', 3);//选中节点，根据id筛选
                    // var treeObj = treeSelect.zTree('tree');// 获取zTree对象，可以调用zTree方法
                    // console.log(treeObj);//
                    treeSelect.refresh('parentIdTreeSelect');// 刷新树结构(parentIdTreeSelect 和  lay-filter值对应)
                }
            });
        }
    });


</script>
</body>
</html>