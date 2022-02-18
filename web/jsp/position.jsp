<%--
  Created by IntelliJ IDEA.
  User: CHH
  Date: 2021/4/18
  Time: 10:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page"/>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>职位管理</title>
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
        /**
            去除xadmin 对layui table最小行高
         */
        .layui-table td, .layui-table th{
            min-width: 0; !important;

        }
        body .layui-table-cell .layui-form-radio {
            line-height: 28px;
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
        <a class="layui-btn layui-btn-small"style="..."
        onclick="location.reload()" title="刷新">
            <i class="layui-icon layui-icon-refresh" style="..."></i>
        </a>
    </div>
    <%--主容器布局--%>
    <div class="layui-fluid">
        <div class="layui-row layui-col-space15">
            <div class="layui-col-md12">
                <div class="layui-card">
                    <%--搜索栏--%>
                    <div class="layui-card-body">
                        <form class="layui-form layui-col-space5">
                            <div class="layui-inline layui-show-xs-block">
                                <label class="layui-form-label">职位名称</label>
                                <div class="layui-input-inline layui-show-xs-block">
                                    <input type="text" id="searchName" placeholder="职位名称" class="layui-input">
                                </div>
                            </div>
                            <div class="layui-inline layui-show-xs-block">
                                <label class="layui-form-label">职位状态</label>
                                <div class="layui-input-inline layui-show-xs-block">
                                    <select id="searchStatus">
                                        <option value="">全部</option>
                                        <option value="1">启用</option>
                                        <option value="0">禁用</option>
                                    </select>
                                </div>
                            </div>
                            <div class="layui-inline layui-show-xs-block">
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
                    <div class="layui-card-body">
                        <table class="layui-hide" id="tablePosition" lay-filter="tablePositionEvent"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%--表单弹窗--%>
<div>
    <form class="layui-form" id="formPosition" lay-filter="formPostion" style="display: none;margin: 20px;" autocomplete="off">
        <%--标识是新增1 还是修改0--%>
        <input type="hidden" name="isInsert" value="1">
        <%--修改时存放主键ID--%>
        <input type="hidden" name="id">
        <div class="layui-form-item">
            <label class="layui-form-label">职位名称</label>
            <div class="layui-input-block">
                <input type="text" name="positionName" lay-verify="required" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">显示排序</label>
            <div class="layui-input-block">
                <input type="number" name="positionSort" required lay-verify="required" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">职位状态</label>
            <div class="layui-input-block">
                <input type="radio" name="positionStatus" value="1" title="启用" checked>
                <input type="radio" name="positionStatus" value="0" title="禁用">
            </div>
        </div>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">备注</label>
            <div class="layui-input-block">
                <textarea name="remark" placeholder="请输入备注（可留空）" class="layui-textarea"></textarea>
            </div>
        </div>
        <div class="layui-btn-container" style="text-align: end">
            <button type="button" class="layui-btn" layui-submit lay-filter="formPositionSubmit" id="btnSubmit">提交</button>
            <button type="reset" class="layui-btn layui-btn-warm">重置</button>
        </div>

    </form>
</div>
<script src="${ctx}/static/lib/layui/layui.js" charset="utf-8"></script>
<script>
    var layerFormIndex;
    layui.use(function (){
        var table=layui.table,
            $=layui.$,
            layer=layui.layer,
            form=layui.form;
        //菜单树形talbe初始化
        var tablePosition=table.render({
            elem:'#tablePosition',
            id:'tablePosition',
            url:'${ctx}/position?method=selectPageList',//数据url
            page:true,
            cols: [[
                {type:'radio'},//单选框
                {field:'positionName',title:'职位名称',width:400},
                {field:'id',title:'id',event:'id',width:120,hide:true},
                {field:'positionSort',title:'显示顺序',event:'sort',width:110,align:'center'},
                {
                    field: 'positionStatus',
                    title: '状态',
                    event: 'positionStatus',
                    width: 80,
                    templet:function (rowData){
                        if(rowData.positionStatus===1){//目录
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
            limit:10,//meiyeyeshu
            limits:[5,10,15,20,25,30]//定义每页页数选择下拉框

        });
        //查询按钮
        $("#btnSearch").click(function (){
            var searchName=$("#searchName").val();
            var searchStatus=$("#searchStatus").val();
            //表格重载
            table.reload('tablePosition',{
                where:{
                    searchName:searchName,
                    searchStatus:searchStatus,

                }
            });
        });
        //打开新增弹窗
        $("#btnAdd").click(function (){
           $('#formPosition [type="reset"]').click();//重置表单
           $('#formPosition [name="isInsert"]').val(1);//设置为新增
            //获取下一序号
            $.post('${ctx}/position?method=selectNextSort',function (jsonMsg){
                var nextSort=jsonMsg.data;
                $('#formPosition [name="positionSort"]').val(nextSort);
            });
           layerFormIndex=layer.open({
              type: 1,//0(信息框，默认) 1（页面层） 2（iframe层）3（加载层）4（tips层）
              skin:'layui-layer-molv',
               area:['500px','420px'],
               title:'新增职位',
               content:$("#formPosition"),
               cancel:function (){
                  layer.closeAll();
               }
           });

        });
        //打开修改弹窗（表单回填）
        $("#btnEdit").click(function (){
            $('#formPosition [type="reset"]').click();//重置表单
            $('#formPosition [name="isInsert"]').val(0);//设置为修改
            //获取table中勾选的数据
            var selectData=table.checkStatus('tablePosition');
            if(selectData.data.length>0){
                var selectId=selectData.data[0].id;
                //加载被修改数据
                $.post('${ctx}/position?method=selectById',{
                    id:selectId
                },function (jsonMsg){
                    if(jsonMsg.state){
                        //数据回填
                        $('#formPosition [name="id"]').val(jsonMsg.data.id);
                        $('#formPosition [name="positionName"]').val(jsonMsg.data.positionName);
                        $('#formPosition [name="positionSort"]').val(jsonMsg.data.positionSort);
                        //删除所有单选 选中
                        $('#formPosition [name="positionStatus"]').removeAttr("checked");
                        //通过选择器 选中需要回填的radio，设置checked
                        $('#formPosition [name="positionStatus"][value="'+jsonMsg.data.positionStatus+'"]').prop("checked","checked");
                        $('#formPosition [name="remark"]').val(jsonMsg.data.remark);
                        //由于使用的layui form 需要在修改值后更新表单
                        form.render();//更新全部

                        //打开修改弹窗
                        layerFormIndex=layer.open({
                            type: 1,//0(信息框，默认) 1（页面层） 2（iframe层）3（加载层）4（tips层）
                            skin:'layui-layer-molv',
                            area:['500px','420px'],
                            title:'修改职位',
                            content:$("#formPosition"),
                            cancel:function (){
                                layer.closeAll();
                            }
                        });
                    }else {
                        layer.msg(jsonMsg.msg,{icon:5});
                    }
                });

            }else {
                layer.msg("请选择需要被修改的数据！")
            }

        });
        //提交表单
        $("#btnSubmit").click(function (){
            //判断是新增还是修改
            var isInsert=$('#formPosition [name="isInsert"]').val();
            var url="";
            if(isInsert=="1"){
                url='${ctx}/position?method=insert';
            }else {
                url='${ctx}/position?method=update';
            }
            //获取表单值
            var id=$('#formPosition [name="id"]').val();
            var positionName=$('#formPosition [name="positionName"]').val();
            var positionSort=$('#formPosition [name="positionSort"]').val();
            var positionStatus=$('#formPosition [name="positionStatus"]:checked').val();
            var remark=$('#formPosition [name="remark"]').val();
            if(positionName===undefined||positionName===null||positionName===""){
                layer.msg("请输入职位名称",{icon:5});
                return;
            }
            if(positionSort===undefined||positionSort===null||positionSort===""){
                layer.msg("请输入正确的排序（整数）",{icon:5});
                return;
            }
            if(positionStatus===undefined||positionStatus===null||positionStatus===""){
                layer.msg("请输入职位状态",{icon:5});
                return;
            }
            //表单提交
            layerIndex=layer.load();//打开加载层
            $.post(url,{
                id:id,
                positionName:positionName,
                positionSort:positionSort,
                positionStatus:positionStatus,
                remark:remark
            },function (jsonMsg){
                layer.close(layerIndex);
               if(jsonMsg.state){
                   layer.msg(jsonMsg.msg,{icon:6});
                   layer.close(layerFormIndex);//关闭弹窗
                   table.reload('tablePosition',{});//表格重载
               }else {
                   layer.msg(jsonMsg.msg,{icon:5});
               }
            });

        });
        //删除
        $("#btnDelete").click(function (){

            //获取table中勾选的数据
            var selectData=table.checkStatus('tablePosition');
            if(selectData.data.length>0){
                var selectId=selectData.data[0].id;
                //加载被修改数据
                layer.confirm('你确定要删除该职位吗？',{icon:3,title:'提示'},function (index){
                    layer.close(index);//关闭询问框
                    //发送请求
                    var layerIndex=layer.load();//打开加载层
                    $.post('${ctx}/position?method=deleteById',{id:selectId},function (jsonMsg){
                        layer.close(layerIndex);
                        if(jsonMsg.state){
                            layer.msg(jsonMsg.msg,{icon:6});
                            table.reload('tablePosition',{});//表格重载
                        }else {
                            layer.msg(jsonMsg.msg,{icon:5});
                        }
                    });
                });

            }else {
                layer.msg("请选择需要被删除的数据！")
            }

        });
    });
</script>
</body>
</html>
