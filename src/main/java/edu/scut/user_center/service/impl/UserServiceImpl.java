package edu.scut.user_center.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.scut.user_center.model.entity.User;
import edu.scut.user_center.service.UserService;
import edu.scut.user_center.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static edu.scut.user_center.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author DS
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-03-20 16:31:39
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    // 盐值，混淆密码
    private static final String SALT = "YSQ";

    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param confirmPassword 确认密码
     * @return 用户ID
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String confirmPassword) {
        // 非空校验
        if (CharSequenceUtil.hasBlank(userAccount, userPassword, confirmPassword)) {
            return -1;
        }

        // 长度校验
        if (userAccount.length() < 4) {
            return -1;
        }
        if (userPassword.length() < 8 || confirmPassword.length() < 8) {
            return -1;
        }

        // 账号不能包含特殊字符
        boolean isMatch = ReUtil.isMatch(".*(\\pP|\\pS|\\s+).*", userAccount);
        if (isMatch) {
            return -1;
        }

        // 密码和确认密码要一致
        if (!userPassword.equals(confirmPassword)) {
            return -1;
        }

        // 账号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        long count = this.count(queryWrapper);
        if (count > 0) {
            return -1;
        }

        // 密码加密
        String securePassword = SecureUtil.md5(SALT + userPassword);

        // 将数据插入数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(securePassword);
        boolean saved = this.save(user);
        if (!saved) {
            return -1;
        }

        return user.getId();
    }


    /**
     *
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param request HTTP请求
     * @return 脱敏后的用户信息
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 非空校验
        if (CharSequenceUtil.hasBlank(userAccount, userPassword)) {
            return null;
        }

        // 长度校验
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 8) {
            return null;
        }

        // 账号不能包含特殊字符
        boolean isMatch = ReUtil.isMatch(".*(\\pP|\\pS|\\s+).*", userAccount);
        if (isMatch) {
            return null;
        }

        // 校验密码是否输入正确
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        // 密码加密
        String securePassword = SecureUtil.md5(SALT + userPassword);
        queryWrapper.eq("user_password", securePassword);
        User user = this.getOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user Login failed, userAccount cannot match userPassword");
            return null;
        }

        User safetyUser = getSafetyUser(user);

        // 记录用户状态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }

    /**
     * 用户信息脱敏
     * @param originUser 原始用户信息
     * @return 脱敏后的用户信息
     */
    @Override
    public User getSafetyUser(User originUser) {
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;
    }

    /**
     * 查询用户
     * @param username 用户名
     * @return 用户列表
     */
    @Override
    public List<User> searchUsers(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!CharSequenceUtil.hasBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = this.list(queryWrapper);
        return userList.stream().map(this::getSafetyUser).toList();
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteUser(Long id) {
        if (id <= 0) {
            return false;
        }
        return this.removeById(id);
    }
}



