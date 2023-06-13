package com.example.zopera.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.zopera.common.Result;
import com.example.zopera.common.ResultConstants;
import com.example.zopera.entity.Activity;
import com.example.zopera.entity.Avator;
import com.example.zopera.entity.Opera;
import com.example.zopera.entity.Operavideo;
import com.example.zopera.mapper.AvatorMapper;
import com.example.zopera.mapper.FileMapper;
import com.example.zopera.mapper.OperavideoMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {
    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private FileMapper fileMapper;
    @Resource
    private OperavideoMapper operavideoMapper;
    @Resource
    private AvatorMapper avatorMapper;

    @PostMapping("/upload")
    public Result operaupload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename(); // 源文件名称
        String type = FileUtil.extName(originalFilename); // 文件类型
        long size = file.getSize();// 文件大小

        // 定义文件唯一标识
        String uuid = IdUtil.fastSimpleUUID();

        // 文件名.文件类型 + "files/"
        String fileUuid = uuid + StrUtil.DOT + type;
        File uploadFile = new File(fileUploadPath + fileUuid);
        // 上传文件类型是否为MP4
        if (type.equals("mp4")) {
            String url;
            // 存储磁盘
            File uploadParentFile = uploadFile.getParentFile();
            // 判断目录是否存在，创建目录
            if (!uploadParentFile.exists()) {
                uploadParentFile.mkdirs();
            }
            // 获取文件的md5
            String md5 = SecureUtil.md5(file.getInputStream());
            // 从数据库中查询是否存在相同的记录
            Operavideo dbfiles = getFileByMd5(md5);
            if (dbfiles != null) {
                url = dbfiles.getUrl();
            } else {
                // 获取到的文件存储到本地磁盘中
                file.transferTo(uploadFile);
                url = "http://localhost:9090/file/" + fileUuid;
            }
            int KB = 1024;// 定义KB的计算常量
            // 存储数据库
            Operavideo saveFile = new Operavideo();
            // originalFilename
            saveFile.setName(originalFilename.substring(0, originalFilename.lastIndexOf(".")));
            saveFile.setType(type);
            saveFile.setSize(size / KB);
            saveFile.setUrl(url);
            saveFile.setMd5(md5);
            operavideoMapper.insert(saveFile);
            return Result.success(url);
        } else {
            return Result.error(ResultConstants.CODE_666, "文件格式不正确");
        }
    }

    /**
     * 图片上传接口
     */
    @PostMapping("/avatorupload")
    public Result avatorupload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename(); // 源文件名称
        String type = FileUtil.extName(originalFilename); // 文件类型
        long size = file.getSize();// 文件大小
        String filePath = "E:\\GraduationDesign\\avator\\";

        // 定义文件唯一标识
        String uuid = IdUtil.fastSimpleUUID();

        // 文件名.文件类型 + "avator/"
        String fileUuid = uuid + StrUtil.DOT + type;
        File uploadFile = new File(filePath + fileUuid);
        // 上传文件类型是否为png或者jpg
        if (type.equals("png") || type.equals("jpg") || type.equals("jpeg")) {
            String url;
            // 存储磁盘
            File uploadParentFile = uploadFile.getParentFile();
            // 判断目录是否存在，创建目录
            if (!uploadParentFile.exists()) {
                uploadParentFile.mkdirs();
            }
            // 获取到的文件存储到本地磁盘中
            file.transferTo(uploadFile);
            // "http://localhost:9090/file/"
            url = "http://localhost:9090/file/" + fileUuid;
            int KB = 1024;// 定义KB的计算常量
            // 存储数据库
            Avator saveFile = new Avator();
            saveFile.setName(originalFilename.substring(0, originalFilename.lastIndexOf(".")));
            saveFile.setType(type);
            saveFile.setSize(size / KB);
            saveFile.setAvatorurl(url);
            avatorMapper.insert(saveFile);
            return Result.success(url);
        } else {
            return Result.error(ResultConstants.CODE_666, "文件格式不正确");
        }
    }

    /**
     * 文件下载 http://localhost:9090/file/{fileuuid}
     */
    @GetMapping("/{fileUuid}")
    public void fileDownload(@PathVariable String fileUuid, HttpServletResponse response) throws IOException {
//        String filePath = "E:\\GraduationDesign\\avator\\";
        String filePath = "E:\\GraduationDesign\\avator\\";
        String type = FileUtil.extName(fileUuid); // 获取文件扩展名
        File uploadFile;
        ServletOutputStream os = response.getOutputStream();
        if (type.equals("mp4")) {
            // 根据文件的唯一标识码识别文件
            uploadFile = new File(fileUploadPath + fileUuid);
            // 读取文件的字节流
            os.write(FileUtil.readBytes(uploadFile));
        } else {
            // 根据文件的唯一标识码识别文件
            uploadFile = new File(filePath + fileUuid);
            // 读取文件的字节流
            os.write(FileUtil.readBytes(uploadFile));
        }
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUuid, "UTF-8"));
        response.setContentType("application/octet-stream");

        // File file = new File(String.valueOf(uploadFile));

        // response.setHeader("Accept-Ranges", "bytes");
        // response.setContentLength(Math.toIntExact(file.length()));

        os.flush();
        os.close();
    }

    /**
     * 通过文件的md5查询文件
     *
     * @param md5
     * @return
     */
    private Operavideo getFileByMd5(String md5) {
        // 查询文件的md5是否存在
        QueryWrapper<Operavideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<Operavideo> filesList = operavideoMapper.selectList(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }

    /**
     * 添加文件
     * 
     * @param opera
     * @return
     */
    @PostMapping("/operaSave")
    public Result save(@RequestBody Opera opera) {
        return Result.success(fileMapper.insert(opera));
    }

    @PostMapping("/update")
    public Result update(@RequestBody Opera files) {
        return Result.success(fileMapper.updateById(files));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        Opera files = fileMapper.selectById(id);
        files.setIsDelete(true);
        fileMapper.updateById(files);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        // select * from sys_file where id in (id,id,id...)
        QueryWrapper<Opera> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        List<Opera> files = fileMapper.selectList(queryWrapper);
        for (Opera file : files) {
            file.setIsDelete(true);
            fileMapper.updateById(file);
        }
        return Result.success();
    }

    @GetMapping("/findOperaReviewPage")
    public Result findOperaReviewPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
        @RequestParam(defaultValue = "") String name, @RequestParam Integer state) {
        Page<Opera> objectPage = new Page<>(pageNum, pageSize);
        QueryWrapper<Opera> queryWrapper = new QueryWrapper<>();
        IPage<Opera> operaPage;
        switch (state) {
            case 0:
                queryWrapper.eq("state", 0);
                if (!"".equals(name)) {
                    queryWrapper.like("name", name);
                }
                // 查询未删除的记录
                queryWrapper.eq("is_delete", false);
                operaPage = fileMapper.selectPage(objectPage, queryWrapper);
                break;
            case 1:
                queryWrapper.eq("state", 1);
                // 查询未删除的记录
                queryWrapper.eq("is_delete", false);
                operaPage = fileMapper.selectPage(objectPage, queryWrapper);
                break;
            case 2:
                queryWrapper.eq("state", 2);
                // 查询未删除的记录
                queryWrapper.eq("is_delete", false);
                operaPage = fileMapper.selectPage(objectPage, queryWrapper);
                break;
            default:
                if (!"".equals(name)) {
                    queryWrapper.like("name", name);
                }
                // 查询未删除的记录
                queryWrapper.eq("is_delete", false);
                operaPage = fileMapper.selectPage(objectPage, queryWrapper);
                break;
        }
        return Result.success(operaPage);
    }

    /**
     * 更新戏曲审核状态
     *
     * @param id, state, stateInfo
     * @return
     */
    @PostMapping("/updateOperaByIdState")
    public Result updateOperaByIdState(@RequestParam Integer id, @RequestParam Integer state,
        @RequestParam(defaultValue = "") String stateInfo) {
        int updateState = fileMapper.updateOperaByIdState(id, state, stateInfo);
        return Result.success(updateState);
    }

    // /**
    // * 分页查询接口
    // *
    // * @param pageNum
    // * @param pageSize
    // * @param name
    // * @return
    // */
    // @GetMapping("/page")
    // public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
    // @RequestParam(defaultValue = "") String name) {
    // Page<Opera> objectPage = new Page<>(pageNum, pageSize);
    // QueryWrapper<Opera> queryWrapper = new QueryWrapper<>();
    // // queryWrapper.orderByDesc("id");
    // if (!"".equals(name)) {
    // queryWrapper.like("name", name);
    // }
    // // 查询未删除的记录
    // queryWrapper.eq("is_delete", false);
    // // queryWrapper.eq("state", 0);// 查询状态0记录
    // return Result.success(fileMapper.selectPage(objectPage, queryWrapper));
    // }
}
