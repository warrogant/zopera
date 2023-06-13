package com.example.zopera.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.zopera.common.Result;
import com.example.zopera.common.ResultConstants;
import com.example.zopera.controller.dto.UserDto;
import com.example.zopera.controller.dto.UserPasswordDTO;
import com.example.zopera.entity.Favorite;
import com.example.zopera.entity.Opera;
import com.example.zopera.entity.User;
import com.example.zopera.mapper.FavoriteMapper;
import com.example.zopera.mapper.ReceptionMapper;
import com.example.zopera.service.FavoriteService;
import com.example.zopera.service.UserService;
import com.example.zopera.utils.TokenUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * user接口
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FavoriteService favoriteService;

    @Resource
    private ReceptionMapper receptionMapper;
    // 登录接口
    @PostMapping("/login")
    private Result login(@RequestBody UserDto userDto) {
        // 拿到前端发送的请求参数
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(ResultConstants.CODE_400, "参数错误");
        }
        UserDto dto = userService.login(userDto);
        return Result.success(dto);
    }

    // 注册接口
    @PostMapping("/register")
    private Result register(@RequestBody UserDto userDto) {
        // 拿到前端发送的请求参数
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(ResultConstants.CODE_400, "参数错误");
        }
        return Result.success(userService.register(userDto));
    }

    // 编辑个人信息
    @GetMapping("/nickname/{nickname}")
    public Result editPerson(@PathVariable String nickname) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nickname", nickname);
        return Result.success(userService.getOne(queryWrapper));
    }

    // 查询
    @GetMapping
    public Result findAllUser() {
        return Result.success(userService.list());
    }

    // 新增和修改
    @PostMapping("/save")
    private Result saveUser(@RequestBody User user) {
        return Result.success(userService.saveOrUpdate(user));
    }

    // 修改密码
    @PostMapping("/modifyPassword")
    private Result modifyPassword(@RequestBody UserPasswordDTO userPasswordDTO) {
        userService.updatePassword(userPasswordDTO);
        return Result.success("修改成功！");
    }

    // 删除
    @DeleteMapping("/del/{id}")
    public Result deleteUser(@PathVariable("id") Integer id) {
        return Result.success(userService.removeById(id));
    }

    // 批量删除
    @DeleteMapping("/del/batch")
    public Result batchDeleteUser(@RequestBody List<Integer> ids) {

        return Result.success(userService.removeByIds(ids));
    }

    // 分页查询
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
        @RequestParam(defaultValue = "") String username, @RequestParam(defaultValue = "") String email,
        @RequestParam(defaultValue = "") String phone) {
        IPage<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!"".equals(username)) {
            queryWrapper.like("username", username);
        }
        if (!"".equals(email)) {
            queryWrapper.like("email", email);
        }
        if (!"".equals(phone)) {
            queryWrapper.like("phone", phone);
        }
        // 当前用户信息
        User currentUser = TokenUtils.getCurrentUser();
        System.out.println("当前用户：" + currentUser.getNickname());

        return Result.success(userService.page(page, queryWrapper));
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<User> list = userService.list();
        // 通过工具类创建writer 写出到磁盘路径
        // ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 自定义标题别名
        writer.addHeaderAlias("id", "ID");
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("birth", "出生日期");
        writer.addHeaderAlias("location", "地址");
        writer.addHeaderAlias("createTime", "创建时间");
        writer.addHeaderAlias("avator", "头像");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    /**
     * excel 导入
     *
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<User> list = reader.readAll(User.class);
        userService.saveBatch(list);
        return Result.success(true);
    }

    /**
     * 收藏信息的查询
     *
     * @param userId,operaId
     * @return
     */
    @GetMapping("/favorite")
    public Result getFavorite(@RequestParam("userId") Integer userId, @RequestParam("operaId") Integer operaId) {
        return Result.success(favoriteService.selectFavorite(userId, operaId));
    }

//    查询已收藏的戏曲
    @GetMapping("/selectByUserId/{userId}")
    public Result selectByUserId(@PathVariable Integer userId) {
        List<Favorite> favorites = favoriteService.selectByUserId(userId);
        // 如果收藏列表为空，则表示该用户未收藏戏曲
        return Result.success(favorites);
    }

    /**
     * 收藏信息的增加
     *
     * @param userId,operaId
     * @return
     */
    @PostMapping("/favorite/add")
    public boolean favoriteOpera(@RequestParam("userId") Integer userId,
                                 @RequestParam("operaId") Integer operaId) {
        return favoriteService.addFavorite(userId, operaId);
    }

    /**
     * 收藏信息的删除
     *
     * @param userId,operaId
     * @return
     */
    @DeleteMapping("/favorite/del")
    public boolean deleteFavorite(@RequestParam("userId") Integer userId,
                                  @RequestParam("operaId") Integer operaId) {
        return favoriteService.deleteFavorite(userId, operaId);
    }

}
