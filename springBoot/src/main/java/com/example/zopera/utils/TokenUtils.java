package com.example.zopera.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.zopera.entity.User;
import com.example.zopera.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Token配置
 */
@Component
public class TokenUtils {

    private static UserService staticUserService;
    @Resource
    private UserService userService;

    @PostConstruct
    public void setUserService() {
        staticUserService = userService;
    }

    /**
     * 创建TOKEN
     *
     * @param userId
     * @return
     */
    public static String createToken(String userId, String sign) {
        return JWT.create().withAudience(userId)    //以userId为载荷保存到token中做为数据
                .withExpiresAt(DateUtil.offsetHour(new Date(), +8))     ///8小时过期
                .sign(Algorithm.HMAC256(sign));     //以 password 作为 token 的密钥
    }

    /*
    获取当前的用户信息
     */
    public static User getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StrUtil.isNotBlank(token)) {
                String userId = JWT.decode(token).getAudience().get(0);
                return staticUserService.getById(Integer.valueOf(userId));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
