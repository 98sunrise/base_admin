package com.gx.servlet.Impl;

import com.gx.common.BaseServlet;
import com.gx.po.SysUser;
import com.gx.service.Impl.IUserService;
import com.gx.service.Impl.UserServiceImpl;
import com.gx.util.MD5Util;
import com.gx.util.Tools;
import com.gx.vo.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class UserServlet extends BaseServlet {
    // 上传配置
    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 10;  // 10MB 内存临界值 - 超过后将产生临时文件并存储于临时目录中
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 30; // 30MB 最大文件大小
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 40; // 40MB 最大请求大小
    private static final String UPLOAD_PATH="D:/2021project/javaProjectUp/BaseAdmin/user/";//员工头像上传目录

    //service
    private final IUserService userService=new UserServiceImpl();
    /**
     * 页面
     */
    public void index(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/user.jsp").forward(request,response);
    }

    /**
     * 查询部门数据for 下拉树
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void selectDepartmentForTree(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<LayuiTreeVo> data=this.userService.selectDepartmentForTree();
        returnJson(response,data);

    }

    /**
     * 分页查询
     */
    public void selectPageList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int page=this.getParamPage(request);
        int limit=this.getParamLimit(request);
        String strDepartmentId=request.getParameter("departmentId");
        String userName=request.getParameter("userName");
        String realName=request.getParameter("realName");
        String mobile=request.getParameter("mobile");
        String strUserStatus=request.getParameter("userStatus");
        String startEndDate=request.getParameter("startEndDate");
        //部门id
        Integer departmentId=null;
        if(Tools.isNotNull(strDepartmentId)){
            departmentId=Integer.parseInt(strDepartmentId);
        }
        //状态
        Integer userStatus=null;
        if(Tools.isNotNull(strUserStatus)){
            userStatus=Integer.parseInt(strUserStatus);
        }
        //开始时间和结束时间
        String startDate=null;
        String endDate=null;
        if(Tools.isNotNull(startEndDate)){
            String[] strDates=startEndDate.split(" - ");
            if(strDates.length==2){
                if(strDates[0].matches("^\\d{4}-\\d{2}-\\d{2}$")){
                    startDate=strDates[0];
                }
                if(strDates[1].matches("^\\d{4}-\\d{2}-\\d{2}$")){
                    endDate=strDates[1];
                }
            }
        }
        LayuiTableData<UserVo> layuiTableData=this.userService.selectPageList(page,limit,departmentId,userName,realName,mobile,userStatus,startDate,endDate);
        returnJson(response,layuiTableData);

    }
    /**
     * 查询部门数据 for 下拉树形(表单弹窗里面的)
     */
    public void selectForTreeSelect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<TreeSelectVo> data=this.userService.selectForTreeSelect();
        returnJson(response,data);
    }

    /**
     * 查询职位信息 for h5下拉框
     */
    public void selectPositionForH5Select(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        returnJson(response,this.userService.selectPositionForH5Select());
    }

    /**
     * 查询角色信息 for h5下拉框
     */
    public void selectRoleForH5Select(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        returnJson(response,this.userService.selectRoleForH5Select());
    }

    /**
     * 根据图片名返回图片 流
     */
    public void getPortraitImage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取参数
        String imgName=request.getParameter("imgName");
        if (Tools.isNotNull(imgName)){
            //图片名不未空
            String imgPath=UPLOAD_PATH+imgName;
            File fileImg=new File(imgPath);
            if (fileImg.exists()){
                //指定返的类型
                response.setContentType(Tools.getImageContentType(imgName));

                InputStream in=null;
                OutputStream out=null;
                try {
                    in= new FileInputStream(fileImg);
                    out=response.getOutputStream();
                    //复制
                    // byte[] buff=new byte[1024*1024*10];//10M
                    // int count=0;
                    // while ((count=in.read(buff,0,buff.length))!=-1){
                    //     out.write(buff,0,count);
                    // }
                    //commons-io
                    IOUtils.copy(in,out);
                    out.flush();
                }finally {
                    if (in!=null)in.close();
                    if (out!=null)out.close();
                }

            }
        }
    }
    /**
     * 新增
     */
    public void insert(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonMsg jsonMsg=new JsonMsg();
        //判断表单是否是文件上传的表单
        if(!ServletFileUpload.isMultipartContent(request)){
            // 如果不是则停止
            jsonMsg.setMsg("Error: 表单必须包含 enctype=multipart/form-data");
            returnJson(response,jsonMsg);
        }
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd_HHmmssSSS_");

        //配置上传相关的参数
        DiskFileItemFactory factory=new DiskFileItemFactory();
        //设置存临界值 - 超过后将产生临时文件并存储于临时目录中
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        //设置临时存储目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        ServletFileUpload upload=new ServletFileUpload(factory);
        //设置文件最大值 30M
        upload.setFileSizeMax(MAX_FILE_SIZE);
        //设置请求的大小最大值
        upload.setFileSizeMax(MAX_REQUEST_SIZE);
        //设置中文编码
        upload.setHeaderEncoding("UTF-8");
        //判断文件存放目录是否存在,如果不存在就创建所有目录
        File uploadDir=new File(UPLOAD_PATH);
        if(!uploadDir.exists()){
            uploadDir.mkdirs();
        }
        SysUser saveUser=new SysUser();
        //解析请求内容，提取文件 和 普通参数
        try{
            List<FileItem> fileItems=upload.parseRequest(request);
            if(fileItems !=null && fileItems.size() > 0){
                //遍历
                for (FileItem item:fileItems) {
                    //获取自动名称 -- 参数名request.getParameter("")
                    String fieldName=item.getFieldName();
                    //判断是文件 还是普通字段
                    if(!item.isFormField()){
                        //不是表单元素  文件
                         if("portrait".equals(fieldName)){//判断是否是头像文件
                            //拼接文件名  item.getName()--》文件名
                            String fileName=dateFormat.format(new Date())+System.nanoTime()+Tools.getFileExt(item.getName());
                            //存放路径
                            String filePath=UPLOAD_PATH+fileName;
                            File saveFile=new File(filePath);
                            System.err.println(filePath);
                            //保存文件到硬盘
                            item.write(saveFile);
                            //把文件名保存到需要新增的对象中
                            saveUser.setPortrait(fileName);
                        }

                    }else {
                        // 表单元素
                        //需要通过流去读取
                        BufferedReader br=new BufferedReader(new InputStreamReader(item.getInputStream()));
                        String strValue=br.readLine();//读取到值
                        if(fieldName==null) continue;
                        switch (fieldName){
                            case "userName"://用户名
                                if(Tools.isNotNull(strValue)){
                                    saveUser.setUserName(strValue);
                                }else {
                                    throw new RuntimeException("请输入用户名！");
                                }
                                break;
                            case "userPassword":
                                String userPassword=strValue;
                                if(Tools.isNotNull(userPassword)){
                                    Random random=new Random();
                                    //生成一个随机的8位数作为盐   10000000 ~ 99999999
                                    String salt=String.valueOf(random.nextInt(90000000)+10000000);
                                    //对输入的密码+盐 取MD5值
                                    userPassword= MD5Util.getMD5(userPassword+salt);
                                    saveUser.setUserPassword(userPassword);
                                    saveUser.setSalt(salt);
                                }else {
                                    throw new RuntimeException("请输入密码！");
                                }
                                break;
                            case "departmentId" :
                                if (Tools.isInteger(strValue)){
                                    saveUser.setDepartmentId(Integer.parseInt(strValue));
                                }else {
                                    throw new RuntimeException("请选择部门！");
                                }
                                break;
                            case "positionId":
                                if (Tools.isInteger(strValue)) {
                                    saveUser.setPositionId(Integer.parseInt(strValue));
                                } else {
                                    throw new RuntimeException("请选择部门！");
                                }
                                break;
                            case "roleId": //角色id
                                if (Tools.isInteger(strValue)) {
                                    saveUser.setRoleId(Integer.parseInt(strValue));
                                } else {
                                    throw new RuntimeException("请选择角色！");
                                }
                                break;
                            case "realName": //姓名
                                saveUser.setRealName(strValue);
                                break;
                            case "gender": //性别
                                if (Tools.isInteger(strValue)) {
                                    saveUser.setGender(Byte.parseByte(strValue));
                                } else {
                                    throw new RuntimeException("请选择性别！");
                                }
                                break;
                            case "birthday": //生日
                                if(Tools.isNotNull(strValue)){
                                    SimpleDateFormat dateFormatBirthday=new SimpleDateFormat("yyyy-MM-dd");
                                    saveUser.setBirthday(dateFormatBirthday.parse(strValue));
                                }
                                break;
                            case "email": //email
                                saveUser.setEmail(strValue);
                                break;
                            case "mobile": //手机号
                                saveUser.setMobile(strValue);
                                break;
                            case "qq": //qq
                                saveUser.setQq(strValue);
                                break;
                            case "wechat": //微信
                                saveUser.setWechat(strValue);
                                break;
                            case "userStatus": //状态
                                if (Tools.isInteger(strValue)) {
                                    saveUser.setUserStatus(Byte.parseByte(strValue));
                                } else {
                                    throw new RuntimeException("请选择性别！");
                                }
                                break;
                            case "remark": //备注
                                saveUser.setRemark(strValue);
                                break;
                        }
                    }
                }
                boolean isOk=this.userService.insert(saveUser);
                if (isOk){
                    jsonMsg.setState(true);
                    jsonMsg.setMsg("新增成功");
                }else{
                    jsonMsg.setMsg("新增失败");
                }
            }else {
                jsonMsg.setMsg("参数异常");
            }
        }catch (FileUploadBase.SizeLimitExceededException e) {
            e.printStackTrace();
            jsonMsg.setMsg("您上传文件超过了上传文件" + MAX_FILE_SIZE + "M 的限制");
        } catch (FileUploadException e) {
            e.printStackTrace();
            jsonMsg.setMsg("文件上传失败");
        } catch (RuntimeException e) {
            e.printStackTrace();
            //数据校验失败的异常信息
            jsonMsg.setMsg(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            jsonMsg.setMsg("表单提交失败");
        }
        returnJson(response,jsonMsg);
    }
    /**
     * 根据id查询用户数据 by id
     */
    public void selectById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonMsg jsonMsg=new JsonMsg();
        String strId=request.getParameter("id");
        if(Tools.isInteger(strId)){
            SysUser user=this.userService.selectById(Integer.parseInt(strId));
            jsonMsg.setState(true);
            jsonMsg.setData(user);
        }else {
            jsonMsg.setMsg("非法访问");
        }
        returnJson(response,jsonMsg);
    }

    /**
     * 修改
     */
    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonMsg jsonMsg=new JsonMsg();
        //判断表单是否是文件上传的表单
        if (!ServletFileUpload.isMultipartContent(request)){
            // 如果不是则停止
            jsonMsg.setMsg( "Error: 表单必须包含 enctype=multipart/form-data");
            returnJson(response,jsonMsg);
        }
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd_HHmmssSSS_");

        //配置上传相关的参数
        DiskFileItemFactory factory=new DiskFileItemFactory();
        //设置存临界值 - 超过后将产生临时文件并存储于临时目录中
        factory.setSizeThreshold(MEMORY_THRESHOLD);

        //设置临时存储目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        ServletFileUpload upload=new ServletFileUpload(factory);
        //设置文件最大值 30M
        upload.setFileSizeMax(MAX_FILE_SIZE);
        //设置请求的大小最大值
        upload.setSizeMax(MAX_REQUEST_SIZE);

        //中文编码
        upload.setHeaderEncoding("UTF-8");

        //判断文件存放目录是否存在
        File uploadDir=new File(UPLOAD_PATH);
        if (!uploadDir.exists()){
            uploadDir.mkdirs();
        }

        SysUser saveUser=new SysUser();
        //解析请求内容，提前文件 和 普通参数
        try {
            List<FileItem> fileItems=upload.parseRequest(request);

            if (fileItems != null && fileItems.size() > 0){
                //遍历
                for (FileItem item:fileItems) {
                    //获取自动名称 -- 参数名request.getParameter("")
                    String fieldName=item.getFieldName();
                    //判断是文件 还是普通字段
                    if (!item.isFormField()){
                        //不是表单元素  文件
                        if ("portrait".equals(fieldName)){//判断是否是头像文件
                            //拼接文件名  item.getName()--》文件名
                            String fileName=dateFormat.format(new Date())+System.nanoTime()+Tools.getFileExt(item.getName());
                            //存放路径
                            String filePath=UPLOAD_PATH+fileName;
                            File saveFile=new File(filePath);
                            System.err.println(filePath);
                            //保存文件到硬盘
                            item.write(saveFile);
                            //把文件名保存到需要新增的对象中
                            saveUser.setPortrait(fileName);
                        }
                    }else {
                        // 表单元素
                        //需要通过流去读取
                        BufferedReader br=new BufferedReader(new InputStreamReader(item.getInputStream(), StandardCharsets.UTF_8));
                        String strValue=br.readLine();//读取到值
                        if (fieldName==null) continue;
                        switch (fieldName) {
                            case "userName": //用户名
                                if (Tools.isNotNull(strValue)) {
                                    saveUser.setUserName(strValue);
                                } else {
                                    throw new RuntimeException("请输入用户名！");
                                }
                                break;
                            case "departmentId": //部门id
                                if (Tools.isInteger(strValue)) {
                                    saveUser.setDepartmentId(Integer.parseInt(strValue));
                                } else {
                                    throw new RuntimeException("请选择部门！");
                                }
                                break;
                            case "positionId": //职位id
                                if (Tools.isInteger(strValue)) {
                                    saveUser.setPositionId(Integer.parseInt(strValue));
                                } else {
                                    throw new RuntimeException("请选择部门！");
                                }
                                break;
                            case "roleId": //角色id
                                if (Tools.isInteger(strValue)) {
                                    saveUser.setRoleId(Integer.parseInt(strValue));
                                } else {
                                    throw new RuntimeException("请选择角色！");
                                }
                                break;
                            case "realName": //姓名
                                saveUser.setRealName(strValue);
                                break;
                            case "gender": //性别
                                if (Tools.isInteger(strValue)) {
                                    saveUser.setGender(Byte.parseByte(strValue));
                                } else {
                                    throw new RuntimeException("请选择性别！");
                                }
                                break;
                            case "birthday": //生日
                                if (Tools.isNotNull(strValue)) {
                                    SimpleDateFormat dateFormatBirthday = new SimpleDateFormat("yyyy-MM-dd");
                                    saveUser.setBirthday(dateFormatBirthday.parse(strValue));
                                }
                                break;
                            case "email": //email
                                saveUser.setEmail(strValue);
                                break;
                            case "mobile": //手机号
                                saveUser.setMobile(strValue);
                                break;
                            case "qq": //qq
                                saveUser.setQq(strValue);
                                break;
                            case "wechat": //微信
                                saveUser.setWechat(strValue);
                                break;
                            case "userStatus": //状态
                                if (Tools.isInteger(strValue)) {
                                    saveUser.setUserStatus(Byte.parseByte(strValue));
                                } else {
                                    throw new RuntimeException("请选择性别！");
                                }
                                break;
                            case "remark": //备注
                                saveUser.setRemark(strValue);
                                break;
                            case "id": //id
                                if (Tools.isInteger(strValue)) {
                                    saveUser.setId(Integer.parseInt(strValue));
                                } else {
                                    throw new RuntimeException("非法访问！");
                                }
                                break;
                        }
                    }
                }
                //查询未修改的数据
                SysUser oldUser=this.userService.selectById(saveUser.getId());
                //判断是否上传新图片 null未上传  ！=null上传
                boolean isUploadNewImg=saveUser.getPortrait()!=null;
                //把以前的密码和盐填入saveUser
                saveUser.setUserPassword(oldUser.getUserPassword());
                saveUser.setSalt(oldUser.getSalt());
                //判断原来是否有图片
                boolean hasOldImg=oldUser.getPortrait()!=null;
                //判断是否新上传图片
                if (saveUser.getPortrait()==null){//未上传图片
                    if (hasOldImg){
                        saveUser.setPortrait(oldUser.getPortrait());
                    }
                }
                boolean isOk=this.userService.update(saveUser);
                if (isOk){
                    // //在有新图片上传的情况下  旧图片的删除
                    if (hasOldImg && isUploadNewImg){
                        String oldPath=UPLOAD_PATH+oldUser.getPortrait();
                        File oldImg=new File(oldPath);
                        if (oldImg.exists()){
                            try {
                                oldImg.delete();
                            }catch (Exception ignored){

                            }
                        }
                    }
                    jsonMsg.setState(true);
                    jsonMsg.setMsg("修改成功");
                }else{
                    //修改失败时 删除新上传的图片
                    if (saveUser.getPortrait()!=null){
                        String newPath=UPLOAD_PATH+saveUser.getPortrait();
                        File newImg=new File(newPath);
                        if (newImg.exists()){
                            try {
                                newImg.delete();
                            }catch (Exception ignored){

                            }
                        }
                    }
                    jsonMsg.setMsg("修改失败");
                }
            }else{
                jsonMsg.setMsg("参数异常");
            }
        } catch (FileUploadBase.SizeLimitExceededException e) {
            e.printStackTrace();
            jsonMsg.setMsg("您上传文件超过了上传文件" + MAX_FILE_SIZE + "M 的限制");
        } catch (FileUploadException e) {
            e.printStackTrace();
            jsonMsg.setMsg("文件上传失败");
        } catch (RuntimeException e) {
            e.printStackTrace();
            //数据校验失败的异常信息
            jsonMsg.setMsg(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            jsonMsg.setMsg("表单提交失败");
        }

        returnJson(response,jsonMsg);
    }
    /**
     * 删除员工 by id
     */
    public void deleteById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonMsg jsonMsg=new JsonMsg();
        String strId=request.getParameter("id");
        if (Tools.isInteger(strId)){
            boolean isOk= this.userService.deleteById(Integer.parseInt(strId));
            if (isOk){
                jsonMsg.setState(true);
                jsonMsg.setMsg("删除成功");
            }else {
                jsonMsg.setMsg("删除失败");
            }
        }else {
            jsonMsg.setMsg("非法访问");
        }
        returnJson(response,jsonMsg);
    }
    /**
     * 重置密码
     */
    public void resetPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonMsg jsonMsg=new JsonMsg();
        String strId=request.getParameter("id");
        String password=request.getParameter("password");
        if (Tools.isInteger(strId)){
            if (Tools.isNotNull(password)){
                //查询未修改的数据
                SysUser oldUser=this.userService.selectById(Integer.parseInt(strId));
                Random random = new Random();
                //生成一个随机的8位数作为盐   10000000 ~ 99999999
                String salt = String.valueOf(random.nextInt(90000000) + 10000000);
                //对输入的密码+盐 取MD5值
                String userPassword = MD5Util.getMD5(password + salt);
                oldUser.setUserPassword(userPassword);
                oldUser.setSalt(salt);
                //调用修改方法
                boolean isOk= this.userService.update(oldUser);
                if (isOk){
                    jsonMsg.setState(true);
                    jsonMsg.setMsg("重置成功");
                }else {
                    jsonMsg.setMsg("重置失败");
                }
            }else {
                jsonMsg.setMsg("请输入新密码");
            }
        }else {
            jsonMsg.setMsg("非法访问");
        }
        returnJson(response,jsonMsg);

    }

    //导出

    /**
     * 导出为 xlsx(新版本)
     */
    public void exportXlsx(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //departmentId,userName,realName ,mobile ,userStatus ,startEndDate
        String strDepartmentId=request.getParameter("departmentId");
        String userName=request.getParameter("userName");
        String realName=request.getParameter("realName");
        String mobile=request.getParameter("mobile");
        String strUserStatus=request.getParameter("userStatus");
        String startEndDate=request.getParameter("startEndDate");
        //部门id
        Integer departmentId=null;
        if (Tools.isNotNull(strDepartmentId)){
            departmentId=Integer.parseInt(strDepartmentId);
        }
        //状态
        Integer userStatus=null;
        if (Tools.isNotNull(strUserStatus)){
            userStatus=Integer.parseInt(strUserStatus);
        }
        //开始时间和结束时间   2021-04-22 - 2021-05-22
        String startDate=null;
        String endDate=null;
        if (Tools.isNotNull(startEndDate)){
            String[] strDates=startEndDate.split(" - ");
            if (strDates.length==2){
                if (strDates[0].matches("^\\d{4}-\\d{2}-\\d{2}$")){
                    startDate=strDates[0];
                }
                if (strDates[1].matches("^\\d{4}-\\d{2}-\\d{2}$")){
                    endDate=strDates[1];
                }
            }
        }
        //查询出要导导出的数据
        List<UserVo> exportList=this.userService.selectForExport(departmentId,userName,realName,mobile,userStatus,startDate,endDate);

        //生日时间格式化
        SimpleDateFormat birthdayFormat=new SimpleDateFormat("yyyy-MM-dd");
        //导出时间的格式化
        SimpleDateFormat createFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //导出时间
        Date exportDate=new Date();
        //使用POI导出到excel
        //没有选择用XSSF，而是选择的SXSSF，这样能减少内存消耗，降低了内存溢出的风险。
        //xlsx方式
        //1-创建工作簿 org.apache.poi.xssf.streaming.SXSSFWorkbook
        SXSSFWorkbook workbook=new SXSSFWorkbook(100);//保存在内存中，知道刷新的行数
        //2-创建工作表对象 org.apache.poi.xssf.streaming.SXSSFSheet
        SXSSFSheet sheet=workbook.createSheet("员工信息");
        //3-写入excel的标题
        //创建第一行为标题行 org.apache.poi.xssf.streaming.SXSSFRow
        SXSSFRow titleRow=sheet.createRow(0);
        // 设置行高使用HSSFRow对象的setHeight和setHeightInPoints方法，
        // 这两个方法的区别在于setHeightInPoints的单位是点，而setHeight的单位是1/20个点
        titleRow.setHeightInPoints(30.0f);
        //合并列
        //创建单元格 org.apache.poi.xssf.streaming.SXSSFCell
        SXSSFCell cell=titleRow.createCell(0);//序号从0开始
        //指定单元格合并范围 org.apache.poi.ss.util.CellRangeAddress
        CellRangeAddress region=new CellRangeAddress(0,0,0,14);
        sheet.addMergedRegion(region);//合并单元格
        cell.setCellValue("员工数据("+createFormat.format(exportDate)+")");
        //单元格样式
        //org.apache.poi.ss.usermodel.CellStyle
        CellStyle titleStyle=workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);////垂直居中
        Font titleFont=workbook.createFont();//org.apache.poi.ss.usermodel.Font
        titleFont.setFontHeightInPoints((short) 24);//字体大小
        titleStyle.setFont(titleFont);//字体
        titleStyle.setBorderLeft(BorderStyle.THIN);//边框 左 org.apache.poi.ss.usermodel.BorderStyle
        titleStyle.setBorderTop(BorderStyle.THIN);//边框 上
        titleStyle.setBorderRight(BorderStyle.THIN);//边框 右
        titleStyle.setBorderBottom(BorderStyle.THIN);//边框 下
        //把样式设置给单元格
        titleRow.getCell(0).setCellStyle(titleStyle);

        //4-表头
        SXSSFRow headerNameRow=sheet.createRow(1);
        headerNameRow.createCell(0).setCellValue("序号");
        headerNameRow.createCell(1).setCellValue("登录名");
        headerNameRow.createCell(2).setCellValue("姓名");
        headerNameRow.createCell(3).setCellValue("部门");
        headerNameRow.createCell(4).setCellValue("职位");
        headerNameRow.createCell(5).setCellValue("角色");
        headerNameRow.createCell(6).setCellValue("性别");
        headerNameRow.createCell(7).setCellValue("出生日期");
        headerNameRow.createCell(8).setCellValue("Email");
        headerNameRow.createCell(9).setCellValue("手机号");
        headerNameRow.createCell(10).setCellValue("QQ");
        headerNameRow.createCell(11).setCellValue("微信");
        headerNameRow.createCell(12).setCellValue("状态");
        headerNameRow.createCell(13).setCellValue("创建时间");
        headerNameRow.createCell(14).setCellValue("备注");
        //样式
        CellStyle headerStyle= workbook.createCellStyle();
        headerStyle.setBorderLeft(BorderStyle.THIN);//边框 左
        headerStyle.setBorderTop(BorderStyle.THIN);//边框 上
        headerStyle.setBorderRight(BorderStyle.THIN);//边框 右
        headerStyle.setBorderBottom(BorderStyle.THIN);//边框 下
        //背景颜色  org.apache.poi.ss.usermodel.IndexedColors
        headerStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        //org.apache.poi.ss.usermodel.FillPatternType
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font headerFont=workbook.createFont();
        headerFont.setBold(true);//字体加粗
        headerStyle.setFont(headerFont);
        //通过循环给表头设置样式 getPhysicalNumberOfCells()获取单元格个数
        for (int i=0;i<headerNameRow.getPhysicalNumberOfCells();i++){
            headerNameRow.getCell(i).setCellStyle(headerStyle);
        }
        //5-写数据
        if(exportList!=null && exportList.size()>0){
            //数据部分 样式
            CellStyle dateStyle=workbook.createCellStyle();
            dateStyle.setBorderLeft(BorderStyle.THIN);//边框 左
            dateStyle.setBorderTop(BorderStyle.THIN);//边框 上
            dateStyle.setBorderRight(BorderStyle.THIN);//边框 右
            dateStyle.setBorderBottom(BorderStyle.THIN);//边框 下
            dateStyle.setAlignment(HorizontalAlignment.LEFT);//左对齐
            for(int i = 0; i < exportList.size(); i++){
                UserVo userVo=exportList.get(i);
                //第一行标题，第二行是表头
                Row rowNow=sheet.createRow(2+i);
                //设置值
                rowNow.createCell(0).setCellValue(i+1);//序号
                rowNow.createCell(1).setCellValue(userVo.getUserName());//登录名
                rowNow.createCell(2).setCellValue(userVo.getRealName());//姓名
                rowNow.createCell(3).setCellValue(userVo.getDepartmentName());//部门
                rowNow.createCell(4).setCellValue(userVo.getPositionName());//职位
                rowNow.createCell(5).setCellValue(userVo.getRoleName());//角色
                String strGender="未知";
                switch (userVo.getGender()){
                    case 1:
                        strGender="男";
                        break;
                    case 2:
                        strGender="女";
                        break;
                }
                rowNow.createCell(6).setCellValue(strGender);//性别
                if (userVo.getBirthday()!=null){
                    rowNow.createCell(7).setCellValue(birthdayFormat.format(userVo.getBirthday()));//出生日期
                }
                rowNow.createCell(8).setCellValue(userVo.getEmail());//Email
                rowNow.createCell(9).setCellValue(userVo.getMobile());//手机号
                rowNow.createCell(10).setCellValue(userVo.getQq());//QQ
                rowNow.createCell(11).setCellValue(userVo.getWechat());//微信
                rowNow.createCell(12).setCellValue(userVo.getUserStatus()==0?"禁用":"启用");//状态
                if (userVo.getGmtCreate()!=null){
                    rowNow.createCell(13).setCellValue(createFormat.format(userVo.getGmtCreate()));//创建时间
                }
                rowNow.createCell(14).setCellValue(userVo.getRemark());//备注
                //设置样式
                for (int j = 0; j < rowNow.getPhysicalNumberOfCells(); j++){
                    rowNow.getCell(j).setCellStyle(dateStyle);
                }
            }
            //设置自动列宽 如果使了SXSSF，需要把所有数据加载到内存，数据比较多时容易内存条溢出，注意！！！！！
            sheet.trackAllColumnsForAutoSizing();//缓存所有数据 for 自动宽度
            for (int i = 0; i < headerNameRow.getPhysicalNumberOfCells(); i++) {
                sheet.autoSizeColumn(i);//设置自动列宽
                sheet.setColumnWidth(i,sheet.getColumnWidth(i)*17/10);
            }
            //6-输出
            SimpleDateFormat fileFormat=new SimpleDateFormat("yyyyMMddHHmmss");
            String lastFileName="员工数据"+fileFormat.format(new Date())+".xlsx";//文件名
            response.setContentType("application/msexcel;charset=UTF-8");//指定xlsx的MIME类型
            response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode(lastFileName, "UTF-8"));//指定文件下载名称
            //获取输出流
            OutputStream out = response.getOutputStream();
            workbook.write(out);//把工作簿写到输出流
            out.flush();
            out.close();
        }


    }
    /**
     * 老版本的导出
     */
    public void exportXls(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //departmentId,userName,realName ,mobile ,userStatus ,startEndDate
        String strDepartmentId=request.getParameter("departmentId");
        String userName=request.getParameter("userName");
        String realName=request.getParameter("realName");
        String mobile=request.getParameter("mobile");
        String strUserStatus=request.getParameter("userStatus");
        String startEndDate=request.getParameter("startEndDate");
        //部门id
        Integer departmentId=null;
        if (Tools.isNotNull(strDepartmentId)){
            departmentId=Integer.parseInt(strDepartmentId);
        }
        //状态
        Integer userStatus=null;
        if (Tools.isNotNull(strUserStatus)){
            userStatus=Integer.parseInt(strUserStatus);
        }
        //开始时间和结束时间   2021-04-22 - 2021-05-22
        String startDate=null;
        String endDate=null;
        if (Tools.isNotNull(startEndDate)){
            String[] strDates=startEndDate.split(" - ");
            if (strDates.length==2){
                if (strDates[0].matches("^\\d{4}-\\d{2}-\\d{2}$")){
                    startDate=strDates[0];
                }
                if (strDates[1].matches("^\\d{4}-\\d{2}-\\d{2}$")){
                    endDate=strDates[1];
                }
            }
        }
        List<UserVo> exportList=this.userService.selectForExport(departmentId,userName,realName,mobile,userStatus,startDate,endDate);


        SimpleDateFormat birthdayFormat=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat createFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date exportDate=new Date();
        //使用POI导出到excel
        //xls方式 org.apache.poi.hssf.usermodel.HSSF
        //1-创建工作簿
        HSSFWorkbook workbook=new HSSFWorkbook();//保存在内存中，直到刷新的行数。
        //2-创建工作表对象 SXSSFSheet
        HSSFSheet sheet=workbook.createSheet("员工信息");
        //3-写入excel的标题
        //创建第一行为标题行
        HSSFRow titleRow = sheet.createRow(0);
        // 设置行高使用HSSFRow对象的setHeight和setHeightInPoints方法，这两个方法的区别在于setHeightInPoints的单位是点，而setHeight的单位是1/20个点
        titleRow.setHeightInPoints(30.0f);
        //合并列
        HSSFCell cell=titleRow.createCell(0);
        CellRangeAddress region=new CellRangeAddress(0, 0, 0, 14);
        sheet.addMergedRegion(region);
        cell.setCellValue("员工数据  ("+createFormat.format(exportDate)+")");
        //单元格样式
        CellStyle titleStyle= workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        Font titleFont= workbook.createFont();
        titleFont.setFontHeightInPoints((short) 24);//字体大小
        titleStyle.setFont(titleFont);//字体
        titleStyle.setBorderLeft(BorderStyle.THIN);//边框 左
        titleStyle.setBorderTop(BorderStyle.THIN);//边框 上
        titleStyle.setBorderRight(BorderStyle.THIN);//边框 右
        titleStyle.setBorderBottom(BorderStyle.THIN);//边框 下
        titleRow.getCell(0).setCellStyle(titleStyle);


        //4-表头
        HSSFRow headerNameRow = sheet.createRow(1);
        headerNameRow.createCell(0).setCellValue("序号");
        headerNameRow.createCell(1).setCellValue("登录名");
        headerNameRow.createCell(2).setCellValue("姓名");
        headerNameRow.createCell(3).setCellValue("部门");
        headerNameRow.createCell(4).setCellValue("职位");
        headerNameRow.createCell(5).setCellValue("角色");
        headerNameRow.createCell(6).setCellValue("性别");
        headerNameRow.createCell(7).setCellValue("出生日期");
        headerNameRow.createCell(8).setCellValue("Email");
        headerNameRow.createCell(9).setCellValue("手机号");
        headerNameRow.createCell(10).setCellValue("QQ");
        headerNameRow.createCell(11).setCellValue("微信");
        headerNameRow.createCell(12).setCellValue("状态");
        headerNameRow.createCell(13).setCellValue("创建时间");
        headerNameRow.createCell(14).setCellValue("备注");
        //样式
        CellStyle headerStyle= workbook.createCellStyle();
        headerStyle.setBorderLeft(BorderStyle.THIN);//边框 左
        headerStyle.setBorderTop(BorderStyle.THIN);//边框 上
        headerStyle.setBorderRight(BorderStyle.THIN);//边框 右
        headerStyle.setBorderBottom(BorderStyle.THIN);//边框 下
        headerStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());//背景颜色
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont= workbook.createFont();
        headerFont.setBold(true);//加粗
        headerStyle.setFont(headerFont);
        for (int i=0;i<headerNameRow.getPhysicalNumberOfCells();i++){
            headerNameRow.getCell(i).setCellStyle(headerStyle);
        }

        //5-写数据
        if (exportList!=null && exportList.size()>0){
            //数据部分 样式
            CellStyle dateStyle=workbook.createCellStyle();
            dateStyle.setBorderLeft(BorderStyle.THIN);//边框 左
            dateStyle.setBorderTop(BorderStyle.THIN);//边框 上
            dateStyle.setBorderRight(BorderStyle.THIN);//边框 右
            dateStyle.setBorderBottom(BorderStyle.THIN);//边框 下
            dateStyle.setAlignment(HorizontalAlignment.LEFT);//左对齐

            for (int i = 0; i < exportList.size(); i++) {
                UserVo userVo=exportList.get(i);
                //第一行标题，第二行是表头
                Row rowNow=sheet.createRow(2+i);
                //设置值
                rowNow.createCell(0).setCellValue(i+1);//序号
                rowNow.createCell(1).setCellValue(userVo.getUserName());//登录名
                rowNow.createCell(2).setCellValue(userVo.getRealName());//姓名
                rowNow.createCell(3).setCellValue(userVo.getDepartmentName());//部门
                rowNow.createCell(4).setCellValue(userVo.getPositionName());//职位
                rowNow.createCell(5).setCellValue(userVo.getRoleName());//角色
                String strGender="未知";
                switch (userVo.getGender()){
                    case 1:
                        strGender="男";
                        break;
                    case 2:
                        strGender="女";
                        break;
                }
                rowNow.createCell(6).setCellValue(strGender);//性别
                if (userVo.getBirthday()!=null){
                    rowNow.createCell(7).setCellValue(birthdayFormat.format(userVo.getBirthday()));//出生日期
                }
                rowNow.createCell(8).setCellValue(userVo.getEmail());//Email
                rowNow.createCell(9).setCellValue(userVo.getMobile());//手机号
                rowNow.createCell(10).setCellValue(userVo.getQq());//QQ
                rowNow.createCell(11).setCellValue(userVo.getWechat());//微信
                rowNow.createCell(12).setCellValue(userVo.getUserStatus()==0?"禁用":"启用");//状态
                if (userVo.getGmtCreate()!=null){
                    rowNow.createCell(13).setCellValue(createFormat.format(userVo.getGmtCreate()));//创建时间
                }
                rowNow.createCell(14).setCellValue(userVo.getRemark());//备注

                //设置样式
                for (int j = 0; j < rowNow.getPhysicalNumberOfCells(); j++) {
                    rowNow.getCell(j).setCellStyle(dateStyle);
                }
            }

            //设置自动列宽
            for (int i = 0; i < headerNameRow.getPhysicalNumberOfCells(); i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i,sheet.getColumnWidth(i)*17/10);
            }

            //6-输出
            SimpleDateFormat fileFormat=new SimpleDateFormat("yyyyMMddHHmmss");
            String lastFileName = "员工数据"+ fileFormat.format(new Date()) + ".xls";//文件名
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");//指定xls的MIME类型
            response.setHeader("Content-Disposition", "attachment; filename="+URLEncoder.encode(lastFileName, "UTF-8"));//指定文件下载名称
            //获取输出流
            OutputStream out = response.getOutputStream();
            workbook.write(out);//把工作簿写到输出流
            out.flush();
            out.close();
        }
    }
}
